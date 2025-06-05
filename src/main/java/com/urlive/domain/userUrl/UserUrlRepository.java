package com.urlive.domain.userUrl;

import com.urlive.domain.url.Url;
import com.urlive.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserUrlRepository extends JpaRepository<UserUrl, Long> {

    Optional<UserUrl> findUserUrlByUserAndUrl(User user, Url url);

    @Query("""
            select uu from UserUrl uu
            join fetch uu.url
            where uu.user.id = :userId
            """)
    List<UserUrl> findUserUrls(@Param("userId") Long id);
}
