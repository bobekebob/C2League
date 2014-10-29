package sk.insomnia.rowingRace.controller;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import sk.insomnia.rowingRace.dto.TeamDto;
import sk.insomnia.rowingRace.so.School;

/**
 * Created by martin on 10/29/2014.
 */
public class OrganizationAdministrationController extends AbstractController {

    @FXML
    private ComboBox<School> cbOrganization;
    @FXML
    private TableView<TeamDto> tbTeams;
    @FXML
    private TableColumn<TeamDto, String> tcTeamName;
    @FXML
    private TableColumn<TeamDto, String> tcTeamCategory;

    @Override
    public void initializeFormData() {

    }

    @FXML
    private void deleteOrganization(){

    }

    @FXML
    private void deleteTeam(){

    }
}

