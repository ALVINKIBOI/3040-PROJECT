/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import java.util.Locale;

/**
 *
 * @author kiboi
 */
public final class VerificationUtils {

    private VerificationUtils() {
    }

    public static char[] normalize(char[] answer) {

        if (answer == null) {
            return new char[0];
        }

        String normalized = new String(answer)
                .trim()
                .replaceAll("\\s+", " ")
                .toLowerCase(Locale.ROOT);

        return normalized.toCharArray();
    }
}
