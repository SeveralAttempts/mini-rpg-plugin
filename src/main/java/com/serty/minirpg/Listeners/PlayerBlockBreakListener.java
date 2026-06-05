package com.serty.minirpg.Listeners;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import com.serty.minirpg.Services.MiniRpgService;

@Singleton
public class PlayerBlockBreakListener implements Listener {
    private final MiniRpgService miniRpgService;

    @Inject
    public PlayerBlockBreakListener(MiniRpgService miniRpgService) {
        super();
        this.miniRpgService = miniRpgService; 
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        var playerName = e.getPlayer().getName();
        var result = miniRpgService.onBlockBreakNewLevel(playerName);

        result.ifPresent(x -> {
            miniRpgService.updateUserDataFile();
        });
    }
}
