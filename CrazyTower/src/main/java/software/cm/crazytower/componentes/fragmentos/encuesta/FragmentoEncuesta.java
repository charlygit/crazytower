package software.cm.crazytower.componentes.fragmentos.encuesta;

import android.support.v4.app.Fragment;

public abstract class FragmentoEncuesta extends Fragment  {
    private static final int CANT_MAX_OPCIONES = 8;

    private static final String PARAMETRO_TITULO = "paramTitulo";
    private static final String PARAMETRO_CANT_OPCIONES = "paramCantOpciones";
    private static final String PARAMETRO_OPCION_PREFIJO = "paramOpcion";

    /*public static final String PARAMETRO_OPCION_1 = "paramOpcion1";
    public static final String PARAMETRO_OPCION_2 = "paramOpcion2";
    public static final String PARAMETRO_OPCION_3 = "paramOpcion3";
    public static final String PARAMETRO_OPCION_4 = "paramOpcion4";
    public static final String PARAMETRO_OPCION_5 = "paramOpcion5";
    public static final String PARAMETRO_OPCION_6 = "paramOpcion6";
    public static final String PARAMETRO_OPCION_7 = "paramOpcion7";
    public static final String PARAMETRO_OPCION_8 = "paramOpcion8";*/

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
}
