package com.mahdi.car.service;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.mahdi.car.WebSocket;

import java.net.URI;
import java.net.URISyntaxException;

public class WebSocketThread {

    private Context context = null;

    WebSocket webSocket = null;

    public WebSocketThread(Context context){
        this.context = context;
    }

    public int open(String serverIP) {
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

    public void stop(){
        if(webSocket != null && webSocket.isOpen()){
            webSocket.close();
        }
    }

    public void destroy(){
        if(webSocket != null && webSocket.isOpen()){
            webSocket.close();
        }
        webSocket = null;
    }

    public void send(String message) {
        if (webSocket != null && webSocket.isOpen()) {
            webSocket.send(message);
        }
    }

    private void broadcastIntentOpened() {
        Intent intent = new Intent("mahdi.websocket");
        intent.putExtra("command", "opened");
        context.sendBroadcast(intent);
    }

    private void broadcastIntentClosed() {
        Intent intent = new Intent("mahdi.websocket");
        intent.putExtra("command", "closed");
        context.sendBroadcast(intent);
    }

    private void broadcastIntentMessage(String message) {
        Intent intent = new Intent("mahdi.websocket");
        intent.putExtra("command", "onMessage");
        intent.putExtra("message", message);
        context.sendBroadcast(intent);
    }

}
