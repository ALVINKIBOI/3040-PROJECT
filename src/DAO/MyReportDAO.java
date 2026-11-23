/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Database.DataBaseConnection;
import Models.MyReportData;

import java.sql.*;
import java.time.LocalDate;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author kiboi
 */
public class MyReportDAO {

    private static final String EDITABLE
            = "('OPEN', 'MATCHED')";

    public List<MyReportData> getMyReports(
            int userId, String keyword, String type)
            throws SQLException {

        String sql = """
            SELECT i.*, c.name AS category_name,
                   l.name AS location_name
            FROM items i
            JOIN categories c
              ON i.category_id = c.category_id
            JOIN locations l
              ON i.location_id = l.location_id
            WHERE i.user_id = ?
              AND i.is_deleted = FALSE
              AND (? = 'ALL' OR i.item_type = ?)
              AND (
                    ? = ''
                    OR LOWER(i.title) LIKE ?
                  )
            ORDER BY i.date_reported DESC, i.item_id DESC
            """;

        List<MyReportData> reports = new ArrayList<>();

        try (Connection conn
                = DataBaseConnection.getConnection(); PreparedStatement ps
                = conn.prepareStatement(sql)) {

            String search = keyword == null
                    ? "" : keyword.trim().toLowerCase();

            ps.setInt(1, userId);
            ps.setString(2, type);
            ps.setString(3, type);
            ps.setString(4, search);
            ps.setString(5, "%" + search + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    reports.add(mapReport(rs));
                }
            }
        }

        return reports;
    }

    public MyReportData getOwnedReport(
            int itemId, int userId)
            throws SQLException {

        String sql = """
            SELECT i.*, c.name AS category_name,
                   l.name AS location_name
            FROM items i
            JOIN categories c
              ON i.category_id = c.category_id
            JOIN locations l
              ON i.location_id = l.location_id
            WHERE i.item_id = ?
              AND i.user_id = ?
              AND i.is_deleted = FALSE
            """;

        try (Connection conn
                = DataBaseConnection.getConnection(); PreparedStatement ps
                = conn.prepareStatement(sql)) {

            ps.setInt(1, itemId);
            ps.setInt(2, userId);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapReport(rs) : null;
            }
        }
    }

    public boolean updateReport(
            int itemId, int userId,
            String title, String description,
            int categoryId, int locationId,
            LocalDate dateOccurred,
            String distinguishingFeatures,
            BigDecimal reward,
            String storageLocation)
            throws SQLException {

        String sql = """
            UPDATE items
            SET title = ?,
                description = ?,
                category_id = ?,
                location_id = ?,
                date_occurred = ?,
                distinguishing_features =
                    CASE WHEN item_type = 'LOST'
                         THEN ? ELSE NULL END,
                reward_offered =
                    CASE WHEN item_type = 'LOST'
                         THEN ? ELSE NULL END,
                storage_location =
                    CASE WHEN item_type = 'FOUND'
                         THEN ? ELSE NULL END
            WHERE item_id = ?
              AND user_id = ?
              AND is_deleted = FALSE
              AND status IN ('OPEN', 'MATCHED')
            """;

        try (Connection conn
                = DataBaseConnection.getConnection(); PreparedStatement ps
                = conn.prepareStatement(sql)) {

            ps.setString(1, title);
            ps.setString(2, description);
            ps.setInt(3, categoryId);
            ps.setInt(4, locationId);
            ps.setDate(5, Date.valueOf(dateOccurred));
            ps.setString(6, distinguishingFeatures);

            if (reward == null) {
                ps.setNull(7, Types.DECIMAL);
            } else {
                ps.setBigDecimal(7, reward);
            }

            ps.setString(8, storageLocation);
            ps.setInt(9, itemId);
            ps.setInt(10, userId);

            return ps.executeUpdate() == 1;
        }
    }

    public boolean softDelete(
            int itemId, int userId)
            throws SQLException {

        String sql = """
            UPDATE items
            SET is_deleted = TRUE
            WHERE item_id = ?
              AND user_id = ?
              AND is_deleted = FALSE
              AND status IN ('OPEN', 'MATCHED')
            """;

        try (Connection conn
                = DataBaseConnection.getConnection(); PreparedStatement ps
                = conn.prepareStatement(sql)) {

            ps.setInt(1, itemId);
            ps.setInt(2, userId);

            return ps.executeUpdate() == 1;
        }
    }

    private MyReportData mapReport(ResultSet rs)
            throws SQLException {

        return new MyReportData(
                rs.getInt("item_id"),
                rs.getString("title"),
                rs.getString("item_type"),
                rs.getString("description"),
                rs.getInt("category_id"),
                rs.getString("category_name"),
                rs.getInt("location_id"),
                rs.getString("location_name"),
                rs.getDate("date_occurred").toLocalDate(),
                rs.getString("status"),
                rs.getString("photo_path"),
                rs.getString("distinguishing_features"),
                rs.getBigDecimal("reward_offered"),
                rs.getString("storage_location"),
                rs.getString("verification_question")
        );
    }
}
