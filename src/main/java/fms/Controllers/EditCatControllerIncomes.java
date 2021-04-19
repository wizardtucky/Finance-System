package fms.Controllers;

import fms.model.FinanceManagementSystem;
import fms.model.Finances.Income;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class EditCatControllerIncomes {

    public ListView incomeList;
    public Button addFinanceButton;
    public Button editCategoryButton;
    public Button exitToFinanceButton;
    public Button removeFinanceButton;
    public TextField newCatNameField;
    public TextField newCatDiscriptionField;
    public TextField newFinanceAmountField;
    public TextField newFinanceNameField;
    public TextField newFinanceNameFieldEdit;
    public TextField newFinanceAmountFieldEdit;
    public Label selectedCatLabel;
    public Button expensesMenuButton;
    public Button editFinanceButton;
    private FinanceManagementSystem financeOs;

    private String catId;

    private String selectedIncomeId = "";

    private List<Income> income;

    public void SetFinanceOs(FinanceManagementSystem os, String id) {
        this.financeOs = os;
        this.catId = id;
        fillIncomeList();
    }
    public void fillIncomeList(){
        incomeList.getItems().clear();
        income = financeOs.getIncomeListById(catId);
        for(Income inc: income){
            incomeList.getItems().add(" ID: " + inc.getId() + ", " + inc.getName() + " (" + inc.getMoney() + ")");
        }
    }
    public void getSelectedIncomeItem(MouseEvent mouseEvent) {
        String listItemText;
        if(incomeList.getSelectionModel().isEmpty())
            return;
        listItemText = incomeList.getSelectionModel().getSelectedItem().toString();
        String[] split = listItemText.split(",");
        split = split[0].split("ID: ");
        selectedIncomeId = split[1];
        System.out.println(selectedIncomeId);
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
        financeOs.addIncomeToCat(catId, newFinanceNameField.getText(), newFinanceAmountField.getText());
        newFinanceNameField.clear();
        newFinanceAmountField.clear();
        fillIncomeList();
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
        fillIncomeList();
    }
    public void editFinanceAction(ActionEvent actionEvent) throws Exception {
        if(!financeOs.checkAccessLevel(3)){
            displayLoginPopup("Locked", "You AccessLevel does not match, Access Denied.", Alert.AlertType.ERROR);
            return;
        }
        if (newFinanceAmountFieldEdit.getText().isEmpty() || newFinanceAmountFieldEdit.getText().matches("[0-9]")){
            displayLoginPopup("Empty", "You didn't enter correct Amount", Alert.AlertType.ERROR);
            return;
        }
        String newName = this.newFinanceNameFieldEdit.getText();
        String newAmount = this.newFinanceAmountFieldEdit.getText();
        if(!newFinanceNameFieldEdit.getText().isEmpty()){
            financeOs.editIncomeName(catId,selectedIncomeId, newName);
        }
        if(!newFinanceAmountFieldEdit.getText().isEmpty()){
            financeOs.editIncomeAmount(catId,selectedIncomeId, newAmount);
        }
        newFinanceAmountFieldEdit.clear();
        newFinanceNameFieldEdit.clear();
        fillIncomeList();
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
    public void goToExpensesMenu() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../../FXML/EditCategoryExpenses.fxml"));
        Parent root = loader.load();

        EditCatControllerExpenses controller = loader.getController();
        controller.SetFinanceOs(financeOs, catId);

        Stage stage = (Stage) expensesMenuButton.getScene().getWindow();

        stage.setTitle("FinancesOS");
        stage.setScene(new Scene(root));
        stage.show();
    }
    public void deleteIncome(ActionEvent actionEvent) throws Exception {
        financeOs.removeIncomefromCat(catId, selectedIncomeId);
        fillIncomeList();
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
