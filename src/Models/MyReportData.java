/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Models;
import java.time.LocalDate;
import java.math.BigDecimal;

/**
 *
 * @author kiboi
 */
public record MyReportData(
        int itemId,
        String title,
        String itemType,
        String description,
        int categoryId,
        String categoryName,
        int locationId,
        String locationName,
        LocalDate dateOccurred,
        String status,
        String photoPath,
        String distinguishingFeatures,
        BigDecimal rewardOffered,
        String storageLocation,
        String verificationQuestion
) {
}