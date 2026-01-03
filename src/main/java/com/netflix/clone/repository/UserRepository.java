package com.netflix.clone.repository;

import com.netflix.clone.helper.enums.Roles;
import com.netflix.clone.models.User;
import com.netflix.clone.models.Video;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    Optional<User> findByVerificationToken(String token);

    Optional<User> findByPasswordResetToken(String passwordResetToken);

    long countByRoleAndActive(Roles roles, boolean active);

    long countByRole(Roles roles);

    @Query("SELECT u FROM User u WHERE LOWER(u.fullName) LIKE LOWER(CONCAT('%',:search,'%')) OR LOWER(u.email) LIKE LOWER(CONCAT('%',:search,'%'))")
    Page<User> searchUsers(@Param("search") String search, Pageable pageable);

    @Query("SELECT v.id FROM User u JOIN u.watchList v WHERE u.email =:email AND v.id IN :videoIds")
    Set<Long> findWatchListVideoIds(@Param("email") String email, @Param("videoIds") List<Long> videoIds);

    @Query("SELECT v FROM User u JOIN u.watchList v " +
            "WHERE u.id =:userId AND v.published = true AND (" +
            "LOWER(v.title) LIKE LOWER(CONCAT('%',:search,'%')) OR " +
            "LOWER(v.description) LIKE LOWER(CONCAT('%',:search,'%')))")
    Page<Video> searchWatchListByUserId(@Param("userId") Long userId, @Param("search") String search, Pageable pageable);

    @Query("SELECT v FROM User u JOIN u.watchList v WHERE u.id =:userId AND v.published = true")
    Page<Video> findWatchListByUserId(@Param("userId") Long userId, Pageable pageable);
}
