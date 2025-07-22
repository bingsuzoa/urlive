package com.urlive.service;

import org.springframework.stereotype.Service;

import java.security.*;

@Service
public class RsaService {

    public RsaService() throws NoSuchAlgorithmException {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048);
        this.keyPair = generator.generateKeyPair();
    }

    private final KeyPair keyPair;

    public PublicKey getPublicKey() {
        return keyPair.getPublic();
    }

    public PrivateKey getPrivateKey() {
        return keyPair.getPrivate();
    }


}
