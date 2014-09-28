package sk.insomnia.rowingRace.dao.jdbc;

import java.sql.*;
import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

import sk.insomnia.rowingRace.connection.DbConnection;
import sk.insomnia.rowingRace.constants.RowingRaceCodeTables;
import sk.insomnia.rowingRace.dao.TeamDao;
import sk.insomnia.rowingRace.so.Racer;
import sk.insomnia.rowingRace.so.Team;

public class TeamDaoImpl implements TeamDao {

	
	
	public void delete(Team team) throws SQLException {
        Connection connection = DbConnection.getConnection(); 
		if (team.getId()!=null){			
			PreparedStatement ps = connection.prepareStatement("UPDATE RR_TEAM SET DELETED=1 WHERE TEAM_ID=?");
			ps.setLong(1, team.getId());
			ps.executeUpdate();
	        ps.close();
		} 		
	}
	public void saveOrUpdate(Team team,Long schoolID) throws SQLException {
        Connection connection = DbConnection.getConnection();
		if (team.getId()!=null){			
			PreparedStatement ps = connection.prepareStatement("UPDATE RR_TEAM SET NAME=?,CATEGORY_FK=?,MAX_RACERS=?,NUMBER_OF_ALTERNATES=? WHERE TEAM_ID=?");
			ps.setString(1, team.getName());			
			ps.setLong(2, team.getTeamCategory().getId());
            ps.setInt(3, team.getMaxRacers());
            ps.setInt(4, team.getNumberOfAlternates());
			ps.setLong(5, team.getId());
			ps.executeUpdate();
	        ps.close();
		} else {
			PreparedStatement ps = connection.prepareStatement("INSERT INTO RR_TEAM (NAME,CATEGORY_FK,MAX_RACERS,NUMBER_OF_ALTERNATES) VALUES (?,?,?,?)",Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, team.getName());
			ps.setLong(2, team.getTeamCategory().getId());
            ps.setInt(3, team.getMaxRacers());
            ps.setInt(4, team.getNumberOfAlternates());
			ps.executeUpdate();
			ResultSet rs = ps.getGeneratedKeys();
	        if (rs.next()){
	        	team.setId(rs.getLong(1));
	        }
	        ps.close();
	        rs.close();
	        
			ps = connection.prepareStatement("INSERT INTO RR_SCHOOL_TEAMS (SCHOOL_FK,TEAM_FK) VALUES (?,?)");
			ps.setLong(1, schoolID);
			ps.setLong(2, team.getId());
			ps.executeUpdate();
	        ps.close();
	        
		}
        DbConnection.releaseConnection(connection);
	}

	
	public Team findById(Long id) throws SQLException {
        Connection connection = DbConnection.getConnection();
		Team team = null;
		PreparedStatement ps = connection.prepareStatement("SELECT NAME,CATEGORY_FK,MAX_RACERS,NUMBER_OF_ALTERNATES FROM RR_TEAM WHERE TEAM_ID=?");
		ps.setLong(1, id);
		ResultSet rs = ps.executeQuery();
		rs.beforeFirst();
		if (rs.next()){
			team = new Team();
			team.setId(id);
			team.setName(rs.getString("NAME"));
            team.setMaxRacers(rs.getInt("MAX_RACERS"));
            team.setNumberOfAlternates(rs.getInt("NUMBER_OF_ALTERNATES"));
			CodeTableDaoImpl tcdi = new CodeTableDaoImpl();
			team.setTeamCategory(tcdi.findById(rs.getLong("CATEGORY_FK"),RowingRaceCodeTables.CT_TEAM_CATEGORIES));
		}
        ps.close();
        rs.close();
        
        ps = connection.prepareStatement("SELECT RACER_FK FROM RR_TEAM_RACERS TR, RR_RACER R WHERE TR.TEAM_FK=? AND TR.RACER_FK=R.RACER_ID AND R.DELETED=0");
        ps.setLong(1, id);
        rs = ps.executeQuery();
    	RacerDaoImpl rDao = new RacerDaoImpl();
        while (rs.next()){
        	if (team.getRacers() == null){
        		team.setRacers(new ArrayList<Racer>());
        	}
        	team.getRacers().add(rDao.findById(rs.getLong("RACER_FK")));
        }
        ps.close();
        rs.close();
        DbConnection.releaseConnection(connection);
		return team;
	}

}
