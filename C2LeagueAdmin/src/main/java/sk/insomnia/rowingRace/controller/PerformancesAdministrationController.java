package sk.insomnia.rowingRace.controller;

import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import sk.insomnia.rowingRace.service.facade.RowingRaceDbFacade;
import sk.insomnia.rowingRace.so.RaceYear;
import sk.insomnia.rowingRace.so.RowingRace;


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

    }

    @Override
    public void setDbService(RowingRaceDbFacade dbService) {
        this.dbService = dbService;
    }
}
