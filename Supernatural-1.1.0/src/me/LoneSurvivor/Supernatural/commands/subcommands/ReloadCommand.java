package me.LoneSurvivor.Supernatural.commands.subcommands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.LoneSurvivor.Supernatural.Supernatural;

public class ReloadCommand {

    public ReloadCommand(Supernatural supernatural, CommandSender sender, String[] args) {
    	if(sender instanceof Player) {
    		Player player = (Player) sender;
            if (player.hasPermission("supernatural.reload")) {
            	supernatural.saveDefaultConfig();
            	supernatural.reloadConfig();
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', supernatural.getConfig().getString("Messages.reload-success").replaceAll("%prefix%", supernatural.getConfig().getString("Messages.prefix"))));
                return;
            } else {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', supernatural.getConfig().getString("Messages.insufficient-permission").replaceAll("%prefix%", supernatural.getConfig().getString("Messages.prefix"))));
            }
    	} else {
    		supernatural.saveDefaultConfig();
            supernatural.reloadConfig();
            System.out.println(ChatColor.stripColor(supernatural.getConfig().getString("Messages.reload-success").replaceAll("%prefix%", supernatural.getConfig().getString("Messages.prefix"))));
    	}	
    }
    
}
