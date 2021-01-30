package me.LoneSurvivor.Supernatural.commands.subcommands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.LoneSurvivor.Supernatural.Supernatural;
import net.milkbowl.vault.economy.Economy;

public class AddBountyCommand {

    public AddBountyCommand(Supernatural supernatural, Economy eco, CommandSender sender, String[] args) {
    	if(sender instanceof Player) {
    		Player player = (Player) sender;
            if (player.hasPermission("supernatural.addbounty")) {
				if (Bukkit.getPlayer(args[1]) != null) {
					Player target = Bukkit.getPlayer(args[1]);
					if(supernatural.isSupernatural(supernatural.getRace(target))) {
						if(!player.getUniqueId().equals(target.getUniqueId())) {
							try {
								double bounty = Integer.parseInt(args[2]);
								if(bounty <= 0) {
									player.sendMessage(ChatColor.translateAlternateColorCodes('&', supernatural.getConfig().getString("Messages.invalid-bounty").replaceAll("%prefix%", supernatural.getConfig().getString("Messages.prefix"))));
									return;
								}
								if(eco.getBalance(player) < bounty) {
									player.sendMessage(ChatColor.translateAlternateColorCodes('&', supernatural.getConfig().getString("Messages.insufficient-balance").replaceAll("%prefix%", supernatural.getConfig().getString("Messages.prefix"))));
									return;
								}
		                        eco.withdrawPlayer(player, bounty);
								double tax = 0.12;
								bounty = bounty - bounty*tax;
								player.sendMessage(ChatColor.translateAlternateColorCodes('&', supernatural.getConfig().getString("Messages.bounty-notify-sender").replaceAll("%prefix%", supernatural.getConfig().getString("Messages.prefix")).replaceAll("%bounty%", ("" + bounty)).replaceAll("%target%", target.getName()).replaceAll("%player%", player.getName()).replaceAll("%percenttax%", ("" + (tax*100)))));
								target.sendMessage(ChatColor.translateAlternateColorCodes('&', supernatural.getConfig().getString("Messages.bounty-notify-target").replaceAll("%prefix%", supernatural.getConfig().getString("Messages.prefix")).replaceAll("%bounty%", ("" + bounty)).replaceAll("%target%", target.getName()).replaceAll("%player%", player.getName()).replaceAll("%percenttax%", ("" + (tax*100)))));
								for(World w : Bukkit.getWorlds()) {
									for(Player p : w.getPlayers()) {
										if(supernatural.getRace(p).equalsIgnoreCase("WitchHunter")) {
											if(!p.getUniqueId().equals(player.getUniqueId())) {
												p.sendMessage(ChatColor.translateAlternateColorCodes('&', supernatural.getConfig().getString("Messages.bounty-notify-all").replaceAll("%prefix%", supernatural.getConfig().getString("Messages.prefix")).replaceAll("%bounty%", ("" + bounty)).replaceAll("%target%", target.getName()).replaceAll("%player%", player.getName()).replaceAll("%percenttax%", ("" + (tax*100)))));
											}
										}
									}
								}
								supernatural.setPlayerBounty(target, supernatural.getPlayerBounty(target) + bounty);
							} catch(Exception e) {
								player.sendMessage(ChatColor.translateAlternateColorCodes('&', supernatural.getConfig().getString("Messages.invalid-bounty").replaceAll("%prefix%", supernatural.getConfig().getString("Messages.prefix"))));
							}
						} else {
							player.sendMessage(ChatColor.translateAlternateColorCodes('&', supernatural.getConfig().getString("Messages.no-bounty-on-self").replaceAll("%prefix%", supernatural.getConfig().getString("Messages.prefix"))));
							}
					} else {
						player.sendMessage(ChatColor.translateAlternateColorCodes('&', supernatural.getConfig().getString("Messages.target-must-be-supernatural").replaceAll("%prefix%", supernatural.getConfig().getString("Messages.prefix"))));
					}
				} else {
					player.sendMessage(ChatColor.translateAlternateColorCodes('&', supernatural.getConfig().getString("Messages.must-specify-player").replaceAll("%prefix%", supernatural.getConfig().getString("Messages.prefix"))));
				}
            } else {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', supernatural.getConfig().getString("Messages.insufficient-permission").replaceAll("%prefix%", supernatural.getConfig().getString("Messages.prefix"))));
            }
    	} else {
            System.out.println(supernatural.getConfig().getString("Messages.no-console"));
    	}	
    }

}
