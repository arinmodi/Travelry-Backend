package com.learning.travelry.repository;

import com.learning.travelry.entities.Diary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;

public interface DiaryRepository extends JpaRepository<Diary, Long> {

    Diary findDiaryById(BigInteger id);

}
