package cpe_lab_inventory;

public class Transaction {
    private int transactionId;
    private String studentId;
    private String studentName;
    private int itemId;
    private String itemName;
    private String status;
    private String timestamp;

    public Transaction(int transactionId, String studentId, String studentName, int itemId, String itemName, String status, String timestamp) {
        this.transactionId = transactionId;
        this.studentId = studentId;
        this.studentName = studentName;
        this.itemId = itemId;
        this.itemName = itemName;
        this.status = status;
        this.timestamp = timestamp;
    }

    public int getTransactionId() { return transactionId; }
    public String getStudentId() { return studentId; }
    public String getStudentName() { return studentName; }
    public int getItemId() { return itemId; }
    public String getItemName() { return itemName; }
    public String getStatus() { return status; }
    public String getTimestamp() { return timestamp; }
}
