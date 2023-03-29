package com.mahdi.car.messenger;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


public class AppStartReceiver extends BroadcastReceiver
{

    public void onReceive(Context context, Intent intent)
    {
        String ip = intent.getStringExtra("ip");
        String message = intent.getStringExtra("message");
        //NotificationCenter.getInstance().postNotificationName(NotificationCenter.udpEsp8266Event, ip, message);

    }

}
