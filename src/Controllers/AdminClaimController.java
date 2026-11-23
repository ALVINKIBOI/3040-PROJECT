/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controllers;

import Models.PendingClaimData;
import Service.AdminClaimService;

import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author kiboi
 */
public class AdminClaimController {

    private final AdminClaimService service
            = new AdminClaimService();

    public List<PendingClaimData> getPendingClaims(
            int adminId) throws SQLException {

        return service.getPendingClaims(adminId);
    }

    public void reviewClaim(
            int adminId,
            int claimId,
            boolean approve,
            String decisionNote) throws SQLException {

        service.reviewClaim(
                adminId, claimId, approve, decisionNote
        );
    }
}
