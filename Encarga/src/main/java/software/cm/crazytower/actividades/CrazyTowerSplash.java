package software.cm.crazytower.actividades;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import software.cm.crazytower.R;
import software.cm.crazytower.componentes.BroadcastReceiverConexionSerial;
import software.cm.crazytower.componentes.TextProgressBar;
import software.cm.crazytower.errores.ExcepcionGeneral;
import software.cm.crazytower.helpers.Constantes;
import software.cm.crazytower.helpers.UtilidadesAndroid;
import software.cm.crazytower.helpers.UtilidadesArchivo;
import software.cm.crazytower.helpers.UtilidadesInternet;

public class CrazyTowerSplash extends Activity {
    private final int SPLASH_DISPLAY_LENGTH = 1000;
    private TextProgressBar barraProgreso;

    private BroadcastReceiverConexionSerial broadcastReceiverConexionSerial;
    /*private String[] urlsDescarga = {
        "http://nine9.com/wp-content/uploads/2016/06/nike_justdoit_00.jpg",
        "http://hlgstudios.com/wp-content/uploads/2011/09/reebok_logo_transparent.png",
        "http://www.sernac.cl/wp-content/uploads/2015/12/Adidas.jpg"
    };*/

    private ArrayList<Long> idsArchivosDescarga = new ArrayList<>();
    private List<String> urlsDescarga =
            new ArrayList<>(Arrays.asList("VideoClaro.3gp;http://server1.elgenero.com/cgi-bin/3gpv.cgi?file=Justin%20Quiles%20-%20SI%20Ella%20Quisiera.3gp"));

    private List<String> urisArchivosDescargados = new ArrayList<>();
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        String idDispositivo = UtilidadesAndroid.obtenerIdentificadorDispositivo(this);
        Log.i("idDispositivo", idDispositivo);

        setContentView(R.layout.activity_crazytower_splash);

        this.barraProgreso = (TextProgressBar) findViewById(R.id.barraProgreso);
        this.barraProgreso.setMax(100);
        /*DescargadorImagenes descargadorImagenes = new DescargadorImagenes(barraProgreso);
        descargadorImagenes.execute(this.urlsDescarga);*/

        this.registerReceiver(descargaCompletaBroadcastReceiver,
                new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

        /*this.broadcastReceiverConexionSerial = new BroadcastReceiverConexionSerial();
        IntentFilter filter = ControladorArduino.crearFiltroArduinoBroadcastReceiver();

        registerReceiver(this.broadcastReceiverConexionSerial, filter);*/
        /*new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                *//* Create an Intent that will start the Menu-Activity. *//*
                Intent mainIntent = new Intent(CrazyTowerSplash.this, CrazyTowerHome.class);
                CrazyTowerSplash.this.startActivity(mainIntent);
                CrazyTowerSplash.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);*/
        this.descargarArchivos();
    }

    private void descargarArchivos() {
        if (this.urlsDescarga == null || this.urlsDescarga.isEmpty()) {
            Intent mainIntent = new Intent(CrazyTowerSplash.this, CrazyTowerHome.class);

            int cantArchivosDescargados = this.urisArchivosDescargados.size();
            for (int i=0; i < cantArchivosDescargados; i++) {
                mainIntent.putExtra("archivo" + i, this.urisArchivosDescargados.get(i));
            }

            CrazyTowerSplash.this.startActivity(mainIntent);
            CrazyTowerSplash.this.finish();
            return;
        }

        String urlAtenti = this.urlsDescarga.remove(0);
        this.barraProgreso.setProgress(0);
        this.encolarDescarga(urlAtenti);
    }

