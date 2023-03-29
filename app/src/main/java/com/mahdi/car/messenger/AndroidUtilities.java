package com.mahdi.car.messenger;


import android.annotation.SuppressLint;
import android.app.Activity;

import android.content.Context;

import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.ContentObserver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;

import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;

import android.graphics.drawable.Drawable;

import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.provider.CallLog;

import android.text.Html;
import android.text.Spanned;
import android.util.DisplayMetrics;
import android.util.Log;

import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;


import java.io.File;

import java.lang.reflect.Field;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import java.util.Date;

import java.util.Locale;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


import androidx.palette.graphics.Palette;

import androidx.recyclerview.widget.RecyclerView;
import com.mahdi.car.App;

import com.mahdi.car.R;

public class AndroidUtilities
{
    //---------------------------------------------------------
    public static int width = 400;

    public static int statusBarHeight = 0;
    public static int height = 0;
    public static int navigationBarHeight = 0;
    //---------------------------------------------------------

    public static Activity parentActivity;
    public static final Pattern VALID_NAME_PATTERN_REGEX = Pattern.compile("[a-zA-Z_0-9]+$");


    public static boolean isRight = false;

    public static float mDensity = 1;
    public static Point displaySize = new Point();
    public static int roundMessageSize;
    public static boolean incorrectDisplaySizeFix;
    public static Integer photoSize = null;
    public static DisplayMetrics displayMetrics = new DisplayMetrics();
    public static int leftBaseline;
    public static boolean usingHardwareInput;
    public static boolean isInMultiwindow;
    public static Pattern WEB_URL = null;

    private static Boolean isTablet = false;

    private static ContentObserver callLogContentObserver;
    private static Runnable unregisterRunnable;
    private static boolean hasCallPermissions = Build.VERSION.SDK_INT >= 23;
    private static Field mAttachInfoField;
    private static Field mStableInsetsField;

    private static DecimalFormat formatter;

    public static int currentApiVersion;

    static {
        try {
            final String GOOD_IRI_CHAR = "a-zA-Z0-9\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF";
            final Pattern IP_ADDRESS = Pattern.compile("((25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9])\\.(25[0-5]|2[0-4]" + "[0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1]" + "[0-9]{2}|[1-9][0-9]|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}" + "|[1-9][0-9]|[0-9]))");
            final String IRI = "[" + GOOD_IRI_CHAR + "]([" + GOOD_IRI_CHAR + "\\-]{0,61}[" + GOOD_IRI_CHAR + "]){0,1}";
            final String GOOD_GTLD_CHAR = "a-zA-Z\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF";
            final String GTLD = "[" + GOOD_GTLD_CHAR + "]{2,63}";
            final String HOST_NAME = "(" + IRI + "\\.)+" + GTLD;
            final Pattern DOMAIN_NAME = Pattern.compile("(" + HOST_NAME + "|" + IP_ADDRESS + ")");
            WEB_URL = Pattern.compile("((?:(http|https|Http|Https):\\/\\/(?:(?:[a-zA-Z0-9\\$\\-\\_\\.\\+\\!\\*\\'\\(\\)" + "\\,\\;\\?\\&\\=]|(?:\\%[a-fA-F0-9]{2})){1,64}(?:\\:(?:[a-zA-Z0-9\\$\\-\\_" + "\\.\\+\\!\\*\\'\\(\\)\\,\\;\\?\\&\\=]|(?:\\%[a-fA-F0-9]{2})){1,25})?\\@)?)?" + "(?:" + DOMAIN_NAME + ")" + "(?:\\:\\d{1,5})?)" // plus option port number
                    + "(\\/(?:(?:[" + GOOD_IRI_CHAR + "\\;\\/\\?\\:\\@\\&\\=\\#\\~"  // plus option query params
                    + "\\-\\.\\+\\!\\*\\'\\(\\)\\,\\_])|(?:\\%[a-fA-F0-9]{2}))*)?" + "(?:\\b|$)");
        } catch (Exception e) {
            FileLog.e(e);
        }
    }


