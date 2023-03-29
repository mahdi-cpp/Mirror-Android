package com.mahdi.car.server.https;


import android.util.Log;

import com.mahdi.car.server.*;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Query;

public class Server
{

    public static LoginController login;
    public static UserController user;
    public static PostController post;
    public static StoryController story;
    public static NotificationController notification;
    public static TicketController ticket;
    public static CommentController comment;
    public static ReportController report;
    public static UploadInterface uploadInterface;
    public static BookmarkController bookmark;

    public static void init()
    {

        login = ServerClientInstance.getLoginRetrofitInstance().create(LoginController.class);

        user = ServerClientInstance.getLoginRetrofitInstance().create(UserController.class);
        post = ServerClientInstance.getLoginRetrofitInstance().create(PostController.class);
        story = ServerClientInstance.getLoginRetrofitInstance().create(StoryController.class);

        bookmark = ServerClientInstance.getLoginRetrofitInstance().create(BookmarkController.class);
        notification = ServerClientInstance.getLoginRetrofitInstance().create(NotificationController.class);
        ticket = ServerClientInstance.getLoginRetrofitInstance().create(TicketController.class);
        comment = ServerClientInstance.getLoginRetrofitInstance().create(CommentController.class);
        report = ServerClientInstance.getLoginRetrofitInstance().create(ReportController.class);


        uploadInterface = ServerClientInstance.getLoginRetrofitInstance().create(UploadInterface.class);

    }

    public static void setLike(int owner_userid, long postid, boolean value)
    {

        Call<Integer> call = Server.post.setLike(owner_userid, postid, value);
        call.enqueue(new Callback<Integer>()
        {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response)
            {

                if (response.isSuccessful()) {
                    Log.e("setLike", "isSuccessful");
                } else {
                    Log.e("setLike", "Fail: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t)
            {

            }
        });
    }

    public static void setBookmark(long postid, boolean value)
    {
        Call<Integer> call = Server.bookmark.setBookmark(postid, value);
        call.enqueue(new Callback<Integer>()
        {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response)
            {

                if (response.isSuccessful()) {
                    Log.e("setBookmark", "isSuccessful");
                } else {
                    Log.e("setBookmark", "Fail: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t)
            {

            }
        });
    }

    public static void setFollowing(int followerid, boolean value, ServerDelegate delegate)
    {

        Call<Integer> call = user.setFollowing(followerid, value);
        call.enqueue(new Callback<Integer>()
        {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response)
            {
                delegate.response(response);
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t)
            {

            }
        });
    }

    public static void setArchive(long postid, @Query("value") int value, ServerDelegate delegate)
    {

        Call<Integer> call = Server.post.setArchive(postid, value);
        call.enqueue(new Callback<Integer>()
        {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response)
            {
                if (response.isSuccessful()) {
                    delegate.response(response);
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t)
            {

            }
        });
    }
}
