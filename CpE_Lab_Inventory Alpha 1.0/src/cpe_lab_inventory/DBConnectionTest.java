package cpe_lab_inventory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class DBConnectionTest {
    public static void main(String[] args) {
        System.out.println("========== Database Connection Test ==========");
        
        try {
            System.out.println("Attempting to connect to cpelab_db...");
            Connection conn = DatabaseManager.getConnection();
            
            System.out.println("✓ Connection successful!");
            System.out.println("✓ Database URL: jdbc:mysql://localhost:3306/cpelab_db");
            
            // Test users table
            System.out.println("\n--- Testing USERS table ---");
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) as count FROM users");
            if (rs.next()) {
                System.out.println("✓ Users table count: " + rs.getInt("count"));
            }
            
            // Test items table
            System.out.println("\n--- Testing ITEMS table ---");
            rs = stmt.executeQuery("SELECT COUNT(*) as count FROM items");
            if (rs.next()) {
                System.out.println("✓ Items table count: " + rs.getInt("count"));
            }
            
            // Test transactions table
            System.out.println("\n--- Testing TRANSACTIONS table ---");
            rs = stmt.executeQuery("SELECT COUNT(*) as count FROM transactions");
            if (rs.next()) {
                System.out.println("✓ Transactions table count: " + rs.getInt("count"));
            }
            
            // Show sample user
            System.out.println("\n--- Sample User Data ---");
            rs = stmt.executeQuery("SELECT user_id, username, f_name, l_name, roles FROM users LIMIT 1");
            if (rs.next()) {
                System.out.println("ID: " + rs.getInt("user_id"));
                System.out.println("Username: " + rs.getString("username"));
                System.out.println("Name: " + rs.getString("f_name") + " " + rs.getString("l_name"));
                System.out.println("Role: " + rs.getString("roles"));
            } else {
                System.out.println("No users found - ready to register!");
            }
            
            conn.close();
            System.out.println("\n✓ Connection test passed!");
            
        } catch (Exception e) {
            System.out.println("✗ Connection failed!");
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
