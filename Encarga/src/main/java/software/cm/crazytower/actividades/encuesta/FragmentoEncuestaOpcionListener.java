package software.cm.crazytower.actividades.encuesta;

import android.widget.Button;

public interface FragmentoEncuestaOpcionListener {
    void procesarRespuesta(String pregunta, String respuesta);
    void ejecutarSeleccionOpcion(Button botonApretado);
}
