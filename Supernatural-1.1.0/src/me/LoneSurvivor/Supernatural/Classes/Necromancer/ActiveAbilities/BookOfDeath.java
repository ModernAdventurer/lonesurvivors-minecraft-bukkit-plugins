package me.LoneSurvivor.Supernatural.Classes.Necromancer.ActiveAbilities;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import me.LoneSurvivor.Supernatural.Supernatural;
import me.LoneSurvivor.Supernatural.repositories.Constants;

public class BookOfDeath {
	public BookOfDeath(Supernatural supernatural, Constants constants, Player p) {
    	if(supernatural.getConfig().getInt("recruitingItemLimit") != -1) {
    		if(supernatural.getRecruitingItems(p, "BookOfDeath") >= supernatural.getConfig().getInt("recruitingItemLimit")) {
    			p.sendMessage(ChatColor.translateAlternateColorCodes('&', supernatural.getConfig().getString("Messages.recruiting-item-limit").replaceAll("%prefix%", supernatural.getConfig().getString("Messages.prefix"))));
	    		return;
	    	}
    	}
    	supernatural.setCooldown(p.getUniqueId(), "BookOfDeath", supernatural.getConfig().getInt("Spells.Necromancer.BookOfDeath.Cooldown"));
    	supernatural.setMagic(p, supernatural.getMagic(p) - supernatural.getConfig().getInt("Spells.Necromancer.BookOfDeath.Cost"), false);
    	supernatural.setRecruitingItems(p, supernatural.getRace(p), (supernatural.getRecruitingItems(p, "BookOfDeath") + 1));
    	supernatural.addItemSafely(p, constants.getCustomItems().get("BookOfDeath"));
		if(p.getHealth()>10) {
			p.setHealth(p.getHealth()-10);
		} else {
			p.setHealth(0);
		}
	}
}
