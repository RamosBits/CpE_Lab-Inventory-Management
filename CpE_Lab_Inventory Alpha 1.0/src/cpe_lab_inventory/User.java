package cpe_lab_inventory;

public class User {
    private int userId;
    private String username;
    private String fName;
    private String lName;
    private String roles;

    public User(int userId, String username, String fName, String lName, String roles) {
        this.userId = userId;
        this.username = username;
        this.fName = fName;
        this.lName = lName;
        this.roles = roles;
    }

    public int getUserId() { return userId; }
    public String getUsername() { return username; }
    public String getfName() { return fName; }
    public String getlName() { return lName; }
    public String getRoles() { return roles; }

    @Override
    public String toString() {
        return fName + " " + lName + " (" + roles + ")";
    }
}
