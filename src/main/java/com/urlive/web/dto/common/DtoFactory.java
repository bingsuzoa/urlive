package com.urlive.web.dto.common;

import com.urlive.domain.url.Url;
import com.urlive.web.dto.user.UserResponse;
import com.urlive.web.dto.userUrl.UserUrlResponse;
import com.urlive.domain.user.Country;
import com.urlive.domain.user.Gender;
import com.urlive.domain.user.User;
import com.urlive.domain.userUrl.UserUrl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class DtoFactory {

    public static UserResponse createUserResponseDto(User user) {
        Long id = user.getId();
        String name = user.getName();
        int age = user.getAge();
        Gender gender = user.getGender();
        Country country = user.getCountry();
        return new UserResponse(id, name, age, gender, country);
    }

    public static List<UserUrlResponse> getBoardDto(User user) {
        Set<UserUrl> urls = user.getUrls();
        List<UserUrlResponse> userUrls = new ArrayList<>();

        for (UserUrl userUrl : urls) {
            String name = user.getName();
            String originalUrl = userUrl.getOriginalUrl();
            String shortUrl = userUrl.getShortUrl();
            String title = userUrl.getTitle();
            LocalDateTime createdAt = userUrl.getCreateTime();
            Long viewCount = userUrl.getViewCount();

            userUrls.add(new UserUrlResponse(name, originalUrl, shortUrl, title, createdAt, viewCount));
        }
        return userUrls;
    }

    public static UserUrlResponse getUserUrlDto(UserUrl userUrl) {
        String name = userUrl.getName();
        String originalUrl = userUrl.getOriginalUrl();
        String shortUrl = userUrl.getShortUrl();
        String title = userUrl.getTitle();
        LocalDateTime createdAt = userUrl.getCreateTime();
        Long viewCount = userUrl.getViewCount();
        return new UserUrlResponse(name, originalUrl, shortUrl, title, createdAt, viewCount);
    }
}
