package sk.insomnia.rowingRace.dao.jdbc;

import java.sql.*;

import sk.insomnia.rowingRace.connection.DbConnection;
import sk.insomnia.rowingRace.constants.RowingRaceCodeTables;
import sk.insomnia.rowingRace.dao.AddressDao;
import sk.insomnia.rowingRace.so.Address;

public class AddressDaoImpl implements AddressDao {


	public void saveOrUpdate(Address address) throws SQLException {
		Connection connection = DbConnection.getConnection();
		if (address.getId()!=null){
			
			PreparedStatement ps = connection.prepareStatement("UPDATE RR_ADDRESS SET STREET=?,CITY=?,ZIP=?,STATE=?,COUNTRY_FK=? WHERE ADDRESS_ID=?");
			ps.setString(1, address.getStreet());
			ps.setString(2, address.getCity());
			ps.setString(3, address.getZip());
			ps.setString(4, address.getState());
			ps.setLong(5, address.getCountry().getId());
			ps.setLong(6, address.getId());
			ps.executeUpdate();
			ps.close();
		} else {
			PreparedStatement ps = connection.prepareStatement("INSERT INTO RR_ADDRESS (STREET,CITY,ZIP,STATE,COUNTRY_FK) VALUES (?,?,?,?,?)",Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, address.getStreet());
			ps.setString(2, address.getCity());
			ps.setString(3, address.getZip());
			ps.setString(4, address.getState());
			ps.setLong(5, address.getCountry().getId());
			ps.executeUpdate();
			ResultSet rs = ps.getGeneratedKeys();
	        if (rs.next()){
	            address.setId(rs.getLong(1));
	        }
	        ps.close();
	        rs.close();	        			
		}
		DbConnection.releaseConnection(connection);
	}

	public Address getById(Long id) throws SQLException {
		Address address = null;
        Connection connection = DbConnection.getConnection();
        PreparedStatement ps = connection.prepareStatement("SELECT STREET,CITY,ZIP,STATE,COUNTRY_FK FROM RR_ADDRESS WHERE ADDRESS_ID=?");
		ps.setLong(1, id);
		ResultSet rs = ps.executeQuery();
		rs.beforeFirst();
		if (rs.next()){
			address = new Address();
			address.setId(id);
			address.setStreet(rs.getString("STREET"));
			address.setCity(rs.getString("CITY"));
			address.setZip(rs.getString("ZIP"));
			address.setState(rs.getString("STATE"));
			if (rs.getLong("COUNTRY_FK")>0){
				CodeTableDaoImpl cDao = new CodeTableDaoImpl();
				address.setCountry(cDao.findById(rs.getLong("COUNTRY_FK"),RowingRaceCodeTables.CT_COUNTRIES));
			}
		}
        DbConnection.releaseConnection(connection);
        return address;
	}

}
