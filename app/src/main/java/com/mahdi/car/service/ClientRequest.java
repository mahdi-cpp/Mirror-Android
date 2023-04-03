package com.mahdi.car.service;

public class ClientRequest {
    private static int item = 0;
    public static int CLIENT_REQUEST_ADB_CONNECT = item++;         // android client request to connect to adb by WiFi IP #adb connect 192.168.1.1
    public static int CLIENT_REQUEST_UPDATE = item++;              // android client is now connected to server and request get current data for sync
    public static int CLIENT_REQUEST_MIRROR = item++;              // android client request for start screen mirror
}
