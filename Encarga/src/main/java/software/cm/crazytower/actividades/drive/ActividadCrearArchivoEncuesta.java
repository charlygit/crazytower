package software.cm.crazytower.actividades.drive;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.drive.DriveContents;
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.Metadata;
import com.google.android.gms.drive.MetadataChangeSet;
import com.google.android.gms.drive.query.Filters;
import com.google.android.gms.drive.query.Query;
import com.google.android.gms.drive.query.SearchableField;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import software.cm.crazytower.actividades.ActividadBaseEncarga;
import software.cm.crazytower.helpers.UtilidadesAndroid;
import software.cm.crazytower.modelo.encuesta.ResultadoEncuesta;

public class ActividadCrearArchivoEncuesta extends ActividadGoogleDriveBase {
    private static final String TAG = "CrearDirEncuesta";
    private boolean existeDirectorio;
    private DriveId idDirectorio;
    private String idDispositivo;
    private ResultadoEncuesta resultadoEncuesta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.resultadoEncuesta = getIntent().getExtras().getParcelable(
                ActividadBaseEncarga.PARAM_RESULTADO_ENCUESTA);
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        super.onConnected(connectionHint);
        // create new contents resource
        Drive.DriveApi.newDriveContents(getGoogleApiClient())
                .setResultCallback(driveContentsCallback);
    }

    final private ResultCallback<DriveApi.DriveContentsResult> driveContentsCallback = new
        ResultCallback<DriveApi.DriveContentsResult>() {
            @Override
            public void onResult(DriveApi.DriveContentsResult result) {
                if (!result.getStatus().isSuccess()) {
                    showMessage("Error while trying to create new file contents");
                    return;
                }
                final DriveContents driveContents = result.getDriveContents();

                // Perform I/O off the UI thread.
                new Thread() {
                    @Override
                    public void run() {
                        // write content to DriveContents
                        OutputStream outputStream = driveContents.getOutputStream();
                        Writer writer = new OutputStreamWriter(outputStream);
                        try {
                            writer.write(new GsonBuilder().create().toJson(ActividadCrearArchivoEncuesta.this.resultadoEncuesta));
                            writer.close();
                        } catch (IOException e) {
                            Log.e(TAG, e.getMessage());
                        }

                        MetadataChangeSet changeSet = new MetadataChangeSet.Builder()
                                .setTitle(System.currentTimeMillis() + "")
                                .setMimeType("text/plain")
                                .setStarred(true).build();

                        // create a file on root folder
                        ActividadCrearArchivoEncuesta.this.datosAplicacionAtenti.getDriveId()
                                .asDriveFolder()
                                .createFile(getGoogleApiClient(), changeSet, driveContents)
                                .setResultCallback(fileCallback);
                    }
                }.start();
            }
        };

    final private ResultCallback<DriveFolder.DriveFileResult> fileCallback = new
        ResultCallback<DriveFolder.DriveFileResult>() {
            @Override
            public void onResult(DriveFolder.DriveFileResult result) {
                if (!result.getStatus().isSuccess()) {
                    showMessage("Error while trying to create the file");
                    return;
                }
                showMessage("Created a file with content: " + result.getDriveFile().getDriveId());
            }
        };
}