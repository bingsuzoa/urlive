package com.urlive.service;

import com.urlive.web.dto.common.DtoFactory;
import com.urlive.web.dto.userUrl.UpdateTitleRequest;
import com.urlive.web.dto.userUrl.UserUrlResponse;
import com.urlive.domain.url.Url;
import com.urlive.domain.user.User;
import com.urlive.domain.userUrl.UserUrl;
import com.urlive.domain.userUrl.UserUrlRepository;
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

    @Transactional
    public UserUrlResponse updateTitle(Long id, UpdateTitleRequest updateTitleRequest) {
        Optional<UserUrl> optionalUserUrl = userUrlRepository.findById(id);
        if(optionalUserUrl.isEmpty()) {
            throw new IllegalArgumentException(UserUrl.INVALID_USER_URL);
        }
        UserUrl userUrl = optionalUserUrl.get();
        userUrl.updateTitle(updateTitleRequest.newTitle());
        return DtoFactory.getUserUrlDto(userUrl);
    }

    @Transactional
    public UserUrlResponse deleteUserUrl(Long id) {
        Optional<UserUrl> optionalUserUrl = userUrlRepository.findById(id);
        if(optionalUserUrl.isEmpty()) {
            throw new IllegalArgumentException(UserUrl.INVALID_USER_URL);
        }
        UserUrl userUrl = optionalUserUrl.get();
        userUrlRepository.delete(userUrl);
        return DtoFactory.getUserUrlDto(userUrl);
    }
}
