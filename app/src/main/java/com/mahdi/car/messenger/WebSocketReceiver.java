package com.mahdi.car.messenger;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class WebSocketReceiver extends BroadcastReceiver {

    public interface Delegate {
        void onOpened();

        void onClose();

        void onMessage(String message);
    }

    private Delegate delegate = null;

    public void setDelegate(Delegate delegate) {
        this.delegate = delegate;
    }

    public WebSocketReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        String command = intent.getStringExtra("command");

        if (command.contains("opened")) {
            if (delegate != null)
                delegate.onOpened();
        } else if (command.contains("closed")) {
            if (delegate != null)
                delegate.onClose();
        } else if (command.contains("onMessage")) {
            String message = intent.getStringExtra("message");
            if (delegate != null)
                delegate.onMessage(message);
        }
    }
}
