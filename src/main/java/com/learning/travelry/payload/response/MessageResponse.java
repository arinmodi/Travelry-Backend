package com.learning.travelry.payload.response;

public class MessageResponse {
    private String message;
    public MessageResponse(String message) {
        this.message = message;
    }

    public MessageResponse(String message, String token) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
