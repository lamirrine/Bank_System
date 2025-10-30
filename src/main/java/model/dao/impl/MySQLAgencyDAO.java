package model.dao.impl;

import model.dao.IAgencyDAO;
import model.entities.Agency;
import config.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class MySQLAgencyDAO implements IAgencyDAO {

    @Override
    public Agency findById(int id) throws Exception {
        String sql = "SELECT * FROM agencies WHERE id = ?";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Agency a = new Agency();
                    a.setId(rs.getInt("id"));
                    a.setName(rs.getString("name"));
                    a.setAddress(rs.getString("address"));
                    a.setPhone(rs.getString("phone"));
                    return a;
                }
            }
        } catch (Exception ex) {
            // table may not exist
        }
        return null;
    }

    @Override
    public List<Agency> findAll() throws Exception {
        List<Agency> list = new ArrayList<>();
        String sql = "SELECT * FROM agencies";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Agency a = new Agency();
                a.setId(rs.getInt("id"));
                a.setName(rs.getString("name"));
                a.setAddress(rs.getString("address"));
                a.setPhone(rs.getString("phone"));
                list.add(a);
            }
        } catch (Exception ex) {
            // ignore
        }
        return list;
    }

    @Override
    public int save(Agency agency) throws Exception {
        String sql = "INSERT INTO agencies(name,address,phone) VALUES(?,?,?)";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, agency.getName());
            ps.setString(2, agency.getAddress());
            ps.setString(3, agency.getPhone());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (Exception ex) {
            // ignore
        }
        return -1;
    }
}
