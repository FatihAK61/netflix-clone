package com.netflix.clone.service.impl;

import com.netflix.clone.service.interfaces.IFileUploadService;
import com.netflix.clone.utils.service.FileHandlerUtil;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileUploadServiceImpl implements IFileUploadService {

    private Path videoStorageLocation;
    private Path imageStorageLocation;

    @Value("${file.upload.video-dir:uploads/videos}")
    private String videoDir;

    @Value("${file.upload.image-dir:uploads/images}")
    private String imageDir;

    @PostConstruct
    public void init() {
        this.videoStorageLocation = Paths.get(videoDir).toAbsolutePath().normalize();
        this.imageStorageLocation = Paths.get(imageDir).toAbsolutePath().normalize();

        try {
            Files.createDirectories(videoStorageLocation);
            Files.createDirectories(imageStorageLocation);
        } catch (Exception ex) {
            throw new RuntimeException("Could not initialize file upload service", ex);
        }
    }

    @Override
    public String storeVideoFile(MultipartFile file) {
        return storeFile(file, videoStorageLocation);
    }

    @Override
    public String storeImageFile(MultipartFile file) {
        return storeFile(file, imageStorageLocation);
    }

    @Override
    public ResponseEntity<Resource> serveVideo(String uuid, String range) {
        try {
            Path filePath = FileHandlerUtil.findFileByUuid(videoStorageLocation, uuid);
            Resource resource = FileHandlerUtil.createFullResource(filePath);

            String fileName = resource.getFilename();
            String contentType = FileHandlerUtil.detectVideoContentType(fileName);
            long fileLength = resource.contentLength();

            if (isFullContentRequest(range))
                return buildFullVideoResponse(resource, contentType, fileName, fileLength);

            return buildPartialVideoResponse(filePath, range, contentType, fileName, fileLength);

        } catch (Exception ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    public ResponseEntity<Resource> serveImage(String uuid) {
        try {
            Path filePath = FileHandlerUtil.findFileByUuid(imageStorageLocation, uuid);
            Resource resource = FileHandlerUtil.createFullResource(filePath);
            String fileName = resource.getFilename();
            String contentType = FileHandlerUtil.detectImageContentType(fileName);

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + fileName + "\"")
                    .body(resource);
            
        } catch (Exception ex) {
            return ResponseEntity.notFound().build();
        }
    }

    private String storeFile(MultipartFile file, Path storageLocation) {
        String fileExtension = FileHandlerUtil.extractFileExtention(file.getOriginalFilename());
        String uuid = UUID.randomUUID().toString();
        String fileName = uuid + fileExtension;

        try {
            if (file.isEmpty())
                throw new RuntimeException("Failed to store empty file " + file.getOriginalFilename());
            Path targetLocation = storageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return uuid;
        } catch (Exception ex) {
            throw new RuntimeException("Could not store file " + file.getOriginalFilename() + ". Please try again!", ex);
        }
    }

    private boolean isFullContentRequest(String rangeHeader) {
        return rangeHeader == null || rangeHeader.isEmpty();
    }

    private ResponseEntity<Resource> buildFullVideoResponse(Resource resource, String contentType, String fileName, long fileLength) {
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + fileName + "\"")
                .header(HttpHeaders.ACCEPT_RANGES, "bytes")
                .header(HttpHeaders.CONTENT_LENGTH, String.valueOf(fileLength))
                .body(resource);
    }

    private ResponseEntity<Resource> buildPartialVideoResponse(Path filePath, String rangeHeader, String contentType, String fileName, long fileLength) throws IOException {
        long[] range = FileHandlerUtil.parseRangeHeader(rangeHeader, fileLength);
        long rangeStart = range[0];
        long rangeEnd = range[1];

        if (!isValidRange(rangeStart, rangeEnd, fileLength))
            return buildRangeNotSatisfiableResponse(fileLength);

        long contentLength = rangeEnd - rangeStart + 1;
        Resource rangeResource = FileHandlerUtil.createRangeResource(filePath, rangeStart, contentLength);

        return ResponseEntity.status(206)
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + fileName + "\"")
                .header(HttpHeaders.ACCEPT_RANGES, "bytes")
                .header(HttpHeaders.CONTENT_LENGTH, String.valueOf(contentLength))
                .header(HttpHeaders.CONTENT_RANGE, "bytes " + rangeStart + "-" + rangeEnd + "/" + fileLength)
                .body(rangeResource);
    }

    private ResponseEntity<Resource> buildRangeNotSatisfiableResponse(long fileLength) {
        return ResponseEntity.status(416)
                .header(HttpHeaders.CONTENT_RANGE, "bytes */" + fileLength)
                .build();
    }

    private boolean isValidRange(long rangeStart, long rangeEnd, long fileLength) {
        return rangeStart <= rangeEnd && rangeEnd >= 0 && rangeEnd < fileLength;
    }

}
