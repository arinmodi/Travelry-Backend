package com.learning.travelry.entities;

import jakarta.persistence.*;


@Entity
@Table(name = "members")
public class Member {

    @Id
    @Column(name = "entity_id")
    String entityId;

    @ManyToOne
    @JoinColumn(name = "member_email", nullable = false)
    private User member;

    @ManyToOne
    @JoinColumn(name = "diary_id", nullable = false)
    private Diary diary;

    public Member() {
    }

    public Member(String entityId, User member, Diary diary) {
        this.entityId = entityId;
        this.member = member;
        this.diary = diary;
    }

    public User getMember() {
        return member;
    }

    public void setMember(User member) {
        this.member = member;
    }

    public Diary getDiary() {
        return diary;
    }

    public void setDiary(Diary diary) {
        this.diary = diary;
    }

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }
}
