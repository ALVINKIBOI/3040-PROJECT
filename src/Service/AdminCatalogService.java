/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Service;

import DAO.AdminCatalogDAO;
import Models.CatalogKind;
import Models.ManagedCatalogEntry;

import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author kiboi
 */
public class AdminCatalogService {

    private final AdminCatalogDAO dao
            = new AdminCatalogDAO();

    public List<ManagedCatalogEntry> list(
            int adminId, CatalogKind kind)
            throws SQLException {

        requireAdminId(adminId);
        requireKind(kind);

        return dao.list(adminId, kind);
    }

    public void add(
            int adminId,
            CatalogKind kind,
            String name,
            String details) throws SQLException {

        validate(adminId, kind, name, details);

        dao.add(
                adminId,
                kind,
                name.trim(),
                details.trim()
        );
    }

    public void update(
            int adminId,
            CatalogKind kind,
            int id,
            String name,
            String details) throws SQLException {

        validate(adminId, kind, name, details);

        if (id <= 0) {
            throw new IllegalArgumentException(
                    "Select an entry first."
            );
        }

        dao.update(
                adminId,
                kind,
                id,
                name.trim(),
                details.trim()
        );
    }

    public void delete(
            int adminId,
            CatalogKind kind,
            int id) throws SQLException {

        requireAdminId(adminId);
        requireKind(kind);

        if (id <= 0) {
            throw new IllegalArgumentException(
                    "Select an entry first."
            );
        }

        dao.delete(adminId, kind, id);
    }

    private void requireAdminId(int adminId) {
        if (adminId <= 0) {
            throw new IllegalArgumentException(
                    "Administrator login required."
            );
        }
    }

    private void requireKind(CatalogKind kind) {
        if (kind == null) {
            throw new IllegalArgumentException(
                    "Catalog type is required."
            );
        }
    }

    private void validate(
            int adminId,
            CatalogKind kind,
            String name,
            String details) {

        requireAdminId(adminId);
        requireKind(kind);

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException(
                    "Name is required."
            );
        }

        if (name.trim().length() > 100) {
            throw new IllegalArgumentException(
                    "Name must not exceed 100 characters."
            );
        }

        if (details == null) {
            throw new IllegalArgumentException(
                    "Description or building is required."
            );
        }

        if (kind == CatalogKind.LOCATION
                && details.trim().length() > 100) {
            throw new IllegalArgumentException(
                    "Building name must not exceed 100 characters."
            );
        }
    }
}
