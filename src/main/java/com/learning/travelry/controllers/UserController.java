package com.learning.travelry.controllers;

import com.learning.travelry.entities.*;
import com.learning.travelry.exceptions.AwsExceptions.FileEmptyException;
import com.learning.travelry.payload.request.UpdateProfileRequest;
import com.learning.travelry.payload.response.MediaUploadResponse;
import com.learning.travelry.payload.response.MessageResponse;
import com.learning.travelry.service.*;
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
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private MediaService mediaService;

    @Autowired
    private DiaryService diaryService;

    @Autowired
    private ActivityService activityService;

    @Autowired
    private MemberService memberService;

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
            return ResponseEntity.ok(new MediaUploadResponse(imageUrl, null));
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

    @GetMapping("/activities")
    public ResponseEntity<?> getActivities(
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
            List<PublicActivity> activities = activityService.getMyActivities(
                    email, sort, limit, offset
            );
            return ResponseEntity.ok().body(activities);
        } catch (Exception e) {
            System.out.println(e.toString());
            return ResponseEntity.internalServerError().body(new MessageResponse("Something bad happen"));
        }
    }

    @GetMapping("/{id}/activities")
    public ResponseEntity<?> getDiaryActivities(
            @PathVariable(name = "id") String id,
            @RequestParam(name = "sort", required = false, defaultValue = "-1") String sortDirection,
            @RequestParam(name = "limit", required = false, defaultValue = "4") String limit,
            @RequestParam(name = "offset", required = false, defaultValue = "0") String offset
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = (String) authentication.getPrincipal();

        Diary diary = diaryService.getDiaryById(new BigInteger(id));

        if (diary == null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error : Invalid Request"));
        }

        List<PublicUser> members = memberService.getMembersByDiaryId(diary.getId());

        boolean isMemberOrOwner = false;

        if (diary.getCreator().getEmail().equals(email)) {
            isMemberOrOwner = true;
        } else {
            for (PublicUser pu : members) {
                if (pu.getEmail().equals(email)) {
                    isMemberOrOwner = true;
                    break;
                }
            }
        }

        if (!isMemberOrOwner) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error : Unauthorized access"));
        }

        String sort = null;
        if (sortDirection.equals("1")) {
            sort = "ASC";
        }else {
            sort = "DESC";
        }

        try {
            List<PublicActivity> activities = activityService.getDiaryActivities(
                    id, sort, limit, offset
            );
            return ResponseEntity.ok().body(activities);
        } catch (Exception e) {
            System.out.println(e.toString());
            return ResponseEntity.internalServerError().body(new MessageResponse("Something bad happen"));
        }
    }

    @GetMapping("/{email}")
    public ResponseEntity<?> getUserDetails(
            @PathVariable(name = "email") String email
    ) {
        User user = userService.getUser(email);

        PublicUser pu = user == null ? null : new PublicUser(
                user.getEmail(), user.getUsername(), user.getProfilePhoto()
        );

        return ResponseEntity.ok().body(pu);
    }

}
