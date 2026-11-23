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
public record MatchableItem(
        int itemId,
        int userId,
        String itemType,
        int categoryId,
        int locationId,
        String title,
        String description,
        LocalDate dateOccurred,
        String status
        ) {

}
