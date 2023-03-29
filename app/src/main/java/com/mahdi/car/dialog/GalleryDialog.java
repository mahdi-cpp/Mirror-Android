package com.mahdi.car.dialog;

import android.content.Context;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.util.ArrayList;

import com.mahdi.car.dialog.cell.GalleryCell;
import com.mahdi.car.dialog.component.SimpleHeaderView;
import com.mahdi.car.dialog.parent.BaseDialog;
import com.mahdi.car.share.CustomGridLayoutManager;
import com.mahdi.car.share.component.ui.LayoutHelper;
import com.mahdi.car.messenger.MediaController;
import com.mahdi.car.messenger.NotificationCenter;

public class GalleryDialog extends BaseDialog implements NotificationCenter.NotificationCenterDelegate
{
    private int rowCount = 0;
    private final int edgeRow = rowCount++;
    private final int empetyStateViewRow = rowCount++;
    private final int empetyStateViewRow2 = rowCount++;
    private final int headerViewRow = rowCount++;
    private final int headerSpaceViewRow = rowCount++;

    private Delegate delegate;
    private SimpleHeaderView simpleHeaderView;
    private RecyclerView.Adapter adapter;

    private ArrayList<MediaController.AlbumEntry> albums = new ArrayList<>();


    public GalleryDialog(Context context, FrameLayout parent, int type)
    {
        super(context);

        adapter = new Adapter(context);
        init(adapter, rowCount, parent);

        layoutManager = new CustomGridLayoutManager(context, 4);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup()
        {
            @Override
            public int getSpanSize(int position)
            {
                //define span size for this position
                //some example for your first three items
                if (position < rowCount) {
                    return 4; //total
                } else {
                    return 1; //item will take 1/4 space of row
                }
            }
        });
        recyclerView.setLayoutManager(layoutManager);

//        blurringView = null;

        simpleHeaderView = new SimpleHeaderView(context);
        addHeader(simpleHeaderView);

        headerSpaceView.setLayoutParams(LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, 80));

        NotificationCenter.getInstance().addObserver(this, NotificationCenter.albumsDidLoaded);
        MediaController.loadGalleryPhotosAlbums(type);

    }

    @Override
    protected void cancel()
    {

    }

    @Override
    public void didReceivedNotification(int id, Object... args)
    {
        if (id == NotificationCenter.albumsDidLoaded) {

            albums = (ArrayList<MediaController.AlbumEntry>) args[1];


            if (recyclerView != null) {
                //listView.setEmptyView(emptyView);
            }
            if (adapter != null) {
                adapter.notifyDataSetChanged();
            }
        }
    }


    private class Adapter extends RecyclerView.Adapter
    {

        private Context context;

        public Adapter(Context context)
        {
            this.context = context;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {

            View view;
            if (viewType == edgeRow) {
                view = new EdgeView(context);
            } else if (viewType == empetyStateViewRow || viewType == empetyStateViewRow2) {
                view = new GlassView(context);
            } else if (viewType == headerViewRow) {
                view = headerEdgeView;
            } else if (viewType == headerSpaceViewRow) {
                view = headerSpaceView;
            } else if (viewType == rowCount + 1) {
                view = new GalleryCell(context);
            } else {
                view = new EmptyProgressView(context);
            }

            return new Holder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
        {
            if (holder.itemView instanceof GalleryCell) {

                GalleryCell cell = (GalleryCell) holder.itemView;
                int index = position - rowCount;
                MediaController.AlbumEntry albumEntry = albums.get(index);
                cell.setAlbum(albumEntry);
                cell.setDelegate((GalleryCell.Delegate) () -> {
                    delegate.onClick(albumEntry);
                    hide();
                });

            } else if (holder.itemView instanceof EmptyProgressView) {

                EmptyProgressView emptyProgressView = (EmptyProgressView) holder.itemView;

                int size = albums.size();
                size = size == 0 ? 1 : size;
                emptyProgressView.setLayoutParams(LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, (20 / size) * 60));
            }
        }

        @Override
        public int getItemCount()
        {
            return albums.size() + rowCount + 1;
        }

        @Override
        public int getItemViewType(int position)
        {
            if (position == edgeRow)
                return edgeRow;
            else if (position == empetyStateViewRow || position == empetyStateViewRow2)
                return empetyStateViewRow;
            else if (position == headerViewRow)
                return headerViewRow;
            else if (position == headerSpaceViewRow)
                return headerSpaceViewRow;
            else if (position < albums.size() + rowCount) {
                return rowCount + 1;
            } else {
                return rowCount + 2;
            }
        }
    }


    public interface Delegate
    {
        void onClick(MediaController.AlbumEntry album);
    }

    public void setDelegate(Delegate delegate)
    {
        this.delegate = delegate;
    }

}
