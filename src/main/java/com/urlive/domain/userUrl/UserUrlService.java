package com.urlive.domain.userUrl;

import com.urlive.domain.url.Url;
import com.urlive.domain.user.User;
import com.urlive.web.dto.domain.common.DtoFactory;
import com.urlive.web.dto.domain.userUrl.UpdateTitleRequest;
import com.urlive.web.dto.domain.userUrl.UserUrlResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserUrlService {

    @Autowired
    public UserUrlService(UserUrlRepository userUrlRepository) {
        this.userUrlRepository = userUrlRepository;
    }

    private final UserUrlRepository userUrlRepository;

    @Transactional
    public UserUrl saveUserUrl(User user, Url url) {
        return userUrlRepository.save(new UserUrl(user, url));
    }

    @Transactional(readOnly = true)
    public UserUrl getUserUrl(User user, Url url) {
        Optional<UserUrl> optionalUserUrl = userUrlRepository.findUserUrlByUserAndUrl(user, url);
        if (optionalUserUrl.isEmpty()) {
            throw new IllegalArgumentException(UserUrl.INVALID_USER_URL);
        }
        return optionalUserUrl.get();
    }

    @Transactional
    public UserUrlResponse updateTitle(Long id,
                                       UpdateTitleRequest updateTitleRequest) {
        Optional<UserUrl> optionalUserUrl = userUrlRepository.findById(id);
        if (optionalUserUrl.isEmpty()) {
            throw new IllegalArgumentException(UserUrl.INVALID_USER_URL);
        }
        UserUrl userUrl = optionalUserUrl.get();
        userUrl.updateTitle(updateTitleRequest.newTitle());
        return DtoFactory.getUserUrlDto(userUrl);
    }

    @Transactional
    public UserUrlResponse deleteUserUrl(Long id) {
        Optional<UserUrl> optionalUserUrl = userUrlRepository.findById(id);
        if (optionalUserUrl.isEmpty()) {
            throw new IllegalArgumentException(UserUrl.INVALID_USER_URL);
        }
        UserUrl userUrl = optionalUserUrl.get();
        userUrlRepository.delete(userUrl);
        return DtoFactory.getUserUrlDto(userUrl);
    }
}
