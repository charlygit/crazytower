package software.cm.crazytower.servicios;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import java.io.IOException;
import java.util.UUID;

import software.cm.crazytower.errores.ExcepcionGeneral;

public class ServicioBluetooth extends Service {
    private static final int CANT_TOTAL_INTENTOS = 5;
    private static final int INTERVALO_ENTRE_INTENTOS = 5000;
    private int intentoConexion = 0;

    private final IBinder mBinder = new LocalBinder();
    private BluetoothAdapter adaptadorBluetooth = null;
    private BluetoothSocket socketBluetooth = null;
    private boolean hayConexionBluetoothEstablecida = false;

    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    static final String MAC_ARDUINO = "20:16:08:04:79:10";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        new Handler().postDelayed(new ConexionBluetooth(), 1000L);

        Toast.makeText(this, "ServicioBluetooth - onCreate", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "ServicioBluetooth - onDestroy", Toast.LENGTH_LONG).show();
    }

    // ---------------------------------
    // METODOS EXPUESTOS POR EL SERVICIO
    // ---------------------------------
    public boolean hayConexionBluetoothEstablecida() {
        return this.hayConexionBluetoothEstablecida;
    }

    public BluetoothSocket obtenerSocketBluetooth() {
        return this.socketBluetooth;
    }

    public void enviarDatoSocketBluetooth(String dato) {
        if (this.socketBluetooth != null) {
            try {
                this.socketBluetooth.getOutputStream().write(dato.getBytes());
            } catch (Exception e) {
                Toast.makeText(this, "Se produjo un error al activar la carga del dispositivo", Toast.LENGTH_LONG).show();
            }
        }
    }
    // -----------------
    // CLASES AUXILIARES
    // -----------------
    public class LocalBinder extends Binder {
        public ServicioBluetooth obtenerServicio() {
            return ServicioBluetooth.this;
        }
    }

    private class ConexionBluetooth implements Runnable {
        private boolean conexionExitosa = true;

        @Override
        public void run() {
            try {
                if (socketBluetooth == null || !hayConexionBluetoothEstablecida) {
                    adaptadorBluetooth = BluetoothAdapter.getDefaultAdapter();

                    if (adaptadorBluetooth != null) {
                        if (!adaptadorBluetooth.isEnabled()) {
                            adaptadorBluetooth.enable();
                        }

                        BluetoothDevice dispositivo = adaptadorBluetooth.getRemoteDevice(MAC_ARDUINO);
                        socketBluetooth = dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID);
                        BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                        socketBluetooth.connect();
                    } else {
                        Toast.makeText(getApplicationContext(),
                                "No hay un adaptador de bluetooth en el dispositivo",
                                Toast.LENGTH_LONG).show();
                    }
                }
            }
            catch (Exception e) {
                conexionExitosa = false;
                Toast.makeText(ServicioBluetooth.this,
                        "Se produjo un error al intentar conectar con el dispositivo de carga",
                        Toast.LENGTH_LONG).show();

                if (intentoConexion < CANT_TOTAL_INTENTOS) {
                    Toast.makeText(ServicioBluetooth.this,
                            "Intentos de conexión restantes: " + (CANT_TOTAL_INTENTOS - intentoConexion),
                            Toast.LENGTH_LONG).show();
                    intentoConexion++;

                    new Handler().postDelayed(new ConexionBluetooth(), INTERVALO_ENTRE_INTENTOS);
                }
            }
        }
    }

    private void desconectarSocketBT()
    {
        if (socketBluetooth!=null) //If the btSocket is busy
        {
            try
            {
                socketBluetooth.close(); //close connection
            }
            catch (IOException e)
            {}
        }
    }
}
