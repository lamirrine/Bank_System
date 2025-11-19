package model.utils;

import java.nio.charset.StandardCharsets; // ADICIONAR ESTA IMPORTAÇÃO
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class PasswordUtil {

    public static String hashPassword(String plainText) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            md.update(plainText.getBytes(StandardCharsets.UTF_8));

            byte[] hashedBytes = md.digest();
            return Base64.getEncoder().encodeToString(hashedBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Erro ao gerar hash: " + e.getMessage(), e);
        }
    }

    public static boolean checkPassword(String plainPassword, String storedHash) {
        if (plainPassword == null || storedHash == null) {
            return false;
        }
        String inputHash = hashPassword(plainPassword);
        return inputHash.equals(storedHash);
    }

}