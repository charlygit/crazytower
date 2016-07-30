package software.cm.crazytower.modelo;

import android.os.Parcel;
import android.os.Parcelable;

public class Nodo implements Parcelable{
    private String ip;
    private String mac;

    public Nodo() {
    }

    public Nodo(String ip, String mac) {
        this.ip = ip;
        this.mac = mac;
    }

    public String getIp() {
        return ip;
    }

    public Nodo setIp(String ip) {
        this.ip = ip;
        return this;
    }

    public String getMac() {
        return mac;
    }

    public Nodo setMac(String mac) {
        this.mac = mac;
        return this;
    }

    // PARCELEABLE
    public Nodo(Parcel in){
        this.ip = in.readString();
        this.mac = in.readString();
    }

    @Override
    public int describeContents(){
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.ip);
        dest.writeString(this.mac);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Nodo createFromParcel(Parcel in) {
            return new Nodo(in);
        }

        public Nodo[] newArray(int size) {
            return new Nodo[size];
        }
    };
}
