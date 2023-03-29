package com.mahdi.car.server.model.story;


import java.io.Serializable;
import java.util.Date;

public class Story implements Serializable
{

    private static final long serialVersionUID = 1L;

    public int ID;
    public int userid;
    public Date created;
    //public Media media;
    //public Quiz quiz;
    public String Photo;
    public String Video;
    public int VideoDuration;
    public String Color;
}

