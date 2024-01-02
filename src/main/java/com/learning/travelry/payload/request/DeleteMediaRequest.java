package com.learning.travelry.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigInteger;

public class DeleteMediaRequest {

    @NotBlank(message = "file name can't be blank")
    private String fileName;

    @NotNull(message = "media id can't be null")
    private BigInteger mediaId;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public BigInteger getMediaId() {
        return mediaId;
    }

    public void setMediaId(BigInteger mediaId) {
        this.mediaId = mediaId;
    }
}
