package sk.insomnia.rowingRace.service.impl;

import sk.insomnia.rowingRace.connection.DbConnection;
import sk.insomnia.rowingRace.constants.RowingRaceCodeTables;
import sk.insomnia.rowingRace.dao.CodeTableDao;
import sk.insomnia.rowingRace.dao.DisciplineCategoryDao;
import sk.insomnia.rowingRace.dao.DisciplineDao;
import sk.insomnia.rowingRace.dao.IntervalDao;
import sk.insomnia.rowingRace.dao.LanguageMutationsDao;
import sk.insomnia.rowingRace.dao.RacerDao;
import sk.insomnia.rowingRace.dao.RowingRaceDao;
import sk.insomnia.rowingRace.dao.SchoolDao;
import sk.insomnia.rowingRace.dao.TeamDao;
import sk.insomnia.rowingRace.dao.jdbc.CodeTableDaoImpl;
import sk.insomnia.rowingRace.dao.jdbc.DisciplineCategoryDaoImpl;
import sk.insomnia.rowingRace.dao.jdbc.DisciplineDaoImpl;
import sk.insomnia.rowingRace.dao.jdbc.IntervalDaoImpl;
import sk.insomnia.rowingRace.dao.jdbc.LanguageMutationsDaoImpl;
import sk.insomnia.rowingRace.dao.jdbc.PerformanceDaoImpl;
import sk.insomnia.rowingRace.dao.jdbc.RaceRoundDaoImpl;
import sk.insomnia.rowingRace.dao.jdbc.RacerDaoImpl;
import sk.insomnia.rowingRace.dao.jdbc.RowingRaceDaoImpl;
import sk.insomnia.rowingRace.dao.jdbc.SchoolDaoImpl;
import sk.insomnia.rowingRace.dao.jdbc.TeamDaoImpl;
import sk.insomnia.rowingRace.dto.DisciplineCategoryDto;
import sk.insomnia.rowingRace.dto.EnumEntityDto;
import sk.insomnia.rowingRace.dto.SimpleEnumEntityDto;
import sk.insomnia.rowingRace.mapping.MappingUtil;
import sk.insomnia.rowingRace.service.facade.ConnectivityException;
import sk.insomnia.rowingRace.service.facade.RowingRaceDbFacade;
import sk.insomnia.rowingRace.so.Discipline;
import sk.insomnia.rowingRace.so.DisciplineCategory;
import sk.insomnia.rowingRace.so.EnumEntity;
import sk.insomnia.rowingRace.so.EnumEntitySO;
import sk.insomnia.rowingRace.so.Interval;
import sk.insomnia.rowingRace.so.LanguageMutation;
import sk.insomnia.rowingRace.so.Performance;
import sk.insomnia.rowingRace.so.RaceRound;
import sk.insomnia.rowingRace.so.RaceYear;
import sk.insomnia.rowingRace.so.Racer;
import sk.insomnia.rowingRace.so.RowingRace;
import sk.insomnia.rowingRace.so.School;
import sk.insomnia.rowingRace.so.Team;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class RowingRaceDataDbService implements RowingRaceDbFacade {

    private final ResourceBundle bundle;
    private static final LanguageMutationsDao languageMutationsDao = new LanguageMutationsDaoImpl();
    private static final RowingRaceDao rowingRaceDao = new RowingRaceDaoImpl();
    private static final SchoolDaoImpl schoolDao = new SchoolDaoImpl();
    private static final PerformanceDaoImpl performanceDao = new PerformanceDaoImpl();
    private static final CodeTableDaoImpl codeTableDao = new CodeTableDaoImpl();


    public RowingRaceDataDbService(Locale locale) {
        this.bundle = ResourceBundle.getBundle("sk.insomnia.rowingRace.resources.bundles.data", locale);
    }

    public void saveSchool(School school) throws SQLException, ConnectivityException {
        SchoolDaoImpl dao = new SchoolDaoImpl();
        dao.saveSchool(school);
    }

    public String getSchoolCode() throws SQLException, ConnectivityException {
        String retVal = null;
        SchoolDaoImpl dao = new SchoolDaoImpl();
        DecimalFormat phoneDecimalFmt = new DecimalFormat("0000000000");
        retVal = phoneDecimalFmt.format(dao.getSchoolCode());
        return retVal;
    }


    public RowingRace loadRowingRace(List<EnumEntity> raceCategories) throws SQLException, ConnectivityException {
        return rowingRaceDao.getRowingRace(raceCategories);
    }

    public RowingRace loadRowingRace() throws SQLException, ConnectivityException {
        return rowingRaceDao.getRowingRace();
    }


    public School findByKey(String key) throws SQLException,
            ConnectivityException {
        return schoolDao.findByCode(key);
    }


    public School findById(Long id) throws SQLException, ConnectivityException {
        return schoolDao.findById(id);
    }


    public boolean isConnectivity() {
        return DbConnection.isConnectivity();
    }


    public void saveOrUpdate(Performance performance) throws SQLException,
            ConnectivityException {
        performanceDao.saveOrUpdate(performance);
    }


    @Deprecated
    public List<EnumEntityDto> getCodeTable(RowingRaceCodeTables codeTable) throws SQLException,
            ConnectivityException {
        List<EnumEntityDto> codeTables = new ArrayList<EnumEntityDto>();


        List<EnumEntity> tcs = codeTableDao.getAll(codeTable);
        Iterator<EnumEntity> it = tcs.iterator();
        while (it.hasNext()) {
            SimpleEnumEntityDto ctd = new SimpleEnumEntityDto();
            EnumEntity ct = it.next();
            ctd.setId(ct.getId());
            ctd.setAcronym(ct.getAcronym());
            ctd.setValue(bundle.getString(ct.getAcronym()));
            codeTables.add(ctd);
        }
        return codeTables;
    }

    public List<EnumEntity> getCodeTableValues(RowingRaceCodeTables codeTable) throws SQLException,
            ConnectivityException {
        CodeTableDao tcDao = new CodeTableDaoImpl();
        return tcDao.getAll(codeTable);
    }

    @Override
    public void deleteCodeTableValue(EnumEntity enumEntity, RowingRaceCodeTables codeTable) throws SQLException, ConnectivityException {
        CodeTableDao codeTableDao = new CodeTableDaoImpl();
        codeTableDao.deleteCodeTableValue(enumEntity, codeTable);
    }

    @Override
    public void saveCodeTableValue(EnumEntity enumEntity, RowingRaceCodeTables codeTable) throws SQLException, ConnectivityException {
        CodeTableDao ctDao = new CodeTableDaoImpl();
        ctDao.saveOrUpdate(enumEntity, codeTable);
    }

    @Override
    public void addMutationToKey(Long id, LanguageMutation mutation) throws SQLException, ConnectivityException {
        languageMutationsDao.addMutationToKey(id, mutation);
    }

    @Override
    public void deleteMutation(LanguageMutation mutation) throws SQLException, ConnectivityException {
        languageMutationsDao.removeMutationFromKey(mutation);
    }

    @Override
    public void updateMutation(LanguageMutation mutation) throws SQLException, ConnectivityException {
        languageMutationsDao.updateMutation(mutation);
    }

    @Override
    public List<EnumEntity> loadValuesForCodeTable(EnumEntityDto value, RowingRaceCodeTables masterCodeTable,RowingRaceCodeTables slaveCodeTable) throws SQLException {
        return codeTableDao.getSlaveValues(value, masterCodeTable, slaveCodeTable);
    }

    @Override
    public void saveSlaveValues(Long masterValueId, RowingRaceCodeTables masterCodeTable, List<EnumEntityDto> slaveValues, RowingRaceCodeTables slaveCodeTable) throws SQLException {
        codeTableDao.saveSlaveValues(masterValueId, masterCodeTable, SimpleEnumEntityDto.toSoList(slaveValues), slaveCodeTable);
    }

    public List<DisciplineCategoryDto> loadDisciplineCategories()
            throws SQLException, ConnectivityException {
        DisciplineCategoryDao dao = new DisciplineCategoryDaoImpl();
        return MappingUtil.disciplineCategorieAsDtoList(dao.getAll());
    }

    public void saveDisciplineCategory(DisciplineCategoryDto disciplineCategory) throws SQLException, ConnectivityException {
        DisciplineCategoryDao dao = new DisciplineCategoryDaoImpl();
        DisciplineCategory disciplineCategorySO = new DisciplineCategory();

        dao.saveOrUpdate(disciplineCategorySO);
    }

    public void saveOrUpdateRaceYear(RaceYear raceYear) throws SQLException,
            ConnectivityException {
        RowingRaceDao rowingRaceDao = new RowingRaceDaoImpl();
        rowingRaceDao.saveOrUpdate(raceYear);
    }

    public void deleteRaceYear(RaceYear year) throws SQLException,
            ConnectivityException {
        RowingRaceDao rDao = new RowingRaceDaoImpl();
        rDao.deleteRaceYear(year);
    }


    public void deleteRowingRaceRound(RaceRound raceRound) throws SQLException,
            ConnectivityException {
        RaceRoundDaoImpl dao = new RaceRoundDaoImpl();
        dao.delete(raceRound);
    }

    public void deleteRacer(Racer racer) throws SQLException,
            ConnectivityException {
        RacerDao dao = new RacerDaoImpl();
        dao.deleteRacer(racer);
    }

    public void deleteTeam(Team team) throws SQLException,
            ConnectivityException {
        TeamDao dao = new TeamDaoImpl();
        dao.delete(team);
    }

    public void deleteSchool(School school) throws SQLException,
            ConnectivityException {
        SchoolDao dao = new SchoolDaoImpl();
        dao.delete(school);
    }

    public void addRowingRaceRound(RaceRound raceRound) throws SQLException,
            ConnectivityException {
        RaceRoundDaoImpl dao = new RaceRoundDaoImpl();
        dao.saveOrUpdate(raceRound);
    }

    public void addTeamToSchool(Team team, Long schoolId) throws SQLException, ConnectivityException {
        TeamDao dao = new TeamDaoImpl();
        dao.saveOrUpdate(team, schoolId);
    }

    public void addRacerToTeam(Racer racer, Long teamId) throws SQLException,
            ConnectivityException {
        RacerDao dao = new RacerDaoImpl();
        dao.saveOrUpdate(racer, teamId);
    }

    public void saveTeam(Team team) throws SQLException, ConnectivityException {
        TeamDao dao = new TeamDaoImpl();
        dao.saveOrUpdate(team, null);
    }

    public void addIntervalToDiscipline(Interval interval, Long disciplineId)
            throws SQLException, ConnectivityException {
        IntervalDao dao = new IntervalDaoImpl();
        dao.saveOrUpdate(interval, disciplineId);

    }

    public void addDisciplineToCategory(Discipline discipline,
                                        Long disciplineCategoryId) throws SQLException,
            ConnectivityException {
        DisciplineDao dao = new DisciplineDaoImpl();
        dao.saveOrUpdate(discipline, disciplineCategoryId);
    }

    public void saveInterval(Interval interval) throws SQLException,
            ConnectivityException {
        IntervalDao dao = new IntervalDaoImpl();
        dao.saveOrUpdate(interval, null);
    }

    public void saveDiscipline(Discipline discipline) throws SQLException,
            ConnectivityException {
        DisciplineDao dao = new DisciplineDaoImpl();
        dao.saveOrUpdate(discipline, null);
    }

    public void saveRacer(Racer racer) throws SQLException,
            ConnectivityException {
        RacerDao dao = new RacerDaoImpl();
        dao.saveOrUpdate(racer, null);
    }

}
