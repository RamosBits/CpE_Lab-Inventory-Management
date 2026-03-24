package cpe_lab_inventory;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class StudentsController extends BaseModuleController {
    @FXML
    private TableView<Student> studentsTable;
    @FXML
    private TextField studentIdField;
    @FXML
    private TextField studentNameField;
    @FXML
    private TextField studentCourseField;
    @FXML
    private TextField studentYearField;
    @FXML
    private TextField studentContactField;
    @FXML
    private TableColumn<Student, String> studentIdColumn;
    @FXML
    private TableColumn<Student, String> studentNameColumn;
    @FXML
    private TableColumn<Student, String> studentCourseColumn;
    @FXML
    private TableColumn<Student, Integer> studentYearColumn;
    @FXML
    private TableColumn<Student, String> studentContactColumn;
    @FXML
    private TableColumn<Student, String> registeredDateColumn;

    @FXML
    public void initialize() {
        setupStudentsTableColumns();
        loadStudentsTable();
    }

    private void setupStudentsTableColumns() {
        studentIdColumn.setCellValueFactory(new PropertyValueFactory<>("studentId"));
        studentNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        studentCourseColumn.setCellValueFactory(new PropertyValueFactory<>("course"));
        studentYearColumn.setCellValueFactory(new PropertyValueFactory<>("yearLevel"));
        studentContactColumn.setCellValueFactory(new PropertyValueFactory<>("contactNumber"));
        registeredDateColumn.setCellValueFactory(new PropertyValueFactory<>("registeredAt"));
    }

    @FXML
    private void handleAddStudent() {
        String studentId = studentIdField.getText().trim();
        String name = studentNameField.getText().trim();
        String course = studentCourseField.getText().trim();
        String yearStr = studentYearField.getText().trim();
        String contact = studentContactField.getText().trim();

        if (studentId.isEmpty() || name.isEmpty() || course.isEmpty() || yearStr.isEmpty() || contact.isEmpty()) {
            showAlert("Validation Error", "All fields are required.", Alert.AlertType.WARNING);
            return;
        }

        try {
            int year = Integer.parseInt(yearStr);
            if (year < 1 || year > 4) {
                showAlert("Validation Error", "Year level must be between 1 and 4.", Alert.AlertType.WARNING);
                return;
            }

            boolean success = DatabaseManager.saveStudent(studentId, name, course, year, contact);
            if (success) {
                showAlert("Success", "Student added successfully!", Alert.AlertType.INFORMATION);
                clearStudentFields();
                loadStudentsTable();
            } else {
                showAlert("Error", "Failed to add student. Student ID may already exist.", Alert.AlertType.ERROR);
            }
        } catch (NumberFormatException e) {
            showAlert("Input Error", "Year level must be a valid number.", Alert.AlertType.ERROR);
        }
    }

    private void clearStudentFields() {
        studentIdField.clear();
        studentNameField.clear();
        studentCourseField.clear();
        studentYearField.clear();
        studentContactField.clear();
    }

    private void loadStudentsTable() {
        try {
            ObservableList<Student> students = DatabaseManager.getAllStudents();
            studentsTable.setItems(students);
        } catch (Exception e) {
            showAlert("Database Error", "Failed to load students table: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
}
