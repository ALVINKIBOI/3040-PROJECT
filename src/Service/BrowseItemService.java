/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Service;

import DAO.BrowseItemDAO;
import Models.BrowseItemData;
import Models.ItemDetailsData;
import Models.ItemSearchFilter;

import java.sql.SQLException;
import java.util.List;
import java.util.Set;

/**
 *
 * @author kiboi
 */
public class BrowseItemService {

    private final BrowseItemDAO dao
            = new BrowseItemDAO();

    private static final Set<String> TYPES
            = Set.of("LOST", "FOUND");

    private static final Set<String> STATUSES
            = Set.of(
                    "OPEN",
                    "MATCHED",
                    "CLAIM_PENDING",
                    "RETURNED",
                    "ARCHIVED"
            );

    public List<BrowseItemData> search(
            ItemSearchFilter filter)
            throws SQLException {

        if (filter == null) {
            throw new IllegalArgumentException(
                    "Search filter is required."
            );
        }

        if (filter.itemType() != null
                && !TYPES.contains(filter.itemType())) {
            throw new IllegalArgumentException(
                    "Invalid report type."
            );
        }

        if (filter.status() != null
                && !STATUSES.contains(filter.status())) {
            throw new IllegalArgumentException(
                    "Invalid item status."
            );
        }

        if (filter.keyword() != null
                && filter.keyword().length() > 100) {
            throw new IllegalArgumentException(
                    "Search text is too long."
            );
        }

        return dao.searchItems(filter);
    }

    public ItemDetailsData getDetails(int itemId)
            throws SQLException {

        if (itemId <= 0) {
            throw new IllegalArgumentException(
                    "Invalid item ID."
            );
        }

        return dao.getItemDetails(itemId);
    }
}
