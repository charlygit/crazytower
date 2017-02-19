package software.cm.crazytower.helpers;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import software.cm.crazytower.errores.ExcepcionGeneral;

public class UtilidadesInternet {
    public static Bitmap descargarImagen(String url) throws ExcepcionGeneral {
        try {
            InputStream in = new java.net.URL(url).openStream();
            return (BitmapFactory.decodeStream(in));
        } catch (Exception e) {
            throw new ExcepcionGeneral(e);
        }
    }

    public static Bitmap descargarImagen(String url, int timeout) throws ExcepcionGeneral {
        try {
            URL urlObj = new URL(url);
            URLConnection con = urlObj.openConnection();
            con.setConnectTimeout(timeout);
            con.setReadTimeout(timeout);
            InputStream in = con.getInputStream();

            return (BitmapFactory.decodeStream(in));
        } catch (Exception e) {
            throw new ExcepcionGeneral(e);
        }
    }
}
