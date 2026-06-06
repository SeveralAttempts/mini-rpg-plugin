package com.serty.minirpg.Listeners;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.bukkit.attribute.Attribute;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.serty.minirpg.Services.MiniRpgService;

@Singleton
public class PlayerJoinListener implements Listener {
    private final MiniRpgService miniRpgService;

    @Inject
    public PlayerJoinListener(MiniRpgService miniRpgService) {
        super();
        this.miniRpgService = miniRpgService;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        var userName = event.getPlayer().getName();
        var blockBreakSpeed = event.getPlayer().getAttribute(Attribute.BLOCK_BREAK_SPEED);
        var movementSpeed = event.getPlayer().getAttribute(Attribute.MOVEMENT_SPEED);
        miniRpgService.tryAddUser(userName, blockBreakSpeed.getValue(), movementSpeed.getValue());
        miniRpgService.updatePlayerStats(userName);
    }
}
