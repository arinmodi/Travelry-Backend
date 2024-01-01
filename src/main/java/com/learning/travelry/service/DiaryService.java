package com.learning.travelry.service;

import com.learning.travelry.entities.Diary;
import com.learning.travelry.payload.request.UpdateDiaryRequest;

import java.math.BigInteger;

public interface DiaryService {

    void save(Diary theDiary);

    Diary getDiaryById(BigInteger id);

    boolean updateDiary(BigInteger id, String coverImage, String headerImage, String headerColor, String name);
}
