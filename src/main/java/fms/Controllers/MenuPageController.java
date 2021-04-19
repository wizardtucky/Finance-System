package fms.Controllers;

import fms.model.FinanceManagementSystem;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;

import java.io.IOException;

public class MenuPageController {
    public Button financeMenu;
    public Button userMenuButton;
    public Menu xButton;
    public MenuItem saveButton;
    public MenuItem exitButton;
    private FinanceManagementSystem FinanceOs;

    public void loadFinanceMenu(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/FinancePage.fxml"));
        Parent root = loader.load();

        FinanceController controller = loader.getController();
        controller.SetFinanceOs(FinanceOs);

        Stage stage = (Stage) financeMenu.getScene().getWindow();

        stage.setTitle("FinancesOS");
        stage.setScene(new Scene(root));
        stage.show();
    }
    public void loadUserMenu(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/UserMenuPage.fxml"));
        Parent root = loader.load();

        UserMenuController controller = loader.getController(); /// ?????? paaiskink iki galo
        controller.SetFinanceOs(FinanceOs);

        Stage stage = (Stage) financeMenu.getScene().getWindow();

        stage.setTitle("FinancesOS");
        stage.setScene(new Scene(root));
        stage.show();
    }
    public void exitProgram(){
        displayLoginPopup("Exit the program", "You exited the program", Alert.AlertType.INFORMATION);
        System.out.println("Exit");
        System.exit(0);
    }
    public void saveData(){
        displayLoginPopup("Data Save", "Data has been saved successfully", Alert.AlertType.INFORMATION);

    }
    public void saveAndExit(){
        displayLoginPopup("Save/Exit", "Data has been saved and program closed", Alert.AlertType.INFORMATION);
        System.out.println("Exit");
        System.exit(0);
    }
    public void displayLoginPopup(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(message);
        alert.showAndWait();
    }
    public void SetFinanceOs(FinanceManagementSystem os) {
        this.FinanceOs = os;
    }
}
