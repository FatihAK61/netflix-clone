package com.netflix.clone.service.interfaces;

import com.netflix.clone.dto.request.VideoRequest;
import com.netflix.clone.dto.response.MessageResponse;
import com.netflix.clone.dto.response.PageResponse;
import com.netflix.clone.dto.response.VideoResponse;
import com.netflix.clone.dto.response.VideoStatsResponse;
import jakarta.validation.Valid;

import java.util.List;

public interface IVideoService {

    MessageResponse createVideoByAdmin(@Valid VideoRequest videoRequest);

    PageResponse<VideoResponse> getAllVideosByAdmin(int page, int size, String search);

    MessageResponse updateVideoByAdmin(Long id, @Valid VideoRequest videoRequest);

    MessageResponse deleteVideoByAdmin(Long id);

    MessageResponse toggleVideoPublishStatusByAdmin(Long id, boolean value);

    VideoStatsResponse getVideoStatsByAdmin();

    PageResponse<VideoResponse> getPublishedVideos(int page, int size, String search, String currentUserEmail);

    List<VideoResponse> getFeaturesVideos();
    
}
