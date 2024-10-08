package me.LoneSurvivor.Supernatural.Classes.WitchHunter.ActiveAbilities;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import me.LoneSurvivor.Supernatural.Supernatural;
import me.LoneSurvivor.Supernatural.repositories.Constants;

public class BookOfWitchHunter {
	public BookOfWitchHunter(Supernatural supernatural, Constants constants, Player p) {
    	if(supernatural.getConfig().getInt("recruitingItemLimit") != -1) {
    		if(supernatural.getRecruitingItems(p, "BookOfWitchHunter") >= supernatural.getConfig().getInt("recruitingItemLimit")) {
    			p.sendMessage(ChatColor.translateAlternateColorCodes('&', supernatural.getConfig().getString("Messages.recruiting-item-limit").replaceAll("%prefix%", supernatural.getConfig().getString("Messages.prefix"))));
	    		return;
	    	}
    	}
		supernatural.setCooldown(p.getUniqueId(), "BookOfWitchHunter", supernatural.getConfig().getInt("Classes.WitchHunter.ActiveAbilities.BookOfWitchHunter.Cooldown"));
		supernatural.setMagic(p, supernatural.getMagic(p) - supernatural.getConfig().getInt("Classes.WitchHunter.ActiveAbilities.BookOfWitchHunter.Magic-Cost"), false);
		p.setFoodLevel(p.getFoodLevel() - supernatural.getConfig().getInt("Classes.WitchHunter.ActiveAbilities.BookOfWitchHunter.Food-Cost"));
		p.setHealth(p.getHealth() - supernatural.getConfig().getInt("Classes.WitchHunter.ActiveAbilities.BookOfWitchHunter.Health-Cost"));
    	supernatural.setRecruitingItems(p, "BookOfWitchHunter", (supernatural.getRecruitingItems(p, "BookOfWitchHunter") + 1));
    	supernatural.addItemSafely(p, constants.getCustomItems().get("BookOfWitchHunter"));
	}
}
