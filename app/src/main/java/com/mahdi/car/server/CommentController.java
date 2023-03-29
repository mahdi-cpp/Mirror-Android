package com.mahdi.car.server;


import com.mahdi.car.server.model.Comment;
import com.mahdi.car.server.model.User;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

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



