package com.serty.minirpg.Services;

import java.io.File;
import java.util.ArrayList;
import java.util.Optional;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;

import com.serty.minirpg.Entities.DefaultAttributeValues;
import com.serty.minirpg.Entities.MiniRpgUserData;

@Singleton
public class MiniRpgService {
    public ArrayList<MiniRpgUserData> usersData;
    private File pluginFolder;

    private final JsonUserDataService jsonUserDataService;

    @Inject
    public MiniRpgService(JsonUserDataService jsonUserDataService) {
        super();
        this.jsonUserDataService = jsonUserDataService;
    }

    public void initialize(File dataFolder) {
        jsonUserDataService.initializeUserData(dataFolder);
        usersData = jsonUserDataService.readJsonConfig(dataFolder);
        pluginFolder = dataFolder;
    }

    public boolean tryAddUser(String username) {
        var user = usersData.stream().filter(x -> x.userName.equals(username)).findFirst();
        var userMc = Bukkit.getPlayer(username);

        if (user.isEmpty())
        {
            var miningSpeed = userMc.getAttribute(Attribute.BLOCK_BREAK_SPEED);
            var movementSpeed = userMc.getAttribute(Attribute.MOVEMENT_SPEED);
            var newUser = new MiniRpgUserData(username, miningSpeed.getBaseValue(), movementSpeed.getBaseValue(), 1, 12, 0);
            resetPlayerStats(username);
            usersData.add(newUser);
            return true;
        }

        return false;
    }

    public Optional<MiniRpgUserData> onBlockBreakNewLevel(String username) {
        var userData = usersData.stream()
        .filter(x -> x.userName.equals(username))
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

        if (level != userData.get().currentLevel) {
            updatePlayerStats(username);
            return Optional.of(userData.get());
        }

        return Optional.empty();
    }

    public void updateUserDataFile() {
        jsonUserDataService.writeJsonConfig(usersData, pluginFolder);
    }

    public void recalcNewLevel(String username, int newLevel) {
        var userData = usersData.stream()
            .filter(x -> x.userName.equals(username))
            .findFirst();

        userData.ifPresent(x -> {
            if (x.currentLevel > newLevel) return;

            var levelDelta = Math.abs(x.currentLevel - newLevel);

            x.currentLevel += levelDelta;
            x.blocksForNextLevel = (int)(x.blocksForNextLevel * (1 + 0.2 * levelDelta));
            x.generalMiningSpeedValue *= 1 + 0.05 * levelDelta;
            x.generalMovementSpeedValue *= 1 + 0.02 * levelDelta;
        });

        updatePlayerStats(username);
    }

    public String getBlocksCountForNextLevel(String username) {
        var userData = usersData.stream()
            .filter(x -> x.userName.equals(username))
            .findFirst();
        
        if (userData.isPresent()) 
            return "Blocks for next level: " + userData.get().blocksForNextLevel;

        return "Blocks for next level: none";
    }

    public String getStats(String username) {
        var userData = usersData.stream()
            .filter(x -> x.userName.equals(username))
            .findFirst();
        
        if (userData.isPresent()) 
            return "Mining: " + userData.get().generalMiningSpeedValue + " Movement: " + userData.get().generalMovementSpeedValue;

        return "Blocks for next level: none";
    }

    public String getLevel(String userName) {
        var level = usersData.stream()
            .filter(x -> x.userName.equals(userName))
            .findFirst().get().currentLevel;
        return "Your level is " + level;
    }

    public void updatePlayerStats(String username) {
        var player = Bukkit.getPlayer(username);

        if (player != null && player.isOnline()) {
            var userData = usersData.stream().findFirst().filter(x -> x.userName.equals(username));

            userData.ifPresent(x -> {
                player.getAttribute(Attribute.BLOCK_BREAK_SPEED).setBaseValue(x.generalMiningSpeedValue);
                player.getAttribute(Attribute.MOVEMENT_SPEED).setBaseValue(x.generalMovementSpeedValue);
            });
        }
    }

    public void resetPlayerStats(String username) {
        var player = Bukkit.getPlayer(username);

        if (player != null && player.isOnline()) {
            var userData = usersData.stream().findFirst().filter(x -> x.userName.equals(username));

            userData.ifPresent(x -> {
                player.getAttribute(Attribute.BLOCK_BREAK_SPEED).setBaseValue(DefaultAttributeValues.DEFAULT_BLOCK_BREAK_SPEED);
                player.getAttribute(Attribute.MOVEMENT_SPEED).setBaseValue(DefaultAttributeValues.DEFAULT_MOVEMENT_SPEED);
                turnToDefaultPlayer(x);
            });
        }
    }

    private void turnToDefaultPlayer(MiniRpgUserData data) {
        data.blocksForNextLevel = 12;
        data.generalMiningSpeedValue = DefaultAttributeValues.DEFAULT_BLOCK_BREAK_SPEED;
        data.generalMovementSpeedValue = DefaultAttributeValues.DEFAULT_MOVEMENT_SPEED;
        data.currentLevel = 1;
        data.currentBlocks = 0;
    }
}
