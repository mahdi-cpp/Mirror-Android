package com.mahdi.car.server.dtos;

import com.mahdi.car.server.model.Post;
import com.mahdi.car.server.model.story.Story;

import java.util.ArrayList;
import java.util.List;

public class StoryDTO
{
    public Post post;
    public List<Story> stories = new ArrayList<>();
}
