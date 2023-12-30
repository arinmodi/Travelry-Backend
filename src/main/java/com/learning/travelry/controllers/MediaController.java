package com.learning.travelry.controllers;

import com.learning.travelry.exceptions.AwsExceptions.FileEmptyException;
import com.learning.travelry.payload.request.DeleteMediaRequest;
import com.learning.travelry.payload.request.MediaUploadRequest;
import com.learning.travelry.payload.response.MediaUploadListResponse;
import com.learning.travelry.payload.response.MediaUploadResponse;
import com.learning.travelry.payload.response.MessageResponse;
import com.learning.travelry.service.MediaService;
import jakarta.validation.Valid;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/diary/media")
public class MediaController {

    @Autowired
    private MediaService mediaService;

    private boolean isValidFile(MultipartFile multipartFile) {
        if (Objects.isNull(multipartFile.getOriginalFilename())) {
            return false;
        }
        return !multipartFile.getOriginalFilename().trim().equals("");
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadMedia(@Valid @ModelAttribute MediaUploadRequest mediaUploadRequest, BindingResult bindingResult)
            throws FileEmptyException, IOException {

        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage).toList();
            return ResponseEntity.badRequest().body(new MessageResponse("Validation errors: " + errors));
        }

        for (MultipartFile multipartFile : mediaUploadRequest.getMultipartFiles()) {
            if (multipartFile.isEmpty()) {
                throw new FileEmptyException(
                        multipartFile.getOriginalFilename() + " is empty. Cannot save an empty file"
                );
            }
        }

        boolean isValidFile = true;
        List<String> allowedFileExtensions = new ArrayList<>(Arrays.asList("png", "jpg", "jpeg", "mp4"));

        for (MultipartFile multipartFile : mediaUploadRequest.getMultipartFiles()) {
            boolean isValidLocal = isValidFile(multipartFile) &&
                    allowedFileExtensions.contains(FilenameUtils.getExtension(
                            multipartFile.getOriginalFilename()
                    ));
            isValidFile = isValidFile && isValidLocal;
        }

        if (isValidFile) {
            try {
                List<String[]> response = mediaService.uploadFiles(
                        mediaUploadRequest.getMultipartFiles(), mediaUploadRequest.getDiaryName()
                );

                List<MediaUploadResponse> finalResponse = new ArrayList<>();

                for (String[] res : response) {
                    finalResponse.add(new MediaUploadResponse(res[1], res[0]));
                }

                return ResponseEntity.ok(new MediaUploadListResponse(finalResponse));
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
        int response = mediaService.deleteFile(deleteMediaRequest.getFileName());

        if (response == 1) {
            return ResponseEntity.ok(new MessageResponse("File Deleted"));
        } else if (response == 0) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error : file not exists"));
        } else {
            return ResponseEntity.internalServerError().body(new MessageResponse("Server Error"));
        }
    }

}
