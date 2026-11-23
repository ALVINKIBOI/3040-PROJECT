/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Database.DataBaseConnection;
import Models.ItemReport;
import java.sql.*;

/**
 *
 * @author kiboi
 */
public class ItemDAO {

    public int insertReport(
            ItemReport data,
            String photoPath,
            String verificationHash)
            throws SQLException {

        String sql = """
            INSERT INTO items (
                item_type,
                user_id,
                category_id,
                location_id,
                title,
                description,
                date_occurred,
                photo_path,
                distinguishing_features,
                reward_offered,
                storage_location,
                verification_question,
                verification_answer
            )
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;

        try (Connection conn
                = DataBaseConnection.getConnection(); PreparedStatement ps
                = conn.prepareStatement(
                        sql,
                        Statement.RETURN_GENERATED_KEYS
                )) {

            ps.setString(1, data.itemType());
            ps.setInt(2, data.userId());
            ps.setInt(3, data.categoryId());
            ps.setInt(4, data.locationId());
            ps.setString(5, data.title());
            ps.setString(6, data.description());

            ps.setDate(
                    7,
                    Date.valueOf(data.dateOccurred())
            );

            if (photoPath == null) {
                ps.setNull(8, Types.VARCHAR);
            } else {
                ps.setString(8, photoPath);
            }

            if ("LOST".equals(data.itemType())) {
                ps.setString(9, data.distinguishingFeatures());

                if (data.rewardOffered() == null) {
                    ps.setNull(10, Types.DECIMAL);
                } else {
                    ps.setBigDecimal(10, data.rewardOffered());
                }

                ps.setNull(11, Types.VARCHAR);
                ps.setNull(12, Types.LONGVARCHAR);
                ps.setNull(13, Types.VARCHAR);

            } else {
                ps.setNull(9, Types.LONGVARCHAR);
                ps.setNull(10, Types.DECIMAL);

                ps.setString(11, data.storageLocation());
                ps.setString(12, data.verificationQuestion());
                ps.setString(13, verificationHash);
            }

            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }

            throw new SQLException(
                    "Report saved but ID was not returned."
            );
        }
    }

}
