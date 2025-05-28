package com.urlive.controller;

import com.urlive.controller.dto.userUrl.UserUrlResponseDto;
import com.urlive.controller.dto.url.UrlCreateRequestDto;
import com.urlive.controller.dto.user.UserCreateRequestDto;
import com.urlive.controller.dto.user.UserResponseDto;
import com.urlive.controller.response.ApiResponse;
import com.urlive.controller.response.ApiResponseBuilder;
import com.urlive.controller.response.ResponseMessage;
import com.urlive.domain.user.UserService;
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
            ApiResponseBuilder apiResponseBuilder
    ) {
        this.userService = userService;
        this.apiResponseBuilder = apiResponseBuilder;
    }

    private final UserService userService;
    private final ApiResponseBuilder apiResponseBuilder;

    @PostMapping("/user")
    public ResponseEntity<ApiResponse<UserResponseDto>> join(@RequestBody UserCreateRequestDto userCreateRequestDto) {
        return apiResponseBuilder.created(ResponseMessage.USER_CREATE_SUCCESS, userService.saveUser(userCreateRequestDto));
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<ApiResponse<List<UserUrlResponseDto>>> getUrlsByUserId(@PathVariable Long id) {
        return apiResponseBuilder.ok(ResponseMessage.URLS_VIEW_SUCCESS, userService.getUrlsByUserId(id));
    }

    @PostMapping("/url/{id}")
    public ResponseEntity<ApiResponse<UserUrlResponseDto>> createShortUrl(@PathVariable Long id, @RequestBody @Valid UrlCreateRequestDto urlCreateRequestDto) {
        return apiResponseBuilder.ok(ResponseMessage.USER_CREATE_SUCCESS, userService.createShortUrl(id, urlCreateRequestDto));
    }

}
