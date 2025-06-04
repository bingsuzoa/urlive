package com.urlive.service;


import com.urlive.domain.url.Url;
import com.urlive.domain.user.User;
import com.urlive.domain.user.UserRepository;
import com.urlive.domain.userUrl.UserUrl;
import com.urlive.web.dto.DecodeUrlResponse;
import com.urlive.web.dto.common.DtoFactory;
import com.urlive.web.dto.url.UrlCreateRequest;
import com.urlive.web.dto.user.PasswordChangeRequest;
import com.urlive.web.dto.user.UserCreateRequest;
import com.urlive.web.dto.user.UserResponse;
import com.urlive.web.dto.userUrl.UserUrlResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    public UserService(
            UserRepository userRepository,
            UrlService urlService,
            UserUrlService userUrlService,
            PasswordService passwordService
    ) {
        this.userRepository = userRepository;
        this.urlService = urlService;
        this.userUrlService = userUrlService;
        this.passwordService = passwordService;
    }

    private final UserRepository userRepository;
    private final UrlService urlService;
    private final UserUrlService userUrlService;
    private final PasswordService passwordService;

    @Transactional
    public UserResponse saveUser(UserCreateRequest userCreateRequest) {
        String encodedPassword = passwordService.encode(userCreateRequest.password());
        User user = userRepository.save(userCreateRequest.toEntityWithEncodedPassword(encodedPassword));
        return DtoFactory.createUserResponseDto(user);
    }

    @Transactional
    public UserResponse changePassword(Long id, PasswordChangeRequest passwordChangeRequest) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()) {
            throw new IllegalArgumentException(User.NOT_EXIST_USER_ID);
        }
        String newEncodedPassword = passwordService.changePassword(id, passwordChangeRequest.rawNewPassword());
        User user = optionalUser.get();
        user.changePassword(newEncodedPassword);
        return DtoFactory.createUserResponseDto(user);
    }

    @Transactional(readOnly = true)
    public List<UserUrlResponse> getUrlsByUserId(Long id) {
        Optional<User> optionalUser = userRepository.findUrlsById(id);
        if (optionalUser.isEmpty()) {
            throw new IllegalArgumentException(User.NOT_EXIST_USER_ID);
        }
        return DtoFactory.getBoardDto(optionalUser.get());
    }

    @Transactional
    public UserUrlResponse createShortUrl(Long id,
                                            UrlCreateRequest urlCreateRequest) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()) {
            throw new IllegalArgumentException(User.NOT_EXIST_USER_ID);
        }
        User user = optionalUser.get();
        Url url = urlService.getShortUrl(urlCreateRequest);

        Optional<UserUrl> optionalUserUrl = userUrlService.getUserUrl(user, url);
        if (optionalUserUrl.isEmpty()) {
            UserUrl userUrl = new UserUrl(user, url);
            userUrlService.saveUserUrl(userUrl);
            return DtoFactory.getUserUrlDto(userUrl);
        }
        return DtoFactory.getUserUrlDto(optionalUserUrl.get());
    }

    public DecodeUrlResponse decodeShortUrl(String shortUrl) {
        Url url = urlService.decodeShortUrl(shortUrl);
        return new DecodeUrlResponse(url.getOriginalUrl());
    }
}
