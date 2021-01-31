package me.LoneSurvivor.Supernatural.Classes.Necromancer.ActiveAbilities;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import me.LoneSurvivor.Supernatural.Supernatural;
import me.LoneSurvivor.Supernatural.repositories.Constants;

public class SetRessurectionSpawn {
	public SetRessurectionSpawn(Supernatural supernatural, Constants constants, Player p) {
		supernatural.setCooldown(p.getUniqueId(), "SetRessurectionSpawn", supernatural.getConfig().getInt("Spells.Necromancer.SetRessurectionSpawn.Cooldown"));
		supernatural.setMagic(p, supernatural.getMagic(p) - supernatural.getConfig().getInt("Spells.Necromancer.SetRessurectionSpawn.Magic-Cost"), false);
		p.setFoodLevel(p.getFoodLevel() - supernatural.getConfig().getInt("Spells.Necromancer.SetRessurectionSpawn.Food-Cost"));
		p.setHealth(p.getHealth() - supernatural.getConfig().getInt("Spells.Necromancer.SetRessurectionSpawn.Health-Cost"));
		supernatural.setRessurectLocation(p, p.getLocation());
		p.sendMessage(ChatColor.translateAlternateColorCodes('&', supernatural.getConfig().getString("Messages.set-ressurection-location").replaceAll("%prefix%", supernatural.getConfig().getString("Messages.prefix"))));
	}
}
