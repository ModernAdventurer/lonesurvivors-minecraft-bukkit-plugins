package me.LoneSurvivor.Supernatural.Classes.Vampire.ActiveAbilities;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import me.LoneSurvivor.Supernatural.Supernatural;
import me.LoneSurvivor.Supernatural.repositories.Constants;

public class Bloodrose {
	public Bloodrose(Supernatural supernatural, Constants constants, Player p) {
    	if(supernatural.getConfig().getInt("recruitingItemLimit") != -1) {
    		if(supernatural.getRecruitingItems(p, "Bloodrose") >= supernatural.getConfig().getInt("recruitingItemLimit")) {
    			p.sendMessage(ChatColor.translateAlternateColorCodes('&', supernatural.getConfig().getString("Messages.recruiting-item-limit").replaceAll("%prefix%", supernatural.getConfig().getString("Messages.prefix"))));
	    		return;
	    	}
    	}
    	supernatural.setCooldown(p.getUniqueId(), "Bloodrose", supernatural.getConfig().getInt("Spells.Vampire.Bloodrose.Cooldown"));
		supernatural.setMagic(p, supernatural.getMagic(p) - supernatural.getConfig().getInt("Spells.Vampire.Bloodrose.Cost"), false);
		supernatural.setRecruitingItems(p, supernatural.getRace(p), (supernatural.getRecruitingItems(p, "Bloodrose") + 1));
		supernatural.addItemSafely(p, constants.getCustomItems().get("Bloodrose"));
		if(p.getHealth()>10) {
			p.setHealth(p.getHealth()-10);
		} else {
			p.setHealth(0);
		}
	}
}
