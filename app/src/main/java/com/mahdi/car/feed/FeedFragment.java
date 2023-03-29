package com.mahdi.car.feed;

import static android.content.Context.BIND_AUTO_CREATE;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator;
import androidx.recyclerview.widget.RecyclerView;

import com.mahdi.car.core.BaseFragment;
import com.mahdi.car.core.FatherView;
import com.mahdi.car.dialog.popup.QDialog;
import com.mahdi.car.direct.DirectView;
import com.mahdi.car.feed.cell.MirrorPhotoView;
import com.mahdi.car.library.viewAnimator.ViewAnimator;
import com.mahdi.car.messenger.UdpReceiver;
import com.mahdi.car.messenger.WebSocketReceiver;
import com.mahdi.car.server.dtos.FeedDTO;
import com.mahdi.car.server.https.Server;
import com.mahdi.car.server.model.Post;
import com.mahdi.car.server.model.User;
import com.mahdi.car.service.UDPListenerService;
import com.mahdi.car.service.WebSocketService;
import com.mahdi.car.share.CustomLinearLayoutManager;
import com.mahdi.car.share.component.ui.LayoutHelper;
import com.mahdi.car.story.camera.StoryCameraView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class FeedFragment extends BaseFragment {
    private DirectView directView;

    private StoryCameraView storyCameraView;

    private CustomLinearLayoutManager layoutManager;

    private List<User> stories = new ArrayList<>();
    private List<Post> posts = new ArrayList<>();
    private HashMap<Integer, Integer> hashMap = new HashMap<>();

    private boolean checkPermission = true;

    private GestureDetector gestureDetector;

    private boolean isAnimation = false;
    private boolean isShowDirect = false;

    private MirrorPhotoView mirrorPhotoView;


    UdpReceiver udpReceiver = null;
    WebSocketReceiver webSocketReceiver = null;

    IntentFilter udpIntentFilter;
    IntentFilter webSocketIntentFilter;

    String ubuntu_ip = null;

    //BoundService class Objet
    WebSocketService webSocketService;
    //boolean variable to keep a check on service bind and unbind event
    boolean isBound = false;

    @Override
    public View createView(Context context) {

        adapter = new RecyclerView.Adapter() {
            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return null;
            }

            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

            }

            @Override
            public int getItemCount() {
                return 0;
            }
        };

