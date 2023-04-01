package com.mahdi.car;


import android.Manifest;
import android.app.ActivityManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.mahdi.car.Thread.UdpThread;
import com.mahdi.car.Thread.WebSocketThread;
import com.mahdi.car.core.QZoomView;
import com.mahdi.car.core.RootView;
import com.mahdi.car.messenger.AndroidUtilities;
import com.mahdi.car.messenger.FileLog;
import com.mahdi.car.messenger.MediaController;
import com.mahdi.car.messenger.NotificationCenter;
import com.mahdi.car.messenger.UdpReceiver;
import com.mahdi.car.messenger.Utilities;
import com.mahdi.car.messenger.WebSocketReceiver;
import com.mahdi.car.share.component.ui.LayoutHelper;

import java.util.ArrayList;

public class MainActivity extends android.app.Activity implements NotificationCenter.NotificationCenterDelegate {

    public static final String TAG = "MyActivity";

    private boolean finished;
    private int currentConnectionState;

    // boolean flag to toggle the ui
    private boolean mRequestingLocationUpdates;

    private static boolean isAutoUpdate = false;

    private BroadcastReceiver receiver;

    public static int height;

    //BoundService class Objet
//    private UDPListenerService udpListenerService;
//    private WebSocketService webSocketService;

    //boolean variable to keep a check on service bind and unbind event
    boolean isBoundWebSocket = false;
    boolean isBoundUDP = false;

    UdpReceiver udpReceiver = null;
    WebSocketReceiver webSocketReceiver = null;
    IntentFilter udpIntentFilter;
    IntentFilter webSocketIntentFilter;

    private UdpThread udpThread;
    private WebSocketThread webSocketThread;

    //TimerTask timerTask = null;
    //running timer task as daemon thread
    //Timer timer = null;

    //Boolean shouldListenForUDPBroadcast = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        App.postInitApplication();
        AndroidUtilities.checkDisplaySize(this, getResources().getConfiguration());


        requestWindowFeature(Window.FEATURE_NO_TITLE);

        //FatherView.instance().setParentActivity(this);

        setTheme(R.style.Theme_TMessages);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            try {
                setTaskDescription(new ActivityManager.TaskDescription(null, null, 0xffffffff | 0xffffffff));
            } catch (Exception e) {
                //
            }

            if (Build.VERSION.SDK_INT >= 24) {
                AndroidUtilities.isInMultiwindow = isInMultiWindowMode();
            }

