package me.LoneSurvivor.Supernatural.Classes.Demon.ActiveAbilities;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.LoneSurvivor.Supernatural.Supernatural;
import me.LoneSurvivor.Supernatural.repositories.Constants;

public class Snare {
	public Snare(Supernatural supernatural, Constants constants, Player p, LivingEntity t) {
		supernatural.setCooldown(p.getUniqueId(), "Snare", supernatural.getConfig().getInt("Classes.Demon.ActiveAbilities.Snare.Cooldown"));
		supernatural.setMagic(p, supernatural.getMagic(p) - supernatural.getConfig().getInt("Classes.Demon.ActiveAbilities.Snare.Magic-Cost"), false);
		p.setFoodLevel(p.getFoodLevel() - supernatural.getConfig().getInt("Classes.Demon.ActiveAbilities.Snare.Food-Cost"));
		p.setHealth(p.getHealth() - supernatural.getConfig().getInt("Classes.Demon.ActiveAbilities.Snare.Health-Cost"));
		t.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 200, 128));
		t.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 200, 6));
	}
}
