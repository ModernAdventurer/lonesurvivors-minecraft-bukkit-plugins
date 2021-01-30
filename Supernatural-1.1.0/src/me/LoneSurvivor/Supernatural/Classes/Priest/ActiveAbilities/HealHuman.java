package me.LoneSurvivor.Supernatural.Classes.Priest.ActiveAbilities;

import org.bukkit.ChatColor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import me.LoneSurvivor.Supernatural.Supernatural;
import me.LoneSurvivor.Supernatural.repositories.Constants;

public class HealHuman {
	public HealHuman(Supernatural supernatural, Constants constants, Player p, LivingEntity e) {
		if(e instanceof Player) {
			Player t = (Player) e;
			if(supernatural.isSupernatural(supernatural.getRace(t))) {
				return;
			}
			if(t.getHealth()==20) {
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', supernatural.getConfig().getString("Messages.full-health").replaceAll("%prefix%", supernatural.getConfig().getString("Messages.prefix")).replaceAll("%player%", p.getName()).replaceAll("%target%", t.getName())));
				return;
			}
			supernatural.setCooldown(p.getUniqueId(), "HealHuman", supernatural.getConfig().getInt("Spells.Priest.HealHuman.Cooldown"));
			supernatural.setMagic(p, supernatural.getMagic(p) - supernatural.getConfig().getInt("Spells.Priest.HealHuman.Cost"), false);
			//Increase target health
			int health = (int) (t.getHealth() + Math.round(Math.random()*5));
			if(health > 20) {
				health = 20;
			}
			t.setHealth(health);
			t.sendMessage(ChatColor.translateAlternateColorCodes('&', supernatural.getConfig().getString("Messages.heal-received").replaceAll("%prefix%", supernatural.getConfig().getString("Messages.prefix")).replaceAll("%player%", p.getName()).replaceAll("%target%", t.getName())));
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', supernatural.getConfig().getString("Messages.target-healed").replaceAll("%prefix%", supernatural.getConfig().getString("Messages.prefix")).replaceAll("%player%", p.getName()).replaceAll("%target%", t.getName())));
		}
	}
}