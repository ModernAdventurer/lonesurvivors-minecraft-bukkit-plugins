package me.LoneSurvivor.Supernatural.Classes.WitchHunter.ActiveAbilities;

import org.bukkit.Location;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import me.LoneSurvivor.Supernatural.Supernatural;
import me.LoneSurvivor.Supernatural.repositories.Constants;

public class VolleyArrow {
	public VolleyArrow(Supernatural supernatural, Constants constants, Player p) {
		supernatural.setCooldown(p.getUniqueId(), "VolleyArrow", supernatural.getConfig().getInt("Spells.WitchHunter.VolleyArrow.Cooldown"));
		supernatural.setMagic(p, supernatural.getMagic(p) - supernatural.getConfig().getInt("Spells.WitchHunter.VolleyArrow.Cost"), false);
		Location loc = p.getLocation();
		loc.setYaw(loc.getYaw());
		Vector playerDirection = loc.getDirection().multiply(2);
		Arrow arrow = p.launchProjectile(Arrow.class, playerDirection);
		arrow.setPickupStatus(Arrow.PickupStatus.DISALLOWED);
		supernatural.getServer().getScheduler().scheduleSyncDelayedTask(supernatural, new Runnable() {
            public void run() {
        		Location loc = p.getLocation();
        		loc.setYaw(loc.getYaw());
        		Vector playerDirection = loc.getDirection().multiply(2);
        		Arrow arrow = p.launchProjectile(Arrow.class, playerDirection);
        		arrow.setPickupStatus(Arrow.PickupStatus.DISALLOWED);
            }
        }, 5L);
		supernatural.getServer().getScheduler().scheduleSyncDelayedTask(supernatural, new Runnable() {
            public void run() {
        		Location loc = p.getLocation();
        		loc.setYaw(loc.getYaw());
        		Vector playerDirection = loc.getDirection().multiply(2);
        		Arrow arrow = p.launchProjectile(Arrow.class, playerDirection);
        		arrow.setPickupStatus(Arrow.PickupStatus.DISALLOWED);
            }
        }, 10L);
		supernatural.getServer().getScheduler().scheduleSyncDelayedTask(supernatural, new Runnable() {
            public void run() {
        		Location loc = p.getLocation();
        		loc.setYaw(loc.getYaw());
        		Vector playerDirection = loc.getDirection().multiply(2);
        		Arrow arrow = p.launchProjectile(Arrow.class, playerDirection);
        		arrow.setPickupStatus(Arrow.PickupStatus.DISALLOWED);
            }
        }, 15L);
		supernatural.getServer().getScheduler().scheduleSyncDelayedTask(supernatural, new Runnable() {
            public void run() {
        		Location loc = p.getLocation();
        		loc.setYaw(loc.getYaw());
        		Vector playerDirection = loc.getDirection().multiply(2);
        		Arrow arrow = p.launchProjectile(Arrow.class, playerDirection);
        		arrow.setPickupStatus(Arrow.PickupStatus.DISALLOWED);
            }
        }, 20L);
	}
}
