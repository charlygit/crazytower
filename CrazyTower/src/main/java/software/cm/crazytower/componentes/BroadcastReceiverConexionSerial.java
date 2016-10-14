package software.cm.crazytower.componentes;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbManager;
import android.util.Log;
import android.widget.Toast;

import software.cm.crazytower.arduino.ControladorArduino;

public class BroadcastReceiverConexionSerial extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            if (intent.getAction().equals(ControladorArduino.PERMISO_USB_LISTENER)) {
                Toast.makeText(context, "Toast intent action", Toast.LENGTH_LONG).show();
                boolean granted = intent.getExtras().getBoolean(UsbManager.EXTRA_PERMISSION_GRANTED);

                if (granted) {
                    Toast.makeText(context, "Toast granted", Toast.LENGTH_LONG).show();
                    ControladorArduino.crearConexionArduino(context);
                } else {
                    Log.d("SERIAL", "PERM NOT GRANTED");
                    Toast.makeText(context, "PERM NOT GRANTED", Toast.LENGTH_LONG).show();
                }
            } else if (intent.getAction().equals(UsbManager.ACTION_USB_DEVICE_ATTACHED)) {
                Toast.makeText(context, "ACTION_USB_DEVICE_ATTACHED", Toast.LENGTH_LONG).show();
                ControladorArduino.iniciarConexionArduino(context);
            } else if (intent.getAction().equals(UsbManager.ACTION_USB_DEVICE_DETACHED)) {
                Toast.makeText(context, "ACTION_USB_DEVICE_DETACHED", Toast.LENGTH_LONG).show();
                ControladorArduino.finalizarConexionArduino(context);

            }
        } catch (Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
