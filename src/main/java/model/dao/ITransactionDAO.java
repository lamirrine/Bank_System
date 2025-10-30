package model.dao;

import model.entities.Transaction;
import java.util.Date;
import java.util.List;
import java.sql.SQLException;

public interface ITransactionDAO {
    // Persistência (Registrar qualquer operação)
    void save(Transaction transaction) throws SQLException;

    // Requisito Extrato (busca transações por período)
    List<Transaction> findByAccountId(int accountId, Date start, Date end) throws SQLException;

    // Requisito Dashboard (Últimas Transações)
    List<Transaction> findRecentByAccountId(int accountId, int limit) throws SQLException;
}