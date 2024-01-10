package com.learning.travelry.repository;

import com.learning.travelry.entities.Media;
import com.learning.travelry.entities.PublicMedia;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigInteger;
import java.util.List;

public interface MediaRepository extends JpaRepository<Media, Long> {

    Media findMediaById(BigInteger id);

    @Query(
            value = "SELECT " +
                    "m.media_id AS id, m.media_name AS name, m.url AS url, m.media_owner as owner, m.created as created, " +
                    "m.isvideo as isVideo FROM media m WHERE m.diary_id = :id " +
                    "ORDER BY CASE WHEN :sort = 'DESC' THEN m.created END DESC, " +
                    "CASE WHEN :sort = 'ASC' THEN m.created END ASC " +
                    "LIMIT :limit OFFSET :offset",
            nativeQuery = true
    )
    List<PublicMedia> getMedia(
            @Param("id") BigInteger id,
            @Param("sort") String sort,
            @Param("limit") BigInteger limit,
            @Param("offset") BigInteger offset
    );

    @Transactional
    void deleteById(BigInteger id);

}
