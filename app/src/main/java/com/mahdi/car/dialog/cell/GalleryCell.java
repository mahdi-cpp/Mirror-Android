package com.mahdi.car.dialog.cell;


import android.content.Context;
import android.graphics.Canvas;
import android.view.Gravity;
import android.view.MotionEvent;

import java.io.File;

import com.mahdi.car.core.cell.CellView;
import com.mahdi.car.messenger.MediaController;
import com.mahdi.car.messenger.AndroidUtilities;
import com.mahdi.car.share.component.ui.LayoutHelper;

public class GalleryCell extends CellView
{
    private Delegate delegate;

    private MediaController.AlbumEntry albumEntry;
    //private ImageView imageView;
    //private TextView nameTextView;

    public GalleryCell(Context context)
    {
        super(context);

        int width = AndroidUtilities.width / 4;

        width = AndroidUtilities.pxToDp(width);

        setLayoutParams(LayoutHelper.createFrame(width, width + 8, Gravity.LEFT | Gravity.TOP, 0, 0, 0, 0));

        int imageSize = (int) (width / 1.5f);

        //        imageView = new ImageView(context);
        //        addView(imageView, LayoutHelper.createFrame(imageSize, imageSize, Gravity.CENTER_HORIZONTAL | Gravity.TOP, 20, 4, 20, 0));

        //        nameTextView = new TextView(context);
        //        nameTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);
        //        nameTextView.setTextColor(0xff000000);
        //        nameTextView.setSingleLine(true);
        //        nameTextView.setEllipsize(TextUtils.TruncateAt.END);
        //        nameTextView.setMaxLines(1);
        //        nameTextView.setGravity(Gravity.CENTER_HORIZONTAL);
        //        addView(nameTextView, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT, Gravity.BOTTOM, 0, 0, 0, 12));

    }

    public void setAlbum(MediaController.AlbumEntry albumEntry)
    {
        this.albumEntry = albumEntry;

        if (albumEntry != null) {

            /*
            imageView.setOrientation(0, true);
            if (albumEntry.coverPhoto != null && albumEntry.coverPhoto.path != null) {
                imageView.setOrientation(albumEntry.coverPhoto.orientation, true);
                if (albumEntry.coverPhoto.isVideo) {
                    imageView.setImage("vthumb://" + albumEntry.coverPhoto.imageId + ":" + albumEntry.coverPhoto.path, null, null);
                } else {
                    imageView.setImage("thumb://" + albumEntry.coverPhoto.imageId + ":" + albumEntry.coverPhoto.path, null, null);
                }
            } else {
                //albumView.imageView.setImageResource(R.drawable.nophotos);
            }
            */

            //.with(this).load(new File(albumEntry.coverPhoto.path)).apply(new RequestOptions().override(200, 200).transforms(new CenterCrop(), new RoundedCorners(dp(200)))).into(imageView);

            setMedia(new File(albumEntry.coverPhoto.path), 200, 200, dp(200));

            //nameTextView.setText(albumEntry.bucketName);
            //countTextView.setText(String.format("%d", albumEntry.photos.size()));

        } else {
            setVisibility(INVISIBLE);
        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas)
    {
        super.dispatchDraw(canvas);
        //drawMedia(0, 0, getWidth(), getHeight());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                if (x > 0 && x < getWidth() && y > 0 && y < getHeight()) {
                    isPressed = 1;
                    invalidate();
                }

                break;

            case MotionEvent.ACTION_MOVE:
                break;

            case MotionEvent.ACTION_UP:

                if (x > 0 && x < getWidth() && y > 0 && y < getHeight() && isPressed == 1) {
                    delegate.click();
                }

                isPressed = -1;
                invalidate();
                break;

        }
        return true;
    }

    public interface Delegate
    {
        void click();
    }

    public void setDelegate(Delegate delegate)
    {
        this.delegate = delegate;
    }

}
