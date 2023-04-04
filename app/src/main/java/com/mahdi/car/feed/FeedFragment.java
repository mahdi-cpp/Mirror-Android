package com.mahdi.car.feed;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.Gravity;
import android.view.View;

import androidx.interpolator.view.animation.LinearOutSlowInInterpolator;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mahdi.car.App;
import com.mahdi.car.core.BaseFragment;
import com.mahdi.car.core.QZoomView;
import com.mahdi.car.core.RootView;
import com.mahdi.car.feed.components.CastButton;
import com.mahdi.car.feed.components.FeedView;
import com.mahdi.car.model.Mirror;
import com.mahdi.car.service.ClientRequest;
import com.mahdi.car.service.ServerResponse;
import com.mahdi.car.service.ServiceManager;
import com.mahdi.car.setting.SettingView;
import com.mahdi.car.share.component.ui.LayoutHelper;
import com.mahdi.car.story.camera.StoryCameraView;

public class FeedFragment extends BaseFragment {

    private SettingView settingView;
    private StoryCameraView storyCameraView;

    private boolean isAnimation = false;

    private CastButton mirrorButton;

    FeedView feedView;

    @Override
    public View createView(Context context) {

        config.hasServerData = false;
        super.createView(context);
        swipe.setScrollEnabled(false);

        toolbar.settFeed();
        toolbar.setTransparent(false);
        toolbar.setName("");

        settingView = new SettingView(context);
        settingView.setTranslationX(width);
        settingView.setDelegate(new SettingView.Delegate() {
            @Override
            public void leftPressed() {
                hideSettingView(0);
            }
        });
        contentView.addView(settingView, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT));

        mirrorButton = new CastButton(context);
        mirrorButton.setVisibility(View.INVISIBLE);
        mirrorButton.setTitle("Screen Mirror");
        mirrorButton.setDelegate((CastButton.Delegate) () -> {

            if (mirrorButton.isConnected()) {
                return;
            }

            if (ServiceManager.instance().webSocketIsOpen()) {

                Mirror mirror = new Mirror();
                //String androidId = Settings.Secure.getString(getParentActivity().getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);

                mirror.clientRequest = ClientRequest.CLIENT_REQUEST_MIRROR;
                mirror.wifiIp = ServiceManager.instance().getWifiIp();
                mirror.username = "Mahdi Abdolmaleki";
                mirror.title = "Start Screen Mirror ...";
                mirror.bitrate = "2 Mbit";
                mirror.resolution = "Full HD";
                mirror.connectionType = "WiFi";

                Gson gson = new GsonBuilder().disableHtmlEscaping().create();
                ServiceManager.instance().webSocketSend(gson.toJson(mirror, Mirror.class));
            }
        });


        feedView = new FeedView(context);

        swipe.addView(feedView, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT, Gravity.TOP, 0, 0, 0, 0));
        swipe.addView(mirrorButton, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, 90, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 40, 0, 40, 170));


        parentView.invalidate();
        return contentView;
    }

    @Override
    public void onWebSocketOpened() {
        super.onWebSocketOpened();
        feedView.setConnection(true);
        mirrorButton.setVisibility(View.VISIBLE);
    }

    @Override
    public void onWebSocketClosed() {
        super.onWebSocketClosed();

        feedView.setConnection(false);
        RootView.instance().floatViewParent.hide();
        mirrorButton.disconnect();
        mirrorButton.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onWebSocketReceive(String jsonString) {
        super.onWebSocketReceive(jsonString);
    }

    @Override
    public void onServerEvents(int serverResult) {
        super.onServerEvents(serverResult);

        mirrorButton.setVisibility(View.VISIBLE);

        if (serverResult == ServerResponse.MIRROR_SUCCESS_START) {
            mirrorButton.setTitle("Mirror Started");
        } else if (serverResult == ServerResponse.MIRROR_FINISHED) {
            mirrorButton.setTitle("Mirror Finished");
        } else if (serverResult == ServerResponse.MIRROR_ERROR_START) {
            mirrorButton.setTitle("MIRROR_ERROR_START");
        }

        if (RootView.instance().floatViewParent.isShow()) {
            mirrorButton.connect(RootView.instance().floatViewParent.mirror.connectionType);
        } else {
            mirrorButton.disconnect();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (ServiceManager.instance().webSocketIsOpen()) {
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

        if (storyCameraView != null && storyCameraView.isShow()) {
            storyCameraView.onBackPressed();
            return false;
        }

        return true;
    }

    public void showSettingView() {

        if (isAnimation) {
            return;
        }

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
            }
        });
        animatorSet.start();
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


}

