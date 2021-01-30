package me.LoneSurvivor.Supernatural.Classes.Angel.ActiveAbilities;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.LoneSurvivor.Supernatural.Supernatural;
import me.LoneSurvivor.Supernatural.repositories.Constants;

public class Wings {
	public Wings(Supernatural supernatural, Constants constants, Player p) {
		supernatural.setCooldown(p.getUniqueId(), "Wings", supernatural.getConfig().getInt("Spells.Angel.Wings.Cooldown"));
		supernatural.setMagic(p, supernatural.getMagic(p) - supernatural.getConfig().getInt("Spells.Angel.Wings.Cost"), false);
		p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, 400, 1));
	}
}
