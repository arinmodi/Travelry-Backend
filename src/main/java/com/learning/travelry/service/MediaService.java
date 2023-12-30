package com.learning.travelry.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public interface MediaService {

    List<String[]> uploadFiles(MultipartFile[] multipartFiles, String diaryName) throws ExecutionException, InterruptedException;

    int deleteFile(String fileName);
}
