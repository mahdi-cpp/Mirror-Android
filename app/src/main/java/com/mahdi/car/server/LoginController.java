package com.mahdi.car.server;

import com.mahdi.car.server.dtos.LoginResponseDTO;
import retrofit2.Call;
import retrofit2.http.*;

import java.math.BigInteger;


public interface LoginController {

    @GET("login/v1/server_awake")
    Call<BigInteger> serverAwake();

    @POST("/login/v1/login_sms")
    Call<LoginResponseDTO> loginSMS(@Query("phone") String phone, @Query("android_id") String android_id);

    @POST("/login/v1/verify_code")
    Call<LoginResponseDTO> verifyCode(@Query("phone") String phone, @Query("code") String code);

    @POST("/login/v1/username")
    Call<LoginResponseDTO> username(@Query("phone") String phone, @Query("username") String username);

}
