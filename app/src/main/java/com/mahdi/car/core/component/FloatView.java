package com.mahdi.car.core.component;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.text.Layout;
import android.text.StaticLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;

import com.mahdi.car.core.cell.CellView;
import com.mahdi.car.server.model.User;
import com.mahdi.car.share.Themp;
import com.mahdi.car.share.component.ui.LayoutHelper;

public class FloatView extends CellView {

    private static int itemCount = 0;
    private static final int ITEM_BUTTON = itemCount++;

    private StaticLayout toolbarTitleLayout;
    private StaticLayout toolbarNameLayout;

    private StaticLayout descriptionLayout;
    private StaticLayout titleLayout;

    private StaticLayout videoBitrateLayout;
    private StaticLayout videoSizeLayout;

    private Drawable[] drawables = new Drawable[4];
    private String[] photos = new String[4];

    private int space = dp(16);
    private int photoSize = dp(30);
    private int heightSize = dp(40);

    private int button_y_shift = screenH - dp(200);
    private boolean buttonVisible = true;


    private int bottomToolBar = dp(48);
    private int headerHeight = dp(54);

    public FloatView(Context context) {

        super(context);

        setBackgroundColor(0xffffffff);
        setLayoutParams(LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT, Gravity.TOP, 0, 0, 0, 0));

        startStoryRing();

        round = dp(18);
        cellWidth = (width - (space * 3)) / 2;
        space = dp(16);

        avatarSize = dp(33);
        avatarX = dp(15);
        avatarY = dp(11);

        setAllPosts("Mahdi Abdolmaleki");
    }

    public void setAllPosts(String name) {

        for (int i = 0; i < 4; i++) {
            drawables[i] = null;
            photos[i] = null;
        }

        photos[0] = "mov_15208_24787-m.jpg";

        if (photos[0] != null) {
            setGallery(drawables, 0, photos[0]);
        }

        if (name == null) {
            name = "";
        }

        if (name.length() > 20) {
            name = name.substring(0, 20) + " ...";
        }
        User user = new User();
        userid = user.ID;
        username = user.Username;
        user.Avatar = "00044.jpg";
        setAvatar(user.Avatar);

        toolbarNameLayout = new StaticLayout(name, Themp.TEXT_PAINT_FILL_AND_STROKE_3_BLACK[5], dp(200), Layout.Alignment.ALIGN_NORMAL, 1.2f, 0.2f, false);
        toolbarTitleLayout = new StaticLayout("Screen is live on display", Themp.TEXT_PAINT_FILL_GREY[5], dp(200), Layout.Alignment.ALIGN_NORMAL, 1.2f, 0.2f, false);

        titleLayout = new StaticLayout("Screen Mirror On Ubuntu Desktop", Themp.TEXT_PAINT_FILL_AND_STROKE_3_BLACK[11], width, Layout.Alignment.ALIGN_CENTER, 1.2f, 0.2f, false);
        descriptionLayout = new StaticLayout("Screen is live on display", Themp.TEXT_PAINT_FILL_GREY[8], width, Layout.Alignment.ALIGN_CENTER, 1.2f, 0.2f, false);

        videoBitrateLayout = new StaticLayout("Bitrate:               2M", Themp.TEXT_PAINT_FILL_AND_STROKE_1_BLACK[6], width, Layout.Alignment.ALIGN_NORMAL, 1.2f, 0.2f, false);
        videoSizeLayout = new StaticLayout("Resolution:       488x1080", Themp.TEXT_PAINT_FILL_AND_STROKE_1_BLACK[6], width, Layout.Alignment.ALIGN_NORMAL, 1.2f, 0.2f, false);

        invalidate();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //canvas.drawBitmap(Themp.mainToolBarIcons[1], dp(65), dp(13) , Themp.ICON_PAINT_MULTIPLY_BLACK);

        drawAvatar(avatarX, avatarY);

        drawTextLayout(toolbarNameLayout, dp(65 + 0), dp(7));
        drawTextLayout(toolbarTitleLayout, dp(65 + 0), dp(27));

        //canvas.drawLine(0, dp(0), width, dp(0), Themp.PAINT_GRAY);
        canvas.drawLine(0, dp(54), width, dp(54), Themp.PAINT_GRAY);
        canvas.drawLine(0, screenH - bottomToolBar - statusBarHeight, width, screenH - bottomToolBar - statusBarHeight, Themp.PAINT_GRAY);

        //shadows
        Themp.drawableToolbar[0].setBounds(0, 0, width, dp(54));
        Themp.drawableToolbar[0].draw(canvas);

//        Themp.drawableToolbar[1].setBounds(0, getHeight() - dp(60), width, getHeight() -dp(55));
//        Themp.drawableToolbar[1].draw(canvas);

        canvas.drawBitmap(Themp.toolbar.cast_large, centerX - 100, dp(100), Themp.ICON_PAINT_MULTIPLY_BLACK);

        drawTextLayout(titleLayout, 0, dp(180));
        drawTextLayout(descriptionLayout, 0, dp(230));


        drawTextLayout(videoBitrateLayout, dp(60), dp(350));
        drawTextLayout(videoSizeLayout, dp(60), dp(350 + 25));

        if (buttonVisible) {
            drawRoundRect(dp(70), button_y_shift, new RectF(0, 0, width - dp(140), dp(40)), dp(5), isPressed == ITEM_BUTTON ? Themp.PAINT_BLACK : Themp.PAINT_BLUE);
            StaticLayout layout = new StaticLayout("Finish Screen Mirror", Themp.TEXT_PAINT_FILL_AND_STROKE_3_WHITE[8], getWidth(), Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
            drawTextLayout(layout, 0, button_y_shift + dp(+7));
        }

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {

            case MotionEvent.ACTION_POINTER_DOWN:
            case MotionEvent.ACTION_DOWN:

                Log.e("gggggggggg", "ggggggggggg");

                if (x > dp(100) && x < width - dp(100) && y > button_y_shift && y < button_y_shift + dp(50) && buttonVisible) {
                    isPressed = ITEM_BUTTON;
                } else {
                    isPressed = -1;
                }
                invalidate();
                break;
            case MotionEvent.ACTION_UP:

                if (x > dp(100) && x < width - dp(100) && y > button_y_shift && y < button_y_shift + dp(50) && isPressed == ITEM_BUTTON) {

                }

                isPressed = -1;
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                break;

            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_POINTER_UP:
                isPressed = -1;
                invalidate();
                break;
        }

        return false;
    }
}