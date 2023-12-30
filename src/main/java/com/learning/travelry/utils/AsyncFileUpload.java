package com.learning.travelry.utils;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@Component
public class AsyncFileUpload {

    private final String bucketName;

    private final AmazonS3 s3Client;

    @Autowired
    public AsyncFileUpload(@Value("${aws.bucket.name}") String bucketName, AmazonS3 s3Client) {
        this.bucketName = bucketName;
        this.s3Client = s3Client;
    }

    private String generateFileName(MultipartFile multiPart) {
       return new Date().getTime() + "-" + Objects.requireNonNull(multiPart.getOriginalFilename()).replace(" ", "_");
    }

    @Async
    public CompletableFuture<String[]> uploadFileAsync(MultipartFile multipartFile, String folderName) {
        try {
            return CompletableFuture.completedFuture(uploadFile(multipartFile, folderName));
        } catch (IOException e) {
            System.out.println("Error uploading file asynchronously: {}" + e.getMessage());
            return CompletableFuture.failedFuture(e);
        }

    }

    private String[] uploadFile(MultipartFile multipartFile, String folderName) throws IOException {
        File file = new File(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
            fileOutputStream.write(multipartFile.getBytes());
        }

        // generate file name
        String fileName = folderName + "/" + generateFileName(multipartFile);

        // upload file
        PutObjectRequest request = new PutObjectRequest(bucketName, fileName, file);
        ObjectMetadata metadata = new ObjectMetadata();
        String extension = FilenameUtils.getExtension(multipartFile.getOriginalFilename());
        metadata.setContentType((extension.equals("mp4") ? "video" : "image") + "/" + extension);
        metadata.addUserMetadata("Title", "File Upload - " + fileName);
        metadata.setContentLength(file.length());
        request.setMetadata(metadata);
        s3Client.putObject(request);

        // view url for rendering
        String viewUrl = s3Client.getUrl(bucketName, fileName).toString();

        // delete file
        System.out.println("local file status : " + file.delete());

        System.out.println(fileName + " completed");
        System.out.println(new Date().getTime());

        return new String[] { fileName, viewUrl };
    }

}
