package ru.kungfuept.randomteleport;

import org.bukkit.plugin.java.JavaPlugin;
import ru.kungfuept.randomteleport.commands.SafeBack;
import ru.kungfuept.randomteleport.commands.SafeLeave;

public final class RandomTeleport extends JavaPlugin {
    public void onEnable() {
        getCommand("safeleave").setExecutor(new SafeLeave());
        getCommand("safeback").setExecutor(new SafeBack());
        getServer().getLogger().info("[RandomTeleport] Successful enabled! Hello :)");
        getServer().getLogger().info("[RandomTeleport] Plugin mabe by KungfuEpt. Discord: kungfuept");

    }

    public void onDisable() {
        this.getServer().getLogger().info("[RandomTeleport] Successful disabled! Goodbye :(");
    }
}
