package fms.Controllers;

import fms.model.Categories.Category;
import fms.model.FinanceManagementSystem;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.event.ActionEvent;
import javafx.stage.Stage;

import java.io.IOException;


public class FinanceController {
    public Button addCatButton;
    public Button deleteCatButton;
    public Button editCatButton;
    public Button exitToMenuButton;
    public TextField categoryName;
    public TextField categoryDescription;
    public TreeView selectionTree;
    public Label selectedCatInfo;


    private FinanceManagementSystem financeOs;
    private String selectedCategoryId;
    private Category selectedCat = null;

    private void fillCatTree(){
        selectionTree.setRoot(new TreeItem<String>("fms/model/Categories"));
        financeOs.getTopCategories().forEach(cat -> addTreeItems(cat, selectionTree.getRoot()));
        selectionTree.setShowRoot(false);
    }

    private void addTreeItems(Category category, TreeItem parentItem) {
        TreeItem<String> sectionTreeItem = new TreeItem<String>("ID : " + category.getId() + "   Name -   " + category.getName());
        parentItem.getChildren().add(sectionTreeItem);
        category.getSubCategories().forEach(cat -> addTreeItems(cat, sectionTreeItem));
    }
    public void SetFinanceOs(FinanceManagementSystem os) {
        this.financeOs = os;
        fillCatTree();
    }
    public void addCategotyAction(ActionEvent actionEvent) throws Exception {
        if(!financeOs.checkAccessLevel(3)){
            displayLoginPopup("Locked", "You AccessLevel does not match, Access Denied.", Alert.AlertType.ERROR);
            return;
        }
        if(categoryName.getText().isEmpty()){
            displayLoginPopup("Empty", "You didn't enter Name", Alert.AlertType.ERROR);
            return;
        }
        if(selectionTree.getSelectionModel().isEmpty()){
            financeOs.addTopCategory(categoryName.getText(), categoryDescription.getText());
        } else {
            financeOs.addSubCategory(selectedCategoryId, categoryName.getText(), categoryDescription.getText());
        }
        categoryName.setText("");
        categoryDescription.setText("");
        fillCatTree();
    }
    public void deleteCategoryAction(ActionEvent actionEvent) throws Exception {
        if(!financeOs.checkAccessLevel(3)){
            displayLoginPopup("Locked", "You AccessLevel does not match, Access Denied.", Alert.AlertType.ERROR);
            return;
        }
        if(selectedCategoryId == null) {
            System.out.println("no parameters");
        }
        financeOs.removeCategory(selectedCategoryId);
        fillCatTree();
        deselectItem();
    }
    public void deselectItem() {
        selectedCat = null;
        selectedCategoryId = null;
        selectionTree.getSelectionModel().clearSelection();
        selectedCatInfo.setText("");
    }
    public void getClickedTreeItem(MouseEvent mouseEvent) {
        String TreeItemText;
        if(selectionTree.getSelectionModel().isEmpty()) // if no items selected return
            return;

        TreeItemText = selectionTree.getSelectionModel().getSelectedItem().toString();
        String[] split = TreeItemText.split("ID : ");
        split = split[1].split("   Name -   ");
        selectedCategoryId = split[0].trim();
        selectedCat = financeOs.getCategory(selectedCategoryId);
        showSelectedCatLabel();
        System.out.println("Selected id: " + selectedCategoryId);
    }
    public void showSelectedCatLabel() {
        selectedCatInfo.setText("Name: " + selectedCat.getName() + "\nDescription: " + selectedCat.getDescription());
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

    public void EditCategory(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../../FXML/EditCategoryIncomes.fxml"));
        Parent root = loader.load();

        EditCatControllerIncomes controller = loader.getController();
        controller.SetFinanceOs(financeOs, selectedCategoryId);

        Stage stage = (Stage) editCatButton.getScene().getWindow();

        stage.setTitle("FinancesOS");
        stage.setScene(new Scene(root));
        stage.show();
    }
    public void exitProgram(){
        displayLoginPopup("Exit the program", "You exited the program", Alert.AlertType.INFORMATION);
        System.out.println("Exit");
        System.exit(0);
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

    public void saveData(ActionEvent actionEvent) {
    }
}
