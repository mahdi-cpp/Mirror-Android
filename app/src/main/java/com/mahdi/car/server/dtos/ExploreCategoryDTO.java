package com.mahdi.car.server.dtos;


import java.util.ArrayList;
import java.util.List;

import com.mahdi.car.server.model.Category;
import com.mahdi.car.server.model.Post;

public class ExploreCategoryDTO
{
    public Category category;
    public List<Post> posts = new ArrayList<>();
}
