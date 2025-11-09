package restaurantsystem;

import java.io.Serializable;

/**
 * System Manager class - controls the operation mode of the restaurant system
 */
public class Systemmanager implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Systemmode currentMode;

    public Systemmanager(Systemmode mode) {
        this.currentMode = mode;
    }

    public Systemmode getCurrentMode() {
        return currentMode;
    }

    public void setCurrentMode(Systemmode mode) {
        this.currentMode = mode;
        System.out.println("✅ System mode changed to: " + mode);
    }

    /**
     * Direct system to Online Delivery mode
     */
    public void directToOnline() {
        currentMode = Systemmode.ONLINE_DELIVERY;
        System.out.println("🌐 System set to ONLINE DELIVERY mode.\n");
    }

    /**
     * Direct system to Takeaway mode
     */
    public void directToTakeaway() {
        currentMode = Systemmode.TAKEAWAY;
        System.out.println("🛍️ System set to TAKEAWAY mode.\n");
    }

    /**
     * Direct system to Dine-In mode
     */
    public void directToDineIn() {
        currentMode = Systemmode.DINE_IN;
        System.out.println("🍽️ System set to DINE-IN mode.\n");
    }

    /**
     * Get current mode description
     */
    public String getModeDescription() {
        switch (currentMode) {
            case ONLINE_DELIVERY:
                return "🌐 Online Delivery - Customers order from home for delivery";
            case TAKEAWAY:
                return "🛍️ Takeaway - Customers order for pickup";
            case DINE_IN:
                return "🍽️ Dine-In - Customers order and eat in restaurant";
            default:
                return "Unknown mode";
        }
    }

    @Override
    public String toString() {
        return "System Manager [Current Mode: " + currentMode + "]";
    }
}
