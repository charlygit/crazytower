package software.cm.crazytower.actividades;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

import software.cm.crazytower.R;
import software.cm.crazytower.componentes.receivers.AdminReceiver;
import software.cm.crazytower.errores.ExcepcionGeneral;
import software.cm.crazytower.helpers.APManager;

public class ActividadEsperaDeviceOwner extends AppCompatActivity {
    private static final long TIEMPO_ENTRE_CONSULTAS = 1000 * 10; // 10 Seg.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actividad_espera_device_owner);

        if (APManager.esAplicacionDeviceOwner(this, AdminReceiver.class.getPackage().getName())) {
            this.redirigirActividadSplash();
        } else {
            new Timer().schedule(
                    new ActividadEsperaDeviceOwner.TareaChequeoDeviceOwner(),
                    TIEMPO_ENTRE_CONSULTAS
            );
        }
    }

    private class TareaChequeoDeviceOwner extends TimerTask {
        @Override
        public void run() {
            ActividadEsperaDeviceOwner.this.runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(ActividadEsperaDeviceOwner.this, "Chequea si la app es Device Owner", Toast.LENGTH_SHORT).show();
                }
            });

            String nombrePaquete = AdminReceiver.class.getPackage().getName();

            if (APManager.esAplicacionDeviceOwner(ActividadEsperaDeviceOwner.this, nombrePaquete)) {
                ActividadEsperaDeviceOwner.this.redirigirActividadSplash();
            } else {
                try {
                    APManager.convertirAppModoAdmin(ActividadEsperaDeviceOwner.this, nombrePaquete);
                    ActividadEsperaDeviceOwner.this.redirigirActividadSplash();
                } catch (ExcepcionGeneral excepcionGeneral) {
                    ActividadEsperaDeviceOwner.this.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(ActividadEsperaDeviceOwner.this, "No se pudo convertir a Modo Admin", Toast.LENGTH_SHORT).show();
                        }
                    });

                    new Timer().schedule(
                            new ActividadEsperaDeviceOwner.TareaChequeoDeviceOwner(),
                            TIEMPO_ENTRE_CONSULTAS
                    );
                }
            }
        }
    }

    private void redirigirActividadSplash() {
        Intent mainIntent = new Intent(ActividadEsperaDeviceOwner.this, CrazyTowerSplash.class);
        startActivity(mainIntent);
        ActividadEsperaDeviceOwner.this.finish();
    }
}
