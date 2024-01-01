package com.learning.travelry.utils;

import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class FileUtils {

    static List<String> allowedFileExtensions = new ArrayList<>(Arrays.asList("png", "jpg", "jpeg", "mp4"));
    static List<String> allowedImageExtensions = new ArrayList<>(Arrays.asList("png", "jpg", "jpeg"));


    public static boolean isValidFile(MultipartFile multipartFile) {
        if (Objects.isNull(multipartFile.getOriginalFilename())) {
            return false;
        }
        return !multipartFile.getOriginalFilename().trim().equals("");
    }

    public static boolean isFileMedia(MultipartFile multipartFile) {
        return allowedFileExtensions.contains(FilenameUtils.getExtension(
                multipartFile.getOriginalFilename()
        ));
    }

    public static boolean isFileImage(MultipartFile multipartFile) {
        return allowedFileExtensions.contains(FilenameUtils.getExtension(
                multipartFile.getOriginalFilename()
        ));
    }
}
