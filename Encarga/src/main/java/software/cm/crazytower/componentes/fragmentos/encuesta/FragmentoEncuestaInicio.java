package software.cm.crazytower.componentes.fragmentos.encuesta;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.List;

import software.cm.crazytower.R;

public class FragmentoEncuestaInicio extends FragmentoEncuesta {
    public static final String PARAM_IMAGEN = "urlImagen";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragmento_actividad_encuesta_inicio, container, false);

        ImageView imageView = (ImageView) rootView.findViewById(R.id.imagenEncuestaHome);

        String urlImagen = this.getArguments().getString(PARAM_IMAGEN);
        Uri uri = Uri.parse(urlImagen);
        imageView.setImageURI(uri);

        return rootView;
    }

    @Override
    protected List<ToggleButton> obtenerOpciones() {
        return null;
    }
}
