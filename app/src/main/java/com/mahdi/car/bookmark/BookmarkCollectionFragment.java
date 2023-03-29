package com.mahdi.car.bookmark;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.mahdi.car.bookmark.cell.CollectionCell;
import com.mahdi.car.core.BaseFragment;
import com.mahdi.car.server.dtos.BookmarkDTO;
import com.mahdi.car.server.https.Server;
import com.mahdi.car.server.model.BookmarkCollection;
import com.mahdi.car.server.model.Post;
import com.mahdi.car.share.CustomGridLayoutManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class BookmarkCollectionFragment extends BaseFragment
{
    private Adapter adapter;

    private List<BookmarkCollection> collections = new ArrayList<>();
    private List<Post> allPosts = new ArrayList<>();
    private List<Post> allShops = new ArrayList<>();

    @Override
    public View createView(Context context)
    {
        super.adapter = adapter = new Adapter(context);
        super.linearLayoutManager = new CustomGridLayoutManager(context, 2);
        super.createView(context);

        setEmpetyStateView();

        adapter.notifyDataSetChanged();

        toolbar.setName("Saved");

        //emptyStateView.setBookmark();

        return contentView;
    }

    @Override
    public void serverSend()
    {
        super.serverSend();
        server(Server.bookmark.getAllCollections());
    }

    @Override
    public <T> void serverOnResponse(Call<T> call, Response<T> response)
    {
        super.serverOnResponse(call, response);

        BookmarkDTO bookmarkDTO = (BookmarkDTO) response.body();

        if (bookmarkDTO != null) {

            collections.clear();

            allPosts = bookmarkDTO.allPosts;
            allShops = bookmarkDTO.shops;
            for (BookmarkCollection post : bookmarkDTO.collections) {
                collections.add(post);
            }

            if (collections.size() == 0) {
                //emptyStateView.setVisibility(View.VISIBLE);
            }

            adapter.notifyDataSetChanged();
        }

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
            return collections.size() + 2;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            View view = new CollectionCell(context);
            return new Holder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
        {

            if (holder.itemView instanceof CollectionCell) {

                CollectionCell cell = (CollectionCell) holder.itemView;
                String name;

                if (position == 0) {
                    name = "All Posts";
                    cell.setAllPosts(allPosts, name, position);

                } else if (position == 1) {
                    name = "Wish List";
                    cell.setAllPosts(allShops, name, position);
                } else {
                    BookmarkCollection collection = collections.get(position - 2);
                    cell.setCollection(collection, position);
                    name = collection.name;
                }

//                cell.setDelegate(() -> presentFragment(new BookmarkFragment(name)));
            }
        }

    }
}


