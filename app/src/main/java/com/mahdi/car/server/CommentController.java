package com.mahdi.car.server;


import com.mahdi.car.server.model.Comment;
import com.mahdi.car.server.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface CommentController {

    @GET(value = "/v1/comments/getComments")
    Call<List<Comment>> getComments(@Query("postid") long postid, @Query("page") int page, @Query("size") int size);

    @GET(value = "/v1/comments/likes")
    Call<List<User>> getLikes(@Query("commentid") long commentid, @Query("page") int page, @Query("size") int size);

    @POST(value = "/v1/comments/add")
    Call<Long> addComment(@Query("postid") long postid, @Query("message") String message);

    @POST(value = "/v1/comments/add_reply")
    Call<Long> addReply(@Query("postid") long postid, @Query("reply_commentid") long reply_commentid, @Query("message") String message);

    @POST(value = "/v1/comments/like")
    Call<Integer> setLike(@Query("commentid") long commentid, @Query("value") boolean value);

    @DELETE(value = "/v1/comments/delete")
    Call<Integer> delete(@Query("commentid") long commentid);
}



