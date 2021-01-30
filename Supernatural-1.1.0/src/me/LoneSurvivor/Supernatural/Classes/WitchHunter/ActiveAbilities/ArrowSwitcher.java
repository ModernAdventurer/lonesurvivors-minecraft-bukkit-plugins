package me.LoneSurvivor.Supernatural.Classes.WitchHunter.ActiveAbilities;

import org.bukkit.entity.Player;

import me.LoneSurvivor.Supernatural.Supernatural;
import me.LoneSurvivor.Supernatural.repositories.Constants;

public class ArrowSwitcher {
	public ArrowSwitcher(Supernatural supernatural, Constants constants, Player p) {
		supernatural.setCooldown(p.getUniqueId(), "ArrowSwitcher", supernatural.getConfig().getInt("Spells.WitchHunter.ArrowSwitcher.Cooldown"));
		supernatural.setMagic(p, supernatural.getMagic(p) - supernatural.getConfig().getInt("Spells.WitchHunter.ArrowSwitcher.Cost"), false);
		supernatural.advanceArrowType(p);
	}
}
