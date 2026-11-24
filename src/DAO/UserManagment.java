/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Database.DataBaseConnection;
import Models.ManagedUser;
import Service.AdminAccess;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author kiboi
 */
public class UserManagment {

    public List<ManagedUser> findUsers(
            int adminId, String keyword)
            throws SQLException {

        String sql = """
            SELECT user_id, full_name, usiu_id,
                   email, role, is_active
            FROM users
            WHERE full_name LIKE ?
               OR usiu_id LIKE ?
               OR email LIKE ?
            ORDER BY user_id DESC
            LIMIT 500
            """;

        List<ManagedUser> users = new ArrayList<>();
        String search = "%" + keyword.trim() + "%";

        try (Connection conn
                = DataBaseConnection.getConnection()) {

            AdminAccess.requireAdmin(conn, adminId);

            try (PreparedStatement ps
                    = conn.prepareStatement(sql)) {

                ps.setString(1, search);
                ps.setString(2, search);
                ps.setString(3, search);

                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        users.add(new ManagedUser(
                                rs.getInt("user_id"),
                                rs.getString("full_name"),
                                rs.getString("usiu_id"),
                                rs.getString("email"),
                                rs.getString("role"),
                                rs.getBoolean("is_active")
                        ));
                    }
                }
            }
        }

        return users;
    }

    public void setActive(
            int adminId,
            int targetUserId,
            boolean active) throws SQLException {

        if (adminId == targetUserId) {
            throw new SQLException(
                    "You cannot change your own account status."
            );
        }

        String sql = """
            UPDATE users
            SET is_active = ?
            WHERE user_id = ?
              AND role <> 'ADMIN'
            """;

        try (Connection conn
                = DataBaseConnection.getConnection()) {

            conn.setAutoCommit(false);

            try {
                AdminAccess.requireAdmin(conn, adminId);

                try (PreparedStatement ps
                        = conn.prepareStatement(sql)) {

                    ps.setBoolean(1, active);
                    ps.setInt(2, targetUserId);

                    if (ps.executeUpdate() != 1) {
                        throw new SQLException(
                                "User not found or account is protected."
                        );
                    }
                }

                conn.commit();

            } catch (SQLException ex) {
                conn.rollback();
                throw ex;
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }

    public void updateRole(
            int adminId,
            int targetUserId,
            String newRole) throws SQLException {

        if (adminId == targetUserId) {
            throw new SQLException(
                    "You cannot change your own role."
            );
        }

        if (!"STUDENT".equals(newRole)
                && !"STAFF".equals(newRole)
                && !"ADMIN".equals(newRole)) {
            throw new IllegalArgumentException(
                    "Invalid account role."
            );
        }

        String sql = """
            UPDATE users
            SET role = ?
            WHERE user_id = ?
              AND role <> 'ADMIN'
              AND is_active = TRUE
            """;

        try (Connection conn
                = DataBaseConnection.getConnection()) {

            conn.setAutoCommit(false);

            try {
                AdminAccess.requireAdmin(conn, adminId);

                try (PreparedStatement ps
                        = conn.prepareStatement(sql)) {

                    ps.setString(1, newRole);
                    ps.setInt(2, targetUserId);

                    if (ps.executeUpdate() != 1) {
                        throw new SQLException(
                                "User not found, inactive, "
                                + "or administrator account protected."
                        );
                    }
                }

                conn.commit();

            } catch (SQLException ex) {
                conn.rollback();
                throw ex;
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }
}
