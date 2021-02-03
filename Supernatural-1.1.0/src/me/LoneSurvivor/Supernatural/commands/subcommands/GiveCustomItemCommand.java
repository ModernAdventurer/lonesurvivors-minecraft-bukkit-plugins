package me.LoneSurvivor.Supernatural.commands.subcommands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.LoneSurvivor.Supernatural.Supernatural;
import me.LoneSurvivor.Supernatural.repositories.Constants;

public class GiveCustomItemCommand {
    public GiveCustomItemCommand(Supernatural supernatural, Constants constants, CommandSender sender, String[] args) {
    	Player target = null;
    	int amount = 1;
    	ItemStack item = null;
    	if(Bukkit.getPlayer(args[1]) == null) {
    		if(sender instanceof Player) {
    			target = (Player) sender;
            	if(constants.getCustomItems().containsKey(args[1])) {
            		item = constants.getCustomItems().get(args[1]);
            	} else {
            		target.sendMessage(ChatColor.translateAlternateColorCodes('&', "&4Error: &cUnknown Custom item"));
            		return;
            	}
    			if(args.length > 2) {
        			amount = Integer.parseInt(args[2]);
    			}
    			if(!target.hasPermission("supernatural.setmagic")) {
    	    		target.sendMessage(ChatColor.translateAlternateColorCodes('&', supernatural.getConfig().getString("Messages.insufficient-permission")));
    	    		return;
    	    	}
        	} else {
                System.out.println(ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', supernatural.getConfig().getString("Messages.must-specify-player"))));
        	}
    	} else {
    		target = Bukkit.getPlayer(args[1]);
        	if(constants.getCustomItems().containsKey(args[2])) {
        		item = constants.getCustomItems().get(args[2]);
        	} else {
        		target.sendMessage(ChatColor.translateAlternateColorCodes('&', "&4Error: &cUnknown Custom item"));
        		return;
        	}
			if(args.length > 3) {
    			amount = Integer.parseInt(args[3]);
			}
    	}
    	if(amount>64) {
    		amount = 64;
    	}
        item.setAmount(amount);
        supernatural.addItemSafely(target, item);
    	return;
    }
}
