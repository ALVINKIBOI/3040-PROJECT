/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controllers;

import Models.CatalogKind;
import Models.ManagedCatalogEntry;
import Service.AdminCatalogService;

import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author kiboi
 */
public class AdminCatalogController {

    private final AdminCatalogService service
            = new AdminCatalogService();

    public List<ManagedCatalogEntry> list(
            int adminId, CatalogKind kind)
            throws SQLException {

        return service.list(adminId, kind);
    }

    public void add(
            int adminId,
            CatalogKind kind,
            String name,
            String details) throws SQLException {

        service.add(adminId, kind, name, details);
    }

    public void update(
            int adminId,
            CatalogKind kind,
            int id,
            String name,
            String details) throws SQLException {

        service.update(
                adminId, kind, id, name, details
        );
    }

    public void delete(
            int adminId,
            CatalogKind kind,
            int id) throws SQLException {

        service.delete(adminId, kind, id);
    }
}
