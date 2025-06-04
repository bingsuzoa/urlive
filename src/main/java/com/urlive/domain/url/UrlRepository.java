package com.urlive.domain.url;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UrlRepository extends JpaRepository<Url, Long> {

    Optional<Url> findUrlByOriginalUrl(String originalUrl);

    Optional<Url> findUrlByShortUrl(String shortUrl);

    @Modifying
    @Query("update Url u set u.viewCount = u.viewCount + 1 where u.id = :id")
    int increaseViewCount(@Param("id") Long id);
}
