package com.mahdi.car.server.https;

import com.mahdi.car.App;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServerClientInstance {

    private static OkHttpClient loginClient;
    private static OkHttpClient client;

    private static Retrofit retrofitLogin;
    private static Retrofit retrofit;
    //private static final String BASE_URL = "https://192.168.1.113:54271";
    //private static final String BASE_URL = "http://192.168.1.113:8080";

    public static Retrofit getLoginRetrofitInstance() {

        loginClient = UnsafeOkHttpClient.getLoginUnsafeOkHttpClient();

        if (retrofitLogin == null) {
            retrofitLogin = new Retrofit.Builder()
                    .baseUrl(App.server)
                    .client(loginClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofitLogin;
    }



    public static Retrofit getRetrofitInstance() {

        client = UnsafeOkHttpClient.getUnsafeOkHttpClient();

        if (retrofit == null) {
            retrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl(App.server)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}

