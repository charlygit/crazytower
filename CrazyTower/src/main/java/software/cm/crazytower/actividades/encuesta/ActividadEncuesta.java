package software.cm.crazytower.actividades.encuesta;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import software.cm.crazytower.R;
import software.cm.crazytower.actividades.ActividadServicios;
import software.cm.crazytower.componentes.fragmentos.encuesta.FragmentoEncuesta;
import software.cm.crazytower.componentes.fragmentos.encuesta.FragmentoEncuestaInicio;
import software.cm.crazytower.componentes.fragmentos.encuesta.FragmentoEncuestaSexo;
import software.cm.crazytower.componentes.fragmentos.encuesta.FragmentoImagenSlider;
import software.cm.crazytower.componentes.fragmentos.encuesta.FragmentoEncuestaOpcionesDinamicas;

public class ActividadEncuesta extends FragmentActivity implements FragmentoEncuestaOpcionListener {
    private static final int CANT_PAGINAS = 5;
    private ViewPager mPaginador;
    private PagerAdapter mPaginadorAdapter;
    private int maximaPaginaVisitada;
    private ProgressBar mProgressBar;

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
        this.mProgressBar.setScaleY(3f);
        this.mProgressBar.getProgressDrawable().setColorFilter(
                Color.BLUE, android.graphics.PorterDuff.Mode.SRC_IN);
        this.mProgressBar.setMax(CANT_PAGINAS);

        // Configura los botones para ir hacia adelante y hacia atras
        this.configurarBotonesAdelanteYAtras();

        this.maximaPaginaVisitada = 0;
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
        mPaginador.setCurrentItem(mPaginador.getCurrentItem() + 1);
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
            } else if (position == 1) {
                fragmento = new FragmentoEncuestaOpcionesDinamicas();

                Bundle argumentos = new Bundle();
                argumentos.putInt(FragmentoEncuesta.getNombreParametroCantOpciones(), 10);
                argumentos.putString(FragmentoEncuesta.getNombreParametroTitulo(), "Marca el grupo que te identifica");
                argumentos.putString(FragmentoEncuesta.getNombreParametroOpcion(1), "Hasta 18 años");
                argumentos.putString(FragmentoEncuesta.getNombreParametroOpcion(2), "19 - 25 años");
                argumentos.putString(FragmentoEncuesta.getNombreParametroOpcion(3), "3 o más veces por semana");
                argumentos.putString(FragmentoEncuesta.getNombreParametroOpcion(4), "1-2 veces al mes");
                argumentos.putString(FragmentoEncuesta.getNombreParametroOpcion(5), "1-2 veces por semana");
                argumentos.putString(FragmentoEncuesta.getNombreParametroOpcion(6), "3 o más veces por semana");
                argumentos.putString(FragmentoEncuesta.getNombreParametroOpcion(7), "1-2 veces al mes");
                argumentos.putString(FragmentoEncuesta.getNombreParametroOpcion(8), "1-2 veces por semana");
                argumentos.putString(FragmentoEncuesta.getNombreParametroOpcion(9), "3 o más veces por semana");
                argumentos.putString(FragmentoEncuesta.getNombreParametroOpcion(10), "3 o más veces por semana");

                fragmento.setArguments(argumentos);
            } else if (position == 2) {
                fragmento = new FragmentoEncuestaOpcionesDinamicas();

                Bundle argumentos = new Bundle();
                argumentos.putInt(FragmentoEncuesta.getNombreParametroCantOpciones(), 3);
                argumentos.putString(FragmentoEncuesta.getNombreParametroTitulo(), "¿Cada cuánto haces ejercicio?");
                argumentos.putString(FragmentoEncuesta.getNombreParametroOpcion(1), "1-2 veces al mes");
                argumentos.putString(FragmentoEncuesta.getNombreParametroOpcion(2), "1-2 veces por semana");
                argumentos.putString(FragmentoEncuesta.getNombreParametroOpcion(3), "3 o más veces por semana");

                fragmento.setArguments(argumentos);
            } else if (position == 3) {
                fragmento = new FragmentoEncuestaOpcionesDinamicas();

                Bundle argumentos = new Bundle();
                argumentos.putInt(FragmentoEncuesta.getNombreParametroCantOpciones(), 4);
                argumentos.putString(FragmentoEncuesta.getNombreParametroTitulo(), "¿Qué tipo de actividad prefieres?");
                argumentos.putString(FragmentoEncuesta.getNombreParametroOpcion(1), "Caminar, correr");
                argumentos.putString(FragmentoEncuesta.getNombreParametroOpcion(2), "Gimnasia o aparatos");
                argumentos.putString(FragmentoEncuesta.getNombreParametroOpcion(3), "Yoga, Pilates, etc.");
                argumentos.putString(FragmentoEncuesta.getNombreParametroOpcion(4), "Fútbol, básquetbol, etc.");

                fragmento.setArguments(argumentos);
            } else if (position == 4) {
                fragmento = new FragmentoEncuestaOpcionesDinamicas();

                Bundle argumentos = new Bundle();
                argumentos.putInt(FragmentoEncuesta.getNombreParametroCantOpciones(), 4);
                argumentos.putString(FragmentoEncuesta.getNombreParametroTitulo(), "¿Cuánto pagarías por un par de calzados deportivos");
                argumentos.putString(FragmentoEncuesta.getNombreParametroOpcion(1), "Hasta $1.000");
                argumentos.putString(FragmentoEncuesta.getNombreParametroOpcion(2), "$2.000 a $3.500");
                argumentos.putString(FragmentoEncuesta.getNombreParametroOpcion(3), "$1.000 a $2.000");
                argumentos.putString(FragmentoEncuesta.getNombreParametroOpcion(4), "Más de $3.500");

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
                if (mPaginador.getCurrentItem() < (CANT_PAGINAS - 1)) {
                    mPaginador.setCurrentItem(mPaginador.getCurrentItem() + 1);
                } else {
                    // Se finaliza la accion de encuesta y se pasa a la accion que brinda servicios
                    Intent mainIntent = new Intent(ActividadEncuesta.this, ActividadServicios.class);
                    ActividadEncuesta.this.startActivity(mainIntent);
                    ActividadEncuesta.this.finish();
                }
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
