package ru.kungfuept.randomteleport.commands;

import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class SafeLeave implements CommandExecutor {

    public HashMap<String, Long> cooldowns = new java.util.HashMap<String, Long>();

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof ConsoleCommandSender) {
            commandSender.getServer().getLogger().warning("[RandomTeleport] You must be a player!");
            return true;
        }

        Player player = (Player) commandSender;
        World world = player.getWorld();
        Location zero = player.getLocation().zero();
        Location playerLoc = player.getLocation();

        int cooldownTime = 30;

        if (cooldowns.containsKey(player.getName())) {
            long secondsLeft = ((cooldowns.get(player.getName()) / 1000) + cooldownTime) - (System.currentTimeMillis() / 1000);
            if (secondsLeft > 0) {
                player.sendMessage(ChatColor.RED + "Вы не можете использовать эту команду ещё "+ ChatColor.YELLOW + secondsLeft + ChatColor.RED +" секунд!");
                return true;
            }
        }

        cooldowns.put(player.getName(), System.currentTimeMillis());

        double distanceLoc = Math.sqrt(Math.pow((playerLoc.getBlockX() - zero.getBlockX()), 2) + Math.pow((playerLoc.getBlockZ() - zero.getBlockZ()), 2));
        int distance = (int) Math.round(distanceLoc);

        if (distance < 500) {
            int randomX = (int) (Math.random() * 1000);
            int randomZ = (int) (Math.random() * 1000);
            Location location = new Location(world, randomX, 70, randomZ);

            world.loadChunk(randomX, randomZ, true);
            Chunk chunk = player.getWorld().getChunkAt(location);
            if (!world.isChunkLoaded(chunk))
                world.loadChunk(chunk);

            int randomY = world.getHighestBlockYAt(randomX, randomZ);
            Location randomtp = new Location(world, randomX, randomY, randomZ);

            player.teleport(randomtp);
            player.sendMessage(ChatColor.GREEN + "Вы успешно рандомно телепортированы!");

            if (!(player.getLocation().getBlock().getType() == Material.AIR)) {
                Location loc = new Location(world, player.getLocation().getBlockX(), world.getHighestBlockYAt(player.getLocation()), player.getLocation().getBlockZ());
                player.teleport(loc);
            }

        } else {
            player.sendMessage(ChatColor.DARK_RED + "Вы и так далеко от спавна!");
        }
        return true;
    }
}
