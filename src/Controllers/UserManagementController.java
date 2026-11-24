/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controllers;

import Models.ManagedUser;
import Service.UserManagementService;

import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author kiboi
 */
public class UserManagementController {

    private final UserManagementService service
            = new UserManagementService();

    public List<ManagedUser> search(
            int adminId, String keyword)
            throws SQLException {

        return service.search(adminId, keyword);
    }

    public void setActive(
            int adminId, int userId, boolean active)
            throws SQLException {

        service.setActive(adminId, userId, active);
    }

    public void changeRole(
            int adminId, int userId, String role)
            throws SQLException {

        service.changeRole(adminId, userId, role);
    }
}
