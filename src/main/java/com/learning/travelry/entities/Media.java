package com.learning.travelry.entities;

import com.learning.travelry.utils.FileUtils;
import jakarta.persistence.*;

import java.math.BigInteger;
import java.sql.Timestamp;

@Entity
@Table(name = "media")
public class Media {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "media_id")
    private BigInteger id;

    @Column(name = "media_name")
    private String fileName;

    @Column(name = "url")
    private String url;

    @Column(name = "isvideo")
    private Boolean isVideo;

    @ManyToOne
    @JoinColumn(name = "media_owner", nullable = false)
    private User owner;

    @ManyToOne
    @JoinColumn(name = "diary_id", nullable = false)
    private Diary diary;

    @Column(name = "created")
    Timestamp createdDate;

    public Media() {}

    public Media(String fileName, String url, User owner, Diary diary, Timestamp createdDate) {
        this.fileName = fileName;
        this.url = url;
        this.isVideo = fileName.contains(".mp4");
        this.owner = owner;
        this.diary = diary;
        this.createdDate = createdDate;
    }

    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Boolean getVideo() {
        return isVideo;
    }

    public void setVideo(Boolean video) {
        isVideo = video;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public Diary getDiary() {
        return diary;
    }

    public void setDiary(Diary diary) {
        this.diary = diary;
    }

    public Timestamp getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }
}
