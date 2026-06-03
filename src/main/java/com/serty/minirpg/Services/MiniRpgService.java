package com.serty.minirpg.Services;

import java.io.File;
import java.util.ArrayList;
import java.util.Optional;

import com.serty.minirpg.Entities.MiniRpgUserData;

public class MiniRpgService {
    public static ArrayList<MiniRpgUserData> usersData;
    private static File pluginFolder;

    public static void initialize(File dataFolder) {
        JsonUserDataService.initializeUserData(dataFolder);
        usersData = JsonUserDataService.readJsonConfig(dataFolder);
        pluginFolder = dataFolder;
    }

    public static boolean tryAddUser(String userName, double miningSpeed, double movementSpeed) {
        var user = usersData.stream().filter(x -> x.userName.equals(userName)).findFirst();

        if (user.isEmpty())
        {
            var newUser = new MiniRpgUserData(userName, miningSpeed, movementSpeed, 1, 12, 0);
            usersData.add(newUser);
            return true;
        }

        return false;
    }

    public static Optional<MiniRpgUserData> onBlockBreakNewLevel(String userName) {
        var userData = usersData.stream()
        .filter(x -> x.userName.equals(userName))
        .findFirst();

        var level = userData.get().currentLevel;

        userData.ifPresent(x -> {
            x.currentBlocks++;
            if (x.currentBlocks >= x.blocksForNextLevel)
            {
                x.currentLevel++;
                x.blocksForNextLevel = (int)(x.blocksForNextLevel * 1.2);
                x.currentBlocks = 0;
                x.generalMiningSpeedValue *= 1.05;
                x.generalMovementSpeedValue *= 1.02;
            }
        });

        if (level != userData.get().currentLevel) return Optional.of(userData.get());

        return Optional.empty();
    }

    public static void updateUserDataFile() {
        JsonUserDataService.writeJsonConfig(usersData, pluginFolder);
    }

    public static void recalcNewLevel(String userName, int newLevel) {
        var userData = usersData.stream()
        .filter(x -> x.userName.equals(userName))
        .findFirst();

        userData.ifPresent(x -> {
            if (x.currentLevel > newLevel) return;

            var levelDelta = Math.abs(x.currentLevel - newLevel);

            x.currentLevel += levelDelta;
            x.blocksForNextLevel = (int)(x.blocksForNextLevel * (1 + 0.2 * levelDelta));
            x.generalMiningSpeedValue *= 1 + 0.05 * levelDelta;
            x.generalMovementSpeedValue *= 1 + 0.02 * levelDelta;
        });
    }

    public static int getLevel(String userName) {
        return usersData.stream()
        .filter(x -> x.userName.equals(userName))
        .findFirst().get().currentLevel;
    }
}
