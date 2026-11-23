/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import DAO.CatalogDAO;
import Models.CatalogOption;
import java.util.List;
import javax.swing.*;

/**
 *
 * @author kiboi
 */
public class CatalogLoader {

    private CatalogLoader() {
    }

    public static void load(
            JComboBox<CatalogOption> categories,
            JComboBox<CatalogOption> locations,
            JLabel errorLabel) {

        categories.setEnabled(false);
        locations.setEnabled(false);
        errorLabel.setText("Loading categories and locations...");

        SwingWorker<List<List<CatalogOption>>, Void> worker
                = new SwingWorker<>() {

            @Override
            protected List<List<CatalogOption>>
                    doInBackground() throws Exception {

                CatalogDAO dao = new CatalogDAO();

                return List.of(
                        dao.getCategories(),
                        dao.getLocations()
                );
            }

            @Override
            protected void done() {

                try {
                    List<List<CatalogOption>> data = get();

                    DefaultComboBoxModel<CatalogOption> catModel
                            = new DefaultComboBoxModel<>();

                    for (CatalogOption option : data.get(0)) {
                        catModel.addElement(option);
                    }

                    DefaultComboBoxModel<CatalogOption> locModel
                            = new DefaultComboBoxModel<>();

                    for (CatalogOption option : data.get(1)) {
                        locModel.addElement(option);
                    }

                    categories.setModel(catModel);
                    locations.setModel(locModel);

                    categories.setSelectedIndex(-1);
                    locations.setSelectedIndex(-1);

                    categories.setEnabled(true);
                    locations.setEnabled(true);

                    errorLabel.setText(" ");

                } catch (Exception ex) {
                    errorLabel.setText(
                            "Unable to load categories or locations."
                    );
                    ex.printStackTrace();
                }
            }
        };

        worker.execute();
    }

}
