package me.LoneSurvivor.Supernatural.Classes.Vampire.ActiveAbilities;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import me.LoneSurvivor.Supernatural.Supernatural;
import me.LoneSurvivor.Supernatural.repositories.Constants;

public class Teleport {
	public Teleport(Supernatural supernatural, Constants constants, Player p) {
		if(supernatural.getTeleportLocation(p) != null) {
			supernatural.setCooldown(p.getUniqueId(), "Teleport", supernatural.getConfig().getInt("Spells.Vampire.Teleport.Cooldown"));
			supernatural.setMagic(p, supernatural.getMagic(p) - supernatural.getConfig().getInt("Spells.Vampire.Teleport.Magic-Cost"), false);
			p.setFoodLevel(p.getFoodLevel() - supernatural.getConfig().getInt("Spells.Vampire.Teleport.Food-Cost"));
			p.setHealth(p.getHealth() - supernatural.getConfig().getInt("Spells.Vampire.Teleport.Health-Cost"));
			Location location = supernatural.getTeleportLocation(p);
			p.teleport(location);
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', supernatural.getConfig().getString("Messages.teleport-success").replaceAll("%prefix%", supernatural.getConfig().getString("Messages.prefix"))));
		} else {
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', supernatural.getConfig().getString("Messages.teleport-failure").replaceAll("%prefix%", supernatural.getConfig().getString("Messages.prefix"))));
		}
	}
}
