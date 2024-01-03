package com.learning.travelry.controllers;

import com.learning.travelry.entities.*;
import com.learning.travelry.exceptions.AwsExceptions.FileEmptyException;
import com.learning.travelry.payload.request.DeleteMediaRequest;
import com.learning.travelry.payload.request.MediaUploadRequest;
import com.learning.travelry.payload.response.MediaUploadListResponse;
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

import java.io.IOException;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/diary/media")
public class MediaController {

    @Autowired
    private MediaService mediaService;

    @Autowired
    private UserService userService;

    @Autowired
    private DiaryService diaryService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private ActivityService activityService;

    private boolean isMemberOrOwner(Diary diary, String email) {
        List<PublicUser> members = memberService.getMembersByDiaryId(diary.getId());

        if (diary.getCreator().getEmail().equals(email)) {
            return true;
        } else {
            for (PublicUser pu : members) {
                if (pu.getEmail().equals(email)) {
                    return true;
                }
            }
        }

        return false;
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadMedia(@Valid @ModelAttribute MediaUploadRequest mediaUploadRequest, BindingResult bindingResult)
            throws FileEmptyException, IOException {

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

        Diary diary = diaryService.getDiaryById(new BigInteger(mediaUploadRequest.getDiaryName()));
        User owner = userService.getUser(email);

        if (owner == null || diary == null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error : Invalid request"));
        }


        if (!isMemberOrOwner(diary, email)) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error : Unauthorized access"));
        }

        for (MultipartFile multipartFile : mediaUploadRequest.getMultipartFiles()) {
            if (multipartFile.isEmpty()) {
                throw new FileEmptyException(
                        multipartFile.getOriginalFilename() + " is empty. Cannot save an empty file"
                );
            }
        }

        boolean isValidFile = true;

        for (MultipartFile multipartFile : mediaUploadRequest.getMultipartFiles()) {
            boolean isValidLocal = FileUtils.isValidFile(multipartFile) && FileUtils.isFileMedia(multipartFile);
            isValidFile = isValidFile && isValidLocal;
        }

        if (isValidFile) {
            try {
                List<String[]> response = mediaService.uploadFiles(
                        mediaUploadRequest.getMultipartFiles(), mediaUploadRequest.getDiaryName()
                );

                List<MediaUploadResponse> finalResponse = new ArrayList<>();
                List<Media> medias = new ArrayList<>();

                for (String[] res : response) {
                    finalResponse.add(new MediaUploadResponse(res[1], res[0]));
                    long currentTimestampMillis = Instant.now().toEpochMilli();
                    medias.add(
                            new Media(res[0], res[1], owner, diary, new Timestamp(currentTimestampMillis))
                    );
                }

                if (mediaService.saveAll(medias)) {
                    Activity activity = new Activity(
                            diary, owner, "uploaded " + finalResponse.size() + " file to ",
                            new Timestamp(Instant.now().toEpochMilli())
                    );
                    activityService.save(activity);
                    return ResponseEntity.ok(new MediaUploadListResponse(finalResponse));
                } else {
                    return ResponseEntity.internalServerError().body(new MessageResponse("something bad happen"));
                }
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.internalServerError().body(new MessageResponse("Internal Server Error"));
            }
        } else {
            return ResponseEntity.badRequest().body(new MessageResponse("Error : Invalid file"));
        }
    }

    @DeleteMapping("/")
    public ResponseEntity<?> deleteMedia(@Valid @RequestBody DeleteMediaRequest deleteMediaRequest) {
        Media media = mediaService.getMediaById(deleteMediaRequest.getMediaId());

        if (media == null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error : Invalid request"));
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = (String) authentication.getPrincipal();

        if (!media.getOwner().getEmail().equals(email)) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error : Unauthorized access"));
        }

        int response = mediaService.deleteFile(deleteMediaRequest.getFileName());

        if (response == 1) {
            Activity activity = new Activity(
                    media.getDiary(), media.getOwner(), "deleted 1 file from ",
                    new Timestamp(Instant.now().toEpochMilli())
            );
            activityService.save(activity);
            return ResponseEntity.ok(new MessageResponse("File Deleted"));
        } else if (response == 0) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error : file not exists"));
        } else {
            return ResponseEntity.internalServerError().body(new MessageResponse("Server Error"));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getMedia(
            @PathVariable(value = "id") BigInteger id,
            @RequestParam(name = "sort", required = false, defaultValue = "-1") String sortDirection,
            @RequestParam(name = "limit", required = false, defaultValue = "4") String limit,
            @RequestParam(name = "offset", required = false, defaultValue = "0") String offset
    ) {
        Diary diary = diaryService.getDiaryById(id);

        if (diary == null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error : Invalid request"));
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = (String) authentication.getPrincipal();

        if (!isMemberOrOwner(diary, email)) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error : Unauthorized access"));
        }

        String sort = null;
        if (sortDirection.equals("1")) {
            sort = "ASC";
        } else {
            sort = "DESC";
        }

        try {
            List<PublicMedia> medias = mediaService.getMedia(id, sort, new BigInteger(limit), new BigInteger(offset));
            return ResponseEntity.ok().body(medias);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new MessageResponse("Server Error"));
        }
    }

}
