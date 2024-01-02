package com.learning.travelry.service;

import com.learning.travelry.entities.Diary;
import com.learning.travelry.entities.PublicDiary;
import org.springframework.data.domain.Sort;

import java.math.BigInteger;
import java.util.List;

public interface DiaryService {

    void save(Diary theDiary);

    Diary getDiaryById(BigInteger id);

    boolean updateDiary(BigInteger id, String coverImage, String headerImage, String headerColor, String name);

    List<PublicDiary> getDiary(String email, String sort, String limit, String offset);

    List<PublicDiary> getDiaryWithSearch(String email, String search);
}
