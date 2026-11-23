/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Service;

import DAO.AdminClaimDAO;
import Models.PendingClaimData;

import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author kiboi
 */
public class AdminClaimService {

    private final AdminClaimDAO dao
            = new AdminClaimDAO();

    public List<PendingClaimData> getPendingClaims(
            int adminId) throws SQLException {

        if (adminId <= 0) {
            throw new SecurityException(
                    "Administrator login required."
            );
        }

        return dao.getPendingClaims(adminId);
    }

    public void reviewClaim(
            int adminId,
            int claimId,
            boolean approve,
            String decisionNote) throws SQLException {

        if (adminId <= 0 || claimId <= 0) {
            throw new IllegalArgumentException(
                    "Invalid administrator or claim."
            );
        }

        String note = decisionNote == null
                ? ""
                : decisionNote.trim();

        if (!approve && note.isEmpty()) {
            throw new IllegalArgumentException(
                    "Please explain why the claim was rejected."
            );
        }

        if (note.length() > 1000) {
            throw new IllegalArgumentException(
                    "Decision note must not exceed 1000 characters."
            );
        }

        dao.reviewClaim(
                adminId,
                claimId,
                approve,
                note
        );
    }
}
