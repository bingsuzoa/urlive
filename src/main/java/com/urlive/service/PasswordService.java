package com.urlive.service;

import com.urlive.domain.user.passwordHistory.PasswordHistory;
import com.urlive.domain.user.passwordHistory.PasswordHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PasswordService {

    @Autowired
    public PasswordService(
            PasswordEncoder passwordEncoder,
            PasswordHistoryRepository passwordHistoryRepository
    ) {
        this.passwordEncoder = passwordEncoder;
        this.passwordHistoryRepository = passwordHistoryRepository;
    }

    private static final int FIRST_PAGE = 0;
    private static final int PASSWORD_HISTORY_LIMIT = 2;
    private static final String PASSWORD_ALREADY_USED = "기존에 사용하던 비밀번호와 동일합니다. 다시 작성해주세요.";

    private final PasswordEncoder passwordEncoder;
    private final PasswordHistoryRepository passwordHistoryRepository;

    public String encode(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    public boolean matches(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    public String changePassword(Long id, String rawPassword) {
        isPossiblePasswordToChange(id, rawPassword);
        return passwordEncoder.encode(rawPassword);
    }

    private void isPossiblePasswordToChange(Long id, String rawNewPassword) {
        List<PasswordHistory> histories = passwordHistoryRepository.findRecentHistories(
                id, PageRequest.of(FIRST_PAGE, PASSWORD_HISTORY_LIMIT));

        histories.stream()
                .filter(history -> matches(rawNewPassword, history.getPassword()))
                .findFirst()
                .ifPresent(history -> {
                    throw new IllegalArgumentException(PASSWORD_ALREADY_USED);
                });

    }
}
