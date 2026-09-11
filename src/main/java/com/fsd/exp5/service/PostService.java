package com.fsd.exp5.service;

import com.fsd.exp5.dto.PostRequest;
import com.fsd.exp5.dto.PostResponse;
import com.fsd.exp5.dto.SchedulePostRequest;

import java.util.List;

public interface PostService {
    PostResponse createPost(PostRequest request);
    List<PostResponse> getAllPosts();
    PostResponse getPostById(Long id);
    PostResponse updatePost(Long id, PostRequest request);
    void deletePost(Long id);
    PostResponse schedulePost(Long id, SchedulePostRequest request);
}
