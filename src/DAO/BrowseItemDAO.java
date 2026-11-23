/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;
import Database.DataBaseConnection;
import Models.BrowseItemData;
import Models.ItemDetailsData;
import Models.ItemSearchFilter;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author kiboi
 */
public class BrowseItemDAO {
    
    public List<BrowseItemData> searchItems(
            ItemSearchFilter filter)
            throws SQLException {

        String sql = """
            SELECT
                i.item_id,
                i.title,
                i.item_type,
                c.name AS category_name,
                l.name AS location_name,
                i.date_occurred,
                i.status
            FROM items i
            JOIN categories c
                ON i.category_id = c.category_id
            JOIN locations l
                ON i.location_id = l.location_id
            WHERE i.is_deleted = FALSE
              AND (? = '' OR
                   LOWER(i.title) LIKE ? OR
                   LOWER(i.description) LIKE ?)
              AND (? IS NULL OR i.category_id = ?)
              AND (? IS NULL OR i.location_id = ?)
              AND (? IS NULL OR i.item_type = ?)
              AND (? IS NULL OR i.status = ?)
            ORDER BY i.date_reported DESC
            LIMIT 500
            """;

        List<BrowseItemData> items = new ArrayList<>();

        try (Connection conn =
                    DataBaseConnection.getConnection();
             PreparedStatement ps =
                    conn.prepareStatement(sql)) {

            String keyword = filter.keyword() == null
                    ? ""
                    : filter.keyword().trim()
                        .toLowerCase();

            String search = "%"
                    + keyword.replace("\\", "\\\\")
                             .replace("%", "\\%")
                             .replace("_", "\\_")
                    + "%";

            ps.setString(1, keyword);
            ps.setString(2, search);
            ps.setString(3, search);

            setNullableInt(ps, 4, filter.categoryId());
            setNullableInt(ps, 5, filter.categoryId());

            setNullableInt(ps, 6, filter.locationId());
            setNullableInt(ps, 7, filter.locationId());

            ps.setString(8, filter.itemType());
            ps.setString(9, filter.itemType());

            ps.setString(10, filter.status());
            ps.setString(11, filter.status());

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    items.add(new BrowseItemData(
                        rs.getInt("item_id"),
                        rs.getString("title"),
                        rs.getString("item_type"),
                        rs.getString("category_name"),
                        rs.getString("location_name"),
                        rs.getDate("date_occurred")
                            .toLocalDate(),
                        rs.getString("status")
                    ));
                }
            }
        }

        return items;
    }

    public ItemDetailsData getItemDetails(int itemId)
            throws SQLException {

        String sql = """
            SELECT
                i.item_id,
                i.title,
                i.item_type,
                i.description,
                c.name AS category_name,
                l.name AS location_name,
                i.date_occurred,
                i.status,
                i.photo_path,
                NULL AS distinguishing_features,
                i.storage_location
            FROM items i
            JOIN categories c
                ON i.category_id = c.category_id
            JOIN locations l
                ON i.location_id = l.location_id
            WHERE i.item_id = ?
              AND i.is_deleted = FALSE
            """;

        try (Connection conn =
                    DataBaseConnection.getConnection();
             PreparedStatement ps =
                    conn.prepareStatement(sql)) {

            ps.setInt(1, itemId);

            try (ResultSet rs = ps.executeQuery()) {

                if (!rs.next()) {
                    return null;
                }

                return new ItemDetailsData(
                    rs.getInt("item_id"),
                    rs.getString("title"),
                    rs.getString("item_type"),
                    rs.getString("description"),
                    rs.getString("category_name"),
                    rs.getString("location_name"),
                    rs.getDate("date_occurred")
                        .toLocalDate(),
                    rs.getString("status"),
                    rs.getString("photo_path"),
                    rs.getString("distinguishing_features"),
                    rs.getString("storage_location")
                );
            }
        }
    }

    private void setNullableInt(
            PreparedStatement ps,
            int index,
            Integer value)
            throws SQLException {

        if (value == null) {
            ps.setNull(index, Types.INTEGER);
        } else {
            ps.setInt(index, value);
        }
    }
}
