package com.mahdi.car.server.dtos;

import java.util.ArrayList;
import java.util.List;

import com.mahdi.car.server.model.Post;
import com.mahdi.car.server.model.story.Story;

public class StoryDTO
{
    public Post post;
    public List<Story> stories = new ArrayList<>();
}
