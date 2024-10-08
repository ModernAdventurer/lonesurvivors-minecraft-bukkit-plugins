package me.LoneSurvivor.Supernatural.Classes.Demon.ActiveAbilities;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import me.LoneSurvivor.Supernatural.Supernatural;
import me.LoneSurvivor.Supernatural.repositories.Constants;

public class Hellish {
	public Hellish(Supernatural supernatural, Constants constants, Player p) {
    	if(supernatural.getConfig().getInt("recruitingItemLimit") != -1) {
    		if(supernatural.getRecruitingItems(p, "Hellish") >= supernatural.getConfig().getInt("recruitingItemLimit")) {
    			p.sendMessage(ChatColor.translateAlternateColorCodes('&', supernatural.getConfig().getString("Messages.recruiting-item-limit").replaceAll("%prefix%", supernatural.getConfig().getString("Messages.prefix"))));
	    		return;
	    	}
    	}
		supernatural.setCooldown(p.getUniqueId(), "Hellish", supernatural.getConfig().getInt("Classes.Demon.ActiveAbilities.Hellish.Cooldown"));
		supernatural.setMagic(p, supernatural.getMagic(p) - supernatural.getConfig().getInt("Classes.Demon.ActiveAbilities.Hellish.Magic-Cost"), false);
		p.setFoodLevel(p.getFoodLevel() - supernatural.getConfig().getInt("Classes.Demon.ActiveAbilities.Hellish.Food-Cost"));
		p.setHealth(p.getHealth() - supernatural.getConfig().getInt("Classes.Demon.ActiveAbilities.Hellish.Health-Cost"));
    	supernatural.setRecruitingItems(p, "Hellish", (supernatural.getRecruitingItems(p, "Hellish") + 1));
    	supernatural.addItemSafely(p, constants.getCustomItems().get("Hellish"));
	}
}
