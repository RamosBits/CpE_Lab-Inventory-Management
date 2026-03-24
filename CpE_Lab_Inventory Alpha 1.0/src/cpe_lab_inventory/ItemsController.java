package cpe_lab_inventory;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class ItemsController extends BaseModuleController {
    @FXML
    private TableView<Item> itemsTable;
    @FXML
    private TextField itemNameField;
    @FXML
    private ComboBox<String> itemTypeCombo;
    @FXML
    private TextField itemStockField;
    @FXML
    private TableColumn<Item, Integer> itemIdColumn;
    @FXML
    private TableColumn<Item, String> itemNameColumn;
    @FXML
    private TableColumn<Item, String> itemTypeColumn;
    @FXML
    private TableColumn<Item, Integer> totalStockColumn;
    @FXML
    private TableColumn<Item, Integer> currentStockColumn;

    @FXML
    public void initialize() {
        itemTypeCombo.setItems(FXCollections.observableArrayList("Tool", "Consumable"));
        setupItemsTableColumns();
        loadItemsTable();
    }

    private void setupItemsTableColumns() {
        itemIdColumn.setCellValueFactory(new PropertyValueFactory<>("itemId"));
        itemNameColumn.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        itemTypeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        totalStockColumn.setCellValueFactory(new PropertyValueFactory<>("totalStock"));
        currentStockColumn.setCellValueFactory(new PropertyValueFactory<>("currentStock"));
    }

    @FXML
    private void handleAddItem() {
        String itemName = itemNameField.getText().trim();
        String itemType = itemTypeCombo.getValue();
        String stockStr = itemStockField.getText().trim();

        if (itemName.isEmpty() || itemType == null || stockStr.isEmpty()) {
            showAlert("Validation Error", "All fields are required.", Alert.AlertType.WARNING);
            return;
        }

        try {
            int stock = Integer.parseInt(stockStr);
            boolean success = DatabaseManager.addItem(itemName, itemType, stock, currentUser.getUserId());
            if (success) {
                showAlert("Success", "Item added successfully!", Alert.AlertType.INFORMATION);
                itemNameField.clear();
                itemStockField.clear();
                itemTypeCombo.setValue(null);
                loadItemsTable();
            } else {
                showAlert("Error", "Failed to add item.", Alert.AlertType.ERROR);
            }
        } catch (NumberFormatException e) {
            showAlert("Input Error", "Stock must be a valid number.", Alert.AlertType.ERROR);
        }
    }

    public void loadItemsTable() {
        try {
            ObservableList<Item> items = DatabaseManager.getAllItems();
            itemsTable.setItems(items);
        } catch (Exception e) {
            showAlert("Database Error", "Failed to load items table: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleEditItem() {
        Item selectedItem = itemsTable.getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            showAlert("No Selection", "Please select an item to edit.", Alert.AlertType.WARNING);
            return;
        }

        EditItemDialogController.showEditDialog(selectedItem, this);
    }

    @FXML
    private void handleDeleteItem() {
        Item selectedItem = itemsTable.getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            showAlert("No Selection", "Please select an item to delete.", Alert.AlertType.WARNING);
            return;
        }

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Delete");
        confirmAlert.setHeaderText(null);
        confirmAlert.setContentText("Delete \"" + selectedItem.getItemName() + "\"? (Data will be hidden but kept in database)");
        
        if (confirmAlert.showAndWait().get() == javafx.scene.control.ButtonType.OK) {
            boolean success = DatabaseManager.deleteItem(selectedItem.getItemId());
            if (success) {
                showAlert("Success", "Item deleted successfully!", Alert.AlertType.INFORMATION);
                loadItemsTable();
            } else {
                showAlert("Error", "Failed to delete item.", Alert.AlertType.ERROR);
            }
        }
    }
}