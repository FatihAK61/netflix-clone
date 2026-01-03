package com.netflix.clone.utils.service;

import com.netflix.clone.helper.globalexceptionhandler.ResourceNotFoundException;
import com.netflix.clone.models.User;
import com.netflix.clone.models.Video;
import com.netflix.clone.repository.UserRepository;
import com.netflix.clone.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ServiceUtils {

    private final UserRepository userRepository;
    private final VideoRepository videoRepository;

    public User getUserByEmailOrThrow(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + email));
    }

    public User getUserByIdOrThrow(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + id));
    }

    public Video getVideoByIdOrThrow(Long id) {
        return videoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Video not found: " + id));
    }

}
