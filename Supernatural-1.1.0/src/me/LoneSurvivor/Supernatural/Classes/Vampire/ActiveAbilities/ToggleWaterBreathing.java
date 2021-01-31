package me.LoneSurvivor.Supernatural.Classes.Vampire.ActiveAbilities;

import org.bukkit.entity.Player;

import me.LoneSurvivor.Supernatural.Supernatural;
import me.LoneSurvivor.Supernatural.repositories.Constants;

public class ToggleWaterBreathing {
	public ToggleWaterBreathing(Supernatural supernatural, Constants constants, Player p) {
		supernatural.setCooldown(p.getUniqueId(), "ToggleWaterBreathing", supernatural.getConfig().getInt("Spells.Vampire.ToggleWaterBreathing.Cooldown"));
		supernatural.setMagic(p, supernatural.getMagic(p) - supernatural.getConfig().getInt("Spells.Vampire.ToggleWaterBreathing.Magic-Cost"), false);
		p.setFoodLevel(p.getFoodLevel() - supernatural.getConfig().getInt("Spells.Vampire.ToggleWaterBreathing.Food-Cost"));
		p.setHealth(p.getHealth() - supernatural.getConfig().getInt("Spells.Vampire.ToggleWaterBreathing.Health-Cost"));
		if(!supernatural.getWaterBreathing().containsKey(p)) {
			supernatural.setWaterBreathing(p, false);
		}
		supernatural.setWaterBreathing(p, !supernatural.getWaterBreathing(p));
	}
}