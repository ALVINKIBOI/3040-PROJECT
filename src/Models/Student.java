/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Models;

/**
 *
 * @author kiboi
 */
public class Student extends User{
    private String programme;
    private int yearOfStudy;

    public Student() {
        super();
    }

    public Student(int userID, String fullName, String usiuID, String email, String passwordHash,  String phone, boolean isActive, String programme, int yearOfStudy) {
        super(userID, fullName, email, passwordHash, usiuID, phone, isActive);
        this.programme = programme;
        this.yearOfStudy = yearOfStudy;
    }
    
    //Getters and Setters

    public String getProgramme() {
        return programme;
    }

    public void setProgramme(String programme) {
        this.programme = programme;
    }

    public int getYearOfStudy() {
        return yearOfStudy;
    }

    public void setYearOfStudy(int yearOfStudy) {
        this.yearOfStudy = yearOfStudy;
    }
    
    //Implement parent abstract method
    @Override
    public String getRole(){
        return "STUDENT";       
    }

    @Override
    public String toString() {
        return super.toString() + "Student{" + "programme=" + programme + ", yearOfStudy=" + yearOfStudy + '}';
    }
     
}
