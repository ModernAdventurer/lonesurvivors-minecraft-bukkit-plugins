package me.LoneSurvivor.Supernatural.Classes.Priest.ActiveAbilities;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.entity.SmallFireball;

import me.LoneSurvivor.Supernatural.Supernatural;
import me.LoneSurvivor.Supernatural.repositories.Constants;

public class BanishBall {
	public BanishBall(Supernatural supernatural, Constants constants, Player p) {
		if(supernatural.getBanishLocation() == null) {
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', supernatural.getConfig().getString("Messages.no-banish-location").replaceAll("%prefix%", supernatural.getConfig().getString("Messages.prefix"))));
			return;
		}
		supernatural.setCooldown(p.getUniqueId(), "Banish", supernatural.getConfig().getInt("Classes.Priest.ActiveAbilities.Banish.Cooldown"));
		supernatural.setMagic(p, supernatural.getMagic(p) - supernatural.getConfig().getInt("Classes.Priest.ActiveAbilities.Banish.Magic-Cost"), false);
		p.setFoodLevel(p.getFoodLevel() - supernatural.getConfig().getInt("Classes.Priest.ActiveAbilities.Banish.Food-Cost"));
		p.setHealth(p.getHealth() - supernatural.getConfig().getInt("Classes.Priest.ActiveAbilities.Banish.Health-Cost"));
    	SmallFireball projectile = p.launchProjectile(SmallFireball.class);
    	projectile.setIsIncendiary(false);
    	projectile.setCustomName(p.getUniqueId().toString() + ":Banish");
    	supernatural.getServer().getScheduler().scheduleSyncDelayedTask(supernatural, new Runnable() {
            public void run() {
            	projectile.remove();
            }
        }, ((long) Math.ceil(supernatural.FireballEstimateTimeFromDistance(supernatural.getConfig().getInt("Classes.Priest.ActiveAbilities.Banish.Range"))*20)));
	}
}
