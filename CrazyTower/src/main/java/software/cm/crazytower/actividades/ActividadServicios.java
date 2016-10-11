package software.cm.crazytower.actividades;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.TextView;

import software.cm.crazytower.R;
import software.cm.crazytower.actividades.encuesta.ActividadEncuesta;
import software.cm.crazytower.arduino.ControladorArduino;
import software.cm.crazytower.helpers.APManager;

public class ActividadServicios extends AppCompatActivity {
    private static final Long TIEMPO_VUELTA_HOME = 1000L * 60L;
    private static final String ACCION_CAMBIO_ESTADO_ANCLAJE_RED = "android.net.wifi.WIFI_AP_STATE_CHANGED";
    private static final String ACCION_CAMBIO_ANCLAJE_RED = "android.net.conn.TETHER_STATE_CHANGED";

    private AnclajeRedReceiver anclajeRedReceiver = null;
    private Boolean anclajeRedReceiverRegistrado = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actividad_servicios);

        this.anclajeRedReceiver = new AnclajeRedReceiver();

        ControladorArduino.habilitarPuerto(this);

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                ActividadServicios.this.mostrarMensajeContraseña();
                new Handler().postDelayed(this, 1000);
            }
        }, 1000);


        // Vuelve a la pagina principal luego de un tiempo
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                Intent mainIntent = new Intent(ActividadServicios.this, CrazyTowerHome.class);
                ActividadServicios.this.startActivity(mainIntent);
                ActividadServicios.this.finish();
            }
        }, TIEMPO_VUELTA_HOME);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Intent mainIntent = new Intent(ActividadServicios.this, CrazyTowerHome.class);
        this.startActivity(mainIntent);
        this.finish();

        return super.onTouchEvent(event);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (!APManager.estaAnclajeRedActivo(this)) {
            APManager.setWifiApState(this);
        }

        this.mostrarMensajeContraseña();
    }

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
}
