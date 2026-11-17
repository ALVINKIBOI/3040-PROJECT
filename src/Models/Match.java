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
public class Match {
    private int matchID;
    private LostItem lostItem;
    private FoundItem foundItem;
    private double score;
    private LocalDateTime createdAt;
    private boolean isDismissed;

    public Match(int matchID, LostItem lostItem, FoundItem foundItem, double score, LocalDateTime createdAt, boolean isDismissed) {
        if (lostItem == null || foundItem == null) {
            throw  new IllegalArgumentException("Both items are required");
        }
        
        if (score < 0 || score > 100 || Double.isNaN(score)) {
            throw  new IllegalArgumentException("Score must be between 0 and 100");
        }

        this.matchID = matchID;
        this.lostItem = lostItem;
        this.foundItem = foundItem;
        this.score = score;
        this.createdAt = createdAt;
        this.isDismissed = isDismissed;
    }

    public int getMatchID() {
        return matchID;
    }

    public LostItem getLostItem() {
        return lostItem;
    }

    public FoundItem getFoundItem() {
        return foundItem;
    }

    public double getScore() {
        return score;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public boolean isDismissed(){
        return  isDismissed;
    }
    
    public void dismiss(){
        this.isDismissed = true;
    }
    
    @Override
    public String toString() {
        return "Match #" + matchID
                + ": " + lostItem.getTitle()
                + " / " + foundItem.getTitle()
                + " (" + score + "%)";
    }
    
    
    
}
