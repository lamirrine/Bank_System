package model.dao;

import model.entities.Agency;
import java.util.List;
import java.sql.SQLException;

public interface IAgencyDAO {
    // Busca por Chave Primária
    Agency findById(int agencyId) throws SQLException;

    // Para listar todas as agências (útil para relatórios ou interface de funcionário)
    List<Agency> findAll() throws SQLException;

    // Persistência
    void save(Agency agency) throws SQLException;
    void update(Agency agency) throws SQLException;
}