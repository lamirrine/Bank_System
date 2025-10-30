package model.exceptions;

import java.sql.SQLException;

// Exceção unchecked (RuntimeException) para evitar propagação desnecessária no service
public class DatabaseException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public DatabaseException(String msg) {
        super(msg);
    }

    public DatabaseException(String msg, SQLException cause) {
        super(msg, cause);
    }
}