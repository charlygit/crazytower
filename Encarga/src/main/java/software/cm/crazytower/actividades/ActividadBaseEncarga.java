package software.cm.crazytower.actividades;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import software.cm.crazytower.helpers.Constantes;
import software.cm.crazytower.helpers.UtilidadesAndroid;
import software.cm.crazytower.modelo.DatosAplicacionAtenti;

public class ActividadBaseEncarga extends FragmentActivity {
    protected static final String PARAM_DATOS_DESCARGADOS = "archivosAtenti";
    protected static final String PARAM_RESULTADO_ENCUESTA = "resultadoEncuesta";
    protected DatosAplicacionAtenti datosAplicacionAtenti;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.containsKey(PARAM_DATOS_DESCARGADOS)) {
            this.datosAplicacionAtenti = extras.getParcelable(PARAM_DATOS_DESCARGADOS);
        } else {
            String idDispositivo = UtilidadesAndroid.obtenerIdentificadorDispositivo(this);
            Log.i(Constantes.TAG, "Id dispostivo: " + idDispositivo);

            this.datosAplicacionAtenti = new DatosAplicacionAtenti();
            this.datosAplicacionAtenti.setIdDispositivo(idDispositivo);
        }
    }

    protected void cambiarActividadAtenti(Intent mainIntent) {
        Bundle extras = mainIntent.getExtras();
        if (extras == null || !extras.containsKey(PARAM_DATOS_DESCARGADOS)) {
            mainIntent.putExtra(PARAM_DATOS_DESCARGADOS, this.datosAplicacionAtenti);
        }

        startActivity(mainIntent);
        this.finish();
    }

    public DatosAplicacionAtenti getDatosAplicacionAtenti() {
        return datosAplicacionAtenti;
    }

    public ActividadBaseEncarga setDatosAplicacionAtenti(DatosAplicacionAtenti datosAplicacionAtenti) {
        this.datosAplicacionAtenti = datosAplicacionAtenti;
        return this;
    }
}
