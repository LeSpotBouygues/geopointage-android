package sm20_corp.geopointage.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by gun on 02/11/2016.
 * Geopointage
 */

public class User implements Parcelable{
    @SerializedName("firstName")
    private String firstName;
    @SerializedName("lastName")
    private String lastName;
    @SerializedName("registrationNumber")
    private String id;
    private boolean visibility = true;
    private int place = -1;

    public User(String lastName, String firstName, String id, int place) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.id = id;
        this.place = place;
        this.visibility = true;
    }

    public User(String lastName, String firstName, String id) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.id = id;
        this.visibility = true;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
        out.writeString(lastName);
        out.writeString(firstName);
        out.writeString(id);
        out.writeByte((byte) (visibility ? 1 : 0));
    }

    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };
    // example constructor that takes a Parcel and gives you an object populated with it's values
    private User(Parcel in) {
        lastName = in.readString();
        firstName = in.readString();
        id = in.readString();
        visibility = in.readByte() != 0;
    }

    public int getPlace() {
        return place;
    }

    public void setPlace(int place) {
        this.place = place;
    }

    public boolean isVisibility() {
        return visibility;
    }
    public void setVisibility(boolean visibility) {
        this.visibility = visibility;
    }

    @Override
    public String toString() {
        return "User{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", id='" + id + '\'' +
                ", visibility=" + visibility +
                ", place='" + place + '\'' +
                '}';
    }
}

