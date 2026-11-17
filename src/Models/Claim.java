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
public class Claim {
    private int claimID;
    private User claimant;
    private FoundItem foundItem;
    private LocalDateTime claimDate;
    private String answerProvided;
    private String proofDescription;
    private ClaimStatus status;
    private Administrator reviewedBy;
    private String decisonNote;
    private LocalDateTime reviwedAt;
    

    public Claim(int claimID,  User Claimant, FoundItem foundItem, String answerProvided, String proofDescription) {
        if (claimant == null || foundItem == null){
            throw new IllegalArgumentException("Claimant and found item are required");
        }
        
        this.claimID = claimID;
        this.claimant = claimant;
        this.foundItem = foundItem;
        this.claimDate = LocalDateTime.now();
        this.answerProvided = answerProvided;
        this.proofDescription = proofDescription;
        this.status = ClaimStatus.PENDING;
             
    }
    
    //Getters and Setters
    public int getClaimID() {
        return claimID;
    }

    public void setClaimID(int claimID) {
        this.claimID = claimID;
    }

    public User getClaimant() {
        return claimant;
    }

    public FoundItem getFoundItem() {
        return foundItem;
    }

    public LocalDateTime getClaimDate() {
        return claimDate;
    }

    public String getAnswerProvided() {
        return answerProvided;
    }

    public ClaimStatus getStatus() {
        return status;
    }

    public Administrator getReviewedBy() {
        return reviewedBy;
    }

    public String getDecisonNote() {
        return decisonNote;
    }

    public LocalDateTime getReviwedAt() {
        return reviwedAt;
    }
    
    
    public void setReviewDecison(ClaimStatus newStatus, Administrator admin, String note){
        if (status != ClaimStatus.PENDING) {
            throw new IllegalArgumentException("Only Pending claims can be reviewed");
        }
        
        if (newStatus != ClaimStatus.APPROVED && newStatus != ClaimStatus.REJECTED) {
            throw new IllegalArgumentException("Decision must be approved or rejected");     
        }
        if (admin == null){
            throw new IllegalArgumentException("An Administrator is required");
        }
        this.status = newStatus;
        this.reviewedBy = admin;
        this.decisonNote = note;
        this.reviwedAt = LocalDateTime.now();
    }
    
    @Override
     public String toString() {
        return "Claim #" + claimID
                + " - " + foundItem.getTitle()
                + " - " + status;
    }

  
    
    
    
    
    
    
    
    
    }

    
     
