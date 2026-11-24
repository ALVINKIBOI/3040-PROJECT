/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controllers;

import Models.MyClaimData;
import Service.MyClaimsService;

import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author kiboi
 */
public class MyClaimsController {

    private final MyClaimsService service
            = new MyClaimsService();

    public List<MyClaimData> getMyClaims(int userId)
            throws SQLException {

        return service.getMyClaims(userId);
    }
}
