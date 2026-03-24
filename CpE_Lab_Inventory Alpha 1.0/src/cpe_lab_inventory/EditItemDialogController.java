package cpe_lab_inventory;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class EditItemDialogController {
    @FXML
    private Label titleLabel;
    @FXML
    private TextField itemNameField;
    @FXML
    private ComboBox<String> itemTypeCombo;
    @FXML
    private TextField totalStockField;
    @FXML
    private TextField currentStockField;

    private Item item;
    private Stage dialogStage;
    private boolean saveClicked = false;

    @FXML
    public void initialize() {
        itemTypeCombo.setItems(FXCollections.observableArrayList("Tool", "Consumable"));
    }

    public void setItem(Item item) {
        this.item = item;
        itemNameField.setText(item.getItemName());
        itemTypeCombo.setValue(item.getType());
        totalStockField.setText(String.valueOf(item.getTotalStock()));
        currentStockField.setText(String.valueOf(item.getCurrentStock()));
        titleLabel.setText("Edit: " + item.getItemName());
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public boolean isSaveClicked() {
        return saveClicked;
    }

    @FXML
    private void handleSave() {
        if (validateInput()) {
            try {
                String itemName = itemNameField.getText().trim();
                String type = itemTypeCombo.getValue();
                int totalStock = Integer.parseInt(totalStockField.getText().trim());
                int currentStock = Integer.parseInt(currentStockField.getText().trim());

                if (currentStock > totalStock) {
                    showAlert("Validation Error", "Current Stock cannot be greater than Total Stock.", Alert.AlertType.WARNING);
                    return;
                }

                boolean success = DatabaseManager.updateItem(item.getItemId(), itemName, type, totalStock, currentStock);
                if (success) {
                    saveClicked = true;
                    dialogStage.close();
                } else {
                    showAlert("Error", "Failed to update item.", Alert.AlertType.ERROR);
                }
            } catch (NumberFormatException e) {
                showAlert("Input Error", "Stock values must be valid numbers.", Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    private void handleCancel() {
        saveClicked = false;
        dialogStage.close();
    }

    private boolean validateInput() {
        String itemName = itemNameField.getText().trim();
        String type = itemTypeCombo.getValue();
        String totalStockStr = totalStockField.getText().trim();
        String currentStockStr = currentStockField.getText().trim();

        if (itemName.isEmpty()) {
            showAlert("Validation Error", "Item Name is required.", Alert.AlertType.WARNING);
            return false;
        }
        if (type == null) {
            showAlert("Validation Error", "Type is required.", Alert.AlertType.WARNING);
            return false;
        }
        if (totalStockStr.isEmpty()) {
            showAlert("Validation Error", "Total Stock is required.", Alert.AlertType.WARNING);
            return false;
        }
        if (currentStockStr.isEmpty()) {
            showAlert("Validation Error", "Current Stock is required.", Alert.AlertType.WARNING);
            return false;
        }

        try {
            Integer.parseInt(totalStockStr);
            Integer.parseInt(currentStockStr);
        } catch (NumberFormatException e) {
            showAlert("Input Error", "Stock values must be valid numbers.", Alert.AlertType.ERROR);
            return false;
        }

        return true;
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void showEditDialog(Item item, ItemsController parentController) {
        try {
            FXMLLoader loader = new FXMLLoader(EditItemDialogController.class.getResource("EditItemDialog.fxml"));
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit Item");
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.setScene(new Scene(loader.load()));

            EditItemDialogController controller = loader.getController();
            controller.setItem(item);
            controller.setDialogStage(dialogStage);

            dialogStage.showAndWait();

            if (controller.isSaveClicked()) {
                parentController.loadItemsTable();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Failed to open edit dialog: " + e.getMessage());
            alert.showAndWait();
        }
    }
}
