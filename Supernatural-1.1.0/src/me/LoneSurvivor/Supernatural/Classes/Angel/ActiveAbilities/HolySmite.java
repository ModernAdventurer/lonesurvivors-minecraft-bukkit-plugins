package me.LoneSurvivor.Supernatural.Classes.Angel.ActiveAbilities;

import org.bukkit.entity.Player;

import me.LoneSurvivor.Supernatural.Supernatural;
import me.LoneSurvivor.Supernatural.repositories.Constants;

public class HolySmite {
	public HolySmite(Supernatural supernatural, Constants constants, Player p) {
		supernatural.setCooldown(p.getUniqueId(), "HolySmite", supernatural.getConfig().getInt("Classes.Angel.ActiveAbilities.HolySmite.Cooldown"));
		supernatural.setMagic(p, supernatural.getMagic(p) - supernatural.getConfig().getInt("Classes.Angel.ActiveAbilities.HolySmite.Magic-Cost"), false);
		p.setFoodLevel(p.getFoodLevel() - supernatural.getConfig().getInt("Classes.Angel.ActiveAbilities.HolySmite.Food-Cost"));
		p.setHealth(p.getHealth() - supernatural.getConfig().getInt("Classes.Angel.ActiveAbilities.HolySmite.Health-Cost"));
		p.sendMessage("This Command is WIP");
	}
}
