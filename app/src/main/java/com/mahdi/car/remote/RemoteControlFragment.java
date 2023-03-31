package com.mahdi.car.remote;

import android.annotation.SuppressLint;
import android.content.Context;
import android.provider.Settings;
import android.view.Gravity;
import android.view.View;

import com.mahdi.car.core.BaseFragment;
import com.mahdi.car.remote.components.TouchView;
import com.mahdi.car.server.https.Server;
import com.mahdi.car.share.CircleButton;
import com.mahdi.car.share.OvalButton;
import com.mahdi.car.share.Themp;
import com.mahdi.car.share.component.ui.LayoutHelper;

public class RemoteControlFragment extends BaseFragment {

    TouchView touchView = null;

    private String android_id = null;

    private CircleButton btnMute;
    private CircleButton btnMenu;

    private CircleButton btnPlayPause;
    private CircleButton btnHome;
    private CircleButton btnBack;

    private OvalButton btnOval;

    @SuppressLint("HardwareIds")
    @Override
    public View createView(Context context) {
        loadingShow = false;
        config.hasServerData = false;

        super.createView(context);

        android_id = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);

        toolbar.setName("Remote Control");
        toolbar.settMirror();

//        User user = new User();
//        user.ID = 9;
//        user.is_verified = true;
//        user.Username = "Mahdi";
//        user.Avatar = null;
//
//        mirrorPhotoView = new MirrorPhotoView(context, true);
//        mirrorPhotoView.setUser(user);
//        mirrorPhotoView.setDelegate(new MirrorPhotoView.Delegate() {
//            @Override
//            public void button() {
//
//            }
//        });

        touchView = new TouchView(context, android_id);
        contentView.addView(touchView, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, 470, Gravity.TOP, 10, 90, 10, 0));





        btnMute = new CircleButton(context);
        btnMute.setIcon(Themp.toolbar.mute);
        contentView.addView(btnMute, LayoutHelper.createFrame(70, 70, Gravity.TOP, 30, 70, 0, 0));

        btnHome = new CircleButton(context);
        btnHome.setIcon(Themp.mainToolBarIcons[0]);
        contentView.addView(btnHome, LayoutHelper.createFrame(70, 70, Gravity.BOTTOM, 30, 0, 0, 220));

        btnPlayPause = new CircleButton(context);
        btnPlayPause.setIcon(Themp.toolbar.play_pause_78);
        contentView.addView(btnPlayPause, LayoutHelper.createFrame(70, 70, Gravity.BOTTOM, 30, 0, 0, 140));



        btnMenu = new CircleButton(context);
        btnMenu.setIcon(Themp.toolbar.menu);
        contentView.addView(btnMenu, LayoutHelper.createFrame(70, 70, Gravity.TOP | Gravity.RIGHT, 0, 70, 30, 0));


        btnOval = new OvalButton(context);
        btnOval.setIcons(Themp.toolbar.keyboard_arrow_up, Themp.toolbar.keyboard_arrow_down);
        contentView.addView(btnOval, LayoutHelper.createFrame(70, 150, Gravity.BOTTOM | Gravity.RIGHT, 100, 0, 30, 140));


        btnBack = new CircleButton(context);
        btnBack.setIcon(Themp.toolbar.arrow_back_large);
        contentView.addView(btnBack, LayoutHelper.createFrame(150, 150, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0, 0, 140));


        return contentView;
    }

    @Override
    public void serverSend() {
        super.serverSend();
        server(Server.bookmark.getAllCollections());
    }
}


