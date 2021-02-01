package me.LoneSurvivor.Supernatural.Classes.Ghoul.ActiveAbilities;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import me.LoneSurvivor.Supernatural.Supernatural;
import me.LoneSurvivor.Supernatural.repositories.Constants;

public class Ghoulish {
	public Ghoulish(Supernatural supernatural, Constants constants, Player p) {
    	if(supernatural.getConfig().getInt("recruitingItemLimit") != -1) {
    		if(supernatural.getRecruitingItems(p, "Ghoulish") >= supernatural.getConfig().getInt("recruitingItemLimit")) {
    			p.sendMessage(ChatColor.translateAlternateColorCodes('&', supernatural.getConfig().getString("Messages.recruiting-item-limit").replaceAll("%prefix%", supernatural.getConfig().getString("Messages.prefix"))));
	    		return;
	    	}
    	}
		supernatural.setCooldown(p.getUniqueId(), "Ghoulish", supernatural.getConfig().getInt("Classes.Ghoul.ActiveAbilities.Ghoulish.Cooldown"));
		supernatural.setMagic(p, supernatural.getMagic(p) - supernatural.getConfig().getInt("Classes.Ghoul.ActiveAbilities.Ghoulish.Magic-Cost"), false);
		p.setFoodLevel(p.getFoodLevel() - supernatural.getConfig().getInt("Classes.Ghoul.ActiveAbilities.Ghoulish.Food-Cost"));
		p.setHealth(p.getHealth() - supernatural.getConfig().getInt("Classes.Ghoul.ActiveAbilities.Ghoulish.Health-Cost"));
		supernatural.setRecruitingItems(p, "Ghoulish", (supernatural.getRecruitingItems(p, "Ghoulish") + 1));
		supernatural.addItemSafely(p, constants.getCustomItems().get("Ghoulish"));
	}
}
