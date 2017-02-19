package software.cm.crazytower.componentes.fragmentos.encuesta;

import android.support.v4.app.Fragment;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import java.util.Iterator;
import java.util.List;

import software.cm.crazytower.actividades.encuesta.FragmentoEncuestaOpcionListener;

public abstract class FragmentoEncuesta extends Fragment  {
    private static final int CANT_MAX_OPCIONES = 10;

    private static final String PARAMETRO_TITULO = "paramTitulo";
    private static final String PARAMETRO_CANT_OPCIONES = "paramCantOpciones";
    private static final String PARAMETRO_OPCION_PREFIJO = "paramOpcion";

    protected FragmentoEncuestaOpcionListener fragmentoEncuestaOpcionListener;

    protected String obtenerTitulo() {
        return this.getArguments().getString(PARAMETRO_TITULO);
    }

    protected Integer obtenerCantOpciones() {
        return this.getArguments().getInt(PARAMETRO_CANT_OPCIONES);
    }

    protected String obtenerOpcion(Integer nroOpcion) {
        if (nroOpcion < 1 || nroOpcion > CANT_MAX_OPCIONES) {
            return null;
        }

        return this.getArguments().getString(PARAMETRO_OPCION_PREFIJO + nroOpcion);
    }

    // LISTENERS
    CompoundButton.OnCheckedChangeListener checkGeneralBotonListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                for (ToggleButton boton : FragmentoEncuesta.this.obtenerOpciones()) {
                    if (boton == buttonView) {
                        boton.setOnCheckedChangeListener(checkAvanzarPaginaBotonListener);
                    } else {
                        boton.setOnCheckedChangeListener(null);
                        boton.setChecked(false);
                        boton.setOnCheckedChangeListener(checkGeneralBotonListener);
                    }
                }
            } else {
                boolean algunBotonMarcado = this.hayBotonMarcado();

                if (!algunBotonMarcado) {
                    buttonView.setChecked(true);
                }
            }

            FragmentoEncuesta.this.enviarAccionBotonApretado(buttonView);
        }

        private boolean hayBotonMarcado() {
            Iterator<ToggleButton> itBotones = FragmentoEncuesta.this.obtenerOpciones().iterator();
            boolean algunoMarcado = false;

            while (itBotones.hasNext() && !algunoMarcado) {
                algunoMarcado = itBotones.next().isChecked();
            }

            return (algunoMarcado);
        }
    };

    CompoundButton.OnCheckedChangeListener checkAvanzarPaginaBotonListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            buttonView.setOnCheckedChangeListener(null);
            buttonView.setChecked(true);
            buttonView.setOnCheckedChangeListener(checkAvanzarPaginaBotonListener);

            FragmentoEncuesta.this.enviarAccionBotonApretado(buttonView);
        }

        private boolean hayBotonMarcado() {
            Iterator<ToggleButton> itBotones = FragmentoEncuesta.this.obtenerOpciones().iterator();
            boolean algunoMarcado = false;

            while (itBotones.hasNext() && !algunoMarcado) {
                algunoMarcado = itBotones.next().isChecked();
            }

            return (algunoMarcado);
        }
    };

    protected abstract List<ToggleButton> obtenerOpciones();

    // GETTERS AND SETTERS
    public static String getNombreParametroCantOpciones() {
        return PARAMETRO_CANT_OPCIONES;
    }

    public static String getNombreParametroTitulo() {
        return PARAMETRO_TITULO;
    }

    public static String getNombreParametroOpcion(int nroOpcion) {
        return PARAMETRO_OPCION_PREFIJO + nroOpcion;
    }

    public FragmentoEncuestaOpcionListener getFragmentoEncuestaOpcionListener() {
        return fragmentoEncuestaOpcionListener;
    }

    public FragmentoEncuesta setFragmentoEncuestaOpcionListener(FragmentoEncuestaOpcionListener fragmentoEncuestaOpcionListener) {
        this.fragmentoEncuestaOpcionListener = fragmentoEncuestaOpcionListener;
        return this;
    }

    public void enviarAccionBotonApretado(Button botonApretado) {
        if (this.fragmentoEncuestaOpcionListener != null) {
            this.fragmentoEncuestaOpcionListener.ejecutarSeleccionOpcion(botonApretado);
        }
    }
}
