package restaurantsystem;

import java.util.*;

public class Menu {
    private Date lastUpdate;
    private ArrayList<MenuItem> items;

    public Menu() {
        this.lastUpdate = new Date();
        this.items = new ArrayList<>();
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public ArrayList<MenuItem> getMenuItems() {
        return items;
    }

    private void updateLastUpdate() {
        this.lastUpdate = new Date();
    }

    public boolean addItem(MenuItem item) {
        if (item == null) {
            System.out.println("❌ Error: Cannot add null item");
            return false;
        }

        for (MenuItem existing : items) {
            if (existing.getName().equalsIgnoreCase(item.getName())) {
                System.out.println("❌ Error: '" + item.getName() + "' already exists!");
                return false;
            }
        }

        items.add(item);
        updateLastUpdate();
        System.out.println("✅ " + item.getName() + " added to menu!");
        return true;
    }

    public boolean removeItem(String itemName) {
        if (itemName == null || itemName.trim().isEmpty()) {
            System.out.println("❌ Error: Item name cannot be empty");
            return false;
        }

        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getName().equalsIgnoreCase(itemName)) {
                items.remove(i);
                updateLastUpdate();
                System.out.println("✅ " + itemName + " removed from menu.");
                return true;
            }
        }

        System.out.println("❌ Error: '" + itemName + "' not found.");
        return false;
    }

    public ArrayList<MenuItem> searchItems(String keyword) {
        ArrayList<MenuItem> results = new ArrayList<>();

        if (keyword == null || keyword.trim().isEmpty()) {
            return results;
        }

        String lowerKeyword = keyword.toLowerCase();
        for (MenuItem item : items) {
            if (item.getName().toLowerCase().contains(lowerKeyword) ||
                item.getDescription().toLowerCase().contains(lowerKeyword)) {
                results.add(item);
            }
        }

        return results;
    }

    public ArrayList<MenuItem> getItemsByCategory(String category) {
        ArrayList<MenuItem> categoryItems = new ArrayList<>();
        for (MenuItem item : items) {
            if (item.getCategory().equalsIgnoreCase(category)) {
                categoryItems.add(item);
            }
        }
        return categoryItems;
    }

    public ArrayList<MenuItem> getAllAvailableItems() {
        ArrayList<MenuItem> availableItems = new ArrayList<>();
        for (MenuItem item : items) {
            if (item.isAvailable()) {
                availableItems.add(item);
            }
        }
        return availableItems;
    }

    public void displayMenu() {
        System.out.println("\n" + "=".repeat(90));
        System.out.println("                              🍽️  RESTAURANT MENU  🍽️");
        System.out.println("=".repeat(90));
        
        if (items.isEmpty()) {
            System.out.println("No items in menu!");
            return;
        }

        System.out.printf("%-20s | %-30s | %-10s | %-15s | %s%n", 
                         "Name", "Description", "Price", "Category", "Status");
        System.out.println("-".repeat(90));

        for (MenuItem item : items) {
            System.out.println(item.getInfo());
        }
        
        System.out.println("=".repeat(90));
    }

    /**
     * Select menu items interactively
     */
    public Map<MenuItem, Integer> selectMenuItems(Scanner scanner) {
        Map<MenuItem, Integer> selectedItems = new HashMap<>();

        System.out.println("\n" + "=".repeat(90));
        System.out.println("                              SELECT ITEMS FROM MENU");
        System.out.println("=".repeat(90));

        ArrayList<MenuItem> availableItems = getAllAvailableItems();

        if (availableItems.isEmpty()) {
            System.out.println("❌ No items available!");
            return selectedItems;
        }

        for (int i = 0; i < availableItems.size(); i++) {
            MenuItem item = availableItems.get(i);
            System.out.println((i + 1) + ". " + item.getInfo());
        }

        System.out.println("\n" + "=".repeat(90));
        System.out.println("Select items (enter 0 to finish):");

        while (true) {
            System.out.print("\nItem number: ");
            try {
                int itemNum = scanner.nextInt();
                
                if (itemNum == 0) break;

                if (itemNum < 1 || itemNum > availableItems.size()) {
                    System.out.println("❌ Invalid item number!");
                    continue;
                }

                System.out.print("Quantity: ");
                int qty = scanner.nextInt();

                if (qty <= 0) {
                    System.out.println("❌ Quantity must be > 0!");
                    continue;
                }

                MenuItem selectedItem = availableItems.get(itemNum - 1);
                selectedItems.put(selectedItem, 
                                selectedItems.getOrDefault(selectedItem, 0) + qty);

                System.out.println("✅ Added: " + selectedItem.getName() + " x" + qty);
            } catch (InputMismatchException e) {
                System.out.println("❌ Invalid input!");
                scanner.nextLine();
            }
        }

        scanner.nextLine();
        return selectedItems;
    }

    public MenuItem getItemByName(String name) {
        for (MenuItem item : items) {
            if (item.getName().equalsIgnoreCase(name)) {
                return item;
            }
        }
        return null;
    }
}
