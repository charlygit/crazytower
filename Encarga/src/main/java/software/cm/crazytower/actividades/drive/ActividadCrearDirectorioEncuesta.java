package software.cm.crazytower.actividades.drive;

import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;

import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.Metadata;
import com.google.android.gms.drive.MetadataChangeSet;
import com.google.android.gms.drive.query.Filters;
import com.google.android.gms.drive.query.Query;
import com.google.android.gms.drive.query.SearchableField;

public class ActividadCrearDirectorioEncuesta extends ActividadGoogleDriveBase {
    private static final String TAG = "CrearDirEncuesta";
    private boolean existeDirectorio;
    private DriveId idDirectorio;

    @Override
    public void onConnected(Bundle connectionHint) {
        super.onConnected(connectionHint);

        final String idDispositivo = Settings.Secure.getString(
                this.getContentResolver(), Settings.Secure.ANDROID_ID);

        new Thread() {
            @Override
            public void run() {
                Query query = new Query.Builder()
                        .addFilter(Filters.and(Filters.eq(
                                SearchableField.TITLE, idDispositivo),
                                Filters.eq(SearchableField.TRASHED, false)))
                        .build();

                DriveApi.MetadataBufferResult result = Drive.DriveApi.query(
                        getGoogleApiClient(), query).await();

                if (!result.getStatus().isSuccess()) {
                    Log.e(TAG, "No se puede crear el directorio en la raiz");
                } else {
                    boolean isFound = false;
                    for (Metadata m : result.getMetadataBuffer()) {
                        if (m.getTitle().equals(idDispositivo)) {
                            Log.i(TAG, "El directorio donde guarda el dispositivo ya existe");
                            isFound = true;
                            ActividadCrearDirectorioEncuesta.this.idDirectorio = m.getDriveId();
                            break;
                        }
                    }
                    if (!isFound) {
                        Log.i(TAG, "El directorio donde debe guardar las encuestas el dispositivo no existe, se procede a crearlo");
                        MetadataChangeSet changeSet = new MetadataChangeSet.Builder()
                                .setTitle(idDispositivo)
                                .build();

                        Drive.DriveApi.getRootFolder(getGoogleApiClient())
                                .createFolder(getGoogleApiClient(), changeSet).await();

                        if (!result.getStatus().isSuccess()) {
                            Log.e(TAG, "Error al crear del directorio donde se guardan las encuestas: " + result.getStatus().getStatusMessage());
                        } else {
                            ActividadCrearDirectorioEncuesta.this.idDirectorio = result.getMetadataBuffer().get(0).getDriveId();
                            Log.i(TAG, "El directorio donde se guardaran las encuestas se ha creado exitosamente");
                        }
                    }
                }
            }
        }.start();
    }

    public boolean isExisteDirectorio() {
        return existeDirectorio;
    }

    public void setExisteDirectorio(boolean existeDirectorio) {
        this.existeDirectorio = existeDirectorio;
    }

    public DriveId getIdDirectorio() {
        return idDirectorio;
    }

    public void setIdDirectorio(DriveId idDirectorio) {
        this.idDirectorio = idDirectorio;
    }
}
