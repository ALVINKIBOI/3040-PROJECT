/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author kiboi
 */
public final class AdminAccess {

    private AdminAccess() {
    }

    public static void requireAdmin(
            Connection conn,
            int adminId) throws SQLException {

        String sql = """
            SELECT user_id
            FROM users
            WHERE user_id = ?
              AND role = 'ADMIN'
              AND is_active = TRUE
            """;

        try (PreparedStatement ps
                = conn.prepareStatement(sql)) {

            ps.setInt(1, adminId);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    throw new SQLException(
                            "Active administrator access required."
                    );
                }
            }
        }
    }
}
