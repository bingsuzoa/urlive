//package com.urlive.service.integrationTest;
//
//import com.urlive.config.AsyncSyncTestConfig;
//import com.urlive.config.TestRedisConfig;
//import com.urlive.domain.url.Url;
//import com.urlive.domain.url.UrlRepository;
//import com.urlive.domain.url.UrlService;
//import com.urlive.domain.user.User;
//import com.urlive.domain.user.option.Gender;
//import com.urlive.domain.user.option.country.Country;
//import com.urlive.domain.user.option.country.CountryRepository;
//import com.urlive.web.dto.domain.url.UrlCreateRequest;
//import org.assertj.core.api.Assertions;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.context.annotation.Import;
//import org.springframework.test.context.ActiveProfiles;
//
//@SpringBootTest
//@ActiveProfiles("test")
//@Import({AsyncSyncTestConfig.class, TestRedisConfig.class})
//public class UrlServiceIntegrationTest {
//
//    @Autowired
//    private UrlService urlService;
//
//    @Autowired
//    private UrlRepository urlRepository;
//
//    @Autowired
//    private CountryRepository countryRepository;
//
//    private static final String originalUrl = "http://urlive.com";
//    private static final String shortUrl = "KD";
//
//    @Test
//    @DisplayName("단축URL 디코딩 테스트")
//    void 단축_url_디코딩_테스트() {
//        Country country = countryRepository.findByIsoCode("KR").get();
//        User user = new User("test", "01012345678", "1234", 2025, Gender.MEN, country);
//        Url url = new Url(user, originalUrl, shortUrl);
//        urlRepository.save(url);
//        Assertions.assertThat(urlService.decodeShortUrl(shortUrl)).isEqualTo(originalUrl);
//    }
//
//    @Test
//    @DisplayName("단축 url 생성 및 저장하는 테스트")
//    void 단축_url_생성_테스트() {
//        Country country = countryRepository.findByIsoCode("KR").get();
//        User user = new User("test", "01012345678", "1234", 2025, Gender.MEN, country);
//        UrlCreateRequest request = new UrlCreateRequest(originalUrl);
//
//        Url url = urlService.CreateShortUrl(user, request);
//        Assertions.assertThat(url).isNotNull();
//    }
//
//    /// //에러 테스트
//    @Test
//    @DisplayName("단축URL 디코딩 테스트 : 조회되는 Url이 없으면 예외")
//    void 단축_url_디코딩_예외() {
//        org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, () -> {
//            urlService.decodeShortUrl(shortUrl);
//        });
//    }
//}
