package com.mahdi.car.messenger;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.mahdi.car.App;


public class ScreenReceiver extends BroadcastReceiver
{

    @Override public void onReceive(Context context, Intent intent)
    {
        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
            FileLog.e("screen off");
            App.isScreenOn = false;
        } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
            FileLog.e("screen on");
            App.isScreenOn = true;
        }
        NotificationCenter.getInstance().postNotificationName(NotificationCenter.screenStateChanged);
    }
}
