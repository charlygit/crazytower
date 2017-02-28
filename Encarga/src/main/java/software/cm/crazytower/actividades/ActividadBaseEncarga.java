package software.cm.crazytower.actividades;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import software.cm.crazytower.modelo.ArchivosDescargadosAtenti;

public abstract class ActividadBaseEncarga extends FragmentActivity {
    protected static final String PARAM_DATOS_DESCARGADOS = "archivosAtenti";
    protected ArchivosDescargadosAtenti archivosDescargadosAtenti;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.containsKey(PARAM_DATOS_DESCARGADOS)) {
            this.archivosDescargadosAtenti = extras.getParcelable(PARAM_DATOS_DESCARGADOS);
        } else {
            this.archivosDescargadosAtenti = new ArchivosDescargadosAtenti();
        }
    }

    protected void cambiarActividadAtenti(Intent mainIntent) {
        Bundle extras = mainIntent.getExtras();
        if (extras == null || !extras.containsKey(PARAM_DATOS_DESCARGADOS)) {
            mainIntent.putExtra(PARAM_DATOS_DESCARGADOS, this.archivosDescargadosAtenti);
        }

        startActivity(mainIntent);
        this.finish();
    }

    public ArchivosDescargadosAtenti getArchivosDescargadosAtenti() {
        return archivosDescargadosAtenti;
    }

    public ActividadBaseEncarga setArchivosDescargadosAtenti(ArchivosDescargadosAtenti archivosDescargadosAtenti) {
        this.archivosDescargadosAtenti = archivosDescargadosAtenti;
        return this;
    }
}
