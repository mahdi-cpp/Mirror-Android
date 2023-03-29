package com.mahdi.car.server.model.story;


import java.io.Serializable;


public class Poll implements Serializable
{

    private static final long serialVersionUID = 1L;

    public long quizid;
    public long storyid;
    public String question;
    public String left;
    public String right;
    public int color;
    public int x;
    public int y;
    public int rotate;
    public double zoom;
}

