package me.LoneSurvivor.Supernatural.Classes.Necromancer.ActiveAbilities;

import java.util.Random;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;

import me.LoneSurvivor.Supernatural.Supernatural;
import me.LoneSurvivor.Supernatural.repositories.Constants;

public class SummonUndead {
	public SummonUndead(Supernatural supernatural, Constants constants, Player p) {
		supernatural.setCooldown(p.getUniqueId(), "SummonUndead", supernatural.getConfig().getInt("Classes.Necromancer.ActiveAbilities.SummonUndead.Cooldown"));
		supernatural.setMagic(p, supernatural.getMagic(p) - supernatural.getConfig().getInt("Classes.Necromancer.ActiveAbilities.SummonUndead.Magic-Cost"), false);
		p.setFoodLevel(p.getFoodLevel() - supernatural.getConfig().getInt("Classes.Necromancer.ActiveAbilities.SummonUndead.Food-Cost"));
		p.setHealth(p.getHealth() - supernatural.getConfig().getInt("Classes.Necromancer.ActiveAbilities.SummonUndead.Health-Cost"));
		Random rand = new Random();
		int random = (int) Math.floor((rand.nextFloat()*constants.getUndead().size()));
		Monster entity = (Monster) p.getWorld().spawnEntity(p.getLocation(), constants.getUndead().get(random));
		entity.setCustomName(p.getName() + "'s Follower");
		entity.setCustomNameVisible(true);
		entity.getPersistentDataContainer().set(new NamespacedKey(supernatural, "Leader"), PersistentDataType.STRING, p.getUniqueId().toString());
		entity.getPersistentDataContainer().set(new NamespacedKey(supernatural, "Following"), PersistentDataType.INTEGER, 1);
	}
}
