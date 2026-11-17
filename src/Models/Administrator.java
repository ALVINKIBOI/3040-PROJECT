/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Models;

/**
 *
 * @author kiboi
 */
public class Administrator extends User{
    private String adminLevel;
    private String officeName;

    public Administrator() {
        super();
    }

    public Administrator(int userID,  String fullName, String usiuID, String email, String passwordHash,  String phone, boolean isActive,String adminLevel, String officeName) {
        super(userID, fullName, email, passwordHash, usiuID, phone, isActive);
        this.adminLevel = adminLevel;
        this.officeName = officeName;
    }
    
    //Getters and Setters

    public String getAdminLevel() {
        return adminLevel;
    }

    public void setAdminLevel(String adminLevel) {
        this.adminLevel = adminLevel;
    }

    public String getOfficeName() {
        return officeName;
    }

    public void setOfficeName(String officeName) {
        this.officeName = officeName;
    }
    
    @Override
    public String getRole(){
        return "ADMIN";
    }
    
    //Administrator operations
    public void approveClaim(){
        System.out.println("Administrator is approving a claim");
        
    }
    
    public void rejectClaim(){
       System.out.println("Administrator is rejecting a claim");
        
    }
    
    public void manageUsers(){
       System.out.println("Administrator is managing user accounts");
        
    }
    
    public void generateReport(){
       System.out.println("Administrator is generating a report");
        
    }
    
    @Override
    public String toString() {
        return super.toString() + "Administrator{" + "adminLevel=" + adminLevel + ", officeName=" + officeName + '}';
    }
    
    
    
    
}
