package me.LoneSurvivor.Supernatural.commands.subcommands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.LoneSurvivor.Supernatural.Supernatural;

public class SetClassCommand {

    public SetClassCommand(Supernatural supernatural, CommandSender sender, String[] args) {
    	if(sender instanceof Player) {
    		Player player = (Player) sender;
            if (player.hasPermission("supernatural.setclass")) {
				if (Bukkit.getPlayer(args[1]) != null) {
					Player target = Bukkit.getPlayer(args[1]);
					supernatural.setRace(target, args[2], false, true);
				} else {
					supernatural.setRace(player, args[1], false, true);
				}
            } else {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', supernatural.getConfig().getString("Messages.insufficient-permission").replaceAll("%prefix%", supernatural.getConfig().getString("Messages.prefix"))));
            }
    	} else {
            System.out.println(supernatural.getConfig().getString("Messages.no-console"));
    	}	
    }

}
