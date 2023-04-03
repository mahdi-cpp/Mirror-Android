package com.mahdi.car.movie;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.mahdi.car.core.BaseFragment;
import com.mahdi.car.core.RootView;
import com.mahdi.car.movie.cell.CollectionCell;
import com.mahdi.car.server.dtos.BookmarkDTO;
import com.mahdi.car.server.https.Server;
import com.mahdi.car.server.model.BookmarkCollection;
import com.mahdi.car.server.model.Media;
import com.mahdi.car.server.model.Post;
import com.mahdi.car.service.ServiceManager;
import com.mahdi.car.share.CustomGridLayoutManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class MovieFragment extends BaseFragment {

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

        toolbar.setName("Movies Soon");
        toolbar.setMovie();

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

        mediaAlbums1[0].Thumbnail = "movie_1.jpg";
        mediaAlbums1[1].Thumbnail = "movie_2.jpg";
        mediaAlbums1[2].Thumbnail = "movie_3.jpg";
        mediaAlbums1[3].Thumbnail = "movie_4.jpg";
        post.Medias.add(mediaAlbums1[0]);
        post.Medias.add(mediaAlbums1[1]);
        post.Medias.add(mediaAlbums1[2]);
        post.Medias.add(mediaAlbums1[3]);
        allPosts.add(post);

        mediaAlbums2[0].Thumbnail = "series_1.jpg";
        mediaAlbums2[1].Thumbnail = "series_2.jpg";
        mediaAlbums2[2].Thumbnail = "series_3.jpg";
        mediaAlbums2[3].Thumbnail = "series_4.jpg";
        post = new Post();
        post.Medias.add(mediaAlbums2[0]);
        post.Medias.add(mediaAlbums2[1]);
        post.Medias.add(mediaAlbums2[2]);
        post.Medias.add(mediaAlbums2[3]);
        allPosts.add(post);

        mediaAlbums3[0].Thumbnail = "Theatre_1.jpg";
        mediaAlbums3[1].Thumbnail = "Theatre_2.jpg";
        mediaAlbums3[2].Thumbnail = "Theatre_3.jpg";
        mediaAlbums3[3].Thumbnail = "Theatre_4.jpg";
        post = new Post();
        post.Medias.add(mediaAlbums3[0]);
        post.Medias.add(mediaAlbums3[1]);
        post.Medias.add(mediaAlbums3[2]);
        post.Medias.add(mediaAlbums3[3]);
        allPosts.add(post);

        mediaAlbums4[0].Thumbnail = "animation_1.jpg";
        mediaAlbums4[1].Thumbnail = "animation_2.jpg";
        mediaAlbums4[2].Thumbnail = "animation_3.jpg";
        mediaAlbums4[3].Thumbnail = "animation_4.jpg";
        post = new Post();
        post.Medias.add(mediaAlbums4[0]);
        post.Medias.add(mediaAlbums4[1]);
        post.Medias.add(mediaAlbums4[2]);
        post.Medias.add(mediaAlbums4[3]);
        allPosts.add(post);

        return contentView;
    }

    private class Adapter extends RecyclerView.Adapter {
        Context context;

        public Adapter(Context context) {
            this.context = context;
        }

        @Override
        public int getItemCount() {
            return 4;
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
                    name = "Movies";
                    cell.setAllPosts(allPosts.get(position), name, position);
                } else if (position == 1) {
                    name = "Series";
                    cell.setAllPosts(allPosts.get(position), name, position);
                } else if (position == 2) {
                    name = "Theatre";
                    cell.setAllPosts(allPosts.get(position), name, position);

                } else if (position == 3) {
                    name = "Animations";
                    cell.setAllPosts(allPosts.get(position), name, position);
                }
//                }  else {
//                    BookmarkCollection collection = collections.get(position - 2);
//                    cell.setCollection(collection, position);
//                    name = collection.name;
//                }

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

