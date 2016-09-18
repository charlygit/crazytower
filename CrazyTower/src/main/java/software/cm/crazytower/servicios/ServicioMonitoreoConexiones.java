package software.cm.crazytower.servicios;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.ResultReceiver;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import software.cm.crazytower.componentes.ListaNodosReceptor;
import software.cm.crazytower.componentes.Receptor;
import software.cm.crazytower.helpers.APManager;
import software.cm.crazytower.modelo.Nodo;

public class ServicioMonitoreoConexiones extends Service {
    private boolean enEjecucion = false;
    private static final long TIEMPO_CAMBIO_CONTRASENIA = 1000 * 60 * 1;
    private static final long INTERVALO_ENTRE_EJECUCION_TAREA = 5000L;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public ServicioMonitoreoConexiones() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(ServicioMonitoreoConexiones.class.getSimpleName(), "Se invoca a onStartCommand");

        if (!this.enEjecucion) {
            Log.i(ServicioMonitoreoConexiones.class.getSimpleName(), "Se crea la tarea para monitorear las conexiones");

            new Timer().schedule(
                    new TareaChequeoUsuarioConectadosWifi(),
                    TIEMPO_CAMBIO_CONTRASENIA
            );

            this.enEjecucion = true;
        }

        return super.onStartCommand(intent, flags, startId);
    }

    private class TareaChequeoUsuarioConectadosWifi extends TimerTask {
        private Date fechaUltimaModificacionContrasenia;

        public TareaChequeoUsuarioConectadosWifi() {
            this.fechaUltimaModificacionContrasenia = new Date();
        }

        public TareaChequeoUsuarioConectadosWifi(Date fechaUltimaModificacionContrasenia) {
            this.fechaUltimaModificacionContrasenia = fechaUltimaModificacionContrasenia;
        }

        @Override
        public void run() {
            Date fechaAhora = new Date();

            if (fechaAhora.getTime() - this.fechaUltimaModificacionContrasenia.getTime() >= TIEMPO_CAMBIO_CONTRASENIA) {
                Log.i(TareaChequeoUsuarioConectadosWifi.class.getSimpleName(), "La contraseña debe cambiar porque caducó");

                ArrayList<Nodo> listaNodosConectados = new ArrayList<>(APManager.leerTablaARP());

                if (listaNodosConectados.isEmpty()) {
                    Log.i(TareaChequeoUsuarioConectadosWifi.class.getSimpleName(), "Se recrea la conexión con nueva contraseña");

                    APManager.recrearContrasenia(ServicioMonitoreoConexiones.this);

                    new Timer().schedule(
                            new TareaChequeoUsuarioConectadosWifi(),
                            TIEMPO_CAMBIO_CONTRASENIA);
                } else {
                    Log.i(TareaChequeoUsuarioConectadosWifi.class.getSimpleName(), "Como hay usuarios conectados se espera para el cambio de contraseña");

                    new Timer().schedule(
                            new TareaChequeoUsuarioConectadosWifi(this.fechaUltimaModificacionContrasenia),
                            INTERVALO_ENTRE_EJECUCION_TAREA);
                }
            } else {
                Log.i(TareaChequeoUsuarioConectadosWifi.class.getSimpleName(), "Se mantiene la contraseña porque aún no caducó");

                new Timer().schedule(
                    new TareaChequeoUsuarioConectadosWifi(this.fechaUltimaModificacionContrasenia),
                    INTERVALO_ENTRE_EJECUCION_TAREA);
            }
        }
    }
}
