package com.learning.travelry.repository;

import com.learning.travelry.entities.Member;
import com.learning.travelry.entities.PublicUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigInteger;
import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Member findByEntityId(String entityId);

    @Query("SELECT new com.learning.travelry.entities.PublicUser(m.member.email, m.member.username, m.member.profilePhoto) FROM Member m WHERE m.diary.id = :id")
    List<PublicUser> findUsersByDiaryId(@Param("id") BigInteger id);
}
