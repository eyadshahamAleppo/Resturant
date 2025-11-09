package restaurantsystem;

import java.io.Serializable;

/**
 * MenuItem class represents a single item in the menu
 */
public class MenuItem implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String name;
    private String description;
    private double price;
    private String category;
    private boolean isAvailable;

    public MenuItem(String name, String description, double price, String category, boolean isAvailable) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
        this.isAvailable = isAvailable;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    /**
     * Get formatted info about the menu item
     */
    public String getInfo() {
        return String.format("%-20s | %-30s | EGP %.2f | %-15s | %s",
                name, description, price, category, isAvailable ? "Available" : "Not Available");
    }

    @Override
    public String toString() {
        return name + " - EGP " + price;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        MenuItem menuItem = (MenuItem) obj;
        return name.equalsIgnoreCase(menuItem.name);
    }

    @Override
    public int hashCode() {
        return name.toLowerCase().hashCode();
    }
}
