package com.netflix.clone.controller;

import com.netflix.clone.dto.response.MessageResponse;
import com.netflix.clone.dto.response.PageResponse;
import com.netflix.clone.dto.response.VideoResponse;
import com.netflix.clone.service.interfaces.IWatchListService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/watch-list")
@RequiredArgsConstructor
public class WatchListController {

    private final IWatchListService watchListService;

    @PostMapping("/add/{videoId}")
    public ResponseEntity<MessageResponse> addVideoToWatchList(@PathVariable Long videoId, Authentication authentication) {
        String currentUserEmail = authentication.getName();
        return ResponseEntity.ok(watchListService.addVideoToWatchList(currentUserEmail, videoId));
    }

    @DeleteMapping("/delete/{videoId}")
    public ResponseEntity<MessageResponse> removeFromWatchList(@PathVariable Long videoId, Authentication authentication) {
        String currentUserMail = authentication.getName();
        return ResponseEntity.ok(watchListService.removeFromWatchList(currentUserMail, videoId));
    }

    @GetMapping("/getWatchList")
    public ResponseEntity<PageResponse<VideoResponse>> getWatchList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search,
            Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(watchListService.getWatchListPaginated(email, page, size, search));
    }

}
