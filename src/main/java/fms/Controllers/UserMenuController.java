package fms.Controllers;

import fms.model.FinanceManagementSystem;

import fms.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class UserMenuController {

    public ListView userList;
    public TextField newUserName;
    public TextField newUserAccess;
    public TextField editUserAccess;
    public TextField editUserName;
    public Label userInformationLabel;
    public PasswordField newUserPassword;
    public PasswordField editUserPassword;
    public Button addButton;
    public Button editButton;
    public Button deleteUserButton;
    public Button exitToMenuButton;
    private FinanceManagementSystem financeOs;

    private List<User> users;
    String selectedUserName = new String();
    String selectedUserPassword = new String();
    String selectedUserAccessLevel = new String();
    User selectedUser = null;

    public void SetFinanceOs(FinanceManagementSystem os) {
        this.financeOs = os;
        fillUserList();
    }
    private void fillUserList(){
        System.out.println("refreshes");
        userList.getItems().clear();
        users = financeOs.getUsers();
        for(User user: users){
            userList.getItems().add( user.getName() + " " + user.getAccessLevel() + " " + user.getPassword());
        }
    }
    public void addUser(ActionEvent actionEvent) throws Exception {
        if(!financeOs.checkAccessLevel(3)){
            displayLoginPopup("Locked", "You AccessLevel does not match, Access Denied.", Alert.AlertType.ERROR);
            return;
        }
        if (newUserName.getText().isEmpty() || !newUserAccess.getText().matches("[0-9]") || newUserPassword.getText().isEmpty()){
            displayLoginPopup("Empty", "You didn't enter correct Amount", Alert.AlertType.ERROR);
            return;
        }
        int newUserAccessLvl = Integer.parseInt(newUserAccess.getText());
        User newUser = new User(newUserName.getText(),newUserPassword.getText(),newUserAccessLvl);
        financeOs.addUser(newUser);
        newUserAccess.clear();
        newUserName.clear();
        newUserPassword.clear();
        fillUserList();
    }
    public void selectUser(MouseEvent mouseEvent){
        String Item = userList.getSelectionModel().getSelectedItem().toString();
        String[] split = Item.split(" ");
        selectedUserName = split[0];
        selectedUserAccessLevel = split[1];
        selectedUserPassword = split[2];

        int intSelectedUserAccessLevel = Integer.parseInt(selectedUserAccessLevel);
        selectedUser = new User(selectedUserName, selectedUserPassword, intSelectedUserAccessLevel);
        System.out.println(userList.getSelectionModel().getSelectedItem());
    }
    public void removeUser(ActionEvent actionEvent) throws Exception {
        if(!financeOs.checkAccessLevel(3)){
            displayLoginPopup("Locked", "You AccessLevel does not match, Access Denied.", Alert.AlertType.ERROR);
            return;
        }
        int userAccessLvl = Integer.parseInt(selectedUserAccessLevel);
        financeOs.removeUser(selectedUserName, selectedUserPassword,userAccessLvl);
        nullSelectedUser();
        fillUserList();
    }
    public void editUser(ActionEvent actionEvent) throws Exception {
        if(!financeOs.checkAccessLevel(3)){
            displayLoginPopup("Locked", "You AccessLevel does not match, Access Denied.", Alert.AlertType.ERROR);
            return;
        }
        if (editUserName.getText().isEmpty() || !editUserAccess.getText().matches("[0-9]") || editUserPassword.getText().isEmpty()){
            displayLoginPopup("Empty", "You didn't enter correct Amount", Alert.AlertType.ERROR);
            return;
        }
        int editUserAccesslvl = Integer.parseInt(editUserAccess.getText());
        User edditedUser = new User(editUserName.getText(),editUserPassword.getText(),editUserAccesslvl);
        System.out.println("PRINT OUT EdditedUser: " + edditedUser.getName() + " " + edditedUser.getPassword() + " " + edditedUser.getAccessLevel());

        financeOs.editUser(selectedUser,edditedUser);
        editUserAccess.clear();
        editUserPassword.clear();
        editUserName.clear();
        nullSelectedUser();
        fillUserList();
    }
    public void deSelectUser(MouseEvent mouseEvent) {
        userList.getSelectionModel().clearSelection();
        nullSelectedUser();
    }
    public void nullSelectedUser(){
        selectedUserName = null;
        selectedUserAccessLevel = null;
        selectedUserPassword = null;
    }
    public void exitToMainMenu() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../../FXML/MenuPage.fxml"));
        Parent root = loader.load();

        MenuPageController controller = loader.getController();
        controller.SetFinanceOs(financeOs);

        Stage stage = (Stage) exitToMenuButton.getScene().getWindow();

        stage.setTitle("fms/FinanceOS");
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
}
