package com.netflix.clone.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class VideoRequest {

    @NotBlank(message = "Title cannot be blank!")
    private String title;

    @Size(max = 4000, message = "Description cannot be more than 4000 characters!")
    private String description;

    private Integer year;
    private String rating;
    private Integer duration;
    private String src;
    private String poster;
    private boolean published;
    private List<String> categories;
    
}
