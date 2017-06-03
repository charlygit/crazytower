package software.cm.crazytower.modelo.encuesta;

import android.os.Parcel;
import android.os.Parcelable;

public class RespuestaPregunta implements Parcelable {
    private String pregunta;
    private String respuesta;

    public RespuestaPregunta() {
    }

    public RespuestaPregunta(String pregunta, String respuesta) {
        this.pregunta = pregunta;
        this.respuesta = respuesta;
    }

    public String getPregunta() {
        return pregunta;
    }

    public RespuestaPregunta setPregunta(String pregunta) {
        this.pregunta = pregunta;
        return this;
    }

    public String getRespuesta() {
        return respuesta;
    }

    public RespuestaPregunta setRespuesta(String respuesta) {
        this.respuesta = respuesta;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RespuestaPregunta that = (RespuestaPregunta) o;

        return pregunta.equals(that.pregunta);

    }

    @Override
    public int hashCode() {
        return pregunta.hashCode();
    }

    // ------------------------
    // PARCEABLE IMPLEMENTACION
    // ------------------------
    protected RespuestaPregunta(Parcel in) {
        pregunta = in.readString();
        respuesta = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(pregunta);
        dest.writeString(respuesta);
    }

    public static final Creator<RespuestaPregunta> CREATOR = new Creator<RespuestaPregunta>() {
        @Override
        public RespuestaPregunta createFromParcel(Parcel in) {
            return new RespuestaPregunta(in);
        }

        @Override
        public RespuestaPregunta[] newArray(int size) {
            return new RespuestaPregunta[size];
        }
    };
}
