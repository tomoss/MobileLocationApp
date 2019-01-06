package org.scd.mobiletrackingapp.remote;

import com.google.gson.JsonObject;

import org.scd.mobiletrackingapp.model.ResUser;
import org.scd.mobiletrackingapp.model.UserLogin;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface UserService {

    @POST("users/login")
    Call<ResUser> login(@Body UserLogin userLogin, @Header("Authorization") String credentials);

   // @POST("users/register")
    //Call<ResUser> register()

}
