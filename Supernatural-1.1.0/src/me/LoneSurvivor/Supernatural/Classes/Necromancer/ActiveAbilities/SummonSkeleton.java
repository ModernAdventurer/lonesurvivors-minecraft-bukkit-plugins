package me.LoneSurvivor.Supernatural.Classes.Necromancer.ActiveAbilities;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.persistence.PersistentDataType;

import me.LoneSurvivor.Supernatural.Supernatural;
import me.LoneSurvivor.Supernatural.repositories.Constants;

public class SummonSkeleton {
	public SummonSkeleton(Supernatural supernatural, Constants constants, Player p) {
		supernatural.setCooldown(p.getUniqueId(), "SummonSkeleton", supernatural.getConfig().getInt("Spells.Necromancer.SummonSkeleton.Cooldown"));
		supernatural.setMagic(p, supernatural.getMagic(p) - supernatural.getConfig().getInt("Spells.Necromancer.SummonSkeleton.Magic-Cost"), false);
		p.setFoodLevel(p.getFoodLevel() - supernatural.getConfig().getInt("Spells.Necromancer.SummonSkeleton.Food-Cost"));
		p.setHealth(p.getHealth() - supernatural.getConfig().getInt("Spells.Necromancer.SummonSkeleton.Health-Cost"));
		Skeleton skeleton = (Skeleton) p.getWorld().spawnEntity(p.getLocation(), EntityType.SKELETON);
		skeleton.setCustomName(p.getName() + "'s Follower");
		skeleton.setCustomNameVisible(true);
		skeleton.getPersistentDataContainer().set(new NamespacedKey(supernatural, "Leader"), PersistentDataType.STRING, p.getUniqueId().toString());
		skeleton.getPersistentDataContainer().set(new NamespacedKey(supernatural, "Following"), PersistentDataType.INTEGER, 1);
	}
}
