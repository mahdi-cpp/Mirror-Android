package com.mahdi.car.messenger;


import com.mahdi.car.App;

import java.io.File;
import java.util.HashMap;

public class FileLoader
{


    public static final int MEDIA_DIR_IMAGE = 4;
    public static final int MEDIA_DIR_AUDIO = 1;
    public static final int MEDIA_DIR_VIDEO = 2;
    public static final int MEDIA_DIR_DOCUMENT = 3;
    public static final int MEDIA_DIR_CACHE = 4;

    private static volatile FileLoader Instance = null;
    private HashMap<Integer, File> mediaDirs = null;
    private volatile DispatchQueue fileLoaderQueue = new DispatchQueue("fileUploadQueue");


    private HashMap<String, Long> uploadSizes = new HashMap<>();


    private int currentLoadOperationsCount = 0;
    private int currentAudioLoadOperationsCount = 0;
    private int currentPhotoLoadOperationsCount = 0;
    private int currentUploadOperationsCount = 0;
    private int currentUploadSmallOperationsCount = 0;

    public static FileLoader getInstance()
    {
        FileLoader localInstance = Instance;
        if (localInstance == null) {
            synchronized (FileLoader.class) {
                localInstance = Instance;
                if (localInstance == null) {
                    Instance = localInstance = new FileLoader();
                }
            }
        }
        return localInstance;
    }


    public static String getExtensionByMime(String mime)
    {
        int index;
        if ((index = mime.indexOf('/')) != -1) {
            return mime.substring(index + 1);
        }
        return "";
    }

    public static File getInternalCacheDir()
    {
        return App.applicationContext.getCacheDir();
    }



    public void setMediaDirs(HashMap<Integer, File> dirs)
    {
        mediaDirs = dirs;
    }


    public File getDirectory(int type)
    {
        File dir = mediaDirs.get(type);
        if (dir == null && type != MEDIA_DIR_CACHE) {
            dir = mediaDirs.get(MEDIA_DIR_CACHE);
        }
        try {
            if (!dir.isDirectory()) {
                dir.mkdirs();
            }
        } catch (Exception e) {
            //don't promt
        }
        return dir;
    }



}
