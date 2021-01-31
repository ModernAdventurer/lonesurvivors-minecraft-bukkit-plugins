package me.LoneSurvivor.Supernatural.Classes.Werewolf.ActiveAbilities;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import me.LoneSurvivor.Supernatural.Supernatural;
import me.LoneSurvivor.Supernatural.repositories.Constants;

public class Dash {
	public Dash(Supernatural supernatural, Constants constants, Player p) {
		if(!supernatural.isDay(p)) {
			//Block below
			Location l = p.getLocation();
			l.setY(l.getY()-1.25);
			if(l.getBlock().getType() == Material.AIR) {
				return;
			}
			supernatural.setCooldown(p.getUniqueId(), "Dash", supernatural.getConfig().getInt("Spells.Werewolf.Dash.Cooldown"));
			supernatural.setMagic(p, supernatural.getMagic(p) - supernatural.getConfig().getInt("Spells.Werewolf.Dash.Magic-Cost"), false);
			p.setFoodLevel(p.getFoodLevel() - supernatural.getConfig().getInt("Spells.Werewolf.Dash.Food-Cost"));
			p.setHealth(p.getHealth() - supernatural.getConfig().getInt("Spells.Werewolf.Dash.Health-Cost"));
			Vector direction = p.getLocation().getDirection().multiply(1.5).setY(p.getVelocity().getY());
			p.setVelocity(direction);
		} else {
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', supernatural.getConfig().getString("Messages.night-only-abilities").replaceAll("%prefix%", supernatural.getConfig().getString("Messages.prefix"))));
			return;
		}
	}
}
