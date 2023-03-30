package com.mahdi.car.mirror;

import android.annotation.SuppressLint;
import android.content.Context;
import android.provider.Settings;
import android.view.Gravity;
import android.view.View;

import com.mahdi.car.core.BaseFragment;
import com.mahdi.car.mirror.components.MirrorPhotoView;
import com.mahdi.car.mirror.components.WelcomeView;
import com.mahdi.car.server.https.Server;
import com.mahdi.car.server.model.User;
import com.mahdi.car.share.component.ui.LayoutHelper;

public class MirrorScreenFragment extends BaseFragment {

    WelcomeView welcomeView = null;

    private String android_id = null;
    private MirrorPhotoView mirrorPhotoView;

    @SuppressLint("HardwareIds")
    @Override
    public View createView(Context context) {
        loadingShow = false;
        config.hasServerData = false;

        super.createView(context);

        android_id = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);

        toolbar.setName("Mirror Screen");
        toolbar.settMirror();

        User user = new User();
        user.ID = 9;
        user.is_verified = true;
        user.Username = "Mahdi";
        user.Avatar = null;

        mirrorPhotoView = new MirrorPhotoView(context, true);
        mirrorPhotoView.setUser(user);
        mirrorPhotoView.setDelegate(new MirrorPhotoView.Delegate() {
            @Override
            public void button() {

            }
        });

        welcomeView = new WelcomeView(context, android_id);

        contentView.addView(mirrorPhotoView, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, 600, Gravity.TOP, 0, 50, 0, 0));

        return contentView;
    }

    @Override
    public void serverSend() {
        super.serverSend();
        server(Server.bookmark.getAllCollections());
    }
}


