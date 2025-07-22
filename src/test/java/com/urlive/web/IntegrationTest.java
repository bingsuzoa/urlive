//package com.urlive.web;
//
//import com.urlive.config.AsyncSyncTestConfig;
//import com.urlive.config.TestRedisConfig;
//import com.urlive.domain.url.UrlRepository;
//import com.urlive.domain.user.User;
//import com.urlive.domain.user.UserRepository;
//import com.urlive.domain.user.UserService;
//import com.urlive.domain.user.option.Gender;
//import com.urlive.domain.user.option.country.CountryRepository;
//import com.urlive.domain.user.passwordHistory.PasswordService;
//import com.urlive.global.responseFormat.ApiResponse;
//import com.urlive.global.responseFormat.ApiResponseBuilder;
//import com.urlive.service.UrliveFacade;
//import com.urlive.web.dto.domain.url.UrlCreateRequest;
//import com.urlive.web.dto.domain.user.PasswordChangeRequest;
//import com.urlive.web.dto.domain.user.UserCreateRequest;
//import com.urlive.web.dto.domain.user.UserResponse;
//import com.urlive.web.dto.domain.userUrl.UpdateTitleRequest;
//import com.urlive.web.dto.domain.userUrl.UserUrlResponse;
//import org.assertj.core.api.Assertions;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.params.ParameterizedTest;
//import org.junit.jupiter.params.provider.ValueSource;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.web.client.TestRestTemplate;
//import org.springframework.boot.test.web.server.LocalServerPort;
//import org.springframework.context.annotation.Import;
//import org.springframework.core.ParameterizedTypeReference;
//import org.springframework.data.redis.core.StringRedisTemplate;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpMethod;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.test.context.ActiveProfiles;
//
//import java.util.List;
//
//
//@ActiveProfiles("test")
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@Import({AsyncSyncTestConfig.class, TestRedisConfig.class})
//public class IntegrationTest {
//
//    @LocalServerPort
//    private int port;
//
//    @Autowired
//    private UrliveFacade urliveFacade;
//
//    @Autowired
//    private ApiResponseBuilder apiResponseBuilder;
//
//    @Autowired
//    private TestRestTemplate restTemplate;
//
//    @Autowired
//    private StringRedisTemplate redisTemplate;
//
//    @Autowired
//    private UserService userService;
//
//    @Autowired
//    private UrlRepository urlRepository;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private PasswordService passwordService;
//
//    @Autowired
//    private CountryRepository countryRepository;
//
//    @AfterEach
//    void deleteAll() {
//        userRepository.deleteAll();
//        urlRepository.deleteAll();
//    }
//
//    Long getUser() {
//        UserCreateRequest request = new UserCreateRequest("test", "01012345678", "user1111",
//                20250312, 1, "KR");
//        UserResponse response = userService.saveUser(request);
//        return response.id();
//    }
//
//    String getShortUrl(Long id) {
//        UrlCreateRequest urlCreateRequest = new UrlCreateRequest("https://urlive.com");
//        String shortUrl = urliveFacade.createShortUrl(id, urlCreateRequest).shortUrl();
//        return shortUrl;
//    }
//
//    /// ////////////////해피 테스트
//    @Test
//    @DisplayName("회원가입 요청 성공 테스트")
//    void 회원가입_요청_성공() {
//        UserCreateRequest request = new UserCreateRequest("test", "01012345678", "user1111",
//                20250312, 1, "KR");
//        String url = "http://localhost:" + port + "/user";
//
//        ResponseEntity<ApiResponse<UserResponse>> responseEntity = restTemplate.exchange(
//                url,
//                HttpMethod.POST,
//                new HttpEntity<>(request),
//                new ParameterizedTypeReference<ApiResponse<UserResponse>>() {
//                }
//        );
//
//        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
//        List<User> users = userRepository.findAll();
//        Assertions.assertThat(users.get(0).getGender()).isEqualTo(Gender.WOMEN);
//        Assertions.assertThat(users.get(0).getCountry().getIsoCode()).isEqualTo("KR");
//    }
//
//    @Test
//    @DisplayName("단축 URL 생성 테스트")
//    void 단축_URL_생성() {
//        Long id = getUser();
//        UrlCreateRequest request = new UrlCreateRequest("https://urlive.com");
//
//        String url = "http://localhost:" + port + "/users/" + id + "/urls";
//
//        HttpStatus status = HttpStatus.valueOf(
//                restTemplate.postForEntity(url, request, Void.class).getStatusCode().value());
//        Assertions.assertThat(status).isEqualTo(HttpStatus.OK);
//    }
//
//
//    @Test
//    @DisplayName("사용자 단축Url 목록 조회 테스트")
//    void 목록_조회() {
//        Long id = getUser();
//        String shortUrl = getShortUrl(id);
//
//        UrlCreateRequest request = new UrlCreateRequest("https://urlive.com");
//        urliveFacade.getUrlsByUser(id);
//
//        String url = "http://localhost:" + port + "/users/" + id + "/urls";
//        ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
//        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
//    }
//
//    @Test
//    @DisplayName("사용자 단축 Url title 변경 테스트")
//    void title_변경() {
//        Long id = getUser();
//        UrlCreateRequest urlCreateRequest = new UrlCreateRequest("https://urlive.com");
//        Long userUrlId = urliveFacade.createShortUrl(id, urlCreateRequest).id();
//
//        UpdateTitleRequest request = new UpdateTitleRequest("updateTitle");
//        String url = "http://localhost:" + port + "/user-urls/" + userUrlId;
//
//        ResponseEntity<ApiResponse<UserUrlResponse>> responseEntity = restTemplate.exchange(
//                url,
//                HttpMethod.PATCH,
//                new HttpEntity<>(request),
//                new ParameterizedTypeReference<ApiResponse<UserUrlResponse>>() {
//                }
//        );
//        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
//        Assertions.assertThat("updateTitle").isEqualTo(responseEntity.getBody().getData().title());
//    }
//
//    @Test
//    @DisplayName("사용자 단축 Url 삭제 테스트")
//    void title_삭제() {
//        Long id = getUser();
//        UrlCreateRequest urlCreateRequest = new UrlCreateRequest("https://urlive.com");
//        Long userUrlId = urliveFacade.createShortUrl(id, urlCreateRequest).id();
//
//        String url = "http://localhost:" + port + "/user-urls/" + userUrlId;
//
//        ResponseEntity<ApiResponse<UserUrlResponse>> responseEntity = restTemplate.exchange(
//                url,
//                HttpMethod.DELETE,
//                HttpEntity.EMPTY,
//                new ParameterizedTypeReference<ApiResponse<UserUrlResponse>>() {
//                }
//        );
//        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
//    }
//
//    /// ///////예외 테스트
//    @Test
//    @DisplayName("회원가입 실패 테스트")
//    void 회원가입_실패() {
//        UserCreateRequest request = new UserCreateRequest("", "010123456789", "",
//                null, null, null);
//        String url = "http://localhost:" + port + "/user";
//
//        ResponseEntity<ApiResponse<UserResponse>> responseEntity = restTemplate.exchange(
//                url,
//                HttpMethod.POST,
//                new HttpEntity<>(request),
//                new ParameterizedTypeReference<ApiResponse<UserResponse>>() {
//                }
//        );
//
//        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
//        Assertions.assertThat(responseEntity.getBody().getMessage()).contains("이름", "휴대폰", "비밀번호", "생년월일", "성별", "국가");
//    }
//
//    @ParameterizedTest
//    @DisplayName("단축 URL 생성 실패 테스트")
//    @ValueSource(strings = {"urlive.com", "https:urlive.com", "http://", "http://.com", "http://urlive"})
//    void URL_생성_실패(String value) {
//        Long id = getUser();
//
//        UrlCreateRequest request = new UrlCreateRequest(value);
//        String url = "http://localhost:" + port + "/users/" + id + "/urls";
//
//        ResponseEntity<ApiResponse<UserResponse>> responseEntity = restTemplate.exchange(
//                url,
//                HttpMethod.POST,
//                new HttpEntity<>(request),
//                new ParameterizedTypeReference<ApiResponse<UserResponse>>() {
//                }
//        );
//
//        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
//    }
//
//    @Test
//    @DisplayName("비밀번호 변경 실패 테스트")
//    void 비밀번호_변경_실패() {
//        Long id = getUser();
//
//        String passwordHistory1 = "password2222";
//        String passwordHistory2 = "password3333";
//        userService.changePassword(id, new PasswordChangeRequest(passwordHistory1));
//        userService.changePassword(id, new PasswordChangeRequest(passwordHistory2));
//
//        org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, () -> {
//            userService.changePassword(id, new PasswordChangeRequest(passwordHistory1));
//        });
//    }
//}
