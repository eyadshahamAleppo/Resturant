package restaurantsystem;

import java.io.Serializable;

/**
 * Address class for customer delivery addresses
 */
public class Address implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private int addressId;
    private String fullAddress;
    private boolean isDefault;

    public Address(int addressId, String fullAddress, boolean isDefault) {
        this.addressId = addressId;
        this.fullAddress = fullAddress;
        this.isDefault = isDefault;
    }

    // Getters and Setters
    public int getAddressId() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }

    public String getFullAddress() {
        return fullAddress;
    }

    public void setFullAddress(String fullAddress) {
        this.fullAddress = fullAddress;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setIsDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }

    public boolean validateAddress() {
        if (fullAddress == null || fullAddress.trim().isEmpty()) {
            System.out.println("❌ Address cannot be empty!");
            return false;
        }
        if (fullAddress.length() < 10) {
            System.out.println("❌ Address too short! Please provide full address.");
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Address ID: " + addressId + 
               "\nFull Address: " + fullAddress + 
               "\nDefault: " + (isDefault ? "Yes" : "No");
    }
}
