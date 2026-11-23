/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Main;

import Views.LoginFrame;
import javax.swing.SwingUtilities;
import utils.AppTheme;

/**
 *
 * @author kiboi
 */
public class Main {

    public static void main(String[] args) {
        AppTheme.initialize();

        SwingUtilities.invokeLater(() -> {
            LoginFrame login = new LoginFrame();
            login.setVisible(true);
        });
    }

}
