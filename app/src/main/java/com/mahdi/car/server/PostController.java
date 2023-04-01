package com.mahdi.car.server;

import com.mahdi.car.server.dtos.ExploreDTO;
import com.mahdi.car.server.dtos.FeedDTO;
import com.mahdi.car.server.dtos.PostDTO;
import com.mahdi.car.server.dtos.ProfileDTO;
import com.mahdi.car.server.model.Post;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface PostController
{

    @GET("/v1/post/profile")
    Call<ProfileDTO> getProfile(@Query("username") String username, @Query("page") int page, @Query("size") int size);

    @GET("/v1/post/profile_igtv")
    Call<ProfileDTO> getProfileIGTV(@Query("username") String username, @Query("jump") int jump);

    @GET("v1/post/feed")
    Call<FeedDTO> getFeed(@Query("page") int page, @Query("size") int size);

    @GET("/v1/post/post")
    Call<PostDTO> getPost(@Query("postid") long postid);

    @GET("/v1/post/explore")
    Call<ExploreDTO> getExplore(@Query("page") int page, @Query("size") int size);


    @GET("/v1/post/shops")
    Call<List<Post>> getShops(@Query("userid") int userid);

    @GET("/v1/post/archive")
    Call<List<Post>> getArchives(@Query("page") int page, @Query("size") int size);

    @GET("/v1/post/hashtag")
    Call<List<Post>> getHashtag(@Query("jump") int jump, @Query("hashtag") String hashtag);

    @POST("/v1/post/set_like")
    Call<Integer> setLike(@Query("owner_userid") int userid, @Query("postid") long postid, @Query("value") boolean value);

    @POST("/v1/post/set_archive")
    Call<Integer> setArchive(@Query("postid") long postid, @Query("value") int value);

    @POST("/v1/post/forward")
    Call<Integer> setForward(@Query("owner_userid") int owner_userid, @Query("postid") long postid, @Query("message") String message);

    @PUT("/v1/post/edit_caption")
    Call<Integer> editCaption(@Query("postid") long postid, @Query("caption") String caption);

    @DELETE("/v1/post/delete")
    Call<Integer> setDelete(@Query("postid") long postid);

}