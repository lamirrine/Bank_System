package model.dao;

import model.entities.Agency;
import java.util.List;

public interface IAgencyDAO {
    Agency findById(int id) throws Exception;
    List<Agency> findAll() throws Exception;
    int save(Agency agency) throws Exception;
}
