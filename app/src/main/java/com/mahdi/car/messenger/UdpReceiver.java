package com.mahdi.car.messenger;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class UdpReceiver extends BroadcastReceiver {

    public interface Delegate {
        void receive(String sender, String message);
    }

    private Delegate delegate = null;

    public void setDelegate(Delegate delegate) {
        this.delegate = delegate;
    }

    public UdpReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        String message = intent.getStringExtra("message");
        String sender = intent.getStringExtra("sender");

        if (delegate != null)
            delegate.receive(sender, message);
    }
}
