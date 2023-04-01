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
import com.mahdi.car.feed.components.FeedView;
import com.mahdi.car.model.Person;
import com.mahdi.car.model.State;
import com.mahdi.car.setting.SettingView;
import com.mahdi.car.share.Button;
import com.mahdi.car.share.CustomLinearLayoutManager;
import com.mahdi.car.share.component.ui.LayoutHelper;
import com.mahdi.car.share.component.ui.RangeSeekBar;
import com.mahdi.car.story.camera.StoryCameraView;

import java.util.ArrayList;
import java.util.Date;

public class FeedFragment extends BaseFragment {

    private SettingView settingView;

    private StoryCameraView storyCameraView;

    private CustomLinearLayoutManager layoutManager;

    private boolean isAnimation = false;
    private boolean isSettingShow = false;

    private RangeSeekBar rangeSeekBar;

    private Button btnDisconnect;
    private Button btnConnection;

    FeedView feedView;


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
        toolbar.setName("");

        QDialog.getInstance().setParentActivity(getParentActivity());

        settingView = new SettingView(context);
        settingView.setTranslationX(width);
        settingView.setDelegate(new SettingView.Delegate() {
            @Override
            public void leftPressed() {
                hideSettingView(0);
            }
        });
        contentView.addView(settingView, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT));

        rangeSeekBar = new RangeSeekBar(context);
        //contentView.addView(rangeSeekBar, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, 100, Gravity.TOP, 20, 200, 20, 0));

        btnConnection = new Button(context);
        btnConnection.setTitle("Movies");
        btnConnection.setColor(1);
        btnConnection.setDelegate((Button.Delegate) () -> {

            //RootView.instance().showFloatView("Mahdi Abdolmaleki", "start show phone screen on car display");
        });

        btnDisconnect = new Button(context);
        btnDisconnect.setTitle("Music");
        btnDisconnect.setColor(1);
        btnDisconnect.setDelegate((Button.Delegate) () -> {

            Log.e("gggggggggggg", "Disconnect");
            feedView.setConnection(false);

            Gson gson = new GsonBuilder().disableHtmlEscaping().create();
            Person person = new Person("12", "Ali", "" + new Date().getTime(), 35);
            getParentActivity().webSocketSend(gson.toJson(person, Person.class));

            //RootView.instance().hideFloatView();
        });

        feedView = new FeedView(context);

        swipe.addView(feedView, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT, Gravity.TOP, 0, 0, 0, 0));

//        swipe.addView(btnConnection, LayoutHelper.createFrame(200, 40, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0, 0, 170));
//        swipe.addView(btnDisconnect, LayoutHelper.createFrame(200, 40, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0, 0, 100));

        //RootView.instance().showFloatView("Ali", "Music Play");

        parentView.invalidate();
        //Log.e("Gson", "json:" + gson.toJson(obj, Person.class));
//        Person person = gson.fromJson("{\"age\":35,\"name\":\"Ali\",\"phone\":\"09355512619\"}", Person.class);
//        Log.e("Gson", "person:" + person.age);
        return contentView;
    }


    @Override
    public void toolbarLeftPressed() {
        if (settingView.isShow()) {
            hideSettingView(0);
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
    public void onWebSocketOpened() {
        super.onWebSocketOpened();

        feedView.setConnection(true);
        RootView.instance().floatViewParent.show("WebSocket", "opened");
    }

    @Override
    public void onWebSocketClosed() {
        super.onWebSocketClosed();

        feedView.setConnection(false);
        RootView.instance().floatViewParent.hide();
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
            toolbar.setName(state.music.artistName);

        } catch (JsonSyntaxException e) {

        } catch (NullPointerException e) {
            Log.e("onWebSocketReceive", "NullPointerException: " + e.getMessage());
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

        //RootView.instance().floatViewVisible(0x00000004); //invisible

        if (isAnimation) {
            return;
        }

        isSettingShow = true;
        isAnimation = true;
        settingView.setShow(true);

        ObjectAnimator objectAnimator1 = ObjectAnimator.ofFloat(swipe, "translationX", -width);
        ObjectAnimator objectAnimator2 = ObjectAnimator.ofFloat(settingView, "translationX", 0);
        ObjectAnimator objectAnimator3 = ObjectAnimator.ofFloat(RootView.instance().floatViewParent, "translationX", -width);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(objectAnimator1, objectAnimator2, objectAnimator3);
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

    public void hideSettingView(int delay) {
        if (isAnimation) {
            return;
        }

        settingView.setShow(false);
        isSettingShow = false;
        isAnimation = true;

        ObjectAnimator objectAnimator1 = ObjectAnimator.ofFloat(swipe, "translationX", 0);
        ObjectAnimator objectAnimator2 = ObjectAnimator.ofFloat(settingView, "translationX", width);
        ObjectAnimator objectAnimator3 = ObjectAnimator.ofFloat(RootView.instance().floatViewParent, "translationX", 0);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setStartDelay(delay);
        animatorSet.playTogether(objectAnimator1, objectAnimator2, objectAnimator3);
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
    public void onResume() {
        super.onResume();

        if (getParentActivity().webSocketIsOpen()) {
            feedView.setConnection(true);
        } else {
            feedView.setConnection(false);
            RootView.instance().floatViewParent.hide();
        }

        if (storyCameraView != null)
            storyCameraView.onResume();

    }

    @Override
    protected void onFragmentDestroy() {
        super.onFragmentDestroy();
    }

    @Override
    public boolean onBackPressed() {
        super.onBackPressed();

        if (settingView.isShow()) {
            hideSettingView(0);
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

}

