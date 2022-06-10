package util;

public class OrderTM {
    private String id;
    private String customerId;
    private String itemId;
    private int itemQuantity;
    private int value;

    public OrderTM() {
    }

    public OrderTM(String itemId, int itemQuantity, int value) {
        this.itemId = itemId;
        this.itemQuantity = itemQuantity;
        this.value = value;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public int getItemQuantity() {
        return itemQuantity;
    }

    public void setItemQuantity(int itemQuantity) {
        this.itemQuantity = itemQuantity;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
