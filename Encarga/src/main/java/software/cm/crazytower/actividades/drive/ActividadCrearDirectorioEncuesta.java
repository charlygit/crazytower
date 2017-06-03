package software.cm.crazytower.actividades.drive;

import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.Metadata;
import com.google.android.gms.drive.MetadataChangeSet;
import com.google.android.gms.drive.query.Filters;
import com.google.android.gms.drive.query.Query;
import com.google.android.gms.drive.query.SearchableField;

import software.cm.crazytower.helpers.UtilidadesAndroid;

public class ActividadCrearDirectorioEncuesta extends ActividadGoogleDriveBase {
    private static final String TAG = "CrearDirEncuesta";
    private boolean existeDirectorio;
    private DriveId idDirectorio;
    private String idDispositivo;

    private Runnable procesoConexionApiCliente = new Runnable() {
        @Override
        public void run() {
            String nombreDirectorioRaiz = idDispositivo;

            GoogleApiClient googleApiClient = getGoogleApiClient();

            Query query = armarQueryBusquedaDirectorio(nombreDirectorioRaiz);

            Drive.DriveApi.requestSync(googleApiClient).await();

            DriveApi.MetadataBufferResult result = Drive.DriveApi.query(
                    googleApiClient, query).await();

            if (!result.getStatus().isSuccess()) {
                Log.e(TAG, "No se puede crear el directorio en la raiz. " + result.getStatus().getStatusMessage());
                return;
            }

            existeDirectorio = existeDirectorio(result, nombreDirectorioRaiz);
            if (!existeDirectorio) {
                crearDirectorio(googleApiClient);
            }
        }
    };

    @Override
    public void onConnected(Bundle connectionHint) {
        super.onConnected(connectionHint);
        this.existeDirectorio = false;
        this.idDispositivo = UtilidadesAndroid.obtenerIdentificadorDispositivo(this);

        new Thread(procesoConexionApiCliente).start();
    }

    // --------------------------------------
    // AUXILIARES PARA CREACION DE DIRECTORIO
    // --------------------------------------
    private Query armarQueryBusquedaDirectorio(String nombreDirectorio) {
        Query query = new Query.Builder()
                .addFilter(Filters.and(Filters.eq(
                        SearchableField.TITLE, nombreDirectorio),
                        Filters.eq(SearchableField.TRASHED, false)))
                .build();

        return query;
    }

    private boolean existeDirectorio(DriveApi.MetadataBufferResult resultadoMetadata,  String nombreDirectorio) {
        boolean existeDirectorio = false;

        for (Metadata m : resultadoMetadata.getMetadataBuffer()) {
            if (m.getTitle().equals(nombreDirectorio)) {
                Log.i(TAG, "El directorio donde guarda el dispositivo ya existe");
                existeDirectorio = true;
                ActividadCrearDirectorioEncuesta.this.idDirectorio = m.getDriveId();
                break;
            }
        }

        return existeDirectorio;
    }

    private void crearDirectorio(GoogleApiClient googleApiClient) {
        Log.i(TAG, "El directorio donde debe guardar las encuestas el dispositivo no existe, se procede a crearlo");
        MetadataChangeSet changeSet = new MetadataChangeSet.Builder()
                .setTitle(idDispositivo)
                .build();

        DriveFolder.DriveFolderResult folderResult = Drive.DriveApi.getRootFolder(googleApiClient)
                .createFolder(googleApiClient, changeSet).await();

        if (!folderResult.getStatus().isSuccess()) {
            Log.e(TAG, "Error al crear del directorio donde se guardan las encuestas: "
                    + folderResult.getStatus().getStatus().getStatusMessage());
        } else {
            idDirectorio = folderResult.getDriveFolder().getDriveId();
            existeDirectorio = true;
            Log.i(TAG, "El directorio donde se guardaran las encuestas se ha creado exitosamente");
        }
    }

    // GETTERS AND SETTERS
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