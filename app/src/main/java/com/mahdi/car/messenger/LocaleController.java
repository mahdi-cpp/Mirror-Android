package com.mahdi.car.messenger;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.text.TextUtils;
import com.mahdi.car.App;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class LocaleController
{


    public static boolean isRTL = false;
    public static boolean RTL = true;

    public static int nameDisplayOrder = 1;
    private static volatile LocaleController Instance = null;

    public ArrayList<LocaleInfo> remoteLanguages = new ArrayList<>();

    private HashMap<String, PluralRules> allRules = new HashMap<>();
    private Locale currentLocale;

    private PluralRules currentPluralRules;
    private LocaleInfo currentLocaleInfo;
    private HashMap<String, String> localeValues = new HashMap<>();
    private String languageOverride;
    private boolean changingConfiguration = false;

    private ArrayList<LocaleInfo> otherLanguages = new ArrayList<>();

    public LocaleController()
    {

        loadOtherLanguages();
        if (remoteLanguages.isEmpty()) {
            AndroidUtilities.run(new Runnable()
            {
                @Override public void run()
                {
                    //loadRemoteLanguages();
                }
            });
        }


        LocaleInfo currentInfo = null;
        boolean override = false;



        try {
            IntentFilter timezoneFilter = new IntentFilter(Intent.ACTION_TIMEZONE_CHANGED);
            App.applicationContext.registerReceiver(new TimeZoneChangedReceiver(), timezoneFilter);
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    public static LocaleController getInstance()
    {
        LocaleController localInstance = Instance;
        if (localInstance == null) {
            synchronized (LocaleController.class) {
                localInstance = Instance;
                if (localInstance == null) {
                    Instance = localInstance = new LocaleController();
                }
            }
        }
        return localInstance;
    }

    public static String getString(String key, int res)
    {
        return getInstance().getStringInternal(key, res);
    }


    private void loadOtherLanguages()
    {
        SharedPreferences preferences = App.applicationContext.getSharedPreferences("langconfig", Activity.MODE_PRIVATE);
        String locales = preferences.getString("locales", null);
        if (!TextUtils.isEmpty(locales)) {
            String[] localesArr = locales.split("&");
            for (String locale : localesArr) {
                LocaleInfo localeInfo = LocaleInfo.createWithString(locale);
                if (localeInfo != null) {
                    otherLanguages.add(localeInfo);
                }
            }
        }
        locales = preferences.getString("remote", null);
        if (!TextUtils.isEmpty(locales)) {
            String[] localesArr = locales.split("&");
            for (String locale : localesArr) {
                LocaleInfo localeInfo = LocaleInfo.createWithString(locale);
                localeInfo.shortName = localeInfo.shortName.replace("-", "_");
                if (localeInfo != null) {
                    remoteLanguages.add(localeInfo);
                }
            }
        }
    }


    private String getStringInternal(String key, int res)
    {
        String value = localeValues.get(key);
        if (value == null) {
            try {
                value = App.applicationContext.getString(res);
            } catch (Exception e) {
                FileLog.e(e);
            }
        }
        if (value == null) {
            value = "LOC_ERR:" + key;
        }
        return value;
    }

    public void onDeviceConfigurationChange(Configuration newConfig)
    {
        if (changingConfiguration) {
            return;
        }

        if (languageOverride != null) {
            LocaleInfo toSet = currentLocaleInfo;
            currentLocaleInfo = null;

        } else {
            Locale newLocale = newConfig.locale;
            if (newLocale != null) {
                String d1 = newLocale.getDisplayName();
                String d2 = currentLocale.getDisplayName();
                if (d1 != null && d2 != null && !d1.equals(d2)) {
                    recreateFormatters();
                }
                currentLocale = newLocale;
                currentPluralRules = allRules.get(currentLocale.getLanguage());
                if (currentPluralRules == null) {
                    currentPluralRules = allRules.get("en");
                }
            }
        }
    }


    public void recreateFormatters()
    {
        Locale locale = currentLocale;
        if (locale == null) {
            locale = Locale.getDefault();
        }
        String lang = locale.getLanguage();
        if (lang == null) {
            lang = "en";
        }
        lang = lang.toLowerCase();
        isRTL = lang.startsWith("ar") || BuildVars.DEBUG_VERSION && (lang.startsWith("he") || lang.startsWith("iw") || lang.startsWith("fa"));
        isRTL = false;
        nameDisplayOrder = lang.equals("ko") ? 2 : 1;

    }

    public static class LocaleInfo
    {

        public String name;
        public String nameEnglish;
        public String shortName;
        public String pathToFile;
        public int version;
        public boolean builtIn;

        public static LocaleInfo createWithString(String string)
        {
            if (string == null || string.length() == 0) {
                return null;
            }
            String[] args = string.split("\\|");
            LocaleInfo localeInfo = null;
            if (args.length >= 4) {
                localeInfo = new LocaleInfo();
                localeInfo.name = args[0];
                localeInfo.nameEnglish = args[1];
                localeInfo.shortName = args[2].toLowerCase();
                localeInfo.pathToFile = args[3];
                if (args.length >= 5) {
                    localeInfo.version = Utilities.parseInt(args[4]);
                }
            }
            return localeInfo;
        }

        public String getSaveString()
        {
            return name + "|" + nameEnglish + "|" + shortName + "|" + pathToFile + "|" + version;
        }


        public String getKey()
        {
            if (pathToFile != null && !"remote".equals(pathToFile)) {
                return "local_" + shortName;
            }
            return shortName;
        }

        public boolean isRemote()
        {
            return "remote".equals(pathToFile);
        }

        public boolean isLocal()
        {
            return !TextUtils.isEmpty(pathToFile) && !isRemote();
        }

        public boolean isBuiltIn()
        {
            return builtIn;
        }
    }

    abstract public static class PluralRules
    {
        abstract int quantityForNumber(int n);
    }


    private class TimeZoneChangedReceiver extends BroadcastReceiver
    {
        @Override public void onReceive(Context context, Intent intent)
        {
            App.applicationHandler.post(new Runnable()
            {
                @Override public void run()
                {
                    /*
                    if (!formatterMonth.getTimeZone().equals(TimeZone.getDefault())) {
                        LocaleController.getInstance().recreateFormatters();
                    }
                    */
                }
            });
        }
    }
}
