package sm20_corp.geopointage.Api.ModelApi;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import sm20_corp.geopointage.Model.User;

/**
 * Created by gun on 07/12/2016.
 * Geopointage
 */

public class UserList {
    @SerializedName("status")
    private String status;

    @SerializedName("data")
    private ArrayList<User> user;

    public UserList() {
    }

    public UserList(String status, ArrayList<User> user) {
        this.status = status;
        this.user = user;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<User> getUser() {
        return user;
    }

    public void setUser(ArrayList<User> user) {
        this.user = user;
    }


}
