package com.mahdi.car.server.model;


import java.util.Date;

public class Notification
{

    public Long notificationid;
    public long postid;
    public int type;
    public String thumbnail;
    public Date created;
    public boolean followed_by_viewer;
    public User owner;
    public Comment comment;
}