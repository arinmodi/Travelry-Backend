package com.learning.travelry.repository;

import com.learning.travelry.entities.Activity;
import com.learning.travelry.entities.PublicActivity;
import com.learning.travelry.entities.PublicDiary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigInteger;
import java.util.List;

public interface ActivityRepository extends JpaRepository<Activity, Long> {

    @Query(
            value = "SELECT " +
                    "a.activity_id AS id, a.created AS created, a.message AS message, " +
                    "d.name AS diaryName, u.username AS userName, u.profile_photo AS profilePhoto " +
                    "FROM Users u, Diary d, Activity a " +
                    "WHERE a.user_email = u.email AND a.diary_id = d.id AND d.id IN (" +
                    "SELECT id FROM diary where creator_email = :email " +
                    "UNION " +
                    "SELECT diary_id FROM members where member_email = :email) " +
                    "ORDER BY CASE WHEN :sort = 'DESC' THEN a.created END DESC, " +
                    "CASE WHEN :sort = 'ASC' THEN a.created END ASC " +
                    "LIMIT :limit OFFSET :offset",
            nativeQuery = true
    )
    List<PublicActivity> getMyActivities(
            @Param("email") String email,
            @Param("sort") String sort,
            @Param("limit") BigInteger limit,
            @Param("offset") BigInteger offset
    );

    @Query(
            value = "SELECT " +
                    "a.activity_id AS id, a.created AS created, a.message AS message, " +
                    "d.name AS diaryName, u.username AS userName, u.profile_photo AS profilePhoto " +
                    "FROM Users u, Diary d, Activity a " +
                    "WHERE a.user_email = u.email AND a.diary_id = d.id AND d.id = :id " +
                    "ORDER BY CASE WHEN :sort = 'DESC' THEN a.created END DESC, " +
                    "CASE WHEN :sort = 'ASC' THEN a.created END ASC " +
                    "LIMIT :limit OFFSET :offset",
            nativeQuery = true
    )
    List<PublicActivity> getDiaryActivities(
            @Param("id") BigInteger id,
            @Param("sort") String sort,
            @Param("limit") BigInteger limit,
            @Param("offset") BigInteger offset
    );

}
