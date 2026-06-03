package com.serty.minirpg.Listeners;

import org.bukkit.attribute.Attribute;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.serty.minirpg.Services.MiniRpgService;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        var userName = event.getPlayer().getName();
        var blockBreakSpeed = event.getPlayer().getAttribute(Attribute.BLOCK_BREAK_SPEED);
        var movementSpeed = event.getPlayer().getAttribute(Attribute.MOVEMENT_SPEED);
        MiniRpgService.tryAddUser(userName, blockBreakSpeed.getValue(), movementSpeed.getValue());
    }
}