//        super.linearLayoutManager = layoutManager = new CustomLinearLayoutManager(context);
        super.createView(context);

        toolbar.settFeed();
        toolbar.setTransparent(false);
        toolbar.setName("Car Application");

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
                toolbar.setName("connected");
            }

            @Override
            public void onClose() {
                toolbar.setName("closed");
            }

            @Override
            public void onMessage(String message) {

            }
        });

        udpIntentFilter = new IntentFilter("udp.mahdi");
        webSocketIntentFilter = new IntentFilter("mahdi.websocket");

        getParentActivity().registerReceiver(udpReceiver, udpIntentFilter);
        getParentActivity().registerReceiver(webSocketReceiver, webSocketIntentFilter);

        QDialog.getInstance().setParentActivity(getParentActivity());

        //        storiesView = new StoriesView(context);
        //        storiesView.setAli(new StoriesView.Delegate()
        //        {
        //            @Override
        //            public void permissionSwipe(boolean enabled)
        //            {
        //                permissionSwipe = enabled;
        //                swipe.setScrollEnabled(false);
        //                CustomLinearLayoutManager a = (CustomLinearLayoutManager) linearLayoutManager;
        //                a.setScrollEnabled(enabled);
        //            }
        //        });

        gestureDetector = new GestureDetector(context, new GestureDetector.OnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }

            @Override
            public void onShowPress(MotionEvent e) {

            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return false;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                processScroll(e1, distanceX, distanceY);
                return false;
            }

            @Override
            public void onLongPress(MotionEvent e) {

            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                velocity = velocityX;
                return false;
            }

        });

        User user = new User();
        user.ID = 9;
        user.is_verified = true;
        user.Username = "Mahdi";
        user.Avatar = "/profile-photos/meisamca_212534697_346885453492622_2753089786031568080_n.jpg";

        mirrorPhotoView = new MirrorPhotoView(context, true);
        mirrorPhotoView.setUser(user);
        mirrorPhotoView.setDelegate(new MirrorPhotoView.Delegate() {
            @Override
            public void posts() {

            }

            @Override
            public void follower() {

            }

            @Override
            public void following() {

            }

            @Override
            public void edit() {

            }
        });
        swipe.addView(mirrorPhotoView, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, 600, Gravity.TOP, 0, 48, 0, 0));


        //  WelcomeViewPager welcomeViewPager = new WelcomeViewPager(context);
        //  contentView.addView(welcomeViewPager, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT, Gravity.TOP, 0, 48, 0, 48));


        directView = new DirectView(context);
        directView.setTranslationX(width);
        directView.setDelegate(new DirectView.Delegate() {
            @Override
            public void leftPressed() {
                hide(0);
            }
        });
        contentView.addView(directView, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT));

        parentView.invalidate();

        Intent intent = new Intent(getParentActivity(), WebSocketService.class);
        getParentActivity().startService(intent);
        getParentActivity().bindService(intent, boundServiceConnection, BIND_AUTO_CREATE);

        return contentView;
    }

    @Override
    public void toolbarLeftPressed() {

        webSocketService.send("Android: " + new Date().getTime());

//        if (directView.isShow()) {
//            hide(0);
//        } else {
//            if (storyCameraView == null) {
//                storyCameraView = new StoryCameraView(App.context, getParentActivity(), contentView);
//                QZoomView.getInstance().addCamera(storyCameraView);
//            }
//
//            storyCameraView.show();
//        }

//        if (webSocket.isOpen()) {
//            webSocket.send("Android: Hello Qml");
//        }

    }

    @Override
    public void toolbarCenterPressed() {
        //Intent serviceIntent = new Intent();
        //serviceIntent.setAction("com.mahdi.car.service.UDPListenerService");
        getParentActivity().startService(new Intent(getParentActivity(), UDPListenerService.class));


//        if (webSocket.isOpen()) {
//            webSocket.send("Mahdi Abdolmaleki");
//        } else {
//            try {
//                webSocket = new WebSocket(new URI("ws://localhost:8095"));
//            } catch (URISyntaxException e) {
//                throw new RuntimeException(e);
//            }
//            webSocket.connect();
//        }

        //        try {
        //            //Trim.trimVideo("/storage/emulated/0/DCIM/Screenshots/ali.mp4", "/storage/emulated/0/DCIM/Screenshots/ali4.mp4", 1, 45);
        //        } catch (IOException e) {
        //            e.printStackTrace();
        //        }

        //        presentFragment(new ExploreIGTVFragment());
        //
        //        long startTime = System.currentTimeMillis();
        //
        //        Observer observer = new Observer()
        //        {
        //            @Override
        //            public void onSubscribe(@NonNull Disposable d)
        //            {
        //
        //            }
        //
        //            @Override
        //            public void onNext(Object o)
        //            {
        //                User user = (User) o;
        //                Log.e("onNext:", " " + user.username + " " + (System.currentTimeMillis() - startTime));
        //            }
        //
        //            @Override
        //            public void onError(@NonNull Throwable e)
        //            {
        //
        //            }
        //
        //            @Override
        //            public void onComplete()
        //            {
        //
        //            }
        //        };
        //
        //        //Timer
        //        //Observable.fromIterable(stories).filter(x -> x.userid > 1).zipWith(Observable.interval(50, TimeUnit.MILLISECONDS), (item, interval) -> item).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(observer);
        //
        //        Observable.fromIterable(stories).filter(x -> x.userid > 1).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(observer);
    }

    @Override
    public void toolbarRightPressed() {
        //Toast.makeText(getParentActivity(), String.valueOf(webSocketService.randomGenerator()),Toast.LENGTH_SHORT).show();

        int state = webSocketService.startServer("192.168.1.113");
        //showDirect();
    }

    //    @Override
    //    public boolean onInterceptTouchEvent(MotionEvent event)
    //    {
    //        gestureDetector.onTouchEvent(event);
    //
    //        switch (event.getAction()) {
    //
    //            case MotionEvent.ACTION_DOWN:
    //                //                if (dX != 0) {
    //                //                    //resetX();
    //                //                }
    //                dX = 0;
    //                break;
    //            case MotionEvent.ACTION_MOVE:
    //
    //                break;
    //
    //            case MotionEvent.ACTION_UP:
    //            case MotionEvent.ACTION_POINTER_UP:
    //            case MotionEvent.ACTION_CANCEL:
    //                resetX();
    //                break;
    //        }
    //
    //        return false;
    //    }

    private void processScroll(MotionEvent e1, float distanceX, float distanceY) {
        dX -= distanceX;
        dY -= distanceY;

        if (Math.abs(dX) > Math.abs(dY) && !swipe.isSwipe()) {

            layoutManager.setScrollEnabled(false);
            swipe.setScrollEnabled(false);

            swipe.recyclerView.setTranslationX(dX);

//            if (dX < 0) {
//            directView.setDirectTranslationX(dX);
//            }
        }
    }

    private void resetX() {
        if (!permissionSwipe) {
            return;
        }

        layoutManager.setScrollEnabled(true);
        swipe.setScrollEnabled(true);

        if (dX < -centerX || velocity < -1000) {
            ViewAnimator.animate(swipe.recyclerView).setInterpolator(new LinearOutSlowInInterpolator()).translationX(-width).setDuration(300).start();
            directView.showDirect();
        } else {
            directView.resetDirect();
            ViewAnimator.animate(swipe.recyclerView).setInterpolator(new LinearOutSlowInInterpolator()).translationX(0).setDuration(300).start();
        }

        velocity = 0;
        permissionSwipe = false;
    }

    @Override
    public void serverSend() {
        super.serverSend();
        //Toast.makeText(contentView.getContext(), "mahdi error ", Toast.LENGTH_LONG).show();
        server(Server.post.getFeed(page * size, size));
    }

    @Override
    public <T> void serverOnResponse(Call<T> call, Response<T> response) {
        super.serverOnResponse(call, response);

        if (response.isSuccessful()) {

            FeedDTO feed = (FeedDTO) response.body();
//            List<Post> feed_posts  = (List<Post>) response.body();

            //Toast.makeText(contentView.getContext(), "response.code():  " + feed.posts.size(), Toast.LENGTH_SHORT).show();

            if (feed != null) {

                //if (page == 0) {

                stories = feed.stories;
                directView.setUsers(stories);
                Collections.shuffle(stories);
                //Collections.shuffle(feed.posts);

                FatherView.instance().setOwner(stories.get(0));
                //FatherView.instance().setOwner(feed.user);

                //App.userid = feed.user.userid;
                //App.cartCount = feed.cartCount;
                //}

                //Toast.makeText(contentView.getContext(), "posts size:  " + posts.size(), Toast.LENGTH_LONG).show();

                for (Post post : feed.posts) {
                    if (post.Medias != null) {
                        if (post.Medias.size() > 0) {
                            //if (post.medias.get(0).video != null) {
                            posts.add(post);
                            //}
                        }
                    }
                }


                //                int videoPosition = 0;
                //                List<Uri> uris = new ArrayList<>();
                //
                //                for (int i = 0; i < posts.size(); i++) {
                //                    if (posts.get(i).medias.get(0).video != null) {
                //                        uris.add(Uri.parse(App.videos + posts.get(i).medias.get(0).video));
                //                        hashMap.put(i, videoPosition);
                //                        videoPosition++;
                //                    }
                //                }
                //
                //                player.addMediaSources(uris);


//                if (feed.posts.size() != size) {
//                    has_next_page = false;
//                }
                page++;

//                adapter.notifyDataSetChanged();
            }

        } else {

            if (response.code() == 403) {

//                LoginFragment loginFragment = new LoginFragment();
//                loginFragment.setDelegate(() -> serverSend());
//                presentFragment(loginFragment);

            } else {
                //Log.e("FeedDTO", "Access denied: " + response.code());
                //Toast.makeText(getParentActivity(), "Access denied: " + response.code(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void swipeScrolled() {
        for (int i = 0; i < swipe.recyclerView.getChildCount(); i++) {

            View view = swipe.recyclerView.getChildAt(i);

//            if (view instanceof PostCell) {
//                PostCell cell = (PostCell) view;
//                cell.setSwipe(swipe.isSwipe());
//            }
        }
    }

    @Override
    public void endStoryShow() {
        super.endStoryShow();
        if (directView != null)
            directView.endStoryShow();

//        storiesListView.endStoryShow();
    }

    public void showDirect() {
        if (isAnimation) {
            return;
        }

        isShowDirect = true;
        isAnimation = true;
        directView.setShow(true);

        ObjectAnimator objectAnimator1 = ObjectAnimator.ofFloat(swipe, "translationX", -width);
        ObjectAnimator objectAnimator2 = ObjectAnimator.ofFloat(directView, "translationX", 0);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(objectAnimator1, objectAnimator2);
        animatorSet.setInterpolator(new LinearOutSlowInInterpolator());
        animatorSet.setDuration(300);
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                isAnimation = false;
                directView.refresh();
            }
        });
        animatorSet.start();
    }

    public void hide(int delay) {
        if (isAnimation) {
            return;
        }

        directView.setShow(false);
        isShowDirect = false;
        isAnimation = true;

        ObjectAnimator objectAnimator1 = ObjectAnimator.ofFloat(swipe, "translationX", 0);
        ObjectAnimator objectAnimator2 = ObjectAnimator.ofFloat(directView, "translationX", width);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setStartDelay(delay);
        animatorSet.playTogether(objectAnimator1, objectAnimator2);
        animatorSet.setInterpolator(new LinearOutSlowInInterpolator());
        animatorSet.setDuration(300);
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                isAnimation = false;
                super.onAnimationEnd(animation);
            }
        });
        animatorSet.start();
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

    @Override
    protected void onFragmentDestroy() {
        super.onFragmentDestroy();

        getParentActivity().unregisterReceiver(udpReceiver);
        getParentActivity().unregisterReceiver(webSocketReceiver);

        if (isBound) {
            getParentActivity().unbindService(boundServiceConnection);
            isBound = false;
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if(webSocketService != null){
            if (webSocketService.isOpened()) {
                Log.d("WebSocket:", "opened");
            } else {
                Log.d("WebSocket:", "closed");
            }
        }


        if (storyCameraView != null)
            storyCameraView.onResume();

        if (checkPermission && Build.VERSION.SDK_INT >= 23) {

            Activity activity = getParentActivity();

            if (activity != null) {
                checkPermission = false;
                askForPermissons();
            }
        }
    }

    @Override
    public boolean onBackPressed() {
        super.onBackPressed();

        if (directView.isShow()) {
            hide(0);
            return false;
        }

//        if (storyView != null && storyView.isShow()) {
//            storyView.hide(storiesListView);
//            return false;
//        }

        if (storyCameraView != null && storyCameraView.isShow()) {
            storyCameraView.onBackPressed();
            return false;
        }

        return true;
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void askForPermissons() {
        Activity activity = getParentActivity();
        if (activity == null) {
            return;
        }
        ArrayList<String> permissons = new ArrayList<>();

        if (activity.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissons.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            permissons.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        String[] items = permissons.toArray(new String[permissons.size()]);
        try {
            activity.requestPermissions(items, 1);
        } catch (Exception ignore) {
        }
    }

    @Override
    public void onRequestPermissionsResultFragment(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 1) {
            for (int a = 0; a < permissions.length; a++) {
                if (grantResults.length <= a || grantResults[a] != PackageManager.PERMISSION_GRANTED) {
                    continue;
                }
                switch (permissions[a]) {
                    case Manifest.permission.CAMERA:
                        //ContactsController.getInstance().forceImportContacts();
                        break;
                    case Manifest.permission.WRITE_EXTERNAL_STORAGE:
                        //ImageUtils.getInstance().checkMediaPaths();
                        break;
                }
            }
        }
    }

}

