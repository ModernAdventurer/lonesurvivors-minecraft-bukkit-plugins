package me.LoneSurvivor.SpawnersPlus;

import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.SpawnerSpawnEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import me.LoneSurvivor.SpawnersPlus.Files.DataManager;
import me.LoneSurvivor.SpawnersPlus.Storage.Save;
import me.LoneSurvivor.SpawnersPlus.Storage.Load;
import net.milkbowl.vault.economy.Economy;

import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.CreatureSpawner;

public class SpawnersPlus extends JavaPlugin implements Listener {
    Economy eco;
    DecimalFormat df = new DecimalFormat("0.00");
    public Inventory inv;
    public List<Inventory> shoppages = new ArrayList<Inventory>();
    public static Map < Location, int[] > SpawnerLevels = new HashMap < Location, int[] > ();
    public static Map < Player, Location > OpenSpawnerLocation = new HashMap < Player, Location > ();
    public static Map < EntityType, ItemStack > EntityHead = new HashMap < EntityType, ItemStack > ();
    public DataManager data;
    Save save = new Save();
    Load load = new Load();

    /**
     * Events
     */

    @Override
    public void onEnable() {
        this.data = new DataManager(this);
        data.saveDefaultConfig();
        this.saveDefaultConfig();
        this.getServer().getPluginManager().registerEvents(this, this);
        
        //Sets Up Variables
        SetupEntityHeads();
        SetupShop();
        Map < Location, int[] > SpawnerLevels = load.SpawnerLevels(data);
        
        //Setup Vault
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
        	getServer().getPluginManager().disablePlugin(this);
        }
        RegisteredServiceProvider < Economy > rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
        	getServer().getPluginManager().disablePlugin(this);
        }
        eco = rsp.getProvider();
        
        //Set up Spawner Timers
        int MergeDelay = this.getConfig().getInt("MergeFrequency");
        double DefaultSpeed = this.getConfig().getDouble("Speed.DefaultSpeed");
        double TimeMultiplier = this.getConfig().getDouble("Speed.TimerMultiplier");
        this.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            double timer=0;
            public void run() {
                timer = timer + 1;
                //Try to merge stacked mobs every config# of seconds
                if (timer % MergeDelay == 0) {
                    merge();
                }
                //If any of the spawners should spawn a mob
                if (!SpawnerLevels.isEmpty()) {
                    for (Map.Entry < Location, int[] > entry: SpawnerLevels.entrySet()) {
                        Location location = entry.getKey();
                        int[] levels = entry.getValue();
                        if (location.getBlockY() < 260) {
                            int speed = (int) Math.round(MultiplyNumTimes(DefaultSpeed, TimeMultiplier, levels[0] - 1));
                            if (timer % speed == 0) {
                                spawnMob(location);
                            }
                        }
                    }
                }
            }
        }, 0L, 20L);
    }

    @Override
    public void onDisable() {
        this.saveDefaultConfig();
        save.SaveSpawnerLevels(SpawnerLevels, data);
    }

    @EventHandler()
    @SuppressWarnings("deprecation")
    public void onBlockBreak(BlockBreakEvent event) {
        Location location = event.getBlock().getLocation();
        if (event.getBlock().getType().equals(Material.SPAWNER)) {
        	if (event.getPlayer().hasPermission("spawnersplus.breakspawner")) {
                if (event.getPlayer().getItemInHand().containsEnchantment(Enchantment.SILK_TOUCH)) {
                    if (event.getPlayer().getGameMode() == GameMode.SURVIVAL) {
                        int[] levels = new int[] {
                            1,
                            1,
                            1
                        };
                        if (SpawnerLevels.containsKey(location)) {
                            levels = SpawnerLevels.get(location);
                        }
                        CreatureSpawner cs = (CreatureSpawner) event.getBlock().getState();
                        EntityType entity = cs.getSpawnedType();
                        event.setExpToDrop(0);
                        event.setDropItems(false);
                        ItemStack item = new ItemStack(event.getBlock().getType(), 1);
                        BlockStateMeta bsm = (BlockStateMeta) item.getItemMeta();
                        BlockState bs = bsm.getBlockState();
                        ((CreatureSpawner) bs).setSpawnedType(entity);
                        bsm.setBlockState(bs);
                        item.setItemMeta(bsm);
                        ItemMeta meta = item.getItemMeta();
                        List < String > lore = new ArrayList < String > ();
                        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&b" + capitalizeString(entity.toString().replace('_', ' ')) + " Spawner"));
                        lore.clear();
                        lore.add(ChatColor.translateAlternateColorCodes('&', "&8----------------"));
                        lore.add(ChatColor.translateAlternateColorCodes('&', "&bType: &6" + recap(capitalizeString(entity.toString().replace('_', ' ')))));
                        lore.add(ChatColor.translateAlternateColorCodes('&', "&bSpeed Level: &6" + levels[0]));
                        lore.add(ChatColor.translateAlternateColorCodes('&', "&bQuantity Level: &6" + levels[1]));
                        lore.add(ChatColor.translateAlternateColorCodes('&', "&bDrop Level: &6" + levels[2]));
                        lore.add(ChatColor.translateAlternateColorCodes('&', "&8----------------"));
                        meta.setLore(lore);
                        item.setItemMeta(meta);
                        location.getWorld().dropItem(location, item);
                    }
                }
                SpawnerLevels.remove(location);
        	} else {
        		event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.insufficient-permission")));
        		event.setCancelled(true);
        	    return;
        	}
        }
    }

    @EventHandler()
    public void onMobSpawn(SpawnerSpawnEvent event) {
        event.setCancelled(true);
    }

    @EventHandler()
    public void onBlockPlace(BlockPlaceEvent event) {
        ItemStack item = event.getItemInHand();
        if (event.getBlock().getType().equals(Material.SPAWNER)) {
        	if (event.getPlayer().hasPermission("spawnersplus.placespawner")) {
	            if (item.hasItemMeta()) {
	                List < String > lore = event.getItemInHand().getItemMeta().getLore();
	                CreatureSpawner cs = (CreatureSpawner) event.getBlock().getState();
	                EntityType entity;
	                try {
	                    entity = EntityType.valueOf(ChatColor.stripColor(lore.get(1)).replace("Type: ", "").replace(' ', '_').toUpperCase());
	                } catch (Exception e) {
	                    event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("messages.unknown-entity")));
	                    entity = EntityType.PIG;
	                }
	                cs.setSpawnedType(entity);
	                cs.update();
	                int speed = Integer.parseInt(ChatColor.stripColor(lore.get(2)).replace("Speed Level: ", ""));
	                int quantity = Integer.parseInt(ChatColor.stripColor(lore.get(3)).replace("Quantity Level: ", ""));
	                int drop = Integer.parseInt(ChatColor.stripColor(lore.get(4)).replace("Drop Level: ", ""));
	                int[] levels = new int[] {
	                    speed,
	                    quantity,
	                    drop
	                };
	                SpawnerLevels.put(event.getBlock().getLocation(), levels);
	                return;
	            }
	            if (!SpawnerLevels.containsKey(event.getBlock().getLocation())) {
	                newSpawnerAt(event.getBlock().getLocation());
	            }
        	} else {
        		event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.insufficient-permission")));
        		event.setCancelled(true);
        	    return;
        	}
        }
    }

    @EventHandler()
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        Location location = OpenSpawnerLocation.get(player);
        Boolean invtest = false;
        if (event.getInventory().equals(inv)) invtest = true;
    	for(Inventory page : shoppages) {
    		if (event.getInventory().equals(page)) invtest = true;
    	}
    	if(invtest == false) return;
        if (event.getCurrentItem() == null) return;
        if (event.getCurrentItem().getItemMeta() == null) return;
        if (event.getCurrentItem().getItemMeta().getDisplayName() == null) return;
        event.setCancelled(true);

        //if speed button clicked
        if (event.getCurrentItem().getItemMeta().getDisplayName().contains("Speed")) {
        	if (event.getWhoClicked().hasPermission("spawnersplus.upgradespeed")) {
                int[] levels = SpawnerLevels.get(location);
                //if level cap has not been reached or exceeded
                if (levels[0] + 1 <= this.getConfig().getInt("Speed.UpgradeCap")) {
                    //calculate price based on level and price multiplier
                    double price = calculatePrice(MultiplyNumTimes(this.getConfig().getDouble("Speed.StartingPrice"), this.getConfig().getDouble("Speed.PriceMultiplier"), levels[0] - 1));
                    //if enough money
                    if (eco.getBalance(player) >= price) {
                        //Withdraw money
                        eco.withdrawPlayer(player, price);
                        //Increase level
                        levels[0] += 1;
                        //Update GUI
                        SpawnerLevels.put(location, levels);
                        //Refresh players GUI
                        this.openSpawnerGUI(player, SpawnerLevels.get(location));
                        //Send player success message
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aSpawner speed upgraded for " + price + "$"));
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aNew balance: " + eco.getBalance(player) + "$"));
                    } else {
                    	player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.insufficient-balance")));
                        player.closeInventory();
                    }
                }
                return;
        	} else {
        		event.getWhoClicked().sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.insufficient-permission")));
                player.closeInventory();
        	    return;
        	}
        }
        
        if (event.getCurrentItem().getItemMeta().getDisplayName().contains("Quantity")) {
        	if (event.getWhoClicked().hasPermission("spawnersplus.upgradequantity")) {
	            int[] levels = SpawnerLevels.get(location);
	            if (levels[1] + 1 <= this.getConfig().getInt("Quantity.UpgradeCap")) {
	                double price = calculatePrice(MultiplyNumTimes(this.getConfig().getDouble("Quantity.StartingPrice"), this.getConfig().getDouble("Quantity.PriceMultiplier"), levels[1] - 1));
	                if (eco.getBalance(player) >= price) {
	                    eco.withdrawPlayer(player, price);
	                    levels[1] += 1;
	                    SpawnerLevels.put(location, levels);
	                    this.openSpawnerGUI(player, SpawnerLevels.get(location));
	                    //Send player success message
	                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aSpawner quantity upgraded for " + price + "$"));
	                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aNew balance: " + eco.getBalance(player) + "$"));
	                } else {
	                	player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.insufficient-balance")));
	                    player.closeInventory();
	                }
	            }
	            return;
        	} else {
        		event.getWhoClicked().sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.insufficient-permission")));
                player.closeInventory();
        	    return;
        	}
        }
        
        if (event.getCurrentItem().getItemMeta().getDisplayName().contains("Drop")) {
        	if (event.getWhoClicked().hasPermission("spawnersplus.upgradedrop")) {
	            int[] levels = SpawnerLevels.get(location);
	            CreatureSpawner cr = (CreatureSpawner) location.getBlock().getState();
	            EntityType entity = cr.getSpawnedType();
	            if (levels[2] + 1 <= this.getConfig().getConfigurationSection("Drop.Drops." + entity).getKeys(false).size()) {
	                double price = this.getConfig().getInt("Drop.Drops." + entity + "." + (levels[2]+1) + ".Cost");
	                if (eco.getBalance(player) >= price) {
	                    eco.withdrawPlayer(player, price);
	                    levels[2] += 1;
	                    SpawnerLevels.put(location, levels);
	                    this.openSpawnerGUI(player, SpawnerLevels.get(location));
	                    //Send player success message
	                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aSpawner drops upgraded for " + price + "$"));
	                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aNew balance: " + eco.getBalance(player) + "$"));
	                } else {
	                	player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.insufficient-balance")));
	                    player.closeInventory();
	                }
	            }
	            return;
	        } else {
	    		event.getWhoClicked().sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.insufficient-permission")));
	            player.closeInventory();
	    	    return;
	    	}
        }
        
        if (event.getCurrentItem().getItemMeta().getDisplayName().contains("Close Menu")) {
            player.closeInventory();
            return;
        }
        
        if(event.getCurrentItem().getItemMeta().getDisplayName().contains(" Spawner") && event.getCurrentItem().getItemMeta().getDisplayName().contains(ChatColor.GOLD + "")) {
	        if (EntityType.valueOf(ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName().replace(' ', '_').replace("_Spawner", "").toUpperCase())) != null) {
	        	EntityType entity = EntityType.valueOf(ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName().replace(" Spawner", "").replace(' ', '_').toUpperCase()));
	            double price = this.getConfig().getInt("Drop.Drops." + entity + ".1.Cost");
	            if (eco.getBalance(player) >= price) {
	                eco.withdrawPlayer(player, price);
	                //Give Player Spawner Right Here
	                ItemStack item = new ItemStack(Material.SPAWNER, 1);
	                BlockStateMeta bsm = (BlockStateMeta) item.getItemMeta();
	                BlockState bs = bsm.getBlockState();
	                ((CreatureSpawner) bs).setSpawnedType(entity);
	                bsm.setBlockState(bs);
	                item.setItemMeta(bsm);
	                ItemMeta meta = item.getItemMeta();
	                List < String > lore = new ArrayList < String > ();
	                meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&b" + capitalizeString(entity.toString().replace('_', ' ')) + " Spawner"));
	                lore.clear();
	                lore.add(ChatColor.translateAlternateColorCodes('&', "&8----------------"));
	                lore.add(ChatColor.translateAlternateColorCodes('&', "&bType: &6" + recap(capitalizeString(entity.toString().replace('_', ' ')))));
	                lore.add(ChatColor.translateAlternateColorCodes('&', "&bSpeed Level: &6" + 1));
	                lore.add(ChatColor.translateAlternateColorCodes('&', "&bQuantity Level: &6" + 1));
	                lore.add(ChatColor.translateAlternateColorCodes('&', "&bDrop Level: &6" + 1));
	                lore.add(ChatColor.translateAlternateColorCodes('&', "&8----------------"));
	                meta.setLore(lore);
	                item.setItemMeta(meta);
	                player.getInventory().addItem(item);
	                //Send player success message
	                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aSpawner purchased for " + price + "$"));
	                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aNew balance: " + eco.getBalance(player) + "$"));
	            	//player.closeInventory();
	            } else {
	            	player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.insufficient-balance")));
	                //player.closeInventory();
	            }
	        }
        }
        
        if(event.getCurrentItem().getItemMeta().getDisplayName().contains("Next")) {
        	int page = Integer.parseInt(ChatColor.stripColor(player.getOpenInventory().getItem(49).getItemMeta().getLore().get(1).replace("Page: ", "")));
        	player.closeInventory();
        	player.openInventory(shoppages.get(page));
        }
        
        if(event.getCurrentItem().getItemMeta().getDisplayName().contains("Previous")) {
        	int page = Integer.parseInt(ChatColor.stripColor(player.getOpenInventory().getItem(49).getItemMeta().getLore().get(1).replace("Page: ", "")));
        	player.closeInventory();
        	player.openInventory(shoppages.get(page-2));
        }
        return;
    }

    @EventHandler()
    public void onRightClick(PlayerInteractEvent event) {
        Action action = event.getAction();
        if (action.equals(Action.RIGHT_CLICK_BLOCK)) {
            Player player = event.getPlayer();
            final Block block = event.getClickedBlock();
            Location location = block.getLocation();
            if (block.getType().equals(Material.SPAWNER)) {
                if (!player.isSneaking()) {
                    event.setCancelled(true);
                    //On Right Click Spawner
                    if (!SpawnerLevels.containsKey(location)) {
                        this.newSpawnerAt(location);
                    }
                    OpenSpawnerLocation.put(player, location);
                    this.openSpawnerGUI(player, SpawnerLevels.get(location));
                }
            }
        }
    }

    @EventHandler()
    public void onMobDeath(EntityDeathEvent event) {
        //If has custom name
        if (event.getEntity().isCustomNameVisible()) {
            //If has quantity
            String isQuantity = event.getEntity().getName().split(":")[0];
            if (isQuantity.charAt(isQuantity.length() - 1) == 'X') {
                //stop normal mob drops from droppping
                event.getDrops().clear();
                event.setDroppedExp(0);
            }
        }
    }

    @SuppressWarnings("deprecation")
	@EventHandler()
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof LivingEntity)) {
            return;
        }
        LivingEntity le = (LivingEntity) event.getEntity();
        
        //If has Custom Name
        if(!le.isCustomNameVisible()) {
        	return;
        }

        //If has Quantity
        String[] entityInfo = ChatColor.stripColor(event.getEntity().getName()).split(" ");
        if(!entityInfo[0].contains("X")) {
        	return;
        }
        
        //If has LVL
        if(!le.getName().toString().contains("LVL: ")) {
        	return;
        }
        
        //Create useful variables
        int quantity = Integer.parseInt(entityInfo[0].replace("X", ""));
        int level = Integer.parseInt(entityInfo[entityInfo.length - 1]);
        Location location = le.getLocation();
        EntityType entity = le.getType();
        Player damager = (Player) event.getDamager();

        //if Entity Dies
        if (!(le.getHealth() - event.getDamage() < 1)) {
            return;
        }

        //If is a Stacked Mob
        if (!le.isCustomNameVisible()) {
            return;
        }
        if (!(entityInfo[0].charAt(entityInfo[0].length() - 1) == 'X') && (le.getName().toString().contains("LVL: "))) {
            return;
        }

        //Get Looting
        int looting = 0;
        if (event.getDamager() instanceof Player) {
            looting = damager.getInventory().getItemInMainHand().getEnchantmentLevel(Enchantment.LOOT_BONUS_MOBS);
        }

        //Fire Death
        Boolean fire = false;
        if (le.getFireTicks() != -1 || damager.getInventory().getItemInMainHand().containsEnchantment(Enchantment.FIRE_ASPECT)) {
            fire = true;
        }

        //Drops items
        ArrayList < ItemStack > drops = getDrops(entity, level, looting, fire);
        for (ItemStack drop: drops) {;
            le.getWorld().dropItem(location, drop);
        }

        //Drops XP
        int xp = 0;
        if (this.getConfig().contains("Drop.Drops." + entity + "." + level + ".XP")) {
            if (this.getConfig().isInt("Drop.Drops." + entity + "." + level + ".XP")) {
                //if xp is set amount
                xp = this.getConfig().getInt("Drop.Drops." + entity + "." + level + ".XP");
            } else {
                //if xp is a range
                int XpMin = Integer.parseInt(this.getConfig().getString("Drop.Drops." + entity + "." + level + ".XP").split("-")[0]);
                int XpMax = Integer.parseInt(this.getConfig().getString("Drop.Drops." + entity + "." + level + ".XP").split("-")[1]);
                xp = (int)(Math.round((Math.random() * (XpMax - XpMin))) + XpMin);
            }
            ExperienceOrb orb = le.getWorld().spawn(le.getLocation(), ExperienceOrb.class);
            orb.setExperience(xp);
        }

        //if there is more than one mob cancel the death
        if (quantity > 1) {
            event.setCancelled(true);
            le.setCustomName(ChatColor.translateAlternateColorCodes('&', "&e&l" + (quantity - 1) + "X &6&l" + capitalizeString(event.getEntityType().toString().replace('_', ' ')) + " LVL: " + level));
            le.setHealth(le.getMaxHealth());
        }
    }

    @SuppressWarnings("deprecation")
	@EventHandler()
    public void onEnviromentDamage(EntityDamageEvent event) {
    	
        //If entity is a mob
        if (!(event.getEntity() instanceof LivingEntity)) {
            return;
        }
        LivingEntity le = (LivingEntity) event.getEntity();
        
        //If has Custom Name
        if(!le.isCustomNameVisible()) {
        	return;
        }

        //If has Quantity
        String[] entityInfo = ChatColor.stripColor(event.getEntity().getName()).split(" ");
        if(!entityInfo[0].contains("X")) {
        	return;
        }
        
        //If has LVL
        if(!le.getName().toString().contains("LVL: ")) {
        	return;
        }
        
        //Create useful variables
        int quantity = Integer.parseInt(entityInfo[0].replace("X", ""));
        int level = Integer.parseInt(entityInfo[entityInfo.length - 1]);
        Location location = le.getLocation();
        EntityType entity = le.getType();

        //If Should be attack
        if (event.getCause().equals(DamageCause.ENTITY_ATTACK) || event.getCause().equals(DamageCause.ENTITY_SWEEP_ATTACK)) {
            return;
        }

        //if Entity Dies
        if (!(le.getHealth() - event.getDamage() < 1)) {
            return;
        }

        //If is a Stacked Mob
        if (!le.isCustomNameVisible()) {
            return;
        }
        if (!(entityInfo[0].charAt(entityInfo[0].length() - 1) == 'X') && (le.getName().toString().contains("LVL: "))) {
            return;
        }

        //Get Looting
        int looting = 0;

        //Fire Death
        Boolean fire = false;
        if (le.getFireTicks() != -1) {
            fire = true;
        }

        //Drops items
        ArrayList < ItemStack > drops = getDrops(entity, level, looting, fire);
        for (ItemStack drop: drops) {;
            le.getWorld().dropItem(location, drop);
        }

        //Drops XP
        int xp = 0;
        if (this.getConfig().contains("Drop.Drops." + entity + "." + level + ".XP")) {
            if (this.getConfig().isInt("Drop.Drops." + entity + "." + level + ".XP")) {
                //if xp is set amount
                xp = this.getConfig().getInt("Drop.Drops." + entity + "." + level + ".XP");
            } else {
                //if xp is a range
                int XpMin = Integer.parseInt(this.getConfig().getString("Drop.Drops." + entity + "." + level + ".XP").split("-")[0]);
                int XpMax = Integer.parseInt(this.getConfig().getString("Drop.Drops." + entity + "." + level + ".XP").split("-")[1]);
                xp = (int)(Math.round((Math.random() * (XpMax - XpMin))) + XpMin);
            }
            ExperienceOrb orb = le.getWorld().spawn(le.getLocation(), ExperienceOrb.class);
            orb.setExperience(xp);
        }

        //if there is more than one mob cancel the death
        if (quantity > 1) {
            event.setCancelled(true);
            le.setCustomName(ChatColor.translateAlternateColorCodes('&', "&e&l" + (quantity - 1) + "X &6&l" + capitalizeString(event.getEntityType().toString().replace('_', ' ')) + " LVL: " + level));
            le.setHealth(le.getMaxHealth());
        }
    }

    /**
     * Commands
     */
    
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (label.equalsIgnoreCase("spawnersplus") || label.equalsIgnoreCase("sp")) {
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
                if (args[0].equalsIgnoreCase("givespawner") || args[0].equalsIgnoreCase("gs") || args[0].equalsIgnoreCase("spawner") || args[0].equalsIgnoreCase("give")) {
	            	GiveSpawnerCommand(sender, label, args);
                	return true;
                }
                if (args[0].equalsIgnoreCase("setspawner") || args[0].equalsIgnoreCase("ss") || args[0].equalsIgnoreCase("replace") || args[0].equalsIgnoreCase("set")) {
	            	//SetSpawnerCommand(sender, label, args);
                	//return true;
                }
                if (args[0].equalsIgnoreCase("setprice") || args[0].equalsIgnoreCase("sp") || args[0].equalsIgnoreCase("price")) {
                	SetPriceCommand(sender, label, args);
                	return true;
                }
                if (args[0].equalsIgnoreCase("shop") || args[0].equalsIgnoreCase("spawnershop") || args[0].equalsIgnoreCase("buy") || args[0].equalsIgnoreCase("shopspawner")) {
                	ShopCommand(sender, label, args);
                	return true;
                }
                System.out.println(this.getConfig().getString("Messages.no-console"));
                return false;
            }
        	HelpCommand(sender, label, args);
        	return true;
        }
		return false;
    }
    
    public void HelpCommand(CommandSender sender, String label, String[] args) {
    	if(sender instanceof Player) {
    		Player player = (Player) sender;
        	if (player.hasPermission("spawnersplus.help")) {
		    	player.sendMessage(ChatColor.GRAY + "-------------------");
		        player.sendMessage(ChatColor.GREEN + "Spawners++ Commands");
		        player.sendMessage(ChatColor.GRAY + "-------------------");
		        if (player.hasPermission("spawnersplus.givespawner")) {
		            player.sendMessage(ChatColor.GREEN + "/sp givespawner <Optional:Player> <Optional:Amount> <EntityType> <Optional:Speed> <Optional:Quantity> <Optional:Drops>");
		        }
		        if (player.hasPermission("spawnersplus.help")) {
		            player.sendMessage(ChatColor.GREEN + "/sp help");
		        }
		        if (player.hasPermission("spawnersplus.reload")) {
		            player.sendMessage(ChatColor.GREEN + "/sp reload");
		        }
		        if (player.hasPermission("spawnersplus.setspawner")) {
		        	player.sendMessage(ChatColor.GREEN + "/sp setspawner <EntityType>");
		        }
		    } else {
		        player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.insufficient-permission")));
		    }
    	} else {
    		System.out.print("-------------------");
    		System.out.print("Spawners++ Commands");
    		System.out.print("-------------------");
    		System.out.print("/sp givespawner <Optional:Player> <Optional:Amount> <EntityType> <Optional:Speed> <Optional:Quantity> <Optional:Drops>");
    		System.out.print("/sp help");
    		System.out.print("/sp reload");
    		System.out.print("/sp setspawner <EntityType>");
    	}
    	return;
    }
    
    public void ReloadCommand(CommandSender sender, String label, String[] args) {
    	if(sender instanceof Player) {
    		Player player = (Player) sender;
            if (player.hasPermission("spawnersplus.reload")) {
                this.saveDefaultConfig();
                this.reloadConfig();
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.prefix") + " " + this.getConfig().getString("reload-success")));
                return;
            } else {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.insufficient-permission")));
            }
    	} else {
            this.saveDefaultConfig();
            this.reloadConfig();
            System.out.println(ChatColor.stripColor(this.getConfig().getString("Messages.prefix") + " " + this.getConfig().getString("reload-success")));
    	}	
    }
    
    public void GiveSpawnerCommand(CommandSender sender, String label, String[] args) {
    	if(sender instanceof Player) {
    		Player player = (Player) sender;
    		if (player.hasPermission("spawnersplus.givespawner")) {
    			int currentArg = 1;
    			// command: /sp gs player quantity type type level level level
    			if (args.length > 1) {
    				
    				//Check for defined player
    				Player toPlayer = player;
    				if (Bukkit.getPlayer(args[currentArg]) != null) {
    					toPlayer = Bukkit.getPlayer(args[currentArg]);
    					currentArg = currentArg + 1;
    				}
    				int quantity = 1;
    				
    				//Check for defined quantity
    				if (isNum(args[currentArg])) {
    					quantity = Integer.parseInt(args[currentArg]);
    					if(quantity<1) {
    						player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.invalid-quantity")));
    						return;
    					}
    					currentArg += 1;
    				}
    				
    				//Check defined EntityType
    				String input = "";
    				for (int i = currentArg; i <= args.length - 1; i++) {
    					if (!isNum(args[i])) {
    						if (args[i] != args[currentArg]) {
    							input = input + "_";
    						}
    						input = input + args[i].toUpperCase();
    					}
    				}
    				
    				//Check is EntityType is valid
    				EntityType entity;
    				try {
    					entity = EntityType.valueOf(input);
    				} catch (Exception e) {
    					player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.unknown-entity")));
    					return;
    				}
    				
    				//Check for defined levels
    				int[] levels = new int[] {1,1,1};
    				if (args.length >= 5) {
    					int max = args.length - 1;
    					if (isNum(args[max]) && isNum(args[max - 1]) && isNum(args[max - 2])) {
    						if ((Integer.parseInt(args[max]) > 0) && (Integer.parseInt(args[max-1]) > 0) && (Integer.parseInt(args[max-2]) > 0)) {
    							int levelcap = this.getConfig().getInt("Speed.UpgradeCap");
    							if ((Integer.parseInt(args[max - 2])) <= (levelcap)) {
    								levelcap = this.getConfig().getInt("Quantity.UpgradeCap");
    								if ((Integer.parseInt(args[max - 1])) <= (levelcap)) {
    									levelcap = 1;
    									if(!this.getConfig().isString("Drop.Drops." + entity)) {
    										levelcap = this.getConfig().getConfigurationSection("Drop.Drops." + entity).getKeys(false).size();
    									}
    									if ((Integer.parseInt(args[max])) <= levelcap) {
    										levels = new int[] {Integer.parseInt(args[max - 2]), Integer.parseInt(args[max - 1]), Integer.parseInt(args[max])};
    									} else {
    										player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.invalid-level")));
    										return;
    									}
    								} else {
    									player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.invalid-level")));
    									return;
    								}
    							} else {
    								player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.invalid-level")));
    								return;
    							}
    						} else {
    							player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.invalid-level")));
    							return;
    						}
    					}
    				}
    				
    				//Create item from defined values
    				ItemStack item = new ItemStack(Material.SPAWNER, quantity);
    				BlockStateMeta bsm = (BlockStateMeta) item.getItemMeta();
    				BlockState bs = bsm.getBlockState();
    				((CreatureSpawner) bs).setSpawnedType(entity);
    				bsm.setBlockState(bs);
    				item.setItemMeta(bsm);
    				ItemMeta meta = item.getItemMeta();
    				List < String > lore = new ArrayList < String > ();
    				meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&b" + capitalizeString(entity.toString().replace('_', ' ')) + " Spawner"));
    				lore.clear();
    				lore.add(ChatColor.translateAlternateColorCodes('&', "&8----------------"));
    				lore.add(ChatColor.translateAlternateColorCodes('&', "&bType: &6" + recap(capitalizeString(entity.toString().replace('_', ' ')))));
    				lore.add(ChatColor.translateAlternateColorCodes('&', "&bSpeed Level: &6" + levels[0]));
    				lore.add(ChatColor.translateAlternateColorCodes('&', "&bQuantity Level: &6" + levels[1]));
    				lore.add(ChatColor.translateAlternateColorCodes('&', "&bDrop Level: &6" + levels[2]));
    				lore.add(ChatColor.translateAlternateColorCodes('&', "&8----------------"));
    				meta.setLore(lore);
    				item.setItemMeta(meta);
    				toPlayer.getInventory().addItem(item);
    				return;
    			} else {
            		player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("incorrect-arg-count")));
    				return;
    			}
    		} else {
    			System.out.println(this.getConfig().getString("Messages.no-console"));
    			return;
    		}
    	}
    }
    
    public void SetSpawnerCommand(CommandSender sender, String label, String[] args) {
    	if(sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("spawnersplus.setspawner")) {
                if (player.getInventory().getItemInMainHand().getType() == Material.SPAWNER) {
                    String input = "";
                    for (String arg: args) {
                        if (arg != args[0]) {
                            if (arg != args[1]) {
                                input = input + "_";
                            }
                            input = input + arg.toUpperCase();
                        }
                    }
                    EntityType entity = EntityType.valueOf(input);
                    if (entity == null) {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.unknown-entity")));
                        return;
                    }
                    ItemStack item = player.getInventory().getItemInMainHand();
                    //changes entity type in nbt data
                    BlockStateMeta bsm = (BlockStateMeta) item.getItemMeta();
                    BlockState bs = bsm.getBlockState();
                    ((CreatureSpawner) bs).setSpawnedType(entity);
                    bsm.setBlockState(bs);
                    item.setItemMeta(bsm);
                    ItemMeta meta = item.getItemMeta();
                    List < String > lore = item.getItemMeta().getLore();
                    //changes entity type in name
                    meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&b" + capitalizeString(entity.toString().replace('_', ' ')) + " Spawner"));
                    //changes entity type in meta
                    lore.set(1, ChatColor.translateAlternateColorCodes('&', "&bType: &6" + recap(capitalizeString(entity.toString().replace('_', ' ')))));
                    meta.setLore(lore);
                    item.setItemMeta(meta);
                } else {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.holding-spawner")));
                    return;
                }
            } else {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.insufficient-permission")));
                return;
            }
        } else {
            System.out.println(this.getConfig().getString("Messages.no-console"));
            return;
        }
    }
    
    public void SetPriceCommand(CommandSender sender, String label, String[] args) {
    	//sp setprice entity level price
        if(sender instanceof Player) {
        	Player player = (Player) sender;
        	
        	//If Player has Permission
            if(!player.hasPermission("spawnersplus.setprice")) {
           		player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.insufficient-permission")));
           		return;
            }
            
            //If Arg Count is Correct
            if(args.length != 4) {
            	player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("incorrect-arg-count")));
            	return;
            }
            
            //If Entity is Valid
        	EntityType entity = EntityType.valueOf(args[1].toUpperCase());
        	if(entity == null) {
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.unknown-entity")));
        		return;
        	}
        	
        	//If Level is Valid
    		if(!isNum(args[2])) {
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.invalid-level")));
    			return;
    		}
    		int level = Integer.parseInt(args[2]);
			int levelcap = 1;
			if(!this.getConfig().isString("Drop.Drops." + entity)) {
				levelcap = this.getConfig().getConfigurationSection("Drop.Drops." + entity).getKeys(false).size();
			}
    		if(levelcap < level) {
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.invalid-level")));
    			return;
    		}
    		
    		//If Price is Valid
    		if(!isNum(args[3])) {
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.invalid-price")));
    			return;
    		}
    		int price = Integer.parseInt(args[3]);
    		if(!(price > 0)) {
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.invalid-price")));
    			return;
    		}
    		this.getConfig().set("Drop.Drops." + entity.toString().toUpperCase() + "." + level + ".Cost", price);
    		SetupShop();
       	} else {
    		System.out.println(this.getConfig().getString("Messages.no-console"));
    		return;
       	}
    }
    
    public void ShopCommand(CommandSender sender, String label, String[] args) {
        if(sender instanceof Player) {
        	Player player = (Player) sender;
            if (player.hasPermission("spawnersplus.setspawner")) {
            	player.closeInventory();
            	if(shoppages.size()>0) {
            		player.openInventory(shoppages.get(0));
            	}
            } else {
           		player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.insufficient-permission")));
           		return;
           	}
       	} else {
    		System.out.println(this.getConfig().getString("Messages.no-console"));
    		return;
       	}
	}
   
    /**
     * Reusable / Moved Code
     */
    
    public void openSpawnerGUI(Player player, int[] levels) {
        //get EntityType from location
        Location location = OpenSpawnerLocation.get(player);
        CreatureSpawner cs = (CreatureSpawner) location.getBlock().getState();
        EntityType entity = cs.getSpawnedType();
        if (player.hasPermission("spawnersplus.usespawner") || player.hasPermission("spawnersplus.usespawner." + entity.toString().toLowerCase())) {
            player.closeInventory();

            //Create new inventory
            ItemStack item = new ItemStack(Material.WATER);
            ItemMeta meta = item.getItemMeta();
            List < String > lore = new ArrayList < String > ();
            int slot = 0;
            int guisize = 27;
            String title = ChatColor.translateAlternateColorCodes('&', "&8&lSpawner Upgrade GUI");
            inv = Bukkit.createInventory(null, guisize, title);

            //fill blank spaces in GUI with filler block
            for (slot = 0; slot < guisize - 1; slot++) {
                Material material = Material.GRAY_STAINED_GLASS_PANE;
                item.setType(material);
                //Sets Name
                meta.setDisplayName(" ");
                //Sets Description
                lore.clear();
                meta.setLore(lore);
                //Applies changes
                item.setItemMeta(meta);
                inv.setItem(slot, item);
            }

            //Speed Upgrade
            item.setType(Material.SEA_LANTERN);
            int levelcap = this.getConfig().getInt("Speed.UpgradeCap");
            double defaultValue = this.getConfig().getDouble("Speed.DefaultSpeed");
            double multiplier = this.getConfig().getDouble("Speed.TimerMultiplier");
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&bSpeed Level: &6" + levels[0]));
            lore.clear();
            lore.add(ChatColor.translateAlternateColorCodes('&', "&8----------------"));
            if (levels[0] + 1 <= levelcap) {
                lore.add(ChatColor.translateAlternateColorCodes('&', "&bLevel: &6" + levels[0] + " &b-> &6" + (levels[0] + 1)));
                lore.add(ChatColor.translateAlternateColorCodes('&', "&bSpeed: &6" + Math.round((MultiplyNumTimes(defaultValue, multiplier, levels[0] - 1))) + "s &b-> &6" + Math.round(MultiplyNumTimes(defaultValue, multiplier, levels[0])) + "s"));
                lore.add(ChatColor.translateAlternateColorCodes('&', "&8----------------"));
                double price = calculatePrice(MultiplyNumTimes(this.getConfig().getDouble("Speed.StartingPrice"), this.getConfig().getDouble("Speed.PriceMultiplier"), levels[0] - 1));
                lore.add(ChatColor.translateAlternateColorCodes('&', "&bPrice: &6" + price));
            } else {
                lore.add(ChatColor.translateAlternateColorCodes('&', "&bLevel: &6" + levels[0]));
                lore.add(ChatColor.translateAlternateColorCodes('&', "&bSpeed: &6" + Math.round((MultiplyNumTimes(defaultValue, multiplier, levels[0] - 1))) + "s"));
                lore.add(ChatColor.translateAlternateColorCodes('&', "&8----------------"));
                lore.add(ChatColor.translateAlternateColorCodes('&', "&bPrice: &cMax Level"));
            }
            meta.setLore(lore);
            item.setItemMeta(meta);
            inv.setItem(11, item);

            //Quantity Upgrade
            item.setType(Material.MAGMA_BLOCK);
            levelcap = this.getConfig().getInt("Quantity.UpgradeCap");
            defaultValue = this.getConfig().getDouble("Quantity.DefaultQuantity");
            multiplier = this.getConfig().getDouble("Quantity.QuantityPerLevel");
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&bQuantity Level: &6" + levels[1]));
            lore.clear();
            lore.add(ChatColor.translateAlternateColorCodes('&', "&8----------------"));
            if (levels[1] + 1 <= levelcap) {
                String range = getConfig().getString("Quantity.Quantities." + (levels[1]));
                String torange = getConfig().getString("Quantity.Quantities." + (levels[1]+1));
                lore.add(ChatColor.translateAlternateColorCodes('&', "&bLevel: &6" + levels[1] + " &b-> &6" + (levels[1] + 1)));
                lore.add(ChatColor.translateAlternateColorCodes('&', "&bQuantity: &6" + range + "&b " + capitalizeString(entity.toString().replace('_', ' ')) + "'s -> &6" + torange + "&b " + capitalizeString(entity.toString().replace('_', ' ')) + "'s"));
                lore.add(ChatColor.translateAlternateColorCodes('&', "&8----------------"));
                double price = calculatePrice(MultiplyNumTimes(this.getConfig().getDouble("Quantity.StartingPrice"), this.getConfig().getDouble("Quantity.PriceMultiplier"), levels[1] - 1));
                lore.add(ChatColor.translateAlternateColorCodes('&', "&bPrice: &6" + price));
            } else {
                String range = getConfig().getString("Quantity.Quantities." + (levels[1]));
                lore.add(ChatColor.translateAlternateColorCodes('&', "&bLevel: &6" + levels[1]));
                lore.add(ChatColor.translateAlternateColorCodes('&', "&bQuantity: &6" + range + "&b " + capitalizeString(entity.toString().replace('_', ' ')) + "'s"));
                lore.add(ChatColor.translateAlternateColorCodes('&', "&8----------------"));
                lore.add(ChatColor.translateAlternateColorCodes('&', "&bPrice: &cMax Level"));
            }
            meta.setLore(lore);
            item.setItemMeta(meta);
            inv.setItem(13, item);

            //Drop Upgrade
            if (!this.getConfig().isString("Drop.Drops." + entity)) {
                item.setType(Material.IRON_BLOCK);
                levelcap = this.getConfig().getConfigurationSection("Drop.Drops." + entity).getKeys(false).size();
                meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&bDrop Level: &6" + levels[2]));
                lore.clear();
                lore.add(ChatColor.translateAlternateColorCodes('&', "&8----------------"));
                String str = "&bLevel: &6" + levels[2];
                if(levels[2] + 1 <= levelcap) {
                    str += " &b-> &6" + (levels[2] + 1);
                }
                lore.add(ChatColor.translateAlternateColorCodes('&', str));
                //Common Drops
                ArrayList < String > beforelist = getDropStats(entity, levels[2], 'C');
                if (beforelist.size() > 0) {
                    lore.add(ChatColor.translateAlternateColorCodes('&', "&bCommon Drops:"));
                    for (int i = 0; i < beforelist.size(); i++) {
                        String[] before = beforelist.get(i).split(":");
                        str = "&6" + before[1] + "x&b " + capitalizeString(before[0].replace('_', ' ')) + " (&6" + before[2] + "%&b)";
                        //If upgrade isnt above level cap show comparison to next levels stats
                        if(levels[2] + 1 <= levelcap) {
                            ArrayList < String > afterlist = getDropStats(entity, (levels[2]+1), 'C');
                            String[] after = afterlist.get(i).split(":");
                            str += " -> &6" + after[1] + "x&b " + capitalizeString(after[0].replace('_', ' ')) + " (&6" + after[2] + "%&b)";
                        }
                        lore.add(ChatColor.translateAlternateColorCodes('&', str));
                    }
                }
                //Rare Drops
                beforelist = getDropStats(entity, levels[2], 'R');
                if (beforelist.size() > 0) {
                    lore.add(ChatColor.translateAlternateColorCodes('&', "&bRare Drops:"));
                    for (int i = 0; i < beforelist.size(); i++) {
                        String[] before = beforelist.get(i).split(":");
                        str = "&6" + before[1] + "x&b " + capitalizeString(before[0].replace('_', ' ')) + " (&6" + before[2] + "%&b)";
                        //If upgrade isnt above level cap show comparison to next levels stats
                        if(levels[2] + 1 <= levelcap) {
                        	ArrayList < String > afterlist = getDropStats(entity, (levels[2]+1), 'R');
                            String[] after = afterlist.get(i).split(":");
                            str += " -> &6" + after[1] + "x&b " + capitalizeString(after[0].replace('_', ' ')) + " (&6" + after[2] + "%&b)";
                        }
                        lore.add(ChatColor.translateAlternateColorCodes('&', str));
                    }
                }
                lore.add(ChatColor.translateAlternateColorCodes('&', "&8----------------"));
                if(levels[2] + 1 <= levelcap) {
                    double price = this.getConfig().getInt("Drop.Drops." + entity + "." + (levels[2]+1) + ".Cost");
                	lore.add(ChatColor.translateAlternateColorCodes('&', "&bPrice: &6" + price));
                } else {
                    lore.add(ChatColor.translateAlternateColorCodes('&', "&bPrice: &cMax Level"));
                }
            }
            
            meta.setLore(lore);
            item.setItemMeta(meta);
            inv.setItem(15, item);
            
            //Close Menu Button
            item.setType(Material.BARRIER);
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&4&lClose Menu"));
            lore.clear();
            meta.setLore(lore);
            item.setItemMeta(meta);
            inv.setItem(guisize - 1, item);

            player.openInventory(inv);
        } else {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.insufficient-permission")));
            return;
        }
    }   

	public void SetupEntityHeads() {
    	EntityHead.put(EntityType.BAT, getCustomTextureHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzgyMGExMGRiMjIyZjY5YWMyMjE1ZDdkMTBkY2E0N2VlYWZhMjE1NTUzNzY0YTJiODFiYWZkNDc5ZTc5MzNkMSJ9fX0="));
    	EntityHead.put(EntityType.BEE, getCustomTextureHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZmEyY2I3NGMxMzI0NWQzY2U5YmFjYzhiMTYwMGFmMDJmZDdjOTFmNTAxZmVhZjk3MzY0ZTFmOGI2ZjA0ZjQ3ZiJ9fX0="));
        EntityHead.put(EntityType.BLAZE, getCustomTextureHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjc4ZWYyZTRjZjJjNDFhMmQxNGJmZGU5Y2FmZjEwMjE5ZjViMWJmNWIzNWE0OWViNTFjNjQ2Nzg4MmNiNWYwIn19fQ=="));
    	EntityHead.put(EntityType.CAT, getCustomTextureHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDViM2Y4Y2E0YjNhNTU1Y2NiM2QxOTQ0NDk4MDhiNGM5ZDc4MzMyNzE5NzgwMGQ0ZDY1OTc0Y2M2ODVhZjJlYSJ9fX0="));
    	EntityHead.put(EntityType.CAVE_SPIDER, getCustomTextureHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDE2NDVkZmQ3N2QwOTkyMzEwN2IzNDk2ZTk0ZWViNWMzMDMyOWY5N2VmYzk2ZWQ3NmUyMjZlOTgyMjQifX19"));
    	EntityHead.put(EntityType.CHICKEN, getCustomTextureHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTYzODQ2OWE1OTljZWVmNzIwNzUzNzYwMzI0OGE5YWIxMWZmNTkxZmQzNzhiZWE0NzM1YjM0NmE3ZmFlODkzIn19fQ=="));
    	EntityHead.put(EntityType.COD, getCustomTextureHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzg5MmQ3ZGQ2YWFkZjM1Zjg2ZGEyN2ZiNjNkYTRlZGRhMjExZGY5NmQyODI5ZjY5MTQ2MmE0ZmIxY2FiMCJ9fX0="));
    	EntityHead.put(EntityType.COW, getCustomTextureHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWQ2YzZlZGE5NDJmN2Y1ZjcxYzMxNjFjNzMwNmY0YWVkMzA3ZDgyODk1ZjlkMmIwN2FiNDUyNTcxOGVkYzUifX19"));
    	EntityHead.put(EntityType.CREEPER, new ItemStack(Material.CREEPER_HEAD, 1));
    	EntityHead.put(EntityType.DOLPHIN, getCustomTextureHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGU5Njg4Yjk1MGQ4ODBiNTViN2FhMmNmY2Q3NmU1YTBmYTk0YWFjNmQxNmY3OGU4MzNmNzQ0M2VhMjlmZWQzIn19fQ=="));
    	EntityHead.put(EntityType.DONKEY, getCustomTextureHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjE0NGJkYWQ2YmMxOGEzNzE2YjE5NmRjNGE0YmQ2OTUyNjVlY2NhYWRkMGQ5NDViZWI5NzY0NDNmODI2OTNiIn19fQ=="));
    	EntityHead.put(EntityType.DROWNED, getCustomTextureHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzg0ZGY3OWM0OTEwNGIxOThjZGFkNmQ5OWZkMGQwYmNmMTUzMWM5MmQ0YWI2MjY5ZTQwYjdkM2NiYmI4ZTk4YyJ9fX0="));
    	EntityHead.put(EntityType.ELDER_GUARDIAN, getCustomTextureHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMWM3OTc0ODJhMTRiZmNiODc3MjU3Y2IyY2ZmMWI2ZTZhOGI4NDEzMzM2ZmZiNGMyOWE2MTM5Mjc4YjQzNmIifX19"));
    	EntityHead.put(EntityType.ENDER_DRAGON, getCustomTextureHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmYzNjA2ZmY5NGRkNTI3NWE3YWE0ZTcyNDczZTZhYWZiMWZlNmIxMDQzMzhhMDUxZjA2N2ZiYmNjY2MxYTI2MyJ9fX0="));
    	EntityHead.put(EntityType.ENDERMAN, getCustomTextureHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvN2E1OWJiMGE3YTMyOTY1YjNkOTBkOGVhZmE4OTlkMTgzNWY0MjQ1MDllYWRkNGU2YjcwOWFkYTUwYjljZiJ9fX0="));
    	EntityHead.put(EntityType.ENDERMITE, getCustomTextureHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWJjN2I5ZDM2ZmI5MmI2YmYyOTJiZTczZDMyYzZjNWIwZWNjMjViNDQzMjNhNTQxZmFlMWYxZTY3ZTM5M2EzZSJ9fX0="));
    	EntityHead.put(EntityType.EVOKER, getCustomTextureHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDk1NDEzNWRjODIyMTM5NzhkYjQ3ODc3OGFlMTIxMzU5MWI5M2QyMjhkMzZkZDU0ZjFlYTFkYTQ4ZTdjYmE2In19fQ=="));
    	EntityHead.put(EntityType.FOX, getCustomTextureHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjRhMDM0NzQzNjQzNGViMTNkNTM3YjllYjZiNDViNmVmNGM1YTc4Zjg2ZTkxODYzZWY2MWQyYjhhNTNiODIifX19"));
    	EntityHead.put(EntityType.GHAST, getCustomTextureHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGI2YTcyMTM4ZDY5ZmJiZDJmZWEzZmEyNTFjYWJkODcxNTJlNGYxYzk3ZTVmOTg2YmY2ODU1NzFkYjNjYzAifX19"));
    	EntityHead.put(EntityType.GIANT, getCustomTextureHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTZmYzg1NGJiODRjZjRiNzY5NzI5Nzk3M2UwMmI3OWJjMTA2OTg0NjBiNTFhNjM5YzYwZTVlNDE3NzM0ZTExIn19fQ=="));
    	EntityHead.put(EntityType.GUARDIAN, getCustomTextureHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzI1YWY5NjZhMzI2ZjlkOTg0NjZhN2JmODU4MmNhNGRhNjQ1M2RlMjcxYjNiYzllNTlmNTdhOTliNjM1MTFjNiJ9fX0="));
    	EntityHead.put(EntityType.ILLUSIONER, getCustomTextureHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTEyNTEyZTdkMDE2YTIzNDNhN2JmZjFhNGNkMTUzNTdhYjg1MTU3OWYxMzg5YmQ0ZTNhMjRjYmViODhiIn19fQ=="));
    	EntityHead.put(EntityType.HORSE, getCustomTextureHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDJlYjk2N2FiOTRmZGQ0MWE2MzI1ZjEyNzdkNmRjMDE5MjI2ZTVjZjM0OTc3ZWVlNjk1OTdmYWZjZjVlIn19fQ=="));
    	EntityHead.put(EntityType.HUSK, getCustomTextureHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDY3NGM2M2M4ZGI1ZjRjYTYyOGQ2OWEzYjFmOGEzNmUyOWQ4ZmQ3NzVlMWE2YmRiNmNhYmI0YmU0ZGIxMjEifX19"));
    	EntityHead.put(EntityType.IRON_GOLEM, getCustomTextureHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODkwOTFkNzllYTBmNTllZjdlZjk0ZDdiYmE2ZTVmMTdmMmY3ZDQ1NzJjNDRmOTBmNzZjNDgxOWE3MTQifX19"));
    	EntityHead.put(EntityType.LLAMA, getCustomTextureHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmE1ZjEwZTZlNjIzMmYxODJmZTk2NmY1MDFmMWMzNzk5ZDQ1YWUxOTAzMWExZTQ5NDFiNWRlZTBmZWZmMDU5YiJ9fX0="));
    	EntityHead.put(EntityType.MAGMA_CUBE, getCustomTextureHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzg5NTdkNTAyM2M5MzdjNGM0MWFhMjQxMmQ0MzQxMGJkYTIzY2Y3OWE5ZjZhYjM2Yjc2ZmVmMmQ3YzQyOSJ9fX0="));
    	EntityHead.put(EntityType.MULE, getCustomTextureHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTA0ODZhNzQyZTdkZGEwYmFlNjFjZTJmNTVmYTEzNTI3ZjFjM2IzMzRjNTdjMDM0YmI0Y2YxMzJmYjVmNWYifX19"));
    	EntityHead.put(EntityType.MUSHROOM_COW, getCustomTextureHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDBiYzYxYjk3NTdhN2I4M2UwM2NkMjUwN2EyMTU3OTEzYzJjZjAxNmU3YzA5NmE0ZDZjZjFmZTFiOGRiIn19fQ=="));
    	EntityHead.put(EntityType.OCELOT, getCustomTextureHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTY1N2NkNWMyOTg5ZmY5NzU3MGZlYzRkZGNkYzY5MjZhNjhhMzM5MzI1MGMxYmUxZjBiMTE0YTFkYjEifX19"));
    	EntityHead.put(EntityType.PANDA, getCustomTextureHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzgxOGI2ODFjYWNlMWM2NDE5MTlmNTNlZGFkZWNiMTQyMzMwZDA4OWE4MjZiNTYyMTkxMzhjMzNiN2E1ZTBkYiJ9fX0="));
    	EntityHead.put(EntityType.PARROT, getCustomTextureHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTRiYThkNjZmZWNiMTk5MmU5NGI4Njg3ZDZhYjRhNTMyMGFiNzU5NGFjMTk0YTI2MTVlZDRkZjgxOGVkYmMzIn19fQ=="));
    	EntityHead.put(EntityType.PHANTOM, getCustomTextureHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvN2U5NTE1M2VjMjMyODRiMjgzZjAwZDE5ZDI5NzU2ZjI0NDMxM2EwNjFiNzBhYzAzYjk3ZDIzNmVlNTdiZDk4MiJ9fX0="));
    	EntityHead.put(EntityType.PIG, getCustomTextureHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjIxNjY4ZWY3Y2I3OWRkOWMyMmNlM2QxZjNmNGNiNmUyNTU5ODkzYjZkZjRhNDY5NTE0ZTY2N2MxNmFhNCJ9fX0="));
        EntityHead.put(EntityType.PIG_ZOMBIE, getCustomTextureHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzRlOWM2ZTk4NTgyZmZkOGZmOGZlYjMzMjJjZDE4NDljNDNmYjE2YjE1OGFiYjExY2E3YjQyZWRhNzc0M2ViIn19fQ=="));
    	EntityHead.put(EntityType.PILLAGER, getCustomTextureHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNGFlZTZiYjM3Y2JmYzkyYjBkODZkYjVhZGE0NzkwYzY0ZmY0NDY4ZDY4Yjg0OTQyZmRlMDQ0MDVlOGVmNTMzMyJ9fX0="));
    	EntityHead.put(EntityType.POLAR_BEAR, getCustomTextureHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDQyMTIzYWMxNWVmZmExYmE0NjQ2MjQ3Mjg3MWI4OGYxYjA5YzFkYjQ2NzYyMTM3NmUyZjcxNjU2ZDNmYmMifX19"));
    	EntityHead.put(EntityType.PUFFERFISH, getCustomTextureHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTcxNTI4NzZiYzNhOTZkZDJhMjI5OTI0NWVkYjNiZWVmNjQ3YzhhNTZhYzg4NTNhNjg3YzNlN2I1ZDhiYiJ9fX0="));
    	EntityHead.put(EntityType.RABBIT, getCustomTextureHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvN2QxMTY5YjI2OTRhNmFiYTgyNjM2MDk5MjM2NWJjZGE1YTEwYzg5YTNhYTJiNDhjNDM4NTMxZGQ4Njg1YzNhNyJ9fX0="));
    	EntityHead.put(EntityType.RAVAGER, getCustomTextureHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2QyMGJmNTJlYzM5MGEwNzk5Mjk5MTg0ZmM2NzhiZjg0Y2Y3MzJiYjFiZDc4ZmQxYzRiNDQxODU4ZjAyMzVhOCJ9fX0="));
    	EntityHead.put(EntityType.SALMON, getCustomTextureHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjBlYTlhMjIzNjIwY2RiNTRiMzU3NDEzZDQzYmQ4OWM0MDA4YmNhNmEyMjdmM2I3ZGI5N2Y3NzMzZWFkNWZjZiJ9fX0="));
    	EntityHead.put(EntityType.SHEEP, getCustomTextureHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjMxZjljY2M2YjNlMzJlY2YxM2I4YTExYWMyOWNkMzNkMThjOTVmYzczZGI4YTY2YzVkNjU3Y2NiOGJlNzAifX19"));
    	EntityHead.put(EntityType.SHULKER, getCustomTextureHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTQzM2E0YjczMjczYTY0YzhhYjI4MzBiMGZmZjc3N2E2MWE0ODhjOTJmNjBmODNiZmIzZTQyMWY0MjhhNDQifX19"));
    	EntityHead.put(EntityType.SILVERFISH, getCustomTextureHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZGE5MWRhYjgzOTFhZjVmZGE1NGFjZDJjMGIxOGZiZDgxOWI4NjVlMWE4ZjFkNjIzODEzZmE3NjFlOTI0NTQwIn19fQ=="));
    	EntityHead.put(EntityType.SKELETON, new ItemStack(Material.SKELETON_SKULL, 1));
    	EntityHead.put(EntityType.SKELETON_HORSE, getCustomTextureHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDdlZmZjZTM1MTMyYzg2ZmY3MmJjYWU3N2RmYmIxZDIyNTg3ZTk0ZGYzY2JjMjU3MGVkMTdjZjg5NzNhIn19fQ=="));
    	EntityHead.put(EntityType.SLIME, getCustomTextureHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTIwZTg0ZDMyZDFlOWM5MTlkM2ZkYmI1M2YyYjM3YmEyNzRjMTIxYzU3YjI4MTBlNWE0NzJmNDBkYWNmMDA0ZiJ9fX0="));
    	EntityHead.put(EntityType.SNOWMAN, getCustomTextureHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMWZkZmQxZjc1MzhjMDQwMjU4YmU3YTkxNDQ2ZGE4OWVkODQ1Y2M1ZWY3MjhlYjVlNjkwNTQzMzc4ZmNmNCJ9fX0="));
    	EntityHead.put(EntityType.SPIDER, getCustomTextureHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2Q1NDE1NDFkYWFmZjUwODk2Y2QyNThiZGJkZDRjZjgwYzNiYTgxNjczNTcyNjA3OGJmZTM5MzkyN2U1N2YxIn19fQ=="));
    	EntityHead.put(EntityType.SQUID, getCustomTextureHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDg3MDU2MjRkYWEyOTU2YWE0NTk1NmM4MWJhYjVmNGZkYjJjNzRhNTk2MDUxZTI0MTkyMDM5YWVhM2E4YjgifX19"));
    	EntityHead.put(EntityType.STRAY, getCustomTextureHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzhkZGY3NmU1NTVkZDVjNGFhOGEwYTVmYzU4NDUyMGNkNjNkNDg5YzI1M2RlOTY5ZjdmMjJmODVhOWEyZDU2In19fQ=="));
    	EntityHead.put(EntityType.TRADER_LLAMA, getCustomTextureHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODQyNDc4MGIzYzVjNTM1MWNmNDlmYjViZjQxZmNiMjg5NDkxZGY2YzQzMDY4M2M4NGQ3ODQ2MTg4ZGI0Zjg0ZCJ9fX0="));
    	EntityHead.put(EntityType.TROPICAL_FISH, getCustomTextureHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDZkZDVlNmFkZGI1NmFjYmM2OTRlYTRiYTU5MjNiMWIyNTY4ODE3OGZlZmZhNzIyOTAyOTllMjUwNWM5NzI4MSJ9fX0="));
    	EntityHead.put(EntityType.TURTLE, getCustomTextureHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMGE0MDUwZTdhYWNjNDUzOTIwMjY1OGZkYzMzOWRkMTgyZDdlMzIyZjlmYmNjNGQ1Zjk5YjU3MThhIn19fQ=="));
    	EntityHead.put(EntityType.VEX, getCustomTextureHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzJlYzVhNTE2NjE3ZmYxNTczY2QyZjlkNWYzOTY5ZjU2ZDU1NzVjNGZmNGVmZWZhYmQyYTE4ZGM3YWI5OGNkIn19fQ=="));
    	EntityHead.put(EntityType.VILLAGER, getCustomTextureHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODIyZDhlNzUxYzhmMmZkNGM4OTQyYzQ0YmRiMmY1Y2E0ZDhhZThlNTc1ZWQzZWIzNGMxOGE4NmU5M2IifX19"));
    	EntityHead.put(EntityType.WANDERING_TRADER, getCustomTextureHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWYxMzc5YTgyMjkwZDdhYmUxZWZhYWJiYzcwNzEwZmYyZWMwMmRkMzRhZGUzODZiYzAwYzkzMGM0NjFjZjkzMiJ9fX0="));
    	EntityHead.put(EntityType.WITCH, getCustomTextureHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjBlMTNkMTg0NzRmYzk0ZWQ1NWFlYjcwNjk1NjZlNDY4N2Q3NzNkYWMxNmY0YzNmODcyMmZjOTViZjlmMmRmYSJ9fX0="));
    	EntityHead.put(EntityType.WITHER, getCustomTextureHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2RmNzRlMzIzZWQ0MTQzNjk2NWY1YzU3ZGRmMjgxNWQ1MzMyZmU5OTllNjhmYmI5ZDZjZjVjOGJkNDEzOWYifX19"));
    	EntityHead.put(EntityType.WITHER_SKELETON, new ItemStack(Material.WITHER_SKELETON_SKULL, 1));
    	EntityHead.put(EntityType.WOLF, getCustomTextureHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjlkMWQzMTEzZWM0M2FjMjk2MWRkNTlmMjgxNzVmYjQ3MTg4NzNjNmM0NDhkZmNhODcyMjMxN2Q2NyJ9fX0="));
    	EntityHead.put(EntityType.ZOMBIE, new ItemStack(Material.ZOMBIE_HEAD, 1));
    	EntityHead.put(EntityType.ZOMBIE_HORSE, getCustomTextureHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDIyOTUwZjJkM2VmZGRiMThkZTg2ZjhmNTVhYzUxOGRjZTczZjEyYTZlMGY4NjM2ZDU1MWQ4ZWI0ODBjZWVjIn19fQ=="));
    	EntityHead.put(EntityType.ZOMBIE_VILLAGER, getCustomTextureHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjJiMzkzYmUyZGMyOTczZDQxYTgzNGUxOWRkNmI3M2I4NjY3ODJkNjg0YTA5N2ViZmU5OWNiMzkwMTk0ZiJ9fX0="));
	}

	public void SetupShop() {
		shoppages = new ArrayList<Inventory>();
        //Setup Variables
        int slot = 0;
        int guisize = 54;
        int invnum=0;
        String title = ChatColor.translateAlternateColorCodes('&', "&8&lSpawner Shop");
        ItemStack item = new ItemStack(Material.BARRIER);
        ItemMeta meta = item.getItemMeta();
        List < String > lore = new ArrayList < String > ();
        int cost = 0;
        //put all spawners that meet criteria into a list
        List<EntityType> spawners = new ArrayList <EntityType>();
        for(String entity : this.getConfig().getConfigurationSection("Drop.Drops").getKeys(false)) {
        	if(!this.getConfig().isString("Drop.Drops." + entity)) {
        		if(this.getConfig().getInt("Drop.Drops." + entity + ".1.Cost")!=-1) {
        			if(EntityType.valueOf(entity) != null) {
        				spawners.add(EntityType.valueOf(entity));
        			}
        		}
        	}
        }
        
        //Create glasspane item
        ItemStack clear = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        meta.setDisplayName(" ");
        clear.setItemMeta(meta);
        
        //setup the pages of the shop
        do {
        	//reset page variable
	        Inventory page = Bukkit.createInventory(null, guisize, title);
	        
	        //Fill with glasspanes
	        for (slot = 0; slot < guisize - 1; slot++) {
	        	page.setItem(slot, clear);
	        }
	        
	        //Add spawners to page
	        slot = 10;
	        while(slot < 35 && spawners.size() > 0) {
		        EntityType entity = spawners.get(0);
			    spawners.remove(0);
	    		cost = this.getConfig().getInt("Drop.Drops." + entity + ".1.Cost");
	            item = EntityHead.get(entity);
	            meta = item.getItemMeta();
	            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6&l" + capitalizeString(entity.toString().replace('_', ' ')) + " Spawner"));
	            lore.clear();
	            lore.add(ChatColor.translateAlternateColorCodes('&', "&8-----------"));
	            lore.add(ChatColor.translateAlternateColorCodes('&', "&ePrice: $" + cost));
	            meta.setLore(lore);
	            item.setItemMeta(meta);
	            page.setItem(slot, item);
	            slot += 1;
	            if(slot == 17)
	               	slot = 19;
	            if(slot == 26)
	               	slot = 28;
        	}
	        

	        //Buttons
	        if(invnum!=0) {
		        item = new ItemStack(Material.ARROW);
	            meta = item.getItemMeta();
		        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6Previous Page"));
		        item.setItemMeta(meta);
		        page.setItem(guisize - 6, item);
	        }

	        item = getCustomTextureHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmFkYzA0OGE3Y2U3OGY3ZGFkNzJhMDdkYTI3ZDg1YzA5MTY4ODFlNTUyMmVlZWQxZTNkYWYyMTdhMzhjMWEifX19");
	        meta = item.getItemMeta();
	        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6&lInfo"));
	        lore.clear();
            lore.add(ChatColor.translateAlternateColorCodes('&', "&8-----------"));
	        lore.add(ChatColor.translateAlternateColorCodes('&', "&ePage: " + (invnum+1)));
	        meta.setLore(lore);
	        item.setItemMeta(meta);
	        page.setItem(guisize - 5, item);
	        
	        if(spawners.size()!=0) {
		        item = new ItemStack(Material.ARROW);
	            meta = item.getItemMeta();
		        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6Next Page"));
		        item.setItemMeta(meta);
		        page.setItem(guisize - 4, item);
	        }

	        item = new ItemStack(Material.BARRIER);
            meta = item.getItemMeta();
	        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&4&lClose Menu"));
	        item.setItemMeta(meta);
	        page.setItem(guisize - 1, item);
	        
	        //prepare for next page
    		shoppages.add(page);
	        invnum += 1;
        } while(spawners.size() > 0);
	}

    public ItemStack getCustomTextureHead(String value) {
        @SuppressWarnings("deprecation")
		ItemStack head = new ItemStack(Material.PLAYER_HEAD, 1, (short)3);
        SkullMeta meta = (SkullMeta) head.getItemMeta();
        GameProfile profile = new GameProfile(UUID.randomUUID(), "");
        profile.getProperties().put("textures", new Property("textures", value));
        Field profileField = null;
        try {
            profileField = meta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(meta, profile);
        } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
            e.printStackTrace();
        }
        head.setItemMeta(meta);
        return head;
    }

    public boolean isNum(String num) {
        try {
            Integer.parseInt(num);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public void spawnMob(Location location) {
        if (location.getBlock().getType() == Material.SPAWNER) {
            if (!SpawnerLevels.containsKey(location)) {
                newSpawnerAt(location);
            }
            int[] levels = SpawnerLevels.get(location);
            CreatureSpawner cs = (CreatureSpawner) location.getBlock().getState();
            EntityType enType = cs.getSpawnedType();
            int spawnSearchRadius = 10;
            Location entityLocation;
            Location entityHead;
            int maxAttempts = 100;
            int attempts = 0;
            
            String range = getConfig().getString("Quantity.Quantities." + (levels[1]));
            int min = Integer.parseInt(range.split("-")[0]);
            int max = Integer.parseInt(range.split("-")[1]);
            int increase = (int) ((Math.random()*(max-min))+min);

            if(increase==0) {
            	return;
            }
            
            //Checks if entity can merge into another entity near the spawner
            for (Entity e: location.getWorld().getEntities()) {
                if (e.isCustomNameVisible()) { //if has custom name
                    String[] entityInfo = ChatColor.stripColor(e.getName()).split(" ");
                    if(entityInfo[0].contains("X") && e.getName().toString().contains("LVL: ")) {
	                    int quantity = Integer.parseInt(entityInfo[0].replace("X", ""));
	                    int level = Integer.parseInt(entityInfo[entityInfo.length - 1]);
	                    if (e.getType() == enType) { //if type is same
	                        if (level == levels[2]) { //if level is same
	                            if (location.getBlockX() - spawnSearchRadius < e.getLocation().getBlockX() && //If location is within spawnSearchRadius blocks
	                                location.getBlockX() + spawnSearchRadius > e.getLocation().getBlockX() &&
	                                location.getBlockY() - spawnSearchRadius < e.getLocation().getBlockY() &&
	                                location.getBlockY() + spawnSearchRadius > e.getLocation().getBlockY() &&
	                                location.getBlockZ() - spawnSearchRadius < e.getLocation().getBlockZ() &&
	                                location.getBlockZ() + spawnSearchRadius > e.getLocation().getBlockZ()) {
	                                e.setCustomName(ChatColor.translateAlternateColorCodes('&', "&e&l" + (quantity + increase) + "X &6&l" + capitalizeString(enType.toString().replace('_', ' ')) + " LVL: " + level));
	                                return;
	                            }
	                        }
	                    }
                    }
                }
            }

            //Find a safe place to spawn the entity
            do {
                World w = location.getWorld();
                double x = location.getBlockX() + (Math.random() - 0.5) * 10;
                double y = location.getBlockY() + (Math.random() - 0.5) * 4;
                double z = location.getBlockZ() + (Math.random() - 0.5) * 10;
                entityLocation = new Location(w, x, y, z);
                entityHead = new Location(w, x, y + 1, z);
                attempts += 1;
            } while ((entityLocation.getBlock().getType().isSolid() || entityHead.getBlock().getType().isSolid()) && attempts < maxAttempts);
            if (!(attempts < maxAttempts)) {
                return;
            }
            
            //Checks if entity can merge into another entity near the random location just generated
            for (Entity e: location.getWorld().getEntities()) {
                if (e.isCustomNameVisible()) { //if has custom name
                    //int[] toEnInfo = InfoFromName(e);
                    String[] entityInfo = ChatColor.stripColor(e.getName()).split(" ");
                    int quantity = Integer.parseInt(entityInfo[0].replace("X", ""));
                    int level = Integer.parseInt(entityInfo[entityInfo.length - 1]);
                    if (e.getType() == enType) { //if type is same
                        if (level == levels[2]) { //if level is same
                            if (entityLocation.getBlockX() - spawnSearchRadius < e.getLocation().getBlockX() && //If location is within spawnSearchRadius blocks
                                entityLocation.getBlockX() + spawnSearchRadius > e.getLocation().getBlockX() &&
                                entityLocation.getBlockY() - spawnSearchRadius < e.getLocation().getBlockY() &&
                                entityLocation.getBlockY() + spawnSearchRadius > e.getLocation().getBlockY() &&
                                entityLocation.getBlockZ() - spawnSearchRadius < e.getLocation().getBlockZ() &&
                                entityLocation.getBlockZ() + spawnSearchRadius > e.getLocation().getBlockZ()) {
                                e.setCustomName(ChatColor.translateAlternateColorCodes('&', "&e&l" + (quantity + increase) + "X &6&l" + capitalizeString(enType.toString().replace('_', ' ')) + " LVL: " + level));
                                return;
                            }
                        }
                    }
                }
            }

            //Spawn new Entity
            String entityName = ChatColor.translateAlternateColorCodes('&', "&e&l" + increase + "X &6&l" + capitalizeString(enType.toString().replace('_', ' ')) + " LVL: " + levels[2]);
            Entity newEntity = location.getWorld().spawnEntity(entityLocation, enType);
            newEntity.setCustomName(entityName);
            newEntity.setCustomNameVisible(true);
        }
    }

    public void merge() {
        for (World world: Bukkit.getWorlds()) {
            for (Entity a: world.getEntities()) {
                String[] aInfo = ChatColor.stripColor(a.getName()).split(" ");
                if(aInfo[0].contains("X") && a.getName().toString().contains("LVL: ")) {
                    for (Entity b: world.getEntities()) {
                        String[] bInfo = ChatColor.stripColor(b.getName()).split(" ");
                        if(bInfo[0].contains("X") && b.getName().toString().contains("LVL: ")) {
	                        if (a != b) {
	                            if (a.isCustomNameVisible() && b.isCustomNameVisible()) { //if has custom name
	                                if (a.getType() == b.getType()) { //if type is same
	                                    if (Integer.parseInt(aInfo[aInfo.length - 1]) == Integer.parseInt(bInfo[bInfo.length - 1])) { //if level is same
	                                        if (a.getLocation().getBlockX() - 10 < b.getLocation().getBlockX() && //If location is within spawnSearchRadius blocks
	                                            a.getLocation().getBlockX() + 10 > b.getLocation().getBlockX() &&
	                                            a.getLocation().getBlockY() - 10 < b.getLocation().getBlockY() &&
	                                            a.getLocation().getBlockY() + 10 > b.getLocation().getBlockY() &&
	                                            a.getLocation().getBlockZ() - 10 < b.getLocation().getBlockZ() &&
	                                            a.getLocation().getBlockZ() + 10 > b.getLocation().getBlockZ()) {
	                                            b.remove();
	                                            a.setCustomName(ChatColor.translateAlternateColorCodes('&', "&e&l" +
	                                                ((Integer.parseInt(aInfo[0].replace("X", "")) + (Integer.parseInt(bInfo[0].replace("X", ""))) + "X &6&l" + //Quantity
	                                                    capitalizeString(a.getType().toString().replace('_', ' ')) + //Type
	                                                    " LVL: " + aInfo[aInfo.length - 1].replace("LVL: ", ""))))); //Level
	                                            return;
	                                        }
	                                    }
	                                }
	                            }
	                        }
                        }
                    }
                }
            }
        }
    }

    public void newSpawnerAt(Location location) {
        if (location.getBlock().getState() instanceof CreatureSpawner) {
            int[] defaultlevels = new int[] {
                1,
                1,
                1
            };
            SpawnerLevels.put(location, defaultlevels);
        }
    }

    public double MultiplyNumTimes(double num, double multiply, int times) {
        for (int i = 0; i != times; i++) {
            num = num * multiply;
        }
        return num;
    }

    public double calculatePrice(double price) {
        return Double.parseDouble(df.format(price));
    }

    public ArrayList < ItemStack > getDrops(EntityType entity, int level, int looting, Boolean fire) {
        ArrayList < ItemStack > result = new ArrayList < ItemStack > ();
        Set < String > drops = this.getConfig().getConfigurationSection("Drop.Drops." + entity + "." + level).getKeys(false);
        for (String dropString: drops) {
            if (!dropString.contains("XP")) {
                //Declares Variables
                Material m = Material.matchMaterial(dropString);
                String[] dropInfo = this.getConfig().getString("Drop.Drops." + entity + "." + level + "." + dropString).split(":");
                int chance = Integer.parseInt(dropInfo[1].replace("%", ""));
                int dropcount = 0;
                String rarity = dropInfo[2];
                int min = 0;
                int max = 0;

                //Sets up dropcount if range or not
                if (dropInfo[0].split("-").length > 1) {
                    min = Integer.parseInt(dropInfo[0].split("-")[0]);
                    max = Integer.parseInt(dropInfo[0].split("-")[1]);

                    //Applies Common Item changes if looting item
                    if (looting != 0) {
                        if (rarity.charAt(0) == 'C') {
                            int nums[] = new int[looting + 1];
                            int multiplier = 1;
                            for (int i = 0; i < (looting + 1 + 1) / 2; i++) {
                                nums[i] = multiplier;
                                multiplier = multiplier * 2;
                            }
                            multiplier = 1;
                            for (int i = looting; i > (looting - 1) / 2; i = i - 1) {
                                nums[i] = multiplier;
                                multiplier = multiplier * 2;
                            }
                            int xcount = 0;
                            for (int i = 0; i < nums.length; i++) {
                                xcount += (nums[i]);
                            }
                            double random = Math.random() * xcount;
                            int minrange = 0;
                            int maxrange = 0;
                            for (int i = 0; i < nums.length; i++) {
                                maxrange = minrange + nums[i];
                                if (random > minrange && random < maxrange) {
                                    dropcount += i;
                                }
                                minrange = maxrange;
                            }
                        }
                    }
                }
                //Applies Rare item changes if looting item
                if (looting != 0) {
                    if (rarity.charAt(0) == 'R') {
                        chance += 1 * looting;
                    }
                }

                //Changes material to cooked variant if possible
                if (fire) {
                    if (m == Material.BEEF || m == Material.CHICKEN || m == Material.COD || m == Material.PORKCHOP || m == Material.RABBIT || m == Material.SALMON || m == Material.MUTTON) {
                        m = Material.matchMaterial("COOKED_" + m.name());
                    }
                }

                //Rolls to see if items should drop or not
                if (Math.random() * 100 < chance) {
                    if (dropInfo[0].split("-").length == 1) {
                        dropcount = Integer.parseInt(dropInfo[0]);
                        ItemStack drop = new ItemStack(m, dropcount);
                        result.add(drop);
                    } else {
                        dropcount += (int)(Math.round((Math.random()) * (max - min)) + min);
                        ItemStack drop = new ItemStack(m, dropcount);
                        result.add(drop);
                    }
                }
            }
        }
        return result;
    }

    public ArrayList < String > getDropStats(EntityType entity, int level, char inputRarity) {
        ArrayList < String > result = new ArrayList < String > ();
        Set < String > drops = this.getConfig().getConfigurationSection("Drop.Drops." + entity + "." + level).getKeys(false);
        for (String dropString : drops) {
            if ((!dropString.contains("XP")) && (!dropString.contains("Cost"))) {
                //Declares Variables
                Material m = Material.matchMaterial(dropString);
                String[] dropInfo = this.getConfig().getString("Drop.Drops." + entity + "." + level + "." + dropString).split(":");
                double chance = Double.parseDouble(dropInfo[1].replace("%", ""));
                int dropcount = 0;
                int min = 0;
                int max = 0;
                String rarity = dropInfo[2];
                //Sets up dropcount if range or not
                if (rarity.charAt(0) == inputRarity) {
                    if (dropInfo[0].split("-").length > 1) {
                        min = Integer.parseInt(dropInfo[0].split("-")[0]);
                        max = Integer.parseInt(dropInfo[0].split("-")[1]);
                        String drop = (m + ":" + min + "-" + max + ":" + chance);
                        result.add(drop);
                    } else {
                        dropcount = Integer.parseInt(dropInfo[0]);
                        String drop = (m + ":" + dropcount + ":" + chance);
                        result.add(drop);
                    }
                }
            }
        }
        return result;
    }

    public String recap(String input) {
        return input.toLowerCase().substring(0, 1).toUpperCase() + input.toLowerCase().substring(1);
    }

    public String capitalizeString(String string) {
        char[] chars = string.toLowerCase().toCharArray();
        boolean found = false;
        for (int i = 0; i < chars.length; i++) {
            if (!found && Character.isLetter(chars[i])) {
                chars[i] = Character.toUpperCase(chars[i]);
                found = true;
            } else if (Character.isWhitespace(chars[i]) || chars[i] == ' ') { // You can add other chars here
                found = false;
            }
        }
        return String.valueOf(chars);
    }
}