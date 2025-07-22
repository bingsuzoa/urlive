package com.urlive.domain.user;


import com.urlive.domain.url.Url;
import com.urlive.domain.user.option.country.Country;
import com.urlive.domain.user.option.country.CountryService;
import com.urlive.domain.user.passwordHistory.PasswordService;
import com.urlive.web.dto.domain.common.DtoFactory;
import com.urlive.web.dto.domain.user.PasswordChangeRequest;
import com.urlive.web.dto.domain.user.UserCreateRequest;
import com.urlive.web.dto.domain.user.UserLoginRequest;
import com.urlive.web.dto.domain.user.UserResponse;
import com.urlive.web.dto.domain.userUrl.UserUrlResponse;
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
            CountryService countryService,
            PasswordService passwordService
    ) {
        this.userRepository = userRepository;
        this.countryService = countryService;
        this.passwordService = passwordService;
    }

    private final UserRepository userRepository;
    private final CountryService countryService;
    private final PasswordService passwordService;

    @Transactional
    public UserResponse saveUser(UserCreateRequest userCreateRequest) {
        String encodedPassword = passwordService.encode(userCreateRequest.password());
        Country country = countryService.findByIsoCode(userCreateRequest.isoCode());
        User user = userRepository.save(userCreateRequest.toEntityWithEncodedPassword(encodedPassword, country));
        return DtoFactory.createUserResponseDto(user, country);
    }

    @Transactional(readOnly = true)
    public boolean isDuplicatePhoneNumber(String phoneNumber) {
        Optional<User> optionalUser = userRepository.findUserByPhoneNumber(phoneNumber);
        if (optionalUser.isPresent()) {
            throw new IllegalArgumentException(User.ALREADY_EXIST_USER);
        }
        return false;
    }

    @Transactional(readOnly = true)
    public UserResponse loginUser(UserLoginRequest userLoginRequest) {
        Optional<User> optionalUser = userRepository.findUserByPhoneNumber(userLoginRequest.phoneNumber());
        if (optionalUser.isEmpty()) {
            throw new IllegalArgumentException(User.NOT_EXIST_USER_ID);
        }
        User user = optionalUser.get();
        if (!passwordService.matches(userLoginRequest.encryptedPassword(), user.getPassword())) {
            throw new IllegalArgumentException(User.NOT_MATCH_PASSWORD);
        }
        return DtoFactory.createUserResponseDto(user);
    }

    @Transactional
    public UserResponse changePassword(Long id, PasswordChangeRequest passwordChangeRequest) {
        Optional<User> optionalUser = userRepository.findUserWithCountry(id);
        if (optionalUser.isEmpty()) {
            throw new IllegalArgumentException(User.NOT_EXIST_USER_ID);
        }
        User user = optionalUser.get();
        String newEncodedPassword = passwordService.changePassword(id, passwordChangeRequest.rawNewPassword());
        user.changePassword(newEncodedPassword);
        return DtoFactory.createUserResponseDto(user);
    }

    @Transactional(readOnly = true)
    public List<UserUrlResponse> getUrls(Long id) {
        User user = getUserEntityWithUrls(id);
        Set<Url> urls = user.getUrls();
        return DtoFactory.getBoardDto(urls);
    }

    public User getUserEntityWithUrls(Long id) {
        Optional<User> optionalUser = userRepository.findUserWithUrlsById(id);
        if (optionalUser.isEmpty()) {
            throw new IllegalArgumentException(User.NOT_EXIST_USER_ID);
        }
        return optionalUser.get();
    }
}
