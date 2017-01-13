package com.merrill.examples.oauth2.commons.utils

import org.springframework.security.oauth2.common.OAuth2AccessToken
import org.springframework.security.oauth2.common.OAuth2RefreshToken
import org.springframework.security.oauth2.common.util.SerializationUtils
import org.springframework.security.oauth2.provider.OAuth2Authentication

import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

/**
 * Created by upaulm2 on 1/13/17.
 */
final class Oauth2SerializationUtils {

    public static String extractTokenKey(String value) {
        if (value == null) {
            return null
        }
        MessageDigest digest
        try {
            digest = MessageDigest.getInstance("MD5")
        }
        catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("MD5 algorithm not available.  Fatal (should be in the JDK).")
        }

        try {
            byte[] bytes = digest.digest(value.getBytes("UTF-8"))
            return String.format("%032x", new BigInteger(1, bytes))
        }
        catch (UnsupportedEncodingException e) {
            throw new IllegalStateException("UTF-8 encoding not available.  Fatal (should be in the JDK).")
        }
    }

    public static String serializeAccessToken(OAuth2AccessToken token) {
        Base64.encoder.encodeToString(SerializationUtils.serialize(token))
    }

    public static String serializeRefreshToken(OAuth2RefreshToken token) {
        Base64.encoder.encodeToString(SerializationUtils.serialize(token))
    }

    public static String serializeAuthentication(OAuth2Authentication authentication) {
        Base64.encoder.encodeToString(SerializationUtils.serialize(authentication))
    }

    public static OAuth2AccessToken deserializeAccessToken(String tokenObj) {
        def tokenBytes = Base64.decoder.decode(tokenObj)
        SerializationUtils.deserialize(tokenBytes)
    }

    public static OAuth2RefreshToken deserializeRefreshToken(String tokenObj) {
        def tokenBytes = Base64.decoder.decode(tokenObj)
        SerializationUtils.deserialize(tokenBytes)
    }

    public static OAuth2Authentication deserializeAuthentication(String authentication) {
        def authenticationBytes = Base64.decoder.decode(authentication)
        SerializationUtils.deserialize(authenticationBytes)
    }
}
