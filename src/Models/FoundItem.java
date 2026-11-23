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
public class FoundItem extends Item {

    private User finder;
    private String storageLocation;
    private String verificationQuestion;
    private String verificationAnswer;

    public FoundItem(int itemID, String title, String description, Category category, Location location, LocalDateTime dateReported, LocalDateTime dateOccured, String photoPath, ItemStatus status, User finder, String storageLocation, String verificationQuestion, String verificationAnswer) {
        super(itemID, title, description, category, location, dateReported, dateOccured, photoPath, status);

        this.finder = finder;
        this.storageLocation = storageLocation;
        this.verificationQuestion = verificationQuestion;
        this.verificationAnswer = verificationAnswer;
    }

    public User getFinder() {
        return finder;
    }

    public String getStorageLocation() {
        return storageLocation;
    }

    public void setStorageLocation(String storageLocation) {
        this.storageLocation = storageLocation;
    }

    public String getVerificationQuestion() {
        return verificationQuestion;
    }

    public void setVerificationQuestion(String verificationQuestion) {
        this.verificationQuestion = verificationQuestion;
    }

    //later use by the administrator claim-review service.
    public boolean verifyAnswer(String providedAnswer) {
        return providedAnswer != null && verificationAnswer != null && verificationAnswer.trim().equalsIgnoreCase(providedAnswer.trim());
    }

    @Override
    public String getSummary() {
        return "FOUND ITEM"
                + "ID: " + getItemID() + "\n"
                + "Title: " + getTitle() + "\n"
                + "Category: " + getCategory() + "\n"
                + "Location Found: " + getLocation() + "\n"
                + "Storage Location: " + storageLocation + "\n"
                + "Status: " + getStatus();
    }

}
