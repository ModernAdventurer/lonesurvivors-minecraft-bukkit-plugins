package me.LoneSurvivor.Supernatural.Classes.Demon.ActiveAbilities;

import org.bukkit.entity.Player;

import me.LoneSurvivor.Supernatural.Supernatural;
import me.LoneSurvivor.Supernatural.repositories.Constants;

public class Explosion {
	public Explosion(Supernatural supernatural, Constants constants, Player p) {
		supernatural.setCooldown(p.getUniqueId(), "Explosion", supernatural.getConfig().getInt("Spells.Demon.Explosion.Cooldown"));
		supernatural.setMagic(p, supernatural.getMagic(p) - supernatural.getConfig().getInt("Spells.Demon.Explosion.Magic-Cost"), false);
		p.setFoodLevel(p.getFoodLevel() - supernatural.getConfig().getInt("Spells.Demon.Explosion.Food-Cost"));
		p.setHealth(p.getHealth() - supernatural.getConfig().getInt("Spells.Demon.Explosion.Health-Cost"));
        p.getWorld().createExplosion(p.getLocation(), 2, true);
	}
}