            int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                AndroidUtilities.statusBarHeight = getResources().getDimensionPixelSize(resourceId);
                //AndroidUtilities.statusBarHeightDP = AndroidUtilities.pxToDp(AndroidUtilities.statusBarHeight);
            }

            //            resourceId = getResources().getIdentifier("navigation_bar_height", "dimen", "android");
            //            if (resourceId > 0) {
            //                AndroidUtilities.navigation_bar_height = getResources().getDimensionPixelSize(resourceId);
            //            }

            resourceId = getResources().getIdentifier("navigation_bar_height", "dimen", "android");
            if (resourceId > 0) {
                AndroidUtilities.navigationBarHeight = getResources().getDimensionPixelSize(resourceId);
            }
        }

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        AndroidUtilities.init(this, displayMetrics.widthPixels, displayMetrics.heightPixels);

        RootView.instance().init(this);
        setContentView(RootView.instance().getContentView(), LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT));

        addContentView(RootView.instance().getFullContentView(), LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT));

        QZoomView.getInstance().setParentActivity(this);//for zoom photo
        addContentView(QZoomView.getInstance().getView(), QZoomView.getInstance().getView().getLayoutParams());


        if (Build.VERSION.SDK_INT >= 21) {

            Window window = getWindow();

            // clear FLAG_TRANSLUCENT_STATUS flag:
            //            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

            //            //add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
            //            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            //
            //            //window.setStatusBarColor(0xfff6f6f6);
            //            window.setNavigationBarColor(0x00000000);
            //            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//  set status text dark
        }

        NotificationCenter.getInstance().postNotificationName(NotificationCenter.closeOtherAppActivities, this);
        NotificationCenter.getInstance().addObserver(this, NotificationCenter.finishActivity);
        NotificationCenter.getInstance().addObserver(this, NotificationCenter.appDidLogout);
        NotificationCenter.getInstance().addObserver(this, NotificationCenter.closeOtherAppActivities);
        NotificationCenter.getInstance().addObserver(this, NotificationCenter.didUpdatedConnectionState);
        NotificationCenter.getInstance().addObserver(this, NotificationCenter.wasUnableToFindCurrentLocation);
        NotificationCenter.getInstance().addObserver(this, NotificationCenter.didSetPasscode);
        NotificationCenter.getInstance().addObserver(this, NotificationCenter.reloadInterface);

        createNotificationChannel();

        if (App.authorization != null) {

            //if(true){
            //actionBarLayout.presentFragment(QToolBar.getInstance().homePage = new HomeFragment(), true, true, true);
            // initialize the necessary libraries
            init();

            // restore the values from saved instance state
            restoreValuesFromBundle(savedInstanceState);

            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                long postid = extras.getLong("postid");

                if (postid > 0) {

                }
            }

        } else {

        }

        Intent intent = getIntent();
        if (intent != null) {

            Uri data = intent.getData();
            if (data != null) {
                String url = data.toString();
                int index = url.lastIndexOf('/');
                String postid = url.substring(index + 1, url.length());

            }
        }

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent intent) {

                //                boolean isScreenOn = App.pm.isScreenOn();
                //                if (!isScreenOn) {
                //                    QVideo.getInstance().pause();
                //                } else {
                //                    QVideo.getInstance().update();
                //                }
            }
        };

        IntentFilter screenStateFilter = new IntentFilter();
        screenStateFilter.addAction(Intent.ACTION_SCREEN_ON);
        screenStateFilter.addAction(Intent.ACTION_SCREEN_OFF);
        registerReceiver(receiver, screenStateFilter);

        AndroidUtilities.hideStatusBar();
        AndroidUtilities.showStatusBar();

        //-------------------------------------

        webSocketThread = new WebSocketThread(getApplicationContext());

        udpReceiver = new UdpReceiver();
        udpReceiver.setDelegate((senderIP, message) -> {
            Log.e("udpReceiver", "udp receiver senderIP:" + senderIP);
            if (webSocketThread != null && !webSocketThread.isOpened()) {
                Log.e("udpReceiver", "webSocketThread.open()");
                webSocketThread.open(senderIP);
            }
        });

        webSocketReceiver = new WebSocketReceiver();
        webSocketReceiver.setDelegate(new WebSocketReceiver.Delegate() {
            @Override
            public void onOpened() {
                RootView.instance().onWebSocketOpened();
            }

            @Override
            public void onClose() {
                RootView.instance().onWebSocketClosed();
            }

            @Override
            public void onMessage(String message) {
                RootView.instance().onWebSocketReceive(message);
            }
        });

        udpIntentFilter = new IntentFilter("udp.mahdi");
        webSocketIntentFilter = new IntentFilter("mahdi.websocket");

        registerReceiver(udpReceiver, udpIntentFilter);
        registerReceiver(webSocketReceiver, webSocketIntentFilter);

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            startForegroundService(new Intent(this, UDPListenerService.class));
//        } else {

        //}




//        timerTask = new ProcessTimerTask();
//        //running timer task as daemon thread
//        timer = new Timer(true);
//        timer.scheduleAtFixedRate(timerTask, 0, 5000);


        udpThread = new UdpThread(getApplicationContext());
        udpThread.start();

    }

