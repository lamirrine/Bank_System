package model.dao;

import model.entities.User;
import java.sql.SQLException;

public interface IUserDAO {
    boolean updatePassword(int userId, String newPasswordHash) throws SQLException;

    // Busca por Chave Primária
    User findById(int userId) throws SQLException;

    // Requisito de Login
    User findByEmail(String email) throws SQLException;

    // Persistência
    void save(User user) throws SQLException;
    void update(User user) throws SQLException;

    // Auxiliar para lógica de herança no Service
    String findUserType(int userId) throws SQLException;
}