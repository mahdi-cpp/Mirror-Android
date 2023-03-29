package com.mahdi.car.server;


import com.google.gson.JsonObject;


import com.mahdi.car.server.model.Media;
import com.mahdi.car.server.model.Property;
import com.mahdi.car.server.dtos.PostDTO;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;


public interface TicketController
{

    @GET("/ticket/v1.0/request_post")
     Call<PostDTO> getPost();

    @GET("/ticket/v1.0/property")
    Call<List<Property>> getProperties();

//    @POST("/ticket/v1.0/update")
//    Call<Integer> update(@Body TicketUpdateDTO update);

    @POST("/ticket/v1.0/register")
    Call<Integer> register(@Query("postid") long postid);

    @POST("/ticket/v1.0/facility")
    Call<Boolean> updateFacility(@Query("propertyid") int propertyid, @Query("postid") long postid, @Query("value") int value);

    @Multipart
    @POST("/ticket/v1.0/add_photo")
    Call<Media> addPhoto(@Query("postid") long postid, @Query("w") int w, @Query("h") int h, @Part MultipartBody.Part thumbnail, @Part MultipartBody.Part photo);

    @Multipart
    @POST("/ticket/v1.0/add_video")
    Call<Media> addVideo(@Query("postid") long postid, @Query("w") int w, @Query("h") int h, @Part MultipartBody.Part thumbnail, @Part MultipartBody.Part photo, @Part MultipartBody.Part video);

    @Multipart
    @POST("/ticket/v1.0/add_igtv")
    Call<Long> addIGTV(@Query("title") String title, @Query("caption") String caption, @Query("video_duration") int video_duration, @Query("w") int w, @Query("h") int h, @Part MultipartBody.Part thumbnail, @Part MultipartBody.Part photo, @Part MultipartBody.Part video);

    @Multipart
    @POST("/upload")
    Call<JsonObject> uploadImage(@Part MultipartBody.Part file);

    @POST("/ticket/v1.0/delete_media") Call<Integer> setDeleteMedia(@Query("mediaid") long mediaid);

}