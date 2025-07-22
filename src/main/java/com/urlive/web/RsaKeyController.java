package com.urlive.web;

import com.urlive.service.RsaService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.PublicKey;
import java.util.Base64;
import java.util.Map;

@RestController
public class RsaKeyController {

    public RsaKeyController(RsaService rsaService) {
        this.rsaService = rsaService;
    }

    private final RsaService rsaService;

    @GetMapping("/public-key")
    public Map<String, String> getPublicKey() {
        PublicKey publicKey = rsaService.getPublicKey();
        String publicKeyPEM = Base64.getEncoder().encodeToString(publicKey.getEncoded());

        return Map.of("publicKey",
                "-----BEGIN PUBLIC KEY-----\n" +
                        publicKeyPEM +
                        "\n-----END PUBLIC KEY-----");
    }
}
