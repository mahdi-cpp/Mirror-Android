package com.mahdi.car.music;


import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.mahdi.car.core.BaseFragment;
import com.mahdi.car.core.RootView;
import com.mahdi.car.music.cell.CollectionCell;
import com.mahdi.car.server.https.Server;
import com.mahdi.car.server.model.BookmarkCollection;
import com.mahdi.car.server.model.Media;
import com.mahdi.car.server.model.Post;
import com.mahdi.car.service.ServiceManager;
import com.mahdi.car.share.CustomGridLayoutManager;

import java.util.ArrayList;
import java.util.List;

public class MusicFragment extends BaseFragment {

    private Adapter adapter;

    private List<BookmarkCollection> collections = new ArrayList<>();
    private List<Post> allPosts = new ArrayList<>();


    @SuppressLint("HardwareIds")
    @Override
    public View createView(Context context) {
        loadingShow = false;
        config.hasServerData = false;

        super.adapter = adapter = new Adapter(context);
        super.linearLayoutManager = new CustomGridLayoutManager(context, 2);
        super.createView(context);


        toolbar.setName("Musics Soon");
        toolbar.setMusic();

        Media[] mediaAlbums1 = new Media[4];
        Media[] mediaAlbums2 = new Media[4];
        Media[] mediaAlbums3 = new Media[4];
        Media[] mediaAlbums4 = new Media[4];

        for (int i = 0; i < 4; i++) {
            mediaAlbums1[i] = new Media();
            mediaAlbums2[i] = new Media();
            mediaAlbums3[i] = new Media();
            mediaAlbums4[i] = new Media();
        }


        Post post = new Post();

        mediaAlbums1[0].Thumbnail = "album_1.jpeg";
        mediaAlbums1[1].Thumbnail = "album_2.jpeg";
        mediaAlbums1[2].Thumbnail = "album_3.jpeg";
        mediaAlbums1[3].Thumbnail = "album_4.jpeg";
        post.Medias.add(mediaAlbums1[0]);
        post.Medias.add(mediaAlbums1[1]);
        post.Medias.add(mediaAlbums1[2]);
        post.Medias.add(mediaAlbums1[3]);
        allPosts.add(post);

        mediaAlbums2[0].Thumbnail = "track_1.jpg";
        mediaAlbums2[1].Thumbnail = "track_2.jpg";
        mediaAlbums2[2].Thumbnail = "track_3.jpg";
        mediaAlbums2[3].Thumbnail = "track_4.jpg";
        post = new Post();
        post.Medias.add(mediaAlbums2[0]);
        post.Medias.add(mediaAlbums2[1]);
        post.Medias.add(mediaAlbums2[2]);
        post.Medias.add(mediaAlbums2[3]);
        allPosts.add(post);

        mediaAlbums3[0].Thumbnail = "like_1.jpeg";
        mediaAlbums3[1].Thumbnail = "like_2.jpeg";
        mediaAlbums3[2].Thumbnail = "like_3.jpeg";
        mediaAlbums3[3].Thumbnail = "like_4.jpeg";
        post = new Post();
        post.Medias.add(mediaAlbums3[0]);
        post.Medias.add(mediaAlbums3[1]);
        post.Medias.add(mediaAlbums3[2]);
        post.Medias.add(mediaAlbums3[3]);
        allPosts.add(post);

        mediaAlbums4[0].Thumbnail = "podcast_1.jpeg";
        mediaAlbums4[1].Thumbnail = "podcast_2.jpeg";
        mediaAlbums4[2].Thumbnail = "podcast_3.jpeg";
        mediaAlbums4[3].Thumbnail = "podcast_4.jpeg";
        post = new Post();
        post.Medias.add(mediaAlbums4[0]);
        post.Medias.add(mediaAlbums4[1]);
        post.Medias.add(mediaAlbums4[2]);
        post.Medias.add(mediaAlbums4[3]);
        allPosts.add(post);


        return contentView;
    }

    @Override
    public void serverSend() {
        super.serverSend();
        server(Server.bookmark.getAllCollections());
    }

    private class Adapter extends RecyclerView.Adapter {
        Context context;

        public Adapter(Context context) {
            this.context = context;
        }

        @Override
        public int getItemCount() {
            return collections.size() + 4;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = new CollectionCell(context);
            return new Holder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

            if (holder.itemView instanceof CollectionCell) {

                CollectionCell cell = (CollectionCell) holder.itemView;
                String name;

                if (position == 0) {
                    name = "New Albums";
                    cell.setPost(allPosts.get(position), name, position);

                } else if (position == 1) {
                    name = "New Tracks";
                    cell.setPost(allPosts.get(position), name, position);
                } else if (position == 2) {
                    name = "Likes";
                    cell.setPost(allPosts.get(position), name, position);

                } else if (position == 3) {
                    name = "Podcasts";
                    cell.setPost(allPosts.get(position), name, position);
                } else {
                    BookmarkCollection collection = collections.get(position - 2);
                    cell.setCollection(collection, position);
                    name = collection.name;
                }

//                cell.setDelegate(() -> presentFragment(new BookmarkFragment(name)));
            }
        }

    }

    @Override
    public void onWebSocketOpened() {
        super.onWebSocketOpened();
    }

    @Override
    public void onWebSocketClosed() {
        super.onWebSocketClosed();
        RootView.instance().floatViewParent.hide();
    }

    @Override
    public void onResume() {
        super.onResume();

        if (ServiceManager.instance().webSocketIsOpen()) {
        } else {
            RootView.instance().floatViewParent.hide();
        }
    }
}




