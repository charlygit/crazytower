package software.cm.crazytower.actividades;

import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ViewSwitcher;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import software.cm.crazytower.R;
import software.cm.crazytower.actividades.encuesta.ActividadEncuesta;
import software.cm.crazytower.arduino.ControladorArduino;
import software.cm.crazytower.componentes.BroadcastReceiverConexionSerial;
import software.cm.crazytower.componentes.fragmentos.encuesta.FragmentoEncuesta;
import software.cm.crazytower.componentes.fragmentos.home.FragmentoHomeImagen;
import software.cm.crazytower.componentes.fragmentos.home.FragmentoHomeVideo;
import software.cm.crazytower.errores.ExcepcionGeneral;
import software.cm.crazytower.helpers.Constantes;
import software.cm.crazytower.helpers.UtilidadesArchivo;
import software.cm.crazytower.servicios.ServicioMonitoreoConexiones;

public class CrazyTowerHome extends FragmentActivity {
    // Variables de control del visor de propagandas
    private int nroImagen;
    private ImageSwitcher imageSwitcher;
    private static final Integer DURACION_IMAGEN_MS = 8000;
    private int[] galeriaEstatica = {R.drawable.imagen_ikea};
    private List<Bitmap> imagenes;
    private BroadcastReceiverConexionSerial broadcastReceiverConexionSerial;
    private ViewPager mPaginador;
    private FragmentPagerAdapter mPaginadorAdapter;
    private static final int cantPaginas = 2;
    private int paginaActual = 0;

    String uriVideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.uriVideo = getIntent().getExtras().getString("archivo0");

        setContentView(R.layout.activity_crazytower_homescreen);

        // Iniciar el visor de propagandas
        //this.iniciarImageSwitcher();

        this.mPaginador = (ViewPager) findViewById(R.id.pagerHome);
        this.mPaginador.setOffscreenPageLimit(10);
        this.mPaginador.addOnPageChangeListener(new HomePageOnPageChangeListener());
        this.mPaginadorAdapter = new HomePageAdapter(getSupportFragmentManager());
        this.mPaginador.setAdapter(mPaginadorAdapter);
        this.mPaginador.beginFakeDrag();

        //this.mPaginador.setCurrentItem(0);
        // Inicia el servicio de monitoreo de conexiones
        this.startService(new Intent(this, ServicioMonitoreoConexiones.class));

        this.imagenes = new ArrayList<>();

        // Inicia y registra arduino broadcast receiver
        this.broadcastReceiverConexionSerial = new BroadcastReceiverConexionSerial();
        IntentFilter filter = ControladorArduino.crearFiltroArduinoBroadcastReceiver();

        registerReceiver(this.broadcastReceiverConexionSerial, filter);

        /*Bitmap bitmap;

        for (int i=0; i < 3; i++) {
            bitmap = this.cargarImagen(i);

            if (bitmap != null) {
                this.imagenes.add(bitmap);
            }
        }*/
    }

    private Bitmap cargarImagen(int nroImagen) {
        try {
            String nombreArchivo = String.format("%s_%s", Constantes.PREFIJO_NOMBRE_ARCHIVO_IMAGEN_HOME, nroImagen);
            return (UtilidadesArchivo.cargarArchivo(CrazyTowerHome.this, nombreArchivo));
        } catch (ExcepcionGeneral excepcionGeneral) {
            Log.e(CrazyTowerSplash.class.getSimpleName(), excepcionGeneral.getMessage());

            return null;
        }
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

    @Override
    protected void onDestroy() {
        super.onDestroy();

        unregisterReceiver(this.broadcastReceiverConexionSerial);
    }

    // ----------
    // AUXILIARES
    // ----------
    private void iniciarImageSwitcher() {
        this.imageSwitcher = null; //(ImageSwitcher) findViewById(R.id.imageSwitcher);
        this.imageSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
            public View makeView() {
                ImageView imageView = new ImageView(CrazyTowerHome.this);
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                return imageView;
            }
        });

        this.imageSwitcher.postDelayed(new Runnable() {
            public void run() {
                CrazyTowerHome.this.definirImagen(nroImagen++);

                if (nroImagen == CrazyTowerHome.this.obtenerCantidadImagenes()) {
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

    private void definirImagen(int nroImagen) {
        if (this.imagenes.isEmpty()) {
            this.imageSwitcher.setImageResource(this.galeriaEstatica[nroImagen]);
        } else {
            this.imageSwitcher.setImageDrawable(new BitmapDrawable(getResources(), CrazyTowerHome.this.imagenes.get(nroImagen)));
        }
    }

    private int obtenerCantidadImagenes() {
        if (this.imagenes.isEmpty()) {
            return (this.galeriaEstatica.length);
        } else {
            return (this.imagenes.size());
        }
    }

    public void cambiarPagina() {
        this.paginaActual++;

        if (paginaActual >= cantPaginas) {
            this.paginaActual = 0;
        }

        this.mPaginador.setCurrentItem(this.paginaActual);
    }

    private class HomePageOnPageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            Fragment fragment = ((HomePageAdapter) CrazyTowerHome.this.mPaginador.getAdapter()).getRegisteredFragment(position);

            if (fragment instanceof FragmentoHomeImagen) {
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        CrazyTowerHome.this.cambiarPagina();
                    }
                }, 10000);
            } else {
                ((FragmentoHomeVideo) fragment).reproducirVideo();
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    }

    private class HomePageAdapter extends FragmentPagerAdapter {
        SparseArray<Fragment> registeredFragments = new SparseArray<Fragment>();

        public HomePageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                Bundle argumentos = new Bundle();
                argumentos.putString("urlVideo", CrazyTowerHome.this.uriVideo);

                FragmentoHomeVideo fragmentoHomeVideo = new FragmentoHomeVideo();
                fragmentoHomeVideo.setArguments(argumentos);

                return fragmentoHomeVideo;
            } else {
                Bundle argumentos = new Bundle();
                argumentos.putString("urlImagen", CrazyTowerHome.this.uriVideo);

                FragmentoHomeImagen fragmentoHomeImagen = new FragmentoHomeImagen();
                fragmentoHomeImagen.setArguments(argumentos);

                return fragmentoHomeImagen;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Fragment fragment = (Fragment) super.instantiateItem(container, position);
            registeredFragments.put(position, fragment);
            return fragment;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            registeredFragments.remove(position);
            super.destroyItem(container, position, object);
        }

        public Fragment getRegisteredFragment(int position) {
            return registeredFragments.get(position);
        }
    }
}
