package sk.insomnia.rowingRace.dao.jdbc;

import sk.insomnia.rowingRace.connection.DbConnection;
import sk.insomnia.rowingRace.constants.RowingRaceCodeTables;
import sk.insomnia.rowingRace.dao.LanguageMutationsDao;
import sk.insomnia.rowingRace.dao.RaceCategoryDao;
import sk.insomnia.rowingRace.so.EnumEntitySO;
import sk.insomnia.rowingRace.so.LanguageMutation;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RaceCategoryDaoImpl implements RaceCategoryDao {

    private static final LanguageMutationsDao LANGUAGE_MUTATIONS_DAO = new LanguageMutationsDaoImpl();

    public EnumEntitySO findById(Long id) throws SQLException {
        EnumEntitySO raceCategory = new EnumEntitySO();
        Connection connection = DbConnection.getConnection();
        PreparedStatement ps = connection.prepareStatement("SELECT ACRONYM FROM RR_RACE_CATEGORY WHERE ID=?");
        ps.setLong(1, id);
        ResultSet rs = ps.executeQuery();
        rs.beforeFirst();
        if (rs.next()) {
            raceCategory.setId(id);
            raceCategory.setAcronym(rs.getString("ACRONYM"));
            raceCategory.setLanguageMutations(LANGUAGE_MUTATIONS_DAO.getMutationsForKey(raceCategory.getId(),RowingRaceCodeTables.CT_RACE_CATEGORY));
        }
        ps.close();
        rs.close();
        DbConnection.releaseConnection(connection);
        return raceCategory;

    }


    public List<EnumEntitySO> getAll() throws SQLException {
        List<EnumEntitySO> RaceCategories = new ArrayList<EnumEntitySO>();
        Connection connection = DbConnection.getConnection();
        PreparedStatement ps = connection.prepareStatement("SELECT ACRONYM,ID FROM RR_RACE_CATEGORY");
        ResultSet rs = ps.executeQuery();
        rs.beforeFirst();
        while (rs.next()) {
            EnumEntitySO d = new EnumEntitySO();
            d.setId(rs.getLong("ID"));
            d.setAcronym(rs.getString("ACRONYM"));
            d.setLanguageMutations(LANGUAGE_MUTATIONS_DAO.getMutationsForKey(d.getId(),RowingRaceCodeTables.CT_RACE_CATEGORY));
            RaceCategories.add(d);
        }
        ps.close();
        rs.close();
        DbConnection.releaseConnection(connection);
        return RaceCategories;
    }


    public void saveOrUpdate(EnumEntitySO raceCategory)
            throws SQLException {
        Connection connection = DbConnection.getConnection();

        if (raceCategory.getId() != null) {
            PreparedStatement ps = connection.prepareStatement("UPDATE RR_RACE_CATEGORY SET ACRONYM=? WHERE ID=?");
            ps.setString(1, raceCategory.getAcronym());
            ps.setLong(2, raceCategory.getId());
            ps.executeUpdate();
            ps.close();
        } else {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO RR_RACE_CATEGORY (ACRONYM) VALUES (?,?)", Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, raceCategory.getAcronym());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            rs.beforeFirst();
            if (rs.next()) {
                raceCategory.setId(rs.getLong(1));
            }
            ps.close();
            rs.close();
        }

        LANGUAGE_MUTATIONS_DAO.removeAllMutationsFromKey(raceCategory, RowingRaceCodeTables.CT_RACE_CATEGORY);

        for (LanguageMutation languageMutation : raceCategory.getLanguageMutations()) {
            LANGUAGE_MUTATIONS_DAO.addMutationToKey(raceCategory.getId(), languageMutation);
        }

        DbConnection.releaseConnection(connection);
    }


}
