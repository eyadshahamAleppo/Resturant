package restaurantsystem;

import java.util.*;

public class Customer extends Person {
    private static int idCounter = 1;
    
    private String username;
    private boolean isEliteCustomer;
    private Address address;
    private int dineInCount;
    private double subscriptionFee = 100.0;
    private boolean subscriptionActive;
    private int monthsRemaining;

    public Customer(String username, String password, boolean isEliteCustomer,
                    Address address, String name, String email, String phoneNumber) {
        super(name, email, phoneNumber, password);
        this.id = "CUST" + String.format("%03d", idCounter++);
        this.username = username;
        this.isEliteCustomer = isEliteCustomer;
        this.address = address;
        this.dineInCount = 0;
        this.subscriptionActive = false;
        this.monthsRemaining = 0;
    }

    // Getters and Setters
    public String getCustomerId() { return id; }
    public String getUsername() { return username; }
    public boolean isEliteCustomer() { return isEliteCustomer; }
    public void setEliteCustomer(boolean eliteCustomer) { isEliteCustomer = eliteCustomer; }
    public Address getAddress() { return address; }
    public void setAddress(Address address) { this.address = address; }
    public int getDineInCount() { return dineInCount; }
    public double getSubscriptionFee() { return subscriptionFee; }
    public boolean isSubscriptionActive() { return subscriptionActive && monthsRemaining > 0; }
    public void setSubscriptionActive(boolean subscriptionActive) { 
        this.subscriptionActive = subscriptionActive; 
    }
    public int getMonthsRemaining() { return monthsRemaining; }
    public void setMonthsRemaining(int monthsRemaining) { this.monthsRemaining = monthsRemaining; }

    // ==================== STATIC METHODS ====================
    
    /**
     * Register new customer
     */
    public static void registerCustomer(ArrayList<Customer> customers, Scanner scanner) {
        System.out.println("\n=== CUSTOMER REGISTRATION ===");
        
        System.out.print("Enter your name: ");
        String name = scanner.nextLine();
        
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        
        System.out.print("Enter phone number: ");
        String phone = scanner.nextLine();
        
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        
        System.out.print("Enter address: ");
        String addressStr = scanner.nextLine();
        Address address = new Address(1, addressStr, true);
        
        Customer customer = new Customer(username, password, false, address, name, email, phone);
        customers.add(customer);
        
        System.out.println("\n✅ Registration completed!");
        System.out.println("👤 Your Customer ID: " + customer.getCustomerId());
        System.out.println("📝 Username: " + username);
        
        // Auto login
        System.out.print("\nLogin now? (y/n): ");
        String choice = scanner.nextLine();
        if (choice.equalsIgnoreCase("y")) {
            customer.customerMenu(RestaurantSystem.getMenu(), RestaurantSystem.getTables(), scanner);
        }
    }
    
    /**
     * Find customer by ID or username
     */
    public static Customer findCustomer(String idOrUsername, ArrayList<Customer> customers) {
        for (Customer c : customers) {
            if (c.getCustomerId().equalsIgnoreCase(idOrUsername) || 
                c.getUsername().equalsIgnoreCase(idOrUsername)) {
                return c;
            }
        }
        return null;
    }
    
    // ==================== INSTANCE METHODS ====================
    
    @Override
    public boolean login(String inputUsername, String inputPassword) {
        if ((this.username.equals(inputUsername) || this.id.equals(inputUsername))
                && this.password.equals(inputPassword)) {
            System.out.println("✅ Login successful! Welcome back, " + getName() + "!");
            System.out.println("📊 Dine-in Count: " + dineInCount);
            System.out.println("⭐ Elite Status: " + (isSubscriptionActive() ? "Active" : "Not Active"));
            return true;
        }
        return false;
    }
    
