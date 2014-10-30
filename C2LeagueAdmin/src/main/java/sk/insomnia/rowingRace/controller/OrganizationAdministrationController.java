package sk.insomnia.rowingRace.controller;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import sk.insomnia.rowingRace.dto.EnumEntityDto;
import sk.insomnia.rowingRace.dto.TeamDto;
import sk.insomnia.rowingRace.so.School;

/**
 * Created by martin on 10/29/2014.
 */
public class OrganizationAdministrationController extends AbstractController {

    @FXML
    private ComboBox<School> cbOrganization;
    @FXML
    private TableView<TeamDto> tvTeams;
    @FXML
    private TableView<TeamDto> tvSchools;
    @FXML
    private TableColumn<TeamDto, String> tcTeamName;
    @FXML
    private TableColumn<TeamDto, String> tcTeamCategory;
    @FXML
    private TableColumn<School, String> tcOrganizationName;
    @FXML
    private TableColumn<School, String> tcOrganizationKey;
    @FXML
    private TextField tfOrganizationName;
    @FXML
    private TextField tfCity;
    @FXML
    private TextField tfStreet;
    @FXML
    private TextField tfZipCode;
    @FXML
    private ComboBox<EnumEntityDto> cbCountry;
    @FXML
    private ComboBox<EnumEntityDto> cbRaceCategory;
    @FXML
    private TextField tfTeamName;
    @FXML
    private ComboBox<EnumEntityDto> cbDisciplineCategory;

    @Override
    public void initializeFormData() {

    }

    @FXML
    private TeamDto newTeam(){
        return new TeamDto();
    }
    @FXML
    private TeamDto submitTeam(){
        return null;
    }
    @FXML
    private School newOrganiation() {
        return new School();
    }

    @FXML
    private School submitOrganization() {
        return null;
    }

    @FXML
    private void deleteOrganization() {

    }

    @FXML
    private void deleteTeam() {

    }
}

