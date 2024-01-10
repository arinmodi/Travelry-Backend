package com.learning.travelry.payload.response;

import com.learning.travelry.entities.PublicUser;

public class LoginResponse {

    private String message;
    private String token;

    private PublicUser user;

    private boolean isEmailVerified;

    public LoginResponse(String message, String token, PublicUser user, Boolean isEmailVerified) {
        this.message = message;
        this.token = token;
        this.user = user;
        this.isEmailVerified = isEmailVerified;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public PublicUser getUser() {
        return user;
    }

    public void setUser(PublicUser user) {
        this.user = user;
    }

    public boolean isEmailVerified() {
        return isEmailVerified;
    }

    public void setEmailVerified(boolean emailVerified) {
        isEmailVerified = emailVerified;
    }
}
