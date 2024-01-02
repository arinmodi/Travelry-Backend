package com.learning.travelry.controllers;

import com.learning.travelry.entities.Diary;
import com.learning.travelry.entities.PublicDiary;
import com.learning.travelry.exceptions.AwsExceptions.FileEmptyException;
import com.learning.travelry.payload.request.UpdateProfileRequest;
import com.learning.travelry.payload.response.MessageResponse;
import com.learning.travelry.service.DiaryService;
import com.learning.travelry.service.MediaService;
import com.learning.travelry.service.UserService;
import com.learning.travelry.utils.FileUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.data.domain.Sort;
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

    @Autowired
    private DiaryService diaryService;

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

    @GetMapping("/diary")
    public ResponseEntity<?> getDiaries(
            @RequestParam(name = "sort", required = false, defaultValue = "-1") String sortDirection,
            @RequestParam(name = "limit", required = false, defaultValue = "4") String limit,
            @RequestParam(name = "offset", required = false, defaultValue = "0") String offset
    ) {
        String sort = null;
        if (sortDirection.equals("1")) {
            sort = "ASC";
        }else {
            sort = "DESC";
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = (String) authentication.getPrincipal();

        try {
            List<PublicDiary> diaries = diaryService.getDiary(
                    email, sort, limit, offset
            );
            return ResponseEntity.ok().body(diaries);
        } catch (Exception e) {
            System.out.println(e.toString());
            return ResponseEntity.internalServerError().body(new MessageResponse("Something bad happen"));
        }
    }

    @GetMapping("/diary/search")
    public ResponseEntity<?> getDiariesWithSearch(
            @RequestParam(name = "search", required = true, defaultValue = "") String search
    ) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = (String) authentication.getPrincipal();

        if (search.trim().isEmpty()) {
            return ResponseEntity.internalServerError().body(new MessageResponse("Empty Search string"));
        }

        try {
            List<PublicDiary> diaries = diaryService.getDiaryWithSearch(
                    email, "%"+search+"%"
            );
            return ResponseEntity.ok().body(diaries);
        } catch (Exception e) {
            System.out.println(e.toString());
            return ResponseEntity.internalServerError().body(new MessageResponse("Something bad happen"));
        }
    }

}
