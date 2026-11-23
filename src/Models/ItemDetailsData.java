/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Models;

import java.time.LocalDate;

/**
 *
 * @author kiboi
 */
public record ItemDetailsData(
        int itemId,
        String title,
        String itemType,
        String description,
        String category,
        String location,
        LocalDate dateOccurred,
        String status,
        String photoPath,
        String distinguishingFeatures,
        String storageLocation
) {
}