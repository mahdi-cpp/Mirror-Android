package com.mahdi.car.server;


import com.mahdi.car.server.dtos.BookmarkDTO;
import com.mahdi.car.server.model.Post;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface BookmarkController
{

    @GET("/bookmark/v1.0/collections")
    Call<BookmarkDTO> getAllCollections();

    @GET("/bookmark/v1.0/bookmarks")
    Call<List<Post>> getBookmarks(@Query("page") int page, @Query("size") int size);

    @POST("/bookmark/v1.0/set_bookmark")
    Call<Integer> setBookmark(@Query("postid") long postid, @Query("value") boolean value);

}