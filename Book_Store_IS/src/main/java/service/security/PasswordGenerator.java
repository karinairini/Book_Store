package service.security;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class PasswordGenerator {
    private static String hashPassword(String password, String salt) {
        // Secured Hash Algorithm - 256
        // 1 byte = 8 biți
        // 1 byte = 1 char
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(salt.getBytes(StandardCharsets.UTF_8));

            byte[] hashedPassword = messageDigest.digest(password.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hashedPassword);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Could not hash password.", e);
        }
    }

    public static String generateSalt() {
        try {
            SecureRandom secureRandom = SecureRandom.getInstanceStrong();
            byte[] salt = new byte[16];
            secureRandom.nextBytes(salt); //umple sirul de octeti cu valori aleatoare

            //Encoder este responsabil pentru transformarea datelor binare intr-un sir Base64
            //salt-ul este un sir de octeti, trimis encoder-ului pentru a fi codificat intr-un sir Base64
            return Base64.getEncoder().encodeToString(salt);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Could not generate salt.", e);
        }
    }

    public static String generatePassword(String password, String salt) {
        return hashPassword(password, salt);
    }
}
