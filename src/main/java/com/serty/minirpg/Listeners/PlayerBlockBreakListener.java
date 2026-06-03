package com.serty.minirpg.Listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import com.serty.minirpg.Services.MiniRpgService;

public class PlayerBlockBreakListener implements Listener {

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        var playerName = e.getPlayer().getName();
        var result = MiniRpgService.onBlockBreakNewLevel(playerName);

        result.ifPresent(x -> {
            MiniRpgService.updateUserDataFile();
        });
    }
}
