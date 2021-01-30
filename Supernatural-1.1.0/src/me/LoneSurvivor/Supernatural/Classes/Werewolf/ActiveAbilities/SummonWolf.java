package me.LoneSurvivor.Supernatural.Classes.Werewolf.ActiveAbilities;

import org.bukkit.ChatColor;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;

import me.LoneSurvivor.Supernatural.Supernatural;
import me.LoneSurvivor.Supernatural.repositories.Constants;

public class SummonWolf {
	public SummonWolf(Supernatural supernatural, Constants constants, Player p) {
		if(!supernatural.isDay(p)) {
			supernatural.setCooldown(p.getUniqueId(), "SummonWolf", supernatural.getConfig().getInt("Spells.Werewolf.SummonWolf.Cooldown"));
			supernatural.setMagic(p, supernatural.getMagic(p) - supernatural.getConfig().getInt("Spells.Werewolf.SummonWolf.Cost"), false);
			Wolf wolf = (Wolf) p.getWorld().spawnEntity(p.getLocation(), EntityType.WOLF);
			wolf.setOwner(p);
		} else {
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', supernatural.getConfig().getString("Messages.night-only-abilities").replaceAll("%prefix%", supernatural.getConfig().getString("Messages.prefix"))));
			return;
		}
	}
}
