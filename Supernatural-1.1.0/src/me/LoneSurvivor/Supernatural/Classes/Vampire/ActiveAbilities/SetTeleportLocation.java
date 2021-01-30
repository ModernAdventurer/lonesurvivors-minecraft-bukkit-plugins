package me.LoneSurvivor.Supernatural.Classes.Vampire.ActiveAbilities;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import me.LoneSurvivor.Supernatural.Supernatural;
import me.LoneSurvivor.Supernatural.repositories.Constants;

public class SetTeleportLocation {
	public SetTeleportLocation(Supernatural supernatural, Constants constants, Player p) {
		supernatural.setCooldown(p.getUniqueId(), "SetTeleportLocation", supernatural.getConfig().getInt("Spells.Vampire.SetTeleportLocation.Cooldown"));
		supernatural.setMagic(p, supernatural.getMagic(p) - supernatural.getConfig().getInt("Spells.Vampire.SetTeleportLocation.Cost"), false);
		Location location = p.getLocation();
		supernatural.setTeleportLocation(p, location);
		p.sendMessage(ChatColor.translateAlternateColorCodes('&', supernatural.getConfig().getString("Messages.set-teleport-location").replaceAll("%prefix%", supernatural.getConfig().getString("Messages.prefix"))));
	}
}