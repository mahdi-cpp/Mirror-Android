package com.mahdi.car.server.model;

import java.util.ArrayList;
import java.util.List;

public class Media
{

    public long mediaid;
    public long postid;
    public String Thumbnail;
    public String Photo;
    public String Video;
    public int VideoDuration;
    public int Width;
    public int Height;

    public List<Tag> Tags = new ArrayList<>();
}
