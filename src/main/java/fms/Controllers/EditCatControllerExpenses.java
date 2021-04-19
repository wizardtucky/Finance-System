package fms.Controllers;

import fms.model.FinanceManagementSystem;
import fms.model.Finances.Expenses;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class EditCatControllerExpenses {

    public ListView expenseList;
    public Button addFinanceButton;
    public Button editCategoryButton;
    public Button removeFinanceButton;
    public TextField newCatNameField;
    public TextField newFinanceAmountField;
    public TextField newFinanceNameField;
    public Label selectedCatLabel;
    public TextField newCatDiscriptionField;
    public Button exitToFinanceButton;
    public TextField newFinanceNameFieldEdit;
    public TextField newFinanceAmountFieldEdit;
    public Button editFinanceButton;
    public Button incomesMenuButton;

    private FinanceManagementSystem financeOs;
    private String catId;
    private String selectedExpenseId = "";

    private List<Expenses> expenses;

    public void SetFinanceOs(FinanceManagementSystem os, String id) {
        this.financeOs = os;
        this.catId = id;
        fillExpenseList();
    }

    public void fillExpenseList(){
        System.out.println("works");
        expenseList.getItems().clear();
        expenses = financeOs.getExpenseListById(catId);
        for(Expenses exp: expenses){
            expenseList.getItems().add(" ID: " + exp.getId() + ", " + exp.getName() + " (" + exp.getMoney() + ")");
        }
    }
    public void getSelectedExpenseItem(MouseEvent mouseEvent) {
        String listItemText;
        if(expenseList.getSelectionModel().isEmpty())
            return;
        listItemText = expenseList.getSelectionModel().getSelectedItem().toString();
        String[] split = listItemText.split(",");
        split = split[0].split("ID: ");
        selectedExpenseId = split[1];
        System.out.println(selectedExpenseId);
    }
    public void addFinanceAction(ActionEvent actionEvent) throws Exception {
        if(!financeOs.checkAccessLevel(3)){
            displayLoginPopup("Locked", "You AccessLevel does not match, Access Denied.", Alert.AlertType.ERROR);
            return;
        }
        if(newFinanceNameField.getText().isEmpty()){
            displayLoginPopup("Empty", "You didn't enter Name", Alert.AlertType.ERROR);
            return;
        } else if (newFinanceAmountField.getText().isEmpty() || newFinanceAmountField.getText().matches("[0-9]")){
            displayLoginPopup("Empty", "You didn't enter correct Amount", Alert.AlertType.ERROR);
            return;
        }
        financeOs.addExpenseToCat(catId, newFinanceNameField.getText(), newFinanceAmountField.getText());
        newFinanceNameField.clear();
        newFinanceAmountField.clear();
        fillExpenseList();
    }
    public void editCategoryAction(ActionEvent actionEvent) throws Exception {
        if(!financeOs.checkAccessLevel(3)){
            displayLoginPopup("Locked", "You AccessLevel does not match, Access Denied.", Alert.AlertType.ERROR);
            return;
        }
        if(newCatNameField.getText().isEmpty()){
            displayLoginPopup("Empty", "You didn't enter Name", Alert.AlertType.ERROR);
            return;
        } else if (newCatDiscriptionField.getText().isEmpty()){
            displayLoginPopup("Empty", "You didn't enter Description", Alert.AlertType.ERROR);
            return;
        }
        String newName = this.newCatNameField.getText();
        String newDiscription = this.newCatDiscriptionField.getText();
        if(!newCatNameField.getText().isEmpty()){
            financeOs.editNameOfSelCat(catId, newName);
        }
        if(!newCatDiscriptionField.getText().isEmpty()){
            financeOs.editDescriptionOfSelCat(catId, newDiscription);
        }
        newCatDiscriptionField.clear();
        newCatDiscriptionField.clear();
        fillExpenseList();
    }
    public void editFinanceAction(ActionEvent actionEvent) throws Exception {
        if(!financeOs.checkAccessLevel(3)){
            displayLoginPopup("Locked", "You AccessLevel does not match, Access Denied.", Alert.AlertType.ERROR);
            return;
        }
        String newName = this.newFinanceNameFieldEdit.getText();
        String newAmount = this.newFinanceAmountFieldEdit.getText();
        if(!newFinanceNameFieldEdit.getText().isEmpty()){
            financeOs.editExpenseName(catId,selectedExpenseId, newName);
        }
        if(!newFinanceAmountFieldEdit.getText().isEmpty()){
            financeOs.editExpenseAmount(catId,selectedExpenseId, newAmount);
        }
        newFinanceAmountFieldEdit.clear();
        newFinanceNameFieldEdit.clear();
        fillExpenseList();
    }
    public void exitToFinanceMenu() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../../FXML/FinancePage.fxml"));
        Parent root = loader.load();

        FinanceController controller = loader.getController();
        controller.SetFinanceOs(financeOs);

        Stage stage = (Stage) exitToFinanceButton.getScene().getWindow();

        stage.setTitle("fms/FinanceOS");
        stage.setScene(new Scene(root));
        stage.show();
    }
    public void goToIncomesMenu() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../../FXML/EditCategoryIncomes.fxml"));
        Parent root = loader.load();

        EditCatControllerIncomes controller = loader.getController();
        controller.SetFinanceOs(financeOs, catId);

        Stage stage = (Stage) incomesMenuButton.getScene().getWindow();

        stage.setTitle("FinancesOS");
        stage.setScene(new Scene(root));
        stage.show();
    }
    public void deleteExpense(ActionEvent actionEvent) throws Exception {
        financeOs.removeExpensefromCat(catId, selectedExpenseId);
        fillExpenseList();
    }
    public void deSelecetExp(MouseEvent mouseEvent) {
        expenseList.getSelectionModel().clearSelection();
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
