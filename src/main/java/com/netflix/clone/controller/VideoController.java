package com.netflix.clone.controller;

import com.netflix.clone.dto.request.VideoRequest;
import com.netflix.clone.dto.response.MessageResponse;
import com.netflix.clone.dto.response.PageResponse;
import com.netflix.clone.dto.response.VideoResponse;
import com.netflix.clone.dto.response.VideoStatsResponse;
import com.netflix.clone.service.interfaces.IVideoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/videos")
@RequiredArgsConstructor
public class VideoController {

    private final IVideoService videoService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin")
    public ResponseEntity<MessageResponse> createVideoByAdmin(@Valid @RequestBody VideoRequest videoRequest) {
        return ResponseEntity.ok(videoService.createVideoByAdmin(videoRequest));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin")
    public ResponseEntity<PageResponse<VideoResponse>> getAllVideosByAdmin(
            @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @RequestParam(required = false) String search) {
        return ResponseEntity.ok(videoService.getAllVideosByAdmin(page, size, search));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/admin/{id}")
    public ResponseEntity<MessageResponse> updateVideoByAdmin(@PathVariable Long id, @Valid @RequestBody VideoRequest videoRequest) {
        return ResponseEntity.ok(videoService.updateVideoByAdmin(id, videoRequest));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/admin/{id}")
    public ResponseEntity<MessageResponse> deleteVideoByAdmin(@PathVariable Long id) {
        return ResponseEntity.ok(videoService.deleteVideoByAdmin(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/admin/{id}/publish")
    public ResponseEntity<MessageResponse> toggleVideoPublishStatusByAdmin(@PathVariable Long id, @RequestParam boolean value) {
        return ResponseEntity.ok(videoService.toggleVideoPublishStatusByAdmin(id, value));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/stats")
    public ResponseEntity<VideoStatsResponse> getVideoStatsByAdmin() {
        return ResponseEntity.ok(videoService.getVideoStatsByAdmin());
    }

    @GetMapping("/published")
    public ResponseEntity<PageResponse<VideoResponse>> getPublishedVideos(
            @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search, Authentication authentication) {
        String currentUserEmail = authentication.getName();
        return ResponseEntity.ok(videoService.getPublishedVideos(page, size, search, currentUserEmail));
    }

    @GetMapping("/featured")
    public ResponseEntity<List<VideoResponse>> getFeaturedVideos() {
        return ResponseEntity.ok(videoService.getFeaturesVideos());
    }

}
