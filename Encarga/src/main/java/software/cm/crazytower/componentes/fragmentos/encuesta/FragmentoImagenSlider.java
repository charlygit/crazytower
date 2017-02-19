package software.cm.crazytower.componentes.fragmentos.encuesta;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.List;

import software.cm.crazytower.R;

public class FragmentoImagenSlider extends FragmentoEncuesta {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragmento_actividad_encuesta_slider, container, false);


        TextView textoFragmento = (TextView) rootView.findViewById(R.id.textoFragmento);

        textoFragmento.setText("Pagina " + this.getArguments().getInt("nroPagina"));
        return rootView;
    }

    @Override
    protected List<ToggleButton> obtenerOpciones() {
        return null;
    }
}
