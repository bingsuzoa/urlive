package com.urlive.domain.user;


import com.urlive.controller.dto.board.BoardDto;
import com.urlive.controller.dto.common.DtoFactory;
import com.urlive.controller.dto.user.UserCreateRequestDto;
import com.urlive.controller.dto.user.UserResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private final UserRepository userRepository;

    @Transactional
    public UserResponseDto saveUser(UserCreateRequestDto userCreateRequestDto) {
        User user = userRepository.save(userCreateRequestDto.toEntity());
        return DtoFactory.createUserResponseDto(user);
    }

    @Transactional(readOnly = true)
    public List<BoardDto> getUrlsByUserId(Long id) {
        Optional<User> optionalUser = userRepository.findUrlsById(id);
        if (optionalUser.isEmpty()) {
            throw new IllegalArgumentException(User.NOT_EXIST_USER_ID);
        }
        return DtoFactory.getBoardDto(optionalUser.get());
    }
}
