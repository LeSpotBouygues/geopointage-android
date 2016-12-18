package sm20_corp.geopointage.Api;

/**
 * Created by gun on 07/12/2016.
 * Geopointage
 */

public class APIError {

    private int statusCode;
    private String message;

    public APIError() {
    }

    public int status() {
        return statusCode;
    }

    public String message() {
        return message;
    }

}