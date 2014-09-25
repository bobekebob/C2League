package sk.insomnia.rowingRace.controller;

import javafx.fxml.FXML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sk.insomnia.rowingRace.application.AdminGui;
import sk.insomnia.rowingRace.constants.RowingRaceCodeTables;
import sk.insomnia.rowingRace.dataStore.CommonDataStore;
import sk.insomnia.rowingRace.dto.DisciplineCategoryDto;
import sk.insomnia.rowingRace.dto.DtoUtils;
import sk.insomnia.rowingRace.markers.DisciplineCategory;
import sk.insomnia.rowingRace.markers.RaceCategory;
import sk.insomnia.rowingRace.markers.TeamCategory;
import sk.insomnia.rowingRace.service.facade.ConnectivityException;
import sk.insomnia.rowingRace.service.facade.RowingRaceDbFacade;

import java.sql.SQLException;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class AdminOverviewController extends AbstractController {

    private static final Logger LOG = LoggerFactory.getLogger(AdminOverviewController.class);

    @FXML
    private PerformancesAdministrationController performancesAdministrationController;
    @FXML
    private CodeTablesController codeTablesController;
    @FXML
    private DisciplinesController disciplinesController;
    @FXML
    private RaceCalendarController raceCalendarController;
    @FXML
    private MasterSlaveCodeTableController masterSlaveCodeTableController;


    private AdminGui rowingRaceGui;


    public AdminOverviewController() {
    }


    private RowingRaceDbFacade dbService;


    @FXML
    private void initialize() {
    }


    @Override
    public void initLocale(Locale locale) {
        this.locale = locale;
        this.codeTablesController.initLocale(locale);
        this.performancesAdministrationController.initLocale(locale);
        this.raceCalendarController.initLocale(locale);
        this.disciplinesController.initLocale(locale);
        this.masterSlaveCodeTableController.initLocale(locale);
    }

    @Override
    public void initResourceBundle(ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
        this.codeTablesController.initResourceBundle(resourceBundle);
        this.performancesAdministrationController.initResourceBundle(resourceBundle);
        this.raceCalendarController.initResourceBundle(resourceBundle);
        this.disciplinesController.initResourceBundle(resourceBundle);
        this.masterSlaveCodeTableController.initResourceBundle(resourceBundle);
    }

    public void initializeFormData() {
        //TODO : initialize CommonDataStore here

        initializeDataStore();

        this.codeTablesController.initializeFormData();
        this.performancesAdministrationController.initializeFormData();
        this.disciplinesController.initializeFormData();
        this.raceCalendarController.initializeFormData();
        this.masterSlaveCodeTableController.initializeFormData();
    }

    private void initializeDataStore() {
        try {
            CommonDataStore.registerValuesForClass(RaceCategory.class, DtoUtils.listOfLanguageSpecificValues(dbService.getCodeTableValues(RowingRaceCodeTables.CT_RACE_CATEGORY), this.locale.getLanguage()));
        } catch (DtoUtils.DtoUtilException | ConnectivityException | SQLException e) {
            LOG.error(String.format("Can't register data for %s because of ", RowingRaceCodeTables.CT_RACE_CATEGORY, e));
            errorMessageBase(RowingRaceCodeTables.CT_RACE_CATEGORY, ERR_CODE_TABLE_DATA_LOAD, this.resourceBundle);
        }

        try {
            CommonDataStore.registerValuesForClass(TeamCategory.class, DtoUtils.listOfLanguageSpecificValues(dbService.getCodeTableValues(RowingRaceCodeTables.CT_TEAM_CATEGORIES), this.locale.getLanguage()));
        } catch (DtoUtils.DtoUtilException | ConnectivityException | SQLException e) {
            LOG.error(String.format("Can't register data for %s because of ", RowingRaceCodeTables.CT_TEAM_CATEGORIES, e));
            errorMessageBase(RowingRaceCodeTables.CT_RACE_CATEGORY, ERR_CODE_TABLE_DATA_LOAD, this.resourceBundle);
        }

        try {
            List<DisciplineCategoryDto> disciplineCategoryDtos = dbService.loadDisciplineCategories();
            disciplineCategoryDtos = DtoUtils.<DisciplineCategoryDto>listOfLanguageSpecificValuesForDtoClass(
                    disciplineCategoryDtos,
                    this.locale.getLanguage(),
                    DisciplineCategoryDto.class);
            CommonDataStore.registerValuesForClass(DisciplineCategory.class, disciplineCategoryDtos);
        } catch (DtoUtils.DtoUtilException | ConnectivityException | SQLException e) {
            LOG.error(String.format("Can't register data for %s because of ", RowingRaceCodeTables.CT_DISCIPLINE_CATEGORY, e));
            errorMessageBase(RowingRaceCodeTables.CT_DISCIPLINE_CATEGORY, ERR_CODE_TABLE_DATA_LOAD, this.resourceBundle);
        }
    }


    @Override
    public void setDbService(RowingRaceDbFacade dbService) {
        this.dbService = dbService;
        this.codeTablesController.setDbService(dbService);
        this.raceCalendarController.setDbService(dbService);
        this.disciplinesController.setDbService(dbService);
        this.performancesAdministrationController.setDbService(dbService);
        this.masterSlaveCodeTableController.setDbService(dbService);
    }

    public void setRowingRaceGui(AdminGui rowingRaceGui) {
        this.rowingRaceGui = rowingRaceGui;
    }


    public boolean isOkClicked() {
        boolean okClicked = false;
        return okClicked;
    }
}
