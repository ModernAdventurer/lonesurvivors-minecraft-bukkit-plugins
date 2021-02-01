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
		supernatural.setCooldown(p.getUniqueId(), "BookOfDeath", supernatural.getConfig().getInt("Classes.Necromancer.ActiveAbilities.BookOfDeath.Cooldown"));
		supernatural.setMagic(p, supernatural.getMagic(p) - supernatural.getConfig().getInt("Classes.Necromancer.ActiveAbilities.BookOfDeath.Magic-Cost"), false);
		p.setFoodLevel(p.getFoodLevel() - supernatural.getConfig().getInt("Classes.Necromancer.ActiveAbilities.BookOfDeath.Food-Cost"));
		p.setHealth(p.getHealth() - supernatural.getConfig().getInt("Classes.Necromancer.ActiveAbilities.BookOfDeath.Health-Cost"));
    	supernatural.setRecruitingItems(p, "BookOfDeath", (supernatural.getRecruitingItems(p, "BookOfDeath") + 1));
    	supernatural.addItemSafely(p, constants.getCustomItems().get("BookOfDeath"));
	}
}
