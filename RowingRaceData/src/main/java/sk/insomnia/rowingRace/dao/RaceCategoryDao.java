package sk.insomnia.rowingRace.dao;

import sk.insomnia.rowingRace.so.EnumEntitySO;

import java.sql.SQLException;
import java.util.List;

public interface RaceCategoryDao {

    public EnumEntitySO findById(Long id) throws SQLException;

    public List<EnumEntitySO> getAll() throws SQLException;

    public void saveOrUpdate(EnumEntitySO raceCategory) throws SQLException;

}
