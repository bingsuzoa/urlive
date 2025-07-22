package com.urlive.web;

import com.urlive.global.responseFormat.ApiResponse;
import com.urlive.global.responseFormat.ApiResponseBuilder;
import com.urlive.global.responseFormat.ResponseMessage;
import com.urlive.service.UrliveFacade;
import com.urlive.web.dto.domain.url.UrlCreateRequest;
import com.urlive.web.dto.domain.user.PasswordChangeRequest;
import com.urlive.web.dto.domain.user.UserCreateRequest;
import com.urlive.web.dto.domain.user.UserLoginRequest;
import com.urlive.web.dto.domain.user.UserResponse;
import com.urlive.web.dto.domain.user.countryDto.CountryDto;
import com.urlive.web.dto.domain.userUrl.UpdateTitleRequest;
import com.urlive.web.dto.domain.userUrl.UserUrlResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UrliveController {

    @Autowired
    public UrliveController(
            UrliveFacade urliveFacade,
            ApiResponseBuilder apiResponseBuilder
    ) {
        this.urliveFacade = urliveFacade;
        this.apiResponseBuilder = apiResponseBuilder;
    }

    private final UrliveFacade urliveFacade;
    private final ApiResponseBuilder apiResponseBuilder;

    @PostMapping("/user")
    public ResponseEntity<ApiResponse<UserResponse>> join(@RequestBody @Valid UserCreateRequest userCreateRequest) {
        return apiResponseBuilder.ok(ResponseMessage.USER_CREATE_SUCCESS, urliveFacade.saveUser(userCreateRequest));
    }

    @GetMapping("/user/phoneNumber")
    public ResponseEntity<ApiResponse<Void>> isDuplicatePhoneNumber(@RequestParam("phoneNumber") String phoneNumber) {
        urliveFacade.isDuplicatePhoneNumber(phoneNumber);
        return apiResponseBuilder.ok(ResponseMessage.USER_DUPLICATE_CONFIRM_SUCCESS);
    }

    @PostMapping("/user/login")
    public ResponseEntity<ApiResponse<UserResponse>> login(@RequestBody @Valid UserLoginRequest userLoginRequest) {
        return apiResponseBuilder.ok(ResponseMessage.USER_LOGIN_SUCCESS, urliveFacade.loginUser(userLoginRequest));
    }

    @PatchMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<UserResponse>> changeUserPassword(@PathVariable Long userId,
                                                                        @RequestBody @Valid PasswordChangeRequest passwordChangeRequest) {
        return apiResponseBuilder.ok(ResponseMessage.USER_PASSWORD_CHANGE_SUCCESS, urliveFacade.changePassword(userId, passwordChangeRequest));
    }

    @PostMapping("/users/{userId}/urls")
    public ResponseEntity<ApiResponse<UserUrlResponse>> createShortUrl(@PathVariable Long userId,
                                                                       @RequestBody @Valid UrlCreateRequest urlCreateRequest) {
        return apiResponseBuilder.ok(ResponseMessage.SHORT_URL_CREATE_SUCCESS, urliveFacade.createShortUrl(userId, urlCreateRequest));
    }

    @GetMapping("/users/{userId}/urls")
    public ResponseEntity<ApiResponse<List<UserUrlResponse>>> getUrlsByUserId(@PathVariable Long userId) {
        return apiResponseBuilder.ok(ResponseMessage.URLS_VIEW_SUCCESS, urliveFacade.getUrlsByUser(userId));
    }

    @PatchMapping("/user-urls/{userUrlId}")
    public ResponseEntity<ApiResponse<UserUrlResponse>> updateTitle(@PathVariable Long userUrlId,
                                                                    @RequestBody @Valid UpdateTitleRequest updateTitleRequest) {
        return apiResponseBuilder.ok(ResponseMessage.URL_TITLE_UPDATE_SUCCESS, urliveFacade.updateTitle(userUrlId, updateTitleRequest));
    }

    @DeleteMapping("/user-urls/{userUrlId}")
    public ResponseEntity<ApiResponse<UserUrlResponse>> deleteUserUrl(@PathVariable Long userUrlId) {
        return apiResponseBuilder.ok(ResponseMessage.USER_URL_DELETE_SUCCESS, urliveFacade.deleteUserUrl(userUrlId));
    }

    @GetMapping("/countries")
    public ResponseEntity<ApiResponse<List<CountryDto>>> getCountry() {
        return apiResponseBuilder.ok(ResponseMessage.COUNTRIES_GET_SUCCESS, urliveFacade.getCountries());
    }

}
