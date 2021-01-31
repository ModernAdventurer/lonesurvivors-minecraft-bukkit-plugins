package me.LoneSurvivor.Supernatural.Classes.WitchHunter.ActiveAbilities;

import org.bukkit.Location;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import me.LoneSurvivor.Supernatural.Supernatural;
import me.LoneSurvivor.Supernatural.repositories.Constants;

public class GrappleArrow {
	public GrappleArrow(Supernatural supernatural, Constants constants, Player p) {
		supernatural.setCooldown(p.getUniqueId(), "GrappleArrow", supernatural.getConfig().getInt("Spells.WitchHunter.GrappleArrow.Cooldown"));
		supernatural.setMagic(p, supernatural.getMagic(p) - supernatural.getConfig().getInt("Spells.WitchHunter.GrappleArrow.Magic-Cost"), false);
		p.setFoodLevel(p.getFoodLevel() - supernatural.getConfig().getInt("Spells.WitchHunter.GrappleArrow.Food-Cost"));
		p.setHealth(p.getHealth() - supernatural.getConfig().getInt("Spells.WitchHunter.GrappleArrow.Health-Cost"));
		Location loc = p.getLocation();
		Vector playerDirection = loc.getDirection().multiply(2);
		Arrow arrow = p.launchProjectile(Arrow.class, playerDirection);
		supernatural.setGrappleLocation(p, loc);
		arrow.setPickupStatus(Arrow.PickupStatus.DISALLOWED);
		arrow.setDamage(0);
		supernatural.setGrappleArrows(p, arrow);
	}
}
