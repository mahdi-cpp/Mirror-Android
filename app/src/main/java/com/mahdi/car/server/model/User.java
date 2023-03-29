package com.mahdi.car.server.model;

import java.io.Serializable;
import java.util.Date;

public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    public int ID;
    public String Username;
    public String Phone;
    public String FullName;
    public String Category;
    public String Avatar;
    public String profile_pic_url_hd;
    public String Biography;
    public String Website;

    public Date created;
    public Date last_seen;
    public boolean is_verified;
    public boolean followed_by_viewer;

    public int getID() {
        return ID;
    }
}
