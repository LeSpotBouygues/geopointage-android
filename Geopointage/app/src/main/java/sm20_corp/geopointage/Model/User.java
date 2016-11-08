package sm20_corp.geopointage.Model;

/**
 * Created by gun on 02/11/2016.
 * Geopointage
 */

public class User {

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
}
