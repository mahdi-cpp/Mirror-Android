package com.mahdi.car.server;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.mahdi.car.server.https.Server;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class UploadFileService extends Service
{

    private static final String TAG = "FileService";

    @Override public IBinder onBind(Intent intent)
    {
        return null;
    }

    @Override public void onCreate()
    {
        super.onCreate();
    }

    @SuppressLint("NewApi") @Override public int onStartCommand(Intent intent, int flags, int startId)
    {
        try {

            String title = intent.getStringExtra("title");
            String caption = intent.getStringExtra("caption");
            int video_duration = intent.getIntExtra("video_duration", 60);
            int width = intent.getIntExtra("width", 400);
            int height = intent.getIntExtra("height", 400);
            String thumbnail = intent.getStringExtra("thumbnail");
            String cover = intent.getStringExtra("cover");
            String video = intent.getStringExtra("video");

            upload(title, caption, new File(thumbnail), new File(cover), new File(video), video_duration, width, height);

        } catch (Throwable e) {

        }

        return START_STICKY;
    }

    private void upload(String title, String caption, File thumbnail, File photo, File video, int video_duration, int w, int h)
    {
        MediaType MEDIA_TYPE_JPEG = MediaType.parse("image/jpeg; charset=utf-8");

        Log.e("title: ", " " + title);

        MultipartBody.Part photo1 = MultipartBody.Part.createFormData("thumbnail", thumbnail.getName(), RequestBody.create(MEDIA_TYPE_JPEG, thumbnail));
        MultipartBody.Part photo2 = MultipartBody.Part.createFormData("photo", photo.getName(), RequestBody.create(MEDIA_TYPE_JPEG, photo));
        //MultipartBody.Part video1 = MultipartBody.Part.createFormData("video", video.getName(), RequestBody.create(MEDIA_TYPE_JPEG, video));

        ProgressRequestBody fileBody = new ProgressRequestBody(video, "image/jpeg; charset=utf-8", new ProgressRequestBody.UploadCallbacks()
        {
            @Override public void onProgressUpdate(int percentage)
            {
                sendBroadcastMessage(thumbnail.getAbsolutePath(), percentage);
            }

            @Override public void onError()
            {

            }

            @Override public void onFinish()
            {

            }
        });

        MultipartBody.Part video1 = MultipartBody.Part.createFormData("video", video.getName(), fileBody);

        Call<Long> call = Server.ticket.addIGTV(title, caption, video_duration, w, h, photo1, photo2, video1);
        call.enqueue(new Callback<Long>()
        {
            @Override public void onResponse(Call<Long> call, Response<Long> response)
            {
                if (response.isSuccessful()) {
                    Long result = response.body();
                    Log.e("upload ", "upload ok " + result);
                }

                stop();
            }

            @Override public void onFailure(Call<Long> call, Throwable t)
            {
                stop();
            }
        });
    }

    public void sendBroadcastMessage(String cover, int percent)
    {
        Intent localIntent = new Intent("my.own.broadcast");
        localIntent.putExtra("cover", cover);
        localIntent.putExtra("percent", percent);
        LocalBroadcastManager.getInstance(this).sendBroadcast(localIntent);
    }

    public void stop()
    {
        this.stopSelf();
    }


    @SuppressLint("NewApi") @Override public void onDestroy()
    {
        super.onDestroy();
    }


}
