package com.fsd.exp5.controller;

import com.fsd.exp5.dto.ApiResponse;
import com.fsd.exp5.dto.PostRequest;
import com.fsd.exp5.dto.PostResponse;
import com.fsd.exp5.dto.SchedulePostRequest;
import com.fsd.exp5.service.PostService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@CrossOrigin(origins = "http://localhost:3000")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<PostResponse>> createPost(@Valid @RequestBody PostRequest request) {
        PostResponse createdPost = postService.createPost(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Post created successfully", createdPost));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<PostResponse>>> getAllPosts() {
        List<PostResponse> posts = postService.getAllPosts();
        return ResponseEntity.ok(ApiResponse.success("Posts retrieved successfully", posts));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PostResponse>> getPostById(@PathVariable Long id) {
        PostResponse post = postService.getPostById(id);
        return ResponseEntity.ok(ApiResponse.success("Post retrieved successfully", post));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PostResponse>> updatePost(
            @PathVariable Long id,
            @Valid @RequestBody PostRequest request) {
        PostResponse updatedPost = postService.updatePost(id, request);
        return ResponseEntity.ok(ApiResponse.success("Post updated successfully", updatedPost));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return ResponseEntity.ok(ApiResponse.success("Post deleted successfully", null));
    }

    @PostMapping("/{id}/schedule")
    public ResponseEntity<ApiResponse<PostResponse>> schedulePost(
            @PathVariable Long id,
            @Valid @RequestBody SchedulePostRequest request) {
        PostResponse scheduledPost = postService.schedulePost(id, request);
        return ResponseEntity.ok(ApiResponse.success("Post scheduled successfully", scheduledPost));
    }
}
