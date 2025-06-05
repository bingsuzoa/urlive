package com.urlive.web.dto.common;

import com.urlive.domain.user.Country;
import com.urlive.domain.user.Gender;
import com.urlive.domain.user.User;
import com.urlive.domain.userUrl.UserUrl;
import com.urlive.web.dto.user.UserResponse;
import com.urlive.web.dto.userUrl.UserUrlResponse;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DtoFactory {

    public static UserResponse createUserResponseDto(User user) {
        Long id = user.getId();
        String name = user.getName();
        int age = user.getAge();
        Gender gender = user.getGender();
        Country country = user.getCountry();
        return new UserResponse(id, name, age, gender, country);
    }

    public static List<UserUrlResponse> getBoardDto(List<UserUrl> userUrls) {
        List<UserUrlResponse> userUrlsResponse = new ArrayList<>();

        for (UserUrl userUrl : userUrls) {
            String originalUrl = userUrl.getOriginalUrl();
            String shortUrl = userUrl.getShortUrl();
            String title = userUrl.getTitle();
            LocalDateTime createdAt = userUrl.getCreateTime();
            Long viewCount = userUrl.getViewCount();

            userUrlsResponse.add(new UserUrlResponse(originalUrl, shortUrl, title, createdAt, viewCount));
        }
        return userUrlsResponse;
    }

    public static UserUrlResponse getUserUrlDto(UserUrl userUrl) {
        String originalUrl = userUrl.getOriginalUrl();
        String shortUrl = userUrl.getShortUrl();
        String title = userUrl.getTitle();
        LocalDateTime createdAt = userUrl.getCreateTime();
        Long viewCount = userUrl.getViewCount();
        return new UserUrlResponse(originalUrl, shortUrl, title, createdAt, viewCount);
    }
}
