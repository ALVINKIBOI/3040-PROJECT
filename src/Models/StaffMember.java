/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Models;

/**
 *
 * @author kiboi
 */
public class StaffMember extends User{
    private String depertment;

    public StaffMember() {
        super();
    }

    public StaffMember(int userID, String fullName, String usiuID, String email, String passwordHash,  String phone, boolean isActive, String depertment) {
        super(userID, fullName, email, passwordHash, usiuID, phone, isActive);
        this.depertment = depertment;
    }
    
    //Getters and Setters

    public String getDepertment() {
        return depertment;
    }

    public void setDepertment(String depertment) {
        this.depertment = depertment;
    }
    
    @Override
    public String getRole(){
     return "STAFF";   
    }

    @Override
    public String toString() {
        return super.toString() + "\nStaffMember" + "depertment=" + depertment + '}';
    }   
}
