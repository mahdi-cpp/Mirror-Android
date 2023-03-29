package com.mahdi.car.messenger;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


public class NotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null) {
            return;
        }

        //Application.sendNotification("ff", "gtgtf", null);

    }

}
