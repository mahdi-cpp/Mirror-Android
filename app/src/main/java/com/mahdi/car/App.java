package com.mahdi.car;


import android.app.Activity;
import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;

import android.util.Log;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import androidx.core.app.NotificationCompat;
import com.mahdi.car.share.Themp;
import com.mahdi.car.messenger.*;


import com.mahdi.car.server.https.Server;
import com.mahdi.car.server.https.UnsafeOkHttpGlideModule;

public class App extends android.app.Application implements Application.ActivityLifecycleCallbacks
{
    // adb tcpip 5555
    // adb connect 192.168.1.171:5555

    public static boolean isLocal = true;

    public static String server;
    public static String files;

    public static int userid;
    public static String authorization;
    public static long cartCount = 0;

    //public static String videos = "http://192.168.1.155:8080/homegram-videos/videos/";
    //public static String videos = "http://homegram.ir:8181/homegram-videos/videos/";
    public static String videos;
    public static String videos8;

    public static Context context;
    public static Context applicationContext;

    public static Typeface avenyTypeface;
    //    public static Typeface typeface2;

    public static volatile Handler applicationHandler;
    public static volatile boolean isScreenOn = false;
    public static volatile boolean mainInterfacePaused = true;
    public static volatile boolean mainInterfacePausedStageQueue = true;
    public static volatile long mainInterfacePausedStageQueueTime;
    public static String number = "";
    private static volatile boolean applicationInite = false;
    private static App application;

    public static PowerManager pm;

    @Override
    public void onCreate()
    {

        //enableStrictMode();
        context = getApplicationContext();
        applicationContext = getApplicationContext();
        application = this;

        NativeLoader.initNativeLibs(App.applicationContext);

        if (isLocal) {
            server = "http://192.168.1.113:8080/";
            videos = "http://192.168.1.113:8081/";
        } else {
            server = "http://homegram.ir:54271/homegram-0.0.2/";
            videos = "http://homegram.ir/files/";
        }

        pm = (PowerManager) getSystemService(Context.POWER_SERVICE);

        files = videos ;


        Glide glide = Glide.get(getApplicationContext());
        UnsafeOkHttpGlideModule unsafeOkHttpGlideModule = new UnsafeOkHttpGlideModule();
        unsafeOkHttpGlideModule.registerComponents(glide.getContext(), glide, glide.getRegistry());

        SharedPreferences preferences = App.applicationContext.getSharedPreferences("user", android.app.Activity.MODE_PRIVATE);
        authorization = preferences.getString("authorization", null);

        //Log.i("userid is", " " + userid);

        //userid = 389; //mahdiabdolmaleki.ir
        //userid = 356;
        //userid = 382;
        //userid = 364; //homegram

        avenyTypeface = Typeface.createFromAsset(getApplicationContext().getAssets(), "AvenyTRegular.otf");
        //        typeface2 = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/persianMuharam.ttf");

        AndroidUtilities.checkDisplaySize(context, null);
        applicationHandler = new Handler(context.getMainLooper());

        Server.init();
        registerActivityLifecycleCallbacks(this);

        Themp.createMahdiResources(this);

        super.onCreate();
    }

    public static Context getContext()
    {
        return context;
    }

    public static void postInitApplication()
    {

        if (applicationInite) {
            return;
        }

        applicationInite = true;

        try {
            LocaleController.getInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            final IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
            filter.addAction(Intent.ACTION_SCREEN_OFF);
            final BroadcastReceiver mReceiver = new ScreenReceiver();
            applicationContext.registerReceiver(mReceiver, filter);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            PowerManager pm = (PowerManager) App.applicationContext.getSystemService(Context.POWER_SERVICE);
            isScreenOn = pm.isScreenOn();
            FileLog.e("screen state = " + isScreenOn);
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    public static App getApplication()
    {
        return application;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);

        try {
            LocaleController.getInstance().onDeviceConfigurationChange(newConfig);
            AndroidUtilities.checkDisplaySize(applicationContext, newConfig);
        } catch (Exception e) {
            e.printStackTrace();
        }


        // Checks whether a hardware keyboard is available
        if (newConfig.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_NO) {
            Toast.makeText(this, "keyboard visible", Toast.LENGTH_SHORT).show();
        } else if (newConfig.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_YES) {
            Log.e("keyboard hidden", "keyboard hidden");
        }
    }


    @Override
    public void onTerminate()
    {
        super.onTerminate();

        application = null;
    }

    public void sendNotification(String title, String messageBody, Bitmap bitmap)
    {

        Intent intent = new Intent(this, MyActivity.class);
        intent.putExtra("postid", 20);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent, PendingIntent.FLAG_ONE_SHOT);

        String channelId = getString(R.string.default_notification_channel_id);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, channelId).setSmallIcon(R.drawable.notification_icon).setContentTitle(title).setContentText(messageBody).setAutoCancel(true).setSound(defaultSoundUri);
        //.setLargeIcon(bitmap)
        //.setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, "Channel human readable title", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState)
    {

    }

    @Override
    public void onActivityStarted(Activity activity)
    {

    }

    @Override
    public void onActivityResumed(Activity activity)
    {

    }

    @Override
    public void onActivityPaused(Activity activity)
    {
        //QVideo.getInstance().pause();
    }

    @Override
    public void onActivityStopped(Activity activity)
    {
        //QVideo.getInstance().pause();
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState)
    {

    }

    @Override
    public void onActivityDestroyed(Activity activity)
    {
        //QVideo.getInstance().pause();
    }
}