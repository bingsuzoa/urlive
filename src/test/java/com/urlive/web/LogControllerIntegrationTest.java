//package com.urlive.web;
//
//import com.urlive.config.AsyncSyncTestConfig;
//import com.urlive.config.TestRedisConfig;
//import com.urlive.domain.infrastructure.log.Log;
//import com.urlive.domain.infrastructure.log.LogRepository;
//import com.urlive.domain.infrastructure.log.LogService;
//import com.urlive.domain.url.Url;
//import com.urlive.domain.url.UrlRepository;
//import com.urlive.domain.user.UserRepository;
//import com.urlive.domain.user.UserService;
//import com.urlive.global.responseFormat.ApiResponse;
//import com.urlive.global.responseFormat.ApiResponseBuilder;
//import com.urlive.service.UrliveFacade;
//import com.urlive.web.dto.domain.url.UrlCreateRequest;
//import com.urlive.web.dto.domain.user.UserCreateRequest;
//import com.urlive.web.dto.domain.user.UserResponse;
//import io.restassured.response.ValidatableResponse;
//import org.assertj.core.api.Assertions;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.web.client.TestRestTemplate;
//import org.springframework.boot.test.web.server.LocalServerPort;
//import org.springframework.context.annotation.Import;
//import org.springframework.core.ParameterizedTypeReference;
//import org.springframework.data.redis.core.StringRedisTemplate;
//import org.springframework.http.HttpMethod;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.test.context.ActiveProfiles;
//
//import java.util.List;
//import java.util.Map;
//
//import static io.restassured.RestAssured.given;
//import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
//
//@ActiveProfiles("test")
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@Import({AsyncSyncTestConfig.class, TestRedisConfig.class})
//public class LogControllerIntegrationTest {
//
//    @LocalServerPort
//    private int port;
//
//    @Autowired
//    private UrliveFacade urliveFacade;
//
//    @Autowired
//    private LogService logService;
//
//    @Autowired
//    private UserService userService;
//
//    @Autowired
//    private TestRestTemplate restTemplate;
//
//    @Autowired
//    private StringRedisTemplate redisTemplate;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private UrlRepository urlRepository;
//
//    @Autowired
//    private LogRepository logRepository;
//
//    @Autowired
//    private ApiResponseBuilder apiResponseBuilder;
//
//    @AfterEach
//    void deleteAll() {
//        userRepository.deleteAll();
//        urlRepository.deleteAll();
//        logRepository.deleteAll();
//        redisTemplate.getConnectionFactory().getConnection().flushDb();
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
//    void saveLog(Long id, String shortUrl) {
//        Log log = logRepository.save(new Log(shortUrl, "unknown", "instagram", "231.180.227.51", "iPhone"));
//        Log log2 = logRepository.save(new Log(shortUrl, "unknown", "naver", "231.180.227.51", "Windows"));
//        Log log3 = logRepository.save(new Log(shortUrl, "unknown", "instagram", "231.180.227.51", "Mac"));
//    }
//
//    @Test
//    @DisplayName("단축 URL 리다이렉트 테스트")
//    void 단축_URL로부터_원본_URL_얻기() {
//        Long id = getUser();
//        String shortUrl = getShortUrl(id);
//        String url = "http://localhost:" + port + "/" + shortUrl;
//
//        ValidatableResponse validatableResponse = given()
//                .port(port)
//                .redirects().follow(false)
//                .when()
//                .get(url)
//                .then()
//                .statusCode(302);
//    }
//
//    @Test
//    @DisplayName("사용자 단축Url 조회 시 조회수 증가 확인 테스트")
//    void 조회수_증가() {
//        Long id = getUser();
//        String shortUrl = getShortUrl(id);
//        Url urlEntity = urlRepository.findUrlByShortUrl(shortUrl).get();
//        Long urlId = urlEntity.getId();
//
//        String url = "http://localhost:" + port + "/" + shortUrl;
//        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
//
//        String redisKey = "viewCount:" + urlId;
//        String viewCount = (String) redisTemplate.opsForValue().get(redisKey);
//        Assertions.assertThat(viewCount).isEqualTo("1");
//    }
//
//    @Test
//    @DisplayName("날짜별 유입량 조회")
//    void 날짜별_유입량_조회() {
//        Long id = getUser();
//        String shortUrl = getShortUrl(id);
//        saveLog(id, shortUrl);
//
//        String url = "http://localhost:" + port + "/user-urls/" + shortUrl + "/date?days=7";
//        ResponseEntity<ApiResponse<List<Map<String, Object>>>> response = restTemplate.exchange(
//                url,
//                HttpMethod.GET,
//                null,
//                new ParameterizedTypeReference<>() {
//                }
//        );
//
//        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
//
//        List<Map<String, Object>> data = response.getBody().getData();
//        Map<String, Object> result = data.get(data.size() - 1);
//        Assertions.assertThat(result.get("range")).isEqualTo("2025-07-13");
//        Map<String, Long> stats = (Map<String, Long>) result.get("stats");
//        long count = ((Number) stats.get("count")).longValue();
//        Assertions.assertThat(count).isEqualTo(3L);
//    }
//
//    @Test
//    @DisplayName("날짜별 유입경로별 유입량 조회")
//    void 날짜별_유입경로별_유입량_조회() {
//        Long id = getUser();
//        String shortUrl = getShortUrl(id);
//        saveLog(id, shortUrl);
//
//        String url = "http://localhost:" + port + "/user-urls/" + shortUrl + "/referer?days=7";
//        ResponseEntity<ApiResponse<List<Map<String, Object>>>> response = restTemplate.exchange(
//                url,
//                HttpMethod.GET,
//                null,
//                new ParameterizedTypeReference<>() {
//                }
//        );
//
//        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
//
//        List<Map<String, Object>> data = response.getBody().getData();
//        Map<String, Object> result = data.get(data.size() - 1);
//        Assertions.assertThat(result.get("range")).isEqualTo("2025-07-13");
//        Map<String, Long> stats = (Map<String, Long>) result.get("stats");
//        Assertions.assertThat(stats.size()).isEqualTo(2);
//    }
//
//    @Test
//    @DisplayName("날짜별 디바이스별 유입량 조회")
//    void 날짜별_디바이스별_유입량_조회() {
//        Long id = getUser();
//        String shortUrl = getShortUrl(id);
//        saveLog(id, shortUrl);
//
//        String url = "http://localhost:" + port + "/user-urls/" + shortUrl + "/device?days=7";
//        ResponseEntity<ApiResponse<List<Map<String, Object>>>> response = restTemplate.exchange(
//                url,
//                HttpMethod.GET,
//                null,
//                new ParameterizedTypeReference<>() {
//                }
//        );
//
//        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
//
//        List<Map<String, Object>> data = response.getBody().getData();
//        Map<String, Object> result = data.get(data.size() - 1);
//        Assertions.assertThat(result.get("range")).isEqualTo("2025-07-13");
//        Map<String, Long> stats = (Map<String, Long>) result.get("stats");
//        Assertions.assertThat(stats.size()).isEqualTo(3);
//    }
//}
