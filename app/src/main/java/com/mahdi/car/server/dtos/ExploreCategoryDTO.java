package com.mahdi.car.server.dtos;


import com.mahdi.car.server.model.Category;
import com.mahdi.car.server.model.Post;

import java.util.ArrayList;
import java.util.List;

public class ExploreCategoryDTO
{
    public Category category;
    public List<Post> posts = new ArrayList<>();
}
