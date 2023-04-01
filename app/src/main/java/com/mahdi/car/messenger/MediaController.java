package com.mahdi.car.messenger;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;


import com.mahdi.car.App;
import com.mahdi.car.R;


public class MediaController
{
    //private static Delegate delegate;

    public static AlbumEntry allMediaAlbumEntry;
    public static AlbumEntry allPhotosAlbumEntry;
    private static Runnable refreshGalleryRunnable;
    private static Runnable broadcastPhotosRunnable;

    private boolean saveToGallery = true;

    private static volatile MediaController Instance = null;

    private static final String[] projectionPhotos = {MediaStore.Images.Media._ID, MediaStore.Images.Media.BUCKET_ID, MediaStore.Images.Media.BUCKET_DISPLAY_NAME, MediaStore.Images.Media.DATA, MediaStore.Images.Media.DATE_TAKEN, MediaStore.Images.Media.ORIENTATION};
    private static final String[] projectionVideo = {MediaStore.Video.Media._ID, MediaStore.Video.Media.BUCKET_ID, MediaStore.Video.Media.BUCKET_DISPLAY_NAME, MediaStore.Video.Media.DATA, MediaStore.Video.Media.DATE_TAKEN, MediaStore.Video.Media.DURATION};

    public static class AlbumEntry
    {
        public int bucketId;
        public String bucketName;
        public PhotoEntry coverPhoto;
        public ArrayList<PhotoEntry> photos = new ArrayList<>();
        public HashMap<Integer, PhotoEntry> photosByIds = new HashMap<>();

        public AlbumEntry(int bucketId, String bucketName, PhotoEntry coverPhoto)
        {
            this.bucketId = bucketId;
            this.bucketName = bucketName;
            this.coverPhoto = coverPhoto;
        }

        public void addPhoto(PhotoEntry photoEntry)
        {
            photos.add(photoEntry);
            photosByIds.put(photoEntry.imageId, photoEntry);
        }
    }

//    public static class SavedFilterState
//    {
//        public float enhanceValue;
//        public float exposureValue;
//        public float contrastValue;
//        public float warmthValue;
//        public float saturationValue;
//        public float fadeValue;
//        public int tintShadowsColor;
//        public int tintHighlightsColor;
//        public float highlightsValue;
//        public float shadowsValue;
//        public float vignetteValue;
//        public float grainValue;
//        public int blurType;
//        public float sharpenValue;
//        public PhotoFilterView.CurvesToolValue curvesToolValue = new PhotoFilterView.CurvesToolValue();
//        public float blurExcludeSize;
//        public Point blurExcludePoint;
//        public float blurExcludeBlurSize;
//        public float blurAngle;
//    }

    public static class PhotoEntry
    {
        public int bucketId;
        public int imageId;
        public long dateTaken;
        public int duration;
        public String path;
        public int orientation;
        public String thumbPath;
        public String imagePath;
        public boolean isVideo;
        public CharSequence caption;
        public boolean isFiltered;
        public boolean isPainted;
        public boolean isCropped;
        public boolean isMuted;
        public int ttl;
        //public SavedFilterState savedFilterState;

        public PhotoEntry(int bucketId, int imageId, long dateTaken, String path, int orientation, boolean isVideo)
        {
            this.bucketId = bucketId;
            this.imageId = imageId;
            this.dateTaken = dateTaken;
            this.path = path;
            if (isVideo) {
                this.duration = orientation;
            } else {
                this.orientation = orientation;
            }
            this.isVideo = isVideo;
        }

        public void reset()
        {
            isFiltered = false;
            isPainted = false;
            isCropped = false;
            ttl = 0;
            imagePath = null;
            thumbPath = null;
            caption = null;
            //savedFilterState = null;
        }
    }


