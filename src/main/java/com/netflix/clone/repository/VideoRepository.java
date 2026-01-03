package com.netflix.clone.repository;

import com.netflix.clone.models.Video;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VideoRepository extends JpaRepository<Video, Long> {

    @Query("SELECT u FROM Video u WHERE LOWER(u.title) LIKE LOWER(CONCAT('%',:search,'%')) OR LOWER(u.description) LIKE LOWER(CONCAT('%',:search,'%'))")
    Page<Video> searchVideos(@Param("search") String search, Pageable pageable);

    @Query("SELECT COUNT(v) FROM Video v WHERE v.published = true")
    long countPublishedVideos();

    @Query("SELECT COALESCE(SUM(v.duration), 0) FROM Video v")
    long getTotalDuration();

    @Query("SELECT v FROM Video v WHERE v.published = true AND (LOWER(v.title) LIKE LOWER(CONCAT('%',:search,'%')) OR LOWER(v.description) LIKE LOWER(CONCAT('%',:search,'%'))) ORDER BY v.createdAt DESC")
    Page<Video> searchPublishedVideos(String trim, Pageable pageable);

    @Query("SELECT v FROM Video v WHERE v.published = true ORDER BY v.createdAt DESC")
    Page<Video> findPublishedVideos(Pageable pageable);

    @Query("SELECT v FROM Video v WHERE v.published = true ORDER BY random()")
    List<Video> findRandomPublishedVideos(Pageable pageable);

}
