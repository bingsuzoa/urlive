package com.urlive.domain.user.passwordHistory;

import com.urlive.service.RsaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.util.Base64;
import java.util.List;

@Service
public class PasswordService {

    @Autowired
    public PasswordService(
            RsaService rsaService,
            PasswordEncoder passwordEncoder,
            PasswordHistoryRepository passwordHistoryRepository
    ) {
        this.rsaService = rsaService;
        this.passwordEncoder = passwordEncoder;
        this.passwordHistoryRepository = passwordHistoryRepository;
    }

    private static final String RSA_PKCS1_CIPHER = "RSA/ECB/PKCS1Padding";
    private static final String PASSWORD_ALREADY_USED = "기존에 사용하던 비밀번호와 동일합니다. 다시 작성해주세요.";
    private static final String RSA_PASSWORD_DECODE_FAIL = "비밀번호 복호화에 실패했습니다.";
    public static final int FIRST_PAGE = 0;
    public static final int PASSWORD_HISTORY_LIMIT = 2;

    private final RsaService rsaService;
    private final PasswordEncoder passwordEncoder;
    private final PasswordHistoryRepository passwordHistoryRepository;

    public String encode(String rsaEncodedPassword) {
        String rsaDecodedPassword = getDecryptedPassword(rsaEncodedPassword);
        return passwordEncoder.encode(rsaDecodedPassword);
    }

    public boolean matches(String rsaEncodedPassword, String encodedPassword) {
        String rsaDecodedPassword = getDecryptedPassword(rsaEncodedPassword);
        System.out.println("복호화된 비밀번호: " + rsaDecodedPassword);
        return passwordEncoder.matches(rsaDecodedPassword, encodedPassword);
    }

    public String changePassword(Long id, String rsaEncodedPassword) {
        String rsaDecodedPassword = getDecryptedPassword(rsaEncodedPassword);
        isPossiblePasswordToChange(id, rsaDecodedPassword);
        return passwordEncoder.encode(rsaDecodedPassword);
    }

    private void isPossiblePasswordToChange(Long id, String rawNewPassword) {
        List<PasswordHistory> histories = passwordHistoryRepository.findRecentHistories(
                id, PageRequest.of(FIRST_PAGE, PASSWORD_HISTORY_LIMIT));

        histories.stream()
                .filter(history -> passwordEncoder.matches(rawNewPassword, history.getPassword()))
                .findFirst()
                .ifPresent(history -> {
                    throw new IllegalArgumentException(PASSWORD_ALREADY_USED);
                });
    }

    private String getDecryptedPassword(String encryptedPassword) {
        try {
            Cipher cipher = Cipher.getInstance(RSA_PKCS1_CIPHER);
            PrivateKey privateKey = rsaService.getPrivateKey();
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedPassword));
            return new String(decryptedBytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new IllegalArgumentException(RSA_PASSWORD_DECODE_FAIL);
        }
    }
}
