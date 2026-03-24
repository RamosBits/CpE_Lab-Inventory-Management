package cpe_lab_inventory;

public class Item {
    private int itemId;
    private String itemName;
    private String type;
    private int totalStock;
    private int currentStock;
    private int usersUserId;
    private String timestamp;

    public Item(int itemId, String itemName, String type, int totalStock, int currentStock, int usersUserId, String timestamp) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.type = type;
        this.totalStock = totalStock;
        this.currentStock = currentStock;
        this.usersUserId = usersUserId;
        this.timestamp = timestamp;
    }

    public int getItemId() { return itemId; }
    public String getItemName() { return itemName; }
    public String getType() { return type; }
    public int getTotalStock() { return totalStock; }
    public int getCurrentStock() { return currentStock; }
    public int getUsersUserId() { return usersUserId; }
    public String getTimestamp() { return timestamp; }
}
