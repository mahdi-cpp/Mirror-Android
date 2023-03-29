package com.mahdi.car.messenger;


import android.app.Activity;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.mahdi.car.App;


public class QSave
{

    private Gson gson = new Gson();

    private static volatile QSave Instance = null;

    public QSave() {


    }

    public static QSave getInstance() {

        QSave localInstance = Instance;
        if (localInstance == null) {
            synchronized (QSave.class) {
                localInstance = Instance;
                if (localInstance == null) {
                    Instance = localInstance = new QSave();
                }
            }
        }
        return localInstance;
    }

    /*
    public void setUserMode(int userid, int mode) {

        Realm realm = Realm.getDefaultInstance();

        try {
            realm.beginTransaction();
            User_Notification user_notification = realm.createObject(User_Notification.class, 12);
            user_notification.mode = 2;
            realm.commitTransaction();

        } catch (RealmPrimaryKeyConstraintException e) {

        } finally {
            realm.close();
        }
    }
    */

    public int addUser(int userid, String username, int mode) {

        User_Notification obj = new User_Notification();
        obj.userid = userid;
        obj.username = username;
        obj.mode = mode;

        String notification = gson.toJson(obj);

        SharedPreferences preferences = App.applicationContext.getSharedPreferences("firebase", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("" + userid, notification);
        editor.apply();

        /*
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                // This will create a new object in Realm or throw an exception if the
                // object already exists (same primary key)
                // realm.copyToRealm(obj);

                // This will AutoUpdate an existing object with the same primary key
                // or create a new object if an object with no primary key = 42
                //realm.copyToRealmOrUpdate(obj);

                User_Notification obj = new User_Notification();
                obj.userid = userid;
                obj.username = username;
                obj.mode = mode;
                //realm.copyToRealmOrUpdate(obj);

            }
        });

        /*
        User_Notification user_notification = realm.createObject(User_Notification.class);
        user_notification.userid = 7;

        user_notification.deleteFromRealm();

        try {
            User_Notification person = realm.where(User_Notification.class).equalTo("userid", userid).findFirst();
            // do something with the person ...
        } finally {
            realm.close();
        }
        */

        return 2;

    }

    public User_Notification getUser(int userid) {


        SharedPreferences preferences = App.applicationContext.getSharedPreferences("firebase", Activity.MODE_PRIVATE);
        String notification_string = preferences.getString("" + userid, null);

        User_Notification notification = gson.fromJson(notification_string, User_Notification.class);

        //Realm realm = Realm.getDefaultInstance();
        //User_Notification obj = realm.where(User_Notification.class).equalTo("userid", userid).findFirst();
        return notification;
    }

    /*
    public RealmResults<User_Notification> getUsers(int userid) {

        Realm realm = Realm.getDefaultInstance();
        RealmResults<User_Notification> result = realm.where(User_Notification.class).findAll();
        return result;
    }
    */

}
