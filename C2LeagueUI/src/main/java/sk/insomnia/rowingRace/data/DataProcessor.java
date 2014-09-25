package sk.insomnia.rowingRace.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sk.insomnia.rowingRace.constants.RowingRaceCodeTables;
import sk.insomnia.rowingRace.dto.DtoUtils;
import sk.insomnia.rowingRace.dto.EnumEntityDto;
import sk.insomnia.rowingRace.dto.SimpleEnumEntityDto;
import sk.insomnia.rowingRace.exceptions.ExceptionType;
import sk.insomnia.rowingRace.exceptions.RowingRaceException;
import sk.insomnia.rowingRace.listeners.DataChangeObserver;
import sk.insomnia.rowingRace.listeners.SchoolListener;
import sk.insomnia.rowingRace.service.facade.ConnectivityException;
import sk.insomnia.rowingRace.service.impl.RowingRaceDataDbService;
import sk.insomnia.rowingRace.service.impl.RowingRaceDataFileService;
import sk.insomnia.rowingRace.so.EnumEntity;
import sk.insomnia.rowingRace.so.Performance;
import sk.insomnia.rowingRace.so.Racer;
import sk.insomnia.rowingRace.so.RowingRace;
import sk.insomnia.rowingRace.so.School;
import sk.insomnia.rowingRace.so.Team;
import sk.insomnia.tools.exceptionUtils.ExceptionUtils;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by bobek on 6/28/2014.
 */
public final class DataProcessor implements SchoolListener {

    private static final Logger logger = LoggerFactory.getLogger(DataProcessor.class);
    private static final RowingRaceDataFileService fileService = new RowingRaceDataFileService();
    private final RowingRaceDataDbService dbService;
    private final Locale locale;
    private School school;
    private RowingRace rowingRace;

    public DataProcessor(RowingRaceDataDbService dbService, Locale locale) {
        this.dbService = dbService;
        this.locale = locale;
    }

    public void onRacerAdded(Racer racer, Long teamId) throws RowingRaceException {
        try {
            dbService.addRacerToTeam(racer, teamId);
        } catch (SQLException e) {
            throw new RowingRaceException("Error while adding racer to team.", ExceptionType.SQL_EXCEPTION);
        } catch (ConnectivityException e) {
            throw new RowingRaceException("Error while adding racer to team.", ExceptionType.CONNECTIVITY_EXCEPTION);
        }
        saveSchoolInProcess(true);
    }

    public void onRacerDeleted(Racer racer) throws RowingRaceException {
        try {
            dbService.deleteRacer(racer);
        } catch (SQLException e) {
            throw new RowingRaceException("Error while deleting racer.", ExceptionType.SQL_EXCEPTION);
        } catch (ConnectivityException e) {
            throw new RowingRaceException("Error while adding racer to team.", ExceptionType.CONNECTIVITY_EXCEPTION);
        }
        saveSchoolInProcess(true);
    }

    public void onRacerChanged(Racer racer) throws RowingRaceException {
        try {
            dbService.saveRacer(racer);
        } catch (SQLException e) {
            throw new RowingRaceException(ExceptionUtils.exceptionAsString(e), ExceptionType.SQL_EXCEPTION);
        } catch (ConnectivityException e) {
            throw new RowingRaceException(ExceptionUtils.exceptionAsString(e), ExceptionType.CONNECTIVITY_EXCEPTION);
        }
        saveSchoolInProcess(true);
    }

    public void onTeamChanged(Team team) throws RowingRaceException {
        try {
            dbService.saveTeam(team);
        } catch (SQLException e) {
            throw new RowingRaceException(ExceptionUtils.exceptionAsString(e), ExceptionType.SQL_EXCEPTION);
        } catch (ConnectivityException e) {
            throw new RowingRaceException(ExceptionUtils.exceptionAsString(e), ExceptionType.CONNECTIVITY_EXCEPTION);
        }
        saveSchoolInProcess(true);
    }

    public void onTeamAdded(Team team) throws RowingRaceException {
        if (school == null) {
            throw new RowingRaceException(ExceptionType.NO_SCHOOL_SELECTED);
        }
        try {
            dbService.addTeamToSchool(team, school.getId());
        } catch (SQLException e) {
            throw new RowingRaceException(ExceptionUtils.exceptionAsString(e), ExceptionType.SQL_EXCEPTION);
        } catch (ConnectivityException e) {
            throw new RowingRaceException(ExceptionUtils.exceptionAsString(e), ExceptionType.CONNECTIVITY_EXCEPTION);
        }

        if (school.getTeams() == null) {
            school.setTeams(new ArrayList<Team>());
        }
        school.getTeams().add(team);
        saveSchoolInProcess(true);
    }

