package com.mahdi.car.movie.components;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.text.Layout;
import android.text.StaticLayout;

import com.mahdi.car.core.cell.CellView;
import com.mahdi.car.share.Themp;

public class WelcomeView extends CellView
{
    private Drawable[] drawables = new Drawable[3];

    private StaticLayout staticLayout;
    private StaticLayout usernameLayout;
    private StaticLayout nameLayout;

    private String full_name;
    private String username;
    private int size;

    public WelcomeView(Context context)
    {
        super(context);
        avatarSize = dp(30);
        size = (width - dp(135)) / 3;
        avatarSize = dp(85);
        init();
    }

    public void init()
    {
        this.username = "Username";
        this.full_name = "FullName";

        setGallery(drawables, 0, size * 2, size * 2, 1, "jxwlSDWRGanAGzNDCvUNjFIsLeyqxc-NoFDGYUvZlKdJanisgEGdtXnYptzIS.jpg");
        setGallery(drawables, 1, size * 2, size * 2, 1, "KQaz5VuipJpJ8irdPtr8eacEq04mSG-vdfqWmrUnlCFgAIPUhZNITyGpALvXV.jpg");
        setGallery(drawables, 2, size * 2, size * 2, 1, "kVqUw8in9ih4p9UjriXqKTf1xwBP8I-aOKFNSxlrZjRgFssmXGIJuyUzUUXnG.jpg");
    }

    @Override
    protected void dispatchDraw(Canvas canvas)
    {
        super.dispatchDraw(canvas);

        centerX = getWidth() / 2;

        canvas.save();
        canvas.translate(0, dp(50));
        Themp.gradientDrawable.setBounds(0, 0, getWidth(), getHeight());
        Themp.gradientDrawable.draw(canvas);
        canvas.restore();

        canvas.save();
        canvas.translate(0, dp(150));

        drawRoundRect(dp(10), 0, new RectF(0, 0, getWidth() - dp(20), dp(345)), dp(20), Themp.PAINT_WHITE);

        drawAvatar(centerX - (avatarSize / 2), dp(18));

        usernameLayout = new StaticLayout(username, Themp.TEXT_PAINT_FILL_AND_STROKE_2_BLACK[4], getWidth(), Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
        drawTextLayout(usernameLayout, 0, dp(110));

        nameLayout = new StaticLayout(full_name, Themp.TEXT_PAINT_FILL_AND_STROKE_2_GREY[4], getWidth(), Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
        drawTextLayout(nameLayout, 0, dp(130));

        canvas.drawBitmap(Themp.bitmapClose24, getWidth() - dp(44), dp(10), Themp.ICON_PAINT_MULTIPLY_GREY);//close button

        for (int x = 0; x < 3; x++) {
            if (drawables[x] != null) {
                canvas.save();
                canvas.translate(dp(16) + x * size, dp(165));
                drawables[x].setBounds(0, 0, size - dp(1), size);
                drawables[x].draw(canvas);
                canvas.restore();
            }
        }

        drawTextLayout(staticLayout, 0, dp(261));

        drawRoundRect(centerX - dp(35), dp(295), new RectF(0, 0, dp(70), dp(30)), dp(5), Themp.PAINT_BLUE);
        staticLayout = new StaticLayout("Follow", Themp.TEXT_PAINT_FILL_AND_STROKE_3_WHITE[4], getWidth(), Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
        drawTextLayout(staticLayout, 0, dp(287 + 15));

        canvas.restore();
    }
}