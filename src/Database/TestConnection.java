/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Database;

import java.sql.DatabaseMetaData;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 *
 * @author kiboi
 */
public class TestConnection {
    
    public static void main(String[] args) {
        Connection cn = DataBaseConnection.getConnection();
        if (cn !=null) {
            System.out.println("DATABASE CONNECTION SUCCESSFUL!");
            
            try {
                DatabaseMetaData metaData = cn.getMetaData();
                System.out.println("Database:" + metaData.getDatabaseProductName());
                System.out.println("DataBaseVersion" + metaData.getDatabaseProductVersion());
                System.out.println("URL" + metaData.getURL());
                
                Statement st = cn.createStatement();
                ResultSet rs = st.executeQuery("SELECT DATABASE()");
                
                if (rs.next()){
                    System.out.println("DataBase Name:" + rs.getString(1));
                }
                rs.close();
                st.close();
                cn.close();
                
                System.out.println("Connection closed sucessfully");
     
            } catch (Exception e) {
                System.out.println("Test Failed: " + e.getMessage());
            }
            
        } else {
            System.out.println("Unable to cpnnect to database");
            
        }
    }
    
}
