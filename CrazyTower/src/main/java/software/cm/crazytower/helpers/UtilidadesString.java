package software.cm.crazytower.helpers;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.InputStream;

import software.cm.crazytower.errores.ExcepcionGeneral;

public class UtilidadesString {
    public static boolean esVacioTexto(String texto) {
        return (texto == null || texto.trim().isEmpty());
    }
}
