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
public class TestItemModel {

    public static void main(String[] args) {
        Category electronics = new Category(
                1,
                "Electronics",
                "Electronic devices and accessories"
        );

        Location library = new Location(
                1,
                "Library",
                "Main Library Building"
        );

        Location cafeteria = new Location(
                2,
                "Cafeteria",
                "Student Centre"
        );

        Student student = new Student(
                1,
                "John Kamau",
                "USIU001",
                "john@usiu.ac.ke",
                "hashedPassword",
                "0712345678",
                true,
                "Computer Science",
                2
        );

        LostItem lostItem = new LostItem(
                101,
                "Samsung Galaxy Phone",
                "Black Samsung smartphone",
                electronics,
                library,
                LocalDateTime.now(),
                LocalDate.of(2026, 11, 15).atStartOfDay(),
                null,
                ItemStatus.OPEN,
                student,
                "Small scratch on the back cover",
                new BigDecimal("2000.00")
        );

        FoundItem foundItem = new FoundItem(
                102,
                "Black Samsung Phone",
                "Samsung smartphone found on a table",
                electronics,
                cafeteria,
                LocalDateTime.now(),
                LocalDate.of(2026, 11, 16).atStartOfDay(),
                null,
                ItemStatus.OPEN,
                student,
                "Security Office",
                "What is the phone wallpaper?",
                "A football stadium"
        );

        System.out.println("LOST ITEM");
        System.out.println(lostItem.getSummary());

        System.out.println("FOUND ITEM");
        System.out.println(foundItem.getSummary());

        System.out.println("STATUS UPDATE");
        lostItem.changeStatus(ItemStatus.MATCHED);
        System.out.println("Updated Status: " + lostItem.getStatus());

        System.out.println("VERIFICATION QUESTION");
        System.out.println("Correct Answer: " + foundItem.verifyAnswer("A football stadium"));

        System.out.println("Incorrect Answer " + foundItem.verifyAnswer("A red car"));

        Item[] items = {lostItem, foundItem};

        for (Item item : items) {
            System.out.println(item.getClass().getSimpleName() + ": " + item.getTitle());
        }

    }

}
