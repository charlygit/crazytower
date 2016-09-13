package software.cm.crazytower.componentes.fragmentos.encuesta;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import software.cm.crazytower.R;

public class FragmentoEncuestaOpcionesDinamicas extends FragmentoEncuesta {
    private static Map<Integer, Integer> mapCantOpcionesFragmentoLayout;
    private static Map<Integer, Integer> mapNroOpcionBotonCorrespondiente;
    private LinkedList<ToggleButton> listaOpciones;

    static {
        // Inicializa el map con el layout correspondiente para cada determinada cantidad de opciones
        mapCantOpcionesFragmentoLayout = new HashMap<>();
        mapCantOpcionesFragmentoLayout.put(3, R.layout.fragmento_actividad_encuesta_3_preguntas);
        mapCantOpcionesFragmentoLayout.put(4, R.layout.fragmento_actividad_encuesta_4_preguntas);

        // Inicializa el map con el boton correspondiente a cada nro. de opcion
        mapNroOpcionBotonCorrespondiente = new HashMap<>();
        mapNroOpcionBotonCorrespondiente.put(1, R.id.paginaEncuestaOpcion1);
        mapNroOpcionBotonCorrespondiente.put(2, R.id.paginaEncuestaOpcion2);
        mapNroOpcionBotonCorrespondiente.put(3, R.id.paginaEncuestaOpcion3);
        mapNroOpcionBotonCorrespondiente.put(4, R.id.paginaEncuestaOpcion4);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Integer cantOpciones = this.obtenerCantOpciones();

        ViewGroup rootView = (ViewGroup) inflater.inflate(
                mapCantOpcionesFragmentoLayout.get(cantOpciones), container, false);

        // Se inicializan los botones
        ToggleButton botonOpcion;
        this.listaOpciones = new LinkedList<>();

        TextView textTitulo = (TextView) rootView.findViewById(R.id.paginaEncuestaPregunta);
        textTitulo.setText(this.obtenerTitulo());

        for (int nroOpcion = 1; nroOpcion <= cantOpciones; nroOpcion++) {
            botonOpcion = (ToggleButton) rootView.findViewById(mapNroOpcionBotonCorrespondiente.get(nroOpcion));
            botonOpcion.setOnCheckedChangeListener(checkBotonListener);
            botonOpcion.setText(this.obtenerOpcion(nroOpcion));
            botonOpcion.setTextOn(this.obtenerOpcion(nroOpcion));
            botonOpcion.setTextOff(this.obtenerOpcion(nroOpcion));

            listaOpciones.add(botonOpcion);
        }

        return rootView;
    }

    CompoundButton.OnCheckedChangeListener checkBotonListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked){
                for (ToggleButton boton : FragmentoEncuestaOpcionesDinamicas.this.listaOpciones) {
                    boton.setChecked(boton == buttonView);
                }
            }
        }
    };
}
