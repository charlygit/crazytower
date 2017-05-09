package software.cm.crazytower.actividades;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.ViewGroup;

import java.util.Timer;
import java.util.TimerTask;

import software.cm.crazytower.R;
import software.cm.crazytower.actividades.encuesta.ActividadEncuesta;
import software.cm.crazytower.arduino.ControladorArduino;
import software.cm.crazytower.componentes.BroadcastReceiverConexionSerial;
import software.cm.crazytower.componentes.fragmentos.home.FragmentoHomeImagen;
import software.cm.crazytower.componentes.fragmentos.home.FragmentoHomeVideo;

public class CrazyTowerHome extends ActividadBaseEncarga {
    private BroadcastReceiverConexionSerial broadcastReceiverConexionSerial;
    private ViewPager mPaginador;
    private FragmentPagerAdapter mPaginadorAdapter;
    private int cantPaginas;
    private int paginaActual = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.cantPaginas = this.archivosDescargadosAtenti.getPathImagenes().size() +
                this.archivosDescargadosAtenti.getPathVideos().size();

        setContentView(R.layout.activity_crazytower_homescreen);

        this.mPaginador = (ViewPager) findViewById(R.id.pagerHome);
        this.mPaginador.setOffscreenPageLimit(this.cantPaginas);
        this.mPaginador.addOnPageChangeListener(new HomePageOnPageChangeListener());
        this.mPaginadorAdapter = new HomePageAdapter(getSupportFragmentManager());
        this.mPaginador.setAdapter(mPaginadorAdapter);
        this.mPaginador.beginFakeDrag();

        // Inicia el servicio de monitoreo de conexiones
        // Todo: Eliminar comentario del servicio
        //this.startService(new Intent(this, ServicioMonitoreoConexiones.class));

        // Inicia y registra arduino broadcast receiver
        this.broadcastReceiverConexionSerial = new BroadcastReceiverConexionSerial();
        IntentFilter filter = ControladorArduino.crearFiltroArduinoBroadcastReceiver();

        registerReceiver(this.broadcastReceiverConexionSerial, filter);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Intent mainIntent = new Intent(CrazyTowerHome.this, ActividadEncuesta.class);
        this.cambiarActividadAtenti(mainIntent);

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
            // Si no hay imagenes, solo se muestran videos (tiene que haber al menos uno)
            if (CrazyTowerHome.this.archivosDescargadosAtenti.getPathImagenes().isEmpty()) {
                return this.crearFragmentoVideo(
                        CrazyTowerHome.this.archivosDescargadosAtenti.getPathVideos().get(position));
            }

            // Si no hay videos, solo se muestran imagenes (tiene que haber al menos una)
            if (CrazyTowerHome.this.archivosDescargadosAtenti.getPathVideos().isEmpty()) {
                return this.crearFragmentoImagen(
                        CrazyTowerHome.this.archivosDescargadosAtenti.getPathImagenes().get(position));
            }

            boolean esImagen = position % 2 == 0;
            if (esImagen) {
                return this.crearFragmentoImagen(
                        CrazyTowerHome.this.archivosDescargadosAtenti.getPathImagenes().get(position / 2));
            } else {
                return this.crearFragmentoVideo(
                        CrazyTowerHome.this.archivosDescargadosAtenti.getPathVideos().get(position/2));
            }
        }

        @NonNull
        private Fragment crearFragmentoImagen(String imagen) {
            Bundle argumentos = new Bundle();
            argumentos.putString(FragmentoHomeImagen.PARAM_IMAGEN_URL, imagen);

            FragmentoHomeImagen fragmentoHomeImagen = new FragmentoHomeImagen();
            fragmentoHomeImagen.setArguments(argumentos);

            return fragmentoHomeImagen;
        }

        @NonNull
        private Fragment crearFragmentoVideo(String video) {
            Bundle argumentos = new Bundle();
            argumentos.putString(FragmentoHomeVideo.PARAM_VIDEO_URL, video);

            FragmentoHomeVideo fragmentoHomeVideo = new FragmentoHomeVideo();
            fragmentoHomeVideo.setArguments(argumentos);

            return fragmentoHomeVideo;
        }

        @Override
        public int getCount() {
            return CrazyTowerHome.this.cantPaginas;
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
