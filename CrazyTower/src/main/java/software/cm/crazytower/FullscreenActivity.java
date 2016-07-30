package software.cm.crazytower;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Settings;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import software.cm.crazytower.componentes.AdaptardorArreglo;
import software.cm.crazytower.componentes.ListaNodosReceptor;
import software.cm.crazytower.componentes.Receptor;
import software.cm.crazytower.helpers.APManager;
import software.cm.crazytower.modelo.Nodo;
import software.cm.crazytower.servicios.ServicioMonitoreoConexiones;

@SuppressLint("ParcelCreator")
public class FullscreenActivity extends AppCompatActivity implements Receptor {
    private boolean debeIniciarAnclajeRed = false;

    // Servicio
    private ServicioMonitoreoConexiones servicio;
    private boolean servicioBound = false;
    private ServiceConnection servicioConexion;

    // Receptor
    private ListaNodosReceptor listaNodosReceptor;

    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    private View mListView;
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            mListView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };

    private View mControlsView;
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
            mControlsView.setVisibility(View.VISIBLE);
        }
    };
    private boolean mVisible;
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };
    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };

    @Override
    protected void onStop() {
        super.onStop();
        // Unbind from the service
        if (this.servicioBound) {
            unbindService(this.servicioConexion);
            this.servicioBound = false;
        }
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fullscreen);

        mVisible = true;
        mControlsView = findViewById(R.id.fullscreen_content_controls);
        mListView = findViewById(R.id.listView);

        // Se crea el receptor de lista de nodos
        this.listaNodosReceptor = new ListaNodosReceptor(new Handler());
        this.listaNodosReceptor.setReceptor(this);

        // Set up the user interaction to manually show or hide the system UI.
        /*mListView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //toggle();
            }
        });*/

        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
        findViewById(R.id.btnWifi).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (Settings.System.canWrite(FullscreenActivity.this)) {
                            APManager.setWifiApState(FullscreenActivity.this, true);
                            Log.i("AP Status", "" + APManager.getWifiApConfiguration().status);
                        } else {
                            FullscreenActivity.this.debeIniciarAnclajeRed = true;
                            Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
                            intent.setData(Uri.parse("package:" + FullscreenActivity.this.getPackageName()));
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        findViewById(R.id.btnContadorDispositivos).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Button button = (Button) findViewById(R.id.btnContadorDispositivos);
                final String texto = button.getText().toString();

                button.setEnabled(false);
                button.setText("Calculando...");
                final List<Nodo> nodosConectados = new ArrayList<>();

                try {
                    new AsyncTask<String, String, String>(){
                        @Override
                        protected String doInBackground(String... params) {
                            try {
                                nodosConectados.addAll(APManager.leerTablaARP());

                                if (nodosConectados.isEmpty()) {
                                    nodosConectados.add(new Nodo("No hay ningún dispositivo conectado", ""));
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            return null;
                        }

                        @Override
                        protected void onPostExecute(String s) {
                            super.onPostExecute(s);

                            ListView listView = (ListView) findViewById(R.id.listView);

                            AdaptardorArreglo adapter = new AdaptardorArreglo(FullscreenActivity.this, R.layout.fila, nodosConectados);
                            listView.setAdapter(adapter);

                            button.setEnabled(true);
                            button.setText(texto);
                        }
                    }.execute();
                } catch (Exception e) {
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (this.servicioConexion == null) {
            this.servicioConexion = new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName className, IBinder service) {
                    ServicioMonitoreoConexiones.LocalBinder binder = (ServicioMonitoreoConexiones.LocalBinder) service;
                    FullscreenActivity.this.servicio = binder.obtenerServicio();
                    FullscreenActivity.this.servicioBound = true;

                    FullscreenActivity.this.servicio.iniciarProceso();
                }

                @Override
                public void onServiceDisconnected(ComponentName arg0) {
                    FullscreenActivity.this.servicioBound = false;
                }
            };
            // Bound del servicio
            Intent intent = new Intent(this, ServicioMonitoreoConexiones.class);
            intent.putExtra("receiver", this.listaNodosReceptor);

            bindService(intent, servicioConexion, Context.BIND_AUTO_CREATE);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (this.listaNodosReceptor != null) {
            this.listaNodosReceptor.setReceptor(null);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (this.debeIniciarAnclajeRed) {
            APManager.setWifiApState(FullscreenActivity.this, true);
            FullscreenActivity.this.debeIniciarAnclajeRed = false;
        }

        if (this.listaNodosReceptor != null) {
            this.listaNodosReceptor.setReceptor(this);
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }

    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        /*mControlsView.setVisibility(View.GONE);
        mVisible = false;*/

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    @SuppressLint("InlinedApi")
    private void show() {
        // Show the system bar
        mListView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        mVisible = true;

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }

    /**
     * Schedules a call to hide() in [delay] milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        ListView listView = (ListView) findViewById(R.id.listView);

        ArrayList<Nodo> nodosConectados = resultData.getParcelableArrayList("listaNodos");

        if (nodosConectados.isEmpty()) {
            nodosConectados.add(new Nodo("No hay dispositivos conectados", null));
        }

        AdaptardorArreglo adapter = new AdaptardorArreglo(FullscreenActivity.this, R.layout.fila, nodosConectados);
        listView.setAdapter(adapter);
    }
}
