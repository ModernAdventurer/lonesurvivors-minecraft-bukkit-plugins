package me.LoneSurvivor.Supernatural.Classes.Priest.ActiveAbilities;

import org.bukkit.ChatColor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import me.LoneSurvivor.Supernatural.Supernatural;
import me.LoneSurvivor.Supernatural.repositories.Constants;

public class GuardianAngel {
	public GuardianAngel(Supernatural supernatural, Constants constants, Player p, LivingEntity e) {
		if(e instanceof Player) {
			Player t = (Player) e;
			if(supernatural.isSupernatural(supernatural.getRace(t))) {
				return;
			}
			
			//If Not Already Protected
			for(String uuid : supernatural.getGuardianAngelList()) {
				if(uuid.equals(t.getUniqueId().toString())) {
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', supernatural.getConfig().getString("Messages.guardian-angel-failed-sender").replaceAll("%prefix%", supernatural.getConfig().getString("Messages.prefix")).replaceAll("%player%", p.getName()).replaceAll("%target%", t.getName())));
					t.sendMessage(ChatColor.translateAlternateColorCodes('&', supernatural.getConfig().getString("Messages.guardian-angel-failed-target").replaceAll("%prefix%", supernatural.getConfig().getString("Messages.prefix")).replaceAll("%player%", p.getName()).replaceAll("%target%", t.getName())));
					return;
				}
			}

			supernatural.setCooldown(p.getUniqueId(), "GuardianAngel", supernatural.getConfig().getInt("Spells.Priest.GuardianAngel.Cooldown"));
			supernatural.setMagic(p, supernatural.getMagic(p) - supernatural.getConfig().getInt("Spells.Priest.GuardianAngel.Cost"), false);
		
			//add guardian angel
			supernatural.addGuardianAngel(t);
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', supernatural.getConfig().getString("Messages.guardian-angel-succeeded-sender").replaceAll("%prefix%", supernatural.getConfig().getString("Messages.prefix")).replaceAll("%player%", p.getName()).replaceAll("%target%", t.getName())));
			t.sendMessage(ChatColor.translateAlternateColorCodes('&', supernatural.getConfig().getString("Messages.guardian-angel-succeeded-target").replaceAll("%prefix%", supernatural.getConfig().getString("Messages.prefix")).replaceAll("%player%", p.getName()).replaceAll("%target%", t.getName())));
		}
	}
}
