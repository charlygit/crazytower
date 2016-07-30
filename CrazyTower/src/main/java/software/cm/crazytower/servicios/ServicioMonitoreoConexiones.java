package software.cm.crazytower.servicios;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.ResultReceiver;

import java.util.ArrayList;
import java.util.List;

import software.cm.crazytower.componentes.ListaNodosReceptor;
import software.cm.crazytower.componentes.Receptor;
import software.cm.crazytower.helpers.APManager;
import software.cm.crazytower.modelo.Nodo;

public class ServicioMonitoreoConexiones extends Service {
    private final IBinder binder = new LocalBinder();
    private ResultReceiver listaNodosReceptor;

    public class LocalBinder extends Binder {
        public ServicioMonitoreoConexiones obtenerServicio() {
            // Return this instance of LocalService so clients can call public methods
            return ServicioMonitoreoConexiones.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        this.listaNodosReceptor = intent.getParcelableExtra("receiver");
        return binder;
    }

    public ServicioMonitoreoConexiones() {
    }

    public void iniciarProceso() {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {
                        ArrayList<Nodo> listaNodosConectados = new ArrayList<>(APManager.leerTablaARP());

                        Bundle datosBundle = new Bundle();
                        datosBundle.putParcelableArrayList("listaNodos", listaNodosConectados);

                        ServicioMonitoreoConexiones.this.listaNodosReceptor.send(0, datosBundle);

                        Thread.sleep(5000);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
