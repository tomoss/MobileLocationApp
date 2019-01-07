package org.scd.mobiletrackingapp.remote;

import com.google.gson.JsonObject;

import org.scd.mobiletrackingapp.model.ResLocation;
import org.scd.mobiletrackingapp.model.ResUser;
import org.scd.mobiletrackingapp.model.dto.LocationDTO;
import org.scd.mobiletrackingapp.model.dto.UserLoginDTO;
import org.scd.mobiletrackingapp.model.dto.UserRegisterDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface UserService {

    @POST("users/login")
    Call<ResUser> login(@Body UserLoginDTO userLogin, @Header("Authorization") String credentials);

    @POST("users/register")
    Call<ResUser> register(@Body UserRegisterDTO userRegister);

    @POST("locations/add")
    Call<ResLocation> addLocation(@Body LocationDTO locationDTO, @Header("Authorization") String credentials);
}
