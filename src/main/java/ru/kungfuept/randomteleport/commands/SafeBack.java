package ru.kungfuept.randomteleport.commands;

import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SafeBack implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Player player = (Player) commandSender;
        World world = player.getWorld();
        Location playerLoc = player.getLocation();
        Location spawn = world.getSpawnLocation();

        double distanceLoc = Math.sqrt(Math.pow((playerLoc.getBlockX() - spawn.getBlockX()), 2) + Math.pow((playerLoc.getBlockZ() - spawn.getBlockZ()), 2));
        int distance = (int) Math.round(distanceLoc);

        if (distance < 1000) {
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
            
            player.sendMessage(ChatColor.GREEN + "Вы успешно телепортированы на спавн!");
        } else {
            player.sendMessage(ChatColor.DARK_RED + "Вы слишком далеко от спавна!");
        }
        return true;
    }
}
