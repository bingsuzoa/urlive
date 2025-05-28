package com.urlive.controller.dto.common;

import com.urlive.controller.dto.user.UserResponseDto;
import com.urlive.controller.dto.userUrl.UserUrlResponseDto;
import com.urlive.domain.user.Country;
import com.urlive.domain.user.Gender;
import com.urlive.domain.user.User;
import com.urlive.domain.userUrl.UserUrl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class DtoFactory {

    public static UserResponseDto createUserResponseDto(User user) {
        Long id = user.getId();
        String name = user.getName();
        int age = user.getAge();
        Gender gender = user.getGender();
        Country country = user.getCountry();
        return new UserResponseDto(id, name, age, gender, country);
    }

    public static List<UserUrlResponseDto> getBoardDto(User user) {
        Set<UserUrl> urls = user.getUrls();
        List<UserUrlResponseDto> userUrls = new ArrayList<>();

        for (UserUrl userUrl : urls) {
            String name = user.getName();
            String originalUrl = userUrl.getOriginalUrl();
            String shortUrl = userUrl.getShortUrl();
            String title = userUrl.getTitle();
            LocalDateTime createdAt = userUrl.getCreateTime();
            Long viewCount = userUrl.getViewCount();

            userUrls.add(new UserUrlResponseDto(name, originalUrl, shortUrl, title, createdAt, viewCount));
        }
        return userUrls;
    }

    public static UserUrlResponseDto getUserUrlDto(UserUrl userUrl) {
        String name = userUrl.getName();
        String originalUrl = userUrl.getOriginalUrl();
        String shortUrl = userUrl.getShortUrl();
        String title = userUrl.getTitle();
        LocalDateTime createdAt = userUrl.getCreateTime();
        Long viewCount = userUrl.getViewCount();
        return new UserUrlResponseDto(name, originalUrl, shortUrl, title, createdAt, viewCount);
    }

}
