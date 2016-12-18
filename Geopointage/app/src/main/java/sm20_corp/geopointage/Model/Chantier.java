package sm20_corp.geopointage.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by gun on 04/12/2016.
 * Geopointage
 */

public class Chantier implements Parcelable {

    @SerializedName("address")
    private String address;
    @SerializedName("login")
    private String iotp;
    @SerializedName("latitude")
    private double lat;
    @SerializedName("longitude")
    private double lng;

    public Chantier(String address, String iotp) {
        this.address = address;
        this.iotp = iotp;
    }

    public Chantier(String iotp, double lat, double lng) {
        this.iotp = iotp;
        this.lat = lat;
        this.lng = lng;
    }

    public Chantier() {
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getIotp() {
        return iotp;
    }

    public void setIotp(String iotp) {
        this.iotp = iotp;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    @Override
    public String toString() {
        return "Chantier{" +
                "address='" + address + '\'' +
                ", iotp='" + iotp + '\'' +
                ", lat=" + lat +
                ", lng=" + lng +
                '}';
    }


    /* everything below here is for implementing Parcelable */

    // 99.9% of the time you can just ignore this
    @Override
    public int describeContents() {
        return 0;
    }

    // write your object's data to the passed-in Parcel
    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(address);
        out.writeString(iotp);
        out.writeDouble(lat);
        out.writeDouble(lng);
    }

    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<Chantier> CREATOR = new Parcelable.Creator<Chantier>() {
        public Chantier createFromParcel(Parcel in) {
            return new Chantier(in);
        }

        public Chantier[] newArray(int size) {
            return new Chantier[size];
        }
    };
    // example constructor that takes a Parcel and gives you an object populated with it's values
    private Chantier(Parcel in) {
        address = in.readString();
        iotp = in.readString();
        lat = in.readDouble();
        lng = in.readDouble();
    }
}
