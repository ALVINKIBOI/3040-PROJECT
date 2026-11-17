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
public class Notification {
    private int notificationID;
    private User recipient;
    private String message;
    private String type;
    private LocalDateTime createdAt;
    private boolean isRead;
    
    public Notification(int notificationID, User recipient, String message, String type){
        if (recipient == null) {
            throw new IllegalArgumentException("Notification receipient is required");
        }
        
        this.notificationID = notificationID;
        this.recipient = recipient;
        this.message = message;
        this.type = type;
        this.createdAt = LocalDateTime.now();
        this.isRead = false;
    }

    public int getNotificationID() {
        return notificationID;
    }

    public User getRecipient() {
        return recipient;
    }

    public String getMessage() {
        return message;
    }
    
    public boolean isRead(){
        return isRead;
    }
    
    public void markAsRead(){
        this.isRead = true;
    }
    
    @Override
    public String toString() {
        return (isRead ? "[READ] " : "[NEW] ")
                + message;
    }
    
}
