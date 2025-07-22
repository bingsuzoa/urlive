package com.urlive.service.integrationTest;

import com.urlive.config.AsyncSyncTestConfig;
import com.urlive.config.TestRedisConfig;
import com.urlive.domain.url.Url;
import com.urlive.domain.url.UrlService;
import com.urlive.domain.user.User;
import com.urlive.domain.user.UserRepository;
import com.urlive.domain.user.UserService;
import com.urlive.domain.user.option.Gender;
import com.urlive.domain.user.option.country.Country;
import com.urlive.domain.user.option.country.CountryService;
import com.urlive.domain.user.passwordHistory.PasswordService;
import com.urlive.service.RsaService;
import com.urlive.web.dto.domain.url.UrlCreateRequest;
import com.urlive.web.dto.domain.user.UserCreateRequest;
import com.urlive.web.dto.domain.user.UserLoginRequest;
import com.urlive.web.dto.domain.user.UserResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static com.urlive.domain.user.User.NOT_EXIST_USER_ID;

@SpringBootTest
@ActiveProfiles("test")
@Import({AsyncSyncTestConfig.class, TestRedisConfig.class})
public class UserServiceIntegrationTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CountryService countryService;
    @Autowired
    private PasswordService passwordService;
    @Autowired
    private UrlService urlService;
    @Autowired
    private UserService userService;
    @Autowired
    private RsaService rsaService;

    @AfterEach
    void deleteAll() {
        userRepository.deleteAll();
    }

    private static final String rawPassword = "user1111";

    UserResponse getUser() throws Exception {

        Cipher encryptCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        encryptCipher.init(Cipher.ENCRYPT_MODE, rsaService.getPublicKey());
        byte[] encryptedWrongBytes = encryptCipher.doFinal(rawPassword.getBytes(StandardCharsets.UTF_8));
        String encryptedPassword = Base64.getEncoder().encodeToString(encryptedWrongBytes);

        UserCreateRequest request = new UserCreateRequest(
                "test", "01012345678", encryptedPassword, 20250312, 1, "KR");
        return userService.saveUser(request);
    }

    @Test
    @DisplayName("User객체 저장 테스트")
    void 객체_저장() throws Exception {
        Cipher encryptCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        encryptCipher.init(Cipher.ENCRYPT_MODE, rsaService.getPublicKey());
        byte[] encryptedWrongBytes = encryptCipher.doFinal(rawPassword.getBytes(StandardCharsets.UTF_8));
        String encryptedPassword = Base64.getEncoder().encodeToString(encryptedWrongBytes);

        UserCreateRequest userCreateRequest = new UserCreateRequest(
                "test", "01012345678", encryptedPassword, 20250312, 1, "KR");

        UserResponse response = userService.saveUser(userCreateRequest);
        Assertions.assertThat(response.countryDto().name()).isEqualTo("South Korea");
    }

    @Test
    @DisplayName("이미 등록된 휴대폰 번호 있는지 확인하는 테스트")
    void 등록된_휴대폰_번호_없으면_false반환() throws Exception {
        UserResponse user = getUser();
        Assertions.assertThat(userService.isDuplicatePhoneNumber("01011112222")).isFalse();
    }

    @Test
    @DisplayName("로그인 테스트")
    void 로그인_테스트() throws Exception {
        UserResponse user = getUser();

        Cipher encryptCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        encryptCipher.init(Cipher.ENCRYPT_MODE, rsaService.getPublicKey());
        byte[] encryptedWrongBytes = encryptCipher.doFinal(rawPassword.getBytes(StandardCharsets.UTF_8));
        String encryptedPassword = Base64.getEncoder().encodeToString(encryptedWrongBytes);

        UserLoginRequest request = new UserLoginRequest("01012345678", encryptedPassword);
        Assertions.assertThat(userService.loginUser(request)).isNotNull();
    }

    @Test
    @DisplayName("User의 Url 목록 얻는 테스트")
    void User_Urls_목록_얻기() {
        Country country = countryService.findByIsoCode("KR");
        User user = userRepository.save(new User(
                "test", "01012345678", "password", 20250312, Gender.WOMEN, country));
        Url url1 = urlService.CreateShortUrl(user, new UrlCreateRequest("originalUrl1"));
        Url url2 = urlService.CreateShortUrl(user, new UrlCreateRequest("originalUrl2"));

        Assertions.assertThat(userService.getUrls(user.getId()).size()).isEqualTo(2);
    }

    /// /////////예외 테스트
    @Test
    @DisplayName("이미 등록된 휴대폰 번호 있는지 확인하는 테스트")
    void 등록된_휴대폰_번호_있으면_예외_반환() throws Exception {
        UserResponse user = getUser();
        org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, () -> {
            userService.isDuplicatePhoneNumber("01012345678");
        });
    }

    @Test
    @DisplayName("로그인 실패 테스트 : 없는 아이디")
    void 로그인_실패_테스트() throws Exception {
        UserResponse user = getUser();
        UserLoginRequest request = new UserLoginRequest("01011112222", "user1111!");

        IllegalArgumentException e = org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class,
                () -> userService.loginUser(request));
        Assertions.assertThat(e.getMessage()).isEqualTo(NOT_EXIST_USER_ID);
    }
}
