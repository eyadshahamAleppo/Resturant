package restaurantsystem;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Order class represents a customer order
 */
public class Order implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private static int orderCounter = 1;
    
    private int orderId;
    private LocalDateTime orderDate;
    private Map<MenuItem, Integer> items;
    private double subtotal;
    private double discountAmount;
    private double total;
    private Status status;
    private Payment payment;
    private Address deliveryAddress;
    private Table table;
    private Systemmode orderType;
    private String customerId;

    public Order(String customerId, Map<MenuItem, Integer> items, 
                 Systemmode orderType, Table table) {
        this.orderId = orderCounter++;
        this.orderDate = LocalDateTime.now();
        this.items = new HashMap<>(items);
        this.customerId = customerId;
        this.orderType = orderType;
        this.table = table;
        this.status = Status.PENDING;
        calculateSubtotal();
    }

    // Getters and Setters
    public int getOrderId() {
        return orderId;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public Map<MenuItem, Integer> getItems() {
        return items;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public double getDiscountAmount() {
        return discountAmount;
    }

    public double getTotal() {
        return total;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public Address getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(Address deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public Table getTable() {
        return table;
    }

    public void setTable(Table table) {
        this.table = table;
    }

    public Systemmode getOrderType() {
        return orderType;
    }

    public String getCustomerId() {
        return customerId;
    }

    /**
     * Add item to order
     */
    public void addItem(MenuItem item, int quantity) {
        if (item == null) {
            System.out.println("❌ Cannot add null item!");
            return;
        }
        if (quantity <= 0) {
            System.out.println("❌ Quantity must be greater than 0!");
            return;
        }

        if (items.containsKey(item)) {
            items.put(item, items.get(item) + quantity);
            System.out.println("✅ Updated quantity for " + item.getName());
        } else {
            items.put(item, quantity);
            System.out.println("✅ Added " + item.getName() + " x" + quantity);
        }
        calculateSubtotal();
    }

    /**
     * Remove item from order
     */
    public void removeItem(MenuItem item) {
        if (item == null) {
            System.out.println("❌ Cannot remove null item!");
            return;
        }
        if (items.remove(item) != null) {
            System.out.println("✅ Removed " + item.getName() + " from order");
            calculateSubtotal();
        } else {
            System.out.println("❌ Item not found in order");
        }
    }

    /**
     * Update quantity of an item
     */
    public void updateQuantity(MenuItem item, int newQuantity) {
        if (item == null) {
            System.out.println("❌ Cannot update null item!");
            return;
        }
        if (newQuantity <= 0) {
            System.out.println("❌ Quantity must be greater than 0!");
            return;
        }

        if (items.containsKey(item)) {
            items.put(item, newQuantity);
            System.out.println("✅ Updated " + item.getName() + " quantity to " + newQuantity);
            calculateSubtotal();
        } else {
            System.out.println("❌ Item not found in order.");
        }
    }

    /**
     * Calculate subtotal (before discount)
     */
    public void calculateSubtotal() {
        subtotal = 0;
        for (Map.Entry<MenuItem, Integer> entry : items.entrySet()) {
            subtotal += entry.getKey().getPrice() * entry.getValue();
        }
    }

    /**
     * Apply elite discount if applicable
     */
    public void applyEliteDiscount(boolean isElite, boolean isActive) {
        if (isElite && isActive) {
            discountAmount = subtotal * 0.10;
            System.out.println("✅ Elite discount (10%) applied: EGP " + 
                             String.format("%.2f", discountAmount));
        } else {
            discountAmount = 0;
            if (isElite && !isActive) {
                System.out.println("⚠️ Elite membership expired. Renew to get 10% discount!");
            }
        }
    }

    /**
     * Calculate total (after discount)
     */
    public void calculateTotal() {
        total = subtotal - discountAmount;
    }

    /**
     * Update order status
     */
    public void updateStatus(Status newStatus) {
        status = newStatus;
        System.out.println("📋 Order status updated to: " + status);
    }

    /**
     * Get order summary
     */
    public String getOrderSummary() {
        StringBuilder sb = new StringBuilder();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        
        sb.append("\n").append("=".repeat(60)).append("\n");
        sb.append("                    ORDER SUMMARY\n");
        sb.append("=".repeat(60)).append("\n");
        sb.append("Order ID: ").append(orderId).append("\n");
        sb.append("Date: ").append(orderDate.format(formatter)).append("\n");
        sb.append("Type: ").append(orderType).append("\n");
        sb.append("Customer ID: ").append(customerId).append("\n");
        sb.append("-".repeat(60)).append("\n");
        sb.append("Items:\n");

        for (Map.Entry<MenuItem, Integer> entry : items.entrySet()) {
            MenuItem item = entry.getKey();
            int qty = entry.getValue();
            double itemTotal = item.getPrice() * qty;
            sb.append(String.format("  - %-25s x%-3d = EGP %7.2f\n", 
                                   item.getName(), qty, itemTotal));
        }

        sb.append("-".repeat(60)).append("\n");
        sb.append(String.format("Subtotal:        EGP %7.2f\n", subtotal));
        sb.append(String.format("Discount:        EGP %7.2f\n", discountAmount));
        sb.append(String.format("TOTAL:           EGP %7.2f\n", total));
        sb.append("-".repeat(60)).append("\n");
        sb.append("Status: ").append(status).append("\n");

        if (deliveryAddress != null) {
            sb.append("Delivery Address: ").append(deliveryAddress.getFullAddress()).append("\n");
        }
        if (table != null) {
            sb.append("Table: #").append(table.getTableNumber()).append("\n");
        }
        
        sb.append("=".repeat(60)).append("\n");
        return sb.toString();
    }

    /**
     * Get order counter for resetting
     */
    public static int getOrderCounter() {
        return orderCounter;
    }

    /**
     * Set order counter (used when loading from file)
     */
    public static void setOrderCounter(int counter) {
        orderCounter = counter;
    }

    @Override
    public String toString() {
        return "Order #" + orderId + " [" + orderType + ", Status: " + status + 
               ", Total: EGP " + total + "]";
    }
}