    public static void checkGallery()
    {

        if (Build.VERSION.SDK_INT < 24 || allPhotosAlbumEntry == null) {
            return;
        }


        final int prevSize = allPhotosAlbumEntry.photos.size();
        Utilities.globalQueue.postRunnable(new Runnable()
        {
            @Override
            public void run()
            {
                int count = 0;
                Cursor cursor = null;
                try {
                    if (App.applicationContext.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        cursor = MediaStore.Images.Media.query(App.applicationContext.getContentResolver(), MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[]{"COUNT(_id)"}, null, null, null);
                        if (cursor != null) {
                            if (cursor.moveToNext()) {
                                count += cursor.getInt(0);
                            }
                        }
                    }
                } catch (Throwable e) {
                    FileLog.e(e);
                } finally {
                    if (cursor != null) {
                        cursor.close();
                    }
                }
                try {
                    if (App.applicationContext.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        cursor = MediaStore.Images.Media.query(App.applicationContext.getContentResolver(), MediaStore.Video.Media.EXTERNAL_CONTENT_URI, new String[]{"COUNT(_id)"}, null, null, null);
                        if (cursor != null) {
                            if (cursor.moveToNext()) {
                                count += cursor.getInt(0);
                            }
                        }
                    }
                } catch (Throwable e) {
                    FileLog.e(e);
                } finally {
                    if (cursor != null) {
                        cursor.close();
                    }
                }
                if (prevSize != count) {
                    if (refreshGalleryRunnable != null) {
                        AndroidUtilities.cancelRunOnUIThread(refreshGalleryRunnable);
                        refreshGalleryRunnable = null;
                    }
                    loadGalleryPhotosAlbums(0);
                }
            }
        }, 2000);
    }

    public static MediaController getInstance()
    {
        MediaController localInstance = Instance;
        if (localInstance == null) {
            synchronized (MediaController.class) {
                localInstance = Instance;
                if (localInstance == null) {
                    Instance = localInstance = new MediaController();
                }
            }
        }
        return localInstance;
    }


    public static void loadGalleryPhotosAlbums(final int guid)
    {
        Thread thread = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                final ArrayList<AlbumEntry> mediaAlbumsSorted = new ArrayList<>();
                final ArrayList<AlbumEntry> photoAlbumsSorted = new ArrayList<>();
                HashMap<Integer, AlbumEntry> mediaAlbums = new HashMap<>();
                HashMap<Integer, AlbumEntry> photoAlbums = new HashMap<>();
                AlbumEntry allPhotosAlbum = null;
                AlbumEntry allMediaAlbum = null;
                String cameraFolder = null;
                try {
                    cameraFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath() + "/" + "Camera/";
                } catch (Exception e) {
                    FileLog.e(e);
                }
                Integer mediaCameraAlbumId = null;
                Integer photoCameraAlbumId = null;


                Cursor cursor = null;

                if (guid != 2) {

                    try {
                        if (Build.VERSION.SDK_INT < 23 || Build.VERSION.SDK_INT >= 23 && App.applicationContext.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

                            cursor = MediaStore.Images.Media.query(App.applicationContext.getContentResolver(), MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projectionPhotos, null, null, MediaStore.Images.Media.DATE_TAKEN + " DESC");
                            if (cursor != null) {

                                int imageIdColumn = cursor.getColumnIndex(MediaStore.Images.Media._ID);
                                int bucketIdColumn = cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_ID);
                                int bucketNameColumn = cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
                                int dataColumn = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
                                int dateColumn = cursor.getColumnIndex(MediaStore.Images.Media.DATE_TAKEN);
                                int orientationColumn = cursor.getColumnIndex(MediaStore.Images.Media.ORIENTATION);

                                while (cursor.moveToNext()) {

                                    int imageId = cursor.getInt(imageIdColumn);
                                    int bucketId = cursor.getInt(bucketIdColumn);
                                    String bucketName = cursor.getString(bucketNameColumn);
                                    String path = cursor.getString(dataColumn);
                                    long dateTaken = cursor.getLong(dateColumn);
                                    int orientation = cursor.getInt(orientationColumn);

                                    if (path == null || path.length() == 0) {
                                        continue;
                                    }

                                    PhotoEntry photoEntry = new PhotoEntry(bucketId, imageId, dateTaken, path, orientation, false);

                                    if (allPhotosAlbum == null) {
                                        allPhotosAlbum = new AlbumEntry(0, LocaleController.getString("AllPhotos", R.string.AllPhotos), photoEntry);
                                        photoAlbumsSorted.add(0, allPhotosAlbum);
                                    }
                                    if (allMediaAlbum == null) {
                                        allMediaAlbum = new AlbumEntry(0, LocaleController.getString("AllMedia", R.string.AllMedia), photoEntry);
                                        mediaAlbumsSorted.add(0, allMediaAlbum);
                                    }
                                    allPhotosAlbum.addPhoto(photoEntry);
                                    allMediaAlbum.addPhoto(photoEntry);

                                    AlbumEntry albumEntry = mediaAlbums.get(bucketId);
                                    if (albumEntry == null) {
                                        albumEntry = new AlbumEntry(bucketId, bucketName, photoEntry);
                                        mediaAlbums.put(bucketId, albumEntry);
                                        if (mediaCameraAlbumId == null && cameraFolder != null && path != null && path.startsWith(cameraFolder)) {
                                            mediaAlbumsSorted.add(0, albumEntry);
                                            mediaCameraAlbumId = bucketId;
                                        } else {
                                            mediaAlbumsSorted.add(albumEntry);
                                        }
                                    }
                                    albumEntry.addPhoto(photoEntry);

                                    albumEntry = photoAlbums.get(bucketId);
                                    if (albumEntry == null) {
                                        albumEntry = new AlbumEntry(bucketId, bucketName, photoEntry);
                                        photoAlbums.put(bucketId, albumEntry);
                                        if (photoCameraAlbumId == null && cameraFolder != null && path != null && path.startsWith(cameraFolder)) {
                                            photoAlbumsSorted.add(0, albumEntry);
                                            photoCameraAlbumId = bucketId;
                                        } else {
                                            photoAlbumsSorted.add(albumEntry);
                                        }
                                    }
                                    albumEntry.addPhoto(photoEntry);
                                }
                            }
                        }
                    } catch (Throwable e) {
                        FileLog.e(e);
                    } finally {
                        if (cursor != null) {
                            try {
                                cursor.close();
                            } catch (Exception e) {
                                FileLog.e(e);
                            }
                        }
                    }
                }

