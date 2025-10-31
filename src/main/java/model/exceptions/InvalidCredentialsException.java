package model.exceptions;

/**
 * Exceção lançada quando as credenciais fornecidas para autenticação (login)
 * são inválidas (e-mail não encontrado ou senha incorreta).
 */
public class InvalidCredentialsException extends RuntimeException {

    // Construtor que aceita uma mensagem
    public InvalidCredentialsException(String message) {
        super(message);
    }

    // Construtor padrão
    public InvalidCredentialsException() {
        super("Credenciais de usuário inválidas.");
    }
}