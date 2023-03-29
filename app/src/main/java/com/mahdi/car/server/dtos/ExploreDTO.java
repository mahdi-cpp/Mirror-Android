package com.mahdi.car.server.dtos;

import java.util.ArrayList;
import java.util.List;

import com.mahdi.car.server.model.Category;
import com.mahdi.car.server.model.Post;

public class ExploreDTO
{
    public List<Post> posts = new ArrayList<>();
    public List<Category> categories;
    //    public Header header = new Header();
    //    public List<ExploreCategoryDTO> categories = new ArrayList<>();
    //
    //    public class Header
    //    {
    //        public Post post = new Post();
    //        public HashMap<Integer, Long> postids = new HashMap<>();
    //    }
}
