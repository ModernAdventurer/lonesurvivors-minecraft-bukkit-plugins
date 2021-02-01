package me.LoneSurvivor.Supernatural.Classes.Demon.ActiveAbilities;

import org.bukkit.entity.LargeFireball;
import org.bukkit.entity.Player;

import me.LoneSurvivor.Supernatural.Supernatural;
import me.LoneSurvivor.Supernatural.repositories.Constants;

public class Fireball {
	public Fireball(Supernatural supernatural, Constants constants, Player p) {
		supernatural.setCooldown(p.getUniqueId(), "Fireball", supernatural.getConfig().getInt("Classes.Demon.ActiveAbilities.Fireball.Cooldown"));
		supernatural.setMagic(p, supernatural.getMagic(p) - supernatural.getConfig().getInt("Classes.Demon.ActiveAbilities.Fireball.Magic-Cost"), false);
		p.setFoodLevel(p.getFoodLevel() - supernatural.getConfig().getInt("Classes.Demon.ActiveAbilities.Fireball.Food-Cost"));
		p.setHealth(p.getHealth() - supernatural.getConfig().getInt("Classes.Demon.ActiveAbilities.Fireball.Health-Cost"));
		p.launchProjectile(LargeFireball.class);
	}
}
