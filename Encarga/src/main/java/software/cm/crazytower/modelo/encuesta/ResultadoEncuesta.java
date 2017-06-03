package software.cm.crazytower.modelo.encuesta;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class ResultadoEncuesta implements Parcelable {
    private ArrayList<RespuestaPregunta> respuestaPreguntas = new ArrayList<>();

    public ResultadoEncuesta() {
    }

    public void procesarRespuesta(String pregunta, String respuesta) {
        if (this.existeRespuestaAPregunta(pregunta)) {
            this.actualizarRespuesta(pregunta, respuesta);
        } else {
            this.agregarRespuesta(pregunta, respuesta);
        }
    }

    public boolean existeRespuestaAPregunta(String pregunta) {
        return this.respuestaPreguntas.contains(
                new RespuestaPregunta()
                        .setPregunta(pregunta)
        );
    }

    public void agregarRespuesta(String pregunta, String respuesta) {
        this.respuestaPreguntas.add(
            new RespuestaPregunta()
                .setPregunta(pregunta)
                .setRespuesta(respuesta)
        );
    }

    public void agregarRespuesta(RespuestaPregunta respuestaPregunta) {
        this.respuestaPreguntas.add(respuestaPregunta);
    }

    public void actualizarRespuesta(String pregunta, String respuesta) {
        this.actualizarRespuesta(
            new RespuestaPregunta()
                .setPregunta(pregunta)
                .setRespuesta(respuesta)
        );
    }

    private void actualizarRespuesta(RespuestaPregunta respuestaPregunta) {
        this.respuestaPreguntas.get(this.respuestaPreguntas.indexOf(respuestaPregunta))
                .setRespuesta(respuestaPregunta.getRespuesta());
    }

    // ------------------------
    // PARCEABLE IMPLEMENTACION
    // ------------------------
    protected ResultadoEncuesta(Parcel in) {
        respuestaPreguntas = in.createTypedArrayList(RespuestaPregunta.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(respuestaPreguntas);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ResultadoEncuesta> CREATOR = new Creator<ResultadoEncuesta>() {
        @Override
        public ResultadoEncuesta createFromParcel(Parcel in) {
            return new ResultadoEncuesta(in);
        }

        @Override
        public ResultadoEncuesta[] newArray(int size) {
            return new ResultadoEncuesta[size];
        }
    };
}
