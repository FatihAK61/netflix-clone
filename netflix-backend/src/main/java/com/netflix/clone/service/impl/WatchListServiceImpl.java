package com.netflix.clone.service.impl;

import com.netflix.clone.dto.response.MessageResponse;
import com.netflix.clone.dto.response.PageResponse;
import com.netflix.clone.dto.response.VideoResponse;
import com.netflix.clone.models.User;
import com.netflix.clone.models.Video;
import com.netflix.clone.repository.UserRepository;
import com.netflix.clone.repository.VideoRepository;
import com.netflix.clone.service.interfaces.IWatchListService;
import com.netflix.clone.utils.service.PaginationUtils;
import com.netflix.clone.utils.service.ServiceUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WatchListServiceImpl implements IWatchListService {

    private final UserRepository userRepository;
    private final VideoRepository videoRepository;
    private final ServiceUtils serviceUtils;

    @Transactional
    @Override
    public MessageResponse addVideoToWatchList(String currentUserEmail, Long videoId) {
        User user = serviceUtils.getUserByEmailOrThrow(currentUserEmail);
        Video video = serviceUtils.getVideoByIdOrThrow(videoId);
        user.addWatchList(video);
        userRepository.save(user);
        return new MessageResponse("Video added to watch list successfully!");
    }

    @Transactional
    @Override
    public MessageResponse removeFromWatchList(String currentUserMail, Long videoId) {
        User user = serviceUtils.getUserByEmailOrThrow(currentUserMail);
        Video video = serviceUtils.getVideoByIdOrThrow(videoId);
        user.removeWatchList(video);
        userRepository.save(user);
        return new MessageResponse("Video removed from watch list successfully!");
    }

    @Override
    public PageResponse<VideoResponse> getWatchListPaginated(String email, int page, int size, String search) {
        User user = serviceUtils.getUserByEmailOrThrow(email);
        Pageable pageable = PaginationUtils.createPageRequest(page, size);
        Page<Video> videoPage;

        if (search != null && !search.trim().isEmpty()) {
            videoPage = userRepository.searchWatchListByUserId(user.getId(), search.trim(), pageable);
        } else {
            videoPage = userRepository.findWatchListByUserId(user.getId(), pageable);
        }

        return PaginationUtils.toPageResponse(videoPage, VideoResponse::fromEntity);
    }
}
