package software.cm.crazytower.aplicacion;

import android.app.Application;
import android.content.Intent;

import software.cm.crazytower.servicios.ServicioBluetooth;
import software.cm.crazytower.servicios.ServicioMonitoreoConexiones;

public class AplicacionEncarga extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        this.startService(new Intent(this, ServicioMonitoreoConexiones.class));
        this.startService(new Intent(this, ServicioBluetooth.class));
    }
}
