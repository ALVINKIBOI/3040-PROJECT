/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Service;

import DAO.MyReportDAO;
import Models.MyReportData;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

/**
 *
 * @author kiboi
 */
public class MyReportService {

    private final MyReportDAO dao = new MyReportDAO();

    public List<MyReportData> list(
            int userId, String search, String type)
            throws SQLException {

        validateUser(userId);

        String filter = type == null ? "ALL" : type;

        if (!Set.of("ALL", "LOST", "FOUND")
                .contains(filter)) {
            throw new IllegalArgumentException(
                    "Invalid report type."
            );
        }

        if (search != null && search.length() > 100) {
            throw new IllegalArgumentException(
                    "Search text is too long."
            );
        }

        return dao.getMyReports(userId, search, filter);
    }

    public MyReportData getReport(
            int itemId, int userId)
            throws SQLException {

        validateUser(userId);
        return dao.getOwnedReport(itemId, userId);
    }

    public void update(
            int itemId, int userId,
            String title, String description,
            int categoryId, int locationId,
            LocalDate dateOccurred,
            String features, BigDecimal reward,
            String storageLocation)
            throws SQLException {

        validateUser(userId);

        if (itemId <= 0
                || categoryId <= 0
                || locationId <= 0) {
            throw new IllegalArgumentException(
                    "Invalid report, category or location."
            );
        }

        if (title == null || title.isBlank()
                || description == null
                || description.isBlank()) {
            throw new IllegalArgumentException(
                    "Title and description are required."
            );
        }

        if (dateOccurred == null
                || dateOccurred.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException(
                    "Enter a valid report date."
            );
        }

        if (reward != null
                && (reward.signum() < 0
                || reward.scale() > 2
                || reward.compareTo(
                        new BigDecimal("99999999.99")
                ) > 0)) {
            throw new IllegalArgumentException(
                    "Invalid reward amount."
            );
        }

        MyReportData existing
                = dao.getOwnedReport(itemId, userId);

        if (existing == null) {
            throw new IllegalArgumentException(
                    "Report not found or access denied."
            );
        }

        if ("FOUND".equals(existing.itemType())
                && (storageLocation == null
                || storageLocation.isBlank())) {
            throw new IllegalArgumentException(
                    "Storage location is required."
            );
        }

        boolean updated = dao.updateReport(
                itemId, userId,
                title.trim(), description.trim(),
                categoryId, locationId,
                dateOccurred, features,
                reward, storageLocation
        );

        if (!updated) {
            throw new IllegalStateException(
                    "This report can no longer be edited."
            );
        }
    }

    public void delete(int itemId, int userId)
            throws SQLException {

        validateUser(userId);

        if (!dao.softDelete(itemId, userId)) {
            throw new IllegalStateException(
                    "Report not found or cannot be deleted."
            );
        }
    }

    private void validateUser(int userId) {
        if (userId <= 0) {
            throw new SecurityException(
                    "Authentication is required."
            );
        }
    }

}
