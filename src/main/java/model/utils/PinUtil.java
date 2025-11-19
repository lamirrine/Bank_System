package model.utils;

/**
 * Utilitário para gerenciar o PIN de transação (4 dígitos).
 * Usa o mesmo mecanismo SHA-256 do PasswordUtil.
 */
public class PinUtil {

    /**
     * Gera o hash seguro de um PIN.
     */
    public static String hashPin(String plainPin) {
        if (plainPin == null || !plainPin.matches("\\d{4}")) {
            throw new IllegalArgumentException("O PIN deve conter exatamente 4 dígitos numéricos.");
        }
        return PasswordUtil.hashPassword(plainPin); // Reutiliza a função de hash
    }

    /**
     * Verifica se um PIN corresponde a um hash.
     */
    public static boolean checkPin(String plainPin, String storedHash) {
        if (storedHash == null) {
            return false;
        }
        return PasswordUtil.checkPassword(plainPin, storedHash);
    }
}