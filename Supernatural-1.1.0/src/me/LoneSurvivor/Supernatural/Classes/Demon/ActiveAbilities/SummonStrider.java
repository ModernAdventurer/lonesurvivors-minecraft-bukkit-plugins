package me.LoneSurvivor.Supernatural.Classes.Demon.ActiveAbilities;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Steerable;
import org.bukkit.entity.Strider;

import me.LoneSurvivor.Supernatural.Supernatural;
import me.LoneSurvivor.Supernatural.repositories.Constants;

public class SummonStrider {
	public SummonStrider(Supernatural supernatural, Constants constants, Player p) {
		supernatural.setCooldown(p.getUniqueId(), "SummonStrider", supernatural.getConfig().getInt("Spells.Demon.SummonStrider.Cooldown"));
		supernatural.setMagic(p, supernatural.getMagic(p) - supernatural.getConfig().getInt("Spells.Demon.SummonStrider.Magic-Cost"), false);
		p.setFoodLevel(p.getFoodLevel() - supernatural.getConfig().getInt("Spells.Demon.SummonStrider.Food-Cost"));
		p.setHealth(p.getHealth() - supernatural.getConfig().getInt("Spells.Demon.SummonStrider.Health-Cost"));
		Steerable strider = (Strider) p.getWorld().spawnEntity(p.getLocation(), EntityType.STRIDER);
        strider.setAdult();
        strider.setSaddle(true);
        strider.addPassenger(p);
	}
}
