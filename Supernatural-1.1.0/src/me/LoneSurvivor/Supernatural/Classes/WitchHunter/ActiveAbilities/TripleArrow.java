package me.LoneSurvivor.Supernatural.Classes.WitchHunter.ActiveAbilities;

import org.bukkit.Location;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import me.LoneSurvivor.Supernatural.Supernatural;
import me.LoneSurvivor.Supernatural.repositories.Constants;

public class TripleArrow {
	public TripleArrow(Supernatural supernatural, Constants constants, Player p) {
		supernatural.setCooldown(p.getUniqueId(), "TripleArrow", supernatural.getConfig().getInt("Spells.WitchHunter.TripleArrow.Cooldown"));
		supernatural.setMagic(p, supernatural.getMagic(p) - supernatural.getConfig().getInt("Spells.WitchHunter.TripleArrow.Cost"), false);
		Location loc = p.getLocation();
		loc.setYaw(loc.getYaw() - 10F);
		Vector playerDirection = loc.getDirection().multiply(1.5);
		Arrow arrow = p.launchProjectile(Arrow.class, playerDirection);
		arrow.setPickupStatus(Arrow.PickupStatus.DISALLOWED);
		loc.setYaw(loc.getYaw() + 10F);
		playerDirection = loc.getDirection().multiply(1.5);
		Arrow arrow2 = p.launchProjectile(Arrow.class, playerDirection);
		arrow2.setPickupStatus(Arrow.PickupStatus.DISALLOWED);
		loc.setYaw(loc.getYaw() + 10F);
		playerDirection = loc.getDirection().multiply(1.5);
		Arrow arrow3 = p.launchProjectile(Arrow.class, playerDirection);
		arrow3.setPickupStatus(Arrow.PickupStatus.DISALLOWED);
	}
}
