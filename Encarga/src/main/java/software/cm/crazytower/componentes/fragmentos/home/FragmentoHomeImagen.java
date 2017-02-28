package software.cm.crazytower.componentes.fragmentos.home;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import software.cm.crazytower.R;

public class FragmentoHomeImagen extends Fragment {
    public static final String PARAM_IMAGEN_URL = "urlImagen";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragmento_actividad_home_imagen, container, false);

        ImageView imageView = (ImageView) rootView.findViewById(R.id.imageHome);

        String urlImagen = this.getArguments().getString(PARAM_IMAGEN_URL);
        Uri uri = Uri.parse(urlImagen);
        imageView.setImageURI(uri);

        return rootView;
    }
}
