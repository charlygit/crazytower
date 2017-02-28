package software.cm.crazytower.modelo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class ArchivosDescargadosAtenti implements Parcelable {
    private String pathImagenHome;
    private String pathImagenFin;
    private List<String> pathVideos = new ArrayList<>();
    private List<String> pathImagenes = new ArrayList<>();

    public ArchivosDescargadosAtenti() {
    }

    protected ArchivosDescargadosAtenti(Parcel in) {
        pathImagenHome = in.readString();
        pathImagenFin = in.readString();
        pathVideos = in.createStringArrayList();
        pathImagenes = in.createStringArrayList();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(pathImagenHome);
        dest.writeString(pathImagenFin);
        dest.writeStringList(pathVideos);
        dest.writeStringList(pathImagenes);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ArchivosDescargadosAtenti> CREATOR = new Creator<ArchivosDescargadosAtenti>() {
        @Override
        public ArchivosDescargadosAtenti createFromParcel(Parcel in) {
            return new ArchivosDescargadosAtenti(in);
        }

        @Override
        public ArchivosDescargadosAtenti[] newArray(int size) {
            return new ArchivosDescargadosAtenti[size];
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

    public ArchivosDescargadosAtenti setPathImagenHome(String pathImagenHome) {
        this.pathImagenHome = pathImagenHome;
        return this;
    }

    public String getPathImagenFin() {
        return pathImagenFin;
    }

    public ArchivosDescargadosAtenti setPathImagenFin(String pathImagenFin) {
        this.pathImagenFin = pathImagenFin;
        return this;
    }

    public List<String> getPathVideos() {
        return pathVideos;
    }

    public ArchivosDescargadosAtenti setPathVideos(List<String> pathVideos) {
        this.pathVideos = pathVideos;
        return this;
    }

    public List<String> getPathImagenes() {
        return pathImagenes;
    }

    public ArchivosDescargadosAtenti setPathImagenes(List<String> pathImagenes) {
        this.pathImagenes = pathImagenes;
        return this;
    }
}