                try {
                    if (Build.VERSION.SDK_INT < 23 || Build.VERSION.SDK_INT >= 23 && App.applicationContext.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        cursor = MediaStore.Images.Media.query(App.applicationContext.getContentResolver(), MediaStore.Video.Media.EXTERNAL_CONTENT_URI, projectionVideo, null, null, MediaStore.Video.Media.DATE_TAKEN + " DESC");

                        if (cursor != null) {
                            int imageIdColumn = cursor.getColumnIndex(MediaStore.Video.Media._ID);
                            int bucketIdColumn = cursor.getColumnIndex(MediaStore.Video.Media.BUCKET_ID);
                            int bucketNameColumn = cursor.getColumnIndex(MediaStore.Video.Media.BUCKET_DISPLAY_NAME);
                            int dataColumn = cursor.getColumnIndex(MediaStore.Video.Media.DATA);
                            int dateColumn = cursor.getColumnIndex(MediaStore.Video.Media.DATE_TAKEN);
                            int durationColumn = cursor.getColumnIndex(MediaStore.Video.Media.DURATION);

                            while (cursor.moveToNext()) {

                                int imageId = cursor.getInt(imageIdColumn);
                                int bucketId = cursor.getInt(bucketIdColumn);
                                String bucketName = cursor.getString(bucketNameColumn);
                                String path = cursor.getString(dataColumn);
                                long dateTaken = cursor.getLong(dateColumn);
                                long duration = cursor.getLong(durationColumn);


                                if (path == null || path.length() == 0) {
                                    continue;
                                }

                                PhotoEntry photoEntry = new PhotoEntry(bucketId, imageId, dateTaken, path, (int) (duration / 1000), true);

                                if (allMediaAlbum == null) {
                                    allMediaAlbum = new AlbumEntry(0, LocaleController.getString("AllMedia", R.string.AllMedia), photoEntry);
                                    mediaAlbumsSorted.add(0, allMediaAlbum);
                                }
                                allMediaAlbum.addPhoto(photoEntry);

                                AlbumEntry albumEntry = mediaAlbums.get(bucketId);
                                if (albumEntry == null) {
                                    albumEntry = new AlbumEntry(bucketId, bucketName, photoEntry);
                                    mediaAlbums.put(bucketId, albumEntry);
                                    if (mediaCameraAlbumId == null && cameraFolder != null && path != null && path.startsWith(cameraFolder)) {
                                        mediaAlbumsSorted.add(0, albumEntry);
                                        mediaCameraAlbumId = bucketId;
                                    } else {
                                        mediaAlbumsSorted.add(albumEntry);
                                    }
                                }

                                albumEntry.addPhoto(photoEntry);
                            }
                        }
                    }
                } catch (Throwable e) {
                    FileLog.e(e);
                } finally {
                    if (cursor != null) {
                        try {
                            cursor.close();
                        } catch (Exception e) {
                            FileLog.e(e);
                        }
                    }
                }


