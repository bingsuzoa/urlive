package com.urlive.domain.userUrl;

import com.urlive.domain.url.Url;
import com.urlive.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserUrlRepository extends JpaRepository<UserUrl, Long> {

    Optional<UserUrl> findUserUrlByUserAndUrl(User user, Url url);
}
