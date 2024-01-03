package com.learning.travelry.service;

import com.learning.travelry.entities.Activity;
import com.learning.travelry.entities.PublicActivity;
import com.learning.travelry.repository.ActivityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;

@Service
public class ActivityServiceImpl implements ActivityService {

    @Autowired
    private ActivityRepository activityRepository;

    @Async
    @Override
    public void save(Activity activity) {
        try {
            activityRepository.save(activity);
        }catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<PublicActivity> getMyActivities(String email, String sort, String limit, String offset) {
        return activityRepository.getMyActivities(email, sort, new BigInteger(limit), new BigInteger(offset));
    }

    @Override
    public List<PublicActivity> getDiaryActivities(String id, String sort, String limit, String offset) {
        return activityRepository.getDiaryActivities(
                new BigInteger(id),
                sort,
                new BigInteger(limit),
                new BigInteger(offset)
        );
    }

}
