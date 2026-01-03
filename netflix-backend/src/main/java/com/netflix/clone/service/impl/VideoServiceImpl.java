package com.netflix.clone.service.impl;

import com.netflix.clone.dto.request.VideoRequest;
import com.netflix.clone.dto.response.MessageResponse;
import com.netflix.clone.dto.response.PageResponse;
import com.netflix.clone.dto.response.VideoResponse;
import com.netflix.clone.dto.response.VideoStatsResponse;
import com.netflix.clone.models.Video;
import com.netflix.clone.repository.UserRepository;
import com.netflix.clone.repository.VideoRepository;
import com.netflix.clone.service.interfaces.IVideoService;
import com.netflix.clone.utils.service.PaginationUtils;
import com.netflix.clone.utils.service.ServiceUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class VideoServiceImpl implements IVideoService {

    private final VideoRepository videoRepository;
    private final UserRepository userRepository;
    private final ServiceUtils serviceUtils;

    @Override
    public MessageResponse createVideoByAdmin(VideoRequest videoRequest) {
        Video video = new Video();
        video.setTitle(videoRequest.getTitle());
        video.setDescription(videoRequest.getDescription());
        video.setYear(videoRequest.getYear());
        video.setRating(videoRequest.getRating());
        video.setDuration(videoRequest.getDuration());
        video.setSrcUuid(videoRequest.getSrc());
        video.setPosterUuid(videoRequest.getPoster());
        video.setPublished(videoRequest.isPublished());
        video.setCategories(videoRequest.getCategories() != null ? videoRequest.getCategories() : List.of());
        videoRepository.save(video);
        return new MessageResponse("Video created successfully!");
    }

    @Override
    public PageResponse<VideoResponse> getAllVideosByAdmin(int page, int size, String search) {
        Pageable pageable = PaginationUtils.createPageRequest(page, size, "id");
        Page<Video> videoPage;

        if (search != null && !search.trim().isEmpty()) {
            videoPage = videoRepository.searchVideos(search.trim(), pageable);
        } else {
            videoPage = videoRepository.findAll(pageable);
        }

        return PaginationUtils.toPageResponse(videoPage, VideoResponse::fromEntity);
    }

    @Override
    public MessageResponse updateVideoByAdmin(Long id, VideoRequest videoRequest) {
        Video video = new Video();
        video.setId(id);
        video.setTitle(videoRequest.getTitle());
        video.setDescription(videoRequest.getDescription());
        video.setYear(videoRequest.getYear());
        video.setRating(videoRequest.getRating());
        video.setDuration(videoRequest.getDuration());
        video.setSrcUuid(videoRequest.getSrc());
        video.setPosterUuid(videoRequest.getPoster());
        video.setPublished(videoRequest.isPublished());
        video.setCategories(videoRequest.getCategories() != null ? videoRequest.getCategories() : List.of());
        videoRepository.save(video);
        return new MessageResponse("Video updated successfully!");
    }

    @Override
    public MessageResponse deleteVideoByAdmin(Long id) {
        if (!videoRepository.existsById(id))
            throw new RuntimeException("Video not found!");

        videoRepository.deleteById(id);
        return new MessageResponse("Video deleted successfully!");
    }

    @Override
    public MessageResponse toggleVideoPublishStatusByAdmin(Long id, boolean value) {
        Video video = serviceUtils.getVideoByIdOrThrow(id);
        video.setPublished(value);
        videoRepository.save(video);
        return new MessageResponse("Video publish status changed successfully!");
    }

    @Override
    public VideoStatsResponse getVideoStatsByAdmin() {
        long totalVideo = videoRepository.count();
        long publishedVideos = videoRepository.countPublishedVideos();
        long totalDuration = videoRepository.getTotalDuration();
        return new VideoStatsResponse(totalVideo, publishedVideos, totalDuration);
    }

    @Override
    public PageResponse<VideoResponse> getPublishedVideos(int page, int size, String search, String email) {
        Pageable pageable = PaginationUtils.createPageRequest(page, size, "id");
        Page<Video> videoPage;

        if (search != null && !search.trim().isEmpty()) {
            videoPage = videoRepository.searchPublishedVideos(search.trim(), pageable);
        } else {
            videoPage = videoRepository.findPublishedVideos(pageable);
        }

        List<Video> videos = videoPage.getContent();
        Set<Long> watchListIds = Set.of();

        if (!videos.isEmpty()) {
            try {
                List<Long> videoIds = videos.stream().map(Video::getId).toList();
                watchListIds = userRepository.findWatchListVideoIds(email, videoIds);
            } catch (Exception e) {
                watchListIds = Set.of();
            }
        }

        Set<Long> finalWatchListIds = watchListIds;
        videos.forEach(video -> video.setIsInWatchList(finalWatchListIds.contains(video.getId())));
        List<VideoResponse> videoResponses = videos.stream().map(VideoResponse::fromEntity).toList();
        return PaginationUtils.toPageResponseForVid(videoPage, videoResponses);
    }

    @Override
    public List<VideoResponse> getFeaturesVideos() {
        Pageable pageable = PageRequest.of(0, 5);
        List<Video> videos = videoRepository.findRandomPublishedVideos(pageable);
        return videos.stream().map(VideoResponse::fromEntity).toList();
    }

}
