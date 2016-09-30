package software.cm.crazytower.componentes.fragmentos.encuesta;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import java.util.List;

import software.cm.crazytower.R;

public class FragmentoEncuestaSexo extends FragmentoEncuesta {
    private ToggleButton botonSexoM12A30;
    private ToggleButton botonSexoM3045;
    private ToggleButton botonSexoM45Mas;

    private ToggleButton botonSexoF12A30;
    private ToggleButton botonSexoF3045;
    private ToggleButton botonSexoF45Mas;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragmento_actividad_encuesta_sexo, container, false);

        this.botonSexoM12A30 = (ToggleButton) rootView.findViewById(R.id.sexoM12_30);
        this.botonSexoM12A30.setOnCheckedChangeListener(checkBotonListener);

        this.botonSexoM3045 = (ToggleButton) rootView.findViewById(R.id.sexoM30_45);
        this.botonSexoM3045.setOnCheckedChangeListener(checkBotonListener);

        this.botonSexoM45Mas = (ToggleButton) rootView.findViewById(R.id.sexoM45);
        this.botonSexoM45Mas.setOnCheckedChangeListener(checkBotonListener);

        this.botonSexoF12A30 = (ToggleButton) rootView.findViewById(R.id.sexoF12_30);
        this.botonSexoF12A30.setOnCheckedChangeListener(checkBotonListener);

        this.botonSexoF3045 = (ToggleButton) rootView.findViewById(R.id.sexoF30_45);
        this.botonSexoF3045.setOnCheckedChangeListener(checkBotonListener);

        this.botonSexoF45Mas = (ToggleButton) rootView.findViewById(R.id.sexoF45);
        this.botonSexoF45Mas.setOnCheckedChangeListener(checkBotonListener);

        return rootView;
    }

    CompoundButton.OnCheckedChangeListener checkBotonListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked){
                botonSexoM12A30.setChecked(buttonView == botonSexoM12A30);
                botonSexoM3045.setChecked(buttonView == botonSexoM3045);
                botonSexoM45Mas.setChecked(buttonView == botonSexoM45Mas);

                botonSexoF12A30.setChecked(buttonView == botonSexoF12A30);
                botonSexoF3045.setChecked(buttonView == botonSexoF3045);
                botonSexoF45Mas.setChecked(buttonView == botonSexoF45Mas);

                FragmentoEncuestaSexo.this.enviarAccionBotonApretado(buttonView);
            }
        }
    };

    @Override
    protected List<ToggleButton> obtenerOpciones() {
        return null;
    }
}