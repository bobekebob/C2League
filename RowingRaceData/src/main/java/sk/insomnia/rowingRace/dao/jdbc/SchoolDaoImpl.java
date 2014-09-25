package sk.insomnia.rowingRace.dao.jdbc;

import sk.insomnia.rowingRace.connection.DbConnection;
import sk.insomnia.rowingRace.dao.SchoolDao;
import sk.insomnia.rowingRace.so.EnumEntity;
import sk.insomnia.rowingRace.so.School;
import sk.insomnia.rowingRace.so.Team;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class SchoolDaoImpl implements SchoolDao {

    public static final String TB_CATEGORIES = "RR_SCHOOL_RACE_CATEGORIES";

    public School findById(Long id) throws SQLException {
        School school = null;
        Connection connection = DbConnection.getConnection();
        PreparedStatement ps = connection.prepareStatement("SELECT NAME,SCHOOL_KEY,ADDRESS_FK FROM RR_SCHOOL WHERE SCHOOL_ID=?");
        ps.setLong(1, id);
        ResultSet rs = ps.executeQuery();

        if (rs.getString("NAME") != null) {
            school = new School();
            school.setId(id);
            school.setName(rs.getString("NAME"));
            school.setKey(rs.getString("SCHOOL_KEY"));
            if (rs.getLong("ADDRESS_FK") > 0) {
                AddressDaoImpl aDao = new AddressDaoImpl();
                school.setAddress(aDao.getById(rs.getLong("ADDRESS_FK")));
            }
        }
        ps.close();
        rs.close();
        DbConnection.releaseConnection(connection);
        return school;
    }

    public void delete(School school) throws SQLException {
        Connection connection = DbConnection.getConnection();
        if (school.getId() != null) {
            PreparedStatement ps = connection.prepareStatement("UPDATE RR_SCHOOL SET DELETED=1 WHERE SCHOOL_ID=?");
            ps.setLong(1, school.getId());
            ps.executeUpdate();
            ps.close();
        }
        DbConnection.releaseConnection(connection);
    }

    public void saveSchool(School school) throws SQLException {
        Connection connection = DbConnection.getConnection();
        if (school.getAddress() != null) {
            AddressDaoImpl aDao = new AddressDaoImpl();
            aDao.saveOrUpdate(school.getAddress());
        }
        if (school.getId() != null) {
            PreparedStatement ps = connection.prepareStatement("UPDATE RR_SCHOOL SET NAME=?,SCHOOL_KEY=?,ADDRESS_FK=? WHERE SCHOOL_ID=?");
            ps.setString(1, school.getName());
            ps.setString(2, school.getKey());
            if (school.getAddress() != null) {
                ps.setLong(3, school.getAddress().getId());
            }
            ps.setLong(4, school.getId());
            ps.executeUpdate();
            ps.close();
        } else {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO RR_SCHOOL (NAME,SCHOOL_KEY,ADDRESS_FK) VALUES (?,?,?)", Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, school.getName());
            ps.setString(2, school.getKey());
            if (school.getAddress() != null) {
                ps.setLong(3, school.getAddress().getId());
            }
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                school.setId(rs.getLong(1));
            }
            ps.close();
            rs.close();
        }

        saveSchoolCategories(school, connection);
        DbConnection.releaseConnection(connection);
    }

    private void saveSchoolCategories(School school, Connection connection) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("DELETE FROM " + TB_CATEGORIES + " WHERE SCHOOL_FK=?");
        ps.setLong(1, school.getId());
        ps.executeUpdate();
        ps = connection.prepareStatement("INSERT INTO " + TB_CATEGORIES + " (SCHOOL_FK, CATEGORY_FK) VALUES (?,?)");
        for (EnumEntity raceCategory : school.getRaceCategories()) {
            ps.setLong(1, school.getId());
            ps.setLong(2, raceCategory.getId());
            ps.addBatch();
        }
        ps.executeBatch();
        ps.close();
    }

    public Integer getSchoolCode() throws SQLException {
        Connection connection = DbConnection.getConnection();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT MAX(SCHOOL_KEY) FROM RR_SCHOOL_KEY");
        Integer schoolCode = null;
        if (resultSet.next()) {
            schoolCode = resultSet.getInt(1);
            schoolCode++;
        }
        statement.close();
        resultSet.close();

        PreparedStatement ps = connection.prepareStatement("INSERT INTO RR_SCHOOL_KEY (SCHOOL_KEY) VALUES (?)");
        ps.setInt(1, schoolCode);
        ps.executeUpdate();
        ps.close();
        DbConnection.releaseConnection(connection);
        return schoolCode;
    }


    public School findByCode(String code) throws SQLException {
        School school = null;
        Connection connection = DbConnection.getConnection();
        PreparedStatement ps = connection.prepareStatement("SELECT NAME,SCHOOL_ID,ADDRESS_FK FROM RR_SCHOOL WHERE SCHOOL_KEY=?");
        ps.setString(1, code);
        ResultSet rs = ps.executeQuery();
        rs.beforeFirst();
        if (rs.next()) {
            school = new School();
            school.setId(rs.getLong("SCHOOL_ID"));
            school.setName(rs.getString("NAME"));
            school.setKey(code);
            if (rs.getLong("ADDRESS_FK") > 0) {
                AddressDaoImpl aDao = new AddressDaoImpl();
                school.setAddress(aDao.getById(rs.getLong("ADDRESS_FK")));
            }
        }
        ps.close();
        rs.close();

        ps = connection.prepareStatement("SELECT TEAM_FK FROM RR_SCHOOL_TEAMS ST, RR_TEAM T WHERE SCHOOL_FK=? AND ST.TEAM_FK=T.TEAM_ID AND T.DELETED=0");
        ps.setLong(1, school.getId());
        rs = ps.executeQuery();
        TeamDaoImpl tDao = new TeamDaoImpl();
        while (rs.next()) {
            if (school.getTeams() == null) {
                school.setTeams(new ArrayList<Team>());
            }
            school.getTeams().add(tDao.findById(rs.getLong("TEAM_FK")));
        }

        ps = connection.prepareStatement("SELECT RACE_CATEGORY_FK FROM RR_SCHOOL_RACE_CATEGORIES WHERE SCHOOL_FK=?");
        ps.setLong(1, school.getId());
        rs = ps.executeQuery();

        RaceCategoryDaoImpl raceCategoryDao = new RaceCategoryDaoImpl();
        while (rs.next()) {
            school.getRaceCategories().add(raceCategoryDao.findById(rs.getLong("RACE_CATEGORY_FK")));
        }
        DbConnection.releaseConnection(connection);
        return school;
    }

}
