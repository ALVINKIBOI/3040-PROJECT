/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controllers;

import Models.MatchSuggestion;
import Service.MatchingService;

import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author kiboi
 */
public class MatchingController {

    private final MatchingService service
            = new MatchingService();

    public int matchReport(int itemId)
            throws SQLException {

        return service.matchReport(itemId);
    }

    public void refreshMatchesForUser(int userId)
            throws SQLException {

        service.refreshMatchesForUser(userId);
    }

    public List<MatchSuggestion> getSuggestions(
            int userId) throws SQLException {

        return service.getSuggestions(userId);
    }
}
