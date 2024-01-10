package com.learning.travelry.service;

import com.learning.travelry.entities.User;
import com.learning.travelry.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserServiceImpl implements UserService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

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
    public Boolean updateUser(String profilePhoto, String username, String email) {
        User user = userRepository.findByEmail(email);

        if (user != null) {
            if (profilePhoto != null) {
                user.setProfilePhoto(profilePhoto);
            }

            if (username != null) {
                user.setUsername(username);
            }

            userRepository.save(user);

            return true;
        } else {
            return false;
        }
    }

    @Override
    public User markUserVerified(String email) {
        User user = userRepository.findByEmail(email);

        if (user != null) {
            user.setEmailVerified(true);
            userRepository.save(user);
            return user;
        } else {
            return null;
        }
    }


}
