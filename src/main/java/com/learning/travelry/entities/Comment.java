package com.learning.travelry.entities;

import jakarta.persistence.*;

import java.math.BigInteger;
import java.sql.Timestamp;

@Entity
@Table(name = "comment")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    BigInteger commentId;

    @ManyToOne
    @JoinColumn(name = "sender", nullable = false)
    User sender;

    @ManyToOne
    @JoinColumn(name = "media_id", nullable = false)
    Media media;

    @Column(name = "content")
    String content;

    @Column(name = "created")
    Timestamp created;

    public Comment(User sender, Media media, String content, Timestamp created) {
        this.sender = sender;
        this.media = media;
        this.content = content;
        this.created = created;
    }

    public BigInteger getCommentId() {
        return commentId;
    }

    public void setCommentId(BigInteger commentId) {
        this.commentId = commentId;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public Media getMedia() {
        return media;
    }

    public void setMedia(Media media) {
        this.media = media;
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
}
