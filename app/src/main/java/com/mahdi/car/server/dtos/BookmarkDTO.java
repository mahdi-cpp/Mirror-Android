package com.mahdi.car.server.dtos;



import java.util.ArrayList;
import java.util.List;

import com.mahdi.car.server.model.BookmarkCollection;
import com.mahdi.car.server.model.Post;

public class BookmarkDTO {

    public List<BookmarkCollection> collections = new ArrayList<>();
    public List<Post> allPosts = new ArrayList<>();
    public List<Post> shops = new ArrayList<>();
}
