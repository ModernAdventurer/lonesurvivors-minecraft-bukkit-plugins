package me.LoneSurvivor.Supernatural.Classes.Necromancer.ActiveAbilities;

import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import me.LoneSurvivor.Supernatural.Supernatural;
import me.LoneSurvivor.Supernatural.repositories.Constants;

public class HealUndead {
	public HealUndead(Supernatural supernatural, Constants constants, Player p) {
		supernatural.setCooldown(p.getUniqueId(), "HealUndead", supernatural.getConfig().getInt("Spells.Necromancer.HealUndead.Cooldown"));
		supernatural.setMagic(p, supernatural.getMagic(p) - supernatural.getConfig().getInt("Spells.Necromancer.HealUndead.Magic-Cost"), false);
		p.setFoodLevel(p.getFoodLevel() - supernatural.getConfig().getInt("Spells.Necromancer.HealUndead.Food-Cost"));
		p.setHealth(p.getHealth() - supernatural.getConfig().getInt("Spells.Necromancer.HealUndead.Health-Cost"));
		for(Entity e : p.getNearbyEntities(10, 10, 10)) {
			if(e instanceof LivingEntity) {
				LivingEntity le = (LivingEntity) e;
				if(constants.getUndead().contains(e.getType())) {
					double health = le.getHealth() + (le.getAttribute(Attribute.GENERIC_MAX_HEALTH).getDefaultValue()*0.35);
					if(health>le.getAttribute(Attribute.GENERIC_MAX_HEALTH).getDefaultValue()) {
						health = le.getAttribute(Attribute.GENERIC_MAX_HEALTH).getDefaultValue();
					}
					le.setHealth(health);
				}
			}
		}
	}
}
