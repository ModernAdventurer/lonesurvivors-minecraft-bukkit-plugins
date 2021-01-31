package me.LoneSurvivor.Supernatural.Classes.Ghoul.ActiveAbilities;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import me.LoneSurvivor.Supernatural.Supernatural;
import me.LoneSurvivor.Supernatural.repositories.Constants;

public class UnholyBond {
	public UnholyBond(Supernatural supernatural, Constants constants, Player p, LivingEntity t) {
		supernatural.setCooldown(p.getUniqueId(), "UnholyBond", supernatural.getConfig().getInt("Spells.Ghoul.UnholyBond.Cooldown"));
		supernatural.setMagic(p, supernatural.getMagic(p) - supernatural.getConfig().getInt("Spells.Ghoul.UnholyBond.Magic-Cost"), false);
		p.setFoodLevel(p.getFoodLevel() - supernatural.getConfig().getInt("Spells.Ghoul.UnholyBond.Food-Cost"));
		p.setHealth(p.getHealth() - supernatural.getConfig().getInt("Spells.Ghoul.UnholyBond.Health-Cost"));
		Map<Player, Map<LivingEntity, Integer>> UnholyBond = supernatural.getUnholyBond();
		Map<LivingEntity, Integer> TaggedEntities = new HashMap<LivingEntity, Integer>();
		if(UnholyBond.containsKey(p)) TaggedEntities = UnholyBond.get(p);
		TaggedEntities.put(t, 3000); //2.5 mins
		UnholyBond.put(p, TaggedEntities);
		supernatural.setUnholyBond(UnholyBond);
	}
}