    private BroadcastReceiver descargaCompletaBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0L);

            // Descartamos la notificacion de un archivo descargado que no es de nuestra app
            if (!CrazyTowerSplash.this.idsArchivosDescarga.contains(id)) {
                return;
            }

            DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
            DownloadManager.Query query = new DownloadManager.Query();
            query.setFilterById(id);
            Cursor cursor = downloadManager.query(query);

            // Si es vacio se corta la ejecucion y se loguea el error
            if (!cursor.moveToFirst()) {
                CrazyTowerSplash.this.descargarArchivos();
                Log.w("DescargaArchivos", "Cursor vacio");
                return;
            }

            int statusIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
            if (DownloadManager.STATUS_SUCCESSFUL != cursor.getInt(statusIndex)) {
                CrazyTowerSplash.this.descargarArchivos();
                Log.w("DescargaArchivos", "Download Failed");
                return;
            }

            int uriIndex = cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI);
            String uriArchivoDescargado = cursor.getString(uriIndex);
            CrazyTowerSplash.this.urisArchivosDescargados.add(uriArchivoDescargado);
            CrazyTowerSplash.this.descargarArchivos();
        }
    };

    private void encolarDescarga(String urlAtenti) {
        String nombreArchivo = urlAtenti.split(";")[0];
        String url = urlAtenti.split(";")[1];

        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));

        // only download via WIFI
        request.setTitle("Descarga video claro");
        request.setDescription("Descarga de contenido Atenti");

        // we just want to download silently
        request.setVisibleInDownloadsUi(false);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN);
        //request.setDestinationInExternalFilesDir(this, null, nombreArchivo);

        // enqueue this request
        final DownloadManager downloadManager = (DownloadManager) this.getSystemService(Context.DOWNLOAD_SERVICE);
        final long idArchivoDescarga = downloadManager.enqueue(request);
        this.idsArchivosDescarga.add(idArchivoDescarga);

        this.definirBarraProgreso(downloadManager, idArchivoDescarga);
    }

    private void definirBarraProgreso(final DownloadManager downloadManager, final long idArchivoDescarga) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean downloading = true;

                while (downloading) {
                    DownloadManager.Query q = new DownloadManager.Query();
                    q.setFilterById(idArchivoDescarga);

                    Cursor cursor = downloadManager.query(q);
                    cursor.moveToFirst();
                    int bytes_downloaded = cursor.getInt(cursor
                            .getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                    int bytes_total = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));

                    if (cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)) == DownloadManager.STATUS_SUCCESSFUL) {
                        downloading = false;
                    }

                    final int progresoDescarga = (int) ((bytes_downloaded * 100l) / bytes_total);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            CrazyTowerSplash.this.barraProgreso.setProgress(progresoDescarga);
                            CrazyTowerSplash.this.barraProgreso.setText(Integer.valueOf(progresoDescarga).toString());
                        }
                    });

                    cursor.close();
                }
            }
        }).start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        unregisterReceiver(this.descargaCompletaBroadcastReceiver);
    }

    private class DescargadorImagenes extends AsyncTask<String, Integer, List<Bitmap>> {
        private TextProgressBar barraProgreso;

        public DescargadorImagenes(TextProgressBar barraProgreso) {
            this.barraProgreso = barraProgreso;
        }

        @Override
        protected List<Bitmap> doInBackground(String... pUrls) {
            List<Bitmap> listaBitmap = new ArrayList<>();
            Bitmap bitmap;

            try {
                int urlsProcesadas = 0;

                for (String url : pUrls) {
                    bitmap = UtilidadesInternet.descargarImagen(url);
                    listaBitmap.add(bitmap);

                    this.publishProgress(++urlsProcesadas);
                }
            } catch (Exception e) {
                Log.e(CrazyTowerSplash.class.getSimpleName(), e.getMessage());
            }

            int i = 1;
            return listaBitmap;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

            this.barraProgreso.setProgress(values[0]);
            this.barraProgreso.setText(((values[0] * 100) / this.barraProgreso.getMax()) + "%");
        }

        @Override
        protected void onPostExecute(List<Bitmap> listaBitmap) {
            if (listaBitmap != null && !listaBitmap.isEmpty()) {
                int iter = 0;

                for (Bitmap bitmap : listaBitmap) {
                    this.guardarImagen(iter++, bitmap);
                }
            }

            Intent mainIntent = new Intent(CrazyTowerSplash.this, CrazyTowerHome.class);
            CrazyTowerSplash.this.startActivity(mainIntent);
            CrazyTowerSplash.this.finish();
        }

        private void guardarImagen(int nroImagen, Bitmap imagen) {
            try {
                String nombreArchivo = String.format("%s_%s", Constantes.PREFIJO_NOMBRE_ARCHIVO_IMAGEN_HOME, nroImagen);
                UtilidadesArchivo.guardarArchivoBitmap(CrazyTowerSplash.this, nombreArchivo, imagen);
            } catch (ExcepcionGeneral excepcionGeneral) {
                Log.e(CrazyTowerSplash.class.getSimpleName(), excepcionGeneral.getMessage());
            }
        }
    }
}
