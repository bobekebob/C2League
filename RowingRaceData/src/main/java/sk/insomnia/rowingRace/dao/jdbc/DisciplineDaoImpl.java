package sk.insomnia.rowingRace.dao.jdbc;

import java.sql.*;
import java.util.ArrayList;

import sk.insomnia.rowingRace.connection.DbConnection;
import sk.insomnia.rowingRace.dao.DisciplineDao;
import sk.insomnia.rowingRace.service.facade.ConnectivityException;
import sk.insomnia.rowingRace.so.Discipline;
import sk.insomnia.rowingRace.so.Interval;

public class DisciplineDaoImpl implements DisciplineDao {

	
	public void saveOrUpdate(Discipline discipline, Long discisplineCategoryId) throws SQLException,
			ConnectivityException {
        Connection connection = DbConnection.getConnection();
		if (discipline.getId()!=null)
		{			
			PreparedStatement ps = connection.prepareStatement("UPDATE RR_DISCIPLINE SET NAME=? WHERE DISCIPLINE_ID=?");
			ps.setString(1, discipline.getName());
			ps.setLong(2, discipline.getId());
			ps.executeUpdate();
			ps.close();
		} else {			
			PreparedStatement ps = connection.prepareStatement("INSERT INTO RR_DISCIPLINE (NAME) VALUES (?)",Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, discipline.getName());
			ps.executeUpdate();
			ResultSet rs = ps.getGeneratedKeys();
	        if (rs.next()){
	        	discipline.setId(rs.getLong(1));
	        }
	        rs.close();	       
	        
	        ps = connection.prepareStatement("INSERT INTO RR_CATEGORY_DISCIPLINES (D_CATEGORY_FK,DISCIPLINE_FK) VALUES (?,?)");
			ps.setLong(1, discisplineCategoryId);
			ps.setLong(2, discipline.getId());
			ps.executeUpdate();
	        rs.close();	       
	        
	        
		}
        DbConnection.releaseConnection(connection);
	}

	
	public Discipline getById(Long id) throws SQLException,
			ConnectivityException {
		Discipline discipline = null;
        Connection connection = DbConnection.getConnection();
		PreparedStatement ps = connection.prepareStatement("SELECT NAME FROM RR_DISCIPLINE WHERE DISCIPLINE_ID=?");
		ps.setLong(1, id);		
		ResultSet rs = ps.executeQuery();				
		if (rs.next()){
			discipline = new Discipline();
			discipline.setId(id);
			discipline.setName(rs.getString("NAME"));
			
			ps = connection.prepareStatement("SELECT INTERVAL_FK FROM RR_DISCIPLINE_INTERVALS WHERE DISCIPLINE_FK=?");
			ps.setLong(1, id);
			rs = ps.executeQuery();
			IntervalDaoImpl iDao = new IntervalDaoImpl();
			while (rs.next()){
				if (discipline.getIntervals() == null){
					discipline.setIntervals(new ArrayList<Interval>());					
				}
				discipline.getIntervals().add(iDao.getById(rs.getLong("INTERVAL_FK")));
			}
			ps.close();
			rs.close();
		}
		DbConnection.releaseConnection(connection);
		return discipline;
	}

}