    public void onTeamDeleted(Team team) throws RowingRaceException {
        try {
            dbService.deleteTeam(team);
        } catch (SQLException e) {
            throw new RowingRaceException(ExceptionUtils.exceptionAsString(e), ExceptionType.SQL_EXCEPTION);
        } catch (ConnectivityException e) {
            throw new RowingRaceException(ExceptionUtils.exceptionAsString(e), ExceptionType.CONNECTIVITY_EXCEPTION);
        }
        saveSchoolInProcess(true);
    }

    public boolean saveSchoolInProcess(boolean fileOnly) throws RowingRaceException {
        if (school == null) {
            throw new RowingRaceException("School in process was not set.", ExceptionType.PRECONDITION_NOT_MET);
        }
        try {
            if (dbService.isConnectivity() && !fileOnly) {
                dbService.saveSchool(school);
            }
            fileService.saveSchool(school);
        } catch (Exception e) {
            throw new RowingRaceException(String.format("Error while saving data %s", e.getLocalizedMessage()), ExceptionType.UNKNOWN);
        }
        return true;
    }

    public List<EnumEntityDto> getCodeTable(RowingRaceCodeTables rowingRaceCodeTables) throws RowingRaceException, DtoUtils.DtoUtilException {
        try {
            return DtoUtils.listOfLanguageSpecificValues(dbService.getCodeTableValues(rowingRaceCodeTables), this.locale.getLanguage());
        } catch (SQLException e) {
            throw new RowingRaceException(String.format("Can't read code table data. Cause : %s", ExceptionUtils.exceptionAsString(e)), ExceptionType.UNKNOWN);
        } catch (ConnectivityException e) {
            logger.trace(String.format("Connect to database is not open. Can't load codetable %s", rowingRaceCodeTables.getTableName()));
        }
        return null;
    }

    public String getSchoolCode() throws RowingRaceException {
        try {
            return dbService.getSchoolCode();
        } catch (SQLException e) {
            throw new RowingRaceException(ExceptionType.SQL_EXCEPTION);
        } catch (ConnectivityException e) {
            throw new RowingRaceException(ExceptionType.CONNECTIVITY_EXCEPTION);
        }
    }

    public void loadRowingRace(List<EnumEntity> raceCategoryDaos) throws RowingRaceException {
        RowingRace race = null;
        try {
            race = dbService.loadRowingRace(raceCategoryDaos);
        } catch (SQLException e) {
            throw new RowingRaceException(ExceptionType.SQL_EXCEPTION);
        } catch (ConnectivityException e) {
            throw new RowingRaceException(ExceptionType.CONNECTIVITY_EXCEPTION);
        }
        if (race != null) {
            try {
                fileService.saveRowingRace(race);
            } catch (IOException e) {
                throw new RowingRaceException(ExceptionUtils.exceptionAsString(e), ExceptionType.FILE_WRITE_EXCEPTION);
            }
        } else {
            try {
                race = fileService.loadRowingRace();
            } catch (IOException e) {
                throw new RowingRaceException(ExceptionUtils.exceptionAsString(e), ExceptionType.FILE_READ_EXCEPTION);
            }
        }
        DataChangeObserver.notifyRaceSelected(race);
    }

    public void saveOrUpdatePerformance(Performance performance) throws RowingRaceException {
        try {
            dbService.saveOrUpdate(performance);
        } catch (SQLException e) {
            throw new RowingRaceException(ExceptionType.SQL_EXCEPTION);
        } catch (ConnectivityException e) {
            throw new RowingRaceException(ExceptionType.CONNECTIVITY_EXCEPTION);
        }
    }

    @Override
    public void onSchoolSelected(School school) throws RowingRaceException {
        this.school = school;
        if (school.getRaceCategories() != null) {
            loadRowingRace(school.getRaceCategories());
        }
    }

    public EnumEntityDto getSchoolRaceCategory() throws DtoUtils.DtoUtilException {
        // this.school.getRaceCategories().get(0) is used, because we assume that school can only have one
        // race category, list is used only because we sense some future use for it
        if (this.school == null) {
            throw new IllegalStateException("No school selected !");
        }
        return DtoUtils.transformWithLanguageDependentValue(this.school.getRaceCategories().get(0), this.locale.getLanguage(), SimpleEnumEntityDto.class);
    }

    public List<EnumEntityDto> getSlaveValues(EnumEntityDto value, RowingRaceCodeTables masterCodeTable, RowingRaceCodeTables slaveCodeTable) throws SQLException, DtoUtils.DtoUtilException {
        return DtoUtils.listOfLanguageSpecificValues(this.dbService.loadValuesForCodeTable(value, masterCodeTable, slaveCodeTable), this.locale.getLanguage());
    }

}
