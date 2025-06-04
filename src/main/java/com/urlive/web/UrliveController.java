package com.urlive.web;

import com.urlive.global.responseFormat.ApiResponse;
import com.urlive.global.responseFormat.ApiResponseBuilder;
import com.urlive.global.responseFormat.ResponseMessage;
import com.urlive.service.UserService;
import com.urlive.service.UserUrlService;
import com.urlive.web.dto.DecodeUrlResponse;
import com.urlive.web.dto.url.UrlCreateRequest;
import com.urlive.web.dto.user.PasswordChangeRequest;
import com.urlive.web.dto.user.UserCreateRequest;
import com.urlive.web.dto.user.UserResponse;
import com.urlive.web.dto.userUrl.UpdateTitleRequest;
import com.urlive.web.dto.userUrl.UserUrlResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UrliveController {

    @Autowired
    public UrliveController(
            UserService userService,
            UserUrlService userUrlService,
            ApiResponseBuilder apiResponseBuilder
    ) {
        this.userService = userService;
        this.userUrlService = userUrlService;
        this.apiResponseBuilder = apiResponseBuilder;
    }

    private final UserService userService;
    private final UserUrlService userUrlService;
    private final ApiResponseBuilder apiResponseBuilder;

    @PostMapping("/user")
    public ResponseEntity<ApiResponse<UserResponse>> join(@RequestBody @Valid UserCreateRequest userCreateRequest) {
        return apiResponseBuilder.created(ResponseMessage.USER_CREATE_SUCCESS, userService.saveUser(userCreateRequest));
    }

    @PostMapping("/user/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> changeUser(@PathVariable Long id, @Valid PasswordChangeRequest passwordChangeRequest) {
        return apiResponseBuilder.ok(ResponseMessage.USER_PASSWORD_CHANGE_SUCCESS, userService.changePassword(id, passwordChangeRequest));
    }

    @PostMapping("/user-url/{id}")
    public ResponseEntity<ApiResponse<UserUrlResponse>> createShortUrl(@PathVariable Long id,
                                                                       @RequestBody @Valid UrlCreateRequest urlCreateRequest) {
        return apiResponseBuilder.ok(ResponseMessage.SHORT_URL_CREATE_SUCCESS, userService.createShortUrl(id, urlCreateRequest));
    }

    @GetMapping("/user-url/{id}")
    public ResponseEntity<ApiResponse<List<UserUrlResponse>>> getUrlsByUserId(@PathVariable Long id) {
        return apiResponseBuilder.ok(ResponseMessage.URLS_VIEW_SUCCESS, userService.getUrlsByUserId(id));
    }

    @GetMapping("/url/{short-url}")
    public ResponseEntity<ApiResponse<DecodeUrlResponse>> decodeShortUrl(@PathVariable(name = "short-url") String shortUrl) {
        return apiResponseBuilder.ok(ResponseMessage.SHORT_URL_DECODE_SUCCESS, userService.decodeShortUrl(shortUrl));
    }

    @PatchMapping("user-url/{id}")
    public ResponseEntity<ApiResponse<UserUrlResponse>> updateTitle(@PathVariable Long id,
                                                                    @RequestBody @Valid UpdateTitleRequest updateTitleRequest) {
        return apiResponseBuilder.ok(ResponseMessage.URL_TITLE_UPDATE_SUCCESS, userUrlService.updateTitle(id, updateTitleRequest));
    }

    @DeleteMapping("user-url/{id}")
    public ResponseEntity<ApiResponse<UserUrlResponse>> deleteUserUrl(@PathVariable Long id) {
        return apiResponseBuilder.ok(ResponseMessage.USER_URL_DELETE_SUCCESS, userUrlService.deleteUserUrl(id));
    }

}
