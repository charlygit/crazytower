package software.cm.crazytower.actividades;

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
import android.util.Log;
import android.webkit.URLUtil;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import software.cm.crazytower.R;
import software.cm.crazytower.componentes.BroadcastReceiverConexionSerial;
import software.cm.crazytower.componentes.TextProgressBar;
import software.cm.crazytower.errores.ExcepcionGeneral;
import software.cm.crazytower.helpers.Constantes;
import software.cm.crazytower.helpers.UtilidadesAndroid;
import software.cm.crazytower.helpers.UtilidadesArchivo;
import software.cm.crazytower.helpers.UtilidadesInternet;
import software.cm.crazytower.helpers.UtilidadesString;
import software.cm.crazytower.modelo.ConfiguracionAtenti;

public class CrazyTowerSplash extends ActividadBaseEncarga {
    private enum TipoDescarga {ARCHIVO_CONFIGURACION, IMAGEN_HOME, IMAGEN_FIN, IMAGEN, VIDEO, OTROS};
    private static final String PLACEHOLDER_ID_DISPOSITIVO = "ID_DISPOSITIVO";
    private static final String URL_BASE_ENCARGA = "http://www.web-coffee.net/otrasWeb/encarga/";
    private static final String URL_ARCHIVO_CONFIGURACION = URL_BASE_ENCARGA + PLACEHOLDER_ID_DISPOSITIVO + "/configuracionDispositivo.json";
    private static final String URL_DISPOSITIVOS = URL_BASE_ENCARGA + "dispositivos.txt";

    private static TipoDescarga tipoDescarga = TipoDescarga.ARCHIVO_CONFIGURACION;

    private TextProgressBar barraProgreso;
    private TextView labelDescarga;

    private BroadcastReceiverConexionSerial broadcastReceiverConexionSerial;
    /*private String[] urlsDescarga = {
        "http://nine9.com/wp-content/uploads/2016/06/nike_justdoit_00.jpg",
        "http://hlgstudios.com/wp-content/uploads/2011/09/reebok_logo_transparent.png",
        "http://www.sernac.cl/wp-content/uploads/2015/12/Adidas.jpg"
    };*/

    private Long idArchivoConfiguracion;
    private ConfiguracionAtenti configuracionAtenti;

    private Long idImagenHome;
    private Long idImagenFin;
    private ArrayList<Long> idsVideos = new ArrayList<>();
    private ArrayList<Long> idsImagenes = new ArrayList<>();

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        //Log.i("Json", new GsonBuilder().create().toJson(new ConfiguracionAtenti(), ConfiguracionAtenti.class));

        setContentView(R.layout.activity_crazytower_splash);

        this.barraProgreso = (TextProgressBar) findViewById(R.id.barraProgreso);
        this.barraProgreso.setMax(100);

        this.labelDescarga = (TextView) findViewById(R.id.labelDescarga);

