package me.LoneSurvivor.Supernatural.Classes.Ghoul.ActiveAbilities;

import java.util.Random;

import org.bukkit.entity.Player;

import me.LoneSurvivor.Supernatural.Supernatural;
import me.LoneSurvivor.Supernatural.repositories.Constants;

public class SummonMonster {
	public SummonMonster(Supernatural supernatural, Constants constants, Player p) {
		supernatural.setCooldown(p.getUniqueId(), "SummonMonster", supernatural.getConfig().getInt("Classes.Ghoul.ActiveAbilities.SummonMonster.Cooldown"));
		supernatural.setMagic(p, supernatural.getMagic(p) - supernatural.getConfig().getInt("Classes.Ghoul.ActiveAbilities.SummonMonster.Magic-Cost"), false);
		p.setFoodLevel(p.getFoodLevel() - supernatural.getConfig().getInt("Classes.Ghoul.ActiveAbilities.SummonMonster.Food-Cost"));
		p.setHealth(p.getHealth() - supernatural.getConfig().getInt("Classes.Ghoul.ActiveAbilities.SummonMonster.Health-Cost"));
		Random rand = new Random();
		int random = (int) Math.floor((rand.nextFloat()*constants.getMonsters().size()));
		p.getWorld().spawnEntity(p.getLocation(), constants.getMonsters().get(random));
	}
}
