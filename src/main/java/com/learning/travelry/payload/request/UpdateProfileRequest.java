package com.learning.travelry.payload.request;

import com.learning.travelry.annonations.OneNotNull;
import org.springframework.web.multipart.MultipartFile;

@OneNotNull
public class UpdateProfileRequest {

    private MultipartFile profileImage;
    private String username;

    public MultipartFile getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(MultipartFile profileImage) {
        this.profileImage = profileImage;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
