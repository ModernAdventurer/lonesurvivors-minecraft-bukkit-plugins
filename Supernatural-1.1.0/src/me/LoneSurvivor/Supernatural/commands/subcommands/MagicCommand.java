package me.LoneSurvivor.Supernatural.commands.subcommands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.LoneSurvivor.Supernatural.Supernatural;

public class MagicCommand {
    public MagicCommand(Supernatural supernatural, CommandSender sender, String[] args) {
    	Player target = null;
    	int magic = 0;
    	if(args.length<3 || Bukkit.getPlayer(args[2]) == null) {
    		if(sender instanceof Player) {
    			target = (Player) sender;
    			if(!args[1].equalsIgnoreCase("get")) {
            		magic = Integer.parseInt(args[2]);
    			}
    			if(!target.hasPermission("supernatural.setmagic")) {
    	    		target.sendMessage(ChatColor.translateAlternateColorCodes('&', supernatural.getConfig().getString("Messages.insufficient-permission")));
    	    		return;
    	    	}
        	} else {
                System.out.println(ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', supernatural.getConfig().getString("Messages.must-specify-player"))));
        	}
    	} else {
    		target = Bukkit.getPlayer(args[2]);
			if(!args[1].equalsIgnoreCase("get")) {
	    		magic = Integer.parseInt(args[3]);
			}
    	}
    	if(args[1].equalsIgnoreCase("set")) {
    		supernatural.setMagic(target, magic, true);
    	}
    	if(args[1].equalsIgnoreCase("add")) {
    		supernatural.setMagic(target, supernatural.getMagic(target) + magic, true);
    	}
    	if(args[1].equalsIgnoreCase("remove")) {
			supernatural.setMagic(target, supernatural.getMagic(target) - magic, true);
    	}
    	if(args[1].equalsIgnoreCase("get")) {
        	if(sender instanceof Player) {
    			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', supernatural.getConfig().getString("Messages.prefix") + " &e" + target.getName() + " has &6" + supernatural.getMagic(target) + " &emagic"));
        	} else {
            	sender.sendMessage(ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', supernatural.getConfig().getString("Messages.prefix") + " &e" + target.getName() + " has &6" + supernatural.getMagic(target) + " &emagic")));
        	}
        	return;
    	}
    }
}
