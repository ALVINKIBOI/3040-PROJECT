/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controllers;
import Models.ItemReport;
import Service.ItemService;

/**
 *
 * @author kiboi
 */
public class LoginController {
    
    private final ItemService itemService =
            new ItemService();

    public int submitReport(
            ItemReport data,
            char[] verificationAnswer)
            throws Exception {

        return itemService.createReport(
            data, verificationAnswer
        );
    }
}
