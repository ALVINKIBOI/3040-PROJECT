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
public record NotificationData(
        int notificationId,
        String message,
        String type,
        LocalDateTime createdAt,
        boolean isRead
        ) {

}
