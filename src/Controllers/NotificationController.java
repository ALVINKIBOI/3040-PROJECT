/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controllers;

import Models.NotificationData;
import Service.NotificationService;

import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author kiboi
 */
public class NotificationController {

    private final NotificationService service
            = new NotificationService();

    public List<NotificationData> getNotifications(
            int userId) throws SQLException {

        return service.getNotifications(userId);
    }

    public int countUnread(int userId)
            throws SQLException {

        return service.countUnread(userId);
    }

    public boolean markAsRead(
            int notificationId,
            int userId) throws SQLException {

        return service.markAsRead(
                notificationId, userId
        );
    }

    public int markAllAsRead(int userId)
            throws SQLException {

        return service.markAllAsRead(userId);
    }

}
