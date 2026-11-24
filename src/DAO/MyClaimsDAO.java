/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Database.DataBaseConnection;
import Models.MyClaimData;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author kiboi
 */
public class MyClaimsDAO {

    public List<MyClaimData> getMyClaims(int userId)
            throws SQLException {

        String sql = """
            SELECT
                c.claim_id,
                c.found_item_id,
                i.title AS item_title,
                c.claim_date,
                c.status,
                c.proof_description,
                c.decision_note,
                c.reviewed_at
            FROM claims c
            JOIN items i
                ON c.found_item_id = i.item_id
            WHERE c.claimant_id = ?
            ORDER BY c.claim_date DESC,
                     c.claim_id DESC
            """;

        List<MyClaimData> claims = new ArrayList<>();

        try (Connection conn
                = DataBaseConnection.getConnection(); PreparedStatement ps
                = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    Timestamp claimDate
                            = rs.getTimestamp("claim_date");

                    Timestamp reviewedAt
                            = rs.getTimestamp("reviewed_at");

                    claims.add(new MyClaimData(
                            rs.getInt("claim_id"),
                            rs.getInt("found_item_id"),
                            rs.getString("item_title"),
                            claimDate == null
                                    ? null
                                    : claimDate.toLocalDateTime(),
                            rs.getString("status"),
                            rs.getString("proof_description"),
                            rs.getString("decision_note"),
                            reviewedAt == null
                                    ? null
                                    : reviewedAt.toLocalDateTime()
                    ));
                }
            }
        }

        return claims;
    }
}
