package me.LoneSurvivor.Supernatural.Classes.WitchHunter.ActiveAbilities;

import org.bukkit.Location;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import me.LoneSurvivor.Supernatural.Supernatural;
import me.LoneSurvivor.Supernatural.repositories.Constants;

public class PowerArrow {
	public PowerArrow(Supernatural supernatural, Constants constants, Player p) {
		supernatural.setCooldown(p.getUniqueId(), "PowerArrow", supernatural.getConfig().getInt("Classes.WitchHunter.ActiveAbilities.PowerArrow.Cooldown"));
		supernatural.setMagic(p, supernatural.getMagic(p) - supernatural.getConfig().getInt("Classes.WitchHunter.ActiveAbilities.PowerArrow.Magic-Cost"), false);
		p.setFoodLevel(p.getFoodLevel() - supernatural.getConfig().getInt("Classes.WitchHunter.ActiveAbilities.PowerArrow.Food-Cost"));
		p.setHealth(p.getHealth() - supernatural.getConfig().getInt("Classes.WitchHunter.ActiveAbilities.PowerArrow.Health-Cost"));
		Location loc = p.getLocation();
		Vector playerDirection = loc.getDirection();
		playerDirection.multiply(4);
		Arrow arrow = p.launchProjectile(Arrow.class, playerDirection);
		arrow.setPickupStatus(Arrow.PickupStatus.DISALLOWED);
		arrow.setDamage(arrow.getDamage()*4);
		arrow.setKnockbackStrength(arrow.getKnockbackStrength()*2);
		arrow.setCustomName(p.getUniqueId().toString() + ":PowerArrow");
	}
}
