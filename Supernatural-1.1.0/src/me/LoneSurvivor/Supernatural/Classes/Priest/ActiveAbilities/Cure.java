package me.LoneSurvivor.Supernatural.Classes.Priest.ActiveAbilities;

import org.bukkit.entity.Player;

import me.LoneSurvivor.Supernatural.Supernatural;
import me.LoneSurvivor.Supernatural.repositories.Constants;

public class Cure {
	public Cure(Supernatural supernatural, Constants constants, Player p) {
		supernatural.setCooldown(p.getUniqueId(), "Cure", supernatural.getConfig().getInt("Classes.Priest.ActiveAbilities.Cure.Cooldown"));
		supernatural.setMagic(p, supernatural.getMagic(p) - supernatural.getConfig().getInt("Classes.Priest.ActiveAbilities.Cure.Magic-Cost"), false);
		p.setFoodLevel(p.getFoodLevel() - supernatural.getConfig().getInt("Classes.Priest.ActiveAbilities.Cure.Food-Cost"));
		p.setHealth(p.getHealth() - supernatural.getConfig().getInt("Classes.Priest.ActiveAbilities.Cure.Health-Cost"));
		supernatural.addItemSafely(p, constants.getCustomItems().get("Cure"));
	}
}
