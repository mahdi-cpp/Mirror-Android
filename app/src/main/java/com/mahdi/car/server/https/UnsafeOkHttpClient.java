package com.mahdi.car.server.https;

import com.mahdi.car.App;

import java.io.IOException;
import java.security.cert.CertificateException;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class UnsafeOkHttpClient {

    public static OkHttpClient getLoginUnsafeOkHttpClient() {

        try {

//            // Create a trust manager that does not validate certificate chains
//            final TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
//                @Override
//                public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
//
//                }
//
//                @Override
//                public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
//                }
//
//                @Override
//                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
//                    return new java.security.cert.X509Certificate[]{};
//                }
//            }};
//
//            // Install the all-trusting trust manager
//            final SSLContext sslContext = SSLContext.getInstance("SSL");
//            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
//
//            // Create an ssl socket factory with our all-trusting manager
//            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            //builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0]);
//            builder.hostnameVerifier(new HostnameVerifier() {
//                @Override
//                public boolean verify(String hostname, SSLSession session) {
//                    return true;
//                }
//            });

            OkHttpClient okHttpClient = builder.connectTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS)
                    .retryOnConnectionFailure(true)
                    .build();


            return okHttpClient;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static OkHttpClient getUnsafeOkHttpClient() {

        try {

            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                @Override
                public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {

                }

                @Override
                public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                }

                @Override
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return new java.security.cert.X509Certificate[]{};
                }
            }};

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0]);
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });

            OkHttpClient okHttpClient;

            okHttpClient = builder.connectTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS)
                    .retryOnConnectionFailure(true)
                    .addInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {

                            Request request = chain.request();
                            Request newRequest = request.newBuilder().addHeader("Authorization", "Bearer " + App.authorization).build();
                            return chain.proceed(newRequest);
                        }
                    })
                    .build();


            return okHttpClient;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}















