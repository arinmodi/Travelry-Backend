package com.learning.travelry.payload.request;

import jakarta.validation.constraints.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

public class MediaUploadRequest implements Serializable {

    @NotNull(message = "Multipart files must not be null")
    @Size(min = 1, max = 4, message = "Min 1 and Max 4 file allowed")
    private MultipartFile[] multipartFiles;

    @NotBlank(message = "diary name can't be empty")
    private String diaryName;

    public MultipartFile[] getMultipartFiles() {
        return multipartFiles;
    }

    public void setMultipartFiles(MultipartFile[] multipartFiles) {
        this.multipartFiles = multipartFiles;
    }

    public String getDiaryName() {
        return diaryName;
    }

    public void setDiaryName(String diaryName) {
        this.diaryName = diaryName;
    }
}
