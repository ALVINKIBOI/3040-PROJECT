/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Service;
import DAO.ItemDAO;
import Models.ItemReport;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.*;
import java.security.GeneralSecurityException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.UUID;
import javax.imageio.ImageIO;
import utils.PasswordUtils;

/**
 *
 * @author kiboi
 */
public class ItemService {
    
    private final ItemDAO itemDAO = new ItemDAO();

    public int createReport(
            ItemReport data,
            char[] verificationAnswer)
            throws Exception {

        validate(data, verificationAnswer);

        String verificationHash = null;

        if ("FOUND".equals(data.itemType())) {
            verificationHash =
                PasswordUtils.hashPassword(verificationAnswer);
        }

        Path savedPhoto = null;

        try {
            if (data.photo() != null) {
                savedPhoto = savePhoto(data.photo());
            }

            return itemDAO.insertReport(
                data,
                savedPhoto == null
                    ? null
                    : savedPhoto.toString(),
                verificationHash
            );

        } catch (Exception ex) {

            if (savedPhoto != null) {
                try {
                    Files.deleteIfExists(savedPhoto);
                } catch (IOException cleanupError) {
                    ex.addSuppressed(cleanupError);
                }
            }

            throw ex;

        } finally {
            if (verificationAnswer != null) {
                Arrays.fill(verificationAnswer, '\0');
            }
        }
    }

    private void validate(
            ItemReport data,
            char[] answer) {

        if (data == null
                || data.userId() <= 0
                || data.categoryId() <= 0
                || data.locationId() <= 0) {

            throw new IllegalArgumentException(
                "Valid user, category and location are required."
            );
        }

        if (!"LOST".equals(data.itemType())
                && !"FOUND".equals(data.itemType())) {

            throw new IllegalArgumentException(
                "Invalid report type."
            );
        }

        if (data.title() == null
                || data.title().isBlank()
                || data.description() == null
                || data.description().isBlank()) {

            throw new IllegalArgumentException(
                "Title and description are required."
            );
        }

        if (data.dateOccurred() == null
                || data.dateOccurred().isAfter(LocalDate.now())) {

            throw new IllegalArgumentException(
                "Enter a valid date that is not in the future."
            );
        }

        if ("LOST".equals(data.itemType())) {

            BigDecimal reward = data.rewardOffered();

            if (reward != null
                    && (reward.signum() < 0
                        || reward.compareTo(
                            new BigDecimal("99999999.99")
                        ) > 0
                        || reward.scale() > 2)) {

                throw new IllegalArgumentException(
                    "Enter a valid reward amount."
                );
            }

        } else {

            if (data.storageLocation() == null
                    || data.storageLocation().isBlank()
                    || data.verificationQuestion() == null
                    || data.verificationQuestion().isBlank()
                    || answer == null
                    || answer.length == 0) {

                throw new IllegalArgumentException(
                    "Storage and verification details are required."
                );
            }
        }
    }

    private Path savePhoto(File source) throws IOException {

        if (!source.isFile()
                || source.length() > 5L * 1024 * 1024) {
            throw new IOException("Invalid photo size.");
        }

        var image = ImageIO.read(source);

        if (image == null) {
            throw new IOException("Unsupported image.");
        }

        Path directory = Paths.get(
            System.getProperty("user.home"),
            "USIULostFound",
            "photos"
        );

        Files.createDirectories(directory);

        String filename =
            UUID.randomUUID().toString() + ".png";

        Path destination = directory.resolve(filename);

        try {
            boolean written = ImageIO.write(
                image, "png", destination.toFile()
            );

            if (!written) {
                throw new IOException(
                    "Unable to encode photo."
                );
            }

            return destination;

        } catch (IOException ex) {
            Files.deleteIfExists(destination);
            throw ex;
        }
    }
    
}
