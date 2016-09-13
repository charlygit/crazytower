package software.cm.crazytower.componentes.fragmentos.encuesta;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import software.cm.crazytower.R;

public class FragmentoEncuestaInicio extends FragmentoEncuesta {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragmento_actividad_encuesta_inicio, container, false);

        return rootView;
    }
}
