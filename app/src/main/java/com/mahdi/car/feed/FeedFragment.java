package com.mahdi.car.feed;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.mahdi.car.App;
import com.mahdi.car.core.BaseFragment;
import com.mahdi.car.core.QZoomView;
import com.mahdi.car.core.RootView;
import com.mahdi.car.dialog.popup.QDialog;
import com.mahdi.car.setting.SettingView;
import com.mahdi.car.library.viewAnimator.ViewAnimator;
import com.mahdi.car.model.Person;
import com.mahdi.car.model.State;
import com.mahdi.car.server.https.Server;
import com.mahdi.car.server.model.Post;
import com.mahdi.car.server.model.User;
import com.mahdi.car.share.Button;
import com.mahdi.car.share.CustomLinearLayoutManager;
import com.mahdi.car.share.component.ui.LayoutHelper;
import com.mahdi.car.share.component.ui.RangeSeekBar;
import com.mahdi.car.story.camera.StoryCameraView;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class FeedFragment extends BaseFragment {

    private SettingView settingView;

    private StoryCameraView storyCameraView;

    private CustomLinearLayoutManager layoutManager;

    private List<User> stories = new ArrayList<>();
    private List<Post> posts = new ArrayList<>();
    private HashMap<Integer, Integer> hashMap = new HashMap<>();

    private boolean checkPermission = true;

    private boolean isAnimation = false;
    private boolean isSettingShow = false;

    private RangeSeekBar rangeSeekBar;

    private Button btnDisconnect;
    private Button btnConnection;

    @Override
    public View createView(Context context) {

        config.hasServerData = false;

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

        QDialog.getInstance().setParentActivity(getParentActivity());

        settingView = new SettingView(context);
        settingView.setTranslationX(width);
        settingView.setDelegate(new SettingView.Delegate() {
            @Override
            public void leftPressed() {
                hide(0);
            }
        });
        contentView.addView(settingView, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT));

        rangeSeekBar = new RangeSeekBar(context);
        //contentView.addView(rangeSeekBar, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, 100, Gravity.TOP, 20, 200, 20, 0));

        btnConnection = new Button(context);
        btnConnection.setTitle("Connect");
        btnConnection.setDelegate((Button.Delegate) () -> {
            getParentActivity().websocketStart();
            RootView.instance().showFloatView("Mahdi Abdolmaleki", "start show phone screen on car display");
        });

        btnDisconnect = new Button(context);
        btnDisconnect.setTitle("Disconnect");
        btnDisconnect.setDelegate((Button.Delegate) () -> {
            Gson gson = new GsonBuilder().disableHtmlEscaping().create();
            Person person = new Person("12", "Ali", "" + new Date().getTime(), 35);
            getParentActivity().webSocketSend(gson.toJson(person, Person.class));

            RootView.instance().hideFloatView();
        });

        swipe.addView(btnConnection, LayoutHelper.createFrame(220, 40, Gravity.TOP, 100, 100, 0, 0));
        swipe.addView(btnDisconnect, LayoutHelper.createFrame(220, 40, Gravity.TOP, 100, 170, 0, 0));

        parentView.invalidate();

        //Log.e("Gson", "json:" + gson.toJson(obj, Person.class));

//        Person person = gson.fromJson("{\"age\":35,\"name\":\"Ali\",\"phone\":\"09355512619\"}", Person.class);
//        Log.e("Gson", "person:" + person.age);

        return contentView;
    }

    @Override
    public void toolbarLeftPressed() {
        if (settingView.isShow()) {
            hide(0);
        } else {
            if (storyCameraView == null) {
                storyCameraView = new StoryCameraView(App.context, getParentActivity(), contentView);
                QZoomView.getInstance().addCamera(storyCameraView);
            }
            storyCameraView.show();
        }
    }

    @Override
    public void toolbarRightPressed() {
        showSettingView();
    }

    @Override
    public void onWebSocketReceive(String jsonString) {
        super.onWebSocketReceive(jsonString);

        try {
            Log.d("onWebSocketReceive", jsonString);
//            JsonObject jsonObject = new Gson().fromJson(jsonString, JsonObject.class);
//            toolbar.setName(jsonObject.get("name").toString());

            Gson gson = new GsonBuilder().disableHtmlEscaping().create();
            State state = gson.fromJson(jsonString, State.class);
            toolbar.setName(state.music.coverPath);

        } catch (JsonSyntaxException e) {

        } catch (NullPointerException e) {
            Log.e("onWebSocketReceive", "NullPointerException: " + e.getMessage());
        }
    }

    private void processScroll(MotionEvent e1, float distanceX, float distanceY) {
        dX -= distanceX;
        dY -= distanceY;

        if (Math.abs(dX) > Math.abs(dY) && !swipe.isSwipe()) {

            layoutManager.setScrollEnabled(false);
            swipe.setScrollEnabled(false);

            swipe.recyclerView.setTranslationX(dX);
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
            settingView.show();
        } else {
            settingView.reset();
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
        if (settingView != null)
            settingView.endStoryShow();

//        storiesListView.endStoryShow();
    }

    public void showSettingView() {

        RootView.instance().floatViewVisible(0x00000004); //invisible

        if (isAnimation) {
            return;
        }

        isSettingShow = true;
        isAnimation = true;
        settingView.setShow(true);

        ObjectAnimator objectAnimator1 = ObjectAnimator.ofFloat(swipe, "translationX", -width);
        ObjectAnimator objectAnimator2 = ObjectAnimator.ofFloat(settingView, "translationX", 0);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(objectAnimator1, objectAnimator2);
        animatorSet.setInterpolator(new LinearOutSlowInInterpolator());
        animatorSet.setDuration(300);
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                isAnimation = false;
            }
        });
        animatorSet.start();
    }

    public void hide(int delay) {
        if (isAnimation) {
            return;
        }

        settingView.setShow(false);
        isSettingShow = false;
        isAnimation = true;

        ObjectAnimator objectAnimator1 = ObjectAnimator.ofFloat(swipe, "translationX", 0);
        ObjectAnimator objectAnimator2 = ObjectAnimator.ofFloat(settingView, "translationX", width);

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
                RootView.instance().floatViewVisible(0); //visible
            }
        });
        animatorSet.start();
    }

    @Override
    protected void onFragmentDestroy() {
        super.onFragmentDestroy();


    }

    @Override
    public void onResume() {
        super.onResume();

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

        if (settingView.isShow()) {
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

