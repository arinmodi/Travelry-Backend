package com.learning.travelry.payload.request;

import jakarta.validation.constraints.NotBlank;

public class DeleteMediaRequest {

    @NotBlank(message = "file name can't be blank")
    private String fileName;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

}
