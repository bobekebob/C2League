package sk.insomnia.rowingRace.dao.jdbc;

import java.sql.*;

import org.apache.log4j.Logger;

import sk.insomnia.rowingRace.connection.DbConnection;
import sk.insomnia.rowingRace.dao.PerformanceDao;
import sk.insomnia.rowingRace.service.facade.ConnectivityException;
import sk.insomnia.rowingRace.so.Performance;
import sk.insomnia.rowingRace.so.PerformanceParameter;
import sk.insomnia.tools.exceptionUtils.ExceptionUtils;

public class PerformanceDaoImpl implements PerformanceDao{

	private static final Logger logger = Logger.getLogger(PerformanceDaoImpl.class.toString());
	
	@Override
	public void saveOrUpdate(Performance performance) throws SQLException,ConnectivityException {
        Connection connection = DbConnection.getConnection();
		// check whether if team has raced this round and if it has better time
		String dimension = "DIM_DISTANCE";
		String value = "FINAL_TIME";
		try {
			dimension = performance.getRaceRound().getDiscipline().getIntervals().get(0).getDimension().getAcronym();
		} catch (Exception e){
			logger.debug(ExceptionUtils.exceptionAsString(e));
		}
		if (dimension.equals("DIM_DISTANCE")){
			value = "FINAL_DISTANCE";
		}
		PreparedStatement ps = connection.prepareStatement("SELECT PERFORMANCE_ID,FINAL_TIME,FINAL_DISTANCE FROM RR_PERFORMANCE WHERE RACE_ROUND_FK=? AND TEAM_FK=?");
		ps.setLong(1,performance.getRaceRound().getId());
		ps.setLong(2,performance.getTeam().getId());
		ResultSet rs = ps.executeQuery();
		rs.beforeFirst();
		if (rs.next()){
			if (value.equals("FINAL_TIME")){
				//when interval dimension is time, final distance in designed time is important
				double dbValue = rs.getDouble("FINAL_DISTANCE");
				if (dbValue<performance.getFinalDistance()){
					performance.setId(rs.getLong("PERFORMANCE_ID"));
				} else {
					//do nothing
					return;
				}
			} else {
				//when interval dimension is distance, final time in designed distance is important
				double dbValue = rs.getDouble("FINAL_TIME");
				if (dbValue>performance.getFinalTime()){
					performance.setId(rs.getLong("PERFORMANCE_ID"));
				} else {
					return;
				}
			}
		} 
		ps.close();
		rs.close();
		
		if (performance.getId()!=null){
			logger.debug("going to save performance");
			ps = connection.prepareStatement("UPDATE RR_PERFORMANCE SET TEAM_FK=?,FINAL_DISTANCE=?, CREATED_ON=?, FINAL_TIME=? WHERE PERFORMANCE_ID=?");
			ps.setLong(1, performance.getTeam().getId());
			ps.setLong(2, performance.getFinalDistance());
			ps.setDate(3, new java.sql.Date(performance.getDate().getTime()));
			ps.setDouble(4, performance.getFinalTime());
			ps.setLong(5, performance.getId());
			ps.executeUpdate();
	        ps.close();
			logger.debug("performance saved");			
		} else {
			logger.debug("going to save update performance");
			ps = connection.prepareStatement("INSERT INTO RR_PERFORMANCE (TEAM_FK,FINAL_DISTANCE, CREATED_ON, FINAL_TIME,RACE_ROUND_FK) VALUES (?,?,?,?,?)",Statement.RETURN_GENERATED_KEYS);
			ps.setLong(1, performance.getTeam().getId());
			ps.setLong(2, performance.getFinalDistance());
			ps.setDate(3, new java.sql.Date(performance.getDate().getTime()));
			ps.setDouble(4, performance.getFinalTime());
			ps.setLong(5, performance.getRaceRound().getId());
			ps.executeUpdate();
			rs = ps.getGeneratedKeys();
	        if (rs.next()){
	        	performance.setId(rs.getLong(1));
	        }
	        ps.close();
	        rs.close();			
			logger.debug("performance updated");
		}

		logger.debug("going to save performance paramters (count:"+performance.getParameters().size()+")");
		if (performance.getParameters().size()>0){
			logger.debug("going to delete performance parameters for performance_id="+performance.getId());
			ps = connection.prepareStatement("DELETE FROM RR_PERFORMANCE_PARAMS WHERE PERFORMANCE_FK=?");
			ps.setLong(1, performance.getId());
			ps.executeUpdate();
			ps.close();
			logger.debug("performance parameters deleted");
		}

		for (int i = 0; i<performance.getParameters().size(); i++){
			PerformanceParameter pp = performance.getParameters().get(i);					
			ps = connection.prepareStatement("INSERT INTO RR_PERFORMANCE_PARAMS (PERFORMANCE_FK,IMMEDIATE_TIME,IMMEDIATE_SPEED,IMMEDIATE_CADENCE, ORDER_NO,RACER_FK) VALUES (?,?,?,?,?,?)");
			ps.setLong(1, performance.getId());
			ps.setDouble(2, pp.getTime());
			ps.setDouble(3, pp.getSpeed());
			ps.setInt(4,pp.getCadence());
			ps.setInt(5, i);
			ps.setLong(6, pp.getRacedBy().getId());
			ps.executeUpdate();
			ps.close();
			logger.debug("save performance parameter #"+i);
		}
		logger.debug("performance paramters saved");
		DbConnection.releaseConnection(connection);
	}

}
