/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Models;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 *
 * @author kiboi
 */
public class LostItem extends Item{
    private User reporter;
    private String distinguishingFeatures;
    //Avoids floting point precisons that hapen with double
    private BigDecimal rewardOffered;

    public LostItem(int ItemID, String title, String description, Category category, Location location, LocalDateTime dateReported, LocalDateTime dateOccured, String photoPath, ItemStatus status, User reporter, String distinguishingFeatures, BigDecimal rewardOffered) {
        super(ItemID, title, description, category, location, dateReported, dateOccured, photoPath, status);
        
        this.reporter = reporter;
        this.distinguishingFeatures = distinguishingFeatures;
        this.rewardOffered = rewardOffered;
        setRewardOffered(rewardOffered);
    }

    public User getReporter() {
        return reporter;
    }

    public String getDistinguishingFeatures() {
        return distinguishingFeatures;
    }

    public void setDistinguishingFeatures(String distinguishingFeatures) {
        this.distinguishingFeatures = distinguishingFeatures;
    }

    public BigDecimal getRewardOffered() {
        return rewardOffered;
    }

    public void setRewardOffered(BigDecimal rewardOffered) {
        if (rewardOffered != null && rewardOffered.signum() < 0 ) {
            throw new IllegalArgumentException("Reward is not negative");   
        }
        this.rewardOffered = rewardOffered;    
    }
    
    @Override
    public String getSummary(){
        return "LOST ITEM"
                + "ID: " + getItemID()+ "\n"
                + "Title: " + getTitle() + "\n"
                + "Category: " + getCategory() + "\n"
                + "Location: " + getLocation() + "\n"
                + "Status: " + getStatus() + "\n"
                + "Distinguishing Features: "
                + distinguishingFeatures + "\n"
                + "Reward: "
                + (rewardOffered == null
                    ? "None"
                    : "KSh " + rewardOffered);
    }
    
    
    
    
    
}
