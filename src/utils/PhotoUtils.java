/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;
import java.awt.Image;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author kiboi
 */
public class PhotoUtils {
    
    private PhotoUtils() {
    }

    public static File choosePhoto(
            java.awt.Component parent,
            JLabel preview,
            JLabel filenameLabel) {

        JFileChooser chooser = new JFileChooser();

        chooser.setDialogTitle("Select Item Photo");
        chooser.setFileFilter(
            new FileNameExtensionFilter(
                "JPG and PNG Images",
                "jpg", "jpeg", "png"
            )
        );

        if (chooser.showOpenDialog(parent)
                != JFileChooser.APPROVE_OPTION) {
            return null;
        }

        File file = chooser.getSelectedFile();

        try {
            if (file.length() > 5L * 1024 * 1024) {
                throw new IllegalArgumentException(
                    "Photo must not exceed 5 MB."
                );
            }

            var image = ImageIO.read(file);

            if (image == null) {
                throw new IllegalArgumentException(
                    "The selected file is not a valid image."
                );
            }

            Image resized = image.getScaledInstance(
                260, 160, Image.SCALE_SMOOTH
            );

            preview.setIcon(new ImageIcon(resized));
            preview.setText("");

            filenameLabel.setText(file.getName());

            return file;

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(
                parent,
                ex.getMessage(),
                "Invalid Photo",
                JOptionPane.ERROR_MESSAGE
            );

            return null;
        }
    }
    
}
