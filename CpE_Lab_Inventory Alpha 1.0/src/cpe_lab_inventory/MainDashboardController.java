package cpe_lab_inventory;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainDashboardController {
    @FXML
    private Label userLabel;

    private User currentUser;

    public void setUser(User user) {
        this.currentUser = user;
        userLabel.setText("Logged in as: " + user.toString());
    }

    @FXML
    public void initialize() {
        // Menu screen - no table setup needed
    }

    private void setupItemsTableColumns() {
        // Not needed for menu screen
    }

    private void setupStudentsTableColumns() {
        // Not needed for menu screen
    }

    private void setupTransactionsTableColumns() {
        // Not needed for menu screen
    }

    @FXML
    private void handleOpenItems() {
        openModule("Items.fxml", "Inventory Items");
    }

    @FXML
    private void handleOpenTransactions() {
        openModule("Transactions.fxml", "Transactions");
    }

    @FXML
    private void handleOpenStudents() {
        openModule("Students.fxml", "Students");
    }

    private void openModule(String fxmlFile, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();
            
            // Pass current user to the module controller
            if (loader.getController() instanceof BaseModuleController) {
                ((BaseModuleController) loader.getController()).setUser(currentUser);
                ((BaseModuleController) loader.getController()).setMainDashboard(this);
            }
            
            Stage stage = (Stage) userLabel.getScene().getWindow();
            stage.setScene(new Scene(root, 1200, 900));
            stage.setTitle(title);
            stage.show();
        } catch (Exception e) {
            showAlert("Error", "Failed to load " + title + ": " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void handleLogout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));
            Parent root = loader.load();
            
            Stage stage = (Stage) userLabel.getScene().getWindow();
            stage.setScene(new Scene(root, 500, 450));
            stage.show();
        } catch (Exception e) {
            showAlert("Error", "Failed to logout: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
}
