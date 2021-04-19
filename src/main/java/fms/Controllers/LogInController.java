package fms.Controllers;

import fms.HibernateControl.FMSHibernateControl;
import fms.HibernateControl.UserHibernateControl;
import fms.model.FinanceManagementSystem;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.IOException;

public class LogInController {
    public PasswordField password;
    public TextField userName;
    public Button logInBtn;
    FinanceManagementSystem FinanceOs;
    EntityManagerFactory factory = Persistence.createEntityManagerFactory("FMSHibernate");
    FMSHibernateControl FMSHibernateControl = new FMSHibernateControl(factory);
    UserHibernateControl UserHibernateControl = new UserHibernateControl(factory);

    public void SetFinanceOs() throws Exception {
        if(FMSHibernateControl.getFMSCount() != 0)
            this.FinanceOs = FMSHibernateControl.findFirstFMSTable();
        else{
            FinanceManagementSystem fms = new FinanceManagementSystem();
            FMSHibernateControl.create(fms);
            this.FinanceOs = FMSHibernateControl.findFirstFMSTable();
        }
    }

    public void LogIn(ActionEvent actionEvent) throws IOException {
        FinanceOs.setUserListFromDb();
        if(FinanceOs.getAllUsers().size() != 0){
        if(FinanceOs.login(userName.getText(), password.getText())){
            displayLoginPopup("Valid login", "Log in is correct",  Alert.AlertType.INFORMATION);
            loadMenu();
        }
        else
            displayLoginPopup("Invalid login","Log in information is incorrect", Alert.AlertType.ERROR);
        } else
            displayLoginPopup("No Users", "User list is Empty", Alert.AlertType.ERROR);
    }
    public void loadMenu() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../../FXML/MenuPage.fxml"));
        Parent root = loader.load();

        MenuPageController controller = loader.getController();
        controller.SetFinanceOs(FinanceOs);

        Stage stage = (Stage) logInBtn.getScene().getWindow();

        stage.setTitle("AccountingOS");
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void displayLoginPopup(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(message);
        alert.showAndWait();
    }
}
