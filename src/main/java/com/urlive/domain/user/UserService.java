package com.urlive.domain.user;


import com.urlive.controller.dto.userUrl.UserUrlResponseDto;
import com.urlive.controller.dto.common.DtoFactory;
import com.urlive.controller.dto.url.UrlCreateRequestDto;
import com.urlive.controller.dto.user.UserCreateRequestDto;
import com.urlive.controller.dto.user.UserResponseDto;
import com.urlive.domain.url.Url;
import com.urlive.domain.url.UrlService;
import com.urlive.domain.userUrl.UserUrl;
import com.urlive.domain.userUrl.UserUrlService;
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
            UserUrlService userUrlService
    ) {
        this.userRepository = userRepository;
        this.urlService = urlService;
        this.userUrlService = userUrlService;
    }

    private final UserRepository userRepository;
    private final UrlService urlService;
    private final UserUrlService userUrlService;

    @Transactional
    public UserResponseDto saveUser(UserCreateRequestDto userCreateRequestDto) {
        User user = userRepository.save(userCreateRequestDto.toEntity());
        return DtoFactory.createUserResponseDto(user);
    }

    @Transactional(readOnly = true)
    public List<UserUrlResponseDto> getUrlsByUserId(Long id) {
        Optional<User> optionalUser = userRepository.findUrlsById(id);
        if (optionalUser.isEmpty()) {
            throw new IllegalArgumentException(User.NOT_EXIST_USER_ID);
        }
        return DtoFactory.getBoardDto(optionalUser.get());
    }

    @Transactional
    public UserUrlResponseDto createShortUrl(Long id, UrlCreateRequestDto urlCreateRequestDto) {
        Optional<User> optionalUser = userRepository.findUrlsById(id);
        if (optionalUser.isEmpty()) {
            throw new IllegalArgumentException(User.NOT_EXIST_USER_ID);
        }
        User user = optionalUser.get();
        Url url = urlService.getShortUrl(urlCreateRequestDto);

        Optional<UserUrl> optionalUserUrl = userUrlService.getUserUrl(user, url);
        if(optionalUserUrl.isEmpty()) {
            UserUrl userUrl = new UserUrl(user, url);
            userUrlService.saveUserUrl(userUrl);
            return DtoFactory.getUserUrlDto(userUrl);
        }
        return DtoFactory.getUserUrlDto(optionalUserUrl.get());
    }
}
