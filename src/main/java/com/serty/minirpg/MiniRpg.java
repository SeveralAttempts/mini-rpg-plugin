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

public class MiniRpg extends JavaPlugin {
    private final long expectedMinutes = 30L;
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
        if (command.getName().equalsIgnoreCase("minirpg")) {

            switch(args[0].toLowerCase())
            {
                case "getlevel":
                    if (args.length != 1) break;
                    sender.sendMessage(miniRpgService.getLevel(sender.getName()));
                    break;
                case "changelevel":
                    if (args.length != 3) break;
                    miniRpgService.usersData.stream()
                        .filter(x -> x.userName.equals(args[1]))
                        .findFirst()
                        .ifPresent(x -> {
                            miniRpgService.recalcNewLevel(x.userName, Integer.parseInt(args[2]));
                            miniRpgService.updateUserDataFile();
                        });
                    break;
            }
            
        }
        return true;
    }

    private void registerListeners() {

        var pm = getServer().getPluginManager();

        pm.registerEvents(playerJoinListener, this);
        pm.registerEvents(playerBlockBreakListener, this);
    }
}