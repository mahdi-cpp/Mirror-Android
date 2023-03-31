package com.mahdi.car;


import android.Manifest;
import android.app.ActivityManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.StatFs;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
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
import com.mahdi.car.core.QZoomView;
import com.mahdi.car.core.RootView;
import com.mahdi.car.messenger.AndroidUtilities;
import com.mahdi.car.messenger.FileLoader;
import com.mahdi.car.messenger.FileLog;
import com.mahdi.car.messenger.MediaController;
import com.mahdi.car.messenger.NotificationCenter;
import com.mahdi.car.messenger.UdpReceiver;
import com.mahdi.car.messenger.Utilities;
import com.mahdi.car.messenger.WebSocketReceiver;
import com.mahdi.car.service.UDPListenerService;
import com.mahdi.car.service.WebSocketService;
import com.mahdi.car.share.component.ui.LayoutHelper;

import java.io.File;
import java.util.ArrayList;

public class MyActivity extends android.app.Activity implements NotificationCenter.NotificationCenterDelegate {

    public static final String TAG = "LaunchActivity";

    private boolean finished;
    private int currentConnectionState;

    // location last updated time
    private String mLastUpdateTime;

    // location updates interval - 10sec
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 20000;

    // fastest updates interval - 5 sec
    // location updates will be received if another app is requesting the locations
    // than your app can handle
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = 20000;
    private static final int REQUEST_CHECK_SETTINGS = 100;

    // bunch of location related apis
    //    public static LatLng latLng;
    //    private FusedLocationProviderClient mFusedLocationClient;
    //    private SettingsClient mSettingsClient;
    //    private LocationRequest mLocationRequest;
    //    private LocationSettingsRequest mLocationSettingsRequest;
    //    private LocationCallback mLocationCallback;
    //    private Location mCurrentLocation;

    // boolean flag to toggle the ui
    private boolean mRequestingLocationUpdates;

    private static boolean isAutoUpdate = false;

    private BroadcastReceiver receiver;

    public static int height;

    //BoundService class Objet
    WebSocketService webSocketService;

    //boolean variable to keep a check on service bind and unbind event
    boolean isBound = false;

    UdpReceiver udpReceiver = null;
    WebSocketReceiver webSocketReceiver = null;
    IntentFilter udpIntentFilter;
    IntentFilter webSocketIntentFilter;

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

                    //NetbargFragment fragment = new NetbargFragment(postid);
                    //                    fragment.setDelegate(new NetbargFragment.Delegate()
                    //                    {
                    //                        @Override
                    //                        public void finish()
                    //                        {
                    //                            //QToolBar.getInstance().homePage.setIsFragment(false);
                    //                        }
                    //                    });
                    //QToolBar.getInstance().homePage.setIsFragment(true);
                    //presentFragment(fragment);
                }
            }

        } else {

            //            for (BaseFragment fragment : actionBarLayout.fragmentsStack) {
            //                fragment.onFragmentDestroy();
            //            }
            //            actionBarLayout.fragmentsStack.clear();
            //            fragments.clear();
            //
            //            LoginFragment loginFragment = new LoginFragment();
            //            loginFragment.setDelegate(new LoginFragment.LoginFragmentDelegate()
            //            {
            //                @Override
            //                public void ok()
            //                {
            //
            //                    actionBarLayout.presentFragment(QToolBar.getInstance().homePage = new HomeFragment(), true, true, true);
            //                    loginFragment.finishFragment();
            //                }
            //            });
            //            actionBarLayout.presentFragment(loginFragment, true, true, true);
        }

        Intent intent = getIntent();
        if (intent != null) {

            Uri data = intent.getData();
            if (data != null) {
                String url = data.toString();
                int index = url.lastIndexOf('/');
                String postid = url.substring(index + 1, url.length());

                //NetbargFragment fragment = new NetbargFragment(Long.parseLong(postid));
                //                fragment.setDelegate(new NetbargFragment.Delegate()
                //                {
                //                    @Override
                //                    public void finish()
                //                    {
                //                        //QToolBar.getInstance().homePage.setIsFragment(false);
                //                    }
                //                });
                //QToolBar.getInstance().homePage.setIsFragment(true);
                //presentFragment(fragment);
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

        udpReceiver = new UdpReceiver();
        udpReceiver.setDelegate(new UdpReceiver.Delegate() {
            @Override
            public void receive(String sender, String message) {

            }
        });

        webSocketReceiver = new WebSocketReceiver();
        webSocketReceiver.setDelegate(new WebSocketReceiver.Delegate() {
            @Override
            public void onOpened() {
                //toolbar.setName("connected");
            }

            @Override
            public void onClose() {
                //toolbar.setName("closed");
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

        startService(new Intent(this, UDPListenerService.class));

        Intent webSocketIntent = new Intent(this, WebSocketService.class);
        startService(webSocketIntent);
        bindService(webSocketIntent, boundServiceConnection, BIND_AUTO_CREATE);

    }

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

    public void websocketStart() {
        webSocketService.startServer("192.168.1.113");
    }

    public void webSocketSend(String json) {
        webSocketService.send(json);
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
        //
        //        if (QMainView..fragmentsStack.size() != 0) {
        //            BaseFragment fragment = actionBarLayout.fragmentsStack.get(actionBarLayout.fragmentsStack.size() - 1);
        //            fragment.onActivityResultFragment(requestCode, resultCode, data);
        //        }

        if (AndroidUtilities.isTablet()) {

        }
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

                //                if (QSnapp.getInstance().googleMap != null) {
                //                    try {
                //                        QSnapp.getInstance().googleMap.setMyLocationEnabled(true);
                //                    } catch (Exception e) {
                //                        FileLog.e(e);
                //                    }
                //
                //                    LatLng latLng = new LatLng(35.6892, 51.3890);
                //                    CameraUpdate position = CameraUpdateFactory.newLatLngZoom(latLng, QSnapp.getInstance().googleMap.getMaxZoomLevel() - 8);
                //                    QSnapp.getInstance().googleMap.moveCamera(position);
                //
                //                    QSnapp.getInstance().googleMap.getUiSettings().setMyLocationButtonEnabled(false);
                //                    QSnapp.getInstance().googleMap.getUiSettings().setZoomControlsEnabled(false);
                //                    QSnapp.getInstance().googleMap.getUiSettings().setCompassEnabled(false);
                //                }
            }
        }

        //        if (actionBarLayout.fragmentsStack.size() != 0) {
        //            BaseFragment fragment = actionBarLayout.fragmentsStack.get(actionBarLayout.fragmentsStack.size() - 1);
        //            fragment.onRequestPermissionsResultFragment(requestCode, permissions, grantResults);
        //        }

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

        if (isBound) {
            unbindService(boundServiceConnection);
            isBound = false;
        }

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

        if (webSocketService != null) {
            if (webSocketService.isOpened()) {
                Log.d("WebSocket:", "opened");
            } else {
                Log.d("WebSocket:", "closed");
            }
        }

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

        /*
        if (passcodeView.getVisibility() != View.VISIBLE) {

            if (AndroidUtilities.isTablet()) {
                rightActionBarLayout.onResume();
                layersActionBarLayout.onResume();
            }
        } else {
            actionBarLayout.dismissDialogs();
            if (AndroidUtilities.isTablet()) {
                rightActionBarLayout.dismissDialogs();
                layersActionBarLayout.dismissDialogs();
            }
            passcodeView.onResume();
        }
        */
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

            //            for (BaseFragment fragment : actionBarLayout.fragmentsStack) {
            //                fragment.onFragmentDestroy();
            //            }
            //            actionBarLayout.fragmentsStack.clear();
            //            fragments.clear();
            //
            //            onFinish();
            //            finish();

        } else if (id == NotificationCenter.appDidLogout) {

            //if (drawerLayoutAdapter != null) {
            //drawerLayoutAdapter.notifyDataSetChanged();
            //}

            //            for (BaseFragment fragment : actionBarLayout.fragmentsStack) {
            //                fragment.onFragmentDestroy();
            //            }
            //            actionBarLayout.fragmentsStack.clear();
            //            if (U.isTablet()) {
            //
            //            }
            /*
            Intent intent2 = new Intent(this, IntroActivity.class);
            startActivity(intent2);
            onFinish();
            finish();
            */
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

    private void checkFreeDiscSpace() {

        if (Build.VERSION.SDK_INT >= 26) {
            return;
        }
        Utilities.globalQueue.postRunnable(new Runnable() {
            @Override
            public void run() {

                try {
                    SharedPreferences preferences = App.applicationContext.getSharedPreferences("mainconfig", android.app.Activity.MODE_PRIVATE);
                    if (Math.abs(preferences.getLong("last_space_check", 0) - System.currentTimeMillis()) >= 3 * 24 * 3600 * 1000) {
                        File path = FileLoader.getInstance().getDirectory(FileLoader.MEDIA_DIR_CACHE);
                        if (path == null) {
                            return;
                        }
                        long freeSpace;
                        StatFs statFs = new StatFs(path.getAbsolutePath());
                        if (Build.VERSION.SDK_INT < 18) {
                            freeSpace = Math.abs(statFs.getAvailableBlocks() * statFs.getBlockSize());
                        } else {
                            freeSpace = statFs.getAvailableBlocksLong() * statFs.getBlockSizeLong();
                        }
                        preferences.edit().putLong("last_space_check", System.currentTimeMillis()).commit();
                        if (freeSpace < 1024 * 1024 * 100) {
                            AndroidUtilities.run(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        //AlertsCreator.createFreeSpaceDialog(LaunchActivity.this).close();
                                    } catch (Throwable ignore) {

                                    }
                                }
                            });
                        }
                    }
                } catch (Throwable ignore) {

                }
            }
        }, 2000);
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

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MENU) {

            /*else if (ArticleViewer.getInstance().isVisible()) {
                return super.onKeyUp(keyCode, event);
            }
            */
            if (AndroidUtilities.isTablet()) {

            } else {
                //                if (actionBarLayout.fragmentsStack.size() == 1) {
                //                    /*
                //                    if (!drawerLayoutContainer.isDrawerOpened()) {
                //                        if (getCurrentFocus() != null) {
                //                            AndroidUtilities.hideKeyboard(getCurrentFocus());
                //                        }
                //                        drawerLayoutContainer.openDrawer(false);
                //                    } else {
                //                        drawerLayoutContainer.closeDrawer(false);
                //                    }
                //                    */
                //                } else {
                //                    actionBarLayout.onKeyUp(keyCode, event);
                //                }
            }
        }
        return super.onKeyUp(keyCode, event);
    }

    private void init() {

        //        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        //        mSettingsClient = LocationServices.getSettingsClient(this);
        //
        //        mLocationCallback = new LocationCallback()
        //        {
        //            @Override
        //            public void onLocationResult(LocationResult locationResult)
        //            {
        //                super.onLocationResult(locationResult);
        //                // location is received
        //                mCurrentLocation = locationResult.getLastLocation();
        //                mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
        //
        //                updateLocationUI();
        //            }
        //        };
        //
        //        mRequestingLocationUpdates = false;
        //
        //        mLocationRequest = new LocationRequest();
        //        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        //        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        //        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        //
        //        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        //        builder.addLocationRequest(mLocationRequest);
        //        mLocationSettingsRequest = builder.build();

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

            if (savedInstanceState.containsKey("last_updated_on")) {
                mLastUpdateTime = savedInstanceState.getString("last_updated_on");
            }
        }

        //updateLocationUI();
    }

    /**
     * Update the UI displaying the location data
     * and toggling the buttons
     */
    //    private void updateLocationUI()
    //    {
    //
    //        if (mCurrentLocation != null) {
    //            //txtLocationResult.setText("Lat: " + mCurrentLocation.getLatitude() + ", " + "Lng: " + mCurrentLocation.getLongitude());
    //
    //            // giving a blink animation on TextView
    //            //txtLocationResult.setAlpha(0);
    //            //txtLocationResult.animate().alpha(1).setDuration(300);
    //
    //            latLng = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
    //
    //            //Toast.makeText(getApplicationContext(), "Lat: " + mCurrentLocation.getLatitude() + ", " + "Lng: " + mCurrentLocation.getLongitude(), Toast.LENGTH_SHORT).Reply();
    //
    //            // location last updated time
    //            //txtUpdatedOn.setText("Last updated on: " + mLastUpdateTime);
    //        }
    //    }

    /**
     * Starting location updates
     * Check whether location settings are satisfied and then
     * location updates will be requested
     */
    //    private void startLocationUpdates()
    //    {
    //
    //        mSettingsClient.checkLocationSettings(mLocationSettingsRequest).addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>()
    //        {
    //
    //            @SuppressLint("MissingPermission")
    //            @Override
    //            public void onSuccess(LocationSettingsResponse locationSettingsResponse)
    //            {
    //                Log.i(TAG, "All location settings are satisfied.");
    //
    //                //Toast.makeText(getApplicationContext(), "Started location updates!", Toast.LENGTH_SHORT).Reply();
    //
    //                //noinspection MissingPermission
    //                mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
    //
    //                updateLocationUI();
    //            }
    //
    //        }).addOnFailureListener(this, new OnFailureListener()
    //        {
    //            @Override
    //            public void onFailure(@NonNull Exception e)
    //            {
    //                int statusCode = ((ApiException) e).getStatusCode();
    //                switch (statusCode) {
    //                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
    //
    //                        /*
    //                        Log.i(TAG, "Location settings are not satisfied. Attempting to upgrade " + "location settings ");
    //                        try {
    //                            // Show the dialog by calling startResolutionForResult(), and check the
    //                            // result in onActivityResult().
    //                            ResolvableApiException rae = (ResolvableApiException) e;
    //                            rae.startResolutionForResult(LaunchActivity.this, REQUEST_CHECK_SETTINGS);
    //                        } catch (IntentSender.SendIntentException sie) {
    //                            Log.i(TAG, "PendingIntent unable to execute request.");
    //                        }
    //                        break;
    //                        */
    //
    //                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
    //                        String errorMessage = "Location settings are inadequate, and cannot be " + "fixed here. Fix in Settings.";
    //                        //Log.e(TAG, errorMessage);
    //
    //                        //Toast.makeText(LaunchActivity.this, errorMessage, Toast.LENGTH_LONG).Reply();
    //                }
    //
    //                updateLocationUI();
    //            }
    //        });
    //    }
    //
    //    public void stopLocationButtonClick()
    //    {
    //        mRequestingLocationUpdates = false;
    //        stopLocationUpdates();
    //    }
    //
    //    public void stopLocationUpdates()
    //    {
    //        // Removing location updates
    //        mFusedLocationClient.removeLocationUpdates(mLocationCallback).addOnCompleteListener(this, new OnCompleteListener<Void>()
    //        {
    //            @Override
    //            public void onComplete(@NonNull Task<Void> task)
    //            {
    //                Toast.makeText(getApplicationContext(), "Location updates stopped!", Toast.LENGTH_SHORT).show();
    //            }
    //        });
    //    }
    //
    //    public void showLastKnownLocation()
    //    {
    //        if (mCurrentLocation != null) {
    //            Toast.makeText(getApplicationContext(), "Lat: " + mCurrentLocation.getLatitude() + ", Lng: " + mCurrentLocation.getLongitude(), Toast.LENGTH_LONG).show();
    //        } else {
    //            Toast.makeText(getApplicationContext(), "Last known location is not available!", Toast.LENGTH_SHORT).show();
    //        }
    //    }
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


    private ServiceConnection boundServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

            WebSocketService.MyBinder binderBridge = (WebSocketService.MyBinder) service;
            webSocketService = binderBridge.getService();
            isBound = true;

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

            isBound = false;
            webSocketService = null;

        }
    };

}
