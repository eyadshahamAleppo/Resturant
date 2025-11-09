package restaurantsystem;

import java.util.*;

public class Table {
    /**
     * Table status enum
     */
    public enum TableStatus {
        AVAILABLE,
        OCCUPIED
    }
    
    private int tableNumber;
    private int capacity;
    private TableStatus status;

    public Table(int tableNumber, int capacity, TableStatus status) {
        this.tableNumber = tableNumber;
        this.capacity = capacity;
        this.status = status;
    }

    // Getters and Setters
    public int getTableNumber() {
        return tableNumber;
    }

    public void setTableNumber(int tableNumber) {
        this.tableNumber = tableNumber;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public TableStatus getStatus() {
        return status;
    }

    public void setStatus(TableStatus status) {
        this.status = status;
    }

    public boolean isAvailable() {
        return status == TableStatus.AVAILABLE;
    }

    // ==================== STATIC METHODS ====================
    
    /**
     * Select table from available tables
     */
    public static Table selectTable(ArrayList<Table> tables, Scanner scanner) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("                 🪑 AVAILABLE TABLES");
        System.out.println("=".repeat(60));
        
        ArrayList<Table> availableTables = new ArrayList<>();

        for (Table table : tables) {
            if (table.isAvailable()) {
                availableTables.add(table);
                System.out.println(availableTables.size() + ". " + table.toString());
            }
        }

        if (availableTables.isEmpty()) {
            System.out.println("❌ No available tables!");
            return null;
        }

        System.out.print("\nSelect table number: ");
        try {
            int choice = scanner.nextInt();
            scanner.nextLine();

            if (choice < 1 || choice > availableTables.size()) {
                System.out.println("❌ Invalid choice!");
                return null;
            }

            Table selected = availableTables.get(choice - 1);
            selected.assignTable();
            return selected;
        } catch (InputMismatchException e) {
            System.out.println("❌ Invalid input!");
            scanner.nextLine();
            return null;
        }
    }

    /**
     * Get all available tables
     */
    public static ArrayList<Table> getAvailableTables(ArrayList<Table> tables) {
        ArrayList<Table> available = new ArrayList<>();
        for (Table table : tables) {
            if (table.isAvailable()) {
                available.add(table);
            }
        }
        return available;
    }
    
    // ==================== INSTANCE METHODS ====================

    public boolean assignTable() {
        if (this.isAvailable()) {
            status = TableStatus.OCCUPIED;
            System.out.println("✅ Table " + tableNumber + " assigned.");
            return true;
        } else {
            System.out.println("❌ Table " + tableNumber + " is already occupied.");
            return false;
        }
    }

    public void releaseTable() {
        status = TableStatus.AVAILABLE;
        System.out.println("✅ Table " + tableNumber + " is now available.");
    }

    @Override
    public String toString() {
        return "Table #" + tableNumber +
               " | Capacity: " + capacity +
               " | Status: " + (status == TableStatus.AVAILABLE ? "✅ Available" : "❌ Occupied");
    }
}
