package model.services;

import model.dao.IBankDAO;
import model.dao.IAgencyDAO;
import model.dao.impl.MySQLBankDAO;
import model.dao.impl.MySQLAgencyDAO;
import model.entities.Bank;
import model.entities.Agency;

import java.util.List;

public class BankService {
    private IBankDAO bankDAO = new MySQLBankDAO();
    private IAgencyDAO agencyDAO = new MySQLAgencyDAO();

    public Bank getBank(int id) throws Exception {
        return bankDAO.findById(id);
    }

    public List<Agency> listAgencies() throws Exception {
        return agencyDAO.findAll();
    }
}
