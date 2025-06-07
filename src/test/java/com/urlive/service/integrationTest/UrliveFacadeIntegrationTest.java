package com.urlive.service.integrationTest;

import com.urlive.domain.user.User;
import com.urlive.domain.user.UserRepository;
import com.urlive.domain.userUrl.UserUrlRepository;
import com.urlive.service.*;
import com.urlive.web.dto.url.UrlCreateRequest;
import com.urlive.web.dto.user.PasswordChangeRequest;
import com.urlive.web.dto.user.UserCreateRequest;
import com.urlive.web.dto.user.UserResponse;
import com.urlive.web.dto.userUrl.UpdateTitleRequest;
import com.urlive.web.dto.userUrl.UserUrlResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
public class UrliveFacadeIntegrationTest {

    @Autowired
    private UrliveFacade urliveFacade;

    @Autowired
    private UserService userService;

    @Autowired
    private UserUrlService userUrlService;

    @Autowired
    private PasswordService passwordService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CountryService countryService;

    @Autowired
    private UserUrlRepository userUrlRepository;


    User getUser() {
        UserResponse user = urliveFacade.saveUser(new UserCreateRequest("test", "01012345678", "test123", 20250604, 1, "KR"));
        return userRepository.findById(user.id()).get();
    }

    /// ////////////해피 테스트
    @Test
    @DisplayName("회원가입 성공 테스트")
    void 회원가입_성공() {
        UserCreateRequest userCreateRequest =
                new UserCreateRequest("test", "01012345678", "test123", 20250604, 1, "KR");
        Assertions.assertThat(urliveFacade.saveUser(userCreateRequest)).isNotNull();
    }

    @Test
    @DisplayName("비밀번호 변경 테스트")
    void 비밀번호_변경() {
        User user = getUser();
        Assertions.assertThat(urliveFacade.changePassword(user.getId(), new PasswordChangeRequest("12345"))).isNotNull();
    }

    @Test
    @DisplayName("단축Url 생성 테스트")
    void 단축_Url_생성() {
        User user = getUser();
        Assertions.assertThat(urliveFacade.createShortUrl(user.getId(), new UrlCreateRequest("http://test.com")))
                .isNotNull();
    }

    @Test
    @DisplayName("단축url -> original url 변경 테스트")
    void 단축Url로부터_originalUrl_얻기() {
        User user = getUser();
        UserUrlResponse response = urliveFacade.createShortUrl(user.getId(), new UrlCreateRequest("http://test.com"));
        Assertions.assertThat(response.shortUrl()).isNotEqualTo("http://test.com");
    }

    @Test
    @DisplayName("단축url 제목 변경 테스트")
    void 제목_변경() {
        User user = getUser();
        urliveFacade.createShortUrl(user.getId(), new UrlCreateRequest("http://test.com"));

        Assertions.assertThat(urliveFacade.updateTitle(user.getId(), new UpdateTitleRequest("newTitle"))
                .title()).isEqualTo("newTitle");
    }

    @Test
    @DisplayName("단축url 삭제 테스트")
    void url_삭제() {
        User user = getUser();
        UserUrlResponse response = urliveFacade.createShortUrl(user.getId(), new UrlCreateRequest("http://test.com"));
        Assertions.assertThat(urliveFacade.deleteUserUrl(response.id())).isNotNull();
    }

    /// ///////////예외 테스트
    @Test
    @DisplayName("이미 가입된 휴대폰 번호가 있을 경우 회원가입 실패")
    void 회원가입_실패() {
        User user = getUser();

        UserCreateRequest userCreateRequest =
                new UserCreateRequest("test", "01012345678", "test123", 20250604, 1, "KR");

        org.junit.jupiter.api.Assertions.assertThrows(DataIntegrityViolationException.class, () -> {
            urliveFacade.saveUser(userCreateRequest);
        });
    }

    @Test
    @DisplayName("동일한 비밀번호로 변경 실패 테스트")
    void 비밀번호_변경_실패() {
        User user = getUser();

        org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, () -> {
            urliveFacade.changePassword(user.getId(), new PasswordChangeRequest("test123"));
        });
    }

    @Test
    @DisplayName("최근에 변경했던 비밀번호 중 동일한 비밀번호로 변경 시도할 경우 실패")
    void 변경_이력에있는_비밀번호_변경시도시_실패() {
        User user = getUser();
        urliveFacade.changePassword(user.getId(), new PasswordChangeRequest("test456"));
        urliveFacade.changePassword(user.getId(), new PasswordChangeRequest("test789"));

        org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, () -> {
            urliveFacade.changePassword(user.getId(), new PasswordChangeRequest("test456"));
        });
    }

    @ParameterizedTest
    @DisplayName("형식에 안맞는 originalUrl인 경우 예외")
    @ValueSource(strings = {"http://"})
    void 형식에_안맞는_OriginalUrl인경우_예외(String value) {
        User user = getUser();
        org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, () ->
                urliveFacade.createShortUrl(user.getId(), new UrlCreateRequest(value))
        );
    }

    @Test
    @DisplayName("없는 단축Url 삭제 시 예외")
    void url_삭제_예외() {
        User user = getUser();
        UserUrlResponse response = urliveFacade.createShortUrl(user.getId(), new UrlCreateRequest("http://test.com"));
        urliveFacade.deleteUserUrl(response.id());

        org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, () -> {
            urliveFacade.deleteUserUrl(response.id());
        });
    }

}
