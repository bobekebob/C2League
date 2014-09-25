package sk.insomnia.rowingRace.dao;

import java.sql.SQLException;

import sk.insomnia.rowingRace.so.Address;

public interface AddressDao {

	public void saveOrUpdate(Address address) throws SQLException;
	public Address getById(Long id) throws SQLException;
}
