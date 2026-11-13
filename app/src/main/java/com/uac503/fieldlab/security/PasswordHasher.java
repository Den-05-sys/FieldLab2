
package com.uac503.fieldlab.security;

import android.util.Base64;

import java.security.SecureRandom;
import java.security.MessageDigest;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class PasswordHasher {

    private static final int ITERATIONS = 120000;
    private static final int KEY_LENGTH = 256;

    public static String hash(String password) throws Exception {

        byte[] salt = new byte[16];
        new SecureRandom().nextBytes(salt);

        byte[] hash = generate(password, salt);

        return Base64.encodeToString(salt, Base64.NO_WRAP)
                + ":"
                + Base64.encodeToString(hash, Base64.NO_WRAP);
    }

    public static boolean verify(
            String password, String storedHash) throws Exception {

        String[] parts = storedHash.split(":");

        if (parts.length != 2) return false;

        byte[] salt = Base64.decode(parts[0], Base64.NO_WRAP);
        byte[] expected = Base64.decode(parts[1], Base64.NO_WRAP);

        byte[] actual = generate(password, salt);

        return MessageDigest.isEqual(expected, actual);
    }

    private static byte[] generate(
            String password, byte[] salt) throws Exception {

        PBEKeySpec spec = new PBEKeySpec(
                password.toCharArray(),
                salt,
                ITERATIONS,
                KEY_LENGTH
        );

        try {
            SecretKeyFactory factory =
                    SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");

            return factory.generateSecret(spec).getEncoded();

        } finally {
            spec.clearPassword();
        }
    }
}

