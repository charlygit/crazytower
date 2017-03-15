package software.cm.crazytower.arduino;

import android.app.PendingIntent;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.felhr.usbserial.UsbSerialDevice;
import com.felhr.usbserial.UsbSerialInterface;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import software.cm.crazytower.helpers.UtilidadesString;

public class ControladorArduino {
    public static final String PERMISO_USB_LISTENER = "com.hariharan.arduinousb.USB_PERMISSION";

    private static final String FIN_DATOS_ARDUINO = "/n";
    private static final int ARDUINO_VENDOR_ID = 9025;

    private static UsbManager usbManager;
    private static UsbDevice dispositivo;
    private static UsbSerialDevice serialPort;
    private static UsbDeviceConnection conexion;
    private static Context contextoStatic;
    private static int proxSlotLibre = 0;

    private static UsbSerialInterface.UsbReadCallback mCallbackArduino = new UsbSerialInterface.UsbReadCallback() {
        @Override
        public void onReceivedData(byte[] arg0) {
            try {
                String data = new String(arg0, "UTF-8");
                data.concat(FIN_DATOS_ARDUINO);

                //Toast.makeText(contextoStatic, "Recibido: " + data, //Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    public static void habilitarPuerto(Context contexto) {
        enviarDatos(contexto, String.valueOf(proxSlotLibre++));

        if (proxSlotLibre == 4) {
            proxSlotLibre = 0;
        }
    }

    public static void habilitarPuerto(Context contexto, String dato) {
        enviarDatos(contexto, dato);
    }

    public static void habilitarPuerto(Context contexto, BluetoothSocket btSocket, String dato) {
        enviarDatos(contexto, btSocket, dato);
    }

    private static void enviarDatos(Context contexto, String datos) {
        try {
            contextoStatic = contexto;
            if (serialPort != null) {
                if (UtilidadesString.esVacioTexto(datos)) {
                    //Toast.makeText(contexto, "No se pueden enviar datos NULL a Arduino", //Toast.LENGTH_LONG).show();
                    return;
                }

                String string = datos.toString();
                serialPort.write(string.getBytes());

                Toast.makeText(contexto, "Envio: " + datos, Toast.LENGTH_SHORT).show();
            } else {
                //Toast.makeText(contexto, "No se pueden enviar datos a Arduino (puerto serial nulo)", //Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            //Toast.makeText(contexto, "Se produjo un error enviando datos a Arduino", //Toast.LENGTH_LONG).show();
        }
    }

    private static void enviarDatos(Context contexto, BluetoothSocket btSocket, String datos) {
        if (btSocket != null) {
            try {
                btSocket.getOutputStream().write(datos.toString().getBytes());
            } catch (IOException e) {
                Toast.makeText(contexto, "No se pueden enviar datos a Arduino (puerto serial nulo)", Toast.LENGTH_LONG).show();
            }
        }
    }

    public static IntentFilter crearFiltroArduinoBroadcastReceiver() {
        IntentFilter filtro = new IntentFilter();
        filtro.addAction(PERMISO_USB_LISTENER);
        filtro.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
        filtro.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);

        return filtro;
    }

    public static void crearConexionArduino(Context contexto) {
        usbManager = (UsbManager) contexto.getSystemService(contexto.USB_SERVICE);
        conexion = usbManager.openDevice(dispositivo);
        serialPort = UsbSerialDevice.createUsbSerialDevice(dispositivo, conexion);

        if (serialPort != null) {
            if (serialPort.open()) {
                serialPort.setBaudRate(9600);
                serialPort.setDataBits(UsbSerialInterface.DATA_BITS_8);
                serialPort.setStopBits(UsbSerialInterface.STOP_BITS_1);
                serialPort.setParity(UsbSerialInterface.PARITY_NONE);
                serialPort.setFlowControl(UsbSerialInterface.FLOW_CONTROL_OFF);
                serialPort.read(mCallbackArduino);
            } else {
                Log.d("SERIAL", "PORT NOT OPEN");
                //Toast.makeText(contexto, "PORT NOT OPEN", //Toast.LENGTH_LONG).show();
            }
        }
    }

    public static void iniciarConexionArduino(Context contexto) {
        usbManager = (UsbManager) contexto.getSystemService(contexto.USB_SERVICE);
        HashMap<String, UsbDevice> usbDevices = usbManager.getDeviceList();

        if (!usbDevices.isEmpty()) {
            try {
                boolean encontreArduino = false;
                Iterator<Map.Entry<String, UsbDevice>> it = usbDevices.entrySet().iterator();

                while (it.hasNext() && !encontreArduino) {
                    Map.Entry<String, UsbDevice> dispositivoIter = it.next();

                    dispositivo = dispositivoIter.getValue();
                    int dispositivoIterVendorID = dispositivo.getVendorId();

                    if (dispositivoIterVendorID == ARDUINO_VENDOR_ID) {
                        //Toast.makeText(contexto, "If principal", //Toast.LENGTH_LONG).show();
                        PendingIntent pi = PendingIntent.getBroadcast(contexto, 0, new Intent(PERMISO_USB_LISTENER), 0);
                        usbManager.requestPermission(dispositivo, pi);
                        encontreArduino = true;

                        //Toast.makeText(contexto, "Encontre arduino", //Toast.LENGTH_LONG).show();
                    } else {
                        //Toast.makeText(contexto, "Connection null", //Toast.LENGTH_LONG).show();
                        conexion = null;
                        dispositivo = null;
                    }
                }
            } catch (Exception e) {
                //Toast.makeText(contexto, "EX on start", //Toast.LENGTH_LONG).show();
            }
        } else {
            //Toast.makeText(contexto, "No hay dispositivos", //Toast.LENGTH_SHORT).show();
        }
    }

    public static void finalizarConexionArduino(Context contexto) {
        try {
            if (serialPort != null) {
                serialPort.close();

                //Toast.makeText(contexto, "Puerto serial cerrado", //Toast.LENGTH_LONG).show();
            } else {
                //Toast.makeText(contexto, "Puerto serial NULL (No se puede cerrar)", //Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            //Toast.makeText(contexto, "EX on start", //Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
}
