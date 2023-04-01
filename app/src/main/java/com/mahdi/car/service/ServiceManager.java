package com.mahdi.car.service;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.IntentFilter;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.mahdi.car.core.RootView;
import com.mahdi.car.messenger.UdpReceiver;
import com.mahdi.car.messenger.WebSocketReceiver;
import com.mahdi.car.model.Mirror;
import com.mahdi.car.model.Music;
import com.mahdi.car.model.Resource;

public class ServiceManager {

    UdpReceiver udpReceiver = null;
    WebSocketReceiver webSocketReceiver = null;
    IntentFilter udpIntentFilter;
    IntentFilter webSocketIntentFilter;

    private UdpThread udpThread;
    private WebSocketThread webSocketThread;

    private Resource resource;

    private Activity activity;

    private Resource getResource() {
        return resource;
    }

    private void setMirror(Mirror mirror) {
        resource.mirror = mirror;
    }

    private void setMusic(Music music) {
        resource.music = music;
    }

    @SuppressLint("StaticFieldLeak")
    private static volatile ServiceManager Instance = null;

    public static ServiceManager instance() {
        ServiceManager localInstance = Instance;
        if (localInstance == null) {
            synchronized (ServiceManager.class) {
                localInstance = Instance;
                if (localInstance == null) {
                    Instance = localInstance = new ServiceManager();
                }
            }
        }
        return localInstance;
    }

    public void init(Activity activity) {

        this.activity = activity;

        if (webSocketThread != null) {
            return;
        }

        webSocketThread = new WebSocketThread(activity.getApplicationContext());
        udpThread = new UdpThread(activity.getApplicationContext());
        udpThread.start();

        udpReceiver = new UdpReceiver();
        udpReceiver.setDelegate((senderIP, message) -> {
            //Log.e("udpReceiver", "udp receiver senderIP:" + senderIP);
            if (webSocketThread != null && !webSocketThread.isOpened()) {
                Log.e("udpReceiver", "webSocketThread.open()");
                webSocketThread.open(senderIP);
            }
        });

        webSocketReceiver = new WebSocketReceiver();
        webSocketReceiver.setDelegate(new WebSocketReceiver.Delegate() {
            @Override
            public void onOpened() {
                RootView.instance().onWebSocketOpened();
            }

            @Override
            public void onClose() {
                RootView.instance().onWebSocketClosed();
            }

            @Override
            public void onMessage(String message) {
                try {
                    Log.d("webSocketReceiver", message);

                    Gson gson = new GsonBuilder().disableHtmlEscaping().create();
                    Resource resource = gson.fromJson(message, Resource.class);

                    if (resource.mirror.username.length() > 3) {
                        RootView.instance().floatViewParent.show(resource.mirror.username, resource.mirror.title);
                    }

                } catch (JsonSyntaxException e) {

                } catch (NullPointerException e) {
                    Log.e("onWebSocketReceive", "NullPointerException: " + e.getMessage());
                }
                RootView.instance().onWebSocketReceive(message);
            }
        });

        udpIntentFilter = new IntentFilter("udp.mahdi");
        webSocketIntentFilter = new IntentFilter("mahdi.websocket");

        activity.registerReceiver(udpReceiver, udpIntentFilter);
        activity.registerReceiver(webSocketReceiver, webSocketIntentFilter);
    }

    public boolean webSocketIsOpen() {
        if (webSocketThread != null) {
            return webSocketThread.isOpened();
        }

        return false;
    }

    public void webSocketSend(String json) {
        ///webSocketService.send(json);
    }

    public void onDestroy() {
        try {
            activity.unregisterReceiver(udpReceiver);
            activity.unregisterReceiver(webSocketReceiver);

            udpThread.stop();
            webSocketThread.destroy();

        } catch (Throwable e) {

        }
    }


}
