package me.LoneSurvivor.Supernatural.commands.subcommands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.LoneSurvivor.Supernatural.Supernatural;

public class SetBanishLocationCommand {

    public SetBanishLocationCommand(Supernatural supernatural, CommandSender sender, String[] args) {
    	if(sender instanceof Player) {
    		Player player = (Player) sender;
            if (player.hasPermission("supernatural.setbanishlocation")) {
            	supernatural.setBanishLocation(player.getLocation());
            } else {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', supernatural.getConfig().getString("Messages.insufficient-permission")));
            }
    	} else {
            System.out.println(supernatural.getConfig().getString("Messages.no-console"));
    	}	
    }
    
}
