package sm20_corp.geopointage.Api;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import sm20_corp.geopointage.Api.ModelApi.ChantierList;
import sm20_corp.geopointage.Api.ModelApi.Message;
import sm20_corp.geopointage.Api.ModelApi.UserList;

/**
 * Created by gun on 07/12/2016.
 * Geopointage
 */

public interface GeopointageApi {

    String version = "v0/";

    @GET(version + "workers")
    Call<UserList> getUser();

    @FormUrlEncoded
    @POST(version + "workers")
    Call<Message> sendUser(@Field("lastName") String lastName,
                           @Field("firstName") String firstName,
                           @Field("registrationNumber") String registrationNumber);

    @GET(version + "sites")
    Call<ChantierList> getSite();

    @FormUrlEncoded
    @POST(version + "sites")
    Call<Message> sendSite(@Field("address") String address,
                           @Field("login") String iotp);

    @FormUrlEncoded
    @POST(version + "sites/byEotp/{iotp}/comments")
    Call<Message> sendComment(@Path("iotp") String iotp,
                              @Field("body") String body,
                              @Field("firstName") String firstName,
                              @Field("lastName") String lastName);


    @FormUrlEncoded
    @POST(version + "scores/import")
    Call<Message> sendTach(@Field("body") String body);

}
