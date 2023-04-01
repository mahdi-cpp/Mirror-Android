package com.mahdi.car.Thread;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UdpThread {

    private Context context = null;

    DatagramSocket socket;
    Thread thread;
    private Boolean shouldRestartSocketListen = true;

    public UdpThread(Context context){
        this.context = context;
    }

    public void stop() {
        shouldRestartSocketListen = false;
        if (socket != null) {
            socket.close();
        }
    }

    public void start() {

        shouldRestartSocketListen = true;
        thread = new Thread(new Runnable() {
            public void run() {
                try {
                    InetAddress broadcastIP = InetAddress.getByName("0.0.0.0"); //172.16.238.42 //192.168.1.255
                    Integer port = 45454;
                    while (shouldRestartSocketListen) {
                        listenAndWaitAndThrowIntent(broadcastIP, port);
                    }
                    //if (!shouldListenForUDPBroadcast) throw new ThreadDeath();
                } catch (Exception e) {
                    Log.i("UDP", "no longer listening for UDP broadcasts cause of error " + e.getMessage());
                }
            }
        });
        thread.start();
    }

    private void listenAndWaitAndThrowIntent(InetAddress broadcastIP, Integer port) throws Exception {

        byte[] recvBuf = new byte[15000];
        if (socket == null || socket.isClosed()) {
            socket = new DatagramSocket(port, broadcastIP);
            socket.setBroadcast(true);
        }
        //socket.setSoTimeout(1000);
        DatagramPacket packet = new DatagramPacket(recvBuf, recvBuf.length);
        //Log.d("UDP", "Waiting for UDP broadcast");
        socket.receive(packet);

        String senderIP = packet.getAddress().getHostAddress();
        String message = new String(packet.getData()).trim();

        Log.d("UDP", senderIP + "  message: " + message);

        sendUdpBroadcast(senderIP, message);
        socket.close();
    }

    private void sendUdpBroadcast(String senderIP, String message) {
        Intent intent = new Intent("udp.mahdi");
        intent.putExtra("senderIP", senderIP);
        intent.putExtra("message", message);
        context.sendBroadcast(intent);
    }
}
