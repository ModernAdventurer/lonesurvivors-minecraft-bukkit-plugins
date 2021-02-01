package me.LoneSurvivor.Supernatural.Classes.Priest.ActiveAbilities;

import org.bukkit.entity.Player;
import org.bukkit.entity.SmallFireball;

import me.LoneSurvivor.Supernatural.Supernatural;
import me.LoneSurvivor.Supernatural.repositories.Constants;

public class DrainBall {
	public DrainBall(Supernatural supernatural, Constants constants, Player p) {
		supernatural.setCooldown(p.getUniqueId(), "Drain", supernatural.getConfig().getInt("Classes.Priest.ActiveAbilities.Drain.Cooldown"));
		supernatural.setMagic(p, supernatural.getMagic(p) - supernatural.getConfig().getInt("Classes.Priest.ActiveAbilities.Drain.Magic-Cost"), false);
		p.setFoodLevel(p.getFoodLevel() - supernatural.getConfig().getInt("Classes.Priest.ActiveAbilities.Drain.Food-Cost"));
		p.setHealth(p.getHealth() - supernatural.getConfig().getInt("Classes.Priest.ActiveAbilities.Drain.Health-Cost"));
    	SmallFireball projectile = p.launchProjectile(SmallFireball.class);
    	projectile.setIsIncendiary(false);
    	projectile.setCustomName(p.getUniqueId().toString() + ":Drain");
    	supernatural.getServer().getScheduler().scheduleSyncDelayedTask(supernatural, new Runnable() {
            public void run() {
            	projectile.remove();
            }
        }, ((long) Math.ceil(supernatural.FireballEstimateTimeFromDistance(supernatural.getConfig().getInt("Classes.Priest.ActiveAbilities.Drain.Range"))*20)));
	}
}
