package com.mahdi.car.server.model;

import java.util.Date;


public class Comment
{
    public long commentid;
    public long postid;
    public String Message;
    public Date CreatedAt;
    public long likes;
    public boolean isLike;

    public Comment reply;
    public User User = new User();

    public boolean isReply;
}
