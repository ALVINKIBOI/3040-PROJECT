/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Database.DataBaseConnection;
import Models.ClaimItemDetails;
import java.sql.*;

/**
 *
 * @author kiboi
 */
public class ClaimDAO {

    public ClaimItemDetails getClaimItem(int itemId)
            throws SQLException {

        String sql = """
            SELECT item_id, title, status,
                   verification_question
            FROM items
            WHERE item_id = ?
              AND item_type = 'FOUND'
              AND is_deleted = FALSE
              AND status IN ('OPEN', 'MATCHED')
            """;

        try (Connection conn
                = DataBaseConnection.getConnection(); PreparedStatement ps
                = conn.prepareStatement(sql)) {

            ps.setInt(1, itemId);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }

                return new ClaimItemDetails(
                        rs.getInt("item_id"),
                        rs.getString("title"),
                        rs.getString("status"),
                        rs.getString("verification_question")
                );
            }
        }
    }

    public String getActiveRole(
            Connection conn, int userId)
            throws SQLException {

        String sql = """
            SELECT role
            FROM users
            WHERE user_id = ?
              AND is_active = TRUE
            """;

        try (PreparedStatement ps
                = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next()
                        ? rs.getString("role")
                        : null;
            }
        }
    }

    public LockedFoundItem lockFoundItem(
            Connection conn, int itemId)
            throws SQLException {

        String sql = """
            SELECT user_id, status,
                   verification_answer
            FROM items
            WHERE item_id = ?
              AND item_type = 'FOUND'
              AND is_deleted = FALSE
            FOR UPDATE
            """;

        try (PreparedStatement ps
                = conn.prepareStatement(sql)) {

            ps.setInt(1, itemId);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }

                return new LockedFoundItem(
                        rs.getInt("user_id"),
                        rs.getString("status"),
                        rs.getString("verification_answer")
                );
            }
        }
    }

    public int insertClaim(
            Connection conn,
            int claimantId,
            int foundItemId,
            String proof,
            boolean answerVerified)
            throws SQLException {

        String sql = """
            INSERT INTO claims (
                claimant_id,
                found_item_id,
                answer_provided,
                proof_description,
                answer_verified,
                status
            )
            VALUES (?, ?, ?, ?, ?, 'PENDING')
            """;

        try (PreparedStatement ps
                = conn.prepareStatement(
                        sql,
                        Statement.RETURN_GENERATED_KEYS
                )) {

                    ps.setInt(1, claimantId);
                    ps.setInt(2, foundItemId);
                    ps.setString(3, "[REDACTED]");
                    ps.setString(4, proof);
                    ps.setBoolean(5, answerVerified);

                    ps.executeUpdate();

                    try (ResultSet keys = ps.getGeneratedKeys()) {
                        if (keys.next()) {
                            return keys.getInt(1);
                        }
                    }

                    throw new SQLException(
                            "Could not retrieve the new claim ID."
                    );
                }
    }

    public void markClaimPending(
            Connection conn, int itemId)
            throws SQLException {

        String sql = """
            UPDATE items
            SET status = 'CLAIM_PENDING'
            WHERE item_id = ?
              AND item_type = 'FOUND'
              AND is_deleted = FALSE
              AND status IN ('OPEN', 'MATCHED')
            """;

        try (PreparedStatement ps
                = conn.prepareStatement(sql)) {

            ps.setInt(1, itemId);

            if (ps.executeUpdate() != 1) {
                throw new SQLException(
                        "Found item status could not be updated."
                );
            }
        }
    }

    // Internal data: never send the hash to a JFrame.
    public record LockedFoundItem(
            int finderId,
            String status,
            String verificationHash
            ) {

    }

}
