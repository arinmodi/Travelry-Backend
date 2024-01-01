package com.learning.travelry.controllers;

import com.learning.travelry.entities.Diary;
import com.learning.travelry.entities.User;
import com.learning.travelry.exceptions.AwsExceptions.FileEmptyException;
import com.learning.travelry.payload.request.CreateDiaryRequest;
import com.learning.travelry.payload.request.UpdateDiaryRequest;
import com.learning.travelry.payload.request.UpdateProfileRequest;
import com.learning.travelry.payload.response.MessageResponse;
import com.learning.travelry.service.DiaryService;
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

import java.math.BigInteger;
import java.util.List;
import java.util.Objects;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/diary")
public class DiaryController {

    @Autowired
    private DiaryService diaryService;

    @Autowired
    private MediaService mediaService;

    @Autowired
    private UserService userService;

    @PostMapping("/")
    public ResponseEntity<?> createDiary(@Valid @ModelAttribute CreateDiaryRequest createDiaryRequest,
                                         BindingResult bindingResult) throws FileEmptyException {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .filter(Objects::nonNull)
                    .toList();

            if (!errors.isEmpty()) {
                return ResponseEntity.badRequest().body(new MessageResponse("Validation errors: " + errors));
            }
        }


        MultipartFile image = createDiaryRequest.getCoverImage();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = (String) authentication.getPrincipal();


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
                        new MultipartFile[] { image }, "covers"
                    );

                    imageUrl = response.get(0)[1];

                }catch (Exception e) {
                    e.printStackTrace();
                    return ResponseEntity.internalServerError().body(new MessageResponse("Internal Server Error"));
                }
            }
        }

        User creator = userService.getUser(email);

        Diary newDiary = new Diary(
                createDiaryRequest.getDiaryName(),
                imageUrl,
                null,
                createDiaryRequest.getColor(),
                creator
        );

        try {
            diaryService.save(newDiary);
            return ResponseEntity.ok(new MessageResponse("Diary Created Successfully"));
        }catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse("Something bad happen"));
        }
    }

    @PatchMapping("/")
    public ResponseEntity<?> updateDiary(@Valid @ModelAttribute UpdateDiaryRequest updateDiaryRequest,
                                         BindingResult bindingResult) throws FileEmptyException {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .filter(Objects::nonNull)
                    .toList();

            if (!errors.isEmpty()) {
                return ResponseEntity.badRequest().body(new MessageResponse("Validation errors: " + errors));
            }
        }

        if (diaryService.getDiaryById(updateDiaryRequest.getId()) == null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Diary Not Exists"));
        }


        MultipartFile coverImage = updateDiaryRequest.getCoverImage();
        MultipartFile headerImage = updateDiaryRequest.getHeader();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = (String) authentication.getPrincipal();


        if (coverImage != null && coverImage.isEmpty()) {
            throw new FileEmptyException(
                    coverImage.getOriginalFilename() + " is empty. Cannot save an empty file"
            );
        }

        if (headerImage != null && headerImage.isEmpty()) {
            throw new FileEmptyException(
                    headerImage.getOriginalFilename() + " is empty. Cannot save an empty file"
            );
        }

        String coverImageUrl = null;

        if (coverImage != null) {
            boolean isValidFile = (FileUtils.isValidFile(coverImage) && FileUtils.isFileImage(coverImage));

            if (!isValidFile) {
                return ResponseEntity.badRequest().body(new MessageResponse("Error : Invalid Cover file"));
            } else {
                try {
                    List<String[]> response = mediaService.uploadFiles(
                        new MultipartFile[] { coverImage }, "covers"
                    );

                    coverImageUrl = response.get(0)[1];

                }catch (Exception e) {
                    e.printStackTrace();
                    return ResponseEntity.internalServerError().body(new MessageResponse("Internal Server Error"));
                }
            }
        }

        String headerImageUrl = null;

        if (headerImage != null) {
            boolean isValidFile = (FileUtils.isValidFile(headerImage) && FileUtils.isFileImage(headerImage));

            if (!isValidFile) {
                return ResponseEntity.badRequest().body(new MessageResponse("Error : Invalid Header file"));
            } else {
                try {
                    List<String[]> response = mediaService.uploadFiles(
                        new MultipartFile[] { headerImage }, "headers"
                    );

                    headerImageUrl = response.get(0)[1];

                }catch (Exception e) {
                    e.printStackTrace();
                    return ResponseEntity.internalServerError().body(new MessageResponse("Internal Server Error"));
                }
            }
        }

        try {
            diaryService.updateDiary(
                    updateDiaryRequest.getId(),
                    coverImageUrl,
                    headerImageUrl,
                    updateDiaryRequest.getColor(),
                    updateDiaryRequest.getDiaryName()
            );
            return ResponseEntity.ok(new MessageResponse("Diary Updated Successfully"));
        }catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse("Something bad happen"));
        }
    }

}
