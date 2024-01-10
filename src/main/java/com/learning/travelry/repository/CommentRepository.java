package com.learning.travelry.repository;

import com.learning.travelry.entities.Comment;
import com.learning.travelry.entities.PublicComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigInteger;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query(
            value = "SELECT " +
                    "c.comment_id AS id, c.content AS content, c.created AS created, u.profile_photo as profilePhoto, u.email AS email, " +
                    "u.username AS userName FROM comment c, users u " +
                    "where c.sender = u.email AND c.media_id = :id " +
                    "ORDER BY c.created",
            nativeQuery = true
    )
    List<PublicComment> getComments(
            @Param("id")BigInteger id
    );

}
