package com.mahdi.car.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.mahdi.car.WebSocket;

import java.net.URI;
import java.net.URISyntaxException;

// Android Bound Services
// https://medium.com/@anant.rao07/android-bound-services-f1cceb2f1f3e

public class WebSocketService extends Service {

    WebSocket webSocket = null;

    //Instance of inner class created to provide access  to public methods in this class
    private final IBinder localBinder = new MyBinder();

    @Override
    public void onCreate() {

    }

    /**
     * Called by the system to notify a Service that it is no longer     used and is being removed.  The
     * service should clean up any resources it holds (threads,       registered
     * receivers, etc) at this point.  Upon return, there will be no more calls
     * in to this Service object and it is effectively dead.
     */
    @Override
    public void onDestroy() {

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return localBinder;
    }

    /**
     * This method is  Called when activity have disconnected from a particular interface published by the service.
     * Note: Default implementation of the  method just  return false
     */
    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    /**
     * Called when an activity is connected to the service, after it had
     * previously been notified that all had disconnected in its
     * onUnbind method.  This will only be called by system if the implementation of onUnbind method was overridden to return true.
     */
    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
    }

    public int startServer(String serverIP) {
        try {

            if (webSocket == null) {
                webSocket = new WebSocket(new URI("ws://" + serverIP + ":8097"));
                webSocket.setDelegate(new WebSocket.Delegate() {
                    @Override
                    public void onOpen() {
                        broadcastIntentOpened();
                        Log.i("WebSocket", "WebSocket opened");
                    }

                    @Override
                    public void onClose() {
                        broadcastIntentClosed();
                    }

                    @Override
                    public void onMessage(String message) {
                        broadcastIntentMessage(message);
                    }
                });

                webSocket.connect();
                return 1;

            } else if (!webSocket.isOpen()) {
                webSocket.reconnect();
                return 2;
            }

        } catch (URISyntaxException e) {
            //throw new RuntimeException(e);
        }
        return 3;
    }

    public boolean isOpened() {
        if (webSocket != null && webSocket.isOpen()) {
            return true;
        } else {
            return false;
        }
    }

    public void send(String message) {
        if (webSocket != null && webSocket.isOpen()) {
            webSocket.send(message);
        }
    }

    public class MyBinder extends Binder {
        public WebSocketService getService() {
            return WebSocketService.this;

        }
    }

    private void broadcastIntentOpened() {
        Intent intent = new Intent("mahdi.websocket");
        intent.putExtra("command", "opened");
        sendBroadcast(intent);
    }

    private void broadcastIntentClosed() {
        Intent intent = new Intent("mahdi.websocket");
        intent.putExtra("command", "closed");
        sendBroadcast(intent);
    }

    private void broadcastIntentMessage(String message) {
        Intent intent = new Intent("mahdi.websocket");
        intent.putExtra("command", "onMessage");
        intent.putExtra("message", message);
        sendBroadcast(intent);
    }
}
