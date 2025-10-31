package utils.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * Utilitário para realizar hash e verificação de senhas de login.
 * Usa SHA-256 (nativo do Java) por simplicidade, MAS DEVE SER SUBSTITUÍDO
 * por BCrypt ou PBKDF2 em ambiente de produção.
 */
public class PasswordUtil {

    /**
     * Gera um hash SHA-256 de uma string.
     * Nota: Em produção, o salt deve ser armazenado separadamente. Aqui é simplificado.
     */
    public static String hashPassword(String plainText) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(plainText.getBytes());
            byte[] hashedBytes = md.digest();
            // Converte os bytes do hash para uma string Base64 para armazenamento
            return Base64.getEncoder().encodeToString(hashedBytes);
        } catch (NoSuchAlgorithmException e) {
            // Este erro é improvável, pois SHA-256 é padrão em JVMs
            throw new RuntimeException("Erro ao gerar hash: " + e.getMessage(), e);
        }
    }

    /**
     * Verifica se uma senha corresponde a um hash.
     */
    // Dentro de src/main/java/utils/security/PasswordUtil.java

// Adicione este método (ou ajuste o seu método de verificação):
    public static boolean checkPassword(String plainPassword, String storedHash) {
        if (plainPassword == null || storedHash == null) {
            return false;
        }

        // 1. Gera o hash da senha fornecida (texto simples)
        String inputHash = hashPassword(plainPassword); // Reusa o método de hash

        // 2. Compara com o hash armazenado no banco de dados
        return inputHash.equals(storedHash);
    }
}