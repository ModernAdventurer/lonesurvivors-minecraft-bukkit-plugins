package me.LoneSurvivor.Supernatural.Classes.WitchHunter.ActiveAbilities;

import org.bukkit.Location;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import me.LoneSurvivor.Supernatural.Supernatural;
import me.LoneSurvivor.Supernatural.repositories.Constants;

public class FireArrow {
	public FireArrow(Supernatural supernatural, Constants constants, Player p) {
		supernatural.setCooldown(p.getUniqueId(), "FireArrow", supernatural.getConfig().getInt("Spells.WitchHunter.FireArrow.Cooldown"));
		supernatural.setMagic(p, supernatural.getMagic(p) - supernatural.getConfig().getInt("Spells.WitchHunter.FireArrow.Magic-Cost"), false);
		p.setFoodLevel(p.getFoodLevel() - supernatural.getConfig().getInt("Spells.WitchHunter.FireArrow.Food-Cost"));
		p.setHealth(p.getHealth() - supernatural.getConfig().getInt("Spells.WitchHunter.FireArrow.Health-Cost"));
		Location loc = p.getLocation();
		Vector playerDirection = loc.getDirection().multiply(2);
		Arrow arrow = p.launchProjectile(Arrow.class, playerDirection);
		arrow.setPickupStatus(Arrow.PickupStatus.DISALLOWED);
		arrow.setFireTicks(10000);
		arrow.setCustomName(p.getUniqueId().toString() + ":FireArrow");
	}
}
