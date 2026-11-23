/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Models;

import java.time.LocalDateTime;

/**
 *
 * @author kiboi
 */
public record PendingClaimData(
        int claimId,
        int foundItemId,
        String itemTitle,
        String claimantName,
        String claimantEmail,
        LocalDateTime claimDate,
        boolean answerVerified,
        String proofDescription,
        String status
        ) {

}
