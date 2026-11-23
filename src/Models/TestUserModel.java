/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Models;

import java.time.LocalDateTime;

/**
 *
 * @author kiboi
 */
public class TestUserModel {

    public static void main(String[] args) {
        //Create Student
        Student student = new Student(
                1,
                "John Kamau",
                "USIU001",
                "john@usiu.ac.ke",
                "hashed_password",
                "0712345678",
                true,
                "Computer Science",
                2
        );

        //Staff
        StaffMember staff = new StaffMember(
                2,
                "Peter Otieno",
                "USIU002",
                "peter@usiu.ac.ke",
                "hashed_password",
                "0723456789",
                true,
                "Security"
        );

        //Administrator
        Administrator admin = new Administrator(
                3,
                "Mary Wanjiku",
                "USIU003",
                "mary@usiu.ac.ke",
                "hashed_password",
                "0734567890",
                true,
                "Senior Administrator",
                "Student Affairs"
        );

        User[] users = {student, staff, admin};
        System.out.println("System Users");

        for (User user : users) {
            System.out.println(user);
        }

        //Test Login and logout
        System.out.println("LOGIN TEST");

        if (student.login()) {
            System.out.println("Student Login successfull");
        }

        student.logout();
        System.out.println("Student Logged out");

        //Profile Update
        student.updateProfile("John Kamau Updated", "0798765432");

        System.out.println("UPDATED PROFILE");
        System.out.println(student);

        //Admin operations
        System.out.println("ADMIN OPERATIONS");
        admin.approveClaim();
        admin.rejectClaim();
        admin.manageUsers();
        admin.generateReport();

        System.out.println("SYSTEM USERS");
        for (User user : users) {
            System.out.println("Name: " + user.getFullName());
            System.out.println("Role: " + user.getRole());
        }

        //Create a Category
        Category category = new Category(
                1,
                "Electronics",
                "Electronic devices and accessories"
        );

        System.out.println("CATEGORY");
        System.out.println(category);

    }

}
