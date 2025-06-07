package com.urlive.web;


import com.urlive.domain.user.option.Gender;
import com.urlive.domain.user.option.country.Country;
import com.urlive.global.responseFormat.ApiResponse;
import com.urlive.global.responseFormat.ApiResponseBuilder;
import com.urlive.service.UrliveFacade;
import com.urlive.web.dto.user.UserResponse;
import com.urlive.web.dto.userUrl.UserUrlResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UrliveController.class)
@ContextConfiguration(classes = {UrliveController.class, ApiResponseBuilder.class})
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
public class UrliveControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UrliveFacade urliveFacade;

    @MockBean
    private ApiResponseBuilder apiResponseBuilder;


    /// //////////////////////////해피 테스트
    @Test
    @DisplayName("회원가입 요청 확인")
    void 회원가입_요청() throws Exception {
        UserResponse userResponse = new UserResponse(1L, "test", 20250604, Gender.WOMEN, new Country("KR", "대한민국"));
        when(urliveFacade.saveUser(any())).thenReturn(userResponse);

        when(apiResponseBuilder.created(any(), any()))
                .thenReturn(
                        ResponseEntity.status(HttpStatus.CREATED)
                                .body(new ApiResponse<>(201, "message", userResponse))
                );

        String request = """
                {
                    "name" : "test",
                    "phoneNumber" : "01012345678",
                    "password" : "test1234",
                    "age" : 20250604,
                    "gender" : 1,
                    "country" : 1
                }
                """;

        mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("비밀번호 변경 확인")
    void 비밀번호_변경() throws Exception {
        UserResponse userResponse = new UserResponse(1L, "test", 20250604, Gender.WOMEN, new Country("KR", "대한민국"));
        when(urliveFacade.changePassword(any(), any())).thenReturn(userResponse);

        when(apiResponseBuilder.ok(any(), any()))
                .thenReturn(
                        ResponseEntity.status(HttpStatus.OK)
                                .body(new ApiResponse<>(200, "message", userResponse))
                );

        String request = """
                {
                    "rawNewPassword" : "test12345"
                }
                """;

        mockMvc.perform(patch("/user/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("단축 URL 생성하기")
    void 단축_URL_생성() throws Exception {
        UserUrlResponse userUrlResponse = new UserUrlResponse(1L, "originalUrl", "shortUrl", "title", LocalDateTime.now(), 1L);
        when(urliveFacade.createShortUrl(any(), any())).thenReturn(userUrlResponse);

        when(apiResponseBuilder.ok(any(), any()))
                .thenReturn(
                        ResponseEntity.status(HttpStatus.OK)
                                .body(new ApiResponse<>(200, "message", userUrlResponse))
                );

        String request = """
                {
                    "originalUrl" : "http://test.com"
                }
                """;

        mockMvc.perform(post("/users/{userId}/urls", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("사용자 urls 목록 가져오기")
    void URL_목록_가져오기() throws Exception {
        UserUrlResponse userUrlResponse = new UserUrlResponse(1L, "originalUrl", "shortUrl", "title", LocalDateTime.now(), 1L);
        List<UserUrlResponse> userUrls = List.of(userUrlResponse);
        when(urliveFacade.getUrlsByUser(any())).thenReturn(userUrls);

        when(apiResponseBuilder.ok(any(), any()))
                .thenReturn(
                        ResponseEntity.status(HttpStatus.OK)
                                .body(new ApiResponse<>(200, "message", userUrls))
                );

        mockMvc.perform(get("/users/{userId}/urls", 1L))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("리다이렉트 확인")
    void 리다이렉트_확인() throws Exception {
        when(urliveFacade.decodeShortUrl(any())).thenReturn("http://test.com");

        mockMvc.perform(get("/{short-url}", "shortUrl"))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "http://test.com"));
    }

    @Test
    @DisplayName("단축 URL title 변경하기")
    void 단축_URL_타이틀_변경() throws Exception {
        UserUrlResponse userUrlResponse = new UserUrlResponse(1L, "originalUrl", "shortUrl", "title", LocalDateTime.now(), 1L);
        when(urliveFacade.updateTitle(any(), any())).thenReturn(userUrlResponse);

        when(apiResponseBuilder.ok(any(), any()))
                .thenReturn(
                        ResponseEntity.status(HttpStatus.OK)
                                .body(new ApiResponse<>(200, "message", userUrlResponse))
                );

        String request = """
                {
                    "newTitle" : "테스트Title"
                }
                """;

        mockMvc.perform(patch("/user-urls/{userUrlId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("단축 URL 삭제하기")
    void 단축_URL_삭제() throws Exception {
        UserUrlResponse userUrlResponse = new UserUrlResponse(1L, "originalUrl", "shortUrl", "title", LocalDateTime.now(), 1L);
        when(urliveFacade.deleteUserUrl(any())).thenReturn(userUrlResponse);

        when(apiResponseBuilder.ok(any(), any()))
                .thenReturn(
                        ResponseEntity.status(HttpStatus.OK)
                                .body(new ApiResponse<>(200, "message", userUrlResponse))
                );

        mockMvc.perform(delete("/user-urls/{userUrlId}", 1L))
                .andExpect(status().isOk());
    }

    /// //////////////////////////예외 테스트
    @Test
    @DisplayName("비밀번호 변경 확인")
    void 비밀번호_예외() throws Exception {
        UserResponse userResponse = new UserResponse(1L, "test", 20250604, Gender.WOMEN, new Country("KR", "대한민국"));
        when(urliveFacade.changePassword(any(), any())).thenReturn(userResponse);

        String request = """
                {
                    "rawNewPassword" : ""
                }
                """;

        mockMvc.perform(patch("/user/{id}", 1L))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("단축 URL 예외")
    void 단축_URL_예외() throws Exception {
        UserUrlResponse userUrlResponse = new UserUrlResponse(1L, "originalUrl", "shortUrl", "title", LocalDateTime.now(), 1L);
        when(urliveFacade.createShortUrl(any(), any())).thenReturn(userUrlResponse);

        String request = """
                {
                    "originalUrl" : ""
                }
                """;

        mockMvc.perform(post("/users/{userId}/urls", 1L))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("단축 URL title 변경시 blank면 예외 발생")
    void 단축_URL_타이틀_변경시_예외() throws Exception {
        UserUrlResponse userUrlResponse = new UserUrlResponse(1L, "originalUrl", "shortUrl", "title", LocalDateTime.now(), 1L);
        when(urliveFacade.updateTitle(any(), any())).thenReturn(userUrlResponse);

        String request = """
                {
                    "newTitle" : ""
                }
                """;

        mockMvc.perform(patch("/user-urls/{userUrlId}", 1L))
                .andExpect(status().isBadRequest());
    }

}
