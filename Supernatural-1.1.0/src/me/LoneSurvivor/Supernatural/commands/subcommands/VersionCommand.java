package me.LoneSurvivor.Supernatural.commands.subcommands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;

import me.LoneSurvivor.Supernatural.Supernatural;

public class VersionCommand {

    public VersionCommand(Supernatural supernatural, CommandSender sender, String[] args) {
    	if(sender instanceof Player) {
    		Player player = (Player) sender;
    		PluginDescriptionFile pdf = supernatural.getDescription();
        	if (player.hasPermission("supernatural.getversion")) {
		        player.sendMessage(ChatColor.WHITE + pdf.getName() + " Version: " + ChatColor.DARK_GRAY + "[" + ChatColor.WHITE + pdf.getVersion() + ChatColor.DARK_GRAY + "]");
        	}
    	}
    }
    
}
