package software.cm.crazytower.actividades.encuesta;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import software.cm.crazytower.R;
import software.cm.crazytower.actividades.ActividadBaseEncarga;
import software.cm.crazytower.actividades.ActividadServicios;
import software.cm.crazytower.componentes.BroadcastReceiverConexionSerial;
import software.cm.crazytower.componentes.fragmentos.encuesta.FragmentoEncuesta;
import software.cm.crazytower.componentes.fragmentos.encuesta.FragmentoEncuestaInicio;
import software.cm.crazytower.componentes.fragmentos.encuesta.FragmentoEncuestaOpcionesDinamicas;
import software.cm.crazytower.componentes.fragmentos.encuesta.FragmentoImagenSlider;

public class ActividadEncuesta extends ActividadBaseEncarga implements FragmentoEncuestaOpcionListener {
    private static final int CANT_PAGINAS = 5;
    private ViewPager mPaginador;
    private PagerAdapter mPaginadorAdapter;
    private int maximaPaginaVisitada;
    private ProgressBar mProgressBar;
    private BroadcastReceiverConexionSerial broadcastReceiverConexionSerial;

    // Botones adelante y atras
    private Button botonAdelante;
    private Button botonAtras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actividad_encuesta);

        this.mPaginador = (ViewPager) findViewById(R.id.pager);
        this.mPaginador.addOnPageChangeListener(new EncuestaOnPageChangeListener());
        this.mPaginador.setOffscreenPageLimit(10);
        this.mPaginadorAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        this.mPaginador.setAdapter(mPaginadorAdapter);
        this.mPaginador.beginFakeDrag();

        this.mProgressBar = (ProgressBar) findViewById(R.id.progressBar);

        this.mProgressBar.setMax(CANT_PAGINAS);

        // Configura los botones para ir hacia adelante y hacia atras
        this.configurarBotonesAdelanteYAtras();

        this.maximaPaginaVisitada = 0;

        // Inicia y registra arduino broadcast receiver
        /*this.broadcastReceiverConexionSerial = new BroadcastReceiverConexionSerial();
        IntentFilter filter = ControladorArduino.crearFiltroArduinoBroadcastReceiver();

        registerReceiver(this.broadcastReceiverConexionSerial, filter);*/
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //unregisterReceiver(this.broadcastReceiverConexionSerial);
    }

    @Override
    public void onBackPressed() {
        if (mPaginador.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            this.mPaginador.setCurrentItem(mPaginador.getCurrentItem() - 1);
        }
    }

    @Override
    public void ejecutarSeleccionOpcion(Button botonApretado) {
        if (this.mPaginador.getCurrentItem() < (CANT_PAGINAS - 1)) {
            this.mPaginador.setCurrentItem(this.mPaginador.getCurrentItem() + 1);
        } else {
            // Se finaliza la accion de encuesta y se pasa a la accion que brinda servicios
            Intent mainIntent = new Intent(ActividadEncuesta.this, ActividadServicios.class);
            this.cambiarActividadAtenti(mainIntent);
        }
    }


    private class EncuestaOnPageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            if (position > ActividadEncuesta.this.maximaPaginaVisitada) {
                ActividadEncuesta.this.maximaPaginaVisitada = position;
            }

            ActividadEncuesta.this.configurarVisibilidadBotones(position);
            ActividadEncuesta.this.mProgressBar.setProgress(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    }

    private class ScreenSlidePagerAdapter extends FragmentPagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            FragmentoEncuesta fragmento;

            if (position == 0) {
                fragmento = new FragmentoEncuestaInicio();

                Bundle argumentos = new Bundle();
                argumentos.putString(FragmentoEncuestaInicio.PARAM_IMAGEN,
                        ActividadEncuesta.this.archivosDescargadosAtenti.getPathImagenHome());

                fragmento.setArguments(argumentos);
            } else if (position == 1) {
                fragmento = new FragmentoEncuestaOpcionesDinamicas();

                Bundle argumentos = new Bundle();
                argumentos.putInt(FragmentoEncuesta.getNombreParametroCantOpciones(), 10);
                argumentos.putString(FragmentoEncuesta.getNombreParametroTitulo(), "Marca el grupo que te identifica");
                argumentos.putString(FragmentoEncuesta.getNombreParametroOpcion(1), "Hasta 18 años");
                argumentos.putString(FragmentoEncuesta.getNombreParametroOpcion(2), "19 - 25 años");
                argumentos.putString(FragmentoEncuesta.getNombreParametroOpcion(3), "26 - 35 años");
                argumentos.putString(FragmentoEncuesta.getNombreParametroOpcion(4), "36 - 45 años");
                argumentos.putString(FragmentoEncuesta.getNombreParametroOpcion(5), "más de 45 años");
                argumentos.putString(FragmentoEncuesta.getNombreParametroOpcion(6), "Hasta 18 años");
                argumentos.putString(FragmentoEncuesta.getNombreParametroOpcion(7), "19 - 25 años");
                argumentos.putString(FragmentoEncuesta.getNombreParametroOpcion(8), "26 - 35 años");
                argumentos.putString(FragmentoEncuesta.getNombreParametroOpcion(9), "36 - 45 años");
                argumentos.putString(FragmentoEncuesta.getNombreParametroOpcion(10), "más de 45 años");

                fragmento.setArguments(argumentos);
            } else if (position == 2) {
                fragmento = new FragmentoEncuestaOpcionesDinamicas();

                Bundle argumentos = new Bundle();
                argumentos.putInt(FragmentoEncuesta.getNombreParametroCantOpciones(), 4);
                argumentos.putString(FragmentoEncuesta.getNombreParametroTitulo(), "¿Con qué frecuencia realizas ejercicio?");
                argumentos.putString(FragmentoEncuesta.getNombreParametroOpcion(1), "1 vez por semana");
                argumentos.putString(FragmentoEncuesta.getNombreParametroOpcion(2), "2 veces por semana");
                argumentos.putString(FragmentoEncuesta.getNombreParametroOpcion(3), "3 veces por semana");
                argumentos.putString(FragmentoEncuesta.getNombreParametroOpcion(4), "Todos los días");

                fragmento.setArguments(argumentos);
            } else if (position == 3) {
                fragmento = new FragmentoEncuestaOpcionesDinamicas();

                Bundle argumentos = new Bundle();
                argumentos.putInt(FragmentoEncuesta.getNombreParametroCantOpciones(), 5);
                argumentos.putString(FragmentoEncuesta.getNombreParametroTitulo(), "¿Qué tipo de actividad preferís?");
                argumentos.putString(FragmentoEncuesta.getNombreParametroOpcion(1), "Caminar o correr");
                argumentos.putString(FragmentoEncuesta.getNombreParametroOpcion(2), "Aérobica o pesas");
                argumentos.putString(FragmentoEncuesta.getNombreParametroOpcion(3), "Fútbol");
                argumentos.putString(FragmentoEncuesta.getNombreParametroOpcion(4), "Bicicleta");
                argumentos.putString(FragmentoEncuesta.getNombreParametroOpcion(5), "Otros");

                fragmento.setArguments(argumentos);
            } else if (position == 4) {
                fragmento = new FragmentoEncuestaOpcionesDinamicas();

                Bundle argumentos = new Bundle();
                argumentos.putInt(FragmentoEncuesta.getNombreParametroCantOpciones(), 4);
                argumentos.putString(FragmentoEncuesta.getNombreParametroTitulo(), "¿Qué tipo de calzado deportivo preferís");
                argumentos.putString(FragmentoEncuesta.getNombreParametroOpcion(1), "Adidas");
                argumentos.putString(FragmentoEncuesta.getNombreParametroOpcion(2), "Nike");
                argumentos.putString(FragmentoEncuesta.getNombreParametroOpcion(3), "Puma");
                argumentos.putString(FragmentoEncuesta.getNombreParametroOpcion(4), "Saucony");

                fragmento.setArguments(argumentos);
            } else {
                fragmento = new FragmentoImagenSlider();

                Bundle bundle = new Bundle();
                bundle.putInt("nroPagina", position);
                fragmento.setArguments(bundle);
            }

            // Se define el listener del fragmento, para capturar los touch en los botones
            fragmento.setFragmentoEncuestaOpcionListener(ActividadEncuesta.this);

            return fragmento;
        }

        @Override
        public int getCount() {
            return CANT_PAGINAS;
        }
    }

    private void configurarBotonesAdelanteYAtras() {
        this.botonAdelante = (Button) findViewById(R.id.botonAdelante);

        this.botonAtras = (Button) findViewById(R.id.botonAtras);
        this.botonAtras.setVisibility(View.INVISIBLE);

        this.botonAdelante.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPaginador.setCurrentItem(mPaginador.getCurrentItem() + 1);

                /*if (mPaginador.getCurrentItem() < (CANT_PAGINAS - 1)) {
                    mPaginador.setCurrentItem(mPaginador.getCurrentItem() + 1);
                } else {
                    // Se finaliza la accion de encuesta y se pasa a la accion que brinda servicios
                    Intent mainIntent = new Intent(ActividadEncuesta.this, ActividadServicios.class);
                    ActividadEncuesta.this.startActivity(mainIntent);
                    ActividadEncuesta.this.finish();
                }*/
            }
        });

        this.botonAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPaginador.getCurrentItem() > 0) {
                    mPaginador.setCurrentItem(mPaginador.getCurrentItem() -1);
                }
            }
        });
    }

    private void configurarVisibilidadBotones(int position) {
        if (position == 0) {
            botonAtras.setVisibility(View.INVISIBLE);
            botonAdelante.setVisibility(View.VISIBLE);
        } else {
            botonAtras.setVisibility(View.VISIBLE);
            botonAdelante.setVisibility(position < this.maximaPaginaVisitada? View.VISIBLE : View.INVISIBLE);
        }
    }
}
