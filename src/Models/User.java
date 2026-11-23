/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Models;

/**
 *
 * @author kiboi
 */
public abstract class User {

    private int userID;
    private String fullName;
    private String email;
    private String passwordHash;
    private String usiuID;
    private String phone;
    private boolean isActive;

    //current login state
    private boolean loggedIn;

    //consructor
    public User() {
        this.isActive = true;
        this.loggedIn = false;
    }

    //parametarized constructor
    public User(int userID, String fullName, String email, String passwordHash, String usiuID, String phone, boolean isActive) {
        this.userID = userID;
        this.fullName = fullName;
        this.email = email;
        this.passwordHash = passwordHash;
        this.usiuID = usiuID;
        this.phone = phone;
        this.isActive = isActive;
        this.loggedIn = false;
    }

    //getters and setters
    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getUsiuID() {
        return usiuID;
    }

    public void setUsiuID(String usiuID) {
        this.usiuID = usiuID;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;

        if (!isActive) {
            loggedIn = false;
        }
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    //login method
    public boolean login() {
        if (isActive) {
            loggedIn = true;
            return true;
        }
        return false;
    }

    //logout method
    public void logout() {
        loggedIn = false;
    }

    //Update user profile
    public void updateProfile(String fullname, String phone) {
        this.fullName = fullname;
        this.phone = phone;
    }

    //Abstract method implemented by subclass
    public abstract String getRole();

    @Override
    public String toString() {
        return "User ID: " + userID + "\nName: " + fullName + "\nUSIU ID: " + usiuID + "\nEmail: " + email + "\nPhone: " + phone + "\nRole: " + getRole() + "\nActive: " + isActive;
    }

}
