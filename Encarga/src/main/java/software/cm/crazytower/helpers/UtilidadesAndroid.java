package software.cm.crazytower.helpers;

import android.content.Context;
import android.provider.Settings;

public class UtilidadesAndroid {
    public static String obtenerIdentificadorDispositivo(Context context) {
        return (Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID));
    }
}
