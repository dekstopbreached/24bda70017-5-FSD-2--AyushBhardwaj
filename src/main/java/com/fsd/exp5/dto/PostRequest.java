package com.fsd.exp5.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class PostRequest {

    @NotBlank(message = "Content must not be empty")
    private String content;

    @Size(max = 280, message = "Content exceeds limit")
    private String text;

    public PostRequest() {
    }

    public PostRequest(String content, String text) {
        this.content = content;
        this.text = text;
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
}
