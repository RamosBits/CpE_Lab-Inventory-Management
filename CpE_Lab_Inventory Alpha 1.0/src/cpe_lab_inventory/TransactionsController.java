package cpe_lab_inventory;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class TransactionsController extends BaseModuleController {
    @FXML
    private TableView<Transaction> transactionsTable;
    @FXML
    private ComboBox<String> studentCombo;
    @FXML
    private ComboBox<String> itemCombo;
    @FXML
    private TextField quantityField;
    @FXML
    private TextField orNumField;
    @FXML
    private TableColumn<Transaction, Integer> transIdColumn;
    @FXML
    private TableColumn<Transaction, String> studentColumn;
    @FXML
    private TableColumn<Transaction, Integer> itemColumn;
    @FXML
    private TableColumn<Transaction, String> statusColumn;
    @FXML
    private TableColumn<Transaction, String> dateColumn;

    @FXML
    public void initialize() {
        setupTransactionsTableColumns();
        loadStudentCombo();
        loadItemCombo();
        loadTransactionsTable();
    }

    private void setupTransactionsTableColumns() {
        transIdColumn.setCellValueFactory(new PropertyValueFactory<>("transactionId"));
        studentColumn.setCellValueFactory(new PropertyValueFactory<>("studentName"));
        itemColumn.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("timestamp"));
    }

    @FXML
    private void handleBorrow() {
        String studentStr = studentCombo.getValue();
        String itemStr = itemCombo.getValue();
        String quantityStr = quantityField.getText().trim();
        String orNumStr = orNumField.getText().trim();

        if (studentStr == null || itemStr == null || quantityStr.isEmpty() || orNumStr.isEmpty()) {
            showAlert("Validation Error", "Select student, item, and enter quantity and OR number.", Alert.AlertType.WARNING);
            return;
        }

        try {
            // Extract student ID from "student_id - name" format and trim whitespace
            String studentId = studentStr.split(" - ")[0].trim();
            
            // Extract item ID from "item_id - name" format and trim whitespace
            String itemId = itemStr.split(" - ")[0].trim();
            
            int quantity = Integer.parseInt(quantityStr);
            int orNum = Integer.parseInt(orNumStr);
            
            if (quantity <= 0) {
                showAlert("Validation Error", "Quantity must be greater than 0.", Alert.AlertType.WARNING);
                return;
            }
            
            boolean success = DatabaseManager.addTransaction(studentId, currentUser.getUserId(), itemId, quantity, orNum, "Borrowed");
            if (success) {
                showAlert("Success", "Item borrowed successfully!", Alert.AlertType.INFORMATION);
                clearTransactionFields();
                loadTransactionsTable();
            } else {
                showAlert("Error", "Failed to process borrow.", Alert.AlertType.ERROR);
            }
        } catch (NumberFormatException e) {
            showAlert("Input Error", "Quantity and OR number must be valid numbers.", Alert.AlertType.ERROR);
        } catch (Exception e) {
            showAlert("Error", "Error processing borrow: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleReturn() {
        String studentStr = studentCombo.getValue();
        String itemStr = itemCombo.getValue();
        String quantityStr = quantityField.getText().trim();
        String orNumStr = orNumField.getText().trim();

        if (studentStr == null || itemStr == null || quantityStr.isEmpty() || orNumStr.isEmpty()) {
            showAlert("Validation Error", "Select student, item, and enter quantity and OR number.", Alert.AlertType.WARNING);
            return;
        }

        try {
            // Extract student ID from "student_id - name" format and trim whitespace
            String studentId = studentStr.split(" - ")[0].trim();
            
            // Extract item ID from "item_id - name" format and trim whitespace
            String itemId = itemStr.split(" - ")[0].trim();
            
            int quantity = Integer.parseInt(quantityStr);
            int orNum = Integer.parseInt(orNumStr);
            
            if (quantity <= 0) {
                showAlert("Validation Error", "Quantity must be greater than 0.", Alert.AlertType.WARNING);
                return;
            }
            
            boolean success = DatabaseManager.addTransaction(studentId, currentUser.getUserId(), itemId, quantity, orNum, "Returned");
            if (success) {
                showAlert("Success", "Item returned successfully!", Alert.AlertType.INFORMATION);
                clearTransactionFields();
                loadTransactionsTable();
            } else {
                showAlert("Error", "Failed to process return.", Alert.AlertType.ERROR);
            }
        } catch (NumberFormatException e) {
            showAlert("Input Error", "Quantity and OR number must be valid numbers.", Alert.AlertType.ERROR);
        } catch (Exception e) {
            showAlert("Error", "Error processing return: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    
    private void clearTransactionFields() {
        quantityField.clear();
        orNumField.clear();
        studentCombo.setValue(null);
        itemCombo.setValue(null);
    }

    private void loadStudentCombo() {
        try {
            ObservableList<String> students = DatabaseManager.getStudentsList();
            studentCombo.setItems(students);
        } catch (Exception e) {
            showAlert("Database Error", "Failed to load students: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void loadItemCombo() {
        try {
            ObservableList<Item> items = DatabaseManager.getAllItems();
            itemCombo.getItems().clear();
            for (Item item : items) {
                itemCombo.getItems().add(item.getItemId() + " - " + item.getItemName());
            }
        } catch (Exception e) {
            showAlert("Database Error", "Failed to load items: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void loadTransactionsTable() {
        try {
            ObservableList<Transaction> transactions = DatabaseManager.getAllTransactions();
            transactionsTable.setItems(transactions);
        } catch (Exception e) {
            showAlert("Database Error", "Failed to load transactions table: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
}
