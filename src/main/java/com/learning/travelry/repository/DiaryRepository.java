package com.learning.travelry.repository;

import com.learning.travelry.entities.Diary;
import com.learning.travelry.entities.PublicDiary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigInteger;
import java.util.List;

public interface DiaryRepository extends JpaRepository<Diary, Long> {

    Diary findDiaryById(BigInteger id);

    @Query(
            value = "SELECT " +
                    "d.id AS id, d.name AS name, d.cover AS coverImage, d.header AS headerImage, " +
                    "d.color AS headerColor, d.created AS createdDate, d.creator_email AS creatorEmail, " +
                    "u.username as creatorUserName, u.profile_photo as creatorImage " +
                    "FROM diary d JOIN users u ON d.creator_email = u.email WHERE id IN " +
                    "(SELECT id FROM diary WHERE creator_email = :email " +
                    "UNION " +
                    "SELECT diary_id AS id FROM members WHERE member_email = :email) " +
                    "ORDER BY CASE WHEN :sort = 'DESC' THEN created END DESC, " +
                    "CASE WHEN :sort = 'ASC' THEN created END ASC " +
                    "LIMIT :limit OFFSET :offset",
            nativeQuery = true
    )
    List<PublicDiary> getMyDiary(
            @Param("email") String email,
            @Param("sort") String sort,
            @Param("limit") BigInteger limit,
            @Param("offset") BigInteger offset
    );

    @Query(
            value = "SELECT " +
                    "d.id AS id, d.name AS name, d.cover AS coverImage, d.header AS headerImage, " +
                    "d.color AS headerColor, d.created AS createdDate, d.creator_email AS creatorEmail, " +
                    "u.username as creatorUserName, u.profile_photo as creatorImage " +
                    "FROM diary d JOIN users u ON d.creator_email = u.email WHERE id IN " +
                    "(SELECT id FROM diary WHERE creator_email = :email " +
                    "UNION " +
                    "SELECT diary_id AS id FROM members WHERE member_email = :email) " +
                    "AND d.name LIKE :search ORDER BY d.created DESC",
            nativeQuery = true
    )
    List<PublicDiary> getMyDiaryWithSearch(
            @Param("email") String email,
            @Param("search") String search
    );

}
