/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 *
 * @author kiboi
 */
public class PasswordUtils {
    
    private static final int ITERATIONS = 600_000;
    private static final int SALT_LENGTH = 16;
    private static final int KEY_LENGTH = 256;

    private static final SecureRandom RANDOM =
            new SecureRandom();

    private PasswordUtils() {
    }

    public static String hashPassword(char[] password)
            throws GeneralSecurityException {

        byte[] salt = new byte[SALT_LENGTH];
        RANDOM.nextBytes(salt);

        byte[] hash = deriveKey(
                password, salt, ITERATIONS
        );

        return "pbkdf2_sha256"
                + "$" + ITERATIONS
                + "$" + Base64.getEncoder()
                    .encodeToString(salt)
                + "$" + Base64.getEncoder()
                    .encodeToString(hash);
    }

    public static boolean verifyPassword(
            char[] password,
            String storedHash)
            throws GeneralSecurityException {

        if (storedHash == null) {
            return false;
        }

        String[] parts = storedHash.split("\\$");

        if (parts.length != 4
                || !parts[0].equals("pbkdf2_sha256")) {
            return false;
        }

        int iterations;
        byte[] salt;
        byte[] expectedHash;

        try {
            iterations = Integer.parseInt(parts[1]);

            if (iterations < 100_000
                    || iterations > 2_000_000) {
                return false;
            }

            salt = Base64.getDecoder()
                    .decode(parts[2]);

            expectedHash = Base64.getDecoder()
                    .decode(parts[3]);

        } catch (IllegalArgumentException ex) {
            return false;
        }

        if (salt.length != SALT_LENGTH
                || expectedHash.length != KEY_LENGTH / 8) {
            return false;
        }

        byte[] actualHash = deriveKey(
                password, salt, iterations
        );

        return MessageDigest.isEqual(
                expectedHash, actualHash
        );
    }

    private static byte[] deriveKey(
            char[] password,
            byte[] salt,
            int iterations)
            throws GeneralSecurityException {

        PBEKeySpec spec = new PBEKeySpec(
                password, salt, iterations, KEY_LENGTH
        );

        try {
            SecretKeyFactory factory =
                    SecretKeyFactory.getInstance(
                        "PBKDF2WithHmacSHA256"
                    );

            return factory.generateSecret(spec)
                    .getEncoded();

        } finally {
            spec.clearPassword();
        }
    }
    
}
