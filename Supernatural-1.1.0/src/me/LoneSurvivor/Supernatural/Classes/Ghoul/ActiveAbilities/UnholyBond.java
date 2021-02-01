package me.LoneSurvivor.Supernatural.Classes.Ghoul.ActiveAbilities;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import me.LoneSurvivor.Supernatural.Supernatural;
import me.LoneSurvivor.Supernatural.repositories.Constants;

public class UnholyBond {
	public UnholyBond(Supernatural supernatural, Constants constants, Player p, LivingEntity t) {
		supernatural.setCooldown(p.getUniqueId(), "UnholyBond", supernatural.getConfig().getInt("Classes.Ghoul.ActiveAbilities.UnholyBond.Cooldown"));
		supernatural.setMagic(p, supernatural.getMagic(p) - supernatural.getConfig().getInt("Classes.Ghoul.ActiveAbilities.UnholyBond.Magic-Cost"), false);
		p.setFoodLevel(p.getFoodLevel() - supernatural.getConfig().getInt("Classes.Ghoul.ActiveAbilities.UnholyBond.Food-Cost"));
		p.setHealth(p.getHealth() - supernatural.getConfig().getInt("Classes.Ghoul.ActiveAbilities.UnholyBond.Health-Cost"));
		Map<UUID, Map<UUID, Integer>> UnholyBond = supernatural.getUnholyBond();
		Map<UUID, Integer> TaggedEntities = new HashMap<UUID, Integer>();
		if(UnholyBond.containsKey(p.getUniqueId())) TaggedEntities = UnholyBond.get(p.getUniqueId());
		TaggedEntities.put(t.getUniqueId(), 3000); //2.5 mins
		UnholyBond.put(p.getUniqueId(), TaggedEntities);
		supernatural.setUnholyBond(UnholyBond);
	}
}
