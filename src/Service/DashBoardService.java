/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Service;

import DAO.DashboardDAO;
import java.sql.SQLException;
import Models.AuthUser;
import Models.DashboardStats;

/**
 *
 * @author kiboi
 */
public class DashBoardService {

    private final DashboardDAO dashboardDAO
            = new DashboardDAO();

    public DashboardStats loadStats(AuthUser user)
            throws SQLException {

        if (user == null || user.userId() <= 0) {
            throw new SecurityException(
                    "An authenticated user is required."
            );
        }

        return switch (user.role()) {

            case "ADMIN" ->
                dashboardDAO.getAdminStats();

            case "STUDENT", "STAFF" ->
                dashboardDAO.getUserStats(user.userId());

            default ->
                throw new SecurityException(
                        "Unsupported user role."
                );
        };
    }

}
