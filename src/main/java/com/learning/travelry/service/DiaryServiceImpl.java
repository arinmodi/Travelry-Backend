package com.learning.travelry.service;

import com.learning.travelry.entities.Diary;
import com.learning.travelry.entities.PublicDiary;
import com.learning.travelry.repository.DiaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;

@Service
public class DiaryServiceImpl implements DiaryService {

    @Autowired
    private DiaryRepository diaryRepository;

    @Override
    public void save(Diary theDiary) {
        diaryRepository.save(theDiary);
    }

    @Override
    public Diary getDiaryById(BigInteger id) {
        return diaryRepository.findDiaryById(id);
    }

    @Override
    public boolean updateDiary(BigInteger id, String coverImage, String headerImage, String headerColor, String name) {
        Diary diary = diaryRepository.findDiaryById(id);

        if (diary != null) {
            if (coverImage != null) {
                diary.setCoverImage(coverImage);
            }

            if (headerColor != null) {
                diary.setHeaderColor(headerColor);
                diary.setHeaderImage(null);
            }

            if (headerImage != null) {
                diary.setHeaderImage(headerImage);
                diary.setHeaderColor(null);
            }

            if (name != null) {
                diary.setName(name);
            }

            diaryRepository.save(diary);

            return true;
        }

        return false;
    }

    @Override
    public List<PublicDiary> getDiary(String email, String sort, String limit, String offset) {
        return diaryRepository.getMyDiary(email, sort, new BigInteger(limit), new BigInteger(offset));
    }

    @Override
    public List<PublicDiary> getDiaryWithSearch(String email, String search) {
        return diaryRepository.getMyDiaryWithSearch(email, search);
    }
}
