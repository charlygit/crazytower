package software.cm.crazytower.actividades;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ViewSwitcher;

import software.cm.crazytower.R;
import software.cm.crazytower.actividades.encuesta.ActividadEncuesta;
import software.cm.crazytower.helpers.APManager;
import software.cm.crazytower.servicios.ServicioMonitoreoConexiones;

public class CrazyTowerHome extends Activity {
    // Variables de control del visor de propagandas
    private int nroImagen;
    private ImageSwitcher imageSwitcher;
    private static final Integer DURACION_IMAGEN_MS = 8000;
    private int[] gallery = {R.drawable.nike, R.drawable.adidas, R.drawable.puma, R.drawable.reebok};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_crazytower_homescreen);

        // Iniciar el visor de propagandas
        this.iniciarImageSwitcher();

        // Inicia el servicio de monitoreo de conexiones
        this.startService(new Intent(this, ServicioMonitoreoConexiones.class));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Intent mainIntent = new Intent(CrazyTowerHome.this, ActividadEncuesta.class);
        CrazyTowerHome.this.startActivity(mainIntent);
        CrazyTowerHome.this.finish();

        return super.onTouchEvent(event);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.System.canWrite(CrazyTowerHome.this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
                intent.setData(Uri.parse("package:" + CrazyTowerHome.this.getPackageName()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        }
    }

    // ----------
    // AUXILIARES
    // ----------
    private void iniciarImageSwitcher() {
        this.imageSwitcher = (ImageSwitcher) findViewById(R.id.imageSwitcher);
        this.imageSwitcher.setFactory(new ViewSwitcher.ViewFactory() {

            public View makeView() {
                ImageView imageView = new ImageView(CrazyTowerHome.this);
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                return imageView;
            }
        });

        this.imageSwitcher.postDelayed(new Runnable() {
            public void run() {
                imageSwitcher.setImageResource(gallery[nroImagen++]);

                if (nroImagen == gallery.length) {
                    nroImagen = 0;
                }
                imageSwitcher.postDelayed(this, DURACION_IMAGEN_MS);
            }
        }, 0);

        // Se definen las animaciones de entrada y de salida de las imagenes
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        Animation fadeOut = AnimationUtils.loadAnimation(this, R.anim.fade_out);
        imageSwitcher.setInAnimation(fadeIn);
        imageSwitcher.setOutAnimation(fadeOut);
    }
}