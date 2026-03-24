package cpe_lab_inventory;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public abstract class BaseModuleController {
    @FXML
    protected Label userLabel;
    
    protected User currentUser;
    protected MainDashboardController mainDashboardController;

    public void setUser(User user) {
        this.currentUser = user;
        if (userLabel != null) {
            userLabel.setText("Logged in as: " + user.toString());
        }
    }

    public void setMainDashboard(MainDashboardController controller) {
        this.mainDashboardController = controller;
    }

    @FXML
    protected void handleBackToMenu() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("MainDashboard.fxml"));
            Parent root = loader.load();
            
            MainDashboardController controller = loader.getController();
            controller.setUser(currentUser);
            
            Stage stage = (Stage) userLabel.getScene().getWindow();
            stage.setScene(new Scene(root, 1200, 900));
            stage.setTitle("CpE Lab Inventory - Dashboard");
            stage.show();
        } catch (Exception e) {
            showAlert("Error", "Failed to return to menu: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    protected void handleLogout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));
            Parent root = loader.load();
            
            Stage stage = (Stage) userLabel.getScene().getWindow();
            stage.setScene(new Scene(root, 500, 450));
            stage.setTitle("CpE Lab Inventory - Login");
            stage.show();
        } catch (Exception e) {
            showAlert("Error", "Failed to logout: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    protected void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
