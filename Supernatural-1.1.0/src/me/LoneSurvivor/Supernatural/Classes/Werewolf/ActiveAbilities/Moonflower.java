package me.LoneSurvivor.Supernatural.Classes.Werewolf.ActiveAbilities;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import me.LoneSurvivor.Supernatural.Supernatural;
import me.LoneSurvivor.Supernatural.repositories.Constants;

public class Moonflower {
	public Moonflower(Supernatural supernatural, Constants constants, Player p) {
    	if(supernatural.getConfig().getInt("recruitingItemLimit") != -1) {
    		if(supernatural.getRecruitingItems(p, "Moonflower") >= supernatural.getConfig().getInt("recruitingItemLimit")) {
    			p.sendMessage(ChatColor.translateAlternateColorCodes('&', supernatural.getConfig().getString("Messages.recruiting-item-limit").replaceAll("%prefix%", supernatural.getConfig().getString("Messages.prefix"))));
	    		return;
	    	}
    	}
		supernatural.setCooldown(p.getUniqueId(), "Moonflower", supernatural.getConfig().getInt("Classes.Werewolf.ActiveAbilities.Moonflower.Cooldown"));
		supernatural.setMagic(p, supernatural.getMagic(p) - supernatural.getConfig().getInt("Classes.Werewolf.ActiveAbilities.Moonflower.Magic-Cost"), false);
		p.setFoodLevel(p.getFoodLevel() - supernatural.getConfig().getInt("Classes.Werewolf.ActiveAbilities.Moonflower.Food-Cost"));
		p.setHealth(p.getHealth() - supernatural.getConfig().getInt("Classes.Werewolf.ActiveAbilities.Moonflower.Health-Cost"));
		supernatural.setRecruitingItems(p, "Moonflower", (supernatural.getRecruitingItems(p, "Moonflower") + 1));
		supernatural.addItemSafely(p, constants.getCustomItems().get("Moonflower"));
	}
}
