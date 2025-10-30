package model.dao;

import model.entities.User;

public interface IUserDAO {
    User findByUsername(String username) throws Exception;
    User findById(int id) throws Exception;
    int save(User user) throws Exception;
}
