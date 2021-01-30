package me.LoneSurvivor.Supernatural.commands.subcommands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.LoneSurvivor.Supernatural.Supernatural;

public class ClassInformationCommand {

    public ClassInformationCommand(Supernatural supernatural, CommandSender sender, String[] args) {
    	if(sender instanceof Player) {
    		Player player = (Player) sender;
        	if (player.hasPermission("supernatural.classes")) {
        		if(args.length<2) {
    		    	player.sendMessage(ChatColor.GRAY + "--------------");
    		        player.sendMessage(ChatColor.GREEN + "Class List");
    		        player.sendMessage(ChatColor.GRAY + "--------------");
    		        player.sendMessage(ChatColor.GREEN + "1. Vampire");
    		        player.sendMessage(ChatColor.GREEN + "2. Werewolf");
    		        player.sendMessage(ChatColor.GREEN + "3. Ghoul");
    		        player.sendMessage(ChatColor.GREEN + "4. Demon");
    		        player.sendMessage(ChatColor.GREEN + "5. Priest");
    		        player.sendMessage(ChatColor.GREEN + "6. Necromancer");
    		        player.sendMessage(ChatColor.GREEN + "7. Witch Hunter");
    		        player.sendMessage(ChatColor.GREEN + "type: '/sn class <class>' for more information.");
    		        return;
        		}
        		if(args[1].equalsIgnoreCase("Vampire")) {
    		    	player.sendMessage(ChatColor.GRAY + "--------------");
    		        player.sendMessage(ChatColor.GREEN + "Vampire");
    		        player.sendMessage(ChatColor.GRAY + "--------------");
    		        player.sendMessage(ChatColor.DARK_GREEN + "Active Abilities");
    		        player.sendMessage(ChatColor.GREEN + "1. Water Breathing [" + supernatural.getConfig().getInt("Spells.Vampire.WaterBreathing.Cost")*20 + " Magika / Second]");
    		        player.sendMessage(ChatColor.GREEN + "2. Regeneration [" + supernatural.getConfig().getInt("Spells.Vampire.Regeneration.Cost") + " Magika / Heart]");
    		        player.sendMessage(ChatColor.GREEN + "3. Teleportation [" + supernatural.getConfig().getInt("Spells.Vampire.Teleport.Cost") + " Magika]");
    		        player.sendMessage(ChatColor.GREEN + "4. High Jump [" + supernatural.getConfig().getInt("Spells.Vampire.HighJump.Cost") + " Magika]");
    		        player.sendMessage(ChatColor.GREEN + "4. Create Blood Vial [" + supernatural.getConfig().getInt("Spells.Vampire.Bloodvial.Cost") + " Magika]");
    		        player.sendMessage(ChatColor.GREEN + "5. Create Bloodrose Potion [" + supernatural.getConfig().getInt("Spells.Vampire.Bloodrose.Cost") + " Magika & 5 Hearts]");
    		        player.sendMessage(ChatColor.DARK_GREEN + "Passive Abilities");
    		        player.sendMessage(ChatColor.GREEN + "1. Rage - Deal twice as much damage");
    		        player.sendMessage(ChatColor.GREEN + "2. Take 20% less damage in combat");
    		        player.sendMessage(ChatColor.GREEN + "3. Fall Damage Immunity");
    		        player.sendMessage(ChatColor.GREEN + "4. Truce with Skeletons, Zombies, Creepers, and Spiders");
    		        player.sendMessage(ChatColor.GREEN + "5. Killing Animals/Players replenishes Hunger");
    		        player.sendMessage(ChatColor.GREEN + "6. Nightvision");
    		        player.sendMessage(ChatColor.DARK_GREEN + "Weaknesses");
    		        player.sendMessage(ChatColor.GREEN + "1. Catch fire in sunlight unless wearing a gold helmet [100 magika / 3 seconds]");
    		        player.sendMessage(ChatColor.GREEN + "2. 4x Damage to Wooden Objects");
    		        player.sendMessage(ChatColor.GREEN + "3. Cannot use Wooden Objects");
    		        player.sendMessage(ChatColor.GREEN + "4. Cannot eat regular food");
    		        return;
        		}
        		if(args[1].equalsIgnoreCase("Werewolf")) {
    		    	player.sendMessage(ChatColor.GRAY + "--------------");
    		        player.sendMessage(ChatColor.GREEN + "Werewolf");
    		        player.sendMessage(ChatColor.GRAY + "--------------");
    		        player.sendMessage(ChatColor.DARK_GREEN + "Active Abilities");
    		        player.sendMessage(ChatColor.GREEN + "1. Summon Wolf Pet [" + supernatural.getConfig().getInt("Spells.Werewolf.SummonWolf.Cost") + " Magika]");
    		        player.sendMessage(ChatColor.GREEN + "2. Dash [" + supernatural.getConfig().getInt("Spells.Werewolf.Dash.Cost") + " Magika]");
    		        player.sendMessage(ChatColor.GREEN + "3. Create Moonflower Potion [" + supernatural.getConfig().getInt("Spells.Werewolf.Moonflower.Cost") + " Magika & 5 Hearts]");
    		        player.sendMessage(ChatColor.DARK_GREEN + "Passive Abilities");
    		        player.sendMessage(ChatColor.GREEN + "1. Regeneration [Heart / 25 Ticks]");
    		        player.sendMessage(ChatColor.GREEN + "2. 4x Attack Damage");
    		        player.sendMessage(ChatColor.GREEN + "3. 1/3 Fall Damage");
    		        player.sendMessage(ChatColor.GREEN + "4. Truce with Wolves");
    		        player.sendMessage(ChatColor.GREEN + "5. 2x Speed");
    		        player.sendMessage(ChatColor.GREEN + "6. Nightvision");
    		        player.sendMessage(ChatColor.GREEN + "7. Eat Food when hunger is full");
    		        player.sendMessage(ChatColor.DARK_GREEN + "Weaknesses");
    		        player.sendMessage(ChatColor.GREEN + "1. Passive & Active abilities only work at night");
    		        player.sendMessage(ChatColor.GREEN + "2. No weapons or armour at night");
    		        return;
        		}
        		if(args[1].equalsIgnoreCase("Ghoul")) {
    		    	player.sendMessage(ChatColor.GRAY + "--------------");
    		        player.sendMessage(ChatColor.GREEN + "Ghoul");
    		        player.sendMessage(ChatColor.GRAY + "--------------");
    		        player.sendMessage(ChatColor.DARK_GREEN + "Active Abilities");
    		        player.sendMessage(ChatColor.GREEN + "1. Summon Undead [" + supernatural.getConfig().getInt("Spells.Ghoul.SummonMonster.Cost") + " Magika]");
    		        player.sendMessage(ChatColor.GREEN + "2. Unholy Bond [" + supernatural.getConfig().getInt("Spells.Ghoul.UnholyBond.Cost") + " Magika]");
    		        player.sendMessage(ChatColor.GREEN + "3. Create Ghoulish Potion [" + supernatural.getConfig().getInt("Spells.Ghoul.Ghoulish.Cost") + " Magika & 5 Hearts]");
    		        player.sendMessage(ChatColor.DARK_GREEN + "Passive Abilities");
    		        player.sendMessage(ChatColor.GREEN + "1. Regeneration [Heart / 25 Ticks]");
    		        player.sendMessage(ChatColor.GREEN + "2. 50% General Damage Resistance");
    		        player.sendMessage(ChatColor.GREEN + "3. 33% Combat Damage Resistance");
    		        player.sendMessage(ChatColor.GREEN + "4. Fall Damage Immunity");
    		        player.sendMessage(ChatColor.GREEN + "5. Truce with the Undead");
    		        player.sendMessage(ChatColor.GREEN + "6. Nightvision");
    		        player.sendMessage(ChatColor.DARK_GREEN + "Weaknesses");
    		        player.sendMessage(ChatColor.GREEN + "1. Damaged by Water & Rain");
    		        player.sendMessage(ChatColor.GREEN + "2. Cannot use weapons");
    		        return;
        		}
        		if(args[1].equalsIgnoreCase("Demon")) {
    		    	player.sendMessage(ChatColor.GRAY + "--------------");
    		        player.sendMessage(ChatColor.GREEN + "Demon");
    		        player.sendMessage(ChatColor.GRAY + "--------------");
    		        player.sendMessage(ChatColor.DARK_GREEN + "Active Abilities");
    		        player.sendMessage(ChatColor.GREEN + "1. Fireball [" + supernatural.getConfig().getInt("Spells.Demon.Fireball.Cost") + " Magika]");
    		        player.sendMessage(ChatColor.GREEN + "2. Explosion [" + supernatural.getConfig().getInt("Spells.Demon.Explosion.Cost") + " Magika]");
    		        player.sendMessage(ChatColor.GREEN + "3. Summon Strider [" + supernatural.getConfig().getInt("Spells.Demon.Snare.Cost") + " Magika]");
    		        player.sendMessage(ChatColor.GREEN + "4. Snare [" + supernatural.getConfig().getInt("Spells.Demon.SummonStrider.Cost") + " Magika]");
    		        player.sendMessage(ChatColor.GREEN + "5. Create Hellash Potion [" + supernatural.getConfig().getInt("Spells.Demon.Hellish.Cost") + " Magika & 5 Hearts]");
    		        player.sendMessage(ChatColor.DARK_GREEN + "Passive Abilities");
    		        player.sendMessage(ChatColor.GREEN + "1. Immune to Fire Damage");
    		        player.sendMessage(ChatColor.GREEN + "2. Heal while in Lava or Nether");
    		        player.sendMessage(ChatColor.GREEN + "3. Deal more damage in Lava or Nether");
    		        player.sendMessage(ChatColor.GREEN + "4. Truce with Nether Monsters");
    		        player.sendMessage(ChatColor.GREEN + "5. Fall Damage Immunity");
    		        player.sendMessage(ChatColor.GREEN + "6. Nightvision");
    		        player.sendMessage(ChatColor.DARK_GREEN + "Weaknesses");
    		        player.sendMessage(ChatColor.GREEN + "1. Damaged by Water & Rain");
    		        player.sendMessage(ChatColor.GREEN + "2. Cannot use Weapons");
    		        return;
        		}
        		if(args[1].equalsIgnoreCase("Priest")) {
    		    	player.sendMessage(ChatColor.GRAY + "--------------");
    		        player.sendMessage(ChatColor.GREEN + "Priest");
    		        player.sendMessage(ChatColor.GRAY + "--------------");
    		        player.sendMessage(ChatColor.DARK_GREEN + "Active Abilities");
    		        player.sendMessage(ChatColor.GREEN + "1. Banish Supernatural [" + supernatural.getConfig().getInt("Spells.Priest.Banish.Cost") + " Magika]");
    		        player.sendMessage(ChatColor.GREEN + "2. Exorcise Supernatural [" + supernatural.getConfig().getInt("Spells.Priest.Exorcise.Cost") + " Magika]");
    		        player.sendMessage(ChatColor.GREEN + "3. Drain Supernatural [" + supernatural.getConfig().getInt("Spells.Priest.Drain.Cost") + " Magika]");
    		        player.sendMessage(ChatColor.GREEN + "4. Heal Human [" + supernatural.getConfig().getInt("Spells.Priest.HealHuman.Cost") + " Magika]");
    		        player.sendMessage(ChatColor.GREEN + "5. Guardian Angel [" + supernatural.getConfig().getInt("Spells.Priest.GuardianAngel.Cost") + " Magika]");
    		        player.sendMessage(ChatColor.GREEN + "6. Create Supernatural Cure [" + supernatural.getConfig().getInt("Spells.Priest.Cure.Cost") + " Magika]");
    		        player.sendMessage(ChatColor.GREEN + "7. Create Holy Book [" + supernatural.getConfig().getInt("Spells.Priest.HolyBook.Cost") + " Magika & 5 Hearts]");
    		        player.sendMessage(ChatColor.DARK_GREEN + "Passive Abilities");
    		        player.sendMessage(ChatColor.GREEN + "1. 2.5x Damage Against Supernaturals");
    		        player.sendMessage(ChatColor.GREEN + "2. 1/10 Chance of setting supernaturals on fire");
    		        player.sendMessage(ChatColor.GREEN + "3. 1/8 Chance of setting monsters on fire");
    		        player.sendMessage(ChatColor.DARK_GREEN + "Weaknesses");
    		        player.sendMessage(ChatColor.GREEN + "1. Cannot wear armour");
    		        player.sendMessage(ChatColor.GREEN + "2. Cannot attack animals");
    		        return;
        		}
        		if(args[1].equalsIgnoreCase("Necromancer")) {
    		    	player.sendMessage(ChatColor.GRAY + "--------------");
    		        player.sendMessage(ChatColor.GREEN + "Necromancer");
    		        player.sendMessage(ChatColor.GRAY + "--------------");
    		        player.sendMessage(ChatColor.DARK_GREEN + "Active Abilities");
    		        player.sendMessage(ChatColor.GREEN + "1. Summon Skeleton [" + supernatural.getConfig().getInt("Spells.Necromancer.SummonSkeleton.Cost") + " Magika]");
    		        player.sendMessage(ChatColor.GREEN + "2. Summon Undead [" + supernatural.getConfig().getInt("Spells.Necromancer.SummonUndead.Cost") + " Magika]");
    		        player.sendMessage(ChatColor.GREEN + "3. Heal-Undead [" + supernatural.getConfig().getInt("Spells.Necromancer.HealUndead.Cost") + " Magika]");
    		        player.sendMessage(ChatColor.GREEN + "4. Ressurection Spawn [" + supernatural.getConfig().getInt("Spells.Necromancer.RessurectionSpawn.Cost") + " Magika]");
    		        player.sendMessage(ChatColor.GREEN + "5. Create Book of Death [" + supernatural.getConfig().getInt("Spells.Necromancer.BookOfDeath.Cost") + " Magika & 5 Hearts]");
    		        player.sendMessage(ChatColor.DARK_GREEN + "Passive Abilities");
    		        player.sendMessage(ChatColor.GREEN + "1. Full truce with Undead");
    		        player.sendMessage(ChatColor.GREEN + "2. Undead will follow if within 20 blocks");
    		        player.sendMessage(ChatColor.GREEN + "3. Increase damage at night");
    		        player.sendMessage(ChatColor.GREEN + "4. Nightvision");
    		        player.sendMessage(ChatColor.DARK_GREEN + "Weaknesses");
    		        player.sendMessage(ChatColor.GREEN + "1. Can only wear Netherite Armour");
    		        player.sendMessage(ChatColor.GREEN + "2. Cannot use Netherite Weapons");
    		        return;
        		}
        		if(args[1].equalsIgnoreCase("WitchHunter")) {
    		    	player.sendMessage(ChatColor.GRAY + "--------------");
    		        player.sendMessage(ChatColor.GREEN + "Witch Hunter");
    		        player.sendMessage(ChatColor.GRAY + "--------------");
    		        player.sendMessage(ChatColor.DARK_GREEN + "Active Abilities");
    		        player.sendMessage(ChatColor.GREEN + "1. Triple Arrow [" + supernatural.getConfig().getInt("Spells.WitchHunter.TripleArrow.Cost") + " Magika]");
    		        player.sendMessage(ChatColor.GREEN + "2. Grapple Arrow [" + supernatural.getConfig().getInt("Spells.WitchHunter.GrappleArrow.Cost") + " Magika]");
    		        player.sendMessage(ChatColor.GREEN + "3. Fire Arrow [" + supernatural.getConfig().getInt("Spells.WitchHunter.FireArrow.Cost") + " Magika]");
    		        player.sendMessage(ChatColor.GREEN + "4. Power Arrow [" + supernatural.getConfig().getInt("Spells.WitchHunter.PowerArrow.Cost") + " Magika]");
    		        player.sendMessage(ChatColor.GREEN + "5. Volley Arrow [" + supernatural.getConfig().getInt("Spells.WitchHunter.VolleyArrow.Cost") + " Magika]");
    		        player.sendMessage(ChatColor.GREEN + "5. Book of the WitchHunter [" + supernatural.getConfig().getInt("Spells.WitchHunter.BookOfWitchHunter.Cost") + " Magika & 5 Hearts]");
    		        player.sendMessage(ChatColor.DARK_GREEN + "Passive Abilities");
    		        player.sendMessage(ChatColor.GREEN + "1. 1/3 Fall Damage");
    		        player.sendMessage(ChatColor.DARK_GREEN + "Weaknesses");
    		        player.sendMessage(ChatColor.GREEN + "1. Can only wear Leather Armour");
    		        player.sendMessage(ChatColor.GREEN + "2. Cannot use Melee Weapons");
    		        return;
        		}
		    } else {
		        player.sendMessage(ChatColor.translateAlternateColorCodes('&', supernatural.getConfig().getString("Messages.insufficient-permission")));
		    }
    	}
    	return;
    }
    
}
