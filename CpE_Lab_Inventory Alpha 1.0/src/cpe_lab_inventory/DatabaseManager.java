package cpe_lab_inventory;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseManager {
    private static final String URL = "jdbc:mysql://localhost:3306/cpelab_db";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static User authenticateUser(String username, String password) {
        try (Connection conn = getConnection()) {
            String query = "SELECT user_id, username, password, f_name, l_name, roles FROM users WHERE username = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, username);
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String storedPassword = rs.getString("password");
                if (storedPassword.equals(password)) {
                    return new User(
                        rs.getInt("user_id"),
                        rs.getString("username"),
                        rs.getString("f_name"),
                        rs.getString("l_name"),
                        rs.getString("roles")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean addItem(String itemName, String type, int totalStock, int userId) {
        try (Connection conn = getConnection()) {
            String query = "INSERT INTO items (item_name, type, total_stock, current_stock, users_user_id, timestamp) VALUES (?, ?, ?, ?, ?, NOW())";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, itemName);
            stmt.setString(2, type);
            stmt.setInt(3, totalStock);
            stmt.setInt(4, totalStock);
            stmt.setInt(5, userId);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean saveStudent(String studentId, String name, String course, int yearLevel, String contactNumber) {
        try (Connection conn = getConnection()) {
            String query = "INSERT INTO students (student_id, name, course, year_level, contact_number) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, studentId);
            stmt.setString(2, name);
            stmt.setString(3, course);
            stmt.setInt(4, yearLevel);
            stmt.setString(5, contactNumber);
            
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static ObservableList<String> getStudentsList() {
        ObservableList<String> studentList = FXCollections.observableArrayList();
        try (Connection conn = getConnection()) {
            String query = "SELECT CONCAT(student_id, ' - ', name) as display FROM students ORDER BY name";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                studentList.add(rs.getString("display"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return studentList;
    }

    public static boolean updateItem(int itemId, String itemName, String type, int totalStock) {
        try (Connection conn = getConnection()) {
            String query = "UPDATE items SET item_name = ?, type = ?, total_stock = ? WHERE item_id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, itemName);
            stmt.setString(2, type);
            stmt.setInt(3, totalStock);
            stmt.setInt(4, itemId);
            
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean updateItem(int itemId, String itemName, String type, int totalStock, int currentStock) {
        try (Connection conn = getConnection()) {
            String query = "UPDATE items SET item_name = ?, type = ?, total_stock = ?, current_stock = ? WHERE item_id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, itemName);
            stmt.setString(2, type);
            stmt.setInt(3, totalStock);
            stmt.setInt(4, currentStock);
            stmt.setInt(5, itemId);
            
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean deleteItem(int itemId) {
        try (Connection conn = getConnection()) {
            String query = "UPDATE items SET is_deleted = 1 WHERE item_id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, itemId);
            
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static ObservableList<Item> getAllItems() {
        ObservableList<Item> itemList = FXCollections.observableArrayList();
        try (Connection conn = getConnection()) {
            String query = "SELECT item_id, item_name, type, total_stock, current_stock, users_user_id, timestamp FROM items WHERE is_deleted = 0 ORDER BY item_id";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Item item = new Item(
                    rs.getInt("item_id"),
                    rs.getString("item_name"),
                    rs.getString("type"),
                    rs.getInt("total_stock"),
                    rs.getInt("current_stock"),
                    rs.getInt("users_user_id"),
                    rs.getString("timestamp")
                );
                itemList.add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return itemList;
    }

    public static ObservableList<Student> getAllStudents() {
        ObservableList<Student> studentList = FXCollections.observableArrayList();
        try (Connection conn = getConnection()) {
            String query = "SELECT student_id, name, course, year_level, contact_number, registered_at FROM students ORDER BY name";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Student student = new Student(
                    rs.getString("student_id"),
                    rs.getString("name"),
                    rs.getString("course"),
                    rs.getInt("year_level"),
                    rs.getString("contact_number"),
                    rs.getString("registered_at")
                );
                studentList.add(student);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return studentList;
    }

    public static ObservableList<Transaction> getAllTransactions() {
        ObservableList<Transaction> transactionList = FXCollections.observableArrayList();
        try (Connection conn = getConnection()) {
            String query = "SELECT t.transaction_id, t.student_id, s.name as student_name, t.item_id, i.item_name, t.status, t.timestamp FROM transactions t LEFT JOIN students s ON t.student_id = s.student_id LEFT JOIN items i ON t.item_id = i.item_id ORDER BY t.transaction_id DESC";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Transaction transaction = new Transaction(
                    rs.getInt("transaction_id"),
                    rs.getString("student_id"),
                    rs.getString("student_name"),
                    rs.getInt("item_id"),
                    rs.getString("item_name"),
                    rs.getString("status"),
                    rs.getString("timestamp")
                );
                transactionList.add(transaction);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transactionList;
    }

    public static boolean addTransaction(String studentId, int encoderId, String itemIdStr, int quantity, int orNum, String status) {
        try (Connection conn = getConnection()) {
            int itemId = Integer.parseInt(itemIdStr.trim());
            String cleanStudentId = studentId.trim();
            
            System.out.println("DEBUG - Adding Transaction: studentId=" + cleanStudentId + ", encoderId=" + encoderId + ", itemId=" + itemId + ", quantity=" + quantity + ", orNum=" + orNum + ", status=" + status);
            
            String query = "INSERT INTO transactions (student_id, encoder_id, item_id, quantity_taken, or_num, borrow_date, status, timestamp) VALUES (?, ?, ?, ?, ?, NOW(), ?, NOW())";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, cleanStudentId);
            stmt.setInt(2, encoderId);
            stmt.setInt(3, itemId);
            stmt.setInt(4, quantity);
            stmt.setInt(5, orNum);
            stmt.setString(6, status);
            
            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            System.out.println("SQL Error in addTransaction: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
}
