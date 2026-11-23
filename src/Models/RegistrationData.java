/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Models;

/**
 *
 * @author kiboi
 */
public record RegistrationData(
        String fullName,
        String usiuID,
        String email,
        String phone,
        String role,
        String programme,
        Integer yearOfStudy,
        String department
        ) {

}