    /**
     * Customer menu
     */
    public void customerMenu(Menu menu, ArrayList<Table> tables, Scanner scanner) {
        while (true) {
            System.out.println("\n========== CUSTOMER MENU ==========");
            System.out.println("Hello, " + getName() + "!");
            System.out.println("====================================");
            System.out.println("1. 🌐 Online Delivery");
            System.out.println("2. 👤 View Profile");
            System.out.println("3. ⭐ Subscribe to Elite");
            System.out.println("4. 🔙 Logout");
            System.out.println("====================================");
            System.out.print("Choose an option: ");
            
            try {
                int choice = scanner.nextInt();
                scanner.nextLine();
                
                switch (choice) {
                    case 1 -> placeOnlineOrder(menu, scanner);
                    case 2 -> System.out.println("\n" + getDetails());
                    case 3 -> subscribeElite(scanner);
                    case 4 -> {
                        System.out.println("✅ Logged out!");
                        return;
                    }
                    default -> System.out.println("❌ Invalid choice!");
                }
            } catch (InputMismatchException e) {
                System.out.println("❌ Invalid input!");
                scanner.nextLine();
            }
        }
    }
    
    private void placeOnlineOrder(Menu menu, Scanner scanner) {
        System.out.println("\n=== ONLINE DELIVERY ORDER ===");
        
        System.out.println("Delivery Address: " + address.getFullAddress());
        System.out.print("Use this address? (y/n): ");
        String confirm = scanner.nextLine();
        
        Address deliveryAddress = address;
        if (confirm.equalsIgnoreCase("n")) {
            System.out.print("Enter new address: ");
            String newAddr = scanner.nextLine();
            deliveryAddress = new Address(2, newAddr, false);
        }
        
        Map<MenuItem, Integer> items = menu.selectMenuItems(scanner);
        if (items.isEmpty()) return;
        
        Order order = new Order(this.id, items, Systemmode.ONLINE_DELIVERY, null);
        order.setDeliveryAddress(deliveryAddress);
        order.calculateSubtotal();
        order.applyEliteDiscount(isEliteCustomer, isSubscriptionActive());
        order.calculateTotal();
        
        System.out.println(order.getOrderSummary());
        Payment.processPayment(order, scanner);
        RestaurantSystem.getOrders().add(order);
        
        System.out.println("\n✅ Order will be delivered to: " + deliveryAddress.getFullAddress());
    }
    
    public void incrementDineInCount() {
        dineInCount++;
        System.out.println("📈 Dine-in recorded! Total: " + dineInCount);
        
        if (dineInCount >= 5 && !isEliteCustomer) {
            System.out.println("🎉 You're eligible for Elite membership!");
        }
    }
    
    public void subscribeElite(Scanner scanner) {
        System.out.println("\n=== ELITE MEMBERSHIP ===");
        System.out.println("💰 Fee: EGP " + subscriptionFee);
        System.out.println("🎁 Benefits: 10% discount");
        System.out.println("📊 Your dine-ins: " + dineInCount + "/5");
        System.out.print("\nSubscribe? (y/n): ");
        String choice = scanner.nextLine();
        
        if (choice.equalsIgnoreCase("y")) {
            System.out.print("Process payment? (y/n): ");
            String confirm = scanner.nextLine();
            subscribeToElite(confirm.equalsIgnoreCase("y"));
        }
    }
    
    public boolean subscribeToElite(boolean paid) {
        if (paid || dineInCount >= 5) {
            setEliteCustomer(true);
            setSubscriptionActive(true);
            setMonthsRemaining(1);
            System.out.println("⭐ Elite activated! 10% discount on all orders!");
            return true;
        } else if (!paid) {
            System.out.println("💰 Payment required: EGP " + subscriptionFee);
            return false;
        } else {
            System.out.println("⚠️ Need 5 dine-ins. Current: " + dineInCount);
            return false;
        }
    }
    
    @Override
    public String getDetails() {
        return super.getDetails() +
               "\nCustomer ID: " + id +
               "\nUsername: " + username +
               "\n⭐ Elite: " + (isEliteCustomer ? "Yes" : "No") +
               "\n📊 Subscription: " + (isSubscriptionActive() ? "Active" : "Inactive") +
               "\n🏠 Address: " + address.getFullAddress() +
               "\n📈 Dine-ins: " + dineInCount;
    }
}
