/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Database.DataBaseConnection;
import Models.PendingClaimData;
import DAO.NotificationDAO;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author kiboi
 */
public class AdminClaimDAO {

    public List<PendingClaimData> getPendingClaims(
            int adminId) throws SQLException {

        String sql = """
            SELECT
                c.claim_id,
                c.found_item_id,
                i.title AS item_title,
                u.full_name AS claimant_name,
                u.email AS claimant_email,
                c.claim_date,
                c.answer_verified,
                c.proof_description,
                c.status
            FROM claims c
            JOIN items i
                ON c.found_item_id = i.item_id
            JOIN users u
                ON c.claimant_id = u.user_id
            WHERE c.status = 'PENDING'
              AND i.item_type = 'FOUND'
              AND i.is_deleted = FALSE
            ORDER BY c.claim_date ASC, c.claim_id ASC
            """;

        try (Connection conn
                = DataBaseConnection.getConnection()) {

            verifyAdministrator(conn, adminId);

            List<PendingClaimData> result = new ArrayList<>();

            try (PreparedStatement ps
                    = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    result.add(new PendingClaimData(
                            rs.getInt("claim_id"),
                            rs.getInt("found_item_id"),
                            rs.getString("item_title"),
                            rs.getString("claimant_name"),
                            rs.getString("claimant_email"),
                            rs.getTimestamp("claim_date")
                                    .toLocalDateTime(),
                            rs.getBoolean("answer_verified"),
                            rs.getString("proof_description"),
                            rs.getString("status")
                    ));
                }
            }

            return result;
        }
    }

    public void reviewClaim(
            int adminId,
            int claimId,
            boolean approve,
            String decisionNote) throws SQLException {

        try (Connection conn
                = DataBaseConnection.getConnection()) {

            conn.setAutoCommit(false);

            try {
                verifyAdministrator(conn, adminId);

                // Find the item associated with this claim.
                int itemId = getClaimItemId(conn, claimId);

                // Lock item first, matching Phase 4.8's
                // item-then-claim locking order.
                lockPendingItem(conn, itemId);

                // Lock claim and ensure it is still pending.
                lockPendingClaim(conn, claimId, itemId);

                String newClaimStatus
                        = approve ? "APPROVED" : "REJECTED";

                String newItemStatus
                        = approve ? "RETURNED" : "OPEN";

                String updateClaim = """
                    UPDATE claims
                    SET status = ?,
                        reviewed_by = ?,
                        decision_note = ?,
                        reviewed_at = CURRENT_TIMESTAMP
                    WHERE claim_id = ?
                      AND found_item_id = ?
                      AND status = 'PENDING'
                    """;

                try (PreparedStatement ps
                        = conn.prepareStatement(updateClaim)) {

                    ps.setString(1, newClaimStatus);
                    ps.setInt(2, adminId);
                    ps.setString(3, decisionNote);
                    ps.setInt(4, claimId);
                    ps.setInt(5, itemId);

                    if (ps.executeUpdate() != 1) {
                        throw new SQLException(
                                "Claim is no longer pending."
                        );
                    }
                }

                String updateItem = """
                    UPDATE items
                    SET status = ?
                    WHERE item_id = ?
                      AND item_type = 'FOUND'
                      AND is_deleted = FALSE
                      AND status = 'CLAIM_PENDING'
                    """;

                try (PreparedStatement ps
                        = conn.prepareStatement(updateItem)) {

                    ps.setString(1, newItemStatus);
                    ps.setInt(2, itemId);

                    if (ps.executeUpdate() != 1) {
                        throw new SQLException(
                                "Item status could not be updated."
                        );
                    }
                }

                String findClaimantSql = """
    SELECT claimant_id
    FROM claims
    WHERE claim_id = ?
    """;

                int claimantId;

                try (PreparedStatement ps
                        = conn.prepareStatement(findClaimantSql)) {

                    ps.setInt(1, claimId);

                    try (ResultSet rs = ps.executeQuery()) {

                        if (!rs.next()) {
                            throw new SQLException(
                                    "Claimant not found."
                            );
                        }

                        claimantId = rs.getInt("claimant_id");
                    }
                }

                NotificationDAO notificationDAO
                        = new NotificationDAO();

                String notificationMessage = approve
                        ? "Your ownership claim #" + claimId
                        + " has been APPROVED. "
                        + "Contact the lost and found office "
                        + "to arrange item collection."
                        : "Your ownership claim #" + claimId
                        + " has been REJECTED. "
                        + "Open My Claims to view the decision.";

                notificationDAO.insertNotification(
                        conn,
                        claimantId,
                        notificationMessage,
                        approve ? "CLAIM_APPROVED" : "CLAIM_REJECTED"
                );

                conn.commit();

            } catch (Exception ex) {

                try {
                    conn.rollback();
                } catch (SQLException rollbackError) {
                    ex.addSuppressed(rollbackError);
                }

                if (ex instanceof SQLException sqlEx) {
                    throw sqlEx;
                }

                throw new SQLException(
                        "Claim review failed.",
                        ex
                );

            } finally {
                conn.setAutoCommit(true);
            }
        }
    }

    private void verifyAdministrator(
            Connection conn,
            int adminId) throws SQLException {

        String sql = """
            SELECT user_id
            FROM users
            WHERE user_id = ?
              AND role = 'ADMIN'
              AND is_active = TRUE
            """;

        try (PreparedStatement ps
                = conn.prepareStatement(sql)) {

            ps.setInt(1, adminId);

            try (ResultSet rs = ps.executeQuery()) {

                if (!rs.next()) {
                    throw new SQLException(
                            "Administrator access required."
                    );
                }
            }
        }
    }

    private int getClaimItemId(
            Connection conn,
            int claimId) throws SQLException {

        String sql = """
            SELECT found_item_id
            FROM claims
            WHERE claim_id = ?
            """;

        try (PreparedStatement ps
                = conn.prepareStatement(sql)) {

            ps.setInt(1, claimId);

            try (ResultSet rs = ps.executeQuery()) {

                if (!rs.next()) {
                    throw new SQLException(
                            "Claim not found."
                    );
                }

                return rs.getInt("found_item_id");
            }
        }
    }

    private void lockPendingItem(
            Connection conn,
            int itemId) throws SQLException {

        String sql = """
            SELECT item_id
            FROM items
            WHERE item_id = ?
              AND item_type = 'FOUND'
              AND is_deleted = FALSE
              AND status = 'CLAIM_PENDING'
            FOR UPDATE
            """;

        try (PreparedStatement ps
                = conn.prepareStatement(sql)) {

            ps.setInt(1, itemId);

            try (ResultSet rs = ps.executeQuery()) {

                if (!rs.next()) {
                    throw new SQLException(
                            "Item is no longer awaiting review."
                    );
                }
            }
        }
    }

    private void lockPendingClaim(
            Connection conn,
            int claimId,
            int itemId) throws SQLException {

        String sql = """
            SELECT claim_id
            FROM claims
            WHERE claim_id = ?
              AND found_item_id = ?
              AND status = 'PENDING'
            FOR UPDATE
            """;

        try (PreparedStatement ps
                = conn.prepareStatement(sql)) {

            ps.setInt(1, claimId);
            ps.setInt(2, itemId);

            try (ResultSet rs = ps.executeQuery()) {

                if (!rs.next()) {
                    throw new SQLException(
                            "This claim has already been reviewed."
                    );
                }
            }
        }
    }
}
