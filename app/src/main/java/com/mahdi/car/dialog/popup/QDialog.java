package com.mahdi.car.dialog.popup;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Context;
import android.graphics.*;

import android.text.Layout;
import android.text.StaticLayout;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;

import android.widget.Toast;


import com.mahdi.car.core.cell.CellView;
import com.mahdi.car.share.Themp;
import com.mahdi.car.App;
import com.mahdi.car.share.component.ui.LayoutHelper;

import com.mahdi.car.server.dtos.ReportDTO;
import com.mahdi.car.library.viewAnimator.ViewAnimator;
import com.mahdi.car.messenger.AndroidUtilities;
import com.mahdi.car.server.model.Post;
import com.mahdi.car.server.https.Server;
import com.mahdi.car.share.QMenuDelegate;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class QDialog
{

    private static int listTypeCount = 0;
    public static final int LIST_HOME_POST = listTypeCount++;
    public static final int LIST_POST = listTypeCount++;
    public static final int LIST_ME_POST = listTypeCount++;
    public static final int LIST_USER = listTypeCount++;
    public static final int LIST_ME_PROFILE = listTypeCount++;
    public static final int LIST_PROFILE = listTypeCount++;
    public static final int LIST_POLYGON = listTypeCount++;
    public static final int LIST_ARCHIVE = listTypeCount++;
    public static final int LIST_BOOKMARK = listTypeCount++;
    public static final int LIST_REPORT_POST = listTypeCount++;
    public static final int LIST_PROFILE_EDIT_PHOTO = listTypeCount++;

    private Activity parentActivity;

    private FrameLayout containerView;
    public Dialog dialog;

    private int duration1 = 200;
    private int duration2 = 150;

    private boolean permission = true;
    private static volatile QDialog Instance = null;

    public static QDialog getInstance()
    {
        QDialog localInstance = Instance;
        if (localInstance == null) {
            synchronized (QDialog.class) {
                localInstance = Instance;
                if (localInstance == null) {
                    Instance = localInstance = new QDialog();
                }
            }
        }
        return localInstance;
    }

    public void setParentActivity(final Activity activity)
    {
        if (activity == null || parentActivity == activity) {
            return;
        }

        parentActivity = activity;
        permission = true;

        containerView = new FrameLayout(parentActivity);
        containerView.setBackgroundColor(0x66000000);
        containerView.setAlpha(0);
        containerView.setVisibility(View.INVISIBLE);

        dialog = new Dialog(parentActivity);
        dialog.setClickable(true);
        dialog.setAlpha(0);
        dialog.setScaleX(0);
        dialog.setScaleY(0);

        containerView.addView(dialog, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT, Gravity.CENTER));
        parentActivity.addContentView(containerView, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT));
    }

    public boolean isShow()
    {
        return !permission;
    }

    public void list(String photo, int dialogType, int mode, QMenuDelegate delegate)
    {

        if (permission == false) {
            return;
        }
        permission = false;

        dialog.list(photo, dialogType, mode, delegate);
        show();
    }

    public void report(Post post)
    {
        dialog.report(post);
    }

    public void delete(String photo, String message, QMenuDelegate delegate)
    {

        if (permission == false) {
            return;
        }
        permission = false;
        dialog.delete(photo, message, delegate);
        show();
    }

    public void unFollow(String username, String avatar, QMenuDelegate delegate)
    {

        if (permission == false) {
            return;
        }
        permission = false;
        dialog.unFollow(username, avatar, delegate);
        show();
    }

    public void information(String title, String message)
    {

        if (permission == false) {
            return;
        }
        permission = false;
        dialog.information(title, message, null);
        show();
    }

    public void show()
    {

        containerView.setVisibility(View.VISIBLE);

        ViewAnimator.animate(containerView).setInterpolator(new DecelerateInterpolator()).alpha(1).setDuration(duration1).start();

        ViewAnimator.animate(dialog).setInterpolator(new DecelerateInterpolator()).scales(1).alpha(1).setDuration(duration2).addListener(new AnimatorListenerAdapter()
        {
            @Override
            public void onAnimationEnd(Animator animation)
            {
                super.onAnimationEnd(animation);
            }
        }).start();
    }

    public void hide()
    {

        ViewAnimator.animate(containerView).setInterpolator(new DecelerateInterpolator()).alpha(0).setDuration(200).start();

        ViewAnimator.animate(dialog).setInterpolator(new DecelerateInterpolator()).alpha(0).scales(0.2f).setDuration(200).addListener(new AnimatorListenerAdapter()
        {
            @Override
            public void onAnimationEnd(Animator animation)
            {
                super.onAnimationEnd(animation);
                permission = true;
                containerView.setVisibility(View.INVISIBLE);
            }
        }).start();

    }

    public static class Dialog extends CellView
    {

        private QMenuDelegate delegate;

        private static int typeCount = 0;
        public static final int TYPE_LIST = typeCount++;
        public static final int TYPE_DELETE = typeCount++;
        public static final int TYPE_UNFOLLOW = typeCount++;
        public static final int TYPE_INFOMATION = typeCount++;

        public String[] items;
        private String title;
        private String message;
        private String messageLeft;
        private String messageCenter;
        private String messageRight;

        private StaticLayout messageLayout;
        private StaticLayout staticLayout;


        private int width = AndroidUtilities.width;
        private int height = AndroidUtilities.screenHeight;
        private int size = dp(72);
        private int cellHeight = dp(46);
        private int round = dp(8);
        private int isPressed = -1;
        private int CENTER_HORIZONTAL = width / 2;
        private int count;
        private int topMargin;
        private int topMargin2;
        private int rightMargin = dp(30);
        private int leftMargin = rightMargin;
        private int window = width - leftMargin - rightMargin;

        private int buttonStart = dp(106);
        private int buttonHeight = dp(54);

        private int dialogType = -1;
        private int listType = -1;

        private int userid = 0;
        private long postid = 0;

        public Dialog(Context context)
        {
            super(context);
            setBackgroundColor(0x00000000);
        }

        public void list(String photo, int listType, int mode, QMenuDelegate delegate)
        {

            this.delegate = delegate;
            this.dialogType = TYPE_LIST;
            this.listType = listType;

            if (listType == LIST_ME_PROFILE) {
                items = new String[]{"بایگانی", "ویرایش", "حذف"};
            } else if (listType == LIST_PROFILE) {
                items = new String[]{"گزارش...", "اعلان های پست روشن شود"};
            } else if (listType == LIST_HOME_POST) {
                if (mode == 0) {
                    items = new String[]{"کپی کردن پیوند", "گزارش تخلف ...", "اعلان های پست روشن شود", "لغو دنبال کردن"};
                } else {
                    items = new String[]{"کپی کردن پیوند", "گزارش تخلف ...", "اعلان های پست خاموش شود", "لغو دنبال کردن"};
                }
            } else if (listType == LIST_POST) {
                items = new String[]{"کپی کردن پیوند", "گزارش تخلف ...",};
            } else if (listType == LIST_ME_POST) {
                items = new String[]{"کپی کردن پیوند", "بایگانی", "ویرایش", "حذف"};
            } else if (listType == LIST_USER) {
                items = new String[]{"گزارش..."};
            } else if (listType == LIST_POLYGON) {
                items = new String[]{"حذف"};
            } else if (listType == LIST_ARCHIVE) {
                items = new String[]{"نمایش در نمایه", "حذف"};
            } else if (listType == LIST_BOOKMARK) {
                items = new String[]{"گزارش اشکال آگهی"};
            } else if (listType == LIST_PROFILE_EDIT_PHOTO) {
                items = new String[]{"عکس نمایه جدید", "حذف عکس نمایه"};
            } else {
                items = new String[]{"Empty"};
            }

            count = items.length;
            topMargin = (height - (cellHeight * (count + 2))) / 2 + dp(20);
            topMargin2 = topMargin + dp(50);

            photo(photo);
        }

        public void information(String title, String message, QMenuDelegate delegate)
        {

            this.delegate = delegate;
            this.dialogType = TYPE_INFOMATION;
            this.title = title;
            this.message = message;

            messageCenter = "تایید";

            count = 2;
            topMargin = (height - (cellHeight * (count + 2))) / 2 + dp(20);
            topMargin2 = topMargin + dp(50);

        }

        public void delete(String photo, String message, QMenuDelegate delegate)
        {

            this.delegate = delegate;
            this.dialogType = TYPE_DELETE;
            this.message = message;

            messageLeft = "خیر";
            messageRight = "بله";

            count = 3;
            topMargin = (height - (cellHeight * (count + 2))) / 2 + dp(20);
            topMargin2 = topMargin + dp(50);

            photo(photo);
        }

        public void unFollow(String username, String avatar, QMenuDelegate delegate)
        {

            this.delegate = delegate;
            this.dialogType = TYPE_DELETE;
            this.message = " دنبال کردن  " + username + "  لغو شود؟ ";
            messageLeft = "لغو دنبال کردن";
            messageRight = "لغو";

            count = 3;
            topMargin = (height - (cellHeight * (count + 2))) / 2 + dp(20);
            topMargin2 = topMargin + dp(50);

            photo(avatar);
        }

        private void photo(String photo)
        {
            setMedia(App.files + photo, 400, 400, size);

            invalidate();
        }


        public void report(Post post)
        {

            this.delegate = null;
            this.dialogType = TYPE_LIST;
            this.listType = LIST_REPORT_POST;

            this.userid = post.User.ID;
            this.postid = post.postid;

            items = new String[]{"تصاویر آگهی نامناسب است.", "متن آگهی نامناسب است.", "اطلاعات آگهی گمراه کننده است.", "آگهی فروخته شده و یا نامجود است", "آگهی اسپم است و چندین بار پست شده است.", "قیمت آگهی نامناسب است.", "املاک آگهی شده دیگر موجود نیست.", "دلایل دیگر..."};

            count = items.length;
            topMargin = (height - (cellHeight * (count + 2))) / 2 + dp(20);
            topMargin2 = topMargin + dp(50);

            invalidate();
        }

        @Override
        protected void dispatchDraw(Canvas canvas)
        {

            super.dispatchDraw(canvas);

            canvas.drawRoundRect(new RectF(leftMargin, topMargin, width - rightMargin, topMargin + (cellHeight * (count + 2)) - dp(20)), round, round, Themp.PAINT_WHITE);

            canvas.drawCircle(CENTER_HORIZONTAL, topMargin, size / 2 + dp(3), Themp.PAINT_WHITE);


            drawMedia(CENTER_HORIZONTAL - (size / 2), topMargin - (size / 2), CENTER_HORIZONTAL - (size / 2) + size, topMargin - (size / 2) + size);

            if (dialogType == TYPE_LIST) {
                if (isPressed >= 0) {
                    int topPressed = topMargin2 - dp(5);
                    canvas.drawRect(new RectF(leftMargin, topPressed + (isPressed * cellHeight), width - rightMargin, topPressed + (isPressed * cellHeight) + cellHeight), Themp.PAINT_PRESS_BLACK);
                }

                for (int i = 0; i < count; i++) {
                    canvas.save();
                    canvas.translate(0, (i * cellHeight) + topMargin2 + dp(10));
                    staticLayout = new StaticLayout(items[i], Themp.TEXT_PAINT_FILL_BLACK[6], width - rightMargin - dp(20), Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
                    staticLayout.draw(canvas);
                    canvas.restore();
                }

            } else if (dialogType == TYPE_DELETE) {

                canvas.save();
                canvas.translate(leftMargin, topMargin2 + dp(30));
                messageLayout = new StaticLayout(message, Themp.TEXT_PAINT_FILL_BLACK[6], width - leftMargin - rightMargin, Layout.Alignment.ALIGN_CENTER, 1.3f, 0.3f, false);
                messageLayout.draw(canvas);
                canvas.restore();


                canvas.drawLine(leftMargin, topMargin2 + dp(106), width - rightMargin, topMargin2 + dp(106), Themp.STROKE_PAINT_PX_GREY);
                canvas.drawLine(CENTER_HORIZONTAL, topMargin2 + dp(114), CENTER_HORIZONTAL, topMargin2 + dp(106 + 40), Themp.STROKE_PAINT_PX_GREY);


                canvas.save();
                canvas.translate(leftMargin, topMargin2 + dp(120));
                staticLayout = new StaticLayout(messageLeft, Themp.TEXT_PAINT_FILL_AND_STROKE_2_BLACK[6], window / 2, Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
                staticLayout.draw(canvas);
                canvas.restore();

                canvas.save();
                canvas.translate(leftMargin + window / 2, topMargin2 + dp(120));
                staticLayout = new StaticLayout(messageRight, Themp.TEXT_PAINT_FILL_AND_STROKE_2_BLACK[6], window / 2, Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
                staticLayout.draw(canvas);
                canvas.restore();

                if (isPressed == 1) {
                    canvas.drawRect(new RectF(leftMargin, topMargin2 + buttonStart, leftMargin + window / 2, topMargin2 + buttonStart + buttonHeight), Themp.PAINT_PRESS_BLACK);
                } else if (isPressed == 2) {
                    canvas.drawRect(new RectF(leftMargin + window / 2, topMargin2 + buttonStart, leftMargin + window, topMargin2 + buttonStart + buttonHeight), Themp.PAINT_PRESS_BLACK);
                }

            } else if (dialogType == TYPE_INFOMATION) {

                canvas.save();
                canvas.translate(leftMargin, topMargin2 - dp(10));
                messageLayout = new StaticLayout(title, Themp.TEXT_PAINT_FILL_BLACK[8], width - leftMargin - rightMargin, Layout.Alignment.ALIGN_CENTER, 1.3f, 0.3f, false);
                messageLayout.draw(canvas);
                canvas.restore();

                canvas.save();
                canvas.translate(leftMargin, topMargin2 + dp(30));
                messageLayout = new StaticLayout(message, Themp.TEXT_PAINT_FILL_GREY[3], width - leftMargin - rightMargin, Layout.Alignment.ALIGN_CENTER, 1.3f, 0.3f, false);
                messageLayout.draw(canvas);
                canvas.restore();
            }
        }

        @Override
        public boolean onTouchEvent(MotionEvent event)
        {

            float x = event.getX();
            float y = event.getY();

            switch (event.getAction()) {

                case MotionEvent.ACTION_DOWN:

                    if (dialogType == TYPE_LIST) {
                        for (int i = 0; i < count; i++) {
                            if (x > leftMargin && x < width - rightMargin && y > topMargin2 + (i * cellHeight) && y < (topMargin2 + (i * cellHeight)) + cellHeight) {
                                isPressed = i;
                            }
                        }
                    } else if (dialogType == TYPE_DELETE) {
                        if (x > leftMargin && x < leftMargin + window / 2 && y > topMargin2 + buttonStart && y < topMargin2 + buttonStart + buttonHeight) {
                            isPressed = 1;
                        } else if (x > leftMargin + window / 2 && x < width - rightMargin && y > topMargin2 + buttonStart && y < topMargin2 + buttonStart + buttonHeight) {
                            isPressed = 2;
                        }
                    }

                    invalidate();

                    break;
                case MotionEvent.ACTION_MOVE:
                    break;
                case MotionEvent.ACTION_UP:

                    if (dialogType == TYPE_LIST) {
                        for (int i = 0; i < count; i++) {
                            if (x > leftMargin && x < width - rightMargin && y > topMargin2 + (i * cellHeight) && y < (topMargin2 + (i * cellHeight)) + cellHeight && isPressed == i) {

                                if (listType == LIST_REPORT_POST) {
                                    hide();
                                    QDialog.getInstance().processReport(userid, postid, i);

                                } else {
                                    delegate.click(i);
                                }

                            }
                        }
                    } else if (dialogType == TYPE_DELETE) {
                        if (x > leftMargin && x < leftMargin + window / 2 && y > topMargin2 + dp(106) && y < topMargin2 + dp(106 + 50) && isPressed == 1) {
                            delegate.click(1);
                            hide();
                        } else if (x > leftMargin + window / 2 && x < width - rightMargin && y > topMargin2 + dp(106) && y < topMargin2 + dp(106 + 50) && isPressed == 2) {
                            delegate.click(2);
                            hide();
                        }
                    }

                    if (isPressed == -1) {
                        //delegate.dismiss();
                        hide();
                    }

                    isPressed = -1;
                    invalidate();
                    break;

                default:
                    isPressed = -1;
                    invalidate();
            }
            return true;
        }

        private void hide()
        {
            QDialog.getInstance().hide();
        }
    }

    private void processReport(int owner_userid, long postid, int type)
    {

        ReportDTO reportDTO = new ReportDTO();
        reportDTO.reporter_userid = App.userid;
        reportDTO.owner_userid = owner_userid;
        reportDTO.postid = postid;
        reportDTO.type = type;

        Call<Long> call = Server.report.addReport(reportDTO);
        call.enqueue(new Callback<Long>()
        {
            @Override
            public void onResponse(Call<Long> call, Response<Long> response)
            {
                if (response.isSuccessful()) {
                    Toast.makeText(containerView.getContext(), "ثبت شد", Toast.LENGTH_SHORT).show();
                } else {

                }
            }

            @Override
            public void onFailure(Call<Long> call, Throwable t)
            {

            }
        });
    }

    public static class PopupPost extends CellView
    {
        private QMenuDelegate delegate;

        private static int typeCount = 0;
        public static final int TYPE_LIST = typeCount++;
        public static final int TYPE_DELETE = typeCount++;
        public static final int TYPE_UNFOLLOW = typeCount++;

        public String[] items;
        private String message;
        private String messageLeft;
        private String messageRight;

        private StaticLayout messageLayout;
        private StaticLayout staticLayout;

        private int width = AndroidUtilities.width;
        private int height = AndroidUtilities.screenHeight;
        private int size = dp(72);
        private int cellHeight = dp(46);
        private int round = dp(8);
        private int isPressed = -1;
        private int CENTER_HORIZONTAL = width / 2;
        private int count;
        private int topMargin;
        private int topMargin2;
        private int rightMargin = dp(30);
        private int leftMargin = rightMargin;
        private int window = width - leftMargin - rightMargin;

        private int buttonStart = dp(106);
        private int buttonHeight = dp(54);

        private int dialogType = -1;
        private int listType = -1;

        private int userid = 0;
        private long postid = 0;

        public PopupPost(Context context)
        {
            super(context);
            setBackgroundColor(0x00000000);
        }

        public void list(String photo, int listType, int mode, QMenuDelegate delegate)
        {

            this.delegate = delegate;
            this.dialogType = TYPE_LIST;
            this.listType = listType;

            if (listType == LIST_ME_PROFILE) {
                items = new String[]{"بایگانی", "ویرایش", "حذف"};
            } else if (listType == LIST_PROFILE) {
                items = new String[]{"گزارش...", "اعلان های پست روشن شود"};
            } else if (listType == LIST_HOME_POST) {
                if (mode == 0) {
                    items = new String[]{"کپی کردن پیوند", "گزارش تخلف ...", "اعلان های پست روشن شود", "لغو دنبال کردن"};
                } else {
                    items = new String[]{"کپی کردن پیوند", "گزارش تخلف ...", "اعلان های پست خاموش شود", "لغو دنبال کردن"};
                }
            } else if (listType == LIST_POST) {
                items = new String[]{"کپی کردن پیوند", "گزارش تخلف ...",};
            } else if (listType == LIST_ME_POST) {
                items = new String[]{"کپی کردن پیوند", "بایگانی", "ویرایش", "حذف"};
            } else if (listType == LIST_USER) {
                items = new String[]{"گزارش..."};
            } else if (listType == LIST_POLYGON) {
                items = new String[]{"حذف"};
            } else if (listType == LIST_ARCHIVE) {
                items = new String[]{"نمایش در نمایه", "حذف"};
            } else if (listType == LIST_BOOKMARK) {
                items = new String[]{"گزارش اشکال آگهی"};
            } else if (listType == LIST_PROFILE_EDIT_PHOTO) {
                items = new String[]{"عکس نمایه جدید", "حذف عکس نمایه"};
            } else {
                items = new String[]{"Empty"};
            }

            count = items.length;
            topMargin = (height - (cellHeight * (count + 2))) / 2 + dp(20);
            topMargin2 = topMargin + dp(50);

            photo(photo);
        }

        public void delete(String photo, String message, QMenuDelegate delegate)
        {
            this.delegate = delegate;
            this.dialogType = TYPE_DELETE;
            this.message = message;

            messageLeft = "خیر";
            messageRight = "بله";

            count = 3;
            topMargin = (height - (cellHeight * (count + 2))) / 2 + dp(20);
            topMargin2 = topMargin + dp(50);

            photo(photo);
        }

        public void unFollow(String username, String avatar, QMenuDelegate delegate)
        {

            this.delegate = delegate;
            this.dialogType = TYPE_DELETE;
            this.message = " دنبال کردن  " + username + "  لغو شود؟ ";
            messageLeft = "لغو دنبال کردن";
            messageRight = "لغو";

            count = 3;
            topMargin = (height - (cellHeight * (count + 2))) / 2 + dp(20);
            topMargin2 = topMargin + dp(50);

            photo(avatar);
        }

        private void photo(String photo)
        {
            setMedia(App.files + photo, 400, 400, size);
        }

        public void report(Post post)
        {
            this.delegate = null;
            this.dialogType = TYPE_LIST;
            this.listType = LIST_REPORT_POST;

            this.userid = post.User.ID;
            this.postid = post.postid;

            items = new String[]{"تصاویر آگهی نامناسب است.", "متن آگهی نامناسب است.", "اطلاعات آگهی گمراه کننده است.", "آگهی فروخته شده و یا نامجود است", "آگهی اسپم است و چندین بار پست شده است.", "قیمت آگهی نامناسب است.", "املاک آگهی شده دیگر موجود نیست.", "دلایل دیگر..."};

            count = items.length;
            topMargin = (height - (cellHeight * (count + 2))) / 2 + dp(20);
            topMargin2 = topMargin + dp(50);

            invalidate();
        }

        @Override
        protected void dispatchDraw(Canvas canvas)
        {

            super.dispatchDraw(canvas);

            canvas.drawRoundRect(new RectF(leftMargin, topMargin, width - rightMargin, topMargin + (cellHeight * (count + 2)) - dp(20)), round, round, Themp.PAINT_WHITE);

            //            if (drawable != null) {
            //                canvas.drawCircle(CENTER_HORIZONTAL, topMargin, size / 2 + dp(3), Themp.PAINT_WHITE);
            //                drawable.setBounds();
            //                drawable.draw(canvas);
            //            }

            canvas.drawCircle(CENTER_HORIZONTAL, topMargin, size / 2 + dp(3), Themp.PAINT_WHITE);
            drawMedia(CENTER_HORIZONTAL - (size / 2), topMargin - (size / 2), CENTER_HORIZONTAL - (size / 2) + size, topMargin - (size / 2) + size);

            if (dialogType == TYPE_LIST) {
                if (isPressed >= 0) {
                    int topPressed = topMargin2 - dp(5);
                    canvas.drawRect(new RectF(leftMargin, topPressed + (isPressed * cellHeight), width - rightMargin, topPressed + (isPressed * cellHeight) + cellHeight), Themp.PAINT_PRESS_BLACK);
                }

                for (int i = 0; i < count; i++) {
                    canvas.save();
                    canvas.translate(0, (i * cellHeight) + topMargin2 + dp(10));
                    staticLayout = new StaticLayout(items[i], Themp.TEXT_PAINT_FILL_BLACK[6], width - rightMargin - dp(20), Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
                    staticLayout.draw(canvas);
                    canvas.restore();
                }

            } else if (dialogType == TYPE_DELETE) {

                canvas.save();
                canvas.translate(leftMargin, topMargin2 + dp(30));
                messageLayout = new StaticLayout(message, Themp.TEXT_PAINT_FILL_BLACK[6], width - leftMargin - rightMargin, Layout.Alignment.ALIGN_CENTER, 1.3f, 0.3f, false);
                messageLayout.draw(canvas);
                canvas.restore();

                canvas.drawLine(leftMargin, topMargin2 + dp(106), width - rightMargin, topMargin2 + dp(106), Themp.STROKE_PAINT_PX_GREY);
                canvas.drawLine(CENTER_HORIZONTAL, topMargin2 + dp(114), CENTER_HORIZONTAL, topMargin2 + dp(106 + 40), Themp.PAINT_WHITE);


                canvas.save();
                canvas.translate(leftMargin, topMargin2 + dp(120));
                staticLayout = new StaticLayout(messageLeft, Themp.TEXT_PAINT_FILL_AND_STROKE_2_BLACK[6], window / 2, Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
                staticLayout.draw(canvas);
                canvas.restore();

                canvas.save();
                canvas.translate(leftMargin + window / 2, topMargin2 + dp(120));
                staticLayout = new StaticLayout(messageRight, Themp.TEXT_PAINT_FILL_AND_STROKE_2_BLACK[6], window / 2, Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
                staticLayout.draw(canvas);
                canvas.restore();

                if (isPressed == 1) {
                    canvas.drawRect(new RectF(leftMargin, topMargin2 + buttonStart, leftMargin + window / 2, topMargin2 + buttonStart + buttonHeight), Themp.PAINT_PRESS_BLACK);
                } else if (isPressed == 2) {
                    canvas.drawRect(new RectF(leftMargin + window / 2, topMargin2 + buttonStart, leftMargin + window, topMargin2 + buttonStart + buttonHeight), Themp.PAINT_PRESS_BLACK);
                }
            }
        }

        @Override
        public boolean onTouchEvent(MotionEvent event)
        {
            float x = event.getX();
            float y = event.getY();

            switch (event.getAction()) {

                case MotionEvent.ACTION_DOWN:

                    if (dialogType == TYPE_LIST) {
                        for (int i = 0; i < count; i++) {
                            if (x > leftMargin && x < width - rightMargin && y > topMargin2 + (i * cellHeight) && y < (topMargin2 + (i * cellHeight)) + cellHeight) {
                                isPressed = i;
                            }
                        }
                    } else if (dialogType == TYPE_DELETE) {
                        if (x > leftMargin && x < leftMargin + window / 2 && y > topMargin2 + buttonStart && y < topMargin2 + buttonStart + buttonHeight) {
                            isPressed = 1;
                        } else if (x > leftMargin + window / 2 && x < width - rightMargin && y > topMargin2 + buttonStart && y < topMargin2 + buttonStart + buttonHeight) {
                            isPressed = 2;
                        }
                    }

                    invalidate();

                    break;
                case MotionEvent.ACTION_MOVE:
                    break;
                case MotionEvent.ACTION_UP:

                    if (dialogType == TYPE_LIST) {
                        for (int i = 0; i < count; i++) {
                            if (x > leftMargin && x < width - rightMargin && y > topMargin2 + (i * cellHeight) && y < (topMargin2 + (i * cellHeight)) + cellHeight && isPressed == i) {

                                if (listType == LIST_REPORT_POST) {
                                    hide();
                                    QDialog.getInstance().processReport(userid, postid, i);

                                } else {
                                    delegate.click(i);
                                }

                            }
                        }
                    } else if (dialogType == TYPE_DELETE) {
                        if (x > leftMargin && x < leftMargin + window / 2 && y > topMargin2 + dp(106) && y < topMargin2 + dp(106 + 50) && isPressed == 1) {
                            delegate.click(1);
                            hide();
                        } else if (x > leftMargin + window / 2 && x < width - rightMargin && y > topMargin2 + dp(106) && y < topMargin2 + dp(106 + 50) && isPressed == 2) {
                            delegate.click(2);
                            hide();
                        }
                    }

                    if (isPressed == -1) {
                        //delegate.dismiss();
                        hide();
                    }

                    isPressed = -1;
                    invalidate();
                    break;

                default:
                    isPressed = -1;
                    invalidate();
            }
            return true;
        }

        private void hide()
        {
            QDialog.getInstance().hide();
        }
    }

    //Utility functions----------------------------------------------------------
    public static int dp(float value)
    {
        if (value == 0) {
            return 0;
        }
        return (int) Math.ceil(AndroidUtilities.mDensity * value);
    }
    //-------------------------------------------------------------------------------

}
