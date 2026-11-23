/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Models;
import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDate;


/**
 *
 * @author kiboi
 */
public record ItemReport(
        String itemType,
        int userId,
        int categoryId,
        int locationId,
        String title,
        String description,
        LocalDate dateOccurred,
        File photo,
        String distinguishingFeatures,
        BigDecimal rewardOffered,
        String storageLocation,
        String verificationQuestion
) {
}
