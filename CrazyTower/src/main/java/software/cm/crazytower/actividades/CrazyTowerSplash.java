package software.cm.crazytower.actividades;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import software.cm.crazytower.R;
import software.cm.crazytower.componentes.TextProgressBar;
import software.cm.crazytower.errores.ExcepcionGeneral;
import software.cm.crazytower.helpers.Constantes;
import software.cm.crazytower.helpers.UtilidadesArchivo;
import software.cm.crazytower.helpers.UtilidadesInternet;

public class CrazyTowerSplash extends Activity {
    /** Duration of wait **/
    private final int SPLASH_DISPLAY_LENGTH = 1000;
    private String[] urls = {
        "http://nine9.com/wp-content/uploads/2016/06/nike_justdoit_00.jpg",
        "http://hlgstudios.com/wp-content/uploads/2011/09/reebok_logo_transparent.png",
        "http://www.sernac.cl/wp-content/uploads/2015/12/Adidas.jpg"
    };

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_crazytower_splash);

        TextProgressBar barraProgreso = (TextProgressBar) findViewById(R.id.barraProgreso);
        barraProgreso.setMax(3);

        DescargadorImagenes descargadorImagenes = new DescargadorImagenes(barraProgreso);
        descargadorImagenes.execute(this.urls);

        /*new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                *//* Create an Intent that will start the Menu-Activity. *//*
                Intent mainIntent = new Intent(CrazyTowerSplash.this, CrazyTowerHome.class);
                CrazyTowerSplash.this.startActivity(mainIntent);
                CrazyTowerSplash.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);*/
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
