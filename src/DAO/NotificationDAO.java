/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Database.DataBaseConnection;
import Models.NotificationData;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author kiboi
 */
public class NotificationDAO {

    public void insertNotification(
            Connection conn,
            int recipientId,
            String message,
            String type) throws SQLException {

        String sql = """
            INSERT INTO notifications (
                recipient_id,
                message,
                type,
                created_at,
                is_read
            )
            VALUES (?, ?, ?, CURRENT_TIMESTAMP, FALSE)
            """;

        try (PreparedStatement ps
                = conn.prepareStatement(sql)) {

            ps.setInt(1, recipientId);
            ps.setString(2, message);
            ps.setString(3, type);

            ps.executeUpdate();
        }
    }

    public List<NotificationData> getNotifications(
            int userId) throws SQLException {

        String sql = """
            SELECT
                notification_id,
                message,
                type,
                created_at,
                is_read
            FROM notifications
            WHERE recipient_id = ?
            ORDER BY created_at DESC,
                     notification_id DESC
            """;

        List<NotificationData> result
                = new ArrayList<>();

        try (Connection conn
                = DataBaseConnection.getConnection(); PreparedStatement ps
                = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    Timestamp created
                            = rs.getTimestamp("created_at");

                    result.add(new NotificationData(
                            rs.getInt("notification_id"),
                            rs.getString("message"),
                            rs.getString("type"),
                            created == null
                                    ? null
                                    : created.toLocalDateTime(),
                            rs.getBoolean("is_read")
                    ));
                }
            }
        }

        return result;
    }

    public int countUnread(int userId)
            throws SQLException {

        String sql = """
            SELECT COUNT(*)
            FROM notifications
            WHERE recipient_id = ?
              AND is_read = FALSE
            """;

        try (Connection conn
                = DataBaseConnection.getConnection(); PreparedStatement ps
                = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        }
    }

    public boolean markAsRead(
            int notificationId,
            int userId) throws SQLException {

        String sql = """
            UPDATE notifications
            SET is_read = TRUE
            WHERE notification_id = ?
              AND recipient_id = ?
              AND is_read = FALSE
            """;

        try (Connection conn
                = DataBaseConnection.getConnection(); PreparedStatement ps
                = conn.prepareStatement(sql)) {

            ps.setInt(1, notificationId);
            ps.setInt(2, userId);

            return ps.executeUpdate() == 1;
        }
    }

    public int markAllAsRead(int userId)
            throws SQLException {

        String sql = """
            UPDATE notifications
            SET is_read = TRUE
            WHERE recipient_id = ?
              AND is_read = FALSE
            """;

        try (Connection conn
                = DataBaseConnection.getConnection(); PreparedStatement ps
                = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            return ps.executeUpdate();
        }
    }
}
