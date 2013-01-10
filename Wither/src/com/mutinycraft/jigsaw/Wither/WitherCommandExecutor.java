package com.mutinycraft.jigsaw.Wither;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WitherCommandExecutor implements CommandExecutor{
	
	private Wither plugin;
	
	public WitherCommandExecutor(Wither pl){
		plugin = pl;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		// Reload command from player
		if(sender instanceof Player && cmd.getName().equalsIgnoreCase("wither")){
			if((args.length >= 1) && args[0].equalsIgnoreCase("reload")){
				if(sender.hasPermission("wither.reload")){
					plugin.reloadConfig();
					sender.sendMessage(ChatColor.RED + "Wither config.yml has been reloaded!");
					return true;
				}
				else{
					sender.sendMessage(ChatColor.RED + "Usage: /wither reload");
					return true;
				}
			}
			else{
				sender.sendMessage(ChatColor.RED + "You don't have permission to do this!");
				return true;
			}
		}
		
		// Reload command from console
		if(cmd.getName().equalsIgnoreCase("wither") && !(sender instanceof Player)){
			if((args.length >= 1) && args[0].equalsIgnoreCase("reload")){
				plugin.reloadConfig();
				sender.sendMessage(ChatColor.RED + "Wither config.yml has been reloaded!");
				return true;
			}
			else{
				sender.sendMessage(ChatColor.RED + "Usage: /wither reload");
				return true;
			}
		}
		return false;
	}

}
