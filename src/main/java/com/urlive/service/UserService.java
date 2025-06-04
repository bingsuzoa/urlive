package com.urlive.service;


import com.urlive.domain.user.User;
import com.urlive.domain.user.UserRepository;
import com.urlive.domain.userUrl.UserUrl;
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
import java.util.Set;

@Service
public class UserService {

    @Autowired
    public UserService(
            UserRepository userRepository,
            PasswordService passwordService
    ) {
        this.userRepository = userRepository;
        this.passwordService = passwordService;
    }

    private final UserRepository userRepository;
    private final PasswordService passwordService;

    @Transactional
    public UserResponse saveUser(UserCreateRequest userCreateRequest) {
        String encodedPassword = passwordService.encode(userCreateRequest.password());
        User user = userRepository.save(userCreateRequest.toEntityWithEncodedPassword(encodedPassword));
        return DtoFactory.createUserResponseDto(user);
    }

    @Transactional
    public UserResponse changePassword(Long id, PasswordChangeRequest passwordChangeRequest) {
        User user = getUserEntityWithoutUrls(id);
        String newEncodedPassword = passwordService.changePassword(id, passwordChangeRequest.rawNewPassword());
        user.changePassword(newEncodedPassword);
        return DtoFactory.createUserResponseDto(user);
    }

    @Transactional(readOnly = true)
    public List<UserUrlResponse> getUrlsByUser(Long id) {
        User user = getUserEntityWithUrls(id);
        return DtoFactory.getBoardDto(user);
    }

    public boolean isExistingUrlOfUser(Long id, UrlCreateRequest urlCreateRequest) {
        User user = getUserEntityWithUrls(id);
        Set<UserUrl> urls = user.getUrls();
        for (UserUrl url : urls) {
            if (url.getOriginalUrl().equals(urlCreateRequest.originalUrl())) {
                return true;
            }
        }
        return false;
    }

    public User getUserEntityWithoutUrls(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()) {
            throw new IllegalArgumentException(User.NOT_EXIST_USER_ID);
        }
        return optionalUser.get();
    }

    public User getUserEntityWithUrls(Long id) {
        Optional<User> optionalUser = userRepository.findUserWithUrlsById(id);
        if (optionalUser.isEmpty()) {
            throw new IllegalArgumentException(User.NOT_EXIST_USER_ID);
        }
        return optionalUser.get();
    }
}
