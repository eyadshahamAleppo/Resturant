package restaurantsystem;

import java.util.*;

/**
 * Restaurant System - Main Class
 * Simple version without Controller (as per original UML)
 */
public class RestaurantSystem {
    // Global ArrayLists - حسب الـ UML الأصلي
    private static ArrayList<Customer> customers = new ArrayList<>();
    private static ArrayList<Cashier> cashiers = new ArrayList<>();
    private static Menu menu = new Menu();
    private static ArrayList<Table> tables = new ArrayList<>();
    private static ArrayList<Order> orders = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        initializeSystem();
        
        System.out.println("================================");
        System.out.println("   WELCOME TO RESTAURANT SYSTEM");
        System.out.println("================================\n");
        
        mainMenu();
    }
    
    // ==================== INITIALIZATION ====================
    
    private static void initializeSystem() {
        // إضافة عناصر للمينيو
        menu.addItem(new MenuItem("Burger", "Beef burger with cheese", 80.0, "Main", true));
        menu.addItem(new MenuItem("Pizza", "Margherita pizza", 120.0, "Main", true));
        menu.addItem(new MenuItem("Pasta", "Creamy pasta with chicken", 95.0, "Main", true));
        menu.addItem(new MenuItem("Salad", "Fresh green salad", 45.0, "Appetizer", true));
        menu.addItem(new MenuItem("Cola", "Soft drink", 20.0, "Beverage", true));
        menu.addItem(new MenuItem("Juice", "Fresh orange juice", 30.0, "Beverage", true));
        
        // إنشاء طاولات
        for (int i = 1; i <= 10; i++) {
            tables.add(new Table(i, 4, Table.TableStatus.AVAILABLE));
        }
        
        // إنشاء cashiers للتجربة
        cashiers.add(new Cashier("Ahmed Ali", "ahmed@restaurant.com", "0123456789", 
                                "cash123", 5000, "Morning"));
        cashiers.add(new Cashier("Sara Mohamed", "sara@restaurant.com", "0111222333", 
                                "cash456", 5000, "Evening"));
        
        System.out.println("✅ System initialized successfully!");
        System.out.println("\n👔 Sample Cashier Logins:");
        for (Cashier c : cashiers) {
            System.out.println("  - ID: " + c.getId() + " | Password: " + c.getPassword());
        }
        System.out.println();
    }
    
    // ==================== MAIN MENU ====================
    
    private static void mainMenu() {
        while (true) {
            System.out.println("\n========== MAIN MENU ==========");
            System.out.println("1. 📝 Register (New Customer)");
            System.out.println("2. 🔐 Login");
            System.out.println("3. 📋 View Menu");
            System.out.println("4. ❌ Exit");
            System.out.println("================================");
            System.out.print("Choose an option: ");
            
            int choice = getIntInput();
            
            switch (choice) {
                case 1 -> Customer.registerCustomer(customers, scanner);
                case 2 -> login();
                case 3 -> menu.displayMenu();
                case 4 -> {
                    System.out.println("Thank you for using our system!");
                    System.exit(0);
                }
                default -> System.out.println("❌ Invalid choice!");
            }
        }
    }
    
    // ==================== LOGIN ====================
    
    private static void login() {
        System.out.println("\n=== LOGIN ===");
        System.out.print("Enter your ID/Username (CUST### or CH###): ");
        String id = scanner.nextLine();
        
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        
        if (id.toUpperCase().startsWith("CUST")) {
            Customer customer = Customer.findCustomer(id, customers);
            if (customer != null && customer.login(id, password)) {
                customer.customerMenu(menu, tables, scanner);
            } else {
                System.out.println("❌ Login failed! Invalid credentials.");
            }
        } else if (id.toUpperCase().startsWith("CH")) {
            Cashier cashier = Cashier.findCashier(id, cashiers);
            if (cashier != null && cashier.login(id, password)) {
                cashierMenu(cashier);
            } else {
                System.out.println("❌ Login failed! Invalid credentials.");
            }
        } else {
            System.out.println("❌ Invalid ID format!");
        }
    }
    
    // ==================== CASHIER MENU ====================
    
    private static void cashierMenu(Cashier cashier) {
        while (true) {
            System.out.println("\n========== CASHIER MENU ==========");
            System.out.println("Hello, " + cashier.getName() + "!");
            System.out.println("===================================");
            System.out.println("1. 🛍️ Process Takeaway Order");
            System.out.println("2. 🍽️ Process Dine-In Order");
            System.out.println("3. 🪑 View All Tables");
            System.out.println("4. ✅ Release Table");
            System.out.println("5. 📋 View Menu");
            System.out.println("6. 🔙 Logout");
            System.out.println("===================================");
            System.out.print("Choose an option: ");
            
            int choice = getIntInput();
            
            switch (choice) {
                case 1 -> processTakeaway(cashier);
                case 2 -> processDineIn(cashier);
                case 3 -> viewTables();
                case 4 -> releaseTable();
                case 5 -> menu.displayMenu();
                case 6 -> {
                    System.out.println("✅ Logged out successfully!");
                    return;
                }
                default -> System.out.println("❌ Invalid choice!");
            }
        }
    }
    
    private static void processTakeaway(Cashier cashier) {
        System.out.println("\n=== PROCESS TAKEAWAY ORDER ===");
        
        System.out.print("Enter customer ID (or 0 for walk-in): ");
        String custId = scanner.nextLine();
        
        Customer customer = getOrCreateCustomer(custId);
        if (customer == null) return;
        
        Map<MenuItem, Integer> items = menu.selectMenuItems(scanner);
        if (items.isEmpty()) {
            System.out.println("❌ No items selected!");
            return;
        }
        
        Order order = cashier.processTakeawayOrder(customer, items);
        orders.add(order);
        cashier.printReceipt(order);
        
        processPayment(order, cashier);
    }
    
    private static void processDineIn(Cashier cashier) {
        System.out.println("\n=== PROCESS DINE-IN ORDER ===");
        
        System.out.print("Enter customer ID (or 0 for walk-in): ");
        String custId = scanner.nextLine();
        
        Customer customer = getOrCreateCustomer(custId);
        if (customer == null) return;
        
        Table table = Table.selectTable(tables, scanner);
        if (table == null) {
            System.out.println("❌ No available tables!");
            return;
        }
        
        Map<MenuItem, Integer> items = menu.selectMenuItems(scanner);
        if (items.isEmpty()) {
            System.out.println("❌ No items selected!");
            table.releaseTable();
            return;
        }
        
        Order order = cashier.processWalkInOrder(customer, items, table);
        orders.add(order);
        cashier.printReceipt(order);
        
        processPayment(order, cashier);
        
        // سؤال تحرير الطاولة
        System.out.print("\n🪑 Has customer finished? Release table now? (y/n): ");
        String release = scanner.nextLine();
        if (release.equalsIgnoreCase("y")) {
            table.releaseTable();
            System.out.println("✅ Table #" + table.getTableNumber() + " is now available!");
        }
    }
    
    private static void viewTables() {
        System.out.println("\n========== ALL TABLES ==========");
        for (Table table : tables) {
            System.out.println(table);
        }
    }
    
    private static void releaseTable() {
        System.out.println("\n========== RELEASE TABLE ==========");
        
        ArrayList<Table> occupied = new ArrayList<>();
        for (Table table : tables) {
            if (!table.isAvailable()) {
                occupied.add(table);
            }
        }
        
        if (occupied.isEmpty()) {
            System.out.println("✅ All tables are available!");
            return;
        }
        
        System.out.println("Occupied Tables:");
        for (int i = 0; i < occupied.size(); i++) {
            System.out.println((i + 1) + ". " + occupied.get(i));
        }
        
        System.out.print("\nSelect table to release (0 to cancel): ");
        int choice = getIntInput();
        
        if (choice > 0 && choice <= occupied.size()) {
            Table table = occupied.get(choice - 1);
            table.releaseTable();
            System.out.println("✅ Table released!");
        }
    }
    
    private static void processPayment(Order order, Cashier cashier) {
        System.out.println("\n--- PAYMENT ---");
        System.out.println("Total: EGP " + order.getTotal());
        System.out.println("Select payment method:");
        System.out.println("1. Cash");
        System.out.println("2. Credit Card");
        System.out.println("3. Debit Card");
        System.out.println("4. Mobile Wallet");
        System.out.print("Choice: ");
        
        int choice = getIntInput();
        
        Payment.PaymentMethod method;
        switch (choice) {
            case 1 -> method = Payment.PaymentMethod.CASH;
            case 2 -> method = Payment.PaymentMethod.CREDIT_CARD;
            case 3 -> method = Payment.PaymentMethod.DEBIT_CARD;
            case 4 -> method = Payment.PaymentMethod.MOBILE_WALLET;
            default -> method = Payment.PaymentMethod.CASH;
        }
        
        System.out.print("Enter payment amount: EGP ");
        double amount = getDoubleInput();
        
        cashier.acceptPayment(order, amount, method);
    }
    
    // ==================== HELPER METHODS ====================
    
    private static Customer getOrCreateCustomer(String custId) {
        if (custId.equals("0")) {
            System.out.print("Customer name: ");
            String name = scanner.nextLine();
            Address tempAddr = new Address(0, "Walk-in", true);
            return new Customer("guest", "guest", false, tempAddr, name, "n/a", "n/a");
        } else {
            return Customer.findCustomer(custId, customers);
        }
    }
    
    // Get ArrayLists (for Customer class)
    public static Menu getMenu() {
        return menu;
    }
    
    public static ArrayList<Table> getTables() {
        return tables;
    }
    
    public static ArrayList<Order> getOrders() {
        return orders;
    }
    
    private static int getIntInput() {
        while (true) {
            try {
                int value = scanner.nextInt();
                scanner.nextLine();
                return value;
            } catch (InputMismatchException e) {
                System.out.print("❌ Invalid input! Enter a number: ");
                scanner.nextLine();
            }
        }
    }
    
    private static double getDoubleInput() {
        while (true) {
            try {
                double value = scanner.nextDouble();
                scanner.nextLine();
                return value;
            } catch (InputMismatchException e) {
                System.out.print("❌ Invalid input! Enter a number: ");
                scanner.nextLine();
            }
        }
    }
}
