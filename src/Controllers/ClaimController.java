/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controllers;

import Models.ClaimItemDetails;
import Service.ClaimService;

/**
 *
 * @author kiboi
 */
public class ClaimController {

    private final ClaimService service
            = new ClaimService();

    public ClaimItemDetails getClaimItem(int itemId)
            throws Exception {

        return service.getClaimItem(itemId);
    }

    public int submitClaim(
            int userId,
            int itemId,
            char[] answer,
            String proof)
            throws Exception {

        return service.submitClaim(
                userId, itemId, answer, proof
        );
    }

}
