package com.learning.travelry.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

public class CreateDiaryRequest {

    @NotBlank(message = "name must not be null")
    @Size(min = 2, max = 15)
    private String diaryName;

    @NotNull(message = "cover image must not be null")
    private MultipartFile coverImage;

    @NotBlank(message = "Color must not be null")
    @Size(min = 7, max = 7)
    private String color;

    public String getDiaryName() {
        return diaryName;
    }

    public void setDiaryName(String diaryName) {
        this.diaryName = diaryName;
    }

    public MultipartFile getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(MultipartFile coverImage) {
        this.coverImage = coverImage;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
