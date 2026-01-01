package com.netflix.clone.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/file-upload")
@PreAuthorize("hasRole('USER')")
@RequiredArgsConstructor
public class FileUploadController {
}
