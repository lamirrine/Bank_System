package model.dao;

import model.entities.Bank;
import java.util.List;

public interface IBankDAO {
    Bank findById(int id) throws Exception;
    List<Bank> findAll() throws Exception;
    int save(Bank bank) throws Exception;
}
