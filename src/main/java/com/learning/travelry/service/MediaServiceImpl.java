package com.learning.travelry.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.learning.travelry.entities.Media;
import com.learning.travelry.entities.PublicMedia;
import com.learning.travelry.repository.MediaRepository;
import com.learning.travelry.utils.AsyncFileUpload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
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

    @Autowired
    private MediaRepository mediaRepository;

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

    @Override
    public boolean saveAll(List<Media> medias) {
        try {
            mediaRepository.saveAll(medias);
            return true;
        }catch (Exception e) {
            return false;
        }
    }

    @Override
    public Media getMediaById(BigInteger id) {
        return mediaRepository.findMediaById(id);
    }

    @Override
    public List<PublicMedia> getMedia(BigInteger id, String sort, BigInteger limit, BigInteger offset) {
        return mediaRepository.getMedia(id, sort, limit, offset);
    }

}
