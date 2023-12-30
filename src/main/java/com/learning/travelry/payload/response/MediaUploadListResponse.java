package com.learning.travelry.payload.response;

import java.util.List;

public class MediaUploadListResponse {

    private List<MediaUploadResponse> uploadedMedias;

    public MediaUploadListResponse(List<MediaUploadResponse> uploadedMedias) {
        this.uploadedMedias = uploadedMedias;
    }

    public List<MediaUploadResponse> getUploadedMedias() {
        return uploadedMedias;
    }

    public void setUploadedMedias(List<MediaUploadResponse> uploadedMedias) {
        this.uploadedMedias = uploadedMedias;
    }
}
