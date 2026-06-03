package com.serty.minirpg;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import com.serty.minirpg.Listeners.PlayerBlockBreakListener;
import com.serty.minirpg.Listeners.PlayerJoinListener;
import com.serty.minirpg.Services.MiniRpgService;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MiniRpg extends JavaPlugin {
    private final long expectedMinutes = 60L;
    private final long expectedSeconds = 60L;
    private final long defaultTicks = 20L;

    @Override
    public void onEnable() {
        registerListeners();

        MiniRpgService.initialize(getDataFolder());

        long ticks = expectedMinutes * expectedSeconds * defaultTicks;

        Bukkit.getScheduler().runTaskTimer(this, () -> {
            MiniRpgService.updateUserDataFile();
            getLogger().info("MiniRpg: Config update");
        }, ticks, ticks);
    }

    @Override
    public void onDisable() {
        MiniRpgService.updateUserDataFile();
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
            MiniRpgService.usersData.stream()
            .filter(x -> x.userName.equals(args[1]))
            .findFirst()
            .ifPresent(x -> {
                MiniRpgService.recalcNewLevel(x.userName, Integer.parseInt(args[2]));
                MiniRpgService.updateUserDataFile();
            });

            return true;
        }

        return false;
    }

    private void registerListeners() {

        var pm = getServer().getPluginManager();

        pm.registerEvents(new PlayerJoinListener(), this);
        pm.registerEvents(new PlayerBlockBreakListener(), this);
    }
}