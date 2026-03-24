package cpe_lab_inventory;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RegisterController {
    @FXML
    private TextField usernameField;
    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField confirmPasswordField;
    @FXML
    private ComboBox<String> rolesCombo;
    @FXML
    private TextField courseField;
    @FXML
    private ComboBox<String> yearLevelCombo;
    @FXML
    private Label messageLabel;

    @FXML
    public void initialize() {
        rolesCombo.setItems(FXCollections.observableArrayList("Student Assistant", "Faculty Staff"));
        yearLevelCombo.setItems(FXCollections.observableArrayList("1", "2", "3", "4"));
    }

    @FXML
    private void handleRegister() {
        String username = usernameField.getText().trim();
        String firstName = firstNameField.getText().trim();
        String lastName = lastNameField.getText().trim();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        String role = rolesCombo.getValue();
        String course = courseField.getText().trim();
        String yearLevel = yearLevelCombo.getValue();

        if (username.isEmpty() || firstName.isEmpty() || lastName.isEmpty() || password.isEmpty() || role == null || course.isEmpty() || yearLevel == null) {
            messageLabel.setText("All fields are required.");
            messageLabel.setStyle("-fx-text-fill: #d32f2f;");
            return;
        }

        if (!password.equals(confirmPassword)) {
            messageLabel.setText("Passwords do not match.");
            messageLabel.setStyle("-fx-text-fill: #d32f2f;");
            return;
        }

        if (registerUser(username, firstName, lastName, password, role, course, yearLevel)) {
            messageLabel.setText("Registration successful! Redirecting to login...");
            messageLabel.setStyle("-fx-text-fill: #4caf50;");
            new Thread(() -> {
                try {
                    Thread.sleep(2000);
                    loadLoginScreen();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }

    private boolean registerUser(String username, String firstName, String lastName, String password, String role, String course, String yearLevel) {
        try (Connection conn = DatabaseManager.getConnection()) {
            String query = "INSERT INTO users (username, password, f_name, l_name, roles, course, year_level, created_at) VALUES (?, ?, ?, ?, ?, ?, ?, NOW())";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, firstName);
            stmt.setString(4, lastName);
            stmt.setString(5, role);
            stmt.setString(6, course);
            stmt.setString(7, yearLevel);

            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            messageLabel.setText("Registration failed: " + e.getMessage());
            messageLabel.setStyle("-fx-text-fill: #d32f2f;");
            return false;
        }
    }

    @FXML
    private void handleBackToLogin() {
        loadLoginScreen();
    }

    private void loadLoginScreen() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));
            Parent root = loader.load();
            
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(root, 500, 400));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
