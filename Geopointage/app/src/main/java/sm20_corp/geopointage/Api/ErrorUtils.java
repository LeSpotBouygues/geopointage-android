package sm20_corp.geopointage.Api;

import java.io.IOException;
import java.lang.annotation.Annotation;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Response;

/**
 * Created by gun on 07/12/2016.
 * Geopointage
 */

public class ErrorUtils {

    public static APIError parseError(Response<?> response) {
        Converter<ResponseBody, APIError> converter =
                ApiManager.getRetrofit()
                        .responseBodyConverter(APIError.class, new Annotation[0]);

        APIError error;

        try {
            error = converter.convert(response.errorBody());
        } catch (IOException e) {
            return new APIError();
        }

        return error;
    }
}