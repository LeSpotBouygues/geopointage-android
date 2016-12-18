package sm20_corp.geopointage.Api.ModelApi;

import com.google.gson.annotations.SerializedName;

/**
 * Created by gun on 08/12/2016.
 * Geopointage
 */

public class Message {

    @SerializedName("status")
    private String status;

    public Message() {
    }

    public Message(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
