package ru.kungfuept.randomteleport.commands;

import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SafeLeave implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Player player = (Player) commandSender;
        World world = player.getWorld();
        Location zero = player.getLocation().zero();
        Location playerLoc = player.getLocation();
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
        } else {
            player.sendMessage(ChatColor.DARK_RED + "Вы и так далеко от спавна!");
        }
        return true;
    }
}
