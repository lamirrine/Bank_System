package model.dao;

import model.entities.Bank;
import java.sql.SQLException;

public interface IBankDAO {
    // Busca os dados globais de configuração do banco
    Bank getBankInfo() throws SQLException;

    // Atualiza configurações (uso exclusivo para ADMIN)
    void updateInfo(Bank bank) throws SQLException;
}