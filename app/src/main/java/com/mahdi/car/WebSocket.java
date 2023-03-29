package com.mahdi.car;

import com.mahdi.car.messenger.UdpReceiver;

import java.net.URI;
import java.util.Map;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.handshake.ServerHandshake;

/**
 * This example demonstrates how to create a websocket connection to a server. Only the most
 * important callbacks are overloaded.
 */

public class WebSocket extends WebSocketClient {

    public interface Delegate {
        void onOpen();
        void onClose();
        void onMessage(String message);
    }

    private Delegate delegate = null;

    public void setDelegate(Delegate delegate) {
        this.delegate = delegate;
    }

    public WebSocket(URI serverUri, Draft draft) {
        super(serverUri, draft);
    }

    public WebSocket(URI serverURI) {
        super(serverURI);
    }

    public WebSocket(URI serverUri, Map<String, String> httpHeaders) {
        super(serverUri, httpHeaders);
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        send("Galaxy A50 Connected");
        System.out.println("opened connection");
        if(delegate != null)
            delegate.onOpen();
        // if you plan to refuse connection based on ip or httpfields overload: onWebsocketHandshakeReceivedAsClient
    }

    @Override
    public void onMessage(String message) {
        System.out.println("received: " + message);
        if(delegate != null)
            delegate.onMessage(message);
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        // The close codes are documented in class org.java_websocket.framing.CloseFrame
        System.out.println("Connection closed by " + (remote ? "remote peer" : "us") + " Code: " + code + " Reason: " + reason);
        if(delegate != null)
            delegate.onClose();
    }

    @Override
    public void onError(Exception ex) {
        ex.printStackTrace();
        // if the error is fatal then onClose will be called additionally
    }

}