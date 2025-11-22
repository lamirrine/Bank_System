package model.dao;

import model.entities.Agency;
import java.util.List;
import java.sql.SQLException;

public interface IAgencyDAO {

    Agency findById(int agencyId) throws SQLException;

    List<Agency> findAll() throws SQLException;

    // Persistência
    void save(Agency agency) throws SQLException;
    void update(Agency agency) throws SQLException;
}