package com.learning.travelry.payload.response;

public class MediaUploadResponse {
    private String viewUrl;
    private String fileName;

    public MediaUploadResponse(String viewUrl, String fileName) {
        this.viewUrl = viewUrl;
        this.fileName = fileName;
    }

    public String getViewUrl() {
        return viewUrl;
    }

    public void setViewUrl(String viewUrl) {
        this.viewUrl = viewUrl;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
