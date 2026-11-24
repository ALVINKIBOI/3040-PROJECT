/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Service;

import DAO.NotificationDAO;
import Models.NotificationData;

import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author kiboi
 */
public class NotificationService {

    private final NotificationDAO dao
            = new NotificationDAO();

    public List<NotificationData> getNotifications(
            int userId) throws SQLException {

        requireUser(userId);
        return dao.getNotifications(userId);
    }

    public int countUnread(int userId)
            throws SQLException {

        requireUser(userId);
        return dao.countUnread(userId);
    }

    public boolean markAsRead(
            int notificationId,
            int userId) throws SQLException {

        requireUser(userId);

        if (notificationId <= 0) {
            throw new IllegalArgumentException(
                    "Invalid notification."
            );
        }

        return dao.markAsRead(notificationId, userId);
    }

    public int markAllAsRead(int userId)
            throws SQLException {

        requireUser(userId);
        return dao.markAllAsRead(userId);
    }

    private void requireUser(int userId) {

        if (userId <= 0) {
            throw new IllegalArgumentException(
                    "Login is required."
            );
        }
    }
}
