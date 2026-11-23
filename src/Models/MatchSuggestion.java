/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Models;

/**
 *
 * @author kiboi
 */
public record MatchSuggestion(
        int matchId,
        int lostItemId,
        int foundItemId,
        String lostTitle,
        String foundTitle,
        String categoryName,
        String locationName,
        String foundDate,
        double score,
        String foundStatus
        ) {

}
