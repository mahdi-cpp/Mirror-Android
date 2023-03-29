package com.mahdi.car.server.model.story;


import java.io.Serializable;


public class Quiz implements Serializable {

    private static final long serialVersionUID = 1L;

    public long quizid;
    public long storyid;
    public String question;
    public int color;
    public int x;
    public int y;
    public int rotate;
    public double zoom;
}

