package me.LoneSurvivor.Supernatural.commands.subcommands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;

import me.LoneSurvivor.Supernatural.Supernatural;

public class HelpCommand {

    public HelpCommand(Supernatural supernatural, CommandSender sender, String[] args) {
    	if(sender instanceof Player) {
    		Player player = (Player) sender;
    		PluginDescriptionFile pdf = supernatural.getDescription();
        	if (player.hasPermission("supernatural.help")) {
		    	player.sendMessage(ChatColor.GRAY + "-------------------");
		        player.sendMessage(ChatColor.GREEN + pdf.getName() + " Help");
		        player.sendMessage(ChatColor.GRAY + "-------------------");
		        if (player.hasPermission("supernatural.help")) {
		            player.sendMessage(ChatColor.GREEN + "/sn help");
		        }
		        if (player.hasPermission("supernatural.getversion")) {
		            player.sendMessage(ChatColor.GREEN + "/sn version");
		        }
		        if (player.hasPermission("supernatural.reload")) {
		            player.sendMessage(ChatColor.GREEN + "/sn reload");
		        }
		        if (player.hasPermission("supernatural.setclass")) {
		            player.sendMessage(ChatColor.GREEN + "/sn setclass");
		        }
		        if (player.hasPermission("supernatural.setmagic")) {
		            player.sendMessage(ChatColor.GREEN + "/sn setmagic");
		        }
		        if (player.hasPermission("supernatural.classes")) {
		            player.sendMessage(ChatColor.GREEN + "/sn class <class>");
		        }
		        if (player.hasPermission("supernatural.setbanishlocation")) {
		            player.sendMessage(ChatColor.GREEN + "/sn sbl");
		        }
		        if (player.hasPermission("supernatural.addbounty")) {
		            player.sendMessage(ChatColor.GREEN + "/bounty add <target> <bounty>");
		        }
		        if (player.hasPermission("supernatural.checkbounty")) {
		            player.sendMessage(ChatColor.GREEN + "/bounty check OR /bounty check <target>");
		        }
		    } else {
		        player.sendMessage(ChatColor.translateAlternateColorCodes('&', supernatural.getConfig().getString("Messages.insufficient-permission")));
		    }
    	} else {
    		System.out.print("-------------------");
    		System.out.print("Supernatural Commands");
    		System.out.print("-------------------");
    		System.out.print("/sn help");
    		System.out.print("/sn reload");
    		System.out.print("/sn setclass");
    		System.out.print("/sn setmagic");
    		System.out.print("/sn class <class>");
    		System.out.print("/sn sbl");
    		System.out.print("/bounty add <target> <bounty>");
    		System.out.print("/bounty check OR /bounty check <target>");
    	}
    	return;
    }

}