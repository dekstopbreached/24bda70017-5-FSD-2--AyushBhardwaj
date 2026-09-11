package com.fsd.exp5.service;

import com.fsd.exp5.dto.PostRequest;
import com.fsd.exp5.dto.PostResponse;
import com.fsd.exp5.dto.SchedulePostRequest;
import com.fsd.exp5.exception.ResourceNotFoundException;
import com.fsd.exp5.model.Post;
import com.fsd.exp5.repository.PostRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

    private static final Logger log = LoggerFactory.getLogger(PostServiceImpl.class);
    private final PostRepository postRepository;

    public PostServiceImpl(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    public PostResponse createPost(PostRequest request) {
        log.info("Creating new post with content length: {}", request.getContent().length());
        Post post = new Post();
        post.setContent(request.getContent());
        post.setText(request.getText());
        post.setCreatedAt(LocalDateTime.now());
        post.setScheduled(false);

        Post saved = postRepository.save(post);
        log.info("Post created successfully with id: {}", saved.getId());
        return PostResponse.fromEntity(saved);
    }

    @Override
    public List<PostResponse> getAllPosts() {
        log.info("Fetching all posts");
        return postRepository.findAll().stream()
                .map(PostResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public PostResponse getPostById(Long id) {
        log.info("Fetching post by id: {}", id);
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post", id));
        return PostResponse.fromEntity(post);
    }

    @Override
    public PostResponse updatePost(Long id, PostRequest request) {
        log.info("Updating post with id: {}", id);
        Post existing = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post", id));

        existing.setContent(request.getContent());
        existing.setText(request.getText());

        Post updated = postRepository.save(existing);
        log.info("Post with id {} updated successfully", id);
        return PostResponse.fromEntity(updated);
    }

    @Override
    public void deletePost(Long id) {
        log.info("Deleting post with id: {}", id);
        if (!postRepository.existsById(id)) {
            throw new ResourceNotFoundException("Post", id);
        }
        postRepository.deleteById(id);
        log.info("Post with id {} deleted successfully", id);
    }

    @Override
    public PostResponse schedulePost(Long id, SchedulePostRequest request) {
        log.info("Scheduling post id {} for {}", id, request.getScheduledAt());
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post", id));

        post.setScheduledAt(request.getScheduledAt());
        post.setScheduled(true);

        Post saved = postRepository.save(post);
        log.info("Post id {} successfully scheduled", id);
        return PostResponse.fromEntity(saved);
    }
}
