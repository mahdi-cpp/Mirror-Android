package com.mahdi.car.server;

import com.mahdi.car.server.dtos.UploadDTO;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface UploadInterface
{

//    @Multipart
//    @POST ("/post/v1.0/uploadFile")
//    Call<JsonElement> uploadFile(@Url String url, @Part MultipartBody.Part file, @Header("Authorization") String authorization);

    @Multipart
    @POST ("/ticket/v1.0/uploadFile")
    Call<UploadDTO> uploadFile(@Part MultipartBody.Part file);
}
