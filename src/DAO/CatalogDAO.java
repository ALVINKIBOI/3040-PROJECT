/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Database.DataBaseConnection;
import Models.CatalogOption;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author kiboi
 */
public class CatalogDAO {
        
    public List<CatalogOption> getCategories()
            throws SQLException {

        String sql = """
            SELECT category_id, name
            FROM categories
            ORDER BY name
            """;

        List<CatalogOption> result = new ArrayList<>();

        try (Connection conn =
                    DataBaseConnection.getConnection();
             PreparedStatement ps =
                    conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                result.add(new CatalogOption(
                    rs.getInt("category_id"),
                    rs.getString("name")
                ));
            }
        }

        return result;
    }

    public List<CatalogOption> getLocations()
            throws SQLException {

        String sql = """
            SELECT location_id, name, building
            FROM locations
            ORDER BY name
            """;

        List<CatalogOption> result = new ArrayList<>();

        try (Connection conn =
                    DataBaseConnection.getConnection();
             PreparedStatement ps =
                    conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                result.add(new CatalogOption(
                    rs.getInt("location_id"),
                    rs.getString("name") + " - "
                        + rs.getString("building")
                ));
            }
        }

        return result;
    }
    
}
