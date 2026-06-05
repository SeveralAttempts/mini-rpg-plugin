package com.serty.minirpg;

import javax.inject.Inject;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import com.serty.minirpg.Listeners.PlayerBlockBreakListener;
import com.serty.minirpg.Listeners.PlayerJoinListener;
import com.serty.minirpg.Services.MiniRpgService;
import com.serty.minirpg.injection.AppComponent;
import com.serty.minirpg.injection.DaggerAppComponent;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MiniRpg extends JavaPlugin {
    private final long expectedMinutes = 60L;
    private final long expectedSeconds = 60L;
    private final long defaultTicks = 20L;
    
    @Inject MiniRpgService miniRpgService;
    @Inject PlayerJoinListener playerJoinListener;
    @Inject PlayerBlockBreakListener playerBlockBreakListener;

    @Override
    public void onEnable() {
        AppComponent component = DaggerAppComponent.builder()
                .plugin(this)
                .build();

        component.inject(this);

        registerListeners();

        miniRpgService.initialize(getDataFolder());

        long ticks = expectedMinutes * expectedSeconds * defaultTicks;

        Bukkit.getScheduler().runTaskTimer(this, () -> {
            miniRpgService.updateUserDataFile();
            getLogger().info("MiniRpg: Config update");
        }, ticks, ticks);
    }

    @Override
    public void onDisable() {
        miniRpgService.updateUserDataFile();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player))
        {
            sender.sendMessage("The command is for players only...");
            return false;
        }

        if (command.getName().equalsIgnoreCase("minirpg")
                && args.length == 3 && args[0].equals("changelevel"))
        {
            miniRpgService.usersData.stream()
            .filter(x -> x.userName.equals(args[1]))
            .findFirst()
            .ifPresent(x -> {
                miniRpgService.recalcNewLevel(x.userName, Integer.parseInt(args[2]));
                miniRpgService.updateUserDataFile();
            });

            return true;
        }

        return false;
    }

    private void registerListeners() {

        var pm = getServer().getPluginManager();

        pm.registerEvents(playerJoinListener, this);
        pm.registerEvents(playerBlockBreakListener, this);
    }
}