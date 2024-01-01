package com.learning.travelry.controllers;

import com.learning.travelry.exceptions.AwsExceptions.FileEmptyException;
import com.learning.travelry.payload.request.UpdateProfileRequest;
import com.learning.travelry.payload.response.MessageResponse;
import com.learning.travelry.service.MediaService;
import com.learning.travelry.service.UserService;
import com.learning.travelry.utils.FileUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private MediaService mediaService;

    @PatchMapping("/")
    public ResponseEntity<?> updateProfile(@Valid @ModelAttribute UpdateProfileRequest updateProfileRequest,
                                           BindingResult bindingResult) throws FileEmptyException {

        MultipartFile image = updateProfileRequest.getProfileImage();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = (String) authentication.getPrincipal();

        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .filter(Objects::nonNull)
                    .toList();

            if (!errors.isEmpty()) {
                return ResponseEntity.badRequest().body(new MessageResponse("Validation errors: " + errors));
            }
        }

        if (!userService.existEmail(email)) {
            return ResponseEntity.badRequest().body(new MessageResponse("User Not Exists: "));
        }

        if (image != null && image.isEmpty()) {
            throw new FileEmptyException(
                    image.getOriginalFilename() + " is empty. Cannot save an empty file"
            );
        }

        String imageUrl = null;

        if (image != null) {
            boolean isValidFile = (FileUtils.isValidFile(image) && FileUtils.isFileImage(image));

            if (!isValidFile) {
                return ResponseEntity.badRequest().body(new MessageResponse("Error : Invalid file"));
            } else {
                try {
                    List<String[]> response = mediaService.uploadFiles(
                        new MultipartFile[] { image }, "users"
                    );

                    imageUrl = response.get(0)[1];

                }catch (Exception e) {
                    e.printStackTrace();
                    return ResponseEntity.internalServerError().body(new MessageResponse("Internal Server Error"));
                }
            }
        }

        if (userService.updateUser(imageUrl, updateProfileRequest.getUsername(), email)) {
            return ResponseEntity.ok(new MessageResponse("User Updated successfully"));
        } else {
            return ResponseEntity.badRequest().body(new MessageResponse("Something bad happen"));
        }

    }

}
