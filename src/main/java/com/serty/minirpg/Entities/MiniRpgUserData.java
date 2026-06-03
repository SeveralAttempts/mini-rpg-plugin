/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.serty.minirpg.Entities;

/**
 *
 * @author pimgg
 */
public class MiniRpgUserData {
    public String userName;
    public double generalMiningSpeedValue;
    public double generalMovementSpeedValue;
    public int currentLevel;
    public int blocksForNextLevel;
    public int currentBlocks;

    public MiniRpgUserData(String userName,
        double generalMiningSpeedValue,
        double generalMovementSpeedValue,
        int currentLevel,
        int blocksForNextLevel,
        int currentBlocks
    ) {
        this.userName = userName;
        this.generalMiningSpeedValue = generalMiningSpeedValue;
        this.generalMovementSpeedValue = generalMovementSpeedValue;
        this.currentLevel = currentLevel;
        this.blocksForNextLevel = blocksForNextLevel;
        this.currentBlocks = currentBlocks;
    }
}
