package com.urlive.web;

import com.urlive.global.responseFormat.ApiResponse;
import com.urlive.global.responseFormat.ApiResponseBuilder;
import com.urlive.global.responseFormat.ResponseMessage;
import com.urlive.service.UrliveFacade;
import com.urlive.web.dto.url.UrlCreateRequest;
import com.urlive.web.dto.user.PasswordChangeRequest;
import com.urlive.web.dto.user.UserCreateRequest;
import com.urlive.web.dto.user.UserResponse;
import com.urlive.web.dto.userUrl.UpdateTitleRequest;
import com.urlive.web.dto.userUrl.UserUrlResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
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
        return apiResponseBuilder.created(ResponseMessage.USER_CREATE_SUCCESS, urliveFacade.saveUser(userCreateRequest));
    }

    @PostMapping("/user/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> changeUser(@PathVariable Long id, @Valid PasswordChangeRequest passwordChangeRequest) {
        return apiResponseBuilder.ok(ResponseMessage.USER_PASSWORD_CHANGE_SUCCESS, urliveFacade.changePassword(id, passwordChangeRequest));
    }

    @PostMapping("/user-url/{id}")
    public ResponseEntity<ApiResponse<UserUrlResponse>> createShortUrl(@PathVariable Long id,
                                                                       @RequestBody @Valid UrlCreateRequest urlCreateRequest) {
        return apiResponseBuilder.ok(ResponseMessage.SHORT_URL_CREATE_SUCCESS, urliveFacade.createShortUrl(id, urlCreateRequest));
    }

    @GetMapping("/user-url/{id}")
    public ResponseEntity<ApiResponse<List<UserUrlResponse>>> getUrlsByUserId(@PathVariable Long id) {
        return apiResponseBuilder.ok(ResponseMessage.URLS_VIEW_SUCCESS, urliveFacade.getUrlsByUser(id));
    }

    @GetMapping("/{short-url}")
    public ResponseEntity<ApiResponse<Void>> redirectToOriginalUrl(@PathVariable(name = "short-url") String shortUrl) {
        String originalUrl = urliveFacade.decodeShortUrl(shortUrl);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(originalUrl));
        return ResponseEntity.status(HttpStatus.FOUND)
                .headers(headers)
                .build();
    }

    @PatchMapping("user-url/{id}")
    public ResponseEntity<ApiResponse<UserUrlResponse>> updateTitle(@PathVariable Long id,
                                                                    @RequestBody @Valid UpdateTitleRequest updateTitleRequest) {
        return apiResponseBuilder.ok(ResponseMessage.URL_TITLE_UPDATE_SUCCESS, urliveFacade.updateTitle(id, updateTitleRequest));
    }

    @DeleteMapping("user-url/{id}")
    public ResponseEntity<ApiResponse<UserUrlResponse>> deleteUserUrl(@PathVariable Long id) {
        return apiResponseBuilder.ok(ResponseMessage.USER_URL_DELETE_SUCCESS, urliveFacade.deleteUserUrl(id));
    }

}
