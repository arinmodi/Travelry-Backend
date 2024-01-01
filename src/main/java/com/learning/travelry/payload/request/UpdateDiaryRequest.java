package com.learning.travelry.payload.request;

import com.learning.travelry.annonations.OneNotNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigInteger;


@OneNotNull
public class UpdateDiaryRequest {

    @NotNull(message = "id must not be null")
    private BigInteger id;

    private MultipartFile coverImage;

    private String color;

    private MultipartFile header;

    private String diaryName;

    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
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

    public MultipartFile getHeader() {
        return header;
    }

    public void setHeader(MultipartFile header) {
        this.header = header;
    }

    public String getDiaryName() {
        return diaryName;
    }

    public void setDiaryName(String diaryName) {
        this.diaryName = diaryName;
    }
}
