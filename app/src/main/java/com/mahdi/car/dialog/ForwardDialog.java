package com.mahdi.car.dialog;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.mahdi.car.App;
import com.mahdi.car.core.cell.UserCell;
import com.mahdi.car.dialog.parent.BaseDialog;
import com.mahdi.car.share.CustomGridLayoutManager;
import com.mahdi.car.share.component.ui.LayoutHelper;
import com.mahdi.car.dialog.component.ForwardHeaderView;
import com.mahdi.car.server.https.Server;
import com.mahdi.car.server.model.Post;
import com.mahdi.car.server.model.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForwardDialog extends BaseDialog
{
    private int rowCount = 0;
    private final int edgeRow = rowCount++;
    private final int empetyStateViewRow = rowCount++;
    private final int empetyStateViewRow2 = rowCount++;
    private final int headerViewRow = rowCount++;
    private final int headerSpaceViewRow = rowCount++;

    private Delegate delegate;

    private ForwardHeaderView forwardHeaderView;

    private Adapter adapter;

    private List<User> users = new ArrayList<>();
    private List<User> searchUsers = new ArrayList<>();
    private HashMap<Integer, Integer> sends = new HashMap<>();

    private boolean permissionLoading = true;
    private boolean isSearch = false;

    private int page = 0;
    private int size = 20;
    private boolean has_next_page = true;

    private Post post;

    public ForwardDialog(Context context, FrameLayout parent)
    {
        super(context);

        adapter = new Adapter(context);
        init(adapter, rowCount, parent);

        layoutManager = new CustomGridLayoutManager(context, 1);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setPadding(0, 0, 0, 0);

        forwardHeaderView = new ForwardHeaderView(context);
        forwardHeaderView.setVisibility(VISIBLE);
        forwardHeaderView.setDelegate(new ForwardHeaderView.Delegate()
        {
            @Override
            public void afterTextChanged(String text)
            {

                if (text.length() > 0) {

                    isSearch = true;

                    Call<List<User>> call = Server.user.getSearch(text);
                    call.enqueue(new Callback<List<User>>()
                    {
                        @Override
                        public void onResponse(Call<List<User>> call, Response<List<User>> response)
                        {
                            if (response.isSuccessful()) {

                                List<User> newUsers = response.body();
                                if (newUsers != null) {
                                    searchUsers = newUsers;
                                    //count = search_users.size();
                                    //recyclerView.setVisibility(View.VISIBLE);
                                    //recyclerView.setAdapter(searchAdapter);
                                    permissionLoading = true;
                                    adapter.notifyDataSetChanged();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<List<User>> call, Throwable t)
                        {

                        }
                    });

                } else {
                    isSearch = false;
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void clear()
            {
                //                recyclerView.setAdapter(adapter);
                //                setSmoothScroller(3);

                isSearch = false;
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFocusChange(boolean hasFocus)
            {

                //Log.e("hasFocus", "hasFocus:  " + hasFocus);
                //isKeyBoard = hasFocus;
                //updateLayout();

                if (hasFocus)
                    setSmoothScroller(3);

                //layoutManager.scrollToPositionWithOffset(0, 1000);
            }

            @Override
            public void cancel()
            {
                //setSmoothScroller(3);
            }

        });

//        blurringView = null;

        addHeader(forwardHeaderView);

        adapter.notifyDataSetChanged();
    }

    @Override
    protected void cancel()
    {

    }

    public void refresh(boolean progress, Post post)
    {
        this.post = post;
        forwardHeaderView.setPhoto(post.Medias.get(0).Thumbnail);

        if (permissionLoading == false || isSearch) {
            return;
        }
        permissionLoading = false;

        if (progress) {
            //loadingView.showProgress();
        }

        Call<List<User>> call = Server.user.getFollowings(App.userid, page * size, size);

        if (call != null) {
            call.enqueue(new Callback<List<User>>()
            {
                @Override
                public void onResponse(Call<List<User>> call, Response<List<User>> response)
                {
                    if (response.isSuccessful()) {

                        List<User> newUsers = response.body();

                        if (newUsers != null) {

                            for (int a = 0; a < newUsers.size(); a++) {
                                User f = newUsers.get(a);
                                users.add(f);
                            }

                            if (newUsers.size() != size) {
                                has_next_page = false;
                            }
                            page++;

                            adapter.notifyDataSetChanged();

                            permissionLoading = true;

                        }

                    } else {
                        permissionLoading = true;
                    }
                }

                @Override
                public void onFailure(Call<List<User>> call, Throwable t)
                {
                    permissionLoading = true;
                }
            });
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
                view = new HeaderSpaceView(context);
            } else if (viewType == rowCount + 1) {
                view = new UserCell(context);
            } else {
                view = new EmptyProgressView(context);
            }

            return new Holder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
        {
            if (holder.itemView instanceof UserCell) {

//                UserCell cell = (UserCell) holder.itemView;
//                int index = position - rowCount;
//                User user = isSearch ? searchUsers.get(index) : users.get(index);
//                if (cell != null && user != null) {
//
//                    ///cell.setUser(user);
//
//                    if (sends.get(user.ID) != null) {
//                        //cell.setSended(true);
//                    } else {
//                        //cell.setSended(false);
//                    }
//                }

            } else if (holder.itemView instanceof EmptyProgressView) {

                EmptyProgressView emptyProgressView = (EmptyProgressView) holder.itemView;

                int size = isSearch ? searchUsers.size() : users.size();
                size = size == 0 ? 1 : size;
                emptyProgressView.setLayoutParams(LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, (20 / size) * 60));

            }
        }

        @Override
        public int getItemCount()
        {
            return (isSearch ? searchUsers.size() : users.size()) + rowCount + 1;
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
            else if (position < (isSearch ? searchUsers.size() : users.size()) + rowCount) {
                return rowCount + 1;
            } else {
                return rowCount + 2;
            }
        }
    }

    private void forward(int owner_userid, long postid)
    {

        Call<Integer> call = Server.post.setForward(owner_userid, postid, forwardHeaderView.getMessage());
        call.enqueue(new Callback<Integer>()
        {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response)
            {

                if (response.isSuccessful()) {
                    sends.put(owner_userid, 1);


                } else {
                    int code = 0;
                    switch (code) {
                        case 404:
                            Toast.makeText(App.context, "not found", Toast.LENGTH_SHORT).show();
                            break;
                        case 429:
                            Toast.makeText(App.context, "شما در هر دقیقه 10 فورواد می توانید داشته باشید.", Toast.LENGTH_SHORT).show();
                            break;
                        case 500:
                            Toast.makeText(App.context, "server broken", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(App.context, "unknown error: " + code, Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t)
            {

            }
        });
    }

    public interface Delegate
    {
        void onClick();
    }

    public void setDelegate(Delegate delegate)
    {
        this.delegate = delegate;
    }

}
