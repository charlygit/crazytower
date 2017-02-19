package software.cm.crazytower.helpers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;

import software.cm.crazytower.errores.ExcepcionGeneral;

public class UtilidadesArchivo {
    public static void guardarArchivoBitmap(Context context, String nombreImagen, Bitmap imagen) throws ExcepcionGeneral {
        FileOutputStream archivoStream = null;

        if (UtilidadesString.esVacioTexto(nombreImagen)) {
            throw new ExcepcionGeneral("El nombre de la imagen no puede ser vacío");
        }

        try {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            imagen.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            archivoStream = context.openFileOutput(nombreImagen, Context.MODE_PRIVATE);
            archivoStream.write(bytes.toByteArray());
        } catch (Exception e) {
            Log.e(UtilidadesArchivo.class.getSimpleName(), "Error al guardar el archivo " + nombreImagen);
            throw new ExcepcionGeneral(e);
        } finally {
            UtilidadesArchivo.cerrarArchivo(archivoStream);
        }
    }

    public static void guardarArchivoBitmap(Context context, String nombreImagen, String url) throws ExcepcionGeneral {
        UtilidadesArchivo.guardarArchivoBitmap(context, nombreImagen, UtilidadesInternet.descargarImagen(url));
    }

    public static Bitmap cargarArchivo(Context context, String nombreImagen) throws ExcepcionGeneral {
        if (UtilidadesString.esVacioTexto(nombreImagen)) {
            throw new ExcepcionGeneral("El nombre de la imagen no puede ser vacío");
        }

        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inScaled = false;

            return (BitmapFactory.decodeStream(context.openFileInput(nombreImagen), null, options));
        } catch (Exception e) {
            throw new ExcepcionGeneral("Se produjo un error al cargar la imagen " + nombreImagen, e);
        }
    }

    // ----------
    // AUXILIARES
    // ----------
    private static void cerrarArchivo(FileOutputStream archivoStream) {
        if (archivoStream != null) {
            try {
                archivoStream.close();
            } catch (Exception e) {
                Log.e(UtilidadesArchivo.class.getSimpleName(), "Error al cerrar el archivo");
            }
        }
    }
}
