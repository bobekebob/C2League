package sk.insomnia.rowingRace.controller;

import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import sk.insomnia.rowingRace.service.facade.RowingRaceDbFacade;
import sk.insomnia.rowingRace.service.impl.RowingRaceDataDbService;
import sk.insomnia.rowingRace.so.RaceYear;
import sk.insomnia.rowingRace.so.RowingRace;

public class PerformancesAdministrationController extends AbstractController {
	private static final Logger logger = Logger.getLogger(PerformancesAdministrationController.class.toString());
	private RowingRace rowingRace;
	
	@FXML ComboBox<RaceYear> cbYears;
	
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
