package com.urlive.controller;

import com.urlive.controller.dto.url.UrlCreateRequest;
import com.urlive.controller.dto.user.UserCreateRequest;
import com.urlive.controller.dto.user.UserResponse;
import com.urlive.controller.dto.userUrl.UpdateTitleRequest;
import com.urlive.controller.dto.userUrl.UserUrlResponse;
import com.urlive.controller.response.ApiResponse;
import com.urlive.controller.response.ApiResponseBuilder;
import com.urlive.controller.response.ResponseMessage;
import com.urlive.domain.user.UserService;
import com.urlive.domain.userUrl.UserUrlService;
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

    @PostMapping("/user-url/{id}")
    public ResponseEntity<ApiResponse<UserUrlResponse>> createShortUrl(@PathVariable Long id, @RequestBody @Valid UrlCreateRequest urlCreateRequest) {
        return apiResponseBuilder.ok(ResponseMessage.SHORT_KEY_CREATE_SUCCESS, userService.createShortUrl(id, urlCreateRequest));
    }

    @GetMapping("/user-url/{id}")
    public ResponseEntity<ApiResponse<List<UserUrlResponse>>> getUrlsByUserId(@PathVariable Long id) {
        return apiResponseBuilder.ok(ResponseMessage.URLS_VIEW_SUCCESS, userService.getUrlsByUserId(id));
    }

    @PatchMapping("user-url/{id}")
    public ResponseEntity<ApiResponse<UserUrlResponse>> updateTitle(@PathVariable Long id, @RequestBody @Valid UpdateTitleRequest updateTitleRequest) {
        return apiResponseBuilder.ok(ResponseMessage.URL_TITLE_UPDATE_SUCCESS, userUrlService.updateTitle(id, updateTitleRequest));
    }

    @DeleteMapping("user-url/{id}")
    public ResponseEntity<ApiResponse<UserUrlResponse>> deleteUserUrl(@PathVariable Long id) {
        return apiResponseBuilder.ok(ResponseMessage.USER_URL_DELETE_SUCCESS, userUrlService.deleteUserUrl(id));
    }

}