        this.encolarDescargaArchivoConfiguracion();
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
        //this.descargarArchivos();
    }

    private boolean esDescargaAtenti(Long idArchivo) {
        if (idArchivo == null) {
            return false;
        }

        if (idArchivo.equals(this.idArchivoConfiguracion)) {
            return true;
        }

        if (idArchivo.equals(this.idImagenHome)) {
            return true;
        }

        if (idArchivo.equals(this.idImagenFin)) {
            return true;
        }

        if (this.idsVideos.contains(idArchivo)) {
            return true;
        }

        if (this.idsImagenes.contains(idArchivo)) {
            return true;
        }

        return false;
    }

    private void encolarDescargaArchivoConfiguracion() {
        String idDispositivo = UtilidadesAndroid.obtenerIdentificadorDispositivo(this);
        this.idArchivoConfiguracion = this.encolarDescarga(
                URL_ARCHIVO_CONFIGURACION.replace(PLACEHOLDER_ID_DISPOSITIVO, idDispositivo), "Descargando archivo de configuración");
    }

    private void encolarDescargaImagenHome() {
        this.idImagenHome = this.encolarDescarga(this.configuracionAtenti.getUrlImagenInicio(), "Descargando imagen Home" );
    }

    private void encolarDescargaImagenFin() {
        this.idImagenHome = this.encolarDescarga(this.configuracionAtenti.getUrlImagenFin(), "Descargando imagen Fin");
    }

    private void encolarDescargaImagen() {
        if (this.configuracionAtenti.getUrlsImagen() != null && !this.configuracionAtenti.getUrlsImagen().isEmpty()) {
            this.tipoDescarga = TipoDescarga.IMAGEN;
            String proximaUrlADescargar = this.configuracionAtenti.getUrlsImagen().remove(0);
            this.idsImagenes.add(this.encolarDescarga(proximaUrlADescargar, "Descargando imagen propaganda"));
        } else {
            this.encolarDescargaVideo();
        }
    }

    private void encolarDescargaVideo() {
        if (this.configuracionAtenti.getUrlsVideo() != null && !this.configuracionAtenti.getUrlsVideo().isEmpty()) {
            this.tipoDescarga = TipoDescarga.VIDEO;
            String proximaUrlADescargar = this.configuracionAtenti.getUrlsVideo().remove(0);
            this.idsVideos.add(this.encolarDescarga(proximaUrlADescargar, "Descargando video propaganda"));
        } else {
            this.irAEncuesta();
        }
    }

    private void irAEncuesta() {
        Intent mainIntent = new Intent(CrazyTowerSplash.this, CrazyTowerHome.class);
        this.cambiarActividadAtenti(mainIntent);
    }

    private BroadcastReceiver descargaCompletaBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                Long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0L);

                if (esDescargaAtenti(id)) {
                    switch (CrazyTowerSplash.this.tipoDescarga) {
                        case ARCHIVO_CONFIGURACION:
                            procesarArchivoConfiguracion(id);
                            break;
                        case IMAGEN_HOME:
                            procesarImagenHome(id);
                            break;
                        case IMAGEN_FIN:
                            procesarImagenFin(id);
                            break;
                        case IMAGEN:
                            procesarImagen(id);
                            break;
                        case VIDEO:
                            procesarVideo(id);
                            break;
                    }
                }
            } catch (Exception excepcionGeneral) {
                Toast.makeText(CrazyTowerSplash.this, "Se produjo un error durante la descarga de un archivo", Toast.LENGTH_LONG);
            }
        }
    };

    private Cursor validarArchivoDescargado(Long id) throws ExcepcionGeneral {
        DownloadManager downloadManager = (DownloadManager) this.getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterById(id);
        Cursor cursor = downloadManager.query(query);

        // Si es vacio se corta la ejecucion y se loguea el error
        if (!cursor.moveToFirst()) {
            Log.w("DescargaArchivos", "Cursor vacio");
            throw new ExcepcionGeneral("Cursor vacío");
        }

        int statusIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
        if (DownloadManager.STATUS_SUCCESSFUL != cursor.getInt(statusIndex)) {
            Log.w("DescargaArchivos", "Download Failed");
            throw new ExcepcionGeneral("Error al descargar el archivo");
        }

        return cursor;
    }

    private void finalizarDescarga(Cursor cursor) {
        if (cursor != null) {
            cursor.close();
        }
    }

    private void procesarArchivoConfiguracion(Long id) {
        Cursor cursor = null;

        try {
            cursor = this.validarArchivoDescargado(id);

            int uriIndex = cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME);
            String uriArchivoConfiguracion = cursor.getString(uriIndex);

            String archivoConfJson = UtilidadesArchivo.leerArchivoDesdeURI(uriArchivoConfiguracion);
            this.configuracionAtenti = new GsonBuilder().create().fromJson(archivoConfJson, ConfiguracionAtenti.class);

            if (UtilidadesString.esVacioTexto(this.configuracionAtenti.getUrlImagenInicio()) ||
                    UtilidadesString.esVacioTexto(this.configuracionAtenti.getUrlImagenFin())) {
                Toast.makeText(this, "Las imagenes de inicio y fin son requeridas para el funcionamiento del sistema", Toast.LENGTH_LONG);
                return;
            }

            if ((this.configuracionAtenti.getUrlsImagen() == null || this.configuracionAtenti.getUrlsImagen().isEmpty()) &&
                    (this.configuracionAtenti.getUrlsVideo() == null || this.configuracionAtenti.getUrlsVideo().isEmpty())) {
                Toast.makeText(this, "Se necesita al menos una imagen o video de propaganda para el funcionamiento del sistema", Toast.LENGTH_LONG);
                return;
            }

            this.tipoDescarga = TipoDescarga.IMAGEN_HOME;
            this.encolarDescargaImagenHome();
        } catch (ExcepcionGeneral excepcionGeneral) {
            Toast.makeText(this, "Se produjo un error durante la descarga del archivo de configuracion", Toast.LENGTH_LONG);
        } finally {
            finalizarDescarga(cursor);
        }
    }

    private void procesarImagenHome(Long id) {
        Cursor cursor = null;

        try {
            cursor = this.validarArchivoDescargado(id);

            int uriIndex = cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME);
            String urlImagenHome = cursor.getString(uriIndex);
            this.archivosDescargadosAtenti.setPathImagenHome(urlImagenHome);

            this.tipoDescarga = TipoDescarga.IMAGEN_FIN;
            this.encolarDescargaImagenFin();
        } catch (ExcepcionGeneral excepcionGeneral) {
            Toast.makeText(this, "Se produjo un error durante la descarga de la imagen de la home", Toast.LENGTH_LONG);
        } finally {
            finalizarDescarga(cursor);
        }
    }

    private void procesarImagenFin(Long id) {
        Cursor cursor = null;

        try {
            cursor = this.validarArchivoDescargado(id);

            int uriIndex = cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME);
            String urlImagenFin = cursor.getString(uriIndex);
            this.archivosDescargadosAtenti.setPathImagenFin(urlImagenFin);

            this.encolarDescargaImagen();
        } catch (ExcepcionGeneral excepcionGeneral) {
            Toast.makeText(this, "Se produjo un error durante la descarga de la imagen de fin", Toast.LENGTH_LONG);
        } finally {
            finalizarDescarga(cursor);
        }
    }

    private void procesarImagen(Long id) {
        Cursor cursor = null;

        try {
            cursor = this.validarArchivoDescargado(id);

            int uriIndex = cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME);
            String urlPathImagen = cursor.getString(uriIndex);
            this.archivosDescargadosAtenti.agregarPathImagen(urlPathImagen);

            // Se finazalió una descarga y se inicia la siguiente (si es que hay otra)
            this.encolarDescargaImagen();
        } catch (ExcepcionGeneral excepcionGeneral) {
            Toast.makeText(this, "Se produjo un error durante la descarga de una imagen de propaganda", Toast.LENGTH_LONG);
        } finally {
            finalizarDescarga(cursor);
        }
    }

    private void procesarVideo(Long id) {
        Cursor cursor = null;

        try {
            cursor = this.validarArchivoDescargado(id);

            int uriIndex = cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME);
            String urlVideo = cursor.getString(uriIndex);
            this.archivosDescargadosAtenti.agregarPathVideo(urlVideo);

            // Se finazalió una descarga y se inicia la siguiente (si es que hay otra)
            this.encolarDescargaVideo();
        } catch (ExcepcionGeneral excepcionGeneral) {
            excepcionGeneral.printStackTrace();
        } finally {
            finalizarDescarga(cursor);
        }
    }

    private Long encolarDescarga(String url, String tituloDescarga) {
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));

        String nombreArchivo = URLUtil.guessFileName(url, null, null);
        request.setTitle(nombreArchivo);
        request.setDescription("Descarga de contenido Atenti");

        request.setVisibleInDownloadsUi(false);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN);

        final DownloadManager downloadManager = (DownloadManager) this.getSystemService(Context.DOWNLOAD_SERVICE);
        final long idArchivoDescarga = downloadManager.enqueue(request);

        this.definirBarraProgreso(downloadManager, idArchivoDescarga, tituloDescarga);

        return (idArchivoDescarga);
    }

    private void definirBarraProgreso(final DownloadManager downloadManager, final long idArchivoDescarga, final String tituloDescarga) {
        this.labelDescarga.setText(tituloDescarga);

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
