package com.mahdi.car.server.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Post
{
    public long postid;
    public Date CreatedAt;
    public String Location;
    public String title;
    public String Caption;
    public long Likes;
    public boolean isLike;
    public boolean isBookmark;
    public boolean Igtv;
    public int type;
    public List<Media> Medias = new ArrayList<>();
    public User User;
}

