package com.mahdi.car.server.dtos;

import com.mahdi.car.server.model.Post;
import com.mahdi.car.server.model.User;

import java.util.ArrayList;
import java.util.List;

public class FeedDTO
{
    //public User user = new User();
    public List<User> stories = new ArrayList<>();
//    public List<Category> categories = new ArrayList<>();
    public List<Post> posts = new ArrayList<>();
//    public long cartCount;
//    public NotificationDTO notificationDTO = new NotificationDTO();
//
//    public class NotificationDTO
//    {
//        public long likes;
//        public long comments;
//        public long followers;
//        public long forwards;
//    }
}
