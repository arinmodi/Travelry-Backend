package com.learning.travelry.service;

import com.learning.travelry.entities.User;
import com.learning.travelry.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void save(User theUser) {
        userRepository.save(theUser);
    }

    @Override
    public Boolean existEmail(String email) {
        return userRepository.findByEmail(email) != null;
    }

    @Override
    public User getUser(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Boolean markUserVerified(String email) {
        User user = userRepository.findByEmail(email);

        if (user != null) {
            user.setEmailVerified(true);
            userRepository.save(user);
            return true;
        } else {
            return false;
        }
    }

}
