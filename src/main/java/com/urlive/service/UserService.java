package com.urlive.service;


import com.urlive.domain.user.User;
import com.urlive.domain.user.UserRepository;
import com.urlive.domain.user.option.country.Country;
import com.urlive.domain.userUrl.UserUrl;
import com.urlive.domain.userUrl.UserUrlRepository;
import com.urlive.web.dto.common.DtoFactory;
import com.urlive.web.dto.url.UrlCreateRequest;
import com.urlive.web.dto.user.PasswordChangeRequest;
import com.urlive.web.dto.user.UserCreateRequest;
import com.urlive.web.dto.user.UserLoginRequest;
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
            UserUrlRepository userUrlRepository,
            CountryService countryService,
            PasswordService passwordService
    ) {
        this.userRepository = userRepository;
        this.userUrlRepository = userUrlRepository;
        this.countryService = countryService;
        this.passwordService = passwordService;
    }

    private final UserRepository userRepository;
    private final UserUrlRepository userUrlRepository;
    private final CountryService countryService;
    private final PasswordService passwordService;

    @Transactional
    public UserResponse saveUser(UserCreateRequest userCreateRequest) {
        String encodedPassword = passwordService.encode(userCreateRequest.password());
        Country country = countryService.findByIsoCode(userCreateRequest.isoCode());
        User user = userRepository.save(userCreateRequest.toEntityWithEncodedPassword(encodedPassword, country));
        return DtoFactory.createUserResponseDto(user);
    }

    @Transactional(readOnly = true)
    public UserResponse loginUser(UserLoginRequest userLoginRequest) {
        Optional<User> optionalUser = userRepository.findUserByPhoneNumber(userLoginRequest.phoneNumber());
        if(optionalUser.isEmpty()) {
            throw new IllegalArgumentException(User.NOT_EXIST_USER_ID);
        }
        User user = optionalUser.get();
        if(!passwordService.matches(userLoginRequest.password(), user.getPassword())) {
            throw new IllegalArgumentException(User.NOT_MATCH_PASSWORD);
        }
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
    public List<UserUrlResponse> getUserUrlResponses(Long id) {
        List<UserUrl> userUrls = getUserUrls(id);
        return DtoFactory.getBoardDto(userUrls);
    }

    private List<UserUrl> getUserUrls(Long id) {
        return userUrlRepository.findUserUrls(id);
    }

    public boolean existsOriginalUrlForUser(Long id, UrlCreateRequest urlCreateRequest) {
        List<UserUrl> userUrls = getUserUrls(id);
        for (UserUrl url : userUrls) {
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
