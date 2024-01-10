package com.learning.travelry.entities;

import java.math.BigInteger;
import java.sql.Timestamp;

public class CommentForWebSocket {

    private BigInteger id;
    private String content;
    private Timestamp created;
    private String profilePhoto;
    private String userName;
    private String email;

    public CommentForWebSocket(BigInteger id, String content, Timestamp created, String profilePhoto, String userName, String email) {
        this.id = id;
        this.content = content;
        this.created = created;
        this.profilePhoto = profilePhoto;
        this.userName = userName;
        this.email = email;
    }

    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Timestamp getCreated() {
        return created;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }

    public String getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
