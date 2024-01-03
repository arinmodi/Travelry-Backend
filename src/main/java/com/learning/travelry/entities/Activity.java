package com.learning.travelry.entities;

import jakarta.persistence.*;

import java.math.BigInteger;
import java.sql.Timestamp;

@Entity
@Table(name = "activity")
public class Activity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "activity_id")
    BigInteger activityId;

    @ManyToOne
    @JoinColumn(name = "diary_id", nullable = false)
    Diary diary;

    @ManyToOne
    @JoinColumn(name = "user_email", nullable = false)
    User user;

    @Column(name = "message")
    String message;

    @Column(name = "created")
    Timestamp created;

    public Activity() { }

    public Activity(Diary diary, User user, String message, Timestamp created) {
        this.diary = diary;
        this.user = user;
        this.message = message;
        this.created = created;
    }

    public BigInteger getActivityId() {
        return activityId;
    }

    public void setActivityId(BigInteger activityId) {
        this.activityId = activityId;
    }

    public Diary getDiary() {
        return diary;
    }

    public void setDiary(Diary diary) {
        this.diary = diary;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Timestamp getCreated() {
        return created;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }
}