//    public class ProcessTimerTask extends TimerTask {
//        @Override
//        public void run() {
//            Log.e("111", "Timer task");
//            if (webSocketService != null && !webSocketService.isOpened()) {
//                //websocketStart();
//            }
//        }
//    }


    public class MyBroadCastReciever extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
                Log.i("Check", "Screen went OFF");
                Toast.makeText(context, "screen OFF", Toast.LENGTH_LONG).show();
            } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
                Log.i("Check", "Screen went ON");
                Toast.makeText(context, "screen ON", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        //        if (hasFocus) {
        //            hideSystemUI();
        //        }
    }

    private void hideSystemUI() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE
                // Set the content to appear under the system bars so that the
                // content doesn't resize when the system bars hide and show.
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                // Hide the nav bar and status bar
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }


    public void webSocketSend(String json) {
        ///webSocketService.send(json);
    }

    @Override
    protected void onNewIntent(Intent intent) {

        Bundle extras = intent.getExtras();
        if (extras != null) {
            long postid = extras.getLong("postid");
            if (postid > 0) {
                //NetbargFragment fragment = new NetbargFragment(postid);
                //presentFragment(fragment);
            }
        }
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "chanel_2";
            String description = "description";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("13", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void onFinish() {
        if (finished) {
            return;
        }

        finished = true;

        NotificationCenter.getInstance().removeObserver(this, NotificationCenter.appDidLogout);
        NotificationCenter.getInstance().removeObserver(this, NotificationCenter.closeOtherAppActivities);
        NotificationCenter.getInstance().removeObserver(this, NotificationCenter.didUpdatedConnectionState);
        NotificationCenter.getInstance().removeObserver(this, NotificationCenter.wasUnableToFindCurrentLocation);
        NotificationCenter.getInstance().removeObserver(this, NotificationCenter.didSetPasscode);
        NotificationCenter.getInstance().removeObserver(this, NotificationCenter.reloadInterface);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 3012 && resultCode == RESULT_OK) {
            ArrayList<String> matches = data.getStringArrayListExtra("android.speech.extra.RESULTS");
            String result = matches.get(0);
            NotificationCenter.getInstance().postNotificationName(NotificationCenter.voiceSearch, result);
            return;
        }

        super.onActivityResult(requestCode, resultCode, data);

    }

    @SuppressWarnings({"ResourceType"})
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 3 || requestCode == 4 || requestCode == 5 || requestCode == 19 || requestCode == 20) {
            boolean showAlert = true;
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (requestCode == 4) {
                    //ImageUtils.getInstance().checkMediaPaths();
                    return;
                } else if (requestCode == 5) {
                    //ContactsController.getInstance().forceImportContacts();
                    return;
                } else if (requestCode == 3) {

                    return;
                } else if (requestCode == 19 || requestCode == 20) {
                    showAlert = false;
                }
            }

        } else if (requestCode == 2) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                NotificationCenter.getInstance().postNotificationName(NotificationCenter.locationPermissionGranted);
            }
        }


        if (AndroidUtilities.isTablet()) {

        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        RootView.instance().onPause();

        //UserConfig.lastAppPauseTime = System.currentTimeMillis();
        App.mainInterfacePaused = true;
        Utilities.stageQueue.postRunnable(new Runnable() {
            @Override
            public void run() {
                App.mainInterfacePausedStageQueue = true;
                App.mainInterfacePausedStageQueueTime = 0;
            }
        });

        //actionBarLayout.onPause();
        AndroidUtilities.unregisterUpdates();

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {

        RootView.instance().onDestroy();

        unregisterReceiver(receiver);
        unregisterReceiver(udpReceiver);
        unregisterReceiver(webSocketReceiver);

//        if (isBoundWebSocket) {
//            unbindService(boundServiceConnection);
//            isBoundWebSocket = false;
//        }
//
//        if (isBoundUDP) {
//            unbindService(boundUDP);
//            isBoundUDP = false;
//        }


        udpThread.stop();
        webSocketThread.destroy();

//        if (timer != null) {
//            timer.cancel();
//            timer = null;
//        }

        super.onDestroy();
        onFinish();
    }

    @Override
    public void onBackPressed() {
        if (!RootView.instance().onBackPressed()) {
            return;
        }

        //FatherView.instance().clear();
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();

//        if (webSocketService != null) {
//            if (webSocketService.isOpened()) {
//                Log.d("WebSocket:", "opened");
//            } else {
//                Log.d("WebSocket:", "closed");
//            }
//        }

        //FatherView.instance().init(this);
        RootView.instance().onResume();


        //showLanguageAlert(false);
        App.mainInterfacePaused = false;
        Utilities.stageQueue.postRunnable(new Runnable() {
            @Override
            public void run() {
                App.mainInterfacePausedStageQueue = false;
                App.mainInterfacePausedStageQueueTime = System.currentTimeMillis();
            }
        });

        MediaController.checkGallery();

        AndroidUtilities.checkForCrashes(this);
        AndroidUtilities.checkForUpdates(this);


        if (mRequestingLocationUpdates) {

            // Resuming location updates depending on button state and
            // allowed permissions
            if (checkPermissions()) {
                //startLocationUpdates();
            }

            //updateLocationUI();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        AndroidUtilities.checkDisplaySize(this, newConfig);
        super.onConfigurationChanged(newConfig);
        //checkLayout();
    }

    @Override
    public void onMultiWindowModeChanged(boolean isInMultiWindowMode) {
        AndroidUtilities.isInMultiwindow = isInMultiWindowMode;
        //checkLayout();
    }

    @Override
    @SuppressWarnings("unchecked")
    public void didReceivedNotification(int id, Object... args) {

        if (id == NotificationCenter.finishActivity) {


        } else if (id == NotificationCenter.appDidLogout) {


        } else if (id == NotificationCenter.closeOtherAppActivities) {
            if (args[0] != this) {
                onFinish();
                finish();
            }
        } else if (id == NotificationCenter.didUpdatedConnectionState) {
            int state = 0;
            if (currentConnectionState != state) {
                FileLog.d("switch to state " + state);
                currentConnectionState = state;
            }
        } else if (id == NotificationCenter.wasUnableToFindCurrentLocation) {

        } else if (id == NotificationCenter.reloadInterface) {
            // rebuildAllFragments(true);
        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //        outState.putBoolean("is_requesting_updates", mRequestingLocationUpdates);
        //        outState.putParcelable("last_known_location", mCurrentLocation);
        //        outState.putString("last_updated_on", mLastUpdateTime);
    }


    @Override
    public void onLowMemory() {
        super.onLowMemory();
        //actionBarLayout.onLowMemory();
    }

    private void init() {

        // Requesting ACCESS_FINE_LOCATION using Dexter library
        Dexter.withActivity(this).withPermission(Manifest.permission.ACCESS_FINE_LOCATION).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse response) {
                mRequestingLocationUpdates = true;
                //startLocationUpdates();
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse response) {
                if (response.isPermanentlyDenied()) {
                    // open device settings when the permission is
                    // denied permanently
                    openSettings();
                }
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                token.continuePermissionRequest();
            }
        }).check();
    }

    /**
     * Restoring values from saved instance state
     */
    private void restoreValuesFromBundle(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("is_requesting_updates")) {
                mRequestingLocationUpdates = savedInstanceState.getBoolean("is_requesting_updates");
            }

            if (savedInstanceState.containsKey("last_known_location")) {
                //mCurrentLocation = savedInstanceState.getParcelable("last_known_location");
            }

        }

        //updateLocationUI();
    }


    private void openSettings() {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", BuildConfig.APPLICATION_ID, null);
        intent.setData(uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

//
//    private ServiceConnection boundServiceConnection = new ServiceConnection() {
//
//        @Override
//        public void onServiceConnected(ComponentName name, IBinder service) {
//
//            WebSocketService.MyBinder binderBridge = (WebSocketService.MyBinder) service;
//            webSocketService = binderBridge.getService();
//            isBoundWebSocket = true;
//
//        }
//
//        @Override
//        public void onServiceDisconnected(ComponentName name) {
//
//            isBoundWebSocket = false;
//            webSocketService = null;
//
//        }
//    };
//
//    private ServiceConnection boundUDP = new ServiceConnection() {
//
//        @Override
//        public void onServiceConnected(ComponentName name, IBinder service) {
//
//            UDPListenerService.MyBinder binderBridge = (UDPListenerService.MyBinder) service;
//            udpListenerService = binderBridge.getService();
//            isBoundUDP = true;
//
//        }
//
//        @Override
//        public void onServiceDisconnected(ComponentName name) {
//
//            isBoundUDP = false;
//            udpListenerService = null;
//
//        }
//    };

}
