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
			supernatural.setCooldown(p.getUniqueId(), "SummonWolf", supernatural.getConfig().getInt("Classes.Werewolf.ActiveAbilities.SummonWolf.Cooldown"));
			supernatural.setMagic(p, supernatural.getMagic(p) - supernatural.getConfig().getInt("Classes.Werewolf.ActiveAbilities.SummonWolf.Magic-Cost"), false);
			p.setFoodLevel(p.getFoodLevel() - supernatural.getConfig().getInt("Classes.Werewolf.ActiveAbilities.SummonWolf.Food-Cost"));
			p.setHealth(p.getHealth() - supernatural.getConfig().getInt("Classes.Werewolf.ActiveAbilities.SummonWolf.Health-Cost"));
			Wolf wolf = (Wolf) p.getWorld().spawnEntity(p.getLocation(), EntityType.WOLF);
			wolf.setOwner(p);
		} else {
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', supernatural.getConfig().getString("Messages.night-only-abilities").replaceAll("%prefix%", supernatural.getConfig().getString("Messages.prefix"))));
			return;
		}
	}
}
