package com.mahdi.car.setting;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.mahdi.car.WebSocket;
import com.mahdi.car.share.Themp;
import com.mahdi.car.core.BaseFragment;
import com.mahdi.car.setting.cell.SettingCell;
import com.mahdi.car.share.CustomLinearLayoutManager;

import java.net.URI;
import java.net.URISyntaxException;

public class SettingFragment extends BaseFragment
{
    private static int rowCount = 0;
    private static final int rowEmpty = rowCount++;
    private static final int rowNotification = rowCount++;
    private static final int rowAccount = rowCount++;
    private static final int rowCash = rowCount++;
    private static final int rowHelp = rowCount++;
    private static final int rowAbout = rowCount++;
    private static final int rowLogOut = rowCount++;
    private static final int rowPhone = rowCount++;

    private static final int rowVersion = rowCount++;

    @Override
    public View createView(final Context context)
    {
        super.linearLayoutManager = new CustomLinearLayoutManager(context);
        super.adapter = new Adapter(context);

        super.createView(context);
        super.contentView.setBackgroundColor(0xffffff);

        toolbar.setName("Setting");
        toolbar.setSetting();
        swipe.setScrollEnabled(false);

        return contentView;
    }

    private class Adapter extends RecyclerView.Adapter
    {
        Context context;

        public Adapter(Context context)
        {
            this.context = context;
        }

        @Override
        public int getItemCount()
        {
            return rowCount;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            return new Holder(new SettingCell(context));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
        {
            SettingCell cell = (SettingCell) holder.itemView;

            if (position == rowEmpty) {
                cell.setEmpty(30);
            } else if (position == rowNotification) {
                cell.setIcon(Themp.setting.notification, "Notification");
            } else if (position == rowAccount) {
                cell.setIcon(Themp.setting.account, "Account");
            } else if (position == rowCash) {
                cell.setIcon(Themp.setting.cash, "Cash");
            } else if (position == rowAbout) {
                cell.setIcon(Themp.setting.about, "About");
            } else if (position == rowHelp) {
                cell.setIcon(Themp.setting.help, "Help");
            } else if (position == rowPhone) {
                cell.setBlue("Phone:  09355512619");
            } else if (position == rowLogOut) {
                cell.setBlue("Log Out @" + owner().Username + " Account");
            } else if (position == rowVersion) {
                cell.setVersion("Pavarchin for Android version 1.2.3");
            }
        }

        @Override
        public int getItemViewType(int position)
        {
            return position;
        }
    }
}





