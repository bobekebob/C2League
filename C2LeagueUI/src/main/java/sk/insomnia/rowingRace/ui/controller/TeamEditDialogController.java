package sk.insomnia.rowingRace.ui.controller;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialogs;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sk.insomnia.rowingRace.constants.RowingRaceCodeTables;
import sk.insomnia.rowingRace.dto.DtoUtils;
import sk.insomnia.rowingRace.dto.EnumEntityDto;
import sk.insomnia.rowingRace.dto.RaceYearDto;
import sk.insomnia.rowingRace.so.Team;

import java.text.MessageFormat;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class TeamEditDialogController extends AbstractController {

    @FXML
    private TextField tfTeamName;
    @FXML
    private ComboBox<EnumEntityDto> cbTeamCategory;
    @FXML
    private ComboBox<RaceYearDto> cbRaceYear;

    private Team team;
    private Stage dialogStage;
    private boolean okClicked = false;
    private List<EnumEntityDto> teamCategories;

    public void setTeam(Team team) throws DtoUtils.DtoUtilException {
        this.team = team;
        if (team != null) {
            if (team.getName() != null) tfTeamName.setText(team.getName());
            if (team.getTeamCategory() != null)
                cbTeamCategory.setValue(DtoUtils.transformWithLanguageDependentValue(team.getTeamCategory(), locale.getLanguage(), EnumEntityDto.class));
        } else {
            tfTeamName.setText("");
        }
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;

    }

    @FXML
    private void initialize() {
    }

    public boolean isOkClicked() {
        return okClicked;
    }

    @FXML
    private void handleOk() {
        if (isInputValid()) {
            team.setName(tfTeamName.getText());
            team.setTeamCategory(cbTeamCategory.getValue());
            okClicked = true;
            dialogStage.close();
        }
    }

    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    private boolean isInputValid() {
        String errorMessage = "";

        if (tfTeamName.getText() == null || tfTeamName.getText().length() == 0) {
            errorMessage += resourceBundle.getString("ERR_NAME_EMTPY");
        }

        if (cbTeamCategory.getValue() == null) {
            errorMessage += resourceBundle.getString("ERR_TEAM_CATEGORY_EMPTY");
        }
        if (errorMessage.length() == 0) {
            return true;
        } else {
            Dialogs.showErrorDialog(dialogStage, errorMessage,
                    resourceBundle.getString("INFO_CORRECT_FIELDS"),
                    resourceBundle.getString("INFO_CORRECT_FIELDS_TITLE"));
            return false;
        }
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
            teamCategories = dataProcessor.getSlaveValues(dataProcessor.getSchoolRaceCategory(), RowingRaceCodeTables.CT_RACE_CATEGORY, RowingRaceCodeTables.CT_TEAM_CATEGORIES);
            if (teamCategories != null) {
                this.fileService.saveOrUpdate(teamCategories, RowingRaceCodeTables.CT_TEAM_CATEGORIES);
            }
            if (teamCategories == null) {
                teamCategories = this.fileService.loadCodeTable(RowingRaceCodeTables.CT_TEAM_CATEGORIES);
            }

            if (teamCategories != null) {
                setupFormControls();
            } else {
                Object[] params = {RowingRaceCodeTables.CT_TEAM_CATEGORIES};
                String errorMessage = MessageFormat.format(resourceBundle.getString("ERR_NO_VALUES_SET"), params) + "\n";
                displayErrorMessage(errorMessage,
                        resourceBundle.getString("DATA_SAVE"),
                        resourceBundle.getString("DATA_SAVE_TITLE"));
            }
        } catch (Exception e) {
            Object[] params = {RowingRaceCodeTables.CT_TEAM_CATEGORIES};
            String errorMessage = MessageFormat.format(resourceBundle.getString("ERR_CODE_TABLE_DATA_LOAD"), params) + "\n";
            displayErrorMessage(errorMessage,
                    resourceBundle.getString("DATA_SAVE"),
                    resourceBundle.getString("DATA_SAVE_TITLE"));
        }
    }

    private void setupFormControls() {
        this.cbTeamCategory.getItems().clear();
        this.cbTeamCategory.getItems().addAll(teamCategories);
        setRaceYears();
    }

    private void setRaceYears() {
        this.cbRaceYear.getItems().clear();
/*
        List<RaceYearDto> raceYearDtos = DtoUtils.raceYearsToDtos(this.rowingRace.getRaceYears(), this.locale.getLanguage());
        this.cbRaceYear.getItems().addAll(raceYearDtos);
*/
    }
}
