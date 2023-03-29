package com.mahdi.car.movie.cell;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.text.Layout;
import android.text.StaticLayout;
import android.view.Gravity;

import com.mahdi.car.core.cell.CellView;
import com.mahdi.car.messenger.AndroidUtilities;
import com.mahdi.car.server.model.BookmarkCollection;
import com.mahdi.car.server.model.Post;
import com.mahdi.car.share.Themp;
import com.mahdi.car.share.component.ui.LayoutHelper;

import java.util.ArrayList;
import java.util.List;

public class CollectionCell extends CellView {
    private StaticLayout nameLayout;

    private Drawable[] drawables = new Drawable[4];
    private String[] photos = new String[4];
    private List<Post> allPosts;
    private int space = dp(16);
    private int space2 = dp(1.2f);
    private int photoSize = (width / 2) - (space + (space / 2));

    private int widthSize = photoSize / 2;
    private int heightSize = (int) (widthSize * 1.3);

    public CollectionCell(Context context) {
        super(context);
        setLayoutParams(LayoutHelper.createFrame(px(width) / 2, (int) ((px(width) / 2 + 28) * 1.2), Gravity.TOP));

        round = dp(18);
        cellWidth = (width - (space * 3)) / 2;
        space = dp(16);
    }

    public void setAllPosts(List<Post> allPosts, String name, int position) {
        this.allPosts = new ArrayList<>(allPosts);
        this.position = position;

        for (int i = 0; i < 4; i++) {
            drawables[i] = null;
            photos[i] = null;
        }

        for (int i = 0; i < allPosts.size(); i++) {
            photos[i] = allPosts.get(i).Medias.get(0).Thumbnail;
        }

        photos[0] = "mov_15208_24787-m.jpg";
        photos[1] = "mov_94071_35736-m.jpg";
        photos[2] = "mov_16669_1-m.jpg";
        photos[3] = "mov_24841_1-m.jpg";

        if (photos[0] != null) {
            setGallery(drawables, 0, photos[0]);
        }

        if (photos[1] != null) {
            setGallery(drawables, 1, photos[1]);
        }

        if (photos[2] != null) {
            setGallery(drawables, 2, photos[2]);
        }

        if (photos[3] != null) {
            setGallery(drawables, 3, photos[3]);
        }

        nameLayout = new StaticLayout(name, Themp.TEXT_PAINT_FILL_AND_STROKE_3_BLACK[6], photoSize, Layout.Alignment.ALIGN_NORMAL, 1.2f, 0.2f, false);

        invalidate();
    }

    public void setCollection(BookmarkCollection collection, int position) {
        if (collection == null) {
            return;
        }

        allPosts = null;
        this.position = position;

        drawables[0] = null;
        String name = collection.name;

        if (name == null) {
            name = "";
        }

        if (name.length() > 20) {
            name = name.substring(0, 20) + " ...";
        }
        setGallery(drawables, 0, collection.photo);

        nameLayout = new StaticLayout(name, Themp.TEXT_PAINT_FILL_BLACK[4], photoSize, Layout.Alignment.ALIGN_NORMAL, 1.2f, 0.2f, false);

        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int topSpace = dp(14);
        space = AndroidUtilities.isOdd(position) ? dp(8) : dp(16);

        canvas.save();
        canvas.translate(space, topSpace);

        if (allPosts != null) {

            if (drawables[0] != null) {
                drawables[0].setBounds(0, 0, widthSize - space2, heightSize - space2);
                drawables[0].draw(canvas);
            }

            if (drawables[1] != null) {
                canvas.save();
                canvas.translate(widthSize, 0);
                drawables[1].setBounds(0, 0, widthSize, heightSize - space2);
                drawables[1].draw(canvas);
                canvas.restore();
            }

            if (drawables[2] != null) {
                canvas.save();
                canvas.translate(0, heightSize);
                drawables[2].setBounds(0, 0, widthSize - space2, heightSize);
                drawables[2].draw(canvas);
                canvas.restore();
            }

            if (drawables[3] != null) {
                canvas.save();
                canvas.translate(widthSize, heightSize);
                drawables[3].setBounds(0, 0, widthSize, heightSize);
                drawables[3].draw(canvas);
                canvas.restore();
            }

        } else if (drawables[0] != null) {
            drawables[0].setBounds(0, 0, photoSize, photoSize);
            drawables[0].draw(canvas);
            canvas.drawRoundRect(new RectF(0, 0, photoSize, photoSize), round, round, Themp.STROKE_PAINT_PX_FFDDDDDD);
        }

        int c = dp(0);
        canvas.drawRoundRect(new RectF(-c, -c, photoSize + c, heightSize + heightSize), round, round, Themp.STROKE_PAINT_18DP_WHITE);

        if (isPressed == 1) {
            canvas.drawRoundRect(new RectF(0, 0, photoSize, heightSize + heightSize - c), round, round, Themp.PAINT_OPACITY_66000000);
        }

        canvas.drawRoundRect(new RectF(0, 0, photoSize, heightSize + heightSize), round , round, Themp.STROKE_PAINT_PX_FFDDDDDD);
        canvas.restore();

        drawTextLayout(nameLayout, space + dp(8), heightSize + heightSize + dp(18));
    }
}