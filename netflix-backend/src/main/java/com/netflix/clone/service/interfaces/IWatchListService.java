package com.netflix.clone.service.interfaces;

import com.netflix.clone.dto.response.MessageResponse;
import com.netflix.clone.dto.response.PageResponse;
import com.netflix.clone.dto.response.VideoResponse;

public interface IWatchListService {

    MessageResponse addVideoToWatchList(String currentUserEmail, Long videoId);

    MessageResponse removeFromWatchList(String currentUserMail, Long videoId);

    PageResponse<VideoResponse> getWatchListPaginated(String email, int page, int size, String search);

}
