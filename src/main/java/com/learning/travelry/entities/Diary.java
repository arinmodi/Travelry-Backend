package com.learning.travelry.entities;

import jakarta.persistence.*;

import java.math.BigInteger;
import java.sql.Timestamp;

@Entity
@Table(name="diary")
public class Diary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    BigInteger id;

    @Column(name="name")
    String name;

    @Column(name = "cover")
    String coverImage;

    @Column(name = "header")
    String headerImage;

    @Column(name = "color")
    String headerColor;

    @Column(name = "created")
    Timestamp createdDate;

    @ManyToOne
    @JoinColumn(name = "creator_email", nullable = false)
    private User creator;

    public Diary() { }

    public Diary(String name, String coverImage, String headerImage, String headerColor, User creator, Timestamp createdDate) {
        this.name = name;
        this.coverImage = coverImage;
        this.headerImage = headerImage;
        this.headerColor = headerColor;
        this.creator = creator;
        this.createdDate = createdDate;
    }

    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    public String getHeaderImage() {
        return headerImage;
    }

    public void setHeaderImage(String headerImage) {
        this.headerImage = headerImage;
    }

    public String getHeaderColor() {
        return headerColor;
    }

    public void setHeaderColor(String headerColor) {
        this.headerColor = headerColor;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public Timestamp getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }
}
