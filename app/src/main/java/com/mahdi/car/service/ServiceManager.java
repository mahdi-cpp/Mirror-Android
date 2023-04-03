package com.mahdi.car.service;

import static android.content.Context.WIFI_SERVICE;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.IntentFilter;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.format.Formatter;
import android.util.Log;

import com.mahdi.car.core.RootView;
import com.mahdi.car.messenger.UdpReceiver;
import com.mahdi.car.messenger.WebSocketReceiver;
import com.mahdi.car.model.Mirror;

public class ServiceManager {

    UdpReceiver udpReceiver = null;
    WebSocketReceiver webSocketReceiver = null;
    IntentFilter udpIntentFilter;
    IntentFilter webSocketIntentFilter;

    private UdpThread udpThread;
    private WebSocketThread webSocketThread;

    private Mirror mirror;

    private Activity activity;

    private Mirror getMirror() {
        return mirror;
    }

    private void setMirror(Mirror mirror) {
        this.mirror = mirror;
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
            public void onMessage(String jsonString) {
                Log.d("ServiceManager", "webSocketReceiver:" + jsonString);
                RootView.instance().webSocketReceive(jsonString);
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
        if (webSocketThread != null && webSocketThread.isOpened()) {
            webSocketThread.send(json);
        }
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

    public String getWifiIp() {
        WifiManager wifiMgr = (WifiManager) activity.getApplicationContext().getSystemService(WIFI_SERVICE);
        WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
        int ip = wifiInfo.getIpAddress();
        String ipAddress = Formatter.formatIpAddress(ip);

        Log.e("WiFi ip", "ip:" + ipAddress);
        return ipAddress;
    }

}
