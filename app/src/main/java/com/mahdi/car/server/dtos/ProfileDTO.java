package com.mahdi.car.server.dtos;


import com.mahdi.car.server.model.Post;
import com.mahdi.car.server.model.User;

import java.util.ArrayList;
import java.util.List;

public class ProfileDTO
{
    public User user = new User();
    public List<Post> posts = new ArrayList<>();
    public long postsCount;
    public long followersCount;
    public long followingCount;
}
