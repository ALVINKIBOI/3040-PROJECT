/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Database.DataBaseConnection;
import java.sql.*;
import Models.DashboardStats;

/**
 *
 * @author kiboi
 */
public class DashboardDAO {

    public DashboardStats getUserStats(int userId)
            throws SQLException {

        String myReports = """
            SELECT COUNT(*)
            FROM items
            WHERE user_id = ?
              AND is_deleted = FALSE
            """;

        String openReports = """
            SELECT COUNT(*)
            FROM items
            WHERE user_id = ?
              AND status = 'OPEN'
              AND is_deleted = FALSE
            """;

        String suggestedMatches = """
            SELECT COUNT(*)
            FROM matches m
            JOIN items l
              ON m.lost_item_id = l.item_id
            JOIN items f
              ON m.found_item_id = f.item_id
            WHERE l.user_id = ?
              AND l.item_type = 'LOST'
              AND f.item_type = 'FOUND'
              AND l.is_deleted = FALSE
              AND f.is_deleted = FALSE
              AND m.is_dismissed = FALSE
              AND l.status IN ('OPEN', 'MATCHED')
              AND f.status IN ('OPEN', 'MATCHED')
            """;

        String pendingClaims = """
            SELECT COUNT(*)
            FROM claims
            WHERE claimant_id = ?
              AND status = 'PENDING'
            """;

        try (Connection conn
                = DataBaseConnection.getConnection()) {

            return new DashboardStats(
                    count(conn, myReports, userId),
                    count(conn, openReports, userId),
                    count(conn, suggestedMatches, userId),
                    count(conn, pendingClaims, userId)
            );
        }
    }

    public DashboardStats getAdminStats()
            throws SQLException {

        String totalReports = """
            SELECT COUNT(*)
            FROM items
            WHERE is_deleted = FALSE
            """;

        String openReports = """
            SELECT COUNT(*)
            FROM items
            WHERE status = 'OPEN'
              AND is_deleted = FALSE
            """;

        String pendingClaims = """
            SELECT COUNT(*)
            FROM claims
            WHERE status = 'PENDING'
            """;

        String returnedItems = """
            SELECT COUNT(*)
            FROM items
            WHERE status = 'RETURNED'
              AND is_deleted = FALSE
            """;

        try (Connection conn
                = DataBaseConnection.getConnection()) {

            return new DashboardStats(
                    count(conn, totalReports),
                    count(conn, openReports),
                    count(conn, pendingClaims),
                    count(conn, returnedItems)
            );
        }
    }

    private long count(
            Connection conn,
            String sql,
            int... parameters)
            throws SQLException {

        try (PreparedStatement ps
                = conn.prepareStatement(sql)) {

            for (int i = 0; i < parameters.length; i++) {
                ps.setInt(i + 1, parameters[i]);
            }

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return rs.getLong(1);
                }

                return 0;
            }
        }
    }

}
