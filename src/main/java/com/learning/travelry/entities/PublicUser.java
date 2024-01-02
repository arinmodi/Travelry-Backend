package com.learning.travelry.entities;

public class PublicUser {

    private String email;

    private String userName;

    private String profilePhoto;

    public PublicUser(String email, String userName, String profilePhoto) {
        this.email = email;
        this.userName = userName;
        this.profilePhoto = profilePhoto;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }
}
