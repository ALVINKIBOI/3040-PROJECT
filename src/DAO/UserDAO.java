/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Database.DataBaseConnection;
import java.sql.*;
import Models.RegistrationData;

/**
 *
 * @author kiboi
 */
public class UserDAO {

    public void createUser(
            RegistrationData data,
            String passwordHash)
            throws SQLException {

        String sql = """
            INSERT INTO users (
                full_name, usiu_id, email,
                password_hash, phone, is_active,
                role, programme, year_of_study,
                department
            )
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;

        try (Connection conn
                = DataBaseConnection.getConnection(); PreparedStatement ps
                = conn.prepareStatement(sql)) {

            ps.setString(1, data.fullName());
            ps.setString(2, data.usiuID());
            ps.setString(3, data.email());
            ps.setString(4, passwordHash);
            ps.setString(5, data.phone());
            ps.setBoolean(6, true);
            ps.setString(7, data.role());

            if ("STUDENT".equals(data.role())) {

                ps.setString(8, data.programme());

                ps.setInt(9, data.yearOfStudy());

                ps.setNull(10, Types.VARCHAR);

            } else {

                ps.setNull(8, Types.VARCHAR);
                ps.setNull(9, Types.INTEGER);

                ps.setString(10, data.department());
            }

            ps.executeUpdate();
        }
    }

    public UserAccount findByEmail(String email)
            throws SQLException {

        String sql = """
            SELECT user_id, full_name, email,
                   password_hash, role, is_active
            FROM users
            WHERE email = ?
            LIMIT 1
            """;

        try (Connection conn
                = DataBaseConnection.getConnection(); PreparedStatement ps
                = conn.prepareStatement(sql)) {

            ps.setString(1, email);

            try (ResultSet rs = ps.executeQuery()) {

                if (!rs.next()) {
                    return null;
                }

                return new UserAccount(
                        rs.getInt("user_id"),
                        rs.getString("full_name"),
                        rs.getString("password_hash"),
                        rs.getString("role"),
                        rs.getBoolean("is_active")
                );
            }
        }
    }

    // Database-only representation.
    public record UserAccount(
            int userId,
            String fullName,
            String passwordHash,
            String role,
            boolean active
            ) {

    }
}
