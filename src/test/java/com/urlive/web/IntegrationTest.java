package com.urlive.web;

import com.urlive.domain.url.Url;
import com.urlive.domain.url.UrlRepository;
import com.urlive.domain.user.Country;
import com.urlive.domain.user.Gender;
import com.urlive.domain.user.User;
import com.urlive.domain.user.UserRepository;
import com.urlive.domain.userUrl.UserUrl;
import com.urlive.domain.userUrl.UserUrlRepository;
import com.urlive.global.responseFormat.ApiResponse;
import com.urlive.web.dto.url.UrlCreateRequest;
import com.urlive.web.dto.user.UserCreateRequest;
import com.urlive.web.dto.user.UserResponse;
import com.urlive.web.dto.userUrl.UpdateTitleRequest;
import com.urlive.web.dto.userUrl.UserUrlResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class IntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UrlRepository urlRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserUrlRepository userUrlRepository;

    User setUp() {
        return userRepository.save(new User("test", "01012345678", "1234", 2025, Gender.MEN, Country.CHINA));
    }

    @AfterEach
    void init() {
        userUrlRepository.deleteAll();
        userRepository.deleteAll();
    }

    /// ///////해피 테스트
    @Test
    @DisplayName("회원가입 요청 성공 테스트")
    void 회원가입_요청_성공() {
        UserCreateRequest request = new UserCreateRequest(
                "test",
                "01012345678",
                "1234",
                2025,
                1,
                1
        );
        String url = "http://localhost:" + port + "/user";

        ResponseEntity<ApiResponse<UserResponse>> responseEntity = restTemplate.exchange(
                url,
                HttpMethod.POST,
                new HttpEntity<>(request),
                new ParameterizedTypeReference<ApiResponse<UserResponse>>() {
                }
        );
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        List<User> users = userRepository.findAll();
        Assertions.assertThat(users.get(0).getGender()).isEqualTo(Gender.WOMEN);
        Assertions.assertThat(users.get(0).getCountry()).isEqualTo(Country.AMERICA);
    }

    @Test
    @DisplayName("단축 URL 생성 테스트")
    void 단축_URL_생성() {
        User user = setUp();
        Long id = user.getId();

        UrlCreateRequest request = new UrlCreateRequest("https://urlive.com");
        String url = "http://localhost:" + port + "/user-url/" + id;

        ResponseEntity<ApiResponse<UserUrlResponse>> responseEntity = restTemplate.exchange(
                url,
                HttpMethod.POST,
                new HttpEntity<>(request),
                new ParameterizedTypeReference<ApiResponse<UserUrlResponse>>() {}
        );

        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("사용자 단축Url 목록 조회 테스트")
    void 목록_조회() {
        User user = setUp();
        Long id = user.getId();

        UrlCreateRequest request = new UrlCreateRequest("https://urlive.com");
        urlRepository.save(new Url(request.originalUrl()));

        String url = "http://localhost:" + port + "/user-url/" + id;

        ResponseEntity<ApiResponse<List<UserUrlResponse>>> responseEntity = restTemplate.exchange(
                url,
                HttpMethod.GET,
                HttpEntity.EMPTY,
                new ParameterizedTypeReference<ApiResponse<List<UserUrlResponse>>>(){}
        );

        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("사용자 단축 Url title 변경 테스트")
    void title_변경() {
        User user = setUp();
        Long id = user.getId();

        UrlCreateRequest urlCreateRequest = new UrlCreateRequest("https://urlive.com");
        Url urlEntity = urlRepository.save(new Url(urlCreateRequest.originalUrl()));
        UserUrl userUrl = new UserUrl(user, urlEntity);
        userUrlRepository.save(userUrl);

        String title = userUrl.getTitle();

        UpdateTitleRequest request = new UpdateTitleRequest("단축 Url");
        String url = "http://localhost:" + port + "/user-url/" + id;

        ResponseEntity<ApiResponse<UserUrlResponse>> responseEntity = restTemplate.exchange(
                url,
                HttpMethod.PATCH,
                new HttpEntity<>(request),
                new ParameterizedTypeReference<ApiResponse<UserUrlResponse>>() {}
        );

        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(title).isNotEqualTo(responseEntity.getBody().getData().title());
    }

    @Test
    @DisplayName("사용자 단축 Url 삭제 테스트")
    void title_삭제() {
        User user = setUp();

        UrlCreateRequest urlCreateRequest = new UrlCreateRequest("https://urlive.com");
        Url urlEntity = urlRepository.save(new Url(urlCreateRequest.originalUrl()));
        UserUrl userUrl = userUrlRepository.save(new UserUrl(user, urlEntity));
        Long id = userUrl.getId();

        UpdateTitleRequest request = new UpdateTitleRequest("단축 Url");
        String url = "http://localhost:" + port + "/user-url/" + id;

        ResponseEntity<ApiResponse<UserUrlResponse>> responseEntity = restTemplate.exchange(
                url,
                HttpMethod.DELETE,
                HttpEntity.EMPTY,
                new ParameterizedTypeReference<ApiResponse<UserUrlResponse>>() {}
        );

        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    /// ///////예외 테스트
    @Test
    @DisplayName("회원가입 실패 테스트")
    void 회원가입_실패() {
        UserCreateRequest request = new UserCreateRequest(
                "",
                "010123456789",
                "",
                null,
                null,
                null
        );
        String url = "http://localhost:" + port + "/user";

        ResponseEntity<ApiResponse<UserResponse>> responseEntity = restTemplate.exchange(
                url,
                HttpMethod.POST,
                new HttpEntity<>(request),
                new ParameterizedTypeReference<ApiResponse<UserResponse>>() {
                }
        );

        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        Assertions.assertThat(responseEntity.getBody().getMessage()).contains("이름", "휴대폰", "비밀번호", "생년월일", "성별", "국가");
    }

    @ParameterizedTest
    @DisplayName("단축 URL 생성 실패 테스트")
    @ValueSource(strings = {"urlive.com", "https:urlive.com", "http://", "http://.com", "http://urlive"})
    void URL_생성_실패(String value) {
        User user = setUp();
        Long id = user.getId();

        UrlCreateRequest request = new UrlCreateRequest(value);
        String url = "http://localhost:" + port + "/user-url/" + id;

        ResponseEntity<ApiResponse<UserResponse>> responseEntity = restTemplate.exchange(
                url,
                HttpMethod.POST,
                new HttpEntity<>(request),
                new ParameterizedTypeReference<ApiResponse<UserResponse>>() {}
        );

        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("사용자 단축 Url title 실패 테스트")
    void title_변경_실패() {
        User user = setUp();
        Long id = user.getId();

        UrlCreateRequest urlCreateRequest = new UrlCreateRequest("https://urlive.com");
        Url urlEntity = urlRepository.save(new Url(urlCreateRequest.originalUrl()));
        UserUrl userUrl = new UserUrl(user, urlEntity);
        userUrlRepository.save(userUrl);


        UpdateTitleRequest request = new UpdateTitleRequest("");
        String url = "http://localhost:" + port + "/user-url/" + id;

        ResponseEntity<ApiResponse<UserUrlResponse>> responseEntity = restTemplate.exchange(
                url,
                HttpMethod.PATCH,
                new HttpEntity<>(request),
                new ParameterizedTypeReference<ApiResponse<UserUrlResponse>>() {}
        );

        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

}
