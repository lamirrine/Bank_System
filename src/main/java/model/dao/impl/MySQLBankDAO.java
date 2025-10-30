package model.dao.impl;

import model.dao.IBankDAO;
import model.entities.Bank;
import config.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class MySQLBankDAO implements IBankDAO {

    @Override
    public Bank findById(int id) throws Exception {
        String sql = "SELECT * FROM banks WHERE id = ?";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Bank b = new Bank();
                    b.setId(rs.getInt("id"));
                    b.setName(rs.getString("name"));
                    b.setCode(rs.getString("code"));
                    b.setSupportPhone(rs.getString("support_phone"));
                    return b;
                }
            }
        } catch (Exception ex) {
            // table may not exist in demo DB; return null for demo
        }
        return null;
    }

    @Override
    public List<Bank> findAll() throws Exception {
        List<Bank> list = new ArrayList<>();
        String sql = "SELECT * FROM banks";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Bank b = new Bank();
                b.setId(rs.getInt("id"));
                b.setName(rs.getString("name"));
                b.setCode(rs.getString("code"));
                b.setSupportPhone(rs.getString("support_phone"));
                list.add(b);
            }
        } catch (Exception ex) {
            // ignore if table missing
        }
        return list;
    }

    @Override
    public int save(Bank bank) throws Exception {
        String sql = "INSERT INTO banks(name,code,support_phone) VALUES(?,?,?)";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, bank.getName());
            ps.setString(2, bank.getCode());
            ps.setString(3, bank.getSupportPhone());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (Exception ex) {
            // ignore for demo
        }
        return -1;
    }
}
