package utils.security;

import java.nio.charset.StandardCharsets; // ADICIONAR ESTA IMPORTAÇÃO
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class PasswordUtil {

    public static String hashPassword(String plainText) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            // --- CORREÇÃO CRÍTICA: USAR UTF-8 EXPLICITAMENTE ---
            // Força o uso do UTF-8, garantindo que o hash gerado é o mesmo
            // no registo e no login, independentemente da codificação padrão do sistema.
            md.update(plainText.getBytes(StandardCharsets.UTF_8));
            // ----------------------------------------------------

            byte[] hashedBytes = md.digest();
            return Base64.getEncoder().encodeToString(hashedBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Erro ao gerar hash: " + e.getMessage(), e);
        }
    }

    // O método checkPassword() continua o mesmo:
    public static boolean checkPassword(String plainPassword, String storedHash) {
        if (plainPassword == null || storedHash == null) {
            return false;
        }
        String inputHash = hashPassword(plainPassword);
        return inputHash.equals(storedHash);
    }
}