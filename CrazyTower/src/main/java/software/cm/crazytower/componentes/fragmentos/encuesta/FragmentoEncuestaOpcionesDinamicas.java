package software.cm.crazytower.componentes.fragmentos.encuesta;

import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import software.cm.crazytower.R;

import static android.R.attr.typeface;

public class FragmentoEncuestaOpcionesDinamicas extends FragmentoEncuesta {
    private enum AccionListener {AGREGAR, QUITAR};

    private static Map<Integer, Integer> mapCantOpcionesFragmentoLayout;
    private static Map<Integer, Integer> mapNroOpcionBotonCorrespondiente;
    private LinkedList<ToggleButton> listaOpciones;

    static {
        // Inicializa el map con el layout correspondiente para cada determinada cantidad de opciones
        mapCantOpcionesFragmentoLayout = new HashMap<>();
        mapCantOpcionesFragmentoLayout.put(3, R.layout.fragmento_actividad_encuesta_3_preguntas);
        mapCantOpcionesFragmentoLayout.put(4, R.layout.fragmento_actividad_encuesta_4_preguntas);
        mapCantOpcionesFragmentoLayout.put(5, R.layout.fragmento_actividad_encuesta_5_preguntas);
        mapCantOpcionesFragmentoLayout.put(10, R.layout.fragmento_actividad_encuesta_10_preguntas);

        // Inicializa el map con el boton correspondiente a cada nro. de opcion
        mapNroOpcionBotonCorrespondiente = new HashMap<>();
        mapNroOpcionBotonCorrespondiente.put(1, R.id.paginaEncuestaOpcion1);
        mapNroOpcionBotonCorrespondiente.put(2, R.id.paginaEncuestaOpcion2);
        mapNroOpcionBotonCorrespondiente.put(3, R.id.paginaEncuestaOpcion3);
        mapNroOpcionBotonCorrespondiente.put(4, R.id.paginaEncuestaOpcion4);
        mapNroOpcionBotonCorrespondiente.put(5, R.id.paginaEncuestaOpcion5);
        mapNroOpcionBotonCorrespondiente.put(6, R.id.paginaEncuestaOpcion6);
        mapNroOpcionBotonCorrespondiente.put(7, R.id.paginaEncuestaOpcion7);
        mapNroOpcionBotonCorrespondiente.put(8, R.id.paginaEncuestaOpcion8);
        mapNroOpcionBotonCorrespondiente.put(9, R.id.paginaEncuestaOpcion9);
        mapNroOpcionBotonCorrespondiente.put(10, R.id.paginaEncuestaOpcion10);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        int cantOpciones = this.obtenerCantOpciones();

        ViewGroup rootView = (ViewGroup) inflater.inflate(
                mapCantOpcionesFragmentoLayout.get(cantOpciones), container, false);

        // Se inicializan los botones
        ToggleButton botonOpcion;
        this.listaOpciones = new LinkedList<>();

        TextView textTitulo = (TextView) rootView.findViewById(R.id.paginaEncuestaPregunta);
        textTitulo.setText(this.obtenerTitulo());
        String textoOpcion;

        Typeface custom_font = Typeface.createFromAsset(rootView.getContext().getAssets(),  "fonts/ufonts.com_gillsans.ttf");

        for (int nroOpcion = 1; nroOpcion <= cantOpciones; nroOpcion++) {
            botonOpcion = (ToggleButton) rootView.findViewById(mapNroOpcionBotonCorrespondiente.get(nroOpcion));
            botonOpcion.setOnCheckedChangeListener(this.checkGeneralBotonListener);
            textoOpcion = this.obtenerOpcion(nroOpcion);

            botonOpcion.setText(textoOpcion);
            botonOpcion.setTextOn(textoOpcion);
            botonOpcion.setTextOff(textoOpcion);

            botonOpcion.setTypeface(custom_font);

            listaOpciones.add(botonOpcion);
        }

        return rootView;
    }

    @Override
    protected List<ToggleButton> obtenerOpciones() {
        return this.listaOpciones;
    }
}