    static {
        leftBaseline = isTablet() ? 80 : 72;
        checkDisplaySize(App.applicationContext, null);
    }

    public static boolean isEnglishWord(String str)
    {
        if (str == null) {
            return false;
        }
        return VALID_NAME_PATTERN_REGEX.matcher(str).find();
    }

    public static File getCacheDir()
    {
        String state = null;
        try {
            state = Environment.getExternalStorageState();
        } catch (Exception e) {
            FileLog.e(e);
        }
        if (state == null || state.startsWith(Environment.MEDIA_MOUNTED)) {
            try {
                File file = App.applicationContext.getExternalCacheDir();
                if (file != null) {
                    return file;
                }
            } catch (Exception e) {
                FileLog.e(e);
            }
        }
        try {
            File file = App.applicationContext.getCacheDir();
            if (file != null) {
                return file;
            }
        } catch (Exception e) {
            FileLog.e(e);
        }
        return new File("");
    }

    public static void checkDisplaySize(Context context, Configuration newConfiguration)
    {
        try {
            mDensity = context.getResources().getDisplayMetrics().density;
            Configuration configuration = newConfiguration;
            if (configuration == null) {
                configuration = context.getResources().getConfiguration();
            }
            usingHardwareInput = configuration.keyboard != Configuration.KEYBOARD_NOKEYS && configuration.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_NO;
            WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            if (manager != null) {
                Display display = manager.getDefaultDisplay();
                if (display != null) {
                    display.getMetrics(displayMetrics);
                    display.getSize(displaySize);
                }
            }
            if (configuration.screenWidthDp != Configuration.SCREEN_WIDTH_DP_UNDEFINED) {
                int newSize = (int) Math.ceil(configuration.screenWidthDp * mDensity);
                if (Math.abs(displaySize.x - newSize) > 3) {
                    displaySize.x = newSize;
                }
            }
            if (configuration.screenHeightDp != Configuration.SCREEN_HEIGHT_DP_UNDEFINED) {
                int newSize = (int) Math.ceil(configuration.screenHeightDp * mDensity);
                if (Math.abs(displaySize.y - newSize) > 3) {
                    displaySize.y = newSize;
                }
            }
            if (roundMessageSize == 0) {
                if (AndroidUtilities.isTablet()) {
                    roundMessageSize = (int) (AndroidUtilities.getMinTabletSide() * 0.6f);
                } else {
                    roundMessageSize = (int) (Math.min(AndroidUtilities.displaySize.x, AndroidUtilities.displaySize.y) * 0.6f);
                }
            }
            FileLog.e("display size = " + displaySize.x + " " + displaySize.y + " " + displayMetrics.xdpi + "x" + displayMetrics.ydpi);
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    public static float getPixelsInCM(float cm, boolean isX)
    {
        return (cm / 2.54f) * (isX ? displayMetrics.xdpi : displayMetrics.ydpi);
    }

    public static void run(Runnable runnable)
    {
        run(runnable, 0);
    }

    public static void run(Runnable runnable, long delay)
    {
        if (delay == 0) {
            App.applicationHandler.post(runnable);
        } else {
            App.applicationHandler.postDelayed(runnable, delay);
        }
    }

    public static void cancelRunOnUIThread(Runnable runnable)
    {
        App.applicationHandler.removeCallbacks(runnable);
    }

    public static boolean isTablet()
    {
//        if (isTablet == null) {
//            isTablet = App.applicationContext.getResources().getBoolean(R.bool.isTablet);
//        }
        return isTablet;
    }

    public static boolean isSmallTablet()
    {
        float minSide = Math.min(displaySize.x, displaySize.y) / mDensity;
        return minSide <= 700;
    }

    public static int getMinTabletSide()
    {
        if (!isSmallTablet()) {
            int smallSide = Math.min(displaySize.x, displaySize.y);
            int leftSide = smallSide * 35 / 100;
            if (leftSide < dp(320)) {
                leftSide = dp(320);
            }
            return smallSide - leftSide;
        } else {
            int smallSide = Math.min(displaySize.x, displaySize.y);
            int maxSide = Math.max(displaySize.x, displaySize.y);
            int leftSide = maxSide * 35 / 100;
            if (leftSide < dp(320)) {
                leftSide = dp(320);
            }
            return Math.min(smallSide, maxSide - leftSide);
        }
    }

    public static int getPhotoSize()
    {
        return 1080;
    }


    public static int getPhotoThumbnailSize()
    {
        return 180;
    }

    private static void registerLoginContentObserver(boolean shouldRegister, final String number)
    {
        if (shouldRegister) {
            if (callLogContentObserver != null) {
                return;
            }
            App.applicationContext.getContentResolver().registerContentObserver(CallLog.Calls.CONTENT_URI, true, callLogContentObserver = new ContentObserver(new Handler())
            {
                @Override
                public boolean deliverSelfNotifications()
                {
                    return true;
                }

                @Override
                public void onChange(boolean selfChange)
                {
                    registerLoginContentObserver(false, number);
                    removeLoginPhoneCall(number, false);
                }
            });
            run(unregisterRunnable = new Runnable()
            {
                @Override
                public void run()
                {
                    unregisterRunnable = null;
                    registerLoginContentObserver(false, number);
                }
            }, 10000);
        } else {
            if (callLogContentObserver == null) {
                return;
            }
            if (unregisterRunnable != null) {
                cancelRunOnUIThread(unregisterRunnable);
                unregisterRunnable = null;
            }
            try {
                App.applicationContext.getContentResolver().unregisterContentObserver(callLogContentObserver);
            } catch (Exception ignore) {

            } finally {
                callLogContentObserver = null;
            }
        }
    }

    public static void removeLoginPhoneCall(String number, boolean first)
    {
        if (!hasCallPermissions) {
            return;
        }
        Cursor cursor = null;
        try {
            cursor = App.applicationContext.getContentResolver().query(CallLog.Calls.CONTENT_URI, new String[]{CallLog.Calls._ID, CallLog.Calls.NUMBER}, CallLog.Calls.TYPE + " IN (" + CallLog.Calls.MISSED_TYPE + "," + CallLog.Calls.INCOMING_TYPE + "," + CallLog.Calls.REJECTED_TYPE + ")", null, "date DESC LIMIT 5");
            boolean removed = false;
            while (cursor.moveToNext()) {
                String phone = cursor.getString(1);
                if (phone.contains(number) || number.contains(phone)) {
                    removed = true;
                    App.applicationContext.getContentResolver().delete(CallLog.Calls.CONTENT_URI, CallLog.Calls._ID + " = ? ", new String[]{String.valueOf(cursor.getInt(0))});
                    break;
                }
            }
            if (!removed && first) {
                registerLoginContentObserver(true, number);
            }
        } catch (Exception e) {
            FileLog.e(e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    @SuppressLint("SoonBlockedPrivateApi")
    public static int getViewInset(View view)
    {
        if (view == null || Build.VERSION.SDK_INT < 21 || view.getHeight() == AndroidUtilities.displaySize.y || view.getHeight() == AndroidUtilities.displaySize.y) {
            return 0;
        }
        try {
            if (mAttachInfoField == null) {
                mAttachInfoField = View.class.getDeclaredField("mAttachInfo");
                mAttachInfoField.setAccessible(true);
            }
            Object mAttachInfo = mAttachInfoField.get(view);
            if (mAttachInfo != null) {
                if (mStableInsetsField == null) {
                    mStableInsetsField = mAttachInfo.getClass().getDeclaredField("mStableInsets");
                    mStableInsetsField.setAccessible(true);
                }
                Rect insets = (Rect) mStableInsetsField.get(mAttachInfo);
                return insets.bottom;
            }
        } catch (Exception e) {
            FileLog.e(e);
        }
        return 0;
    }

    public static void checkForCrashes(Activity context)
    {

    }

    public static void checkForUpdates(Activity context)
    {
        if (BuildVars.DEBUG_VERSION) {
            //UpdateManager.register(context, BuildVars.DEBUG_VERSION ? BuildVars.HOCKEY_APP_HASH_DEBUG : BuildVars.HOCKEY_APP_HASH);
        }
    }

    public static void unregisterUpdates()
    {
        if (BuildVars.DEBUG_VERSION) {
            //UpdateManager.unregister();
        }
    }

    private static File getAlbumDir()
    {
        if (Build.VERSION.SDK_INT >= 23 && App.applicationContext.checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            return FileLoader.getInstance().getDirectory(FileLoader.MEDIA_DIR_CACHE);
        }
        File storageDir = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Download");
            if (!storageDir.mkdirs()) {
                if (!storageDir.exists()) {
                    FileLog.d("failed to create directory");
                    return null;
                }
            }
        } else {
            FileLog.d("External storage is not mounted READ/WRITE.");
        }

        return storageDir;
    }

    public static File generatePicturePath()
    {
        try {
            File storageDir = getAlbumDir();
            Date date = new Date();
            date.setTime(System.currentTimeMillis() + Utilities.random.nextInt(1000) + 1);
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss_SSS", Locale.US).format(date);

            //return new File(storageDir, "IMG_" + timeStamp + ".jpg");
            //return new File(AndroidUtilities.getCacheDir(), "IMG_" + timeStamp + ".jpg");
            return new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString() + "/ali.jpg");

        } catch (Exception e) {
            FileLog.e(e);
        }
        return null;
    }

    public static File generateVideoPath()
    {
        try {
            //            File storageDir = getAlbumDir();
            Date date = new Date();
            date.setTime(System.currentTimeMillis() + Utilities.random.nextInt(1000) + 1);
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss_SSS", Locale.US).format(date);

            //return new File(storageDir, "VID_" + timeStamp + ".mp4");
            //return new File(AndroidUtilities.getAlbumDir(), "VID_" + timeStamp + ".mp4");

            //            return new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString() + "/record_" + timeStamp + ".mp4");
            return new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/aaa/record_" + timeStamp + ".mp4");

        } catch (Exception e) {
            FileLog.e(e);
        }
        return null;
    }

    public static int getCurrentTime()
    {
        return (int) (new Date().getTime() / 1000);
    }

    public static double roundTwoDecimals(double d)
    {
        DecimalFormat twoDForm = new DecimalFormat("#.########");
        return Double.valueOf(twoDForm.format(d));
    }

    public static int pxToDp(float px)
    {
        Resources resources = App.getContext().getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return (int) dp;
    }

    public static void init(Activity parentActivity, int width, int height)
    {
        AndroidUtilities.parentActivity = parentActivity;

        AndroidUtilities.width = width;
        AndroidUtilities.height = height - AndroidUtilities.statusBarHeight;

        formatter = new DecimalFormat("#");
        formatter.setGroupingUsed(true);
        formatter.setGroupingSize(3);
    }

    public static Bitmap createUserBitmap(Bitmap bitmap)
    {
        Bitmap result = null;

        try {

            result = Bitmap.createBitmap(dp(41.33f), dp(50.6f), Bitmap.Config.ARGB_8888);
            result.eraseColor(Color.TRANSPARENT);
            Canvas canvas = new Canvas(result);

            Drawable drawableLivePin = App.applicationContext.getResources().getDrawable(R.drawable.livepin);
            drawableLivePin.setBounds(0, 0, dp(41.33f), dp(50.6f));
            drawableLivePin.draw(canvas);

            Paint roundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            RectF bitmapRect = new RectF();
            canvas.save();


            //if (Cover != null) {

            //File path = FileLoader.getPathToAttach(Cover, true);
            //Bitmap bitmap = BitmapFactory.decodeFile(path.toString());
            //Bitmap bitmap = null;

            if (bitmap.getWidth() >= bitmap.getHeight()) {
                bitmap = Bitmap.createBitmap(bitmap, bitmap.getWidth() / 2 - bitmap.getHeight() / 2, 0, bitmap.getHeight(), bitmap.getHeight());
            } else {
                bitmap = Bitmap.createBitmap(bitmap, 0, bitmap.getHeight() / 2 - bitmap.getWidth() / 2, bitmap.getWidth(), bitmap.getWidth());
            }

            if (bitmap != null) {
                BitmapShader shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
                Matrix matrix = new Matrix();
                float scale = dp(52) / (float) bitmap.getWidth();
                matrix.postTranslate(dp(5), dp(5));
                matrix.postScale(scale, scale);
                roundPaint.setShader(shader);
                shader.setLocalMatrix(matrix);
                bitmapRect.set(dp(3), dp(3), dp(38), dp(38));
                canvas.drawRoundRect(bitmapRect, dp(26), dp(26), roundPaint);
            }

            //} else {


            //}


        } catch (Throwable e) {
            FileLog.e(e);
        }

        return result;
    }

    public static Bitmap drawableToBitmap(Drawable drawable)
    {

        Bitmap bitmap = null;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if (bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    public static Bitmap notificationBitmap(Bitmap bitmap)
    {

        Bitmap result = null;

        try {

            result = Bitmap.createBitmap(dp(70), dp(70), Bitmap.Config.ARGB_8888);
            result.eraseColor(Color.TRANSPARENT);
            Canvas canvas = new Canvas(result);
            //Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            Paint roundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            RectF bitmapRect = new RectF();
            canvas.save();

            if (bitmap.getWidth() >= bitmap.getHeight()) {
                bitmap = Bitmap.createBitmap(bitmap, bitmap.getWidth() / 2 - bitmap.getHeight() / 2, 0, bitmap.getHeight(), bitmap.getHeight());
            } else {
                bitmap = Bitmap.createBitmap(bitmap, 0, bitmap.getHeight() / 2 - bitmap.getWidth() / 2, bitmap.getWidth(), bitmap.getWidth());
            }

            if (bitmap != null) {

                BitmapShader shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
                Matrix matrix = new Matrix();
                float scale = dp(70) / (float) bitmap.getWidth();
                matrix.postTranslate(dp(0), dp(0));
                matrix.postScale(scale, scale);
                roundPaint.setShader(shader);
                shader.setLocalMatrix(matrix);
                bitmapRect.set(dp(0), dp(0), dp(70), dp(70));

                //paint.setStyle(Paint.Style.STROKE);
                //paint.setColor(0xff888888);
                //paint.setStrokeWidth(1);

                canvas.drawRoundRect(bitmapRect, dp(35), dp(35), roundPaint);
                //canvas.drawRoundRect(bitmapRect, AndroidUtilities.dp(30), AndroidUtilities.dp(30), paint);

            }

        } catch (Throwable e) {
            FileLog.e(e);
        }

        return result;
    }

    public static ArrayList<int[]> getSpans(String body, char prefix)
    {
        ArrayList<int[]> spans = new ArrayList<int[]>();

        Pattern pattern = Pattern.compile(prefix + "\\w+");
        Matcher matcher = pattern.matcher(body);


        // Check all occurrences
        while (matcher.find()) {
            int[] currentSpan = new int[2];
            currentSpan[0] = matcher.start();
            currentSpan[1] = matcher.end();
            spans.add(currentSpan);
        }

        return spans;
    }

    public static String time(Date date)
    {

        if (date == null) {
            return "?";
        }

        Long createdDate = date.getTime();
        Long timeNow = new Date().getTime();

        Long timeElapsed = timeNow - createdDate;

        // For logging in Android for testing purposes
        /*
        Date dateCreatedFriendly = new Date(createdDate);
        Log.d("MicroR", "dateCreatedFriendly: " + dateCreatedFriendly.toString());
        Log.d("MicroR", "timeNow: " + timeNow.toString());
        Log.d("MicroR", "timeElapsed: " + timeElapsed.toString());*/

        // Lengths of respective time durations in Long format.
        Long oneMin = 60000L;
        Long oneHour = 3600000L;
        Long oneDay = 86400000L;
        Long oneWeek = 604800000L;
        Long oneYear = 31622400000L;

        String finalString = "0sec";
        String unit;

        if (timeElapsed > oneYear) {
            double year = 1;
            unit = " year ";
            finalString = String.format("%.0f", year) + unit;

        } else if (timeElapsed < oneMin) {

            // Convert milliseconds to seconds.
            double seconds = (double) ((timeElapsed / 1000));
            // Round up
            seconds = Math.round(seconds);
            // Generate the friendly unit of the ago time
            unit = " second ";
            finalString = String.format("%.0f", seconds) + unit;

        } else if (timeElapsed < oneHour) {

            double minutes = (double) ((timeElapsed / 1000) / 60);
            minutes = Math.round(minutes);
            unit = " minute ";
            finalString = String.format("%.0f", minutes) + unit;

        } else if (timeElapsed < oneDay) {

            double hours = (double) ((timeElapsed / 1000) / 60 / 60);
            hours = Math.round(hours);
            unit = " hor ";
            finalString = String.format("%.0f", hours) + unit;

        } else if (timeElapsed < oneWeek) {

            double days = (double) ((timeElapsed / 1000) / 60 / 60 / 24);
            days = Math.round(days);
            unit = " day ";
            finalString = String.format("%.0f", days) + unit;

        } else if (timeElapsed > oneWeek) {

            double weeks = (double) ((timeElapsed / 1000) / 60 / 60 / 24 / 7);
            weeks = Math.round(weeks);
            unit = " week ";
            finalString = String.format("%.0f", weeks) + unit;

        }

        return finalString;
    }

    public static String timeNotification(Date date)
    {

        if (date == null) {
            return "?";
        }

        Long createdDate = date.getTime();
        Long timeNow = new Date().getTime();

        Long timeElapsed = timeNow - createdDate;


        Long oneMin = 60000L;
        Long oneHour = 3600000L;
        Long oneDay = 86400000L;
        Long oneWeek = 604800000L;
        Long oneYear = 31622400000L;

        String finalString = "0s";
        String unit;

        if (timeElapsed > oneYear) {
            double year = 1;
            unit = "y";
            finalString = String.format("%.0f", year) + unit;

        } else if (timeElapsed < oneMin) {

            // Convert milliseconds to seconds.
            double seconds = (double) ((timeElapsed / 1000));
            // Round up
            seconds = Math.round(seconds);
            // Generate the friendly unit of the ago time
            unit = "s";
            finalString = String.format("%.0f", seconds) + unit;

        } else if (timeElapsed < oneHour) {

            double minutes = (double) ((timeElapsed / 1000) / 60);
            minutes = Math.round(minutes);
            unit = "m";
            finalString = String.format("%.0f", minutes) + unit;

        } else if (timeElapsed < oneDay) {

            double hours = (double) ((timeElapsed / 1000) / 60 / 60);
            hours = Math.round(hours);
            unit = "h";
            finalString = String.format("%.0f", hours) + unit;

        } else if (timeElapsed < oneWeek) {

            double days = (double) ((timeElapsed / 1000) / 60 / 60 / 24);
            days = Math.round(days);
            unit = "d";
            finalString = String.format("%.0f", days) + unit;

        } else if (timeElapsed > oneWeek) {

            double weeks = (double) ((timeElapsed / 1000) / 60 / 60 / 24 / 7);
            weeks = Math.round(weeks);
            unit = "w";
            finalString = String.format("%.0f", weeks) + unit;

        }

        return finalString;
    }

    public static String getDay(Date date)
    {
        if (date == null) {
            return "?";
        }

        Long oneMin = 60000L;
        Long oneHour = 3600000L;
        Long oneDay = 86400000L;

        Long createdDate = date.getTime();
        Long timeNow = new Date().getTime();

        Long timeElapsed = createdDate - timeNow;

        timeElapsed = timeElapsed / 1000;

        String finalString = "@";

        double days = (double) (timeElapsed / 60 / 60 / 24);
        days = Math.round(days);


        double hours = timeElapsed - (days * oneDay);
        double minutes = timeElapsed - (days * oneDay) - (hours * oneHour);
        double second = timeElapsed - (days * oneDay) - (hours * oneHour) - (minutes * oneMin);


        Log.e("houre", "" + hours);


        //finalString = String.format("%.0f", ali);

        return finalString;
    }

    //    public static String getDay(int type, Date date)
    //    {
    //        if (date == null) {
    //            return "?";
    //        }
    //
    //        Long createdDate = date.getTime();
    //        Long timeNow = new Date().getTime();
    //
    //        Long timeElapsed = timeNow - createdDate;
    //
    //        String finalString = "@";
    //        String unit = "";
    //
    //        if (type == 4) {
    //            // Convert milliseconds to seconds.
    //            double seconds = (double) ((timeElapsed / 1000));
    //            // Round up
    //            seconds = Math.round(seconds);
    //            // Generate the friendly unit of the ago time
    //            //unit = " ثانیه ";
    //            finalString = String.format("%.0f", seconds) + unit;
    //
    //        } else if (type == 3) {
    //
    //            double minutes = (double) ((timeElapsed / 1000) / 60);
    //            minutes = Math.round(minutes);
    //            //unit = " دقیقه ";
    //            finalString = String.format("%.0f", minutes) + unit;
    //
    //        } else if (type == 2) {
    //
    //            double hours = (double) ((timeElapsed / 1000) / 60 / 60);
    //            hours = Math.round(hours);
    //            //unit = " ساعت ";
    //            finalString = String.format("%.0f", hours) + unit;
    //
    //        } else if (type == 1) {
    //
    //            double days = (double) ((timeElapsed / 1000) / 60 / 60 / 24);
    //            days = Math.round(days);
    //            //unit = " روز ";
    //            finalString = "" + (int)days * -1;
    //        }
    //
    //        return finalString;
    //    }

    public static boolean isOdd(int val)
    {
        return (val & 0x01) != 0;
    }

    //    public static LatLngBounds getPolygonLatLngBounds(final List<LatLng> polygon)
    //    {
    //
    //        final LatLngBounds.Builder centerBuilder = LatLngBounds.builder();
    //        for (LatLng point : polygon) {
    //            centerBuilder.include(point);
    //        }
    //        return centerBuilder.build();
    //    }

    public static int[] splitToComponentTimes(long seconds)
    {
        int hours = (int) seconds / 3600;
        int remainder = (int) seconds - hours * 3600;
        int minute = remainder / 60;
        remainder = remainder - minute * 60;
        int secs = remainder;

        int[] ints = {hours, minute, secs};
        return ints;
    }

    //-------------------------------------------------------------------------------

    public static Bitmap getCroppedBitmap(Bitmap bitmap)
    {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        // canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2, bitmap.getWidth() / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        //Bitmap _bmp = Bitmap.createScaledBitmap(output, 60, 60, false);
        //return _bmp;
        return output;
    }

    // Generate palette synchronously and return it
    public static Palette createPaletteSync(Bitmap bitmap)
    {
        Palette p = Palette.from(bitmap).generate();
        return p;
    }


    public static Bitmap roundBitmap(Bitmap bitmap, int w, int h)
    {
        Bitmap result = null;

        try {

            result = Bitmap.createBitmap(dp(46), dp(86), Bitmap.Config.ARGB_8888);
            result.eraseColor(Color.TRANSPARENT);
            Canvas canvas = new Canvas(result);

            //            Drawable drawableLivePin = App.applicationContext.getResources().getDrawable(R.drawable.livepin);
            //            drawableLivePin.setBounds(0, 0, dp(41.33f), dp(50.6f));
            //            drawableLivePin.draw(canvas);

            Paint roundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            RectF bitmapRect = new RectF();
            canvas.save();

            //            if (bitmap.getWidth() >= bitmap.getHeight()) {
            //                bitmap = Bitmap.createBitmap(bitmap, bitmap.getWidth() / 2 - bitmap.getHeight() / 2, 0, bitmap.getHeight(), bitmap.getHeight());
            //            } else {
            //                bitmap = Bitmap.createBitmap(bitmap, 0, bitmap.getHeight() / 2 - bitmap.getWidth() / 2, bitmap.getWidth(), bitmap.getWidth());
            //            }

            if (bitmap != null) {
                BitmapShader shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
                Matrix matrix = new Matrix();
                float scale = dp(52) / (float) bitmap.getWidth();
                matrix.postTranslate(dp(5), dp(5));
                matrix.postScale(scale, scale);
                roundPaint.setShader(shader);
                shader.setLocalMatrix(matrix);
                bitmapRect.set(0, 0, w, h);
                canvas.drawRoundRect(bitmapRect, dp(8), dp(8), roundPaint);
            }

        } catch (Throwable e) {
            FileLog.e(e);
        }

        return result;
    }

    public static Spanned fromHtml(String html, int flags)
    {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            return Html.fromHtml(html, flags);
        } else {
            return Html.fromHtml(html);
        }
    }

    //-------------------------------------------------------------------------------

    public static int dp(float value)
    {
        if (value == 0) {
            return 0;
        }
        return (int) Math.ceil(mDensity * value);
    }

    public static void modeIGTV()
    {
        if (Build.VERSION.SDK_INT >= 23) {
            Window window = parentActivity.getWindow();
            View decorView = window.getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    // Hide the nav bar and status bar
                    | View.SYSTEM_UI_FLAG_FULLSCREEN);

            parentActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            parentActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    public static void showStatusBar()
    {
        if (Build.VERSION.SDK_INT >= 23) {
            Window window = parentActivity.getWindow();
            View decorView = window.getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    // Hide the nav bar and status bar
                    | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }

    public static void hideStatusBar()
    {
        if (Build.VERSION.SDK_INT >= 21) {

            View decorView = parentActivity.getWindow().getDecorView();

            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);


            //parentActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //            parentActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //            parentActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

            // Code below is to handle presses of Volume up or Volume down.
            // Without this, after pressing volume buttons, the navigation bar will
            // show up and won't hide


            //            decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener()
            //            {
            //                @Override
            //                public void onSystemUiVisibilityChange(int visibility)
            //                {
            //                    if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
            //                        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE
            //                                // Set the content to appear under the system bars so that the
            //                                // content doesn't resize when the system bars hide and show.
            //                                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            //                                // Hide the nav bar and status bar
            //                                | View.SYSTEM_UI_FLAG_FULLSCREEN);
            //                    }
            //                }
            //            });
        }
    }

    public static void setFlagsFullScreen()
    {
        parentActivity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    public static void clearFlagsFullScreen()
    {
        parentActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        parentActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    public static void showNavigation()
    {
        if (Build.VERSION.SDK_INT >= 23) {

            Window window = parentActivity.getWindow();

            View decorView = window.getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_FULLSCREEN);
        }
    }

    public static void hideNavigation()
    {
        // This work only for android 5.0+
        if (Build.VERSION.SDK_INT >= 21) {

            View decorView = parentActivity.getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE
                    // Set the content to appear under the system bars so that the
                    // content doesn't resize when the system bars hide and show.
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    // Hide the nav bar and status bar
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN);

            // clear FLAG_TRANSLUCENT_STATUS flag:
            //parentActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    public static void setNavigationTransparent(boolean transparent)
    {
        if (Build.VERSION.SDK_INT < 23)
            return;

        if (transparent)
            parentActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        else
            parentActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
    }

    public static class Holder extends RecyclerView.ViewHolder
    {
        public Holder(View itemView)
        {
            super(itemView);
        }
    }

    public static void keyboardShow(View view)
    {
        if (view == null) {
            return;
        }
        try {
            InputMethodManager inputManager = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    public static void keyboardHide(View view)
    {
        if (view == null) {
            return;
        }
        try {
            InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (!imm.isActive()) {
                return;
            }
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

}
