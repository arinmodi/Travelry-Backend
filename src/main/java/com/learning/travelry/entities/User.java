package com.learning.travelry.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="users")
public class User {

    public User() { }

    public User(String email, String username, Boolean isEmailVerified, String password) {
        this.email = email;
        this.username = username;
        this.isEmailVerified = isEmailVerified;
        this.password = password;
    }

    @Id
    @Column(name="email")
    private String email;

    @Column(name = "username")
    private String username;

    @Column(name="isemailverified")
    private Boolean isEmailVerified;

    @Column(name="password")
    private String password;

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

    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", isEmailVerified=" + isEmailVerified +
                ", password='" + password + '\'' +
                '}';
    }
}
