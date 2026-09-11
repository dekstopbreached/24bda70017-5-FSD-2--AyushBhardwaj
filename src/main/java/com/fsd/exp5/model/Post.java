package com.fsd.exp5.model;

import java.time.LocalDateTime;

public class Post {
    private Long id;
    private String content;
    private String text;
    private LocalDateTime createdAt;
    private LocalDateTime scheduledAt;
    private boolean scheduled;

    public Post() {
        this.createdAt = LocalDateTime.now();
    }

    public Post(Long id, String content, String text, LocalDateTime createdAt) {
        this.id = id;
        this.content = content;
        this.text = text;
        this.createdAt = createdAt != null ? createdAt : LocalDateTime.now();
        this.scheduled = false;
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

    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", text='" + text + '\'' +
                ", createdAt=" + createdAt +
                ", scheduledAt=" + scheduledAt +
                ", scheduled=" + scheduled +
                '}';
    }
}
