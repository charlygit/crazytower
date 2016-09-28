package software.cm.crazytower.componentes.fragmentos.encuesta;

import android.support.v4.app.Fragment;
import android.widget.Button;

import software.cm.crazytower.actividades.encuesta.FragmentoEncuestaOpcionListener;

public abstract class FragmentoEncuesta extends Fragment  {
    private static final int CANT_MAX_OPCIONES = 8;

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
