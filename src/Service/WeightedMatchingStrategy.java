/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Service;
import Models.MatchableItem;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
/**
 *
 * @author kiboi
 */
public class WeightedMatchingStrategy implements MatchingStrategy{
    
    private static final Set<String> STOP_WORDS =
        Set.of(
            "a", "an", "the", "my", "and", "or",
            "of", "in", "on", "at", "with", "is",
            "was", "found", "lost", "item"
        );

    @Override
    public double calculateScore(
            MatchableItem lost,
            MatchableItem found) {

        if (lost == null || found == null) {
            return 0;
        }

        if (!"LOST".equals(lost.itemType())
                || !"FOUND".equals(found.itemType())) {
            return 0;
        }

        // Do not match a user's own two reports.
        if (lost.userId() == found.userId()) {
            return 0;
        }

        // Category must match for this initial strategy.
        if (lost.categoryId() != found.categoryId()) {
            return 0;
        }

        double titleScore = textSimilarity(
            lost.title(), found.title()
        );

        double descriptionScore = textSimilarity(
            lost.description(), found.description()
        );

        double categoryScore = 1.0;

        double locationScore =
            lost.locationId() == found.locationId()
                ? 1.0 : 0.0;

        double dateScore = calculateDateScore(
            lost, found
        );

        double score =
              (titleScore * 35)
            + (descriptionScore * 25)
            + (categoryScore * 20)
            + (locationScore * 10)
            + (dateScore * 10);

        return Math.round(score * 100.0) / 100.0;
    }

    private double textSimilarity(
            String first, String second) {

        Set<String> wordsA = tokenize(first);
        Set<String> wordsB = tokenize(second);

        if (wordsA.isEmpty() || wordsB.isEmpty()) {
            return 0;
        }

        Set<String> intersection =
            new HashSet<>(wordsA);

        intersection.retainAll(wordsB);

        Set<String> union =
            new HashSet<>(wordsA);

        union.addAll(wordsB);

        return (double) intersection.size()
                / union.size();
    }

    private Set<String> tokenize(String text) {

        Set<String> result = new HashSet<>();

        if (text == null || text.isBlank()) {
            return result;
        }

        String normalized = text
            .toLowerCase(Locale.ROOT)
            .replaceAll("[^a-z0-9]+", " ")
            .trim();

        if (normalized.isEmpty()) {
            return result;
        }

        Arrays.stream(normalized.split("\\s+"))
            .filter(word -> !word.isBlank())
            .filter(word -> !STOP_WORDS.contains(word))
            .forEach(result::add);

        return result;
    }

    private double calculateDateScore(
            MatchableItem lost,
            MatchableItem found) {

        if (lost.dateOccurred() == null
                || found.dateOccurred() == null) {
            return 0;
        }

        long days = Math.abs(
            ChronoUnit.DAYS.between(
                lost.dateOccurred(),
                found.dateOccurred()
            )
        );

        if (days == 0) {
            return 1.0;
        }

        if (days <= 3) {
            return 0.9;
        }

        if (days <= 7) {
            return 0.7;
        }

        if (days <= 14) {
            return 0.4;
        }

        if (days <= 30) {
            return 0.2;
        }

        return 0;
    }
}
