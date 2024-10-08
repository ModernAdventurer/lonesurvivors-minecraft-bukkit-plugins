package me.LoneSurvivor.Supernatural.Classes.Priest.ActiveAbilities;

import org.bukkit.entity.Player;
import org.bukkit.entity.SmallFireball;

import me.LoneSurvivor.Supernatural.Supernatural;
import me.LoneSurvivor.Supernatural.repositories.Constants;

public class ExorciseBall {
	public ExorciseBall(Supernatural supernatural, Constants constants, Player p) {
		supernatural.setCooldown(p.getUniqueId(), "Exorcise", supernatural.getConfig().getInt("Classes.Priest.ActiveAbilities.Exorcise.Cooldown"));
		supernatural.setMagic(p, supernatural.getMagic(p) - supernatural.getConfig().getInt("Classes.Priest.ActiveAbilities.Exorcise.Magic-Cost"), false);
		p.setFoodLevel(p.getFoodLevel() - supernatural.getConfig().getInt("Classes.Priest.ActiveAbilities.Exorcise.Food-Cost"));
		p.setHealth(p.getHealth() - supernatural.getConfig().getInt("Classes.Priest.ActiveAbilities.Exorcise.Health-Cost"));
    	SmallFireball projectile = p.launchProjectile(SmallFireball.class);
    	projectile.setIsIncendiary(false);
    	projectile.setCustomName(p.getUniqueId().toString() + ":Exorcise");
    	supernatural.getServer().getScheduler().scheduleSyncDelayedTask(supernatural, new Runnable() {
            public void run() {
            	projectile.remove();
            }
        }, ((long) Math.ceil(supernatural.FireballEstimateTimeFromDistance(supernatural.getConfig().getInt("Classes.Priest.ActiveAbilities.Exorcise.Range"))*20)));
	}
}
