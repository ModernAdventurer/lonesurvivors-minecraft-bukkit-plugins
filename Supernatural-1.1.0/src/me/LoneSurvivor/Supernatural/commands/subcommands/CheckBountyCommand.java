package me.LoneSurvivor.Supernatural.commands.subcommands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.LoneSurvivor.Supernatural.Supernatural;

public class CheckBountyCommand {

    public CheckBountyCommand(Supernatural supernatural, CommandSender sender, String[] args) {
    	if(sender instanceof Player) {
    		Player player = (Player) sender;
            if (player.hasPermission("supernatural.checkbounty")) {
            	if (args.length > 1 && Bukkit.getPlayer(args[1]) != null) {
	            	if(supernatural.getRace(player).equalsIgnoreCase("WitchHunter")) {
						Player target = Bukkit.getPlayer(args[1]);
						if(supernatural.getPlayerBounties().containsKey(target.getUniqueId())) {
							double bounty = supernatural.getPlayerBounty(target.getUniqueId());
			                player.sendMessage(ChatColor.translateAlternateColorCodes('&', supernatural.getConfig().getString("Messages.bounty-get").replaceAll("%prefix%", supernatural.getConfig().getString("Messages.prefix")).replaceAll("%bounty%", (""+bounty)).replaceAll("%target%", target.getName())));
						} else {
			                player.sendMessage(ChatColor.translateAlternateColorCodes('&', supernatural.getConfig().getString("Messages.no-bounty").replaceAll("%prefix%", supernatural.getConfig().getString("Messages.prefix"))));
						}
	            	} else {
	            		player.sendMessage(ChatColor.translateAlternateColorCodes('&', supernatural.getConfig().getString("Messages.witchhunter-only-command").replaceAll("%prefix%", supernatural.getConfig().getString("Messages.prefix"))));
	            	}
				} else {
	            	if(supernatural.getRace(player).equalsIgnoreCase("WitchHunter")) {
	            		supernatural.openBountyMenu(player);
	            	} else {
	            		player.sendMessage(ChatColor.translateAlternateColorCodes('&', supernatural.getConfig().getString("Messages.witchhunter-only-command").replaceAll("%prefix%", supernatural.getConfig().getString("Messages.prefix"))));
	            	}
	            }
			}
    	}
    }
    
}
