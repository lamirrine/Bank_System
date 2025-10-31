package model.exceptions;

// Exceção de negócio para falhas específicas no processo de registo do cliente.
public class CustomerRegistrationException extends RuntimeException {

    // Construtor que aceita uma mensagem de erro
    public CustomerRegistrationException(String message) {
        super(message);
    }

    // Construtor que aceita a mensagem e a causa da exceção
    public CustomerRegistrationException(String message, Throwable cause) {
        super(message, cause);
    }
}