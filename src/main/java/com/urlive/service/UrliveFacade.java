package com.urlive.service;

import com.urlive.domain.url.Url;
import com.urlive.domain.user.User;
import com.urlive.web.dto.common.DtoFactory;
import com.urlive.web.dto.url.UrlCreateRequest;
import com.urlive.web.dto.user.PasswordChangeRequest;
import com.urlive.web.dto.user.UserCreateRequest;
import com.urlive.web.dto.user.UserResponse;
import com.urlive.web.dto.userUrl.UpdateTitleRequest;
import com.urlive.web.dto.userUrl.UserUrlResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UrliveFacade {

    protected UrliveFacade() {

    }

    @Autowired
    private UrliveFacade(
            UserService userService,
            UrlService urlService,
            UserUrlService userUrlService
    ) {
        this.userService = userService;
        this.urlService = urlService;
        this.userUrlService = userUrlService;
    }

    private UserService userService;
    private UrlService urlService;
    private UserUrlService userUrlService;

    public UserResponse saveUser(UserCreateRequest userCreateRequest) {
        return userService.saveUser(userCreateRequest);
    }

    public UserResponse changePassword(Long id, PasswordChangeRequest passwordChangeRequest) {
        return userService.changePassword(id, passwordChangeRequest);
    }

    @Transactional
    public UserUrlResponse createShortUrl(Long id, UrlCreateRequest urlCreateRequest) {
        Url url = urlService.findOrCreateShortUrl(urlCreateRequest);
        User user = userService.getUserEntityWithoutUrls(id);
        if (userService.isExistingUrlOfUser(id, urlCreateRequest)) {
            return DtoFactory.getUserUrlDto(userUrlService.getUserUrl(user, url));
        }
        return DtoFactory.getUserUrlDto(userUrlService.saveUserUrl(user, url));
    }

    public List<UserUrlResponse> getUrlsByUser(Long id) {
        return userService.getUserUrlResponses(id);
    }

    @Transactional
    public String decodeShortUrl(String shortUrl) {
        return urlService.decodeShortUrl(shortUrl);
    }

    public UserUrlResponse updateTitle(Long id, UpdateTitleRequest updateTitleRequest) {
        return userUrlService.updateTitle(id, updateTitleRequest);
    }

    public UserUrlResponse deleteUserUrl(Long id) {
        return userUrlService.deleteUserUrl(id);
    }
}
