package model.dao.impl;

import config.DatabaseConnection;
import model.dao.IAgencyDAO;
import model.entities.Agency;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AgencyDAO implements IAgencyDAO {

    private static final String FIND_ALL = "SELECT * FROM agency";

    @Override
    public List<Agency> findAll() throws SQLException {
        List<Agency> agencies = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(FIND_ALL);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Agency agency = new Agency();
                agency.setAgencyId(rs.getInt("agency_id"));
                agency.setName(rs.getString("name"));
                // ... Mapear todos os atributos (LocalTime requer cuidado com conversão de TIME SQL)
                agencies.add(agency);
            }
            return agencies;

        } catch (SQLException e) {
            System.err.println("Erro ao buscar agências: " + e.getMessage());
            throw e;
        }
    }

    // ... Implementação dos outros métodos
    @Override
    public Agency findById(int agencyId) throws SQLException { return null; }
    @Override
    public void save(Agency agency) throws SQLException { /* Lógica de INSERT */ }
    @Override
    public void update(Agency agency) throws SQLException { /* Lógica de UPDATE */ }
}