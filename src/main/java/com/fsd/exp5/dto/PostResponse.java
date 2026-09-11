package com.fsd.exp5.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fsd.exp5.model.Post;

import java.time.LocalDateTime;

public class PostResponse {
    private Long id;
    private String content;
    private String text;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime scheduledAt;

    private boolean scheduled;

    public PostResponse() {
    }

    public PostResponse(Long id, String content, String text, LocalDateTime createdAt, LocalDateTime scheduledAt, boolean scheduled) {
        this.id = id;
        this.content = content;
        this.text = text;
        this.createdAt = createdAt;
        this.scheduledAt = scheduledAt;
        this.scheduled = scheduled;
    }

    public static PostResponse fromEntity(Post post) {
        if (post == null) {
            return null;
        }
        return new PostResponse(
                post.getId(),
                post.getContent(),
                post.getText(),
                post.getCreatedAt(),
                post.getScheduledAt(),
                post.isScheduled()
        );
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getScheduledAt() {
        return scheduledAt;
    }

    public void setScheduledAt(LocalDateTime scheduledAt) {
        this.scheduledAt = scheduledAt;
    }

    public boolean isScheduled() {
        return scheduled;
    }

    public void setScheduled(boolean scheduled) {
        this.scheduled = scheduled;
    }
}
