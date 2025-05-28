package com.urlive.domain.userUrl;

import com.urlive.domain.url.Url;
import com.urlive.domain.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserUrlService {

    @Autowired
    public UserUrlService(UserUrlRepository userUrlRepository) {
        this.userUrlRepository = userUrlRepository;
    }

    private final UserUrlRepository userUrlRepository;

    @Transactional
    public UserUrl saveUserUrl(UserUrl userUrl) {
        return userUrlRepository.save(userUrl);
    }

    @Transactional(readOnly = true)
    public Optional<UserUrl> getUserUrl(User user, Url url) {
        return userUrlRepository.findUserUrlByUserAndUrl(user, url);
    }
}
