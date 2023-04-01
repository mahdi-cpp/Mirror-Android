package com.mahdi.car.server;


import com.mahdi.car.server.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface DirectController {
    @GET("/user/v1.0/followings")
    Call<List<User>> getDirects(@Query("owner_userid") int owner_userid, @Query("jump") int jump);
}