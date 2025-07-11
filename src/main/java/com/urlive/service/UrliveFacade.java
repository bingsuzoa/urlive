package com.urlive.service;

import com.urlive.domain.infrastructure.log.LogService;
import com.urlive.domain.url.Url;
import com.urlive.domain.url.UrlService;
import com.urlive.domain.user.User;
import com.urlive.domain.user.UserService;
import com.urlive.domain.user.option.country.CountryService;
import com.urlive.domain.userUrl.UserUrlService;
import com.urlive.web.dto.domain.common.DtoFactory;
import com.urlive.web.dto.domain.url.UrlCreateRequest;
import com.urlive.web.dto.domain.user.PasswordChangeRequest;
import com.urlive.web.dto.domain.user.UserCreateRequest;
import com.urlive.web.dto.domain.user.UserLoginRequest;
import com.urlive.web.dto.domain.user.UserResponse;
import com.urlive.web.dto.domain.user.country.CountryDto;
import com.urlive.web.dto.domain.userUrl.UpdateTitleRequest;
import com.urlive.web.dto.domain.userUrl.UserUrlResponse;
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
            UserUrlService userUrlService,
            LogService logService,
            CountryService countryService
    ) {
        this.userService = userService;
        this.urlService = urlService;
        this.userUrlService = userUrlService;
        this.logService = logService;
        this.countryService = countryService;
    }

    private UserService userService;
    private UrlService urlService;
    private UserUrlService userUrlService;
    private LogService logService;
    private CountryService countryService;

    public UserResponse saveUser(UserCreateRequest userCreateRequest) {
        return userService.saveUser(userCreateRequest);
    }

    public UserResponse loginUser(UserLoginRequest userLoginRequest) {
        return userService.loginUser(userLoginRequest);
    }

    public UserResponse changePassword(Long id, PasswordChangeRequest passwordChangeRequest) {
        return userService.changePassword(id, passwordChangeRequest);
    }

    @Transactional
    public UserUrlResponse createShortUrl(Long id, UrlCreateRequest urlCreateRequest) {
        Url url = urlService.findOrCreateShortUrl(urlCreateRequest);
        User user = userService.getUserEntityWithUrls(id);
        if (userService.existsOriginalUrlForUser(id, urlCreateRequest)) {
            return DtoFactory.getUserUrlDto(userUrlService.getUserUrl(user, url));
        }
        return DtoFactory.getUserUrlDto(userUrlService.saveUserUrl(user, url));
    }

    public List<UserUrlResponse> getUrlsByUser(Long id) {
        List<UserUrlResponse> urlsOfUser = userService.getUserUrlResponses(id);
        return urlsOfUser;
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

    public List<CountryDto> getCountries() {
        return countryService.getCountries();
    }
}
