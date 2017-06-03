package software.cm.crazytower.modelo;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.drive.DriveId;

import java.util.ArrayList;
import java.util.List;

public class DatosAplicacionAtenti implements Parcelable {
    private String idDispositivo;
    private String pathImagenHome;
    private String pathImagenFin;
    private List<String> pathVideos = new ArrayList<>();
    private List<String> pathImagenes = new ArrayList<>();

    private DriveId driveId;

    public DatosAplicacionAtenti() {
    }

    protected DatosAplicacionAtenti(Parcel in) {
        idDispositivo = in.readString();
        pathImagenHome = in.readString();
        pathImagenFin = in.readString();
        pathVideos = in.createStringArrayList();
        pathImagenes = in.createStringArrayList();
        driveId = in.readParcelable(DriveId.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(idDispositivo);
        dest.writeString(pathImagenHome);
        dest.writeString(pathImagenFin);
        dest.writeStringList(pathVideos);
        dest.writeStringList(pathImagenes);
        dest.writeParcelable(driveId, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<DatosAplicacionAtenti> CREATOR = new Creator<DatosAplicacionAtenti>() {
        @Override
        public DatosAplicacionAtenti createFromParcel(Parcel in) {
            return new DatosAplicacionAtenti(in);
        }

        @Override
        public DatosAplicacionAtenti[] newArray(int size) {
            return new DatosAplicacionAtenti[size];
        }
    };

    public void agregarPathImagen(String pathImagen) {
        this.pathImagenes.add(pathImagen);
    }

    public void agregarPathVideo(String pathVideo) {
        this.pathVideos.add(pathVideo);
    }

    public String getPathImagenHome() {
        return pathImagenHome;
    }

    public DatosAplicacionAtenti setPathImagenHome(String pathImagenHome) {
        this.pathImagenHome = pathImagenHome;
        return this;
    }

    public String getPathImagenFin() {
        return pathImagenFin;
    }

    public DatosAplicacionAtenti setPathImagenFin(String pathImagenFin) {
        this.pathImagenFin = pathImagenFin;
        return this;
    }

    public List<String> getPathVideos() {
        return pathVideos;
    }

    public DatosAplicacionAtenti setPathVideos(List<String> pathVideos) {
        this.pathVideos = pathVideos;
        return this;
    }

    public List<String> getPathImagenes() {
        return pathImagenes;
    }

    public DatosAplicacionAtenti setPathImagenes(List<String> pathImagenes) {
        this.pathImagenes = pathImagenes;
        return this;
    }

    public String getIdDispositivo() {
        return idDispositivo;
    }

    public void setIdDispositivo(String idDispositivo) {
        this.idDispositivo = idDispositivo;
    }

    public DriveId getDriveId() {
        return driveId;
    }

    public DatosAplicacionAtenti setDriveId(DriveId driveId) {
        this.driveId = driveId;
        return this;
    }
}
