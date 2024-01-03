package com.learning.travelry.service;

import com.learning.travelry.entities.Activity;
import com.learning.travelry.entities.PublicActivity;

import java.util.List;

public interface ActivityService {

    void save(Activity activity);

    List<PublicActivity> getMyActivities(String email, String sort, String limit, String offset);

    List<PublicActivity> getDiaryActivities(String id, String sort, String limit, String offset);

}
