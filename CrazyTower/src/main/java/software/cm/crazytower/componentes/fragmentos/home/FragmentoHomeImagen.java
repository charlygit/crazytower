package software.cm.crazytower.componentes.fragmentos.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Timer;
import java.util.TimerTask;

import software.cm.crazytower.R;
import software.cm.crazytower.actividades.CrazyTowerHome;

public class FragmentoHomeImagen extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragmento_actividad_home_imagen, container, false);

        return rootView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        /*if (this.isVisible() && isVisibleToUser) {
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    ((CrazyTowerHome) FragmentoHomeImagen.this.getActivity()).cambiarPagina();
                }
            }, 5000);
        }*/
    }
}
