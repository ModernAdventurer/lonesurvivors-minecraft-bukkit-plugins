package me.LoneSurvivor.Supernatural.Classes.Vampire.ActiveAbilities;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import me.LoneSurvivor.Supernatural.Supernatural;
import me.LoneSurvivor.Supernatural.repositories.Constants;

public class Bloodvial {
	public Bloodvial(Supernatural supernatural, Constants constants, Player p) {
		if(p.getFoodLevel()>6) {
			p.setFoodLevel(p.getFoodLevel()-6);
			supernatural.setCooldown(p.getUniqueId(), "Bloodvial", supernatural.getConfig().getInt("Classes.Vampire.ActiveAbilities.Bloodvial.Cooldown"));
			supernatural.setMagic(p, supernatural.getMagic(p) - supernatural.getConfig().getInt("Classes.Vampire.ActiveAbilities.Bloodvial.Magic-Cost"), false);
			p.setFoodLevel(p.getFoodLevel() - supernatural.getConfig().getInt("Classes.Vampire.ActiveAbilities.Bloodvial.Food-Cost"));
			p.setHealth(p.getHealth() - supernatural.getConfig().getInt("Classes.Vampire.ActiveAbilities.Bloodvial.Health-Cost"));
			p.getInventory().addItem(constants.getCustomItems().get("BloodVial"));
		} else {
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', supernatural.getConfig().getString("Messages.insufficient-hunger").replaceAll("%prefix%", supernatural.getConfig().getString("Messages.prefix"))));
			return;
		}
	}
}
