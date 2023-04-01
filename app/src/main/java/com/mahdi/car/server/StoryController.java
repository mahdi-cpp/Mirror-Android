package com.mahdi.car.server;

import com.mahdi.car.server.model.story.Story;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface StoryController
{
    @GET("/v1/story/stories")
    Call<List<Story>> getStories(@Query("userid") int userid);
}