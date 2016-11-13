package sm20_corp.geopointage.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by gun on 02/11/2016.
 * Geopointage
 */

public class User implements Parcelable{

    String firstName;
    String lastName;
    String id;

    public User(String lastName, String firstName, String id) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.id = id;
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

    }
}
