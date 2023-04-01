package com.mahdi.car.server;


import com.mahdi.car.server.model.Notification;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface NotificationController {

    @GET("/notification/v1.0/notifications")
    Call<List<Notification>> getNotifications(@Query("page") int page, @Query("size") int size);

}