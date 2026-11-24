/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Models;

/**
 *
 * @author kiboi
 */
public record ManagedUser(
        int userId,
        String fullName,
        String usiuId,
        String email,
        String role,
        boolean active
        ) {

}
