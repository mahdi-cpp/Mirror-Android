package com.mahdi.car.setting;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import com.mahdi.car.core.BaseFragment;
import com.mahdi.car.setting.cell.ChatCell;
import com.mahdi.car.setting.component.ChatAddView;
import com.mahdi.car.server.dtos.ProfileDTO;
import com.mahdi.car.server.https.Server;
import com.mahdi.car.server.model.Post;
import com.mahdi.car.server.model.User;
import com.mahdi.car.share.CustomLinearLayoutManager;
import com.mahdi.car.share.component.ui.LayoutHelper;
import retrofit2.Call;
import retrofit2.Response;

public class ChatFragment extends BaseFragment
{
    private ChatAddView chatAddView;

    private List<Post> posts = new ArrayList<>();
    private User user;

    public ChatFragment(User user)
    {
        this.user = user;
    }

    @Override
    public View createView(Context context)
    {
        thisFragment = fragment.chat;

        adapter = new Adapter(context);
        super.linearLayoutManager = new CustomLinearLayoutManager(context);
        super.createView(context);

        toolbar.settChat(user.Avatar, user.Username, user.FullName);

        chatAddView = new ChatAddView(context);
        contentView.addView(chatAddView, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, 68, Gravity.BOTTOM));

        parentView.removeView(contentView);
        parentView.addView(contentView, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT));

        return contentView;
    }

    @Override
    public void serverSend()
    {
        super.serverSend();
        server(Server.post.getProfile("maede_mohamadi72", page * size, 200));
    }

    @Override
    public <T> void serverOnResponse(Call<T> call, Response<T> response)
    {
        super.serverOnResponse(call, response);

        if (response.isSuccessful()) {
            ProfileDTO profile = (ProfileDTO) response.body();

            if (profile != null) {

                for (Post post : profile.posts) {
                    if (post.Medias.size() > 0) {
                        //if (post.medias.get(0).video != null && post.medias.size() > 2) {
                        posts.add(post);
                        //}
                    }
                }

                if (profile.posts.size() != size) {
                    has_next_page = false;
                }
                page++;

                adapter.notifyDataSetChanged();
            }
        }
    }

    private class Adapter extends RecyclerView.Adapter
    {
        Context context;

        private Adapter(Context context)
        {
            this.context = context;
        }

        @Override
        public int getItemCount()
        {
            return posts.size();
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            View view;
            view = new ChatCell(parent.getContext());
            return new Holder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
        {
            if (holder.itemView instanceof ChatCell) {

                ChatCell cell = (ChatCell) holder.itemView;
                Post post = posts.get(position);

                if (cell == null || post == null) {
                    return;
                }

                cell.setPost(user.Avatar, post, position);
                cell.setDelegate(post1 -> {
                    if (post.Igtv) {
                        List<Post> igtvs = new ArrayList<>();
                        igtvs.add(post);
//                        presentFragment(new PlayerIGTVFragment(igtvs));
                    } else {
                        //List<Post> tt = new ArrayList<>();
                        //tt.add(post);
                        //presentFragment(new ExploreListFragment("Post", 0, tt));
                    }
                });
            }
        }
    }

}


