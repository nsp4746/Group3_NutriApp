package com.group3.nutriapp.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Crypto {
    /**
     * Returns the SHA1 digest of a given string
     * @param text String to compute hash of
     * @return Computed hash in lowercase string
     */
    public static String makeSHA1(String text) {
        // Compute the hash
        MessageDigest hasher;
        try { hasher = MessageDigest.getInstance("SHA-1"); }
        catch (NoSuchAlgorithmException ex) { return null; }
        byte[] digest = hasher.digest(text.getBytes(StandardCharsets.US_ASCII));

        // Convert hash to string
        final char[] HEX_DIGITS = ("0123456789abcdef".toCharArray());
        final char[] hex = new char[digest.length * 2];
        for (int i = 0; i < digest.length; ++i) {
            int b = digest[i] & 0xFF;
            hex[i * 2] = HEX_DIGITS[b >>> 4];
            hex[(i * 2) + 1] = HEX_DIGITS[b & 0xF];
        }

        // Return the computed string
        return String.valueOf(hex);
    }
}
