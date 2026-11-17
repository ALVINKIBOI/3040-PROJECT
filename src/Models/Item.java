/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Models;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 *
 * @author kiboi
 */
public abstract class Item {
    int itemID;
    private String title;
    private String description;
    private Category category;
    private Location location;
    private LocalDateTime dateReported;
    private LocalDateTime dateOccured;
    private String photoPath;
    
    private ItemStatus status;

    //Parametarized Constructor

    public Item(int itemID, String title, String description, Category category, Location location, LocalDateTime dateReported, LocalDateTime dateOccured, String photoPath, ItemStatus status) {
        this.itemID = itemID;
        this.title = title;
        this.description = description;
        this.category = category;
        this.location = location;
        this.dateReported = dateReported;
        this.dateOccured = dateOccured;
        this.photoPath = photoPath;
        this.status = status;
    }
    
    public void updateDetails(String title, String description, Category category, Location location, LocalDate dateOccurred,String photoPath){
        this.title = title;
        this.description = description;
        this.category = category;
        this.location = location;
        this.dateOccured = dateOccured;
        this.photoPath = photoPath;
    }
    
    public void changeStatus (ItemStatus newStatus){
        if (newStatus == null) {
            throw  new IllegalArgumentException("Item Status Cannot be null.");           
        }
        
        this.status = newStatus;
    }
    
    public abstract String getSummary();

    public int getItemID() {
        return itemID;
    }

    public void setItemID(int itemID) {
        this.itemID = itemID;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Category getCategory() {
        return category;
    }

    public Location getLocation() {
        return location;
    }

    public LocalDateTime getDateReported() {
        return dateReported;
    }

    public LocalDateTime getDateOccured() {
        return dateOccured;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public ItemStatus getStatus() {
        return status;
    }
    
    @Override
    public String toString(){
        return  getSummary();
    }
   
 
}


