/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controllers;

import Models.BrowseItemData;
import Models.ItemDetailsData;
import Models.ItemSearchFilter;
import Service.BrowseItemService;

import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author kiboi
 */
public class BrowseItemController {

    private final BrowseItemService service
            = new BrowseItemService();

    public List<BrowseItemData> search(
            ItemSearchFilter filter)
            throws SQLException {

        return service.search(filter);
    }

    public ItemDetailsData getDetails(int itemId)
            throws SQLException {

        return service.getDetails(itemId);
    }
}
