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
    	supernatural.setCooldown(p.getUniqueId(), "Moonflower", supernatural.getConfig().getInt("Spells.Werewolf.Moonflower.Cooldown"));
    	supernatural.setMagic(p, supernatural.getMagic(p) - supernatural.getConfig().getInt("Spells.Werewolf.Moonflower.Cost"), false);
		supernatural.setRecruitingItems(p, supernatural.getRace(p), (supernatural.getRecruitingItems(p, "Moonflower") + 1));
		supernatural.addItemSafely(p, constants.getCustomItems().get("Moonflower"));
		if(p.getHealth()>10) {
			p.setHealth(p.getHealth()-10);
		} else {
			p.setHealth(0);
		}
	}
}
