package sk.insomnia.rowingRace.controller;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialogs;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sk.insomnia.rowingRace.service.facade.RowingRaceDbFacade;
import sk.insomnia.rowingRace.so.RaceYear;
import sk.insomnia.rowingRace.so.RowingRace;

import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Logger;


public class PerformancesAdministrationController extends AbstractController {

    private static final Logger logger = Logger.getLogger(PerformancesAdministrationController.class.toString());
    private RowingRace rowingRace;

    @FXML
    ComboBox<RaceYear> cbRaceYear;

    @FXML
    ComboBox<RaceYear> cbRaceRound;

    @FXML
    TableView tbPerformances;
    @FXML
    TableColumn tcInstitutionName;
    @FXML
    TableColumn tcTeamName;
    @FXML
    TableColumn tcPerformanceTime;
    @FXML
    TableColumn tcPerformanceDate;
    @FXML
    TextField tfMinutes;
    @FXML
    TextField tfSeconds;
    @FXML
    TextField tfMillis;


    public RowingRace getRowingRace() {
        return rowingRace;
    }

    public void setRowingRace(RowingRace rowingRace) {
        this.rowingRace = rowingRace;
    }


    @Override
    public void initLocale(Locale locale) {
        this.locale = locale;
    }

    @Override
    public void initResourceBundle(ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
    }

    @Override
    public void initializeFormData() {
        try {
            ControllerUtils.prepareRaceData(cbRaceYear, rowingRace, this);
        } catch (Exception e) {
            Dialogs.showErrorDialog(new Stage(), e.getMessage(),
                    resourceBundle.getString("DATA_LOAD"),
                    resourceBundle.getString("DATA_LOAD_TITLE"));
        }
    }

    @Override
    public void setDbService(RowingRaceDbFacade dbService) {
        this.dbService = dbService;
    }
}
