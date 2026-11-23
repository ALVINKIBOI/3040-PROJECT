/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controllers;

import Models.MyReportData;
import Service.MyReportService;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author kiboi
 */
public class MyReportController {

    private final MyReportService service
            = new MyReportService();

    public List<MyReportData> getMyReports(
            int userId, String search, String type)
            throws SQLException {

        return service.list(userId, search, type);
    }

    public MyReportData getReport(
            int itemId, int userId)
            throws SQLException {

        return service.getReport(itemId, userId);
    }

    public void updateReport(
            int itemId, int userId,
            String title, String description,
            int categoryId, int locationId,
            LocalDate dateOccurred,
            String features, BigDecimal reward,
            String storageLocation)
            throws SQLException {

        service.update(
                itemId, userId,
                title, description,
                categoryId, locationId,
                dateOccurred, features,
                reward, storageLocation
        );
    }

    public void deleteReport(int itemId, int userId)
            throws SQLException {

        service.delete(itemId, userId);
    }
}
