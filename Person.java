package restaurantsystem;

import java.io.Serializable;

/**
 * Abstract base class for all persons in the system
 */
public abstract class Person implements Serializable {
    private static final long serialVersionUID = 1L;
    
    protected String id;
    protected String name;
    protected String email;
    protected String phoneNumber;
    protected String password;

    public Person(String name, String email, String phoneNumber, String password) {
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Login method to authenticate user
     */
    public boolean login(String inputId, String inputPassword) {
        if (this.id != null && this.id.equalsIgnoreCase(inputId) && 
            this.password != null && this.password.equals(inputPassword)) {
            System.out.println("✅ Login successful! Welcome, " + name);
            return true;
        } else {
            System.out.println("❌ Invalid ID or password.");
            return false;
        }
    }

    /**
     * Update personal information
     */
    public void updateInfo(String newName, String newEmail, String newPhone) {
        this.name = newName;
        this.email = newEmail;
        this.phoneNumber = newPhone;
        System.out.println("✅ Information updated successfully!");
    }

    /**
     * Get details of the person
     */
    public String getDetails() {
        return "ID: " + id +
               "\nName: " + name +
               "\nEmail: " + email +
               "\nPhone: " + phoneNumber;
    }

    @Override
    public String toString() {
        return getDetails();
    }
}
