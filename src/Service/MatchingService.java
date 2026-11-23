/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Service;

import DAO.MatchingDAO;
import Models.MatchableItem;
import Models.MatchSuggestion;

import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author kiboi
 */
public class MatchingService {

    public static final double MINIMUM_MATCH_SCORE = 55.0;

    private final MatchingDAO dao;
    private final MatchingStrategy strategy;

    public MatchingService() {
        this(
                new MatchingDAO(),
                new WeightedMatchingStrategy()
        );
    }

    // Constructor injection for swapping strategies.
    public MatchingService(
            MatchingDAO dao,
            MatchingStrategy strategy) {

        this.dao = dao;
        this.strategy = strategy;
    }

    public int matchReport(int itemId)
            throws SQLException {

        MatchableItem item = dao.getItem(itemId);

        if (item == null) {
            return 0;
        }

        List<MatchableItem> candidates
                = dao.getCandidates(item);

        int savedMatches = 0;

        for (MatchableItem candidate : candidates) {

            MatchableItem lost
                    = "LOST".equals(item.itemType())
                    ? item : candidate;

            MatchableItem found
                    = "FOUND".equals(item.itemType())
                    ? item : candidate;

            double score
                    = strategy.calculateScore(lost, found);

            if (score >= MINIMUM_MATCH_SCORE) {

                boolean active = dao.saveMatch(
                        lost.itemId(),
                        found.itemId(),
                        score
                );

                if (active) {
                    savedMatches++;
                }
            }
        }

        return savedMatches;
    }

    public void refreshMatchesForUser(int userId)
            throws SQLException {

        if (userId <= 0) {
            throw new IllegalArgumentException(
                    "Login is required."
            );
        }

        List<Integer> lostItemIds
                = dao.getMyLostItemIds(userId);

        for (int itemId : lostItemIds) {
            matchReport(itemId);
        }
    }

    public List<MatchSuggestion> getSuggestions(
            int userId) throws SQLException {

        if (userId <= 0) {
            throw new IllegalArgumentException(
                    "Login is required."
            );
        }

        return dao.getSuggestions(userId);
    }

}
