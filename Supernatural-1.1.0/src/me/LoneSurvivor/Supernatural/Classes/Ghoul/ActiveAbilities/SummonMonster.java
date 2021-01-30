package me.LoneSurvivor.Supernatural.Classes.Ghoul.ActiveAbilities;

import java.util.Random;

import org.bukkit.entity.Player;

import me.LoneSurvivor.Supernatural.Supernatural;
import me.LoneSurvivor.Supernatural.repositories.Constants;

public class SummonMonster {
	public SummonMonster(Supernatural supernatural, Constants constants, Player p) {
		supernatural.setCooldown(p.getUniqueId(), "SummonMonster", supernatural.getConfig().getInt("Spells.Ghoul.SummonMonster.Cooldown"));
		supernatural.setMagic(p, supernatural.getMagic(p) - supernatural.getConfig().getInt("Spells.Ghoul.SummonMonster.Cost"), false);
		Random rand = new Random();
		int random = (int) Math.floor((rand.nextFloat()*constants.getMonsters().size()));
		p.getWorld().spawnEntity(p.getLocation(), constants.getMonsters().get(random));
	}
}
