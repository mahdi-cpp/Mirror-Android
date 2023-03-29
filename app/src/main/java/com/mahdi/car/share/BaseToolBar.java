package com.mahdi.car.share;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;

import com.mahdi.car.core.cell.CellFrameLayout;


public class BaseToolBar extends CellFrameLayout
{
    protected View shadow;
    protected Bitmap bitmap;
    protected String name = "";
    protected int avatar_size = dp(28);

    //protected Canvas canvas;

    protected Drawable avatarDrawable;
    protected int avatarSize;

    public BaseToolBar(@NonNull Context context)
    {
        super(context);

        shadow = new View(context);
        shadow.setBackground(Themp.drawableToolbar[0]);

        setWillNotDraw(false);
    }

//
//    @Override
//    protected void onDraw(Canvas canvas)
//    {
//        super.onDraw(canvas);
//        this.canvas = canvas;
//    }
//
//    @Override
//    protected void dispatchDraw(Canvas canvas)
//    {
//        super.dispatchDraw(canvas);
//        this.canvas = canvas;
//    }
//
//    protected void drawTextLayoutPX(Layout layout, int x, int y)
//    {
//        drawTextLayout(layout, x, y);
//    }
//
//    protected void drawTextLayoutDP(Layout layout, int x, int y)
//    {
//        drawTextLayout(layout, dp(x), dp(y));
//    }
//
//    private void drawTextLayout(Layout layout, int x, int y)
//    {
//        if (layout != null) {
//            canvas.save();
//            canvas.translate(x, y);
//            layout.draw(canvas);
//            canvas.restore();
//        }
//    }
//
//    protected void drawUsername(Layout layout, int x, int y)
//    {
//        if (layout != null) {
//            canvas.save();
//            canvas.translate(x, y);
//            layout.draw(canvas);
//            if (verify) {
//                int w = (int) layout.getLineWidth(0);
//                canvas.drawBitmap(Themp.bitmapVerifiedProfile, dp(5) + w, dp(2), Themp.ICON_PAINT_SRC_IN_BLUE);
//            }
//            canvas.restore();
//        }
//    }
//
//    public void drawAvatar(int x, int y)
//    {
//        canvas.save();
//        canvas.translate(x, y);
//
//        if (avatarDrawable == null) {
//            canvas.drawBitmap(Themp.bitmapEmptyProfile, null, new Rect(0, 0, avatarSize, avatarSize), Themp.PAINT_WHITE);
//        } else {
//            avatarDrawable.setBounds(0, 0, avatarSize, avatarSize);
//            avatarDrawable.draw(canvas);
//        }
//        canvas.drawCircle(avatarSize / 2, avatarSize / 2, (avatarSize / 2), Themp.STROKE_PAINT_PX_GREY);
//
//        canvas.restore();
//    }

}
