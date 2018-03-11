package fr.thisismac.zombie;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ZombieCommand implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String cmdname, String[] args) {
		
		if (args.length == 0) {
			if (Core.INSTANCE.isSpawned()) {
				sender.sendMessage(ChatColor.GOLD + "_--[King Zombie]--_");
				sender.sendMessage(ChatColor.YELLOW + "X : " + Core.INSTANCE.getSpawnLoc().getBlockX() + " & "  + "Z : " + Core.INSTANCE.getSpawnLoc().getBlockZ());
				sender.sendMessage(ChatColor.YELLOW + "Vous pouvez également y accéder via /warp kingzombie");
				return true;
			}
			else {
				sender.sendMessage(ChatColor.GOLD + "_--[King Zombie]--_");
				sender.sendMessage(ChatColor.YELLOW + "Pas de KingZombie actuellement!");
				return true;
			}
		}
		else if (sender.isOp() && args.length == 4 && args[0].equals("spawn")) {
				
				Core.INSTANCE.spawnZombie(new Location(Bukkit.getWorlds().get(0), Integer.valueOf(args[1]), Integer.valueOf(args[2]), Integer.valueOf(args[3])));
				return true;
		}
		return false;
	}

}
