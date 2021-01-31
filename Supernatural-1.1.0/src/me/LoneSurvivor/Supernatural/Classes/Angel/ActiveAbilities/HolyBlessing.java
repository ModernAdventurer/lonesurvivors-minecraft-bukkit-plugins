package me.LoneSurvivor.Supernatural.Classes.Angel.ActiveAbilities;

import org.bukkit.entity.Player;

import me.LoneSurvivor.Supernatural.Supernatural;
import me.LoneSurvivor.Supernatural.repositories.Constants;

public class HolyBlessing {
	public HolyBlessing(Supernatural supernatural, Constants constants, Player p) {
		supernatural.setCooldown(p.getUniqueId(), "HolyBlessing", supernatural.getConfig().getInt("Spells.Angel.HolyBlessing.Cooldown"));
		supernatural.setMagic(p, supernatural.getMagic(p) - supernatural.getConfig().getInt("Spells.Angel.HolyBlessing.Magic-Cost"), false);
		p.setFoodLevel(p.getFoodLevel() - supernatural.getConfig().getInt("Spells.Angel.HolyBlessing.Food-Cost"));
		p.setHealth(p.getHealth() - supernatural.getConfig().getInt("Spells.Angel.HolyBlessing.Health-Cost"));
		p.sendMessage("This Command is WIP");
	}
}
