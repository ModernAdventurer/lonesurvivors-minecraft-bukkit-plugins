package me.LoneSurvivor.AredoCrates;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Chest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_16_R1.block.CraftChest;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.dynmap.DynmapAPI;
import org.dynmap.markers.Marker;
import org.dynmap.markers.MarkerAPI;
import org.dynmap.markers.MarkerSet;

import me.LoneSurvivor.AredoCrates.Files.DataManager;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_16_R1.IChatBaseComponent;
import net.minecraft.server.v1_16_R1.Packet;
import net.minecraft.server.v1_16_R1.PacketPlayOutChat;
import net.minecraft.server.v1_16_R1.PacketPlayOutTitle;

public class AredoCrates extends JavaPlugin implements Listener {
    public DataManager data;
    private MarkerAPI markerAPI;
    private DynmapAPI dynmapAPI;
    private MarkerSet markerSet;
    public AredoCrates AredoCrates = this;
    Marker marker = null;
    public ArmorStand ArmorStand = null;
    public Location crateLocation = null;
    public int crateTimer = -1;
    
	public void onEnable() {
        this.data = new DataManager(this);
        data.saveDefaultConfig();
        this.saveDefaultConfig();
        Bukkit.getPluginManager().registerEvents(this, this);
        dynmapAPI = (DynmapAPI) Bukkit.getServer().getPluginManager().getPlugin("dynmap");
        markerAPI = dynmapAPI.getMarkerAPI();
        markerSet = markerAPI.createMarkerSet("aredocrates.markerset", "Crates", markerAPI.getMarkerIcons(), false);
        //Repeating Code
    	this.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
    		int tick=0;
            public void run() {
            	tick+=1;
            	if(tick % AredoCrates.getConfig().getInt("Settings.TicksPerCrateSpawn") == 0) spawnCrate();
            	if(crateTimer > 0) {
            		crateTimer -= 1;
            		int time = crateTimer;
            		int seconds = 0;
            		int mins = 0;
            		int hours = 0;
            		while(time >= 20*60*60) {
            			hours += 1;
            			time -= (20*60*60);
            		}
            		while(time >= 20*60) {
            			mins += 1;
            			time -= (20*60);
            		}
            		while(time >= 20) {
            			seconds += 1;
            			time -= (20);
            		}
            		if(time > 0) {
            			seconds += 1;
            		}
            		if(seconds == 60) {
            			seconds = 0;
            			mins+=1;
            		}
            		if(mins == 60) {
            			mins = 0;
            			hours+=1;
            		}
            		String name = AredoCrates.getConfig().getString("Settings.CrateName");
            		if(hours > 0) {
            			name += " &6[&e" + hours + "h&6:&e" + mins + "m&6:&e" + seconds + "s&6]";
            		} else if(mins > 0) {
            			name += " &6[&e" + mins + "m&6:&e" + seconds + "s&6]";
            		} else if(seconds > 0) {
            			name += " &6[&e" + seconds + "s&6]";
            		} else {
            			name += " &6[&eUNLOCKED&6]";
            		}
            		name = ChatColor.translateAlternateColorCodes('&', name);
            		ArmorStand.setCustomName(name);
            		marker.setLabel(ChatColor.stripColor(name));
            	}
            }
        }, 0L, 1L);
	}
	
	public void onDisable() {
        this.removeCrate();
        data.saveConfig();
	}

	public void setupDynmap() {
        //Setup Dynmap
		dynmapAPI = (DynmapAPI) Bukkit.getServer().getPluginManager().getPlugin("Dynmap");
        if (dynmapAPI == null) {
        	this.getLogger().log(Level.SEVERE, "Dynmap Dependancy Missing!");
        	getServer().getPluginManager().disablePlugin(this);
        }
	}
	
	public ItemStack getRandomLoottableItem() {
		List<ItemStack> loottable = new ArrayList<ItemStack>();
		List<Integer> weight = new ArrayList<Integer>();
		List<Integer> min = new ArrayList<Integer>();
		List<Integer> max = new ArrayList<Integer>();
	   	if(data.getConfig().contains("loottable") && data.getConfig().getConfigurationSection("loottable") != null && 
	   			data.getConfig().getConfigurationSection("loottable").getKeys(false) != null && 
	   			data.getConfig().getConfigurationSection("loottable").getKeys(false).size() > 0) {
    		for(String key : data.getConfig().getConfigurationSection("loottable").getKeys(false)) {
           	    loottable.add(data.getConfig().getItemStack("loottable." + key + ".item").clone());
           	    weight.add(data.getConfig().getInt("loottable." + key + ".weight"));
           	    min.add(data.getConfig().getInt("loottable." + key + ".min"));
           	    max.add(data.getConfig().getInt("loottable." + key + ".max"));
    		}
    		int totalweight = 0;
    		for(int w : weight) {
    			totalweight += w;
    		}
    		int random = (int) Math.ceil(Math.random() * totalweight);
    		for(int i = 0; i<weight.size(); i++) {
    			int w = weight.get(i);
    			random-=w;
    			if(random <= 0) {
    				ItemStack item = loottable.get(i);
    				item.setAmount((int) (Math.round(Math.random()*(max.get(i)-min.get(i)))+min.get(i)));
    				return item;
    			}
    		}
	   	}
		return null;
	}
	
	@EventHandler()
	public void crateLockLogic(PlayerInteractEvent event) {
		if((event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.LEFT_CLICK_BLOCK) && crateLocation != null) {
			if(event.getClickedBlock().getLocation().equals(crateLocation)) {
				if(crateTimer == -1) {
					Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.prefix") + " " + "the Magic Crate is being unlocked at: " + crateLocation.getX() + "/" + crateLocation.getY() + "/" + crateLocation.getZ()));
					crateTimer = AredoCrates.getConfig().getInt("Settings.TicksForCrateUnlock");
					event.setCancelled(true);
				}
				if(crateTimer>0) {
					event.setCancelled(true);
			        event.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.DARK_GRAY + "Crate is locked!"));
				}
			}
		}
	}
	
	public Boolean isEmpty(Inventory inv) {
		for(ItemStack item : inv.getContents()) {
		    if(item != null && !item.getType().equals(Material.AIR)) {
			      return false;
		    }
		}
		return true;
	}
	
	@EventHandler
	public void DeleteCrateIfEmpty(InventoryCloseEvent event) {
		if(event.getInventory().getHolder() instanceof Chest) {
			if(crateLocation.equals(event.getInventory().getLocation())) {
				if(isEmpty(event.getInventory())) {
					Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.prefix") + " " + "the Magic Crate was redeemed at: " + crateLocation.getX() + "/" + crateLocation.getY() + "/" + crateLocation.getZ()));
					this.removeCrate();
                }
			}
		}
	}
	
	public int log2(int input) {
        return (int) (Math.log(input) / Math.log(2)); 
    } 
	
	public void spawnCrate() {
		if(crateLocation != null) {
			return;
		}
		
		List<Location> locations = new ArrayList<Location>();
		if(data.getConfig().contains("locations") && data.getConfig().isList("locations")) {
			List<String> stringlocs = data.getConfig().getStringList("locations");
			for(String stringloc : stringlocs) {
				String[] primloc = stringloc.split(":");
				Location location = new Location(Bukkit.getWorld(primloc[3]), Double.parseDouble(primloc[0]), Double.parseDouble(primloc[1]), Double.parseDouble(primloc[2]));
				locations.add(location);
			}
		}
		crateLocation = (Location) getRand(locations);
		Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.prefix") + " " + "Crate Spawned at: " + crateLocation.getX() + "/" + crateLocation.getY() + "/" + crateLocation.getZ()));
		
		//place and fill chest
		crateLocation.getWorld().getBlockAt(crateLocation).setType(Material.CHEST);
		Chest chest = (Chest) crateLocation.getWorld().getBlockAt(crateLocation).getState();
		int playercount = 0;
		for(World w : Bukkit.getWorlds()) {
			playercount += w.getPlayers().size();
		}
		for(int i = 0; i < this.log2(playercount) + this.getConfig().getInt("Settings.StartingCrateItems"); i++) { // make it so starting pos is customisable and so its log2 not log
			chest.getInventory().addItem(this.getRandomLoottableItem());
		}
		
		
		
		//place marker
        marker = markerSet.createMarker("chestMarker", ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Settings.CrateName") + " [LOCKED]")), false, crateLocation.getWorld().getName(), crateLocation.getX(), crateLocation.getY(), crateLocation.getZ(), markerAPI.getMarkerIcon("chest"), false);
        
		//place armourstand
        ArmorStand = (ArmorStand) crateLocation.getWorld().spawnEntity(crateLocation.clone().add(0.5, -1, 0.5), EntityType.ARMOR_STAND);
        ArmorStand.setInvulnerable(true);
        ArmorStand.setBasePlate(false);
        ArmorStand.setCollidable(false);
        ArmorStand.setGravity(false);
        ArmorStand.setVisible(false);
        ArmorStand.setCustomNameVisible(true);
        ArmorStand.setCustomName(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Settings.CrateName") + " &6[&eLOCKED&6]"));
		return;
	}
	
	public void removeCrate() {
		//remove chest
		Chest chest = (Chest) crateLocation.getBlock().getState();
		chest.getInventory().clear();
		crateLocation.getBlock().setType(Material.AIR);
		crateTimer = -1;
		
        //location = null
		crateLocation = null;
		
		//Remove marker
		marker.deleteMarker();
		marker = null;
		
		//remove armourstand
        ArmorStand.remove();
        ArmorStand = null;
    }
	
	public Object getRand(List<?> list) {
		int random = (int) Math.round(Math.random()*(list.size()-1));
		return list.get(random);
	}
	
	public Object getRand(Set<?> set) {
		List<Object> list = new ArrayList<Object>();
		for(Object s : set) {
			list.add(s);
		}
		int random = (int) Math.round(Math.random()*(list.size()-1));
		return list.get(random);
	}
	
	public Object getRand(Object[] array) {
		int random =  (int) Math.round(Math.random()*(array.length-1));
		return array[random];
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    	if (label.equalsIgnoreCase("aredocrates") || label.equalsIgnoreCase("ac")) {
            if (args.length > 0) {
            	//Commands
	            if (args[0].equalsIgnoreCase("help") || args[0].equalsIgnoreCase("?")) {
	            	HelpCommand(sender, label, args);
                	return true;
                }
                if (args[0].equalsIgnoreCase("reload") || args[0].equalsIgnoreCase("rl")) {
	            	ReloadCommand(sender, label, args);
                	return true;
                }
                if (args[0].equalsIgnoreCase("loottable") || args[0].equalsIgnoreCase("lt") || args[0].equalsIgnoreCase("loot")) {
                    LootTableCommand(sender, label, args);
                	return true;
                }
                if (args[0].equalsIgnoreCase("location") || args[0].equalsIgnoreCase("locations") || args[0].equalsIgnoreCase("loc")) {
                    LocationCommand(sender, label, args);
                	return true;
                }
                if (args[0].equalsIgnoreCase("spawncrate") || args[0].equalsIgnoreCase("sc") || args[0].equalsIgnoreCase("spawn")) {
                	SpawnCrateCommand(sender, label, args);
                	return true;
                }
                if (args[0].equalsIgnoreCase("skiptimer") || args[0].equalsIgnoreCase("st") || args[0].equalsIgnoreCase("skip")) {
                    SkipTimerCommand(sender, label, args);
                	return true;
                }
                System.out.println(this.getConfig().getString("Messages.unknown-command").replaceAll("%prefix%", this.getConfig().getString("Messages.prefix")));
                return false;
            }
        	HelpCommand(sender, label, args);
        	return true;
        }
		return false;
    }

    public void SpawnCrateCommand(CommandSender sender, String label, String[] args) {
    	if(sender instanceof Player) {
    		Player player = (Player) sender;
        	if (player.hasPermission("aredocrates.spawncrate")) {
        		spawnCrate();
		    } else {
		        player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.insufficient-permission").replaceAll("%prefix%", this.getConfig().getString("Messages.prefix"))));
		    }
    	} else {
    		spawnCrate();
    	}
    	return;
    }
	
    public void SkipTimerCommand(CommandSender sender, String label, String[] args) {
    	if(sender instanceof Player) {
    		Player player = (Player) sender;
            if (player.hasPermission("aredocrates.skiptimer")) {
            	if(crateTimer != -1 && crateTimer != 0) {
            		crateTimer = 1;
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.skiptimer-success").replaceAll("%prefix%", this.getConfig().getString("Messages.prefix"))));
            	} else {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.skiptimer-fail").replaceAll("%prefix%", this.getConfig().getString("Messages.prefix"))));
            	}
                return;
            } else {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.insufficient-permission").replaceAll("%prefix%", this.getConfig().getString("Messages.prefix"))));
            }
    	} else {
        	if(crateTimer != -1 && crateTimer != 0) {
        		crateTimer = 1;
                System.out.println(ChatColor.stripColor(this.getConfig().getString("Messages.skiptimer-success").replaceAll("%prefix%", this.getConfig().getString("Messages.prefix"))));
        	} else {
                System.out.println(ChatColor.stripColor(this.getConfig().getString("Messages.skiptimer-fail").replaceAll("%prefix%", this.getConfig().getString("Messages.prefix"))));
        	}
    	}	
    }
    
    public void HelpCommand(CommandSender sender, String label, String[] args) {
    	if(sender instanceof Player) {
    		Player player = (Player) sender;
        	if (player.hasPermission("aredocrates.help")) {
		    	player.sendMessage(ChatColor.GRAY + "-------------------");
		        player.sendMessage(ChatColor.GREEN + "AredoCrates Commands");
		        player.sendMessage(ChatColor.GRAY + "-------------------");
		        if (player.hasPermission("aredocrates.help")) {
		            player.sendMessage(ChatColor.GREEN + "/ac help");
		        }
		        if (player.hasPermission("aredocrates.reload")) {
		            player.sendMessage(ChatColor.GREEN + "/ac reload");
		        }
		        if (player.hasPermission("aredocrates.loottable")) {
		            player.sendMessage(ChatColor.GREEN + "/ac loottable add <weight> <min> <max>");
		            player.sendMessage(ChatColor.GREEN + "/ac loottable remove");
		            player.sendMessage(ChatColor.GREEN + "/ac loottable show <page>");
		        }
		        if (player.hasPermission("aredocrates.location")) {
		            player.sendMessage(ChatColor.GREEN + "/ac location add <X> <Y> <Z>");
		            player.sendMessage(ChatColor.GREEN + "/ac location remove <X> <Y> <Z>");
		            player.sendMessage(ChatColor.GREEN + "/ac location show");
		        }
		        if (player.hasPermission("aredocrates.spawncrate")) {
		            player.sendMessage(ChatColor.GREEN + "/ac spawncrate");
		        }
		        if (player.hasPermission("aredocrates.skiptimer")) {
		            player.sendMessage(ChatColor.GREEN + "/ac skiptimer");
		        }
		    } else {
		        player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.insufficient-permission").replaceAll("%prefix%", this.getConfig().getString("Messages.prefix"))));
		    }
    	} else {
    		System.out.print("-------------------");
    		System.out.print("AredoCrates Commands");
    		System.out.print("-------------------");
    		System.out.print("/ac help");
    		System.out.print("/ac reload");
    		System.out.print("/ac loottable add <weight> <min> <max>");
    		System.out.print("/ac loottable remove");
    		System.out.print("/ac loottable show <page>");
    		System.out.print("/ac location add <X> <Y> <Z>");
    		System.out.print("/ac location remove <X> <Y> <Z>");
    		System.out.print("/ac location show");
    		System.out.print("/ac spawncrate");
    		System.out.print("/ac skiptimer");
    	}
    	return;
    }

    public void ReloadCommand(CommandSender sender, String label, String[] args) {
    	if(sender instanceof Player) {
    		Player player = (Player) sender;
            if (player.hasPermission("aredocrates.reload")) {
                this.saveDefaultConfig();
                this.reloadConfig();
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.reload-success").replaceAll("%prefix%", this.getConfig().getString("Messages.prefix"))));
                return;
            } else {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.insufficient-permission").replaceAll("%prefix%", this.getConfig().getString("Messages.prefix"))));
            }
    	} else {
            this.saveDefaultConfig();
            this.reloadConfig();
            System.out.println(ChatColor.stripColor(this.getConfig().getString("Messages.reload-success").replaceAll("%prefix%", this.getConfig().getString("Messages.prefix"))));
    	}	
    }

    public Boolean isInt(String input) {
    	try {
    		Integer.parseInt(input);
    		return true;
    	} catch(Exception e) {
    		return false;
    	}
    }
    
	public void LootTableCommand(CommandSender sender, String label, String[] args) {
    	if(sender instanceof Player) {
    		Player player = (Player) sender;
            if (player.hasPermission("aredocrates.loottable")) {
            	if(args.length == 5 && args[1] instanceof String && args[1].equals("add") && this.isInt(args[2]) && this.isInt(args[3]) && this.isInt(args[4])) {
            		int weight = Integer.parseInt(args[2]);
            		int min = Integer.parseInt(args[3]);
            		int max = Integer.parseInt(args[4]);
            		int itemnumber = 0;
            		if(data.getConfig().contains("loottable") && data.getConfig().getConfigurationSection("loottable") != null) {
            			itemnumber = data.getConfig().getConfigurationSection("loottable").getKeys(false).size();
            		}
            		ItemStack item = player.getInventory().getItemInMainHand().clone();
            		item.setAmount(1);
            		if(item != null && item.getType() != null && item.getType() != Material.AIR) {
                		data.getConfig().set("loottable." + itemnumber + ".item", item);
                		data.getConfig().set("loottable." + itemnumber + ".weight", weight);
                		data.getConfig().set("loottable." + itemnumber + ".min", min);
                		data.getConfig().set("loottable." + itemnumber + ".max", max);
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.loottable-add-success").replaceAll("%prefix%", this.getConfig().getString("Messages.prefix"))));
            		} else {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.empty-hand").replaceAll("%prefix%", this.getConfig().getString("Messages.prefix"))));
            		}
            	} else if(args.length == 2 && args[1] instanceof String && args[1].equals("remove")) {
            		if(data.getConfig().contains("loottable") && data.getConfig().getConfigurationSection("loottable") != null) {
            			ItemStack hand = player.getInventory().getItemInMainHand();
        				hand.setAmount(1);
            			for(String num : data.getConfig().getConfigurationSection("loottable").getKeys(false)) {
            				ItemStack item = data.getConfig().getItemStack("loottable." + num + ".item");
            				if(item != null) {
                				if(item.equals(hand)) {
                        	    	List<ItemStack> loottable = new ArrayList<ItemStack>();
                        	    	List<Integer> weight = new ArrayList<Integer>();
                        	    	List<Integer> min = new ArrayList<Integer>();
                        	    	List<Integer> max = new ArrayList<Integer>();
                        			for(String key2 : data.getConfig().getConfigurationSection("loottable").getKeys(false)) {
                        				if(!num.equals(key2)) {
                                	    	loottable.add(data.getConfig().getItemStack("loottable." + key2 + ".item"));
                                	    	weight.add(data.getConfig().getInt("loottable." + key2 + ".weight"));
                                	    	min.add(data.getConfig().getInt("loottable." + key2 + ".min"));
                                	    	max.add(data.getConfig().getInt("loottable." + key2 + ".max"));
                        				}
                        			}
                        			data.getConfig().set("loottable", null);
                        			for(int i = 0; i<loottable.size(); i++) {
                        				data.getConfig().set("loottable." + i + ".item", loottable.get(i));
                        				data.getConfig().set("loottable." + i + ".weight", weight.get(i));
                        				data.getConfig().set("loottable." + i + ".min", min.get(i));
                        				data.getConfig().set("loottable." + i + ".max", max.get(i));
                        			}
                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.loottable-remove-success").replaceAll("%prefix%", this.getConfig().getString("Messages.prefix"))));
                        			return;
                				}
            				}
            			}
            			player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.item-not-in-loottable").replaceAll("%prefix%", this.getConfig().getString("Messages.prefix"))));
            		} else {
            			
            		}
            	} else if(args.length >= 2 && args[1] instanceof String && args[1].equals("show")) {
            		int page = 1;
            		if(args.length > 2 && this.isInt(args[2]) && Integer.parseInt(args[2])>0) page = Integer.parseInt(args[2]);
            		Inventory gui = Bukkit.createInventory(null, 54);
            		List<ItemStack> loottable = new ArrayList<ItemStack>();
            		List<Integer> weight = new ArrayList<Integer>();
            		List<Integer> min = new ArrayList<Integer>();
            		List<Integer> max = new ArrayList<Integer>();
	        	   	if(data.getConfig().contains("loottable") && data.getConfig().getConfigurationSection("loottable") != null && 
	        	   			data.getConfig().getConfigurationSection("loottable").getKeys(false) != null && 
	        	   			data.getConfig().getConfigurationSection("loottable").getKeys(false).size() > 0) {
	            		for(String key : data.getConfig().getConfigurationSection("loottable").getKeys(false)) {
	                   	    loottable.add(data.getConfig().getItemStack("loottable." + key + ".item").clone());
	                   	    weight.add(data.getConfig().getInt("loottable." + key + ".weight"));
	                   	    min.add(data.getConfig().getInt("loottable." + key + ".min"));
	                   	    max.add(data.getConfig().getInt("loottable." + key + ".max"));
	            		}
	            		for(int i = 0; ((i + ((page-1)*54))<loottable.size()) && (i < 54); i++) {
	            			ItemStack item = loottable.get(i + ((page-1)*54));
	            			ItemMeta meta = item.getItemMeta();
	            			List<String> lore = new ArrayList<String>();
	            			if(meta.hasLore()) lore = meta.getLore();
	            			lore.add(ChatColor.GOLD + "Weight: " + ChatColor.YELLOW + weight.get(i + ((page-1)*54)));
	            			lore.add(ChatColor.GOLD + "Min: " + ChatColor.YELLOW + min.get(i + ((page-1)*54)));
	            			lore.add(ChatColor.GOLD + "Max: " + ChatColor.YELLOW + max.get(i + ((page-1)*54)));
	            			meta.setLore(lore);
	            			item.setItemMeta(meta);
	            			gui.setItem(i, item);
	            		}
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.loottable-open-success").replaceAll("%prefix%", this.getConfig().getString("Messages.prefix"))));
	            		player.openInventory(gui);
	        	   	} else {
	                       player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.loottable-empty").replaceAll("%prefix%", this.getConfig().getString("Messages.prefix"))));
	        	   	}
            	} else {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.incorrect-format").replaceAll("%prefix%", this.getConfig().getString("Messages.prefix"))));
            	}
            } else {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.insufficient-permission").replaceAll("%prefix%", this.getConfig().getString("Messages.prefix"))));
            }
    	} else {
    		sender.sendMessage(this.getConfig().getString("Messages.no-console").replaceAll("%prefix%", this.getConfig().getString("Messages.prefix")));
    	}
    }
    
    public void LocationCommand(CommandSender sender, String label, String[] args) {
    	if(sender instanceof Player) {
    		Player player = (Player) sender;
            if (player.hasPermission("aredocrates.location")) {
            	if(args.length == 5 && args[1] instanceof String && args[1].equals("add") && this.isInt(args[2]) && this.isInt(args[3]) && this.isInt(args[4])) {
            		double x = Integer.parseInt(args[2]);
            		double y = Integer.parseInt(args[3]);
            		double z = Integer.parseInt(args[4]);
            		World w = player.getWorld();
            		List<String> locations = new ArrayList<String>();
            		if(data.getConfig().contains("locations") && data.getConfig().getStringList("locations") != null && data.getConfig().getStringList("locations").size() > 0) {
            			locations = data.getConfig().getStringList("locations");
            		}
            		locations.add(x + ":" + y + ":" + z + ":" + w.getName());
            		data.getConfig().set("locations", locations);
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.location-add-success").replaceAll("%prefix%", this.getConfig().getString("Messages.prefix"))));
            	} else if(args.length == 5 && args[1] instanceof String && args[1].equals("remove") && this.isInt(args[2]) && this.isInt(args[3]) && this.isInt(args[4])) {
        			if(data.getConfig().contains("locations") && data.getConfig().getStringList("locations") != null && data.getConfig().getStringList("locations").size() > 0) {
        				double x = Integer.parseInt(args[2]);
        				double y = Integer.parseInt(args[3]);
        				double z = Integer.parseInt(args[4]);
                		World w = player.getWorld();
                    	List<String> locations = data.getConfig().getStringList("locations");
                		for(String location : locations) {
            				String[] parts1 = location.split(":");
            				Location loc1 = new Location(Bukkit.getWorld(parts1[3]), Double.parseDouble(parts1[0]), Double.parseDouble(parts1[1]), Double.parseDouble(parts1[2]));
            				Location loc2 = new Location(w, x, y, z);
                			if(loc1.equals(loc2)) {
                				locations.remove(location);
                        		data.getConfig().set("locations", locations);
                    			if(crateLocation.equals(loc1)) this.removeCrate();
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.location-remove-success").replaceAll("%prefix%", this.getConfig().getString("Messages.prefix"))));
                                return;
                			}
                		}
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.unknown-location").replaceAll("%prefix%", this.getConfig().getString("Messages.prefix"))));
                		return;
            		} else {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.locations-empty").replaceAll("%prefix%", this.getConfig().getString("Messages.prefix"))));
            		}
            	} else if(args.length == 2 && args[1] instanceof String && args[1].equals("show")) {
            		if(data.getConfig().contains("locations") && data.getConfig().getStringList("locations") != null&& data.getConfig().getStringList("locations").size() > 0) {
                		List<String> locations = data.getConfig().getStringList("locations");
                		player.sendMessage(ChatColor.GOLD + "            Locations");
                		player.sendMessage(ChatColor.GOLD + "=========================");
            			for(String location : locations) {
            				String[] loc = location.split(":");
            				player.sendMessage(ChatColor.GOLD + "World: " + ChatColor.YELLOW + loc[3] + ChatColor.GOLD + " XYZ: " + ChatColor.YELLOW + loc[0] + "/" + loc[1] + "/" + loc[2]);
            			}
            		} else {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.locations-empty").replaceAll("%prefix%", this.getConfig().getString("Messages.prefix"))));
            		}
            	} else {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.incorrect-format").replaceAll("%prefix%", this.getConfig().getString("Messages.prefix"))));
            	}
            } else {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.insufficient-permission").replaceAll("%prefix%", this.getConfig().getString("Messages.prefix"))));
            }
    	}
    }
}