package sk.insomnia.rowingRace.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sk.insomnia.rowingRace.dto.EnumEntityDto;
import sk.insomnia.rowingRace.dto.TeamDto;
import sk.insomnia.rowingRace.service.facade.ConnectivityException;
import sk.insomnia.rowingRace.so.School;

import java.sql.SQLException;

/**
 * Created by martin on 10/29/2014.
 */
public class OrganizationAdministrationController extends AbstractController {

    private static final Logger LOG = LoggerFactory.getLogger(OrganizationAdministrationController.class);

    @FXML
    private TableView<TeamDto> tvTeams;
    @FXML
    private TableView<School> tvSchools;
    @FXML
    private TableColumn<TeamDto, String> tcTeamName;
    @FXML
    private TableColumn<TeamDto, String> tcTeamCategory;
    @FXML
    private TableColumn<School, String> tcRaceCategory;
    @FXML
    private TableColumn<School, String> tcOrganizationAddress;
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


    private static ObservableList<School> organizations = FXCollections.observableArrayList();
    private static ObservableList<TeamDto> teams = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        tvSchools.setItems(organizations);
        tvTeams.setItems(teams);
        tcOrganizationKey.setCellValueFactory(new PropertyValueFactory<School, String>("key"));
        tcOrganizationName.setCellValueFactory(new PropertyValueFactory<School, String>("name"));
        tcOrganizationAddress.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<School, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<School, String> schoolStringCellDataFeatures) {
                SimpleStringProperty retval = new SimpleStringProperty();
                retval.setValue(String.format("%s %s", schoolStringCellDataFeatures.getValue().getAddress().getCity(),
                        schoolStringCellDataFeatures.getValue().getAddress().getStreet()));
                return retval;
            }
        });
        tcRaceCategory.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<School, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<School, String> schoolStringCellDataFeatures) {
                SimpleStringProperty retval = new SimpleStringProperty();
                if (schoolStringCellDataFeatures.getValue().getRaceCategories() != null &&
                        schoolStringCellDataFeatures.getValue().getRaceCategories().size() > 0) {
                    retval.set(schoolStringCellDataFeatures.getValue().getRaceCategories().get(0).getAcronym());
                }
                return retval;
            }
        });

        tcTeamName.setCellValueFactory(new PropertyValueFactory<TeamDto, String>("name"));
        tcTeamCategory.setCellValueFactory(new PropertyValueFactory<TeamDto, String>("category"));
    }

    @Override
    public void initializeFormData() {
        organizations.clear();
        try {
            organizations.addAll(dbService.getSchools());
        } catch (ConnectivityException | SQLException e) {
            displayErrorMessage(resourceBundle.getString("ERROR_LOAD_SCHOOLS"));
            LOG.debug("Error loading organization data from database.", e);
        }
    }

    @FXML
    private void tbTeamsSelected() {
        School school = tvSchools.getSelectionModel().getSelectedItem();
        teams.clear();
        try {
            teams.addAll(dbService.getTeamsBySchoolId(school.getId()));
        } catch (ConnectivityException | SQLException e) {
            LOG.debug(String.format("Error loading teams for school %s with id %d", school.getName(), school.getId()), e);
            displayErrorMessage(resourceBundle.getString("ERROR_LOAD_TEAMS"));
        }
    }

    @FXML
    private TeamDto newTeam() {
        return new TeamDto();
    }

    @FXML
    private TeamDto submitTeam() {
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

