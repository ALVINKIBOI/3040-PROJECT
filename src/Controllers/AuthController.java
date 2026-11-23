/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controllers;

import java.security.GeneralSecurityException;
import java.sql.SQLException;
import Models.AuthUser;
import Models.RegistrationData;
import Service.UserService;

/**
 *
 * @author kiboi
 */
public class AuthController {

    private final UserService userService
            = new UserService();

    public void register(
            RegistrationData data,
            char[] password)
            throws SQLException, GeneralSecurityException {

        userService.register(data, password);
    }

    public AuthUser login(
            String email,
            char[] password)
            throws SQLException, GeneralSecurityException {

        return userService.login(email, password);
    }
}
