package restaurantsystem;

import java.util.*;

public class Payment {
    /**
     * Payment method enum
     */
    public enum PaymentMethod {
        CASH,
        DEBIT_CARD,
        CREDIT_CARD,
        MOBILE_WALLET
    }
    
    private double amount;
    private PaymentMethod paymentMethod;
    private Status status;
    private int orderId;

    public Payment(double amount, PaymentMethod paymentMethod, int orderId) {
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.orderId = orderId;
        this.status = Status.PENDING;
    }

    // Getters and Setters
    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public int getOrderId() {
        return orderId;
    }

    // ==================== STATIC METHODS ====================
    
    /**
     * Process payment for order (static helper)
     */
    public static void processPayment(Order order, Scanner scanner) {
        System.out.println("\n--- PAYMENT ---");
        System.out.println("💰 Total: EGP " + order.getTotal());
        System.out.println("\nSelect payment method:");
        System.out.println("1. 💵 Cash");
        System.out.println("2. 💳 Credit Card");
        System.out.println("3. 💳 Debit Card");
        System.out.println("4. 📱 Mobile Wallet");
        System.out.print("Choice: ");

        int choice;
        try {
            choice = scanner.nextInt();
            scanner.nextLine();
        } catch (InputMismatchException e) {
            scanner.nextLine();
            choice = 1;
        }

        PaymentMethod method;
        switch (choice) {
            case 1 -> method = PaymentMethod.CASH;
            case 2 -> method = PaymentMethod.CREDIT_CARD;
            case 3 -> method = PaymentMethod.DEBIT_CARD;
            case 4 -> method = PaymentMethod.MOBILE_WALLET;
            default -> method = PaymentMethod.CASH;
        }

        System.out.print("\nEnter payment amount: EGP ");
        double amount;
        try {
            amount = scanner.nextDouble();
            scanner.nextLine();
        } catch (InputMismatchException e) {
            scanner.nextLine();
            amount = order.getTotal();
        }

        Payment payment = new Payment(amount, method, order.getOrderId());
        payment.processPayment(order.getTotal());
        
        if (payment.getStatus() == Status.COMPLETE) {
            order.setPayment(payment);
        }
    }
    
    // ==================== INSTANCE METHODS ====================

    public boolean validatePayment(double orderTotal) {
        if (this.amount < orderTotal) {
            System.out.println("❌ Insufficient payment! Required: EGP " + orderTotal + 
                             ", Provided: EGP " + amount);
            return false;
        }
        System.out.println("✅ Payment validated.");
        return true;
    }

    public boolean processPayment(double orderTotal) {
        if (validatePayment(orderTotal)) {
            this.status = Status.COMPLETE;
            System.out.println("✅ Payment of EGP " + amount + " via " + paymentMethod + 
                             " completed!");
            
            if (amount > orderTotal) {
                double change = amount - orderTotal;
                System.out.println("💰 Change: EGP " + String.format("%.2f", change));
            }
            return true;
        } else {
            this.status = Status.FAILED;
            System.out.println("❌ Payment failed!");
            return false;
        }
    }

    public String getReceipt() {
        return "\n" + "=".repeat(40) +
               "\n           PAYMENT RECEIPT" +
               "\n" + "=".repeat(40) +
               "\nOrder ID: " + orderId +
               "\nAmount Paid: EGP " + String.format("%.2f", amount) +
               "\nPayment Method: " + paymentMethod +
               "\nStatus: " + status +
               "\n" + "=".repeat(40);
    }

    @Override
    public String toString() {
        return "Payment [Order #" + orderId + 
               ", Amount: EGP " + amount + 
               ", Method: " + paymentMethod + 
               ", Status: " + status + "]";
    }
}
