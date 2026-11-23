/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Service;

import Models.MatchableItem;

/**
 *
 * @author kiboi
 */
public interface MatchingStrategy {

    double calculateScore(
            MatchableItem lost,
            MatchableItem found
    );
}
