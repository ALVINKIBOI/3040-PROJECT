/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Service;

import DAO.ClaimDAO;
import DAO.ClaimDAO.LockedFoundItem;
import Database.DataBaseConnection;
import Models.ClaimItemDetails;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import utils.PasswordUtils;

/**
 *
 * @author kiboi
 */
public class ClaimService {

    private final ClaimDAO dao = new ClaimDAO();

    public ClaimItemDetails getClaimItem(int itemId)
            throws SQLException {

        if (itemId <= 0) {
            throw new IllegalArgumentException(
                    "Invalid item ID."
            );
        }

        return dao.getClaimItem(itemId);
    }

    public int submitClaim(
            int claimantId,
            int foundItemId,
            char[] answer,
            String proof)
            throws Exception {

        try {
            if (claimantId <= 0 || foundItemId <= 0) {
                throw new IllegalArgumentException(
                        "A valid user and item are required."
                );
            }

            if (answer == null || answer.length == 0) {
                throw new IllegalArgumentException(
                        "Please answer the verification question."
                );
            }

            if (proof == null || proof.isBlank()) {
                throw new IllegalArgumentException(
                        "Please provide proof of ownership."
                );
            }

            if (proof.length() > 2000) {
                throw new IllegalArgumentException(
                        "Proof description is too long."
                );
            }

            try (Connection conn
                    = DataBaseConnection.getConnection()) {

                conn.setAutoCommit(false);

                try {
                    String role = dao.getActiveRole(
                            conn, claimantId
                    );

                    if (!"STUDENT".equals(role)
                            && !"STAFF".equals(role)) {
                        throw new SecurityException(
                                "Only active students and staff can claim items."
                        );
                    }

                    LockedFoundItem item
                            = dao.lockFoundItem(conn, foundItemId);

                    if (item == null) {
                        throw new IllegalArgumentException(
                                "Found item is not available."
                        );
                    }

                    if (item.finderId() == claimantId) {
                        throw new IllegalArgumentException(
                                "You cannot claim an item you reported."
                        );
                    }

                    if (!item.status().equals("OPEN")
                            && !item.status().equals("MATCHED")) {
                        throw new IllegalStateException(
                                "This item is not accepting claims."
                        );
                    }

                    if (item.verificationHash() == null
                            || !item.verificationHash()
                                    .startsWith("pbkdf2_sha256$")) {
                        throw new IllegalStateException(
                                "Verification details are unavailable."
                        );
                    }

                    boolean verified
                            = PasswordUtils.verifyPassword(
                                    answer,
                                    item.verificationHash()
                            );

                    int claimId = dao.insertClaim(
                            conn,
                            claimantId,
                            foundItemId,
                            proof.trim(),
                            verified
                    );

                    dao.markClaimPending(
                            conn, foundItemId
                    );

                    conn.commit();
                    return claimId;

                } catch (Exception ex) {
                    try {
                        conn.rollback();
                    } catch (SQLException rollbackError) {
                        ex.addSuppressed(rollbackError);
                    }
                    throw ex;

                } finally {
                    conn.setAutoCommit(true);
                }
            }

        } finally {
            if (answer != null) {
                Arrays.fill(answer, '\0');
            }
        }
    }

}
