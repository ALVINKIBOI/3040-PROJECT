/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Database.DataBaseConnection;
import Models.CatalogKind;
import Models.ManagedCatalogEntry;
import Service.AdminAccess;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author kiboi
 */
public class AdminCatalogDAO {

    private String table(CatalogKind kind) {
        return kind == CatalogKind.CATEGORY
                ? "categories" : "locations";
    }

    private String idColumn(CatalogKind kind) {
        return kind == CatalogKind.CATEGORY
                ? "category_id" : "location_id";
    }

    private String detailColumn(CatalogKind kind) {
        return kind == CatalogKind.CATEGORY
                ? "description" : "building";
    }

    public List<ManagedCatalogEntry> list(
            int adminId,
            CatalogKind kind) throws SQLException {

        String sql = "SELECT "
                + idColumn(kind) + " AS id, "
                + "name, "
                + detailColumn(kind) + " AS details "
                + "FROM " + table(kind)
                + " ORDER BY name";

        List<ManagedCatalogEntry> result
                = new ArrayList<>();

        try (Connection conn
                = DataBaseConnection.getConnection()) {

            AdminAccess.requireAdmin(conn, adminId);

            try (PreparedStatement ps
                    = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    result.add(new ManagedCatalogEntry(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("details")
                    ));
                }
            }
        }

        return result;
    }

    public void add(
            int adminId,
            CatalogKind kind,
            String name,
            String details) throws SQLException {

        String sql = "INSERT INTO " + table(kind)
                + " (name, " + detailColumn(kind)
                + ") VALUES (?, ?)";

        try (Connection conn
                = DataBaseConnection.getConnection()) {

            conn.setAutoCommit(false);

            try {
                AdminAccess.requireAdmin(conn, adminId);

                try (PreparedStatement ps
                        = conn.prepareStatement(sql)) {

                    ps.setString(1, name);
                    ps.setString(2, details);
                    ps.executeUpdate();
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

    public void update(
            int adminId,
            CatalogKind kind,
            int id,
            String name,
            String details) throws SQLException {

        String sql = "UPDATE " + table(kind)
                + " SET name = ?, "
                + detailColumn(kind) + " = ?"
                + " WHERE " + idColumn(kind) + " = ?";

        try (Connection conn
                = DataBaseConnection.getConnection()) {

            conn.setAutoCommit(false);

            try {
                AdminAccess.requireAdmin(conn, adminId);

                try (PreparedStatement ps
                        = conn.prepareStatement(sql)) {

                    ps.setString(1, name);
                    ps.setString(2, details);
                    ps.setInt(3, id);

                    if (ps.executeUpdate() != 1) {
                        throw new SQLException(
                                "Entry not found."
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

    public void delete(
            int adminId,
            CatalogKind kind,
            int id) throws SQLException {

        String foreignKeyColumn
                = kind == CatalogKind.CATEGORY
                        ? "category_id" : "location_id";

        String checkSql = """
            SELECT COUNT(*)
            FROM items
            WHERE %s = ?
            """.formatted(foreignKeyColumn);

        String deleteSql = "DELETE FROM "
                + table(kind)
                + " WHERE " + idColumn(kind) + " = ?";

        try (Connection conn
                = DataBaseConnection.getConnection()) {

            conn.setAutoCommit(false);

            try {
                AdminAccess.requireAdmin(conn, adminId);

                try (PreparedStatement ps
                        = conn.prepareStatement(checkSql)) {

                    ps.setInt(1, id);

                    try (ResultSet rs = ps.executeQuery()) {

                        rs.next();

                        if (rs.getInt(1) > 0) {
                            throw new SQLException(
                                    "Cannot delete: this entry "
                                    + "is used by existing item reports."
                            );
                        }
                    }
                }

                try (PreparedStatement ps
                        = conn.prepareStatement(deleteSql)) {

                    ps.setInt(1, id);

                    if (ps.executeUpdate() != 1) {
                        throw new SQLException(
                                "Entry not found."
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
