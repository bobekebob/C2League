package sk.insomnia.rowingRace.dao;

import java.sql.SQLException;

import sk.insomnia.rowingRace.so.Team;

public interface TeamDao {

	public void saveOrUpdate(Team team,Long schoolId) throws SQLException;
	public void delete(Team team) throws SQLException;
	public Team findById(Long id) throws SQLException;
}
