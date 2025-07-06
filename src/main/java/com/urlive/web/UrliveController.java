package com.urlive.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.urlive.global.responseFormat.ApiResponse;
import com.urlive.global.responseFormat.ApiResponseBuilder;
import com.urlive.global.responseFormat.ResponseMessage;
import com.urlive.service.UrliveFacade;
import com.urlive.web.dto.url.UrlCreateRequest;
import com.urlive.web.dto.user.PasswordChangeRequest;
import com.urlive.web.dto.user.UserCreateRequest;
import com.urlive.web.dto.user.UserLoginRequest;
import com.urlive.web.dto.user.UserResponse;
import com.urlive.web.dto.userUrl.UpdateTitleRequest;
import com.urlive.web.dto.userUrl.UserUrlResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
    private static final Logger logger = LoggerFactory.getLogger(UrliveController.class);

    @PostMapping("/user")
    public ResponseEntity<ApiResponse<UserResponse>> join(@RequestBody @Valid UserCreateRequest userCreateRequest) {
        return apiResponseBuilder.created(ResponseMessage.USER_CREATE_SUCCESS, urliveFacade.saveUser(userCreateRequest));
    }

    @GetMapping("/user")
    public ResponseEntity<ApiResponse<UserResponse>> login(@RequestBody @Valid UserLoginRequest userLoginRequest) {
        return apiResponseBuilder.ok(ResponseMessage.USER_LOGIN_SUCCESS, urliveFacade.loginUser(userLoginRequest));
    }

    @PatchMapping("/user/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> changeUserPassword(@PathVariable Long id,
                                                                        @RequestBody @Valid PasswordChangeRequest passwordChangeRequest) {
        return apiResponseBuilder.ok(ResponseMessage.USER_PASSWORD_CHANGE_SUCCESS, urliveFacade.changePassword(id, passwordChangeRequest));
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

    @GetMapping("/{short-url}")
    public ResponseEntity<ApiResponse<Void>> redirectToOriginalUrl(
            @PathVariable(name = "short-url") String shortUrl,
            HttpServletRequest request
    ) throws JsonProcessingException {
        String originalUrl = urliveFacade.decodeShortUrl(shortUrl);

        Map<String, Object> logMap = new LinkedHashMap<>();
        logMap.put("event", "redirect");
        logMap.put("shortUrl", shortUrl);
        logMap.put("referer", request.getHeader("Referer"));
        logMap.put("ip", request.getRemoteAddr());
        logMap.put("ua", request.getHeader("User-Agent"));

        logger.info(new ObjectMapper().writeValueAsString(logMap));

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(originalUrl));

        return ResponseEntity.status(HttpStatus.FOUND)
                .headers(headers)
                .build();
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

}
