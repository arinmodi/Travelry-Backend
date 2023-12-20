package com.learning.travelry.service;

import com.learning.travelry.entities.User;

public interface UserService {

        public void save(User theUser);

        public Boolean existEmail(String email);

        public Boolean markUserVerified(String email);

        public User getUser(String email);
}
