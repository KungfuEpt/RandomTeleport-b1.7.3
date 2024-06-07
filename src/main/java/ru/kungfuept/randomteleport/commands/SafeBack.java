package ru.kungfuept.randomteleport.commands;

import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class SafeBack implements CommandExecutor {

    public HashMap<String, Long> cooldowns = new HashMap<String, Long>();

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof ConsoleCommandSender) {
            commandSender.getServer().getLogger().warning("[RandomTeleport] You must be a player!");
            return true;
        }

        Player player = (Player) commandSender;
        World world = player.getWorld();
        Location playerLoc = player.getLocation();
        Location spawn = world.getSpawnLocation();

        int cooldownTime = 30;

        if (cooldowns.containsKey(player.getName())) {
            long secondsLeft = ((cooldowns.get(player.getName()) / 1000) + cooldownTime) - (System.currentTimeMillis() / 1000);
            if (secondsLeft > 0) {
                player.sendMessage(ChatColor.RED + "Вы не можете использовать эту команду ещё " + ChatColor.YELLOW + secondsLeft + ChatColor.RED + " секунд!");
                return true;
            }
        }

        cooldowns.put(player.getName(), System.currentTimeMillis());

        double distanceLoc = Math.sqrt(Math.pow((playerLoc.getBlockX() - spawn.getBlockX()), 2) + Math.pow((playerLoc.getBlockZ() - spawn.getBlockZ()), 2));
        int distance = (int) Math.round(distanceLoc);

        if (distance < 100) {
            int randomX = (int) (Math.random() * 10) + spawn.getBlockX();
            int randomZ = (int) (Math.random() * 10) + spawn.getBlockZ();

            Location location = new Location(world, randomX, 70, randomZ);

            world.loadChunk(randomX, randomZ, true);
            Chunk chunk = player.getWorld().getChunkAt(location);
            if (!world.isChunkLoaded(chunk))
                world.loadChunk(chunk);

            int randomY = world.getHighestBlockYAt(randomX, randomZ);
            Location randomtpspawn = new Location(world, randomX, randomY, randomZ);
            player.teleport(randomtpspawn);

            if (!(player.getLocation().getBlock().getType() == Material.AIR)) {
                Location loc = new Location(world, player.getLocation().getBlockX(), world.getHighestBlockYAt(player.getLocation()), player.getLocation().getBlockZ());
                player.teleport(loc);
            }

            player.sendMessage(ChatColor.GREEN + "Вы успешно телепортированы на спавн!");
        }
        else {
            player.sendMessage(ChatColor.DARK_RED + "Вы слишком далеко от спавна!");
        }
        return true;
    }
}
