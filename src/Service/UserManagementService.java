/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Service;

import DAO.UserManagment;
import Models.ManagedUser;

import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author kiboi
 */
public class UserManagementService {

    private final UserManagment dao
            = new UserManagment();

    public List<ManagedUser> search(
            int adminId, String keyword)
            throws SQLException {

        if (adminId <= 0) {
            throw new IllegalArgumentException(
                    "Administrator login required."
            );
        }

        return dao.findUsers(
                adminId,
                keyword == null ? "" : keyword
        );
    }

    public void setActive(
            int adminId, int userId, boolean active)
            throws SQLException {

        dao.setActive(adminId, userId, active);
    }

    public void changeRole(
            int adminId, int userId, String role)
            throws SQLException {

        dao.updateRole(adminId, userId, role);
    }
}