                for (int a = 0; a < mediaAlbumsSorted.size(); a++) {
                    Collections.sort(mediaAlbumsSorted.get(a).photos, new Comparator<PhotoEntry>()
                    {
                        @Override
                        public int compare(PhotoEntry o1, PhotoEntry o2)
                        {
                            if (o1.dateTaken < o2.dateTaken) {
                                return 1;
                            } else if (o1.dateTaken > o2.dateTaken) {
                                return -1;
                            }
                            return 0;
                        }
                    });
                }
                broadcastNewPhotos(guid, mediaAlbumsSorted, photoAlbumsSorted, mediaCameraAlbumId, allMediaAlbum, allPhotosAlbum, 0);
            }
        });
        thread.setPriority(Thread.MIN_PRIORITY);
        thread.start();
    }

    private static void broadcastNewPhotos(final int guid, final ArrayList<AlbumEntry> mediaAlbumsSorted, final ArrayList<AlbumEntry> photoAlbumsSorted, final Integer cameraAlbumIdFinal, final AlbumEntry allMediaAlbumFinal, final AlbumEntry allPhotosAlbumFinal, int delay)
    {
        if (broadcastPhotosRunnable != null) {
            AndroidUtilities.cancelRunOnUIThread(broadcastPhotosRunnable);
        }

        AndroidUtilities.run(broadcastPhotosRunnable = new Runnable()
        {
            @Override
            public void run()
            {
                /*
                if (PhotoViewer.getInstance().isVisible()) {
                    broadcastNewPhotos(guid, mediaAlbumsSorted, photoAlbumsSorted, cameraAlbumIdFinal, allMediaAlbumFinal, allPhotosAlbumFinal, 1000);
                    return;
                }
                */
                broadcastPhotosRunnable = null;
                allPhotosAlbumEntry = allPhotosAlbumFinal;
                allMediaAlbumEntry = allMediaAlbumFinal;
                //NotificationCenter.getInstance().postNotificationName(NotificationCenter.albumsDidLoaded, guid, mediaAlbumsSorted, photoAlbumsSorted, cameraAlbumIdFinal);

//                if (delegate != null) {
//                    //delegate.albumsDidLoaded();
//                }
            }
        }, delay);
    }


    public void checkSaveToGalleryFiles()
    {
        try {

            File telegramPath = new File(Environment.getExternalStorageDirectory(), "Estate");
            File imagePath = new File(telegramPath, "Estate Images");
            imagePath.mkdir();
            File videoPath = new File(telegramPath, "Estate Video");
            videoPath.mkdir();

            if (saveToGallery) {
                if (imagePath.isDirectory()) {
                    new File(imagePath, ".nomedia").delete();
                }
                if (videoPath.isDirectory()) {
                    new File(videoPath, ".nomedia").delete();
                }
            } else {
                if (imagePath.isDirectory()) {
                    new File(imagePath, ".nomedia").createNewFile();
                }
                if (videoPath.isDirectory()) {
                    new File(videoPath, ".nomedia").createNewFile();
                }
            }

        } catch (Exception e) {
            FileLog.e(e);
        }
    }

//    public interface Delegate
//    {
//        void albumsDidLoaded();
//    }
//
//    public void setDelegate(Delegate delegate)
//    {
//        this.delegate = delegate;
//    }

}
