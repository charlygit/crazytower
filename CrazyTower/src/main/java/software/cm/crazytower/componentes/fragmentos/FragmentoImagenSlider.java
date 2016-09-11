package software.cm.crazytower.componentes.fragmentos;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
}
