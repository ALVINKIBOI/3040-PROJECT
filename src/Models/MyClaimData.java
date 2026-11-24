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
public record MyClaimData(
        int claimId,
        int foundItemId,
        String itemTitle,
        LocalDateTime claimDate,
        String status,
        String proofDescription,
        String decisionNote,
        LocalDateTime reviewedAt
        ) {

}
