package me.LoneSurvivor.Supernatural.Classes.Vampire.ActiveAbilities;

import org.bukkit.entity.Player;

import me.LoneSurvivor.Supernatural.Supernatural;
import me.LoneSurvivor.Supernatural.repositories.Constants;

public class ToggleRegen {
	public ToggleRegen(Supernatural supernatural, Constants constants, Player p) {
		supernatural.setCooldown(p.getUniqueId(), "ToggleRegeneration", supernatural.getConfig().getInt("Classes.Vampire.ActiveAbilities.ToggleRegeneration.Cooldown"));
		supernatural.setMagic(p, supernatural.getMagic(p) - supernatural.getConfig().getInt("Classes.Vampire.ActiveAbilities.ToggleRegeneration.Magic-Cost"), false);
		p.setFoodLevel(p.getFoodLevel() - supernatural.getConfig().getInt("Classes.Vampire.ActiveAbilities.ToggleRegeneration.Food-Cost"));
		p.setHealth(p.getHealth() - supernatural.getConfig().getInt("Classes.Vampire.ActiveAbilities.ToggleRegeneration.Health-Cost"));
		if(!supernatural.getRegeneration().containsKey(p)) {
			supernatural.setRegeneration(p, false);
		}
		supernatural.setRegeneration(p, !supernatural.getRegeneration(p));
	}
}
