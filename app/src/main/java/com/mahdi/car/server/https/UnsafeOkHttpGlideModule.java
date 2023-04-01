package com.mahdi.car.server.https;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.LibraryGlideModule;

import java.io.InputStream;

import okhttp3.OkHttpClient;

@GlideModule
public class UnsafeOkHttpGlideModule extends LibraryGlideModule {
    @Override
    public void registerComponents(Context context, Glide glide, Registry registry) {
        OkHttpClient client = UnsafeOkHttpClient.getUnsafeOkHttpClient();
        registry.replace(GlideUrl.class, InputStream.class,
                new OkHttpUrlLoader.Factory(client));
    }
}