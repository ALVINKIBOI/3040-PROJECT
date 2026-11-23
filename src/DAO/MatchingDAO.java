/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Database.DataBaseConnection;
import Models.MatchableItem;
import Models.MatchSuggestion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author kiboi
 */
public class MatchingDAO {

    private MatchableItem mapItem(ResultSet rs)
            throws SQLException {

        Date date = rs.getDate("date_occurred");

        return new MatchableItem(
                rs.getInt("item_id"),
                rs.getInt("user_id"),
                rs.getString("item_type"),
                rs.getInt("category_id"),
                rs.getInt("location_id"),
                rs.getString("title"),
                rs.getString("description"),
                date == null ? null : date.toLocalDate(),
                rs.getString("status")
        );
    }

    public MatchableItem getItem(int itemId)
            throws SQLException {

        String sql = """
            SELECT
                item_id, user_id, item_type,
                category_id, location_id,
                title, description,
                date_occurred, status
            FROM items
            WHERE item_id = ?
              AND is_deleted = FALSE
              AND status IN ('OPEN', 'MATCHED')
            """;

        try (Connection conn
                = DataBaseConnection.getConnection(); PreparedStatement ps
                = conn.prepareStatement(sql)) {

            ps.setInt(1, itemId);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next()
                        ? mapItem(rs)
                        : null;
            }
        }
    }

    public List<MatchableItem> getCandidates(
            MatchableItem item)
            throws SQLException {

        String oppositeType
                = "LOST".equals(item.itemType())
                ? "FOUND"
                : "LOST";

        String sql = """
            SELECT
                item_id, user_id, item_type,
                category_id, location_id,
                title, description,
                date_occurred, status
            FROM items
            WHERE item_type = ?
              AND category_id = ?
              AND user_id <> ?
              AND is_deleted = FALSE
              AND status IN ('OPEN', 'MATCHED')
            """;

        List<MatchableItem> candidates
                = new ArrayList<>();

        try (Connection conn
                = DataBaseConnection.getConnection(); PreparedStatement ps
                = conn.prepareStatement(sql)) {

            ps.setString(1, oppositeType);
            ps.setInt(2, item.categoryId());
            ps.setInt(3, item.userId());

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    candidates.add(mapItem(rs));
                }
            }
        }

        return candidates;
    }

    public boolean saveMatch(
            int lostId,
            int foundId,
            double score)
            throws SQLException {

        String insertSql = """
            INSERT IGNORE INTO matches (
                lost_item_id,
                found_item_id,
                score,
                created_at,
                is_dismissed
            )
            VALUES (?, ?, ?, CURRENT_TIMESTAMP, FALSE)
            """;

        String updateSql = """
            UPDATE matches
            SET score = ?
            WHERE lost_item_id = ?
              AND found_item_id = ?
              AND is_dismissed = FALSE
            """;

        String checkSql = """
            SELECT is_dismissed
            FROM matches
            WHERE lost_item_id = ?
              AND found_item_id = ?
            """;

        try (Connection conn
                = DataBaseConnection.getConnection()) {

            conn.setAutoCommit(false);

            try {
                try (PreparedStatement ps
                        = conn.prepareStatement(insertSql)) {

                    ps.setInt(1, lostId);
                    ps.setInt(2, foundId);
                    ps.setDouble(3, score);
                    ps.executeUpdate();
                }

                try (PreparedStatement ps
                        = conn.prepareStatement(updateSql)) {

                    ps.setDouble(1, score);
                    ps.setInt(2, lostId);
                    ps.setInt(3, foundId);
                    ps.executeUpdate();
                }

                boolean active = false;

                try (PreparedStatement ps
                        = conn.prepareStatement(checkSql)) {

                    ps.setInt(1, lostId);
                    ps.setInt(2, foundId);

                    try (ResultSet rs = ps.executeQuery()) {
                        active = rs.next()
                                && !rs.getBoolean("is_dismissed");
                    }
                }

                if (active) {
                    markItemMatched(conn, lostId);
                    markItemMatched(conn, foundId);
                }

                conn.commit();
                return active;

            } catch (SQLException ex) {
                conn.rollback();
                throw ex;

            } finally {
                conn.setAutoCommit(true);
            }
        }
    }

    private void markItemMatched(
            Connection conn,
            int itemId)
            throws SQLException {

        String sql = """
            UPDATE items
            SET status = 'MATCHED'
            WHERE item_id = ?
              AND status = 'OPEN'
              AND is_deleted = FALSE
            """;

        try (PreparedStatement ps
                = conn.prepareStatement(sql)) {

            ps.setInt(1, itemId);
            ps.executeUpdate();
        }
    }

    public List<Integer> getMyLostItemIds(
            int userId) throws SQLException {

        String sql = """
            SELECT item_id
            FROM items
            WHERE user_id = ?
              AND item_type = 'LOST'
              AND is_deleted = FALSE
              AND status IN ('OPEN', 'MATCHED')
            """;

        List<Integer> result = new ArrayList<>();

        try (Connection conn
                = DataBaseConnection.getConnection(); PreparedStatement ps
                = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.add(rs.getInt("item_id"));
                }
            }
        }

        return result;
    }

    public List<MatchSuggestion> getSuggestions(
            int userId) throws SQLException {

        String sql = """
            SELECT
                m.match_id,
                lost.item_id AS lost_id,
                found.item_id AS found_id,
                lost.title AS lost_title,
                found.title AS found_title,
                c.name AS category_name,
                l.name AS location_name,
                found.date_occurred AS found_date,
                m.score,
                found.status AS found_status
            FROM matches m
            JOIN items lost
                ON m.lost_item_id = lost.item_id
            JOIN items found
                ON m.found_item_id = found.item_id
            JOIN categories c
                ON found.category_id = c.category_id
            JOIN locations l
                ON found.location_id = l.location_id
            WHERE lost.user_id = ?
              AND lost.item_type = 'LOST'
              AND found.item_type = 'FOUND'
              AND lost.user_id <> found.user_id
              AND lost.is_deleted = FALSE
              AND found.is_deleted = FALSE
              AND lost.status IN ('OPEN', 'MATCHED')
              AND found.status IN ('OPEN', 'MATCHED')
              AND m.is_dismissed = FALSE
            ORDER BY m.score DESC, m.created_at DESC
            """;

        List<MatchSuggestion> result
                = new ArrayList<>();

        try (Connection conn
                = DataBaseConnection.getConnection(); PreparedStatement ps
                = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    Date date
                            = rs.getDate("found_date");

                    result.add(new MatchSuggestion(
                            rs.getInt("match_id"),
                            rs.getInt("lost_id"),
                            rs.getInt("found_id"),
                            rs.getString("lost_title"),
                            rs.getString("found_title"),
                            rs.getString("category_name"),
                            rs.getString("location_name"),
                            date == null
                                    ? ""
                                    : date.toString(),
                            rs.getDouble("score"),
                            rs.getString("found_status")
                    ));
                }
            }
        }

        return result;
    }

}
