package sm20_corp.geopointage.Api;

import android.content.Context;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by gun on 07/12/2016.
 * Geopointage
 */

public class ApiManager {
    private static GeopointageApi restClient;
    private static Retrofit retrofit;
    private static String mUrl = "http://geopointage.lespot-bouygues.com";
    private static String mPort = ":8081/";

    private Context mContext;

    static {
        setupRestClient();
    }

    private ApiManager() {
    }

    public static Retrofit getRetrofit() {
        return (retrofit);
    }

    public static GeopointageApi get() {
        return (restClient);
    }


    private static void setupRestClient() {
        retrofit = new Retrofit.Builder()
                .baseUrl(mUrl + mPort)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        restClient = retrofit.create(GeopointageApi.class);
    }
}