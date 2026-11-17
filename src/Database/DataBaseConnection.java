/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 *
 * @author kiboi
 */
public class DataBaseConnection {
    //Database connection details
    private static final String URL = "JDBC:MYSQL://LOCALHOST/LOSTANDFOUNDSYSTEM";
    private static final String User = "root";
    private static final String Password = "";
    
    //Connection
    public static Connection getConnection(){
        try {
            Connection cn = DriverManager.getConnection(URL,User,Password);
            return cn;
            
            
        } catch (SQLException e) {
            System.out.println("Database Connection Failed");
            System.out.println(e.getMessage());
            
            return null;
        }
    }
}