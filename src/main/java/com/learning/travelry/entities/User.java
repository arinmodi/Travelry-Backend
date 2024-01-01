package com.learning.travelry.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="users")
public class User {

    @Id
    @Column(name="email")
    private String email;

    @Column(name = "username")
    private String username;

    @Column(name="is_email_verified")
    private Boolean isEmailVerified;

    @Column(name="password")
    private String password;

    @Column(name = "profile_photo")
    private String profilePhoto;

    public User() { }

    public User(String email, String username, Boolean isEmailVerified, String password) {
        this.email = email;
        this.username = username;
        this.isEmailVerified = isEmailVerified;
        this.password = password;
    }

    public User(String email, String username, Boolean isEmailVerified, String password, String profilePhoto) {
        this.email = email;
        this.username = username;
        this.isEmailVerified = isEmailVerified;
        this.password = password;
        this.profilePhoto = profilePhoto;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Boolean getEmailVerified() {
        return isEmailVerified;
    }

    public void setEmailVerified(Boolean emailVerified) {
        isEmailVerified = emailVerified;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", isEmailVerified=" + isEmailVerified +
                ", password='" + password + '\'' +
                ", profilePhoto='" + profilePhoto + '\'' +
                '}';
    }
}
