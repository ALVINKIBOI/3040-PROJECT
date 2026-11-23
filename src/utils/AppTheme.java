/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import com.formdev.flatlaf.FlatLightLaf;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.*;

/**
 *
 * @author kiboi
 */
public class AppTheme {

    public static final Color YELLOW
            = new Color(255, 255, 0);

    public static final Color PRIMARY
            = new Color(37, 99, 235);

    public static final Color TEXT
            = new Color(31, 41, 55);

    public static final Color MUTED
            = new Color(100, 116, 139);

    public static final Color BACKGROUND
            = new Color(245, 247, 251);

    public static final Color ERROR
            = new Color(220, 38, 38);

    public static void initialize() {
        FlatLightLaf.setup();

        UIManager.put("Component.arc", 14);
        UIManager.put("Button.arc", 18);
        UIManager.put("TextComponent.arc", 15);
    }

    public static void stylePrimaryButton(
            JButton button) {

        button.setBackground(PRIMARY);
        button.setForeground(Color.WHITE);

        button.setFont(
                new Font("Segoe UI", Font.BOLD, 14)
        );

        button.setFocusPainted(false);
        button.setCursor(
                new java.awt.Cursor(
                        java.awt.Cursor.HAND_CURSOR
                )
        );

        button.putClientProperty(
                "JButton.buttonType", "roundRect"
        );
    }

    public static void styleInput(
            JComponent input) {

        input.setFont(
                new Font("Segoe UI", Font.PLAIN, 14)
        );

        input.setPreferredSize(
                new Dimension(300, 45)
        );

        input.putClientProperty(
                "JComponent.roundRect", true
        );
    }

    public static void styleLinkButton(
            JButton button) {

        button.setForeground(PRIMARY);
        button.setFont(
                new Font("Segoe UI", Font.BOLD, 13)
        );

        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);

        button.setCursor(
                new java.awt.Cursor(
                        java.awt.Cursor.HAND_CURSOR
                )
        );
    }

    public static void styleComboBox(
            javax.swing.JComboBox<?> comboBox) {

        comboBox.setFont(
                new java.awt.Font(
                        "Segoe UI",
                        java.awt.Font.PLAIN,
                        15
                )
        );

        comboBox.setPreferredSize(
                new java.awt.Dimension(340, 45)
        );

        comboBox.putClientProperty(
                "JComponent.roundRect", true
        );
    }

    public static void styleNavButton(
            javax.swing.JButton button) {

        button.setFont(new java.awt.Font(
                "Segoe UI", java.awt.Font.BOLD, 14
        ));

        button.setForeground(
                java.awt.Color.BLACK
        );

        // Transparent-looking background on yellow sidebar
        button.setBackground(
                new java.awt.Color(255, 255, 0)
        );

        
        button.setBorderPainted(false);
        button.setFocusPainted(false);

        button.setHorizontalAlignment(
                javax.swing.SwingConstants.LEFT
        );

        button.setMargin(
                new java.awt.Insets(10, 16, 10, 12)
        );

        button.setCursor(new java.awt.Cursor(
                java.awt.Cursor.HAND_CURSOR
        ));
    }

    public static void setActiveNav(
            javax.swing.JButton active,
            javax.swing.JButton... buttons) {

        for (javax.swing.JButton button : buttons) {

            button.setBackground(YELLOW);
            button.setForeground(
                    java.awt.Color.BLACK
            );
        }

        active.setBackground(PRIMARY);
        active.setForeground(java.awt.Color.BLACK);
    }

    public static void styleStatCard(
            javax.swing.JPanel panel) {

        panel.setBackground(java.awt.Color.WHITE);

        panel.putClientProperty(
                "FlatLaf.style",
                "arc: 16"
        );

        panel.setBorder(
                javax.swing.BorderFactory.createEmptyBorder(
                        18, 18, 18, 18
                )
        );

    }
}
