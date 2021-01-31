package me.LoneSurvivor.Supernatural.Classes.Vampire.ActiveAbilities;

import org.bukkit.entity.Player;

import me.LoneSurvivor.Supernatural.Supernatural;
import me.LoneSurvivor.Supernatural.repositories.Constants;

public class ToggleRegen {
	public ToggleRegen(Supernatural supernatural, Constants constants, Player p) {
		supernatural.setCooldown(p.getUniqueId(), "ToggleRegeneration", supernatural.getConfig().getInt("Spells.Vampire.ToggleRegeneration.Cooldown"));
		supernatural.setMagic(p, supernatural.getMagic(p) - supernatural.getConfig().getInt("Spells.Vampire.ToggleRegeneration.Magic-Cost"), false);
		p.setFoodLevel(p.getFoodLevel() - supernatural.getConfig().getInt("Spells.Vampire.ToggleRegeneration.Food-Cost"));
		p.setHealth(p.getHealth() - supernatural.getConfig().getInt("Spells.Vampire.ToggleRegeneration.Health-Cost"));
		if(!supernatural.getRegeneration().containsKey(p)) {
			supernatural.setRegeneration(p, false);
		}
		supernatural.setRegeneration(p, !supernatural.getRegeneration(p));
	}
}
