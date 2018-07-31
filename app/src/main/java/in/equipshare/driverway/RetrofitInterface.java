package in.equipshare.driverway;

import java.util.List;

import in.equipshare.driverway.model.Model;
import in.equipshare.driverway.model.User;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface RetrofitInterface {

    @FormUrlEncoded
    @POST("/api/register")
    Call<Model> register(@Field(value = "contact") String contact,
                         @Field(value = "pin" ) String pin,
                         @Field(value = "email") String email,
                         @Field(value = "name") String name
                              , @Field(value="bloodgroup")String bloodgroup );



    @FormUrlEncoded
    @POST("/api/login")
    Call<Model> login(@Field(value = "contact") String contact,@Field(value = "pin") String pin);

    @FormUrlEncoded
    @POST("/api/checkMobile")
    Call<Model> checkMobile(@Field(value="contact")String contact);

    @FormUrlEncoded
    @POST("/api/updatemobile")
    Call<Model> updateMobile(@Header("authorization")String authtoken,@Field(value="contact")String contact);

    @FormUrlEncoded
    @POST("/api/changepin")
    Call<Model> changePin(@Header("authorization")String authtoken,@Field(value ="oldpin")String oldpin,
                          @Field(value = "newpin")String newpin);

    @FormUrlEncoded
    @POST("/api/profile")
    Call<Model> updateProfile(@Header("authorization")String authtoken,@Field(value = "name")String name,@Field(value="email") String email
    ,@Field(value ="bloodgroup")String bloodgroup);

    @GET("/api/profile")
    Call<Model>show_profile(@Header("authorization")String authtoken);

    @GET("/api/")
    Call<Model>session_manage(@Header("authorization")String authtoken);

    @GET("/api/qrcode")
    Call<Model>qrcode(@Header("authorization")String authtoken);






}
