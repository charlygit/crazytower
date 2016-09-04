package software.cm.crazytower;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ViewSwitcher;

import software.cm.crazytower.encuesta.ActividadEncuesta;

public class CrazyTowerHome extends Activity {
    // Variables de control del visor de propagandas
    private int nroImagen;
    private ImageSwitcher imageSwitcher;
    private static final Integer DURACION_IMAGEN_MS = 8000;
    private int[] gallery = {R.drawable.a, R.drawable.b, R.drawable.c};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_crazytower_homescreen);

        // Iniciar el visor de propagandas
        this.iniciarImageSwitcher();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Intent mainIntent = new Intent(CrazyTowerHome.this, ActividadEncuesta.class);
        CrazyTowerHome.this.startActivity(mainIntent);
        CrazyTowerHome.this.finish();

        return super.onTouchEvent(event);
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
        }, DURACION_IMAGEN_MS);

        // Se definen las animaciones de entrada y de salida de las imagenes
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        Animation fadeOut = AnimationUtils.loadAnimation(this, R.anim.fade_out);
        imageSwitcher.setInAnimation(fadeIn);
        imageSwitcher.setOutAnimation(fadeOut);
    }
}
