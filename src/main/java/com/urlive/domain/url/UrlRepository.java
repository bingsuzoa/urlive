package com.urlive.domain.url;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UrlRepository extends JpaRepository<Url, Long> {

    Optional<Url> findUrlByOriginalUrl(String originalUrl);

    Optional<Url> findUrlByShortUrl(String shortUrl);

    @Query("select u from Url u left join fetch u.views where u.id = :id")
    Optional<Url> findUrlWithViews(@Param("id") Long id);
}
