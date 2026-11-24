/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import Controllers.AdminCatalogController;
import Models.AuthUser;
import Models.CatalogKind;
import Models.ManagedCatalogEntry;
import java.awt.Component;
import java.util.Collections;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author kiboi
 */
public class CatalogManagmentUI {

    private final AuthUser user;
    private final CatalogKind kind;

    private final JTable table;
    private final JTextField nameField;
    private final JTextField detailField;
    private final JLabel errorLabel;

    private final JButton addButton;
    private final JButton updateButton;
    private final JButton deleteButton;
    private final JButton refreshButton;

    private final Component parent;

    private final AdminCatalogController controller
            = new AdminCatalogController();

    private List<ManagedCatalogEntry> entries
            = Collections.emptyList();

    private boolean busy = false;

    public CatalogManagmentUI(
            Component parent,
            AuthUser user,
            CatalogKind kind,
            JTable table,
            JTextField nameField,
            JTextField detailField,
            JLabel errorLabel,
            JButton addButton,
            JButton updateButton,
            JButton deleteButton,
            JButton refreshButton) {

        this.parent = parent;
        this.user = user;
        this.kind = kind;
        this.table = table;
        this.nameField = nameField;
        this.detailField = detailField;
        this.errorLabel = errorLabel;

        this.addButton = addButton;
        this.updateButton = updateButton;
        this.deleteButton = deleteButton;
        this.refreshButton = refreshButton;

        configureTable();

        addButton.addActionListener(e -> save(false));
        updateButton.addActionListener(e -> save(true));
        deleteButton.addActionListener(e -> delete());
        refreshButton.addActionListener(e -> load());

        load();
    }

    private void configureTable() {

        DefaultTableModel model
                = new DefaultTableModel(
                        new Object[][]{},
                        new String[]{
                            "ID",
                            "Name",
                            kind == CatalogKind.CATEGORY
                                    ? "Description" : "Building"
                        }
                ) {
            @Override
            public boolean isCellEditable(
                    int row, int column) {
                return false;
            }
        };

        table.setModel(model);
        table.setRowHeight(34);
        table.setAutoCreateRowSorter(true);
        table.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        table.getSelectionModel()
                .addListSelectionListener(e -> {

                    if (!e.getValueIsAdjusting()) {
                        displaySelection();
                    }
                });

        updateButton.setEnabled(false);
        deleteButton.setEnabled(false);
    }

    private ManagedCatalogEntry selected() {

        int row = table.getSelectedRow();

        if (row < 0) {
            return null;
        }

        int modelRow
                = table.convertRowIndexToModel(row);

        int id = (Integer) table.getModel().getValueAt(modelRow, 0);

        for (ManagedCatalogEntry entry : entries) {
            if (entry.id() == id) {
                return entry;
            }
        }

        return null;
    }

    private void displaySelection() {

        ManagedCatalogEntry entry = selected();

        if (entry == null) {
            updateButton.setEnabled(false);
            deleteButton.setEnabled(false);
            return;
        }

        nameField.setText(entry.name());
        detailField.setText(
                entry.details() == null
                ? "" : entry.details()
        );

        updateButton.setEnabled(!busy);
        deleteButton.setEnabled(!busy);
    }

    private void setBusy(boolean value) {

        busy = value;

        addButton.setEnabled(!value);
        refreshButton.setEnabled(!value);
        updateButton.setEnabled(
                !value && selected() != null
        );
        deleteButton.setEnabled(
                !value && selected() != null
        );
    }

    public boolean isBusy() {
        return busy;
    }

    public void load() {

        if (busy) {
            return;
        }

        setBusy(true);
        errorLabel.setText("Loading records...");

        SwingWorker<List<ManagedCatalogEntry>, Void> worker
                = new SwingWorker<>() {

            @Override
            protected List<ManagedCatalogEntry>
                    doInBackground() throws Exception {

                return controller.list(
                        user.userId(), kind
                );
            }

            @Override
            protected void done() {

                try {
                    entries = get();

                    DefaultTableModel model
                            = (DefaultTableModel) table.getModel();

                    model.setRowCount(0);

                    for (ManagedCatalogEntry entry : entries) {
                        model.addRow(new Object[]{
                            entry.id(),
                            entry.name(),
                            entry.details()
                        });
                    }

                    nameField.setText("");
                    detailField.setText("");
                    table.clearSelection();

                    errorLabel.setText(
                            entries.size() + " records found"
                    );

                } catch (Exception ex) {
                    showError(ex);
                } finally {
                    setBusy(false);
                }
            }
        };

        worker.execute();
    }

    private void save(boolean updating) {

        if (busy) {
            return;
        }

        ManagedCatalogEntry entry
                = updating ? selected() : null;

        if (updating && entry == null) {
            JOptionPane.showMessageDialog(
                    parent, "Select a record first."
            );
            return;
        }

        final String name = nameField.getText();
        final String details = detailField.getText();

        setBusy(true);

        SwingWorker<Void, Void> worker
                = new SwingWorker<>() {

            @Override
            protected Void doInBackground()
                    throws Exception {

                if (updating) {
                    controller.update(
                            user.userId(),
                            kind,
                            entry.id(),
                            name,
                            details
                    );
                } else {
                    controller.add(
                            user.userId(),
                            kind,
                            name,
                            details
                    );
                }

                return null;
            }

            @Override
            protected void done() {

                setBusy(false);

                try {
                    get();

                    JOptionPane.showMessageDialog(
                            parent,
                            "Record saved successfully."
                    );

                    load();

                } catch (Exception ex) {
                    showError(ex);
                }
            }
        };

        worker.execute();
    }

    private void delete() {

        if (busy) {
            return;
        }

        ManagedCatalogEntry entry = selected();

        if (entry == null) {
            return;
        }

        int confirmation
                = JOptionPane.showConfirmDialog(
                        parent,
                        "Delete \"" + entry.name() + "\"?\n"
                        + "This is allowed only if no reports "
                        + "reference it.",
                        "Confirm Deletion",
                        JOptionPane.YES_NO_OPTION
                );

        if (confirmation != JOptionPane.YES_OPTION) {
            return;
        }

        setBusy(true);

        SwingWorker<Void, Void> worker
                = new SwingWorker<>() {

            @Override
            protected Void doInBackground()
                    throws Exception {

                controller.delete(
                        user.userId(),
                        kind,
                        entry.id()
                );

                return null;
            }

            @Override
            protected void done() {

                setBusy(false);

                try {
                    get();

                    JOptionPane.showMessageDialog(
                            parent,
                            "Record deleted successfully."
                    );

                    load();

                } catch (Exception ex) {
                    showError(ex);
                }
            }
        };

        worker.execute();
    }

    private void showError(Exception ex) {

        Throwable cause = ex;

        while (cause.getCause() != null) {
            cause = cause.getCause();
        }

        errorLabel.setText("Operation failed.");

        JOptionPane.showMessageDialog(
                parent,
                cause.getMessage() == null
                ? "Database operation failed."
                : cause.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
        );
    }

}
