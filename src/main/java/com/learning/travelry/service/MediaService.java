package com.learning.travelry.service;

import com.learning.travelry.entities.Media;
import com.learning.travelry.entities.PublicMedia;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public interface MediaService {

    List<String[]> uploadFiles(MultipartFile[] multipartFiles, String diaryName) throws ExecutionException, InterruptedException;

    int deleteFile(String fileName, BigInteger id);

    boolean saveAll(List<Media> medias);

    Media getMediaById(BigInteger id);

    List<PublicMedia> getMedia(BigInteger id, String sort, BigInteger limit, BigInteger offset);
}
