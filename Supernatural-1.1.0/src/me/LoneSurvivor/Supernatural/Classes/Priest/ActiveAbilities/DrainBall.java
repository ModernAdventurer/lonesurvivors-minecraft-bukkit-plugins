package me.LoneSurvivor.Supernatural.Classes.Priest.ActiveAbilities;

import org.bukkit.entity.Player;
import org.bukkit.entity.SmallFireball;

import me.LoneSurvivor.Supernatural.Supernatural;
import me.LoneSurvivor.Supernatural.repositories.Constants;

public class DrainBall {
	public DrainBall(Supernatural supernatural, Constants constants, Player p) {
		supernatural.setCooldown(p.getUniqueId(), "Drain", supernatural.getConfig().getInt("Spells.Priest.Drain.Cooldown"));
		supernatural.setMagic(p, supernatural.getMagic(p) - supernatural.getConfig().getInt("Spells.Priest.Drain.Cost"), false);
    	SmallFireball projectile = p.launchProjectile(SmallFireball.class);
    	projectile.setIsIncendiary(false);
    	projectile.setCustomName(p.getUniqueId().toString() + ":Drain");
    	supernatural.getServer().getScheduler().scheduleSyncDelayedTask(supernatural, new Runnable() {
            public void run() {
            	projectile.remove();
            }
        }, ((long) Math.ceil(supernatural.FireballEstimateTimeFromDistance(supernatural.getConfig().getInt("Spells.Priest.Drain.Range"))*20)));
	}
}
