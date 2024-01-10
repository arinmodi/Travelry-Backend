package com.learning.travelry.service;

import com.learning.travelry.entities.User;

public interface UserService {

        void save(User theUser);

        Boolean existEmail(String email);

        User markUserVerified(String email);

        User getUser(String email);

        Boolean updateUser(String profilePhoto, String username, String email);
}
