package me.LoneSurvivor.Supernatural.Classes.Angel.ActiveAbilities;

import org.bukkit.entity.Player;

import me.LoneSurvivor.Supernatural.Supernatural;
import me.LoneSurvivor.Supernatural.repositories.Constants;

public class Taunt {
	public Taunt(Supernatural supernatural, Constants constants, Player p) {
		supernatural.setCooldown(p.getUniqueId(), "Taunt", supernatural.getConfig().getInt("Classes.Angel.ActiveAbilities.Taunt.Cooldown"));
		supernatural.setMagic(p, supernatural.getMagic(p) - supernatural.getConfig().getInt("Classes.Angel.ActiveAbilities.Taunt.Magic-Cost"), false);
		p.setFoodLevel(p.getFoodLevel() - supernatural.getConfig().getInt("Classes.Angel.ActiveAbilities.Taunt.Food-Cost"));
		p.setHealth(p.getHealth() - supernatural.getConfig().getInt("Classes.Angel.ActiveAbilities.Taunt.Health-Cost"));
		p.sendMessage("This Command is WIP");
	}
}
