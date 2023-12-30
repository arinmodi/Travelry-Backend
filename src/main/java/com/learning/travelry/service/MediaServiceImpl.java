package com.learning.travelry.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.learning.travelry.utils.AsyncFileUpload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
@Slf4j
@RequiredArgsConstructor
public class MediaServiceImpl implements MediaService {

    @Value("${aws.bucket.name}")
    private String bucketName;

    private final AmazonS3 s3Client;

    private final AsyncFileUpload asyncFileUpload;

    @Override
    public List<String[]> uploadFiles(MultipartFile[] multipartFiles, String folderName)
            throws ExecutionException, InterruptedException {

        List<String[]> response = new ArrayList<>();
        List<CompletableFuture<String[]>> futures = new ArrayList<>();

        for (MultipartFile multipartFile : multipartFiles) {
            futures.add(asyncFileUpload.uploadFileAsync(multipartFile, folderName));
        }

        for (CompletableFuture<String[]> future : futures) {
            future.join();
            response.add(future.get());
        }

        return response;
    }

    @Override
    public int deleteFile(String fileName) {
        DeleteObjectRequest request = new DeleteObjectRequest(bucketName, fileName);
        try {
            if (s3Client.doesObjectExist(bucketName, fileName)) {
                s3Client.deleteObject(request);
            } else {
                return 0;
            }
        } catch(Exception e) {
            System.out.println(e.getMessage());
            return -1;
        }
        return 1;
    }

}
