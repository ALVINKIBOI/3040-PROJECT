/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Service;

import DAO.MyClaimsDAO;
import Models.MyClaimData;

import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author kiboi
 */
public class MyClaimsService {

    private final MyClaimsDAO dao
            = new MyClaimsDAO();

    public List<MyClaimData> getMyClaims(int userId)
            throws SQLException {

        if (userId <= 0) {
            throw new IllegalArgumentException(
                    "Login is required."
            );
        }

        return dao.getMyClaims(userId);
    }
}
