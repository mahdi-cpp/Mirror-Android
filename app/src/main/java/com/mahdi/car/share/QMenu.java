package com.mahdi.car.share;


import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.Toast;

import com.mahdi.car.App;
import com.mahdi.car.dialog.popup.QDialog;
import com.mahdi.car.messenger.FileLog;
import com.mahdi.car.messenger.QSave;
import com.mahdi.car.messenger.User_Notification;
import com.mahdi.car.server.https.Server;
import com.mahdi.car.server.https.ServerDelegate;
import com.mahdi.car.server.model.Post;
import com.mahdi.car.server.model.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class QMenu {

    protected Dialog visibleDialog = null;

    private static volatile QMenu Instance = null;

    public QMenu() {

    }

    public static QMenu getInstance() {

        QMenu localInstance = Instance;
        if (localInstance == null) {
            synchronized (QMenu.class) {
                localInstance = Instance;
                if (localInstance == null) {
                    Instance = localInstance = new QMenu();
                }
            }
        }
        return localInstance;
    }

    public void clipboard(Post post) {
        try {

            String share;

            share = "Homegram\n";
            share += post.Location;
            share += "https://homegram.ir/" + post.User.Username + "/" + post.postid;
            share += "";
            share += "\n";
            share += "\n" + "برای مشاهده اطلاعات بیشتر اپلیکیشن هوم گرام را از لینک زیر دانلود کنید.  http://homegram.ir";

            ClipboardManager clipboard = (ClipboardManager) App.getApplication().getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("1234", share);
            clipboard.setPrimaryClip(clip);
            Toast.makeText(App.context, "کپی شد", Toast.LENGTH_SHORT).show();

        } catch (Throwable e) {
        }
    }

    public void post(Context context, Post post, MenuDelegate delegate) {
        post(context, post, false, delegate);
    }

    public void post(Context context, Post post, boolean isHome, MenuDelegate delegate) {


//        if (post.owner.userid == App.userid) {

            if (true) {

            QDialog.getInstance().list(post.Medias.get(0).Photo, QDialog.LIST_ME_POST, 0, new QMenuDelegate() {
                @Override
                public void click(int index) {

                    if (index == 0) {
                        clipboard(post);
                        QDialog.getInstance().hide();

                    } else if (index == 1) {
                        QDialog.getInstance().hide();
                        Server.setArchive(post.postid, 2, new ServerDelegate() {
                            @Override
                            public void response(Response<Integer> response) {
                                delegate.update();
                            }
                        });

                    } else if (index == 2) {
                        QDialog.getInstance().hide();
                        if (post != null) {
                            //MyActivity.actionBarLayout.presentFragment(new TicketFragment(post.postid));
                        }
                    } else if (index == 3) {

                        QDialog.getInstance().dialog.delete(post.Medias.get(0).Photo, "آیا واقعا قصد دارید این پست را حذف کنید؟", new QMenuDelegate() {
                            @Override
                            public void click(int index) {
                                QDialog.getInstance().hide();
                                if (index == 2) {

                                    Call<Integer> call = Server.post.setDelete(post.postid);
                                    call.enqueue(new Callback<Integer>() {
                                        @Override
                                        public void onResponse(Call<Integer> call, Response<Integer> response) {
                                            delegate.update();
                                        }

                                        @Override
                                        public void onFailure(Call<Integer> call, Throwable t) {

                                        }
                                    });
                                }
                            }
                        });
                    }
                }
            });



        } else {

            if (isHome) {

                String topic = "userid" + post.User.ID;
                int mode = 0;
                User_Notification user_notification = QSave.getInstance().getUser(post.User.ID);
                if (user_notification != null) {
                    mode = user_notification.mode;
                }
                int finalMode = mode;



//                QDialog.getInstance().list(post.medias.get(0).photo, QDialog.LIST_HOME_POST, mode, new QMenuDelegate() {
//                    @Override
//                    public void click(int index) {
//
//                        if (index == 0) {
//                            QDialog.getInstance().hide();
//                            clipboard(post);
//
//                        } else if (index == 1) {
//
//                            QDialog.getInstance().report(post);
//
//                        } else if (index == 2) {
//
//                            QDialog.getInstance().hide();
//                            if (finalMode == 0) {
//
//                                FirebaseMessaging.getInstance().subscribeToTopic(topic).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                    @Override
//                                    public void onComplete(@NonNull Task<Void> task) {
//
//                                        if (task.isSuccessful()) {
//                                            QSave.getInstance().addUser(post.owner.userid, "username", 1);
//                                            Toast.makeText(App.context, "اعلان های پست روشن", Toast.LENGTH_SHORT).show();
//                                        }
//                                    }
//                                });
//
//                            } else {
//                                FirebaseMessaging.getInstance().unsubscribeFromTopic(topic).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                    @Override
//                                    public void onComplete(@NonNull Task<Void> task) {
//
//                                        if (task.isSuccessful()) {
//                                            QSave.getInstance().addUser(post.owner.userid, "username", 0);
//                                            Toast.makeText(App.context, "اعلان های پست خاموش", Toast.LENGTH_SHORT).show();
//                                        }
//                                    }
//                                });
//                            }
//
//                        } else if (index == 3) {//unFollow
//                            QDialog.getInstance().hide();
//                            FirebaseMessaging.getInstance().unsubscribeFromTopic(topic).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                @Override
//                                public void onComplete(@NonNull Task<Void> task) {
//
//                                    if (task.isSuccessful()) {
//                                        Server.setFollowing(post.owner.userid, false, new ServerDelegate() {
//                                            @Override
//                                            public void response(Response<Integer> response) {
//                                                QSave.getInstance().addUser(post.owner.userid, post.owner.username, 0);
//                                                delegate.update();
//                                            }
//                                        });
//
//                                    }
//                                }
//                            });
//
//
//                        } else if (index == 4) {
//
//                            QDialog.getInstance().hide();
//                        }
//                    }
//                });


            } else {

                QDialog.getInstance().list(post.Medias.get(0).Photo, QDialog.LIST_POST, 0, new QMenuDelegate() {
                    @Override
                    public void click(int index) {

                        if (index == 0) {
                            QDialog.getInstance().hide();
                            clipboard(post);

                        } else if (index == 1) {

                            QDialog.getInstance().report(post);
                        }
                    }
                });

            }
        }
    }

    public void bookmark(Post post, boolean isBookmark, MenuDelegate delegate) {

        if (isBookmark) {

            if (post.User.ID == App.userid) {

                QDialog.getInstance().list(post.Medias.get(0).Photo, QDialog.LIST_BOOKMARK, 0, new QMenuDelegate() {
                    @Override
                    public void click(int index) {

                        if (index == 0) {

                            Server.setArchive(post.postid, 2, new ServerDelegate() {
                                @Override
                                public void response(Response<Integer> response) {
                                    delegate.update();
                                }
                            });

                            QDialog.getInstance().hide();

                        } else if (index == 1) {
                            if (post != null) {
                                //MyActivity.actionBarLayout.presentFragment(new TicketFragment(post.postid));
                            }
                            QDialog.getInstance().hide();
                        } else if (index == 2) {

                            QDialog.getInstance().dialog.delete(post.Medias.get(0).Photo, "آیا واقعا قصد دارید این پست را حذف کنید؟", new QMenuDelegate() {
                                @Override
                                public void click(int index) {
                                    QDialog.getInstance().hide();
                                    if (index == 2) {
                                        Call<Integer> call = Server.post.setDelete(post.postid);
                                        call.enqueue(new Callback<Integer>() {
                                            @Override
                                            public void onResponse(Call<Integer> call, Response<Integer> response) {
                                                if (response.isSuccessful()) {
                                                    delegate.update();
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<Integer> call, Throwable t) {

                                            }
                                        });
                                    }
                                }
                            });

                            QDialog.getInstance().hide();
                        }
                    }
                });

            } else {


            }

        } else {

            QDialog.getInstance().list(post.Medias.get(0).Photo, QDialog.LIST_ARCHIVE, 0, new QMenuDelegate() {
                @Override
                public void click(int index) {

                    if (index == 0) {
                        Server.setArchive(post.postid, 1, new ServerDelegate() {
                            @Override
                            public void response(Response<Integer> response) {
                                delegate.update();
                            }
                        });
                        QDialog.getInstance().hide();

                    } else if (index == 1) {

                        QDialog.getInstance().dialog.delete(post.Medias.get(0).Photo, "آیا واقعا قصد دارید این پست را حذف کنید؟", new QMenuDelegate() {
                            @Override
                            public void click(int index) {
                                QDialog.getInstance().hide();
                                if (index == 2) {
                                    Call<Integer> call = Server.post.setDelete(post.postid);
                                    call.enqueue(new Callback<Integer>() {
                                        @Override
                                        public void onResponse(Call<Integer> call, Response<Integer> response) {
                                            if (response.isSuccessful()) {
                                                delegate.update();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<Integer> call, Throwable t) {

                                        }
                                    });
                                }
                            }
                        });
                    }

                    QDialog.getInstance().hide();

                }
            });
        }
    }

//    public void polygon_profile(Context context, Polygon polygon, MenuDelegate delegate) {
//        QDialog.getInstance().list(null, QDialog.LIST_POLYGON, 0, new QMenuDelegate() {
//            @Override
//            public void click(int index) {
//                QDialog.getInstance().dialog.delete(null, "آیا واقعا قصد دارید این موقعیت ذخیره شده را حذف کنید؟", new QMenuDelegate() {
//                    @Override
//                    public void click(int index) {
//                        QDialog.getInstance().hide();
//                        if (index == 2) {
//
//                            Call<Integer> call = Server.map.polygon_delete(polygon.polygonid);
//                            call.enqueue(new Callback<Integer>() {
//                                @Override
//                                public void onResponse(Call<Integer> call, Response<Integer> response) {
//                                    if (response.isSuccessful()) {
//                                        delegate.update();
//                                    }
//                                }
//
//                                @Override
//                                public void onFailure(Call<Integer> call, Throwable t) {
//
//                                }
//                            });
//                        }
//                    }
//                });
//            }
//        });
//
//    }

    public void polygon_save(Context context, PolygonSaveDelegate delegate) {
        /*
        PolygonSaveDialog dialog = new PolygonSaveDialog(context, "یک نام برای ذخیره ی این موقعیت انتخاب کنید.", null);
        dialog.setDelegate(new PolygonSaveDialog.PolygonSaveDialogDelegate() {
            @Override
            public void ok(String name) {
                delegate.ok(name);
            }
        });
        showDialog(dialog.alertDialog);
        */
    }

    public void profile(Context context, User user, MenuDelegate delegate) {

        String topic = "userid" + user.ID;
        int mode = 0;
        User_Notification user_notification = QSave.getInstance().getUser(user.ID);
        if (user_notification != null) {
            mode = user_notification.mode;
        }
        int finalMode = mode;

        QDialog.getInstance().list(user.Avatar, QDialog.LIST_POST, mode, new QMenuDelegate() {
            @Override
            public void click(int index) {
                if (index == 0) {


                } else if (index == 1) {

                    /*
                    menu.dismiss();
                    ProfileDialog reportMenu = new ProfileDialog(context, ProfileDialog.MENU_REPORT_POST);
                    reportMenu.setDelegate(new ProfileDialog.ProfileDialogDelegate() {
                        @Override
                        public void click(int index) {

                        }
                    });
                    showDialog(reportMenu.alertDialog);
                    */

                    /*

                    if (finalMode == 0) {

                        FirebaseMessaging.getInstance().subscribeToTopic(topic)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        if (task.isSuccessful()) {
                                            QRealm.getInstance().addUser(user.userid, "username", 1);
                                            Toast.makeText(Application.context, "اعلان های پست روشن", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                    } else {
                        FirebaseMessaging.getInstance().unsubscribeFromTopic(topic)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        if (task.isSuccessful()) {
                                            QRealm.getInstance().addUser(user.userid, "username", 0);
                                            Toast.makeText(Application.context, "اعلان های پست خاموش", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
                    */
                }
            }
        });
    }

    private void firbase(int userid, String username) {
            /*

           String topic = "userid" + userid;

            FirebaseMessaging.getInstance().unsubscribeFromTopic(topic).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if (task.isSuccessful()) {

                    QRealm.getInstance().addUser(userid, username, 0);

                    } else {

                    }
                }
            });
            */
    }


    public Dialog showDialog(Dialog dialog) {

        if (dialog == null) {
            return null;
        }
        try {
            if (visibleDialog != null) {
                visibleDialog.dismiss();
                visibleDialog = null;
            }
        } catch (Exception e) {
            FileLog.e(e);
        }
        try {
            visibleDialog = dialog;
            visibleDialog.setCanceledOnTouchOutside(true);
            visibleDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    //if (onDismissListener != null) {
                    //onDismissListener.onDismiss(dialog);
                    //}
                    //onDialogDismiss(visibleDialog);
                    visibleDialog = null;
                }
            });
            visibleDialog.show();
            return visibleDialog;
        } catch (Exception e) {
            FileLog.e(e);
        }
        return null;
    }
}
