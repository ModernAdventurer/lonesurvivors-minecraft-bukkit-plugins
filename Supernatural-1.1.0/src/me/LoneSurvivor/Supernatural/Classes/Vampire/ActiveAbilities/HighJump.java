package me.LoneSurvivor.Supernatural.Classes.Vampire.ActiveAbilities;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import me.LoneSurvivor.Supernatural.Supernatural;
import me.LoneSurvivor.Supernatural.repositories.Constants;

public class HighJump {
	public HighJump(Supernatural supernatural, Constants constants, Player p) {
		//Block below
		Location l = p.getLocation();
		l.setY(l.getY()-1.25);
		if(l.getBlock().getType() != Material.AIR) {
			supernatural.setCooldown(p.getUniqueId(), "HighJump", supernatural.getConfig().getInt("Classes.Vampire.ActiveAbilities.HighJump.Cooldown"));
			supernatural.setMagic(p, supernatural.getMagic(p) - supernatural.getConfig().getInt("Classes.Vampire.ActiveAbilities.HighJump.Magic-Cost"), false);
			p.setFoodLevel(p.getFoodLevel() - supernatural.getConfig().getInt("Classes.Vampire.ActiveAbilities.HighJump.Food-Cost"));
			p.setHealth(p.getHealth() - supernatural.getConfig().getInt("Classes.Vampire.ActiveAbilities.HighJump.Health-Cost"));
			Vector vec = p.getVelocity();
			p.setVelocity(vec.setY(vec.getY()+1));
		}
	}
}
