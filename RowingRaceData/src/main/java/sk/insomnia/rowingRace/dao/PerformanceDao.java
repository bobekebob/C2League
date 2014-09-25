package sk.insomnia.rowingRace.dao;

import java.sql.SQLException;

import sk.insomnia.rowingRace.service.facade.ConnectivityException;
import sk.insomnia.rowingRace.so.Performance;

public interface PerformanceDao {

	public void saveOrUpdate(Performance performance)  throws SQLException,ConnectivityException;
}
