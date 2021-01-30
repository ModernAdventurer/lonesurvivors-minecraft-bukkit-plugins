package me.LoneSurvivor.Supernatural.Classes.Demon.ActiveAbilities;

import org.bukkit.entity.LargeFireball;
import org.bukkit.entity.Player;

import me.LoneSurvivor.Supernatural.Supernatural;
import me.LoneSurvivor.Supernatural.repositories.Constants;

public class Fireball {
	public Fireball(Supernatural supernatural, Constants constants, Player p) {
		supernatural.setCooldown(p.getUniqueId(), "Fireball", supernatural.getConfig().getInt("Spells.Demon.Fireball.Cooldown"));
		supernatural.setMagic(p, supernatural.getMagic(p) - supernatural.getConfig().getInt("Spells.Demon.Fireball.Cost"), false);
		p.launchProjectile(LargeFireball.class);
	}
}
