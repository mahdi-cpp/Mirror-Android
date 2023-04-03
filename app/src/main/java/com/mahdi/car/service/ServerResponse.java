package com.mahdi.car.service;

public class ServerResponse {
    private static int item = 0;
    public static int MIRROR_SUCCESS_START = item++;        // Start successfully
    public static int MIRROR_ERROR_START = item++;          // Startup failed
    public static int MIRROR_FINISHED = item++;             // Stop successfully
    public static int UPDATE_MIRROR_DATA = item++;                   // update user interface
}
