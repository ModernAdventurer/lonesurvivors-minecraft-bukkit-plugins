package me.LoneSurvivor.Supernatural.commands.subcommands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.LoneSurvivor.Supernatural.Supernatural;

public class SetMagicCommand {

    public SetMagicCommand(Supernatural supernatural, CommandSender sender, String[] args) {
    	if(sender instanceof Player) {
    		Player player = (Player) sender;
            if (player.hasPermission("supernatural.setmagic")) {
				if (Bukkit.getPlayer(args[1]) != null) {
					supernatural.setMagic(Bukkit.getPlayer(args[1]), Integer.parseInt(args[2]), true);
				} else {
					supernatural.setMagic(player, Integer.parseInt(args[1]), true);
				}
            } else {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', supernatural.getConfig().getString("Messages.insufficient-permission")));
            }
    	} else {
            System.out.println(supernatural.getConfig().getString("Messages.no-console"));
    	}	
    }
    
}
