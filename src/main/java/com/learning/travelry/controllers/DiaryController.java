package com.learning.travelry.controllers;

import com.learning.travelry.entities.*;
import com.learning.travelry.exceptions.AwsExceptions.FileEmptyException;
import com.learning.travelry.payload.request.AddMemberRequest;
import com.learning.travelry.payload.request.CreateDiaryRequest;
import com.learning.travelry.payload.request.UpdateDiaryRequest;
import com.learning.travelry.payload.response.MediaUploadListResponse;
import com.learning.travelry.payload.response.MediaUploadResponse;
import com.learning.travelry.payload.response.MembersListResponse;
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
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
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

    @Autowired
    private MemberService memberService;

    @Autowired
    private ActivityService activityService;

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
                            new MultipartFile[]{image}, "covers"
                    );

                    imageUrl = response.get(0)[1];

                } catch (Exception e) {
                    e.printStackTrace();
                    return ResponseEntity.internalServerError().body(new MessageResponse("Internal Server Error"));
                }
            }
        }

        User creator = userService.getUser(email);

        long currentTimestampMillis = Instant.now().toEpochMilli();

        Diary newDiary = new Diary(
                createDiaryRequest.getDiaryName(),
                imageUrl,
                null,
                createDiaryRequest.getColor(),
                creator,
                new Timestamp(currentTimestampMillis)
        );

        try {
            diaryService.save(newDiary);
            return ResponseEntity.ok(new MessageResponse("Diary Created Successfully"));
        } catch (Exception e) {
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


        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = (String) authentication.getPrincipal();
        Diary diary = diaryService.getDiaryById(updateDiaryRequest.getId());
        User user = userService.getUser(email);

        if (diary == null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Diary Not Exists"));
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


        MultipartFile coverImage = updateDiaryRequest.getCoverImage();
        MultipartFile headerImage = updateDiaryRequest.getHeader();

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
                            new MultipartFile[]{coverImage}, "covers"
                    );

                    coverImageUrl = response.get(0)[1];

                } catch (Exception e) {
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
                            new MultipartFile[]{headerImage}, "headers"
                    );

                    headerImageUrl = response.get(0)[1];

                } catch (Exception e) {
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
            Activity activity = new Activity(
                        diary, user, "updated settings of ",
                        new Timestamp(Instant.now().toEpochMilli())
            );
            activityService.save(activity);
            List<MediaUploadResponse> mediaUploadResponses = new ArrayList<>();
            mediaUploadResponses.add(new MediaUploadResponse(coverImageUrl, null));
            mediaUploadResponses.add(new MediaUploadResponse(headerImageUrl, null));
            return ResponseEntity.ok(new MediaUploadListResponse(mediaUploadResponses));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new MessageResponse("Something bad happen"));
        }
    }

    @PostMapping("/addMember")
    public ResponseEntity<?> addMember(@Valid @RequestBody AddMemberRequest addMemberRequest) {
        BigInteger id = addMemberRequest.getId();
        String member = addMemberRequest.getEmail();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = (String) authentication.getPrincipal();

        try {
            Diary diary = diaryService.getDiaryById(id);

            if (diary == null) {
                return ResponseEntity.badRequest().body(new MessageResponse("Invalid Diary"));
            }

            User memberObject = userService.getUser(member);

            if (memberObject == null) {
                return ResponseEntity.badRequest().body(new MessageResponse("User Not Exists"));
            }

            List<PublicUser> members = memberService.getMembersByDiaryId(id);

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

            if (diary.getCreator().getEmail().equals(addMemberRequest.getEmail())) {
                return ResponseEntity.badRequest().body(new MessageResponse("Owner can't be added as member"));
            }

            if (memberService.save(
                    new Member(
                            id + "-" + member, memberObject, diary
                    )
            )) {
                User user = userService.getUser(email);
                Activity activity = new Activity(
                            diary, user, "added " + member + " to ",
                            new Timestamp(Instant.now().toEpochMilli())
                );
                activityService.save(activity);
                return ResponseEntity.ok(new MessageResponse("Member Added to diary"));
            } else {
                return ResponseEntity.badRequest().body(new MessageResponse("Member already exists"));
            }

        } catch (Exception e) {
            System.out.println(e.toString());
            return ResponseEntity.internalServerError().body(new MessageResponse("Something bad happen"));
        }
    }

    @GetMapping("/{diaryId}/members")
    public ResponseEntity<?> getMembers(@PathVariable(value = "diaryId") BigInteger diaryId) {
        try {
            List<PublicUser> members = memberService.getMembersByDiaryId(diaryId);
            Diary diary = diaryService.getDiaryById(diaryId);
            members.add(0, new PublicUser(
                    diary.getCreator().getEmail(),
                    diary.getCreator().getUsername(),
                    diary.getCreator().getProfilePhoto()
            ));
            return ResponseEntity.ok(new MembersListResponse((members)));
        } catch (Exception e) {
            System.out.println(e.toString());
            return ResponseEntity.internalServerError().body(new MessageResponse("Something bad happen"));
        }
    }
}
