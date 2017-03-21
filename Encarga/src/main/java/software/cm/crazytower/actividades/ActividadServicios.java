package software.cm.crazytower.actividades;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import software.cm.crazytower.R;
import software.cm.crazytower.arduino.ControladorArduino;
import software.cm.crazytower.helpers.APManager;
import software.cm.crazytower.servicios.ServicioBluetooth;

public class ActividadServicios extends ActividadBaseEncarga {
    private static final Long TIEMPO_VUELTA_HOME = 1000L * 60L;
    private static final String ACCION_CAMBIO_ESTADO_ANCLAJE_RED = "android.net.wifi.WIFI_AP_STATE_CHANGED";
    private static final String ACCION_CAMBIO_ANCLAJE_RED = "android.net.conn.TETHER_STATE_CHANGED";

    private AnclajeRedReceiver anclajeRedReceiver = null;
    private Boolean anclajeRedReceiverRegistrado = false;

    private ToggleButton boton0;
    private ToggleButton boton1;
    private ToggleButton boton2;
    private ToggleButton boton3;

    private Handler handler;
    private Runnable runnable;

    private ServicioBluetooth servicioBluetooth;
    private boolean bindingEstablecido = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actividad_servicios);

        this.anclajeRedReceiver = new AnclajeRedReceiver();

        boton0 = (ToggleButton) findViewById(R.id.usb0);
        boton1 = (ToggleButton) findViewById(R.id.usb1);
        boton2 = (ToggleButton) findViewById(R.id.usb2);
        boton3 = (ToggleButton) findViewById(R.id.usb3);

        List<ToggleButton> botonesCarga = Arrays.asList(boton0, boton1, boton2, boton3);

        for (ToggleButton boton : botonesCarga) {
            boton.setOnCheckedChangeListener(checkGeneralBotonListener);
            boton.setEnabled(false);
        }

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                ActividadServicios.this.mostrarMensajeContraseña();
                new Handler().postDelayed(this, 1000);
            }
        }, 1000);


        // Vuelve a la pagina principal luego de un tiempo
        this.handler = new Handler();

        this.runnable = new Runnable() {
            @Override
            public void run() {
                Intent mainIntent = new Intent(ActividadServicios.this, CrazyTowerHome.class);
                ActividadServicios.this.cambiarActividadAtenti(mainIntent);
            }
        };
        this.handler.postDelayed(runnable, TIEMPO_VUELTA_HOME);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Intent mainIntent = new Intent(ActividadServicios.this, CrazyTowerHome.class);
        this.cambiarActividadAtenti(mainIntent);

        return super.onTouchEvent(event);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (!APManager.estaAnclajeRedActivo(this)) {
            APManager.setWifiApState(this);
        }

        this.mostrarMensajeContraseña();

        Intent intent = new Intent(this.getApplicationContext(), ServicioBluetooth.class);
        boolean binding = bindService(intent, mConnection, BIND_IMPORTANT);

        if (!binding) {
            Toast.makeText(
                    ActividadServicios.this,
                    "Por el momento la función de carga de dispositivos se encuentra desactivada.",
                    Toast.LENGTH_LONG).show();
        }
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            ServicioBluetooth.LocalBinder binder = (ServicioBluetooth.LocalBinder) service;
            servicioBluetooth = binder.obtenerServicio();
            bindingEstablecido = true;

            if (!servicioBluetooth.hayConexionBluetoothEstablecida()) {
                List<ToggleButton> botonesCarga = Arrays.asList(boton0, boton1, boton2, boton3);

                for (ToggleButton boton : botonesCarga) {
                    boton.setEnabled(servicioBluetooth.hayConexionBluetoothEstablecida());
                }

                Toast.makeText(
                        ActividadServicios.this,
                        "Por el momento la función de carga de dispositivos se encuentra desactivada.",
                        Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            bindingEstablecido = false;
        }
    };

    private void mostrarMensajeContraseña() {
        TextView textoClaveWifi = (TextView) this.findViewById(R.id.idMensajeClaveWifi);
        textoClaveWifi.setText("Utiliza la wi-fi encarga con la clave " + APManager.obtenerContrasenia());
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!this.anclajeRedReceiverRegistrado) {
            IntentFilter filtro = new IntentFilter();
            filtro.addAction(ACCION_CAMBIO_ANCLAJE_RED);
            filtro.addAction(ACCION_CAMBIO_ESTADO_ANCLAJE_RED);

            registerReceiver(this.anclajeRedReceiver, filtro);
            this.anclajeRedReceiverRegistrado = true;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (this.anclajeRedReceiverRegistrado) {
            unregisterReceiver(this.anclajeRedReceiver);
            this.anclajeRedReceiverRegistrado = false;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (this.anclajeRedReceiverRegistrado) {
            unregisterReceiver(this.anclajeRedReceiver);
            this.anclajeRedReceiverRegistrado = false;
        }

        if (bindingEstablecido) {
            unbindService(mConnection);
            bindingEstablecido = false;
        }
    }

    class AnclajeRedReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String accion = intent.getAction();

            if (ACCION_CAMBIO_ANCLAJE_RED.equals(accion)) {
                ActividadServicios.this.mostrarMensajeContraseña();
                Log.i(AnclajeRedReceiver.class.getSimpleName(), ACCION_CAMBIO_ANCLAJE_RED);
            }

            if (ACCION_CAMBIO_ESTADO_ANCLAJE_RED.equals(accion)) {
                ActividadServicios.this.mostrarMensajeContraseña();
                Log.i(AnclajeRedReceiver.class.getSimpleName(), ACCION_CAMBIO_ESTADO_ANCLAJE_RED);
            }
        }
    }

    CompoundButton.OnCheckedChangeListener checkGeneralBotonListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                List<ToggleButton> botonesCarga = Arrays.asList(boton0, boton1, boton2, boton3);

                for (ToggleButton boton : botonesCarga) {
                    if (boton == buttonView) {
                        String datoAEnviar = this.obtenerDatoEnviadoPorBoton(boton);

                        if (datoAEnviar != null) {
                            //Toast.makeText(ActividadServicios.this, "Se envía el dato: " + datoAEnviar, Toast.LENGTH_SHORT).show();
                            ControladorArduino.habilitarPuerto(ActividadServicios.this, servicioBluetooth.obtenerSocketBluetooth(), datoAEnviar);

                            if (ActividadServicios.this.handler != null && ActividadServicios.this.runnable != null) {
                                ActividadServicios.this.handler.removeCallbacks(ActividadServicios.this.runnable);
                            }

                            new Handler().postDelayed(new Runnable(){
                                @Override
                                public void run() {
                                    Intent mainIntent = new Intent(ActividadServicios.this, CrazyTowerHome.class);
                                    ActividadServicios.this.cambiarActividadAtenti(mainIntent);
                                }
                            }, (TIEMPO_VUELTA_HOME / 120L));
                        }
                    } else {
                        boton.setOnCheckedChangeListener(null);
                        boton.setChecked(false);
                        boton.setOnCheckedChangeListener(checkGeneralBotonListener);
                    }
                }
            } else {
                boolean algunBotonMarcado = this.hayBotonMarcado();

                if (!algunBotonMarcado) {
                    buttonView.setChecked(true);
                }
            }
        }

        private boolean hayBotonMarcado() {
            List<ToggleButton> botonesCarga = Arrays.asList(boton0, boton1, boton2, boton3);
            Iterator<ToggleButton> itBotones = botonesCarga.iterator();
            boolean algunoMarcado = false;

            while (itBotones.hasNext() && !algunoMarcado) {
                algunoMarcado = itBotones.next().isChecked();
            }

            return (algunoMarcado);
        }

        private String obtenerDatoEnviadoPorBoton(ToggleButton toggleButton) {
            if (toggleButton == boton0) {
                return "0";
            } else if (toggleButton == boton1) {
                return "1";
            } else if (toggleButton == boton2) {
                return "2";
            } else if (toggleButton == boton3) {
                return "3";
            } else {
                Toast.makeText(ActividadServicios.this, "El botón no se registra como válido", Toast.LENGTH_LONG).show();
            }

            return null;
        }
    };

    private void msg(String s) {
        Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
    }
}
