package me.LoneSurvivor.Supernatural.commands;

import me.LoneSurvivor.Supernatural.Supernatural;
import me.LoneSurvivor.Supernatural.commands.subcommands.*;
import net.milkbowl.vault.economy.Economy;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class Commands implements CommandExecutor {

	Supernatural supernatural;
	Economy eco;
	
    public Commands(Supernatural supernatural, Economy eco) {
    	this.supernatural = supernatural;
    	this.eco = eco;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
    	if (commandLabel.equalsIgnoreCase("supernatural") || commandLabel.equalsIgnoreCase("sn")) {
            //Commands
            String subCommand = args[0];
	        if (args.length == 0 || (subCommand.equalsIgnoreCase("help") || subCommand.equalsIgnoreCase("?"))) {
	           	new HelpCommand(supernatural, sender, args);
               	return true;
            }
	        if (subCommand.equalsIgnoreCase("version") || subCommand.equalsIgnoreCase("ver")) {
	        	new VersionCommand(supernatural, sender, args);
               	return true;
	        }
            if (subCommand.equalsIgnoreCase("reload") || subCommand.equalsIgnoreCase("rl")) {
            	new ReloadCommand(supernatural, sender, args);
             	return true;
            }
            if (subCommand.equalsIgnoreCase("setclass") || subCommand.equalsIgnoreCase("sc")) {
            	new SetClassCommand(supernatural, sender, args);
            	return true;
            }
            if (subCommand.equalsIgnoreCase("setmagic") || subCommand.equalsIgnoreCase("sm")) {
            	new SetMagicCommand(supernatural, sender, args);
            	return true;
            }
            if (subCommand.equalsIgnoreCase("class") || subCommand.equalsIgnoreCase("classes")) {
            	new ClassInformationCommand(supernatural, sender, args);
            	return true;
            }
            if (subCommand.equalsIgnoreCase("setbanishlocation") || args[0].equalsIgnoreCase("setbanish") || args[0].equalsIgnoreCase("banishlocation") || args[0].equalsIgnoreCase("sbl") || args[0].equalsIgnoreCase("sb") || args[0].equalsIgnoreCase("bl")) {
            	new SetBanishLocationCommand(supernatural, sender, args);
            	return true;
            }
            System.out.println(supernatural.getConfig().getString("Messages.unknown-command"));
            return false;
        }
    	if (commandLabel.equalsIgnoreCase("bounty")) {
            if (args.length > 0) {
            	//Commands
	            if (args[0].equalsIgnoreCase("add") || args[0].equalsIgnoreCase("set")) {
	            	new AddBountyCommand(supernatural, eco, sender, args);
                	return true;
                }
                if (args[0].equalsIgnoreCase("check") || args[0].equalsIgnoreCase("view") || args[0].equalsIgnoreCase("see") || args[0].equalsIgnoreCase("open")) {
                	new CheckBountyCommand(supernatural, sender, args);
                	return true;
                }
                System.out.println(supernatural.getConfig().getString("Messages.unknown-command"));
                return false;
            }
            new CheckBountyCommand(supernatural, sender, args);
        	return true;
        }
		return false;
    }
}
