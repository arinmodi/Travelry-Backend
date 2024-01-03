package com.learning.travelry.payload.request;

import jakarta.validation.constraints.NotBlank;

public class CommentRequest {

    @NotBlank(message = "content can't be blank")
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
