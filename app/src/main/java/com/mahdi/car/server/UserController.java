package com.mahdi.car.server;


import com.mahdi.car.server.model.User;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface UserController
{

    @GET("/v1/users/get_user")
    Call<User> getUser(@Query("owner_userid") int owner_userid);

    @GET("/v1/users/getFollowers")
    Call<List<User>> getFollowers(@Query("owner_userid") int owner_userid, @Query("page") int page, @Query("size") int size);

    @GET("/v1/users/getFollowing")
    Call<List<User>> getFollowings(@Query("owner_userid") int owner_userid, @Query("page") int page, @Query("size") int size);

    @GET("/v1/users/likes")
    Call<List<User>> getLikes(@Query("postid") long postid, @Query("page") int page, @Query("size") int size);

    @GET("/v1/users/search")
    Call<List<User>> getSearch(@Query("text") String text);

    @GET("/v1/users/setting")
    Call<User> getSetting();

    @GET("/v1/users/get_all_users")
    Call<List<User>> getAllUsers();

    //--------------------------------------------------------------------------


    @PUT("/v1/users/set_following")
    Call<Integer> setFollowing(@Query("followerid") int followerid, @Query("value") boolean value);


    //-----------------------------Edit Profile---------------------------------------------
    @PUT("/v1/users/update_name")
    Call<Integer> updateName(@Query("name") String name);

    @PUT("/v1/users/update_username")
    Call<Integer> updateUsername(@Query("username") String username);

    @PUT("/v1/users/update_website")
    Call<Integer> updateWebsite(@Query("website") String website);

    @PUT("/v1/users/update_biography")
    Call<Integer> updateBiography(@Query("biography") String biography);

    //    @PUT("/v1/users/update_province")
    //    Call<Integer> updateProvince(@Query("province") String province);
    //
    //    @PUT("/v1/users/update_location")
    //    Call<Integer> updateLocation(@Query("latitude") double latitude, @Query("longitude") double longitude);

    @Multipart
    @POST("/v1/users/edit_avatar")
    Call<String> updateAvatar(@Query("oldThumbnail") String oldThumbnail, @Query("oldAvatar") String oldAvatar, @Part MultipartBody.Part thumbnail, @Part MultipartBody.Part avatar);

    @DELETE("/v1/users/delete_avatar")
    Call<Integer> deleteAvatar();
    //--------------------------------------------------------------------------

}