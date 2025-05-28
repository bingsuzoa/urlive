package com.urlive.controller.dto.common;

import com.urlive.controller.dto.board.BoardDto;
import com.urlive.controller.dto.user.UserResponseDto;
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

    public static List<BoardDto> getBoardDto(User user) {
        Set<UserUrl> urls = user.getUrls();
        List<BoardDto> boardDto = new ArrayList<>();

        for(UserUrl userUrl : urls) {
            String name = user.getName();
            String shortUrl = userUrl.getShortUrl();
            String title = userUrl.getTitle();
            LocalDateTime createdAt = userUrl.getCreateTime();
            Long viewCount = userUrl.getViewCount();

            boardDto.add(new BoardDto(name, shortUrl, title, createdAt, viewCount));
        }
        return boardDto;
    }


}
