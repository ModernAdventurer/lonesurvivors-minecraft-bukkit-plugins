package me.LoneSurvivor.Supernatural;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_16_R1.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.IronGolem;
import org.bukkit.entity.LargeFireball;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.SmallFireball;
import org.bukkit.entity.Steerable;
import org.bukkit.entity.Strider;
import org.bukkit.entity.Wolf;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.EntityTargetEvent.TargetReason;
import org.bukkit.event.entity.EntityToggleGlideEvent;
import org.bukkit.event.entity.EntityToggleSwimEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerToggleSprintEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import me.LoneSurvivor.Supernatural.Files.DataManager;
import net.minecraft.server.v1_16_R1.ItemArmor;
import net.minecraft.server.v1_16_R1.PacketPlayOutEntityDestroy;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.minecraft.server.v1_16_R1.EntityInsentient;

import org.bukkit.craftbukkit.v1_16_R1.entity.CraftArrow;
import org.bukkit.craftbukkit.v1_16_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_16_R1.entity.CraftPlayer;

public class Supernatural extends JavaPlugin implements Listener {
    private Economy eco;
    private Chat chat;
    Boolean VaultChat = true;
    Map < UUID, String[] > PlayerData = new HashMap < UUID, String[] > ();
    Map < UUID, Location > TeleportLocation = new HashMap < UUID, Location> ();
    Map < UUID, Location > RessurectLocation = new HashMap < UUID, Location> ();
    Map < Material, Integer > MeatNutrition = new HashMap < Material, Integer> ();
    Map < Material, Integer > HolyDonationValue = new HashMap < Material, Integer> ();
    Map < Material, Integer > UnholyDonationValue = new HashMap < Material, Integer> ();
    Map < Player, Map < LivingEntity, Integer > > UnholyBond = new HashMap < Player, Map < LivingEntity, Integer > > ();
    public DataManager data;
    public Supernatural main;
	ArrayList<EntityType> monsters = new ArrayList<EntityType>();
	ArrayList<EntityType> undead = new ArrayList<EntityType>();
    List <ItemStack> CustomItems = new ArrayList<ItemStack>();
    List <ItemStack> SpellIcons = new ArrayList<ItemStack>();
    Map < Player, Boolean > SpellbarOpen = new HashMap < Player, Boolean > ();
    Map < Player, Inventory > Hotbars = new HashMap < Player, Inventory > ();
    Location banishlocation;
    List <UUID> GuardianAngel = new ArrayList<UUID>();
    Map < Player, BossBar > MagicBar = new HashMap < Player, BossBar> ();
    Map < Player, Boolean  > Regeneration = new HashMap < Player, Boolean> ();
    Map < Player, Boolean  > WaterBreathing = new HashMap < Player, Boolean> ();
    Map < Player, Integer  > ArrowType = new HashMap < Player, Integer> ();
    Map < Player, Arrow > GrappleArrows = new HashMap <Player, Arrow> ();
    Map < Player, Location > GrappleLocation = new HashMap <Player, Location> ();
    Map < Player, Arrow > GrappleArrows2 = new HashMap <Player, Arrow> ();
    List < UUID > FlameArrows = new ArrayList < UUID > ();
    List < UUID > PowerArrows = new ArrayList < UUID > ();
    Map < UUID, Double > PlayerBounties = new HashMap < UUID, Double> ();
    Map < Player, List<Inventory> > BountyPages = new HashMap < Player, List<Inventory> >();
    Map < UUID, Map < String, Integer > > RecruitingItems = new HashMap < UUID, Map < String, Integer > >();
    Map < UUID, Map < String, Integer > > Cooldowns = new HashMap < UUID, Map < String, Integer > >();
    List<UUID> PlayerDropItemOnTick = new ArrayList<UUID>();
    HashMap<Player, ItemStack> arrowReplacedItem = new HashMap<Player, ItemStack>();
    
    /**
     * Setup
     */
    
	public void onEnable() {
        this.data = new DataManager(this);
        data.saveDefaultConfig();
        this.saveDefaultConfig();
        Bukkit.getPluginManager().registerEvents(this, this);
        //Setup Vault
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
        	this.getLogger().log(Level.SEVERE, "Vault Dependancy Missing!");
        	getServer().getPluginManager().disablePlugin(this);
        }
        
        if(!setupEconomy()) {
        	this.getLogger().log(Level.SEVERE, "Vault Economy Integration Failed");
        	getServer().getPluginManager().disablePlugin(this);
        }
        
        if(!setupChat()) {
        	this.getLogger().log(Level.SEVERE, "Vault Chat Integration Failed");
        	getServer().getPluginManager().disablePlugin(this);
        }
        
        //Setup ArmorEquipEvent
		getServer().getPluginManager().registerEvents(new ArmorListener(getConfig().getStringList("blocked")), this);
		try {
			Class.forName("org.bukkit.event.block.BlockDispenseArmorEvent");
			getServer().getPluginManager().registerEvents(new DispenserArmorListener(), this);
		} catch(Exception ignored) {}
		
		//Load Data from Data File
    	if (data.getConfig().getConfigurationSection("PlayerData") != null) {
    	    data.getConfig().getConfigurationSection("PlayerData").getKeys(false).forEach(key -> {
    	        PlayerData.put(UUID.fromString(key), data.getConfig().getString("PlayerData." + key).split(":"));
    	    });
    	}
    	if (data.getConfig().getConfigurationSection("RecruitingItems") != null) {
    	    data.getConfig().getConfigurationSection("RecruitingItems").getKeys(false).forEach(key -> {
    	    	Map<String, Integer> temp = new HashMap<String, Integer>();
        	    data.getConfig().getConfigurationSection("RecruitingItems." + key).getKeys(false).forEach(key2 -> {
        	    	temp.put(key2, data.getConfig().getInt("RecruitingItems." + key + "." + key2));
        	    });
        	    RecruitingItems.put(UUID.fromString(key), temp);
    	    });
    	}
    	if (data.getConfig().getConfigurationSection("PlayerBounties") != null) {
    	    data.getConfig().getConfigurationSection("PlayerBounties").getKeys(false).forEach(key -> {
    	    	PlayerBounties.put(UUID.fromString(key), data.getConfig().getDouble("PlayerBounties." + key));
    	    });
    	}
    	if(data.getConfig().getString("BanishLocation") != null) {
        	String[] LPs = data.getConfig().getString("BanishLocation").split(":");
        	banishlocation = new Location(Bukkit.getServer().getWorld(LPs[0]), Double.parseDouble(LPs[1]), Double.parseDouble(LPs[2]), Double.parseDouble(LPs[3]), Float.parseFloat(LPs[4]), Float.parseFloat(LPs[5]));
    	}
    	if (data.getConfig().getConfigurationSection("TeleportLocation") != null) {
    	    data.getConfig().getConfigurationSection("TeleportLocation").getKeys(false).forEach(key -> {
    	    	UUID uuid = UUID.fromString(key);
    	    	String[] LPs = data.getConfig().getString("TeleportLocation." + key).split(":");
    	    	Location location = new Location(Bukkit.getServer().getWorld(LPs[0]), Double.parseDouble(LPs[1]), Double.parseDouble(LPs[2]), Double.parseDouble(LPs[3]), Float.parseFloat(LPs[4]), Float.parseFloat(LPs[5]));
    	    	TeleportLocation.put(uuid, location);
    	    });
    	}
    	if (data.getConfig().getConfigurationSection("RessurectLocation") != null) {
    	    data.getConfig().getConfigurationSection("RessurectLocation").getKeys(false).forEach(key -> {
    	    	UUID uuid = UUID.fromString(key);
    	    	String[] LPs = data.getConfig().getString("RessurectLocation." + key).split(":");
    	    	Location location = new Location(Bukkit.getServer().getWorld(LPs[0]), Double.parseDouble(LPs[1]), Double.parseDouble(LPs[2]), Double.parseDouble(LPs[3]), Float.parseFloat(LPs[4]), Float.parseFloat(LPs[5]));
    	    	RessurectLocation.put(uuid, location);
    	    });
    	}
    	List<String> uuids = new ArrayList<String>();
    	uuids = data.getConfig().getStringList("GuardianAngel");
    	for(String uuid : uuids) {
    		GuardianAngel.add(UUID.fromString(uuid));
    	}
	    data.saveConfig();
	    
	    //Setup Custom Lists
	    SpellIcons();
	    CustomItems();
	    monsterList();
	    MeatNutritionSetup();
	    DonationValueSetup();
	    
	    //Setup Repeating Checks
    	this.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
    		int tick=0;
            public void run() {
            	tick+=1;
            	Followers(tick);
            	for(World world : Bukkit.getWorlds()) {
            		for(Player player : world.getPlayers()) {
            			if(tick % 5 == 0) updateMagicDisplay(player); //Lag Reduced
            			if(tick % 5 == 0) NightVision(player); //Lag Reduced
            			if(tick % 20 == 0) Regeneration(player); //Fully Optimised
            			if(tick % 5 == 0) Speed(player); //Lag Reduced
        				if(tick % 20 == 0) MagicFromHeat(player); //Fully Optimised
        				if(tick % 20 == 0) WaterBreathing(player); //Lag Reduced   
            			if(tick % 20 == 0) ejectArmour(player); //Lag Reduced
            			inSunlight(player, tick);       				
            			if(tick % 20 == 0) waterContact(player); //Fully Optimised
            			if(tick % 20 == 0) HostileAnimals(); //Lag Reduced
            			if(tick % 5 == 0) immovableSpells(player);
        				UnholyBondTimer();
        				GrappleArrowStage2(player);
        				tickCooldowns();
        				if(tick % 5 == 0) despawnFarSpells();
        				//lavaswimstartstop(player);
            		}
            	}
            }
        }, 0L, 1L);
	}
	
	/*List<UUID> swimming = new ArrayList<UUID>();
	
	@EventHandler
	public void lavaswim(EntityToggleGlideEvent event) {
		if(event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();
			if(event.isGliding() == false) {
				if(swimming.contains(player.getUniqueId())) {
					event.setCancelled(true);
				}
			}
		}
	}
	
	public void lavaswimstartstop(Player player) {
		if(swimming.contains(player.getUniqueId())) {
			player.setVelocity(player.getVelocity().multiply(1.05));
		}
		if(player.getEyeLocation().getBlock().getType().equals(Material.LAVA) && player.getLocation().getBlock().getType().equals(Material.LAVA) && player.isSprinting()) {
			if(!swimming.contains(player.getUniqueId())) {
				swimming.add(player.getUniqueId());
				player.setGliding(true);
			}
		}
		if((!player.getEyeLocation().getBlock().getType().equals(Material.LAVA) && !player.getLocation().getBlock().getType().equals(Material.LAVA)) || !player.isSprinting()) {
			if(swimming.contains(player.getUniqueId())) {
				swimming.remove(player.getUniqueId());
				player.setGliding(false);
			}
		}
	}*/
	
	public void onDisable() {
        data.getConfig().set("PlayerData", null);
		if (!PlayerData.isEmpty()) {
            for (Entry<UUID, String[]> entry: PlayerData.entrySet()) {
                data.getConfig().set("PlayerData." + entry.getKey(), entry.getValue()[0] + ":" + entry.getValue()[1]);
            }
        }
        data.getConfig().set("RecruitingItems", null);
		if (!RecruitingItems.isEmpty()) {
            for (Entry<UUID, Map <String, Integer> > entry: RecruitingItems.entrySet()) {
                for (Entry <String, Integer> entry2: entry.getValue().entrySet()) {
                    data.getConfig().set("RecruitingItems." + entry.getKey() + "." + entry2.getKey(), entry2.getValue());
                }
            }
        }
        data.getConfig().set("PlayerBounties", null);
		if (!PlayerBounties.isEmpty()) {
            for (Entry<UUID, Double> entry: PlayerBounties.entrySet()) {
                data.getConfig().set(("PlayerBounties." + entry.getKey()), entry.getValue());
            }
        }
        data.getConfig().set("TeleportLocation", null);
		if (!TeleportLocation.isEmpty()) {
	        for (Entry<UUID, Location> entry: TeleportLocation.entrySet()) {
	        	Location l = entry.getValue();
	            data.getConfig().set("TeleportLocation." + entry.getKey().toString(), (l.getWorld().getName() + ":" + l.getBlockX() + ":" + l.getBlockY() + ":" + l.getBlockZ() + ":" + l.getYaw() + ":" + l.getPitch()));
	        }
        }
        data.getConfig().set("RessurectLocation", null);
		if (!RessurectLocation.isEmpty()) {
	        for (Entry<UUID, Location> entry: RessurectLocation.entrySet()) {
	        	Location l = entry.getValue();
	            data.getConfig().set("RessurectLocation." + entry.getKey().toString(), (l.getWorld().getName() + ":" + l.getBlockX() + ":" + l.getBlockY() + ":" + l.getBlockZ() + ":" + l.getYaw() + ":" + l.getPitch()));
	        }
        }
        data.getConfig().set("BanishLocation", null);
		if(banishlocation != null) {
	        data.getConfig().set("BanishLocation", (banishlocation.getWorld().getName() + ":" + banishlocation.getBlockX() + ":" + banishlocation.getBlockY() + ":" + banishlocation.getBlockZ() + ":" + banishlocation.getYaw() + ":" + banishlocation.getPitch()));
		}
        data.getConfig().set("GuardianAngel", null);
		if(!GuardianAngel.isEmpty()) {
			ArrayList<String> uuids = new ArrayList<String>();
			for(UUID uuid : GuardianAngel) {
				uuids.add(uuid.toString());
			}
			data.getConfig().set("GuardianAngel", uuids);
		}
        data.saveConfig();
		for(World world : Bukkit.getServer().getWorlds()) {
			for(Player player : world.getPlayers()) {
				ItemStack item = player.getInventory().getItem(8);
				if(item.equals(SpellIcons.get(1))) {
					CloseSpellbar(player);
				}
				if(MagicBar.containsKey(player)) {
					BossBar bar = MagicBar.get(player);
					bar.removePlayer(player);
				}
			}
		}
	}

	private boolean setupChat() {
        RegisteredServiceProvider<Chat> chatProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.chat.Chat.class);
        if (chatProvider != null) {
            chat = chatProvider.getProvider();
        }
        return (chat != null);
    }
	
	private boolean setupEconomy() {
        RegisteredServiceProvider<Economy> rsp = Bukkit.getServer().getServicesManager().getRegistration(Economy.class);
        eco = rsp.getProvider();
        return eco != null;
    }
	
	public void despawnFarSpells() {
		for(World w : Bukkit.getWorlds()) {
			for(Entity e : w.getEntities()) {
				if(e.getType().equals(EntityType.SMALL_FIREBALL)) {
					SmallFireball fireball = (SmallFireball) e;
					String name = fireball.getCustomName();
					if(name != null && name.contains(":")) {
						String[] nameparts = name.split(":");
						UUID uuid = UUID.fromString(nameparts[0]);
						if(uuid!=null) {
							Player p = Bukkit.getPlayer(uuid);
							if(p!=null) {
								Location loc = p.getLocation();
								if(loc.distance(fireball.getLocation()) > 20) {
									fireball.remove();
								}
							}
						}
					}
				}
			}
		}
	}
 	
	@EventHandler
    public void PlayerChat(AsyncPlayerChatEvent event) {
		Player player = event.getPlayer();
		World world = player.getWorld();
		String group = chat.getPrimaryGroup(player);
		String prefix = chat.getGroupPrefix(world, group);
		String race = getRace(player);
		event.setFormat(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Prefixes." + race) + " " + prefix + player.getName() + "&8: &7" + event.getMessage()));
    }
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		for(UUID player : PlayerData.keySet()) {
			if(player.equals(event.getPlayer().getUniqueId())) {
				return;
			}
		}
        PlayerData.put(event.getPlayer().getUniqueId(), new String[] {this.getConfig().getString("StartingClass"), this.getConfig().getString("StartingMagic")});
	}
	
	public void updateMagicDisplay(Player player) {
		String[] playerdata = PlayerData.get(player.getUniqueId());
		double magic = Double.parseDouble(playerdata[1]);
		if(!MagicBar.containsKey(player)) {
			BossBar bar = Bukkit.createBossBar("Magika: " + (int) magic, BarColor.BLUE, BarStyle.SOLID);
			MagicBar.put(player, bar);
			bar.addPlayer(player);
		}
		MagicBar.get(player).setTitle("Magika: " + (int) magic);
	}

	public void MeatNutritionSetup() {
		MeatNutrition.put(Material.COOKED_CHICKEN, 6);
		MeatNutrition.put(Material.COOKED_COD, 5);
		MeatNutrition.put(Material.COOKED_MUTTON, 6);
		MeatNutrition.put(Material.COOKED_PORKCHOP, 8);
		MeatNutrition.put(Material.COOKED_RABBIT, 5);
		MeatNutrition.put(Material.COOKED_SALMON, 6);
		MeatNutrition.put(Material.BEEF, 3);
		MeatNutrition.put(Material.CHICKEN, 2);
		MeatNutrition.put(Material.COD, 2);
		MeatNutrition.put(Material.MUTTON, 2);
		MeatNutrition.put(Material.PORKCHOP, 3);
		MeatNutrition.put(Material.RABBIT, 3);
		MeatNutrition.put(Material.SALMON, 2);
		MeatNutrition.put(Material.ROTTEN_FLESH, 4);
		MeatNutrition.put(Material.COOKED_BEEF, 8);
		MeatNutrition.put(Material.TROPICAL_FISH, 1);
	}
	
	public void DonationValueSetup() {
		HolyDonationValue.put(Material.COOKED_CHICKEN, 6);
		HolyDonationValue.put(Material.COOKED_COD, 5);
		HolyDonationValue.put(Material.COOKED_MUTTON, 6);
		HolyDonationValue.put(Material.COOKED_PORKCHOP, 8);
		HolyDonationValue.put(Material.COOKED_RABBIT, 5);
		HolyDonationValue.put(Material.COOKED_SALMON, 6);
		HolyDonationValue.put(Material.BEEF, 3);
		HolyDonationValue.put(Material.CHICKEN, 2);
		HolyDonationValue.put(Material.COD, 2);
		HolyDonationValue.put(Material.MUTTON, 2);
		HolyDonationValue.put(Material.PORKCHOP, 3);
		HolyDonationValue.put(Material.RABBIT, 3);
		HolyDonationValue.put(Material.SALMON, 2);
		HolyDonationValue.put(Material.ROTTEN_FLESH, 4);
		HolyDonationValue.put(Material.COOKED_BEEF, 8);
		HolyDonationValue.put(Material.TROPICAL_FISH, 1);
		HolyDonationValue.put(Material.APPLE, 4);
		HolyDonationValue.put(Material.BAKED_POTATO, 5);
		HolyDonationValue.put(Material.BEETROOT, 1);
		HolyDonationValue.put(Material.BEETROOT_SOUP, 6);
		HolyDonationValue.put(Material.BREAD, 5);
		HolyDonationValue.put(Material.CAKE, 14);
		HolyDonationValue.put(Material.CARROT, 3);
		HolyDonationValue.put(Material.CHORUS_FRUIT, 4);
		HolyDonationValue.put(Material.COOKIE, 2);
		HolyDonationValue.put(Material.DRIED_KELP, 1);
		HolyDonationValue.put(Material.ENCHANTED_GOLDEN_APPLE, 4 + 90*8);
		HolyDonationValue.put(Material.GOLDEN_APPLE, 4 + 10*8);
		HolyDonationValue.put(Material.GOLDEN_CARROT, 6 + 1*8);
		HolyDonationValue.put(Material.HONEY_BOTTLE, 6);
		HolyDonationValue.put(Material.MELON_SLICE, 2);
		HolyDonationValue.put(Material.MUSHROOM_STEW, 6);
		HolyDonationValue.put(Material.POTATO, 1);
		HolyDonationValue.put(Material.PUMPKIN_PIE, 8);
		HolyDonationValue.put(Material.RABBIT_STEW, 10);
		HolyDonationValue.put(Material.SWEET_BERRIES, 2);
		HolyDonationValue.put(Material.GOLD_NUGGET, 1);
		HolyDonationValue.put(Material.GOLD_INGOT, 10);
		HolyDonationValue.put(Material.GOLD_BLOCK, 90);
		UnholyDonationValue.put(Material.BONE, 3);
		UnholyDonationValue.put(Material.ROTTEN_FLESH, 2);
		UnholyDonationValue.put(Material.MAGMA_CREAM, 5);
		UnholyDonationValue.put(Material.PHANTOM_MEMBRANE, 10);
		UnholyDonationValue.put(Material.ARROW, 2);
		UnholyDonationValue.put(Material.ENDER_PEARL, 8);
		UnholyDonationValue.put(Material.BLAZE_ROD, 5);
		UnholyDonationValue.put(Material.STRING, 1);
		UnholyDonationValue.put(Material.SPIDER_EYE, 3);
		UnholyDonationValue.put(Material.GUNPOWDER, 3);
		UnholyDonationValue.put(Material.GHAST_TEAR, 6);
		UnholyDonationValue.put(Material.SLIME_BALL, 4);
		UnholyDonationValue.put(Material.GOLD_NUGGET, 1);
		UnholyDonationValue.put(Material.GOLD_INGOT, 10);
		UnholyDonationValue.put(Material.GOLD_BLOCK, 90);
	}
	
	/**
	 * Hotbar Switcher Logic    
	 */
	
    public void SpellIcons() {
		//0
		ItemStack item = new ItemStack(Material.BOOK);
    	ItemMeta meta = item.getItemMeta();
    	List<String> lore = new ArrayList<String>();
    	meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
    	meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
    	meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&l&6Hotbar"));
    	lore.add(ChatColor.translateAlternateColorCodes('&', "&eSwitches between the abilities"));
    	lore.add(ChatColor.translateAlternateColorCodes('&', "&ebar and the regular hotbar"));
    	meta.setLore(lore);
    	item.setItemMeta(meta);
    	SpellIcons.add(item);
    	//1
		item = new ItemStack(Material.BOOK);
    	meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&l&6Abilities"));
    	item.setItemMeta(meta);
    	SpellIcons.add(item);
    	//2
		item = new ItemStack(Material.BARRIER);
		meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&cEmpty Slot"));
		item.setItemMeta(meta);
		SpellIcons.add(item);
		//3
		item = new ItemStack(Material.PAPER);
		meta = item.getItemMeta();
    	meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
    	meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		meta.setUnbreakable(true);
		meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6Teleport"));
		lore = new ArrayList<String>();
		lore.add(ChatColor.translateAlternateColorCodes('&', "&eLeft Click to Set Teleport Location"));
		lore.add(ChatColor.translateAlternateColorCodes('&', "&eRight Click to Teleport - " + this.getConfig().getInt("SpellCosts.Teleport") + " Magika"));
		meta.setLore(lore);
		item.setItemMeta(meta);
		SpellIcons.add(item);
		//4
		item = new ItemStack(Material.FEATHER);
		meta = item.getItemMeta();
    	meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
    	meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		meta.setUnbreakable(true);
		meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6High Jump"));
		lore = new ArrayList<String>();
		lore.add(ChatColor.translateAlternateColorCodes('&', "&eRight Click to High Jump - " + this.getConfig().getInt("SpellCosts.High-Jump") + " Magika"));
		meta.setLore(lore);
		item.setItemMeta(meta);
		SpellIcons.add(item);
		//5
		item = new ItemStack(Material.POPPY);
		meta = item.getItemMeta();
    	meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
    	meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		meta.setUnbreakable(true);
		meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6Create Bloodrose Potion"));
		lore = new ArrayList<String>();
		lore.add(ChatColor.translateAlternateColorCodes('&', "&eRight Click to Create Bloodrose Potion - " + this.getConfig().getInt("SpellCosts.Bloodrose") + " Magika and 10 Health"));
		meta.setLore(lore);
		item.setItemMeta(meta);
		SpellIcons.add(item);
		//6
		item = new ItemStack(Material.BONE);
		meta = item.getItemMeta();
    	meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
    	meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		meta.setUnbreakable(true);
		meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6Summon Wolf"));
		lore = new ArrayList<String>();
		lore.add(ChatColor.translateAlternateColorCodes('&', "&eRight Click to Summon Wolf - " + this.getConfig().getInt("SpellCosts.Summon-Wolf") + " Magika"));
		meta.setLore(lore);
		item.setItemMeta(meta);
		SpellIcons.add(item);
		//7
		item = new ItemStack(Material.FEATHER);
		meta = item.getItemMeta();
    	meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
    	meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		meta.setUnbreakable(true);
		meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6Dash"));
		lore = new ArrayList<String>();
		lore.add(ChatColor.translateAlternateColorCodes('&', "&eRight Click to Dash - " + this.getConfig().getInt("SpellCosts.Dash") + " Magika"));
		meta.setLore(lore);
		item.setItemMeta(meta);
		SpellIcons.add(item);
		//8
		item = new ItemStack(Material.BLUE_ORCHID);
		meta = item.getItemMeta();
    	meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
    	meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		meta.setUnbreakable(true);
		meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6Create Moonflower Potion"));
		lore = new ArrayList<String>();
		lore.add(ChatColor.translateAlternateColorCodes('&', "&eRight Click to Create Moonflower Potion - " + this.getConfig().getInt("SpellCosts.Moonflower") + " Magika and 10 Health"));
		meta.setLore(lore);
		item.setItemMeta(meta);
		SpellIcons.add(item);
		//9
		item = new ItemStack(Material.BONE_MEAL);
		meta = item.getItemMeta();
    	meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
    	meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		meta.setUnbreakable(true);
		meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6Summon Monster"));
		lore = new ArrayList<String>();
		lore.add(ChatColor.translateAlternateColorCodes('&', "&eRight Click to Summon a Monster - " + this.getConfig().getInt("SpellCosts.Summon-Monster") + " Magika"));
		meta.setLore(lore);
		item.setItemMeta(meta);
		SpellIcons.add(item);
		//10
		item = new ItemStack(Material.BONE);
		meta = item.getItemMeta();
    	meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
    	meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		meta.setUnbreakable(true);
		meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6Unholy Bond"));
		lore = new ArrayList<String>();
		lore.add(ChatColor.translateAlternateColorCodes('&', "&eHit target to use Unhold Bond - " + this.getConfig().getInt("SpellCosts.Unholy-Bond") + " Magika"));
		meta.setLore(lore);
		item.setItemMeta(meta);
		SpellIcons.add(item);
		//11
		item = new ItemStack(Material.WITHER_ROSE);
		meta = item.getItemMeta();
    	meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
    	meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		meta.setUnbreakable(true);
		meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6Create Goulish Potion"));
		lore = new ArrayList<String>();
		lore.add(ChatColor.translateAlternateColorCodes('&', "&eRight Click to Create Goulish Potion - " + this.getConfig().getInt("SpellCosts.Ghoulish") + " Magika and 10 Health"));
		meta.setLore(lore);
		item.setItemMeta(meta);
		SpellIcons.add(item);
		//12
		item = new ItemStack(Material.REDSTONE);
		meta = item.getItemMeta();
    	meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
    	meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		meta.setUnbreakable(true);
		meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6Fireball"));
		lore = new ArrayList<String>();
		lore.add(ChatColor.translateAlternateColorCodes('&', "&eLaunches a fireball in the direction your looking - " + this.getConfig().getInt("SpellCosts.Fireball") + " Magika"));
		meta.setLore(lore);
		item.setItemMeta(meta);
		SpellIcons.add(item);
		//13
		item = new ItemStack(Material.TNT);
		meta = item.getItemMeta();
    	meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
    	meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		meta.setUnbreakable(true);
		meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6Explosion"));
		lore = new ArrayList<String>();
		lore.add(ChatColor.translateAlternateColorCodes('&', "&eCauses an explosion where your standing - " + this.getConfig().getInt("SpellCosts.Explosion") + " Magika"));
		meta.setLore(lore);
		item.setItemMeta(meta);
		SpellIcons.add(item);
		//14
		item = new ItemStack(Material.INK_SAC);
		meta = item.getItemMeta();
    	meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
    	meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		meta.setUnbreakable(true);
		meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6Snare"));
		lore = new ArrayList<String>();
		lore.add(ChatColor.translateAlternateColorCodes('&', "&eFreezes players you hit for 10s - " + this.getConfig().getInt("SpellCosts.Snare") + " Magika"));
		meta.setLore(lore);
		item.setItemMeta(meta);
		SpellIcons.add(item);
		//15
		item = new ItemStack(Material.NETHER_WART);
		meta = item.getItemMeta();
    	meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
    	meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		meta.setUnbreakable(true);
		meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6Create Hellish Potion"));
		lore = new ArrayList<String>();
		lore.add(ChatColor.translateAlternateColorCodes('&', "&eRight Click to Create Hellish Potion - " + this.getConfig().getInt("SpellCosts.Hellish") + " Magika and 10 Health"));
		meta.setLore(lore);
		item.setItemMeta(meta);
		SpellIcons.add(item);
		//16
		item = new ItemStack(Material.IRON_BARS);
		meta = item.getItemMeta();
    	meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
    	meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		meta.setUnbreakable(true);
		meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6Banish Supernatural"));
		lore = new ArrayList<String>();
		lore.add(ChatColor.translateAlternateColorCodes('&', "&eLeft Click to Banish Supernatural - " + this.getConfig().getInt("SpellCosts.Banish") + " Magika"));
		meta.setLore(lore);
		item.setItemMeta(meta);
		SpellIcons.add(item);
		//17
		item = new ItemStack(Material.FLINT_AND_STEEL);
		meta = item.getItemMeta();
    	meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
    	meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		meta.setUnbreakable(true);
		meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6Exorcise Supernatural"));
		lore = new ArrayList<String>();
		lore.add(ChatColor.translateAlternateColorCodes('&', "&eLeft Click to Exorcise Supernatural - " + this.getConfig().getInt("SpellCosts.Exorcise") + " Magika"));
		meta.setLore(lore);
		item.setItemMeta(meta);
		SpellIcons.add(item);
		//18
		item = new ItemStack(Material.STICK);
		meta = item.getItemMeta();
    	meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
    	meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		meta.setUnbreakable(true);
		meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6Drain Supernatural"));
		lore = new ArrayList<String>();
		lore.add(ChatColor.translateAlternateColorCodes('&', "&eLeft Click to Drain Supernatural - " + this.getConfig().getInt("SpellCosts.Drain") + " Magika"));
		meta.setLore(lore);
		item.setItemMeta(meta);
		SpellIcons.add(item);
		//19
		item = new ItemStack(Material.PAPER);
		meta = item.getItemMeta();
    	meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
    	meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		meta.setUnbreakable(true);
		meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6Heal Human"));
		lore = new ArrayList<String>();
		lore.add(ChatColor.translateAlternateColorCodes('&', "&eLeft Click to Heal Human - " + this.getConfig().getInt("SpellCosts.Heal-Human") + " Magika"));
		meta.setLore(lore);
		item.setItemMeta(meta);
		SpellIcons.add(item);
		//20
		item = new ItemStack(Material.WHITE_WOOL);
		meta = item.getItemMeta();
    	meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
    	meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		meta.setUnbreakable(true);
		meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6Guardian Angel"));
		lore = new ArrayList<String>();
		lore.add(ChatColor.translateAlternateColorCodes('&', "&eLeft Click to Guardian Angel - " + this.getConfig().getInt("SpellCosts.Guardian-Angel") + " Magika"));
		meta.setLore(lore);
		item.setItemMeta(meta);
		SpellIcons.add(item);
		//21
		item = new ItemStack(Material.BAMBOO);
		meta = item.getItemMeta();
    	meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
    	meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		meta.setUnbreakable(true);
		meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6Create Supernatural Cure"));
		lore = new ArrayList<String>();
		lore.add(ChatColor.translateAlternateColorCodes('&', "&eRight Click to Create Supernatural Cure - " + this.getConfig().getInt("SpellCosts.Cure") + " Magika"));
		meta.setLore(lore);
		item.setItemMeta(meta);
		SpellIcons.add(item);
		//22
		item = new ItemStack(Material.BOOK);
		meta = item.getItemMeta();
    	meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
    	meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		meta.setUnbreakable(true);
		meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6Create Holy Book"));
		lore = new ArrayList<String>();
		lore.add(ChatColor.translateAlternateColorCodes('&', "&eRight Click to Create Holy Book - " + this.getConfig().getInt("SpellCosts.Holy-Book") + " Magika and 10 Health"));
		meta.setLore(lore);
		item.setItemMeta(meta);
		SpellIcons.add(item);
		//23
		item = new ItemStack(Material.WARPED_FUNGUS_ON_A_STICK);
		meta = item.getItemMeta();
    	meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
    	meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		meta.setUnbreakable(true);
		meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6Summon Strider"));
		lore = new ArrayList<String>();
		lore.add(ChatColor.translateAlternateColorCodes('&', "&eLeft Click to Summon a Strider - " + this.getConfig().getInt("SpellCosts.Summon-Strider") + " Magika"));
		meta.setLore(lore);
		item.setItemMeta(meta);
		SpellIcons.add(item);
		//24
		item = new ItemStack(Material.BOOK);
		meta = item.getItemMeta();
    	meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
    	meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		meta.setUnbreakable(true);
		meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6Create Book Of Death"));
		lore = new ArrayList<String>();
		lore.add(ChatColor.translateAlternateColorCodes('&', "&eRight Click to Create Book Of Death - " + this.getConfig().getInt("SpellCosts.Book-Of-Death") + " Magika and 10 Health"));
		meta.setLore(lore);
		item.setItemMeta(meta);
		SpellIcons.add(item);
		//25
		item = new ItemStack(Material.BONE);
		meta = item.getItemMeta();
    	meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
    	meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		meta.setUnbreakable(true);
		meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6Summon Skeleton"));
		lore = new ArrayList<String>();
		lore.add(ChatColor.translateAlternateColorCodes('&', "&eRight Click to Summon a Skeleton - " + this.getConfig().getInt("SpellCosts.Summon-Skeleton") + " Magika"));
		meta.setLore(lore);
		item.setItemMeta(meta);
		SpellIcons.add(item);
		//26
		item = new ItemStack(Material.ROTTEN_FLESH);
		meta = item.getItemMeta();
    	meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
    	meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		meta.setUnbreakable(true);
		meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6Summon Undead"));
		lore = new ArrayList<String>();
		lore.add(ChatColor.translateAlternateColorCodes('&', "&eRight Click to Summon an Undead Monster - " + this.getConfig().getInt("SpellCosts.Summon-Undead") + " Magika"));
		meta.setLore(lore);
		item.setItemMeta(meta);
		SpellIcons.add(item);
		//27
		item = new ItemStack(Material.RED_DYE);
		meta = item.getItemMeta();
    	meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
    	meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		meta.setUnbreakable(true);
		meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6Heal Undead"));
		lore = new ArrayList<String>();
		lore.add(ChatColor.translateAlternateColorCodes('&', "&eRight Click to Heal Nearby Undead - " + this.getConfig().getInt("SpellCosts.Heal-Undead") + " Magika"));
		meta.setLore(lore);
		item.setItemMeta(meta);
		SpellIcons.add(item);
		//28
		item = new ItemStack(Material.MAP);
		meta = item.getItemMeta();
    	meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
    	meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		meta.setUnbreakable(true);
		meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6Ressurection Spawn"));
		lore = new ArrayList<String>();
		lore.add(ChatColor.translateAlternateColorCodes('&', "&eRight Click to set next spawn point - " + this.getConfig().getInt("SpellCosts.Ressurection-Spawn") + " Magika"));
		meta.setLore(lore);
		item.setItemMeta(meta);
		SpellIcons.add(item);
		//29
		item = new ItemStack(Material.APPLE);
		meta = item.getItemMeta();
    	meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
    	meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		meta.setUnbreakable(true);
		meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6Regeneration - Enabled"));
		lore = new ArrayList<String>();
		lore.add(ChatColor.translateAlternateColorCodes('&', "&eRight Click to Toggle Regeneration - " + this.getConfig().getInt("SpellCosts.Regeneration") + " Magika/Heart"));
		meta.setLore(lore);
		item.setItemMeta(meta);
		SpellIcons.add(item);
		//30
		item = new ItemStack(Material.LILY_PAD);
		meta = item.getItemMeta();
    	meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
    	meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		meta.setUnbreakable(true);
		meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6Water Breathing - Enabled"));
		lore = new ArrayList<String>();
		lore.add(ChatColor.translateAlternateColorCodes('&', "&eRight Click to Toggle Water Breathing - " + this.getConfig().getInt("SpellCosts.Water-Breathing")*20 + " Magika/Sec"));
		meta.setLore(lore);
		item.setItemMeta(meta);
		SpellIcons.add(item);
		//31
		item = new ItemStack(Material.APPLE);
		meta = item.getItemMeta();
		meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		meta.setUnbreakable(true);
		meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6Regeneration - Disabled"));
		lore = new ArrayList<String>();
		lore.add(ChatColor.translateAlternateColorCodes('&', "&eRight Click to Toggle Regeneration - " + this.getConfig().getInt("SpellCosts.Regeneration") + " Magika/Heart"));
		meta.setLore(lore);
		item.setItemMeta(meta);
		SpellIcons.add(item);
		//32
		item = new ItemStack(Material.LILY_PAD);
		meta = item.getItemMeta();
		meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		meta.setUnbreakable(true);
		meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6Water Breathing - Disabled"));
		lore = new ArrayList<String>();
		lore.add(ChatColor.translateAlternateColorCodes('&', "&eRight Click to Toggle Water Breathing - " + this.getConfig().getInt("SpellCosts.Water-Breathing")*20 + " Magika/Sec"));
		meta.setLore(lore);
		item.setItemMeta(meta);
		SpellIcons.add(item);
		//33
		item = new ItemStack(Material.BOW);
		meta = item.getItemMeta();
    	meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
    	meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		meta.setUnbreakable(true);
		meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6Bow - Triple Arrow"));
		lore = new ArrayList<String>();
		lore.add(ChatColor.translateAlternateColorCodes('&', "&eLeft Click to Cycle through arrow types"));
		lore.add(ChatColor.translateAlternateColorCodes('&', "&eRight Click to Shoot Triple Arrow - " + this.getConfig().getInt("SpellCosts.Triple-Arrow") + " Magika"));
		meta.setLore(lore);
		item.setItemMeta(meta);
		SpellIcons.add(item);
		//34
		item = new ItemStack(Material.BOW);
		meta = item.getItemMeta();
    	meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
    	meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		meta.setUnbreakable(true);
		meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6Bow - Grapple Arrow"));
		lore = new ArrayList<String>();
		lore.add(ChatColor.translateAlternateColorCodes('&', "&eLeft Click to Cycle through arrow types"));
		lore.add(ChatColor.translateAlternateColorCodes('&', "&eRight Click to Shoot Grapple Arrow - " + this.getConfig().getInt("SpellCosts.Grapple-Arrow") + " Magika"));
		lore.add(ChatColor.translateAlternateColorCodes('&', "&cJump to Cancel Grapple Arrow"));
		meta.setLore(lore);
		item.setItemMeta(meta);
		SpellIcons.add(item);
		//35
		item = new ItemStack(Material.BOW);
		meta = item.getItemMeta();
    	meta.addEnchant(Enchantment.ARROW_FIRE, 1, true);
    	meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		meta.setUnbreakable(true);
		meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6Bow - Fire Arrow"));
		lore = new ArrayList<String>();
		lore.add(ChatColor.translateAlternateColorCodes('&', "&eLeft Click to Cycle through arrow types"));
		lore.add(ChatColor.translateAlternateColorCodes('&', "&eRight Click to Shoot Fire Arrow - " + this.getConfig().getInt("SpellCosts.Fire-Arrow") + " Magika"));
		meta.setLore(lore);
		item.setItemMeta(meta);
		SpellIcons.add(item);
		//36
		item = new ItemStack(Material.BOW);
		meta = item.getItemMeta();
    	meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
    	meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		meta.setUnbreakable(true);
		meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6Bow - Power Arrow"));
		lore = new ArrayList<String>();
		lore.add(ChatColor.translateAlternateColorCodes('&', "&eLeft Click to Cycle through arrow types"));
		lore.add(ChatColor.translateAlternateColorCodes('&', "&eRight Click to Shoot Power Arrow - " + this.getConfig().getInt("SpellCosts.Power-Arrow") + " Magika"));
		meta.setLore(lore);
		item.setItemMeta(meta);
		SpellIcons.add(item);
		//37
		item = new ItemStack(Material.BOOK);
		meta = item.getItemMeta();
    	meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
    	meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		meta.setUnbreakable(true);
		meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6Create Book Of Witch Hunter"));
		lore = new ArrayList<String>();
		lore.add(ChatColor.translateAlternateColorCodes('&', "&eRight Click to Create Book Of Witch Hunter - " + this.getConfig().getInt("SpellCosts.Book-Of-WitchHunter") + " Magika and 10 Health"));
		meta.setLore(lore);
		item.setItemMeta(meta);
		SpellIcons.add(item);
		//38
		item = new ItemStack(Material.BOW);
		meta = item.getItemMeta();
    	meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
    	meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		meta.setUnbreakable(true);
		meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6Bow - Volley Arrow"));
		lore = new ArrayList<String>();
		lore.add(ChatColor.translateAlternateColorCodes('&', "&eLeft Click to Cycle through arrow types"));
		lore.add(ChatColor.translateAlternateColorCodes('&', "&eRight Click to Shoot Volley Arrow - " + this.getConfig().getInt("SpellCosts.Volley-Arrow") + " Magika"));
		meta.setLore(lore);
		item.setItemMeta(meta);
		SpellIcons.add(item);
		//39
		item = new ItemStack(Material.BONE_MEAL);
		meta = item.getItemMeta();
    	meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
    	meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		meta.setUnbreakable(true);
		meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6Holy Smite"));
		lore = new ArrayList<String>();
		lore.add(ChatColor.translateAlternateColorCodes('&', "&eLeft Click an enemy to use Holy Smite on them - " + this.getConfig().getInt("SpellCosts.Holy-Smite") + " Magika"));
		meta.setLore(lore);
		item.setItemMeta(meta);
		SpellIcons.add(item);
		//40
		item = new ItemStack(Material.LEAD);
		meta = item.getItemMeta();
    	meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
    	meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		meta.setUnbreakable(true);
		meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6Taunt"));
		lore = new ArrayList<String>();
		lore.add(ChatColor.translateAlternateColorCodes('&', "&eLeft Click someone to use Taunt on them - " + this.getConfig().getInt("SpellCosts.Taunt") + " Magika"));
		meta.setLore(lore);
		item.setItemMeta(meta);
		SpellIcons.add(item);
		//41
		item = new ItemStack(Material.DANDELION);
		meta = item.getItemMeta();
    	meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
    	meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		meta.setUnbreakable(true);
		meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6Holy Blessing"));
		lore = new ArrayList<String>();
		lore.add(ChatColor.translateAlternateColorCodes('&', "&eLeft Click someone to use Holy Blessing on them - " + this.getConfig().getInt("SpellCosts.Holy-Blessing") + " Magika"));
		meta.setLore(lore);
		item.setItemMeta(meta);
		SpellIcons.add(item);
		//42
		item = new ItemStack(Material.FEATHER);
		meta = item.getItemMeta();
    	meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
    	meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		meta.setUnbreakable(true);
		meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6Wings"));
		lore = new ArrayList<String>();
		lore.add(ChatColor.translateAlternateColorCodes('&', "&eLeft Click to glide for the next 20s - " + this.getConfig().getInt("SpellCosts.Wings") + " Magika"));
		meta.setLore(lore);
		item.setItemMeta(meta);
		SpellIcons.add(item);
		//43
		item = new ItemStack(Material.FERMENTED_SPIDER_EYE);
		meta = item.getItemMeta();
    	meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
    	meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		meta.setUnbreakable(true);
		meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6Create Blood Vial"));
		lore = new ArrayList<String>();
		lore.add(ChatColor.translateAlternateColorCodes('&', "&eRight Click to Create a Blood Vial - " + this.getConfig().getInt("SpellCosts.BloodVial") + " Magika and 3 Hunger"));
		meta.setLore(lore);
		item.setItemMeta(meta);
		SpellIcons.add(item);
    }
    
    @EventHandler
    public void undroppableSpells(PlayerDropItemEvent event) {
		PlayerDropItemOnTick.add(event.getPlayer().getUniqueId());
    	this.getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
            public void run() {
        		PlayerDropItemOnTick.remove(event.getPlayer().getUniqueId());
            }
        }, 2L);
		for(ItemStack item : SpellIcons) {
			if(event.getItemDrop().getItemStack().equals(item)) {
				event.setCancelled(true);
				return;
			}
		}
    }

    @EventHandler
    public void unconsumableSpells(PlayerItemConsumeEvent event) {
		for(ItemStack item : SpellIcons) {
			if(event.getItem().equals(item)) {
				event.setCancelled(true);
				return;
			}
		}
    }
    
    public void immovableSpells(Player p) {
    	Inventory TopInventory = p.getOpenInventory().getTopInventory();
    	Inventory BottomInventory = p.getOpenInventory().getBottomInventory();
		String race = PlayerData.get(p.getUniqueId())[0];
		Boolean updateInventory = false;
		
    	//Spellbar or Hotbar?
		Boolean SpellbarIsOpen = false;
		if(SpellbarOpen.containsKey(p)) {
			SpellbarIsOpen = SpellbarOpen.get(p);
		}

		//remove spells from cursor
		if(p.getItemOnCursor() != null) {
			ItemStack item = p.getItemOnCursor().clone();
			item.setAmount(1);
			if(SpellIcons.contains(item)) {
				p.setItemOnCursor(new ItemStack(Material.AIR));
				updateInventory = true;
			}
		}
		
		//remove all spells top inventory
		for(int i=0; i<TopInventory.getSize(); i++) {
			if(TopInventory.getItem(i) != null) {
				ItemStack item = TopInventory.getItem(i).clone();
				item.setAmount(1);
				if(SpellIcons.contains(item)) {
					TopInventory.setItem(i, new ItemStack(Material.AIR));
					updateInventory = true;
				}
			}
		}
		
		//remove all spells from bottom inventory
		for(int i=0; i<BottomInventory.getSize(); i++) {
			//Dont remove spells from correct slots
			if((SpellbarIsOpen && i>8) || (!SpellbarIsOpen && i!=8)) {
				if(BottomInventory.getItem(i) != null) {
					ItemStack item = BottomInventory.getItem(i).clone();
					item.setAmount(1);
					if(SpellIcons.contains(item)) {
						BottomInventory.setItem(i, new ItemStack(Material.AIR));
						updateInventory = true;
					}
				}
			}
		}
		
		//put spells back in correct locations
		if(SpellbarIsOpen) {
			Inventory spellbar = getSpellbar(race, p);
			for(int i=0; i<9; i++) {
				ItemStack temp = BottomInventory.getItem(i);
				if(temp == null || !temp.equals(spellbar.getItem(i))) {
					BottomInventory.setItem(i, spellbar.getItem(i));
					if(temp != null) {
						ItemStack temp1 = temp.clone();
						temp1.setAmount(1);
						if(!SpellIcons.contains(temp1)) {
							addItemSafely(p, temp);
						}
					}
					updateInventory = true;
				}
			}
		} else {
			ItemStack temp = BottomInventory.getItem(8);
			if(temp == null || !temp.equals(SpellIcons.get(0))) {
				BottomInventory.setItem(8, SpellIcons.get(0));
				if(temp != null) {
					ItemStack temp1 = temp.clone();
					temp1.setAmount(1);
					if(!temp1.equals(SpellIcons.get(0))) {
						addItemSafely(p, temp);
					}
				}
				updateInventory = true;
			}
		}
		if(updateInventory) {
			p.updateInventory();
		}
    }
    
    @EventHandler
    public void BlockPlaceEvent(BlockPlaceEvent event) {
		Player p = event.getPlayer();
		ItemStack EventItem = p.getItemInHand();
		if(SpellIcons.contains(EventItem)) {
			event.setCancelled(true);
		}
    }
    
	@SuppressWarnings("deprecation")
	@EventHandler
	public void ClickWithSpell(PlayerInteractEvent event) {
		Player p = event.getPlayer();
		ItemStack EventItem = p.getItemInHand();
    	this.getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
            public void run() {
            	//Prevents dropping items from inventory from triggering this event
	        	if(!PlayerDropItemOnTick.contains(p.getUniqueId())) {
	        		if(event.getHand() == EquipmentSlot.OFF_HAND) {
		        		return;
		        	}
	        		
	        		//allows interaction with pressureplates etc (non-clicking action)
	        		if(event.getAction().equals(Action.PHYSICAL)) {
	        			return;
	        		}
	        		
	        		//No Using Spells as Tools
	        		if(SpellIcons.contains(EventItem)) {
    					if(!EventItem.equals(SpellIcons.get(33)) && !EventItem.equals(SpellIcons.get(34))
        						&& !EventItem.equals(SpellIcons.get(35)) && !EventItem.equals(SpellIcons.get(36)) && !EventItem.equals(SpellIcons.get(38))) {
        					event.setCancelled(true);
        				}
	        		}
	        		
	        		if(event.getAction().equals(Action.PHYSICAL)) return;
	        		//Send to SpellManager
	        		if(event.getAction().equals(Action.LEFT_CLICK_AIR) || event.getAction().equals(Action.LEFT_CLICK_BLOCK)) SpellManager(p, null, "left");
	        		if(event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) SpellManager(p, null, "right");
	        	}
            }
        }, 1L);
	}
	
	@EventHandler
	public void HitWithSpell(EntityDamageByEntityEvent event) {
		if(!(event.getDamager() instanceof Player)) {
			return;
		}
		Player p = (Player) event.getDamager();
		if (!(event.getEntity() instanceof LivingEntity)) {
			return;
		}
		LivingEntity t = (LivingEntity) event.getEntity();
		SpellManager(p, t, "hit");
	}
	
	@EventHandler
	public void shootWithSpell(EntityShootBowEvent event) {
		Entity entity = event.getEntity();
		if(entity instanceof Player) {
			Player player = (Player) event.getEntity();
			SpellManager(player, null, "shoot");
			ItemStack EventItem = event.getBow();
			//No Using Spells as Tools
			for(ItemStack item : SpellIcons) {
				if(EventItem.equals(item)) {
					event.setCancelled(true);
					player.updateInventory();
				}
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	public void SpellManager(Player p, LivingEntity t, String action) {
		ItemStack EventItem = p.getItemInHand();
		String race = PlayerData.get(p.getUniqueId())[0];
    	//Spells
    	if(EventItem.equals(SpellIcons.get(0))) OpenSpellbar(p);
    	if(EventItem.equals(SpellIcons.get(1))) CloseSpellbar(p);
		if(race.equalsIgnoreCase("Vampire")) {
			if(EventItem.equals(SpellIcons.get(29)) && action.equals("right")) ToggleRegen(p);
			if(EventItem.equals(SpellIcons.get(31)) && action.equals("right")) ToggleRegen(p);
			if(EventItem.equals(SpellIcons.get(30)) && action.equals("right")) ToggleWater(p);
			if(EventItem.equals(SpellIcons.get(32)) && action.equals("right")) ToggleWater(p);
			if(EventItem.equals(SpellIcons.get(3)) && action.equals("left")) SetTeleportLocation(p);
			if(EventItem.equals(SpellIcons.get(3)) && action.equals("right")) Teleport(p);
			if(EventItem.equals(SpellIcons.get(4)) && action.equals("right")) HighJump(p);
			if(EventItem.equals(SpellIcons.get(5)) && action.equals("right")) Bloodrose(p);
			if(EventItem.equals(SpellIcons.get(43)) && action.equals("right")) Bloodvial(p);
		}
		if(race.equalsIgnoreCase("Werewolf")) {
			if(EventItem.equals(SpellIcons.get(6)) && action.equals("right")) SummonWolf(p);
			if(EventItem.equals(SpellIcons.get(7)) && action.equals("right")) Dash(p);
			if(EventItem.equals(SpellIcons.get(8)) && action.equals("right")) Moonflower(p);
		}
		if(race.equalsIgnoreCase("Ghoul")) {
			if(EventItem.equals(SpellIcons.get(9)) && action.equals("right")) SummonUndead(p);
			if(EventItem.equals(SpellIcons.get(10)) && action.equals("hit")) UnholyBond(p, t);
			if(EventItem.equals(SpellIcons.get(11)) && action.equals("right")) Ghoulish(p);
		}
		if(race.equalsIgnoreCase("Demon")) {
			if(EventItem.equals(SpellIcons.get(12)) && action.equals("right")) Fireball(p);
			if(EventItem.equals(SpellIcons.get(13)) && action.equals("right")) Explosion(p);
			if(EventItem.equals(SpellIcons.get(23)) && action.equals("right")) SummonStrider(p);
			if(EventItem.equals(SpellIcons.get(14)) && action.equals("hit")) Snare(p, t);
			if(EventItem.equals(SpellIcons.get(15)) && action.equals("right")) Hellish(p);
		}
		if(race.equalsIgnoreCase("Priest")) {
			if(EventItem.equals(SpellIcons.get(16)) && action.equals("right")) BanishBall(p);
			if(EventItem.equals(SpellIcons.get(17)) && action.equals("right")) ExorciseBall(p);
			if(EventItem.equals(SpellIcons.get(18)) && action.equals("right")) DrainBall(p);
			if(EventItem.equals(SpellIcons.get(19)) && action.equals("hit")) HealHuman(p, t);
			if(EventItem.equals(SpellIcons.get(20)) && action.equals("hit")) GuardianAngel(p, t);
			if(EventItem.equals(SpellIcons.get(21)) && action.equals("right")) Cure(p);
			if(EventItem.equals(SpellIcons.get(22)) && action.equals("right")) HolyBook(p);
		}
		if(race.equalsIgnoreCase("Necromancer")) {
			if(EventItem.equals(SpellIcons.get(25)) && action.equals("right")) SummonSkeleton(p);
			if(EventItem.equals(SpellIcons.get(26)) && action.equals("right")) SummonUndeadFollower(p);
			if(EventItem.equals(SpellIcons.get(27)) && action.equals("right")) HealUndead(p);
			if(EventItem.equals(SpellIcons.get(28)) && action.equals("right")) SetRessurectionSpawn(p);
			if(EventItem.equals(SpellIcons.get(24)) && action.equals("right")) BookOfDeath(p);
		}
		if(race.equalsIgnoreCase("WitchHunter")) {
			if(EventItem.equals(SpellIcons.get(33)) && action.equals("left")) ArrowSwitcher(p);
			if(EventItem.equals(SpellIcons.get(34)) && action.equals("left")) ArrowSwitcher(p);
			if(EventItem.equals(SpellIcons.get(35)) && action.equals("left")) ArrowSwitcher(p);
			if(EventItem.equals(SpellIcons.get(36)) && action.equals("left")) ArrowSwitcher(p);
			if(EventItem.equals(SpellIcons.get(38)) && action.equals("left")) ArrowSwitcher(p);
			if(EventItem.equals(SpellIcons.get(33)) && action.equals("shoot")) MultiArrow(p);
			if(EventItem.equals(SpellIcons.get(34)) && action.equals("shoot")) GrappleArrow(p);
			if(EventItem.equals(SpellIcons.get(35)) && action.equals("shoot")) FireArrow(p);
			if(EventItem.equals(SpellIcons.get(36)) && action.equals("shoot")) PowerArrow(p);
			if(EventItem.equals(SpellIcons.get(38)) && action.equals("shoot")) VolleyArrow(p);
			if(EventItem.equals(SpellIcons.get(37)) && action.equals("right")) BookOfWitchHunter(p);
		}
		if(race.equalsIgnoreCase("Angel")) {
			if(EventItem.equals(SpellIcons.get(39)) && action.equals("left")) HolySmite(p);
			if(EventItem.equals(SpellIcons.get(40)) && action.equals("left")) Taunt(p);
			if(EventItem.equals(SpellIcons.get(41)) && action.equals("left")) HolyBlessing(p);
			if(EventItem.equals(SpellIcons.get(42)) && action.equals("left")) Wings(p);
		}
	}

	public Inventory getSpellbar(String race, Player player) {
		ItemStack empty = SpellIcons.get(2);
		Inventory spellbar = Bukkit.createInventory(null, 9);
		if(race.equalsIgnoreCase("Human")) {
			spellbar.setItem(0, empty);
			spellbar.setItem(1, empty);
			spellbar.setItem(2, empty);
			spellbar.setItem(3, empty);
			spellbar.setItem(4, empty);
			spellbar.setItem(5, empty);
			spellbar.setItem(6, empty);
			spellbar.setItem(7, empty);
		}
		if(race.equalsIgnoreCase("Vampire")) {
			if(Regeneration.get(player) != null && Regeneration.get(player) == true) {
				ItemStack toggleregen = SpellIcons.get(29);
				spellbar.setItem(0, toggleregen);
			} else {
				ItemStack toggleregen = SpellIcons.get(31);
				spellbar.setItem(0, toggleregen);
			}
			if(WaterBreathing.get(player) != null && WaterBreathing.get(player) == true) {
				ItemStack togglewater = SpellIcons.get(30);
				spellbar.setItem(1, togglewater);
			} else {
				ItemStack togglewater = SpellIcons.get(32);
				spellbar.setItem(1, togglewater);
			}
			ItemStack teleport = SpellIcons.get(3);
			spellbar.setItem(2, teleport);
			ItemStack highjump = SpellIcons.get(4);
			spellbar.setItem(3, highjump);
			ItemStack bloodvial = SpellIcons.get(43);
			spellbar.setItem(4, bloodvial);
			ItemStack bloodrose = SpellIcons.get(5);
			spellbar.setItem(5, bloodrose);
			spellbar.setItem(6, empty);
			spellbar.setItem(7, empty);
		}
		if(race.equalsIgnoreCase("Werewolf")) {
			ItemStack summonwolf = SpellIcons.get(6);
			spellbar.setItem(0, summonwolf);
			ItemStack dash = SpellIcons.get(7);
			spellbar.setItem(1, dash);
			ItemStack moonflower = SpellIcons.get(8);
			spellbar.setItem(2, moonflower);
			spellbar.setItem(3, empty);
			spellbar.setItem(4, empty);
    		spellbar.setItem(5, empty);
    		spellbar.setItem(6, empty);
    		spellbar.setItem(7, empty);
		}
		if(race.equalsIgnoreCase("Ghoul")) {
			ItemStack summonundead = SpellIcons.get(9);
			spellbar.setItem(0, summonundead);
			ItemStack unholybond = SpellIcons.get(10);
			spellbar.setItem(1, unholybond);
			ItemStack ghoulish = SpellIcons.get(11);
			spellbar.setItem(2, ghoulish);
			spellbar.setItem(3, empty);
			spellbar.setItem(4, empty);
			spellbar.setItem(5, empty);
    		spellbar.setItem(6, empty);
    		spellbar.setItem(7, empty);
		}
		if(race.equalsIgnoreCase("Demon")) {
			ItemStack fireball = SpellIcons.get(12);
			spellbar.setItem(0, fireball);
			ItemStack explosion = SpellIcons.get(13);
			spellbar.setItem(1, explosion);
			ItemStack strider = SpellIcons.get(23);
			spellbar.setItem(2, strider);
			ItemStack snare = SpellIcons.get(14);
			spellbar.setItem(3, snare);
			ItemStack hellish = SpellIcons.get(15);
			spellbar.setItem(4, hellish);
			spellbar.setItem(5, empty);
			spellbar.setItem(6, empty);
			spellbar.setItem(7, empty);
		}
		if(race.equalsIgnoreCase("Priest")) {
			ItemStack banishsupernatural = SpellIcons.get(16);
			spellbar.setItem(0, banishsupernatural);
			ItemStack exorcisesupernatural = SpellIcons.get(17);
			spellbar.setItem(1, exorcisesupernatural);
			ItemStack drainsupernatural = SpellIcons.get(18);
			spellbar.setItem(2, drainsupernatural);
			ItemStack healhuman = SpellIcons.get(19);
			spellbar.setItem(3, healhuman);
			ItemStack guardianangel = SpellIcons.get(20);
			spellbar.setItem(4, guardianangel);
			ItemStack createsupernaturalcure = SpellIcons.get(21);
			spellbar.setItem(5, createsupernaturalcure);
			ItemStack createholybook = SpellIcons.get(22);
			spellbar.setItem(6, createholybook);
			spellbar.setItem(7, empty);
		}
		if(race.equalsIgnoreCase("Necromancer")) {
			ItemStack summonSkeleton = SpellIcons.get(25);
			spellbar.setItem(0, summonSkeleton);
			ItemStack summonUndead = SpellIcons.get(26);
			spellbar.setItem(1, summonUndead);
			ItemStack healUndead = SpellIcons.get(27);
			spellbar.setItem(2, healUndead);
			ItemStack ressurectionSpawn = SpellIcons.get(28);
			spellbar.setItem(3, ressurectionSpawn);
			ItemStack createBookOfDeath = SpellIcons.get(24);
			spellbar.setItem(4, createBookOfDeath);
			spellbar.setItem(5, empty);
			spellbar.setItem(6, empty);
			spellbar.setItem(7, empty);
		}
		if(race.equalsIgnoreCase("WitchHunter")) {
			if(ArrowType.get(player) != null && ArrowType.get(player) == 1) {
				ItemStack GrappleArrow = SpellIcons.get(34);
				spellbar.setItem(0, GrappleArrow);
			} else if(ArrowType.get(player) != null && ArrowType.get(player) == 2) {
				ItemStack FireArrow = SpellIcons.get(35);
				spellbar.setItem(0, FireArrow);
			} else if(ArrowType.get(player) != null && ArrowType.get(player) == 3) {
				ItemStack PowerArrow = SpellIcons.get(36);
				spellbar.setItem(0, PowerArrow);
			} else if(ArrowType.get(player) != null && ArrowType.get(player) == 4) {
				ItemStack PowerArrow = SpellIcons.get(38);
				spellbar.setItem(0, PowerArrow);
			} else {
				ItemStack MultiShot = SpellIcons.get(33);
				spellbar.setItem(0, MultiShot);
			}
			ItemStack createBookOfWitchHunter = SpellIcons.get(37);
			spellbar.setItem(1, createBookOfWitchHunter);
			spellbar.setItem(2, empty);
			spellbar.setItem(3, empty);
			spellbar.setItem(4, empty);
			spellbar.setItem(5, empty);
			spellbar.setItem(6, empty);
			spellbar.setItem(7, empty);
		}
		if(race.equalsIgnoreCase("Angel")) {
			ItemStack HolySmite = SpellIcons.get(39);
			spellbar.setItem(0, HolySmite);
			ItemStack Taunt = SpellIcons.get(40);
			spellbar.setItem(1, Taunt);
			ItemStack HolyBlessing = SpellIcons.get(41);
			spellbar.setItem(2, HolyBlessing);
			ItemStack Wings = SpellIcons.get(42);
			spellbar.setItem(3, Wings);
			spellbar.setItem(4, empty);
			spellbar.setItem(5, empty);
			spellbar.setItem(6, empty);
			spellbar.setItem(7, empty);
		}
		if(race.equalsIgnoreCase("Shade")) {
			spellbar.setItem(0, empty);
			spellbar.setItem(1, empty);
			spellbar.setItem(2, empty);
			spellbar.setItem(3, empty);
			spellbar.setItem(4, empty);
			spellbar.setItem(5, empty);
			spellbar.setItem(6, empty);
			spellbar.setItem(7, empty);
		}
		if(race.equalsIgnoreCase("Siren")) {
			spellbar.setItem(0, empty);
			spellbar.setItem(1, empty);
			spellbar.setItem(2, empty);
			spellbar.setItem(3, empty);
			spellbar.setItem(4, empty);
			spellbar.setItem(5, empty);
			spellbar.setItem(6, empty);
			spellbar.setItem(7, empty);
		}
		if(race.equalsIgnoreCase("EarthElemental")) {
			spellbar.setItem(0, empty);
			spellbar.setItem(1, empty);
			spellbar.setItem(2, empty);
			spellbar.setItem(3, empty);
			spellbar.setItem(4, empty);
			spellbar.setItem(5, empty);
			spellbar.setItem(6, empty);
			spellbar.setItem(7, empty);
		}
		if(race.equalsIgnoreCase("WaterElemental")) {
			spellbar.setItem(0, empty);
			spellbar.setItem(1, empty);
			spellbar.setItem(2, empty);
			spellbar.setItem(3, empty);
			spellbar.setItem(4, empty);
			spellbar.setItem(5, empty);
			spellbar.setItem(6, empty);
			spellbar.setItem(7, empty);
		}
		if(race.equalsIgnoreCase("FireElemental")) {
			spellbar.setItem(0, empty);
			spellbar.setItem(1, empty);
			spellbar.setItem(2, empty);
			spellbar.setItem(3, empty);
			spellbar.setItem(4, empty);
			spellbar.setItem(5, empty);
			spellbar.setItem(6, empty);
			spellbar.setItem(7, empty);
		}
		spellbar.setItem(8, SpellIcons.get(1));
		return spellbar;
	}
	
	public void OpenSpellbar(Player p) {
		if(!this.getCooldown(p.getUniqueId(),"HotbarSwitcher")) {
			this.setCooldown(p.getUniqueId(), "HotbarSwitcher", 5);
			SpellbarOpen.put(p, true);
			Inventory inv = p.getInventory();
			String race = PlayerData.get(p.getUniqueId())[0];
			//Save Hotbar
			Inventory hotbar = Bukkit.createInventory(null, 9);
			for(int i=0; i<8; i++) {
				hotbar.setItem(i, inv.getItem(i));
			}
			Hotbars.put(p, hotbar);
			//Replace Hotbar with Spellbar for race
			Inventory spellbar = getSpellbar(race, p);
			for(int i=0; i<9; i++) {
				inv.setItem(i, spellbar.getItem(i));
			}
			p.updateInventory();
		} else {
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.cooldown").replaceAll("%prefix%", this.getConfig().getString("Messages.prefix"))));
		}
	}
	
	public void CloseSpellbar(Player p) {
		if(!this.getCooldown(p.getUniqueId(),"HotbarSwitcher")) {
			this.setCooldown(p.getUniqueId(), "HotbarSwitcher", 5);
			SpellbarOpen.put(p, false);
			Inventory inv = p.getInventory();
			if(Hotbars.containsKey(p)) {
	    		Inventory hotbar = Hotbars.get(p);
	    		for(int i=0; i<8; i++) {
	    			inv.setItem(i, hotbar.getItem(i));
	    		}
	    		Hotbars.remove(p);
			} else {
	    		for(int i=0; i<8; i++) {
	    			inv.setItem(i, new ItemStack(Material.AIR));
	    		}
			}
			inv.setItem(8, SpellIcons.get(0));
			p.updateInventory();
		} else {
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.cooldown").replaceAll("%prefix%", this.getConfig().getString("Messages.prefix"))));
		}
	}
	
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event) {
		Location l = event.getEntity().getLocation();
		Player p = event.getEntity();
		if(event.getDrops().contains(SpellIcons.get(1))) {
    		Inventory hotbar = Hotbars.get(p);
    		for(int i=0; i<8; i++) {
    			if(hotbar.getItem(i) != null) {
        			event.getDrops().add(hotbar.getItem(i));
    			}
    		}
    		Hotbars.remove(p);
		}
		for(ItemStack drop : event.getDrops()) {
			Boolean add = true;
			for(ItemStack item : SpellIcons) {
				if(drop == null || drop.equals(item)) {
					add = false;
				}
			}
			if(add) {
				l.getWorld().dropItem(l, drop);
			}
		}
		event.getDrops().clear();
	}
	
	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		if(player.getHealth() == 0) {
			return;
		}
		ItemStack item = player.getInventory().getItem(8);
		if(item.equals(SpellIcons.get(1))) {
			CloseSpellbar(player);
		}
		player.updateInventory();
	}
	
	public void addItemSafely(Player player, ItemStack item) {
		if(player.getInventory().firstEmpty() == -1) {
			player.getWorld().dropItem(player.getLocation(), item);
		} else {
			player.getInventory().setItem(player.getInventory().firstEmpty(), item);
		}
	}
	
	//Allow shooting of bows from abilities without arrows
	
	@EventHandler
	public void PlayerItemHeldEvent(PlayerItemHeldEvent e) {
		returnItem(e.getPlayer());
	}

	@EventHandler
	public void PlayerDropItemEvent(PlayerDropItemEvent e) {
		returnItem(e.getPlayer());
	}
	 
	public void EntityShootBowEvent(EntityShootBowEvent e) {
		if(e.getEntity() instanceof Player) {
			returnItem((Player) e.getEntity());
		}
	}
	   
	private void returnItem(Player player) {
		if (arrowReplacedItem.containsKey(player.getPlayer())) {
			int slot = player.getInventory().getSize() - 1;
			player.getInventory().setItem(slot, arrowReplacedItem.get(player));
			player.updateInventory();
			arrowReplacedItem.remove(player);
		}
	}

	@EventHandler
	public void PlayerInteractEvent(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		if (!(e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK))) return;
		if(arrowReplacedItem.containsKey(p)) return;
		if(!(p.getItemInHand().getType().equals(Material.BOW))) return;
		if(!SpellIcons.contains(p.getItemInHand())) return;
		int slot = p.getInventory().getSize() - 1;
		ItemStack item = p.getInventory().getItem(slot);
		arrowReplacedItem.put(p, item);
		p.getInventory().setItem(slot, new ItemStack(Material.ARROW, 1));
	}
	
	//Get Variable
	
	public static Boolean isArmour(ItemStack item) {
	    return (CraftItemStack.asNMSCopy(item).getItem() instanceof ItemArmor);
	}

	public static Boolean isDay(Player player) {
		Boolean day = (0000<=player.getWorld().getTime() && player.getWorld().getTime()<14000);
		Boolean clear = !player.getWorld().hasStorm();
		return (day && clear);
	}
	
	public static Boolean inLava(Player player) {
		return (player.getLocation().getBlock().getType() == Material.LAVA);
	}

	public static Boolean inNether(Player player) {
		return (player.getWorld().getEnvironment() == World.Environment.NETHER);
	}

	public Boolean isSupernatural(Entity entity) {
		if(entity instanceof Player) {
			Player target = (Player) entity;
			String targetrace = PlayerData.get(target.getUniqueId())[0];
			if(targetrace.equalsIgnoreCase("Vampire") || targetrace.equalsIgnoreCase("Werewolf") || targetrace.equalsIgnoreCase("Ghoul") || targetrace.equalsIgnoreCase("Demon")) {
				return true;
			}
		}
		return false;
	}
	
	public Boolean isMonster(EntityType entity) {
		for(EntityType e : monsters) {
			if(entity == e) {
				return true;
			}
		}
		return false;
	}
	
	public Boolean isUndead(EntityType entity) {
		for(EntityType e : undead) {
			if(entity == e) {
				return true;
			}
		}
		return false;
	}
	
	public void setMagic(Player player, double d) {
		String[] playerdata = PlayerData.get(player.getUniqueId());
		playerdata[1] = ("" + d);
		PlayerData.put(player.getUniqueId(), playerdata);
	}

	public void changeMagic(Player player, double i) {
		String[] playerdata = PlayerData.get(player.getUniqueId());
		double magic = Double.parseDouble(playerdata[1])+i;
		if(magic<0) {
			magic=0;
		}
		playerdata[1] = ("" + magic);
		PlayerData.put(player.getUniqueId(), playerdata);
	}

	public double getMagic(Player player) {
		return Double.parseDouble(PlayerData.get(player.getUniqueId())[1]);
	}

	public Boolean setRace(Player player, String race, Boolean cureFirst) {
    	if(getRace(player).equals(race)) {
    		player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.already-class").replaceAll("%prefix%", this.getConfig().getString("Messages.prefix")).replaceAll("%class%", race.replaceAll("([^A-Z^_^ ])([A-Z])", "$1 $2"))));
    		return false;
    	}
    	if((cureFirst == true) && (getRace(player).equals("Werewolf") || getRace(player).equals("Vampire") || getRace(player).equals("Ghoul") || getRace(player).equals("Demon"))) {
    		player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.cured-first").replaceAll("%prefix%", this.getConfig().getString("Messages.prefix")).replaceAll("%class%", race.replaceAll("([^A-Z^_^ ])([A-Z])", "$1 $2"))));
    		return false;
    	}
		if(race.equalsIgnoreCase("Human") || race.equalsIgnoreCase("Vampire") || race.equalsIgnoreCase("Werewolf") || race.equalsIgnoreCase("Ghoul") || race.equalsIgnoreCase("Demon") || race.equalsIgnoreCase("Necromancer") || race.equalsIgnoreCase("Priest") || race.equalsIgnoreCase("WitchHunter")) {
			this.setRaceDirect(player, race);
			if(race.equalsIgnoreCase("Vampire")) {
	    		player.getInventory().addItem(CustomItems.get(1));
			}
			if(race.equalsIgnoreCase("Werewolf")) {
	    		player.getInventory().addItem(CustomItems.get(3));
			}
			if(race.equalsIgnoreCase("Ghoul")) {
	    		player.getInventory().addItem(CustomItems.get(5));
			}
			if(race.equalsIgnoreCase("Demon")) {
	    		player.getInventory().addItem(CustomItems.get(7));
			}
			if(race.equalsIgnoreCase("Priest")) {
    			player.getInventory().addItem(CustomItems.get(10));
    		}
			if(race.equalsIgnoreCase("Necromancer")) {
    			player.getInventory().addItem(CustomItems.get(12));
    		}
			if(race.equalsIgnoreCase("WitchHunter")) {
    			player.getInventory().addItem(CustomItems.get(14));
    		}
			return true;
		}
		return false;
	}

	public void setRaceDirect(Player player, String race) {
		if(race.equalsIgnoreCase("Human")) race = "Human";
		else if(race.equalsIgnoreCase("Vampire")) race = "Vampire";
		else if(race.equalsIgnoreCase("Werewolf")) race = "Werewolf";
		else if(race.equalsIgnoreCase("Ghoul")) race = "Ghoul";
		else if(race.equalsIgnoreCase("Demon")) race = "Demon";
		else if(race.equalsIgnoreCase("Necromancer")) race = "Necromancer";
		else if(race.equalsIgnoreCase("Priest")) race = "Priest";
		else if(race.equalsIgnoreCase("WitchHunter")) race = "WitchHunter";
		else if(race.equalsIgnoreCase("Angel")) race = "Angel";
		else if(race.equalsIgnoreCase("Shade")) race = "Shade";
		else if(race.equalsIgnoreCase("Siren")) race = "Siren";
		else if(race.equalsIgnoreCase("EarthElemental")) race = "EarthElemental";
		else if(race.equalsIgnoreCase("WaterElemental")) race = "WaterElemental";
		else if(race.equalsIgnoreCase("FireElemental")) race = "FireElemental";
		else {
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.unknown-race").replaceAll("%prefix%", this.getConfig().getString("Messages.prefix"))));
			return;
		}
		String[] newData = PlayerData.get(player.getUniqueId());
		newData[0] = race;
	    PlayerData.put(player.getUniqueId(), newData);
		player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.setrace-success").replaceAll("%prefix%", this.getConfig().getString("Messages.prefix")).replaceAll("%class%", race.replaceAll("([^A-Z^_^ ])([A-Z])", "$1 $2"))));
		ejectArmour(player);
	}
	
	public String getRace(Player player) {
		String[] newData = PlayerData.get(player.getUniqueId());
    	return newData[0];
	}
	
	@SuppressWarnings("deprecation")
	public Boolean underSky(Player player) {
		for(int i=player.getLocation().getBlock().getY(); i<=255; i++) {
			Location loc = new Location(player.getWorld(), player.getLocation().getBlock().getX(), i, player.getLocation().getBlock().getZ());
			if(!loc.getBlock().getType().isTransparent()) {
				return false;
			}
		}
		return true;
	}
	
	public Entity getEntityFromUUID(UUID uuid) {
		for(World world : Bukkit.getWorlds()) {
			for(Entity entity : world.getEntities()) {
				if(entity.getUniqueId().equals(uuid)) {
					return entity;
				}
			}
		}
		return null;
	}
	
	
	/**
	 * Follower Logic
	 */
	
	public void Followers(int tick) {
		for(World world : Bukkit.getWorlds()) {
			for(Entity entity : world.getLivingEntities()) {
				if(entity instanceof Monster) {
					Monster monster = (Monster) entity;
					if(monster.getPersistentDataContainer().has(new NamespacedKey(this, "Leader"), PersistentDataType.STRING)) {
						Player leader = Bukkit.getPlayer(UUID.fromString(monster.getPersistentDataContainer().get(new NamespacedKey(this, "Leader"), PersistentDataType.STRING)));
						if(monster.getPersistentDataContainer().has(new NamespacedKey(this, "Following"), PersistentDataType.INTEGER)) {
							int following = monster.getPersistentDataContainer().get(new NamespacedKey(this, "Following"), PersistentDataType.INTEGER);

							//FollowerStayClose
							if(tick%5==0) {
								if(following == 1) {
									monster.removePotionEffect(PotionEffectType.SLOW);
									EntityInsentient e = ((EntityInsentient) ((CraftEntity) monster).getHandle());
									if(leader != null && leader.isOnline()) {
										if(monster.getTarget() == null) {
									        if((leader.getLocation().distance(monster.getLocation()) > 5)) {
									            e.getNavigation().a(leader.getLocation().getX(), leader.getLocation().getY(), leader.getLocation().getZ(), (float) 1.5);
									        } else if((leader.getLocation().distance(monster.getLocation()) < 3)) {
									        	e.getNavigation().a(monster.getLocation().getX(), monster.getLocation().getY(), monster.getLocation().getZ(), (float) 1.5);
									        }
										}
										if((leader.getLocation().distance(monster.getLocation()) > 20)) {
											monster.setTarget(null);
											monster.teleport(leader);
										}
									} else if(monster.getTarget() == null) {
									    e.getNavigation().a(monster.getLocation().getX(), monster.getLocation().getY(), monster.getLocation().getZ(), (float) 1.5);
									}
								} else {
									monster.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 2147483647, 255, true, true, false));
								}
							}
							
							//FollowerUntargetDead
							if(monster.getTarget() != null) {
								if(monster.getTarget().isDead() || monster.getLocation().distance(monster.getTarget().getLocation()) > 48) {
									monster.setTarget(null);
								}
							}
						}
					}
				}
			}
		}
	}
	
	@EventHandler
	public void FollowerRightClickToStay(PlayerInteractEntityEvent event) {
		if(event.getHand() == EquipmentSlot.OFF_HAND)
		return;
        Entity follower = event.getRightClicked();
        Player player = event.getPlayer();
		if(follower.getPersistentDataContainer().has(new NamespacedKey(this, "Leader"), PersistentDataType.STRING)) {
			UUID leaderUUID = UUID.fromString(follower.getPersistentDataContainer().get(new NamespacedKey(this, "Leader"), PersistentDataType.STRING));
			if(player.getUniqueId().equals(leaderUUID)) {
				if(follower.getPersistentDataContainer().has(new NamespacedKey(this, "Following"), PersistentDataType.INTEGER)) {
					int following = follower.getPersistentDataContainer().get(new NamespacedKey(this, "Following"), PersistentDataType.INTEGER);
					following = (following+1) % 2;
					if(following == 1) {
						player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.follower-follow").replaceAll("%prefix%", this.getConfig().getString("Messages.prefix"))));
					} else {
						player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.follower-wait").replaceAll("%prefix%", this.getConfig().getString("Messages.prefix"))));
					}
					follower.getPersistentDataContainer().set(new NamespacedKey(this, "Following"), PersistentDataType.INTEGER, following);
				}
			}
		}
	}
	
	@EventHandler	
	public void FollowerNeutralize(EntityTargetEvent event) {
		if(event.getEntity() instanceof Monster) {
			Monster entity = (Monster) event.getEntity();
			//Bukkit.broadcastMessage("target event triggered");
			if(entity.getPersistentDataContainer().has(new NamespacedKey(this, "Leader"), PersistentDataType.STRING)) {
				UUID entityleaderUUID = UUID.fromString(entity.getPersistentDataContainer().get(new NamespacedKey(this, "Leader"), PersistentDataType.STRING));
				//Bukkit.broadcastMessage("targetter is a follower");

				//If not retaliation cancel the event
				if(!event.getReason().equals(TargetReason.TARGET_ATTACKED_ENTITY)) {
					event.setCancelled(true);
					//Bukkit.broadcastMessage("is not retaliation | cancelling");
					return;
				} else {
					//Bukkit.broadcastMessage("is retaliation");
				}
				
				//If its the leader cancel the targetting event
				if(entityleaderUUID.equals(event.getTarget().getUniqueId())) {
					event.setCancelled(true);
					//Bukkit.broadcastMessage("target is leader | cancelling");
					return;
				} else {
					//Bukkit.broadcastMessage("target is not leader");
				}
				
				//If its one of the leaders followers cancel the targetting event
				if(!(event.getTarget() instanceof Player) && event.getTarget().getPersistentDataContainer().has(new NamespacedKey(this, "Leader"), PersistentDataType.STRING)) {
					UUID targetleaderUUID = UUID.fromString(event.getTarget().getPersistentDataContainer().get(new NamespacedKey(this, "Leader"), PersistentDataType.STRING));
					if(entityleaderUUID.equals(targetleaderUUID)) {
						event.setCancelled(true);
						//Bukkit.broadcastMessage("target is one of the leaders followers | cancelling");
						return;
					}
				} else {
					//Bukkit.broadcastMessage("target is not one of the leaders followers");
				}
				//Bukkit.broadcastMessage("passes all checks");
				
				//Stop waiting
				if(entity.getPersistentDataContainer().has(new NamespacedKey(this, "Following"), PersistentDataType.INTEGER)) {
					entity.getPersistentDataContainer().set(new NamespacedKey(this, "Following"), PersistentDataType.INTEGER, 1);
				}
			} else {
				//Bukkit.broadcastMessage("targetter is not a follower");
			}
		}
	}
	
	@EventHandler
	public void FollowerCombatSense(EntityDamageByEntityEvent event) {
		
		//figure out who the attacker and victim are
		LivingEntity victim = (LivingEntity) event.getEntity();
		LivingEntity attacker = null;
		if(event.getDamager() instanceof LivingEntity) {
			attacker = (LivingEntity) event.getDamager();
		} else if(event.getDamager() instanceof Arrow) {
			Arrow arrow = (Arrow) event.getDamager();
			if(arrow.getShooter() instanceof LivingEntity) {
				attacker = (LivingEntity) arrow.getShooter();
			} else {
				return;
			}
		} else {
			return;
		}
		
		if(attacker instanceof Player) { //if attacker is player
			//Bukkit.broadcastMessage("attacker is player");
			if(!victim.getPersistentDataContainer().has(new NamespacedKey(this, "Leader"), PersistentDataType.STRING)
					|| !UUID.fromString(victim.getPersistentDataContainer().get(new NamespacedKey(this, "Leader"), PersistentDataType.STRING)).equals(attacker.getUniqueId())) { //if the victim is not a follower of the attacker
				//Bukkit.broadcastMessage("victim is not a follower");
				//Bukkit.broadcastMessage("searching for followers...");
				for(Entity entity : attacker.getNearbyEntities(128, 256, 128)) { //get all nearby entities
					if(entity.getPersistentDataContainer().has(new NamespacedKey(this, "Leader"), PersistentDataType.STRING)) {
						UUID leaderUUID = UUID.fromString(entity.getPersistentDataContainer().get(new NamespacedKey(this, "Leader"), PersistentDataType.STRING));
						if(leaderUUID.equals(attacker.getUniqueId())) { //if follower of the attacker
							//Bukkit.broadcastMessage("follower found");
							if(entity instanceof Monster) {
								//Bukkit.broadcastMessage("follower is monster");
								Monster monster = (Monster) entity;
								monster.setTarget(victim); //attack the victim
								//Bukkit.broadcastMessage("victim is being targetted by follower");
							}
						}
					}
				}
			}
		}
		
		if(victim instanceof Player) { //if victim is player
			//Bukkit.broadcastMessage("victim is player");
			if(!attacker.getPersistentDataContainer().has(new NamespacedKey(this, "Leader"), PersistentDataType.STRING)
					|| !UUID.fromString(attacker.getPersistentDataContainer().get(new NamespacedKey(this, "Leader"), PersistentDataType.STRING)).equals(victim.getUniqueId())) { //if the victim is not a follower of the attacker
				//Bukkit.broadcastMessage("attacker is not a follower");
				//Bukkit.broadcastMessage("searching for followers...");
				for(Entity entity : victim.getNearbyEntities(128, 256, 128)) { //get all nearby entities
					if(entity.getPersistentDataContainer().has(new NamespacedKey(this, "Leader"), PersistentDataType.STRING)) {
						UUID leaderUUID = UUID.fromString(entity.getPersistentDataContainer().get(new NamespacedKey(this, "Leader"), PersistentDataType.STRING));
						if(leaderUUID.equals(victim.getUniqueId())) { //if follower of the victim
							//Bukkit.broadcastMessage("follower found");
							if(entity instanceof Monster) {
								//Bukkit.broadcastMessage("follower is monster");
								Monster monster = (Monster) entity;
								monster.setTarget(attacker); //attack the attacker
								//Bukkit.broadcastMessage("2 is being targetted by follower");
							}
						}
					}
				}
			}
		}
	}
	
	/**
	 * Passive Abilities
	 */
	
	public void tickCooldowns() {
		for(UUID uuid : Cooldowns.keySet()) {
			Map<String, Integer> cooldowns = Cooldowns.get(uuid);
			for(String cooldown : cooldowns.keySet()) {
				int ticks = cooldowns.get(cooldown)-1;
				if(ticks > 0) {
					cooldowns.put(cooldown, ticks);
				} else {
					cooldowns.remove(cooldown);
				}
			}
			Cooldowns.put(uuid, cooldowns);
		}
	}
	
	public void setCooldown(UUID uuid, String cooldown, Integer ticks) {
		Map<String, Integer> newCooldowns = new HashMap<String, Integer>();
		if(Cooldowns.containsKey(uuid)) {
			newCooldowns = Cooldowns.get(uuid);
		}
		newCooldowns.put(cooldown, ticks);
		Cooldowns.put(uuid, newCooldowns);
	}
	
	public Boolean getCooldown(UUID uuid, String cooldown) {
		try {
			return Cooldowns.get(uuid).containsKey(cooldown);
		} catch(Exception e) {
			return false;
		}
	}
	
	public void NightVision(PlayerRespawnEvent event) {
		Player player = event.getPlayer();
		String race = PlayerData.get(player.getUniqueId())[0];
		if(race.equalsIgnoreCase("Vampire") || (race.equalsIgnoreCase("Werewolf") && !isDay(player)) || race.equalsIgnoreCase("Ghoul") || race.equalsIgnoreCase("Demon") || race.equalsIgnoreCase("Necromancer")) {
			player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 320, 1));
		}
	}
	
	public void NightVision(Player player) {
		String race = PlayerData.get(player.getUniqueId())[0];
		if(race.equalsIgnoreCase("Vampire") || (race.equalsIgnoreCase("Werewolf") && !isDay(player)) || race.equalsIgnoreCase("Ghoul") || race.equalsIgnoreCase("Demon") || race.equalsIgnoreCase("Necromancer")) {
			player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 320, 1));
		}
	}
	
	public void Speed(Player player) {
		String race = PlayerData.get(player.getUniqueId())[0];
		if(race.contains("Werewolf") && !isDay(player)) {
			player.setWalkSpeed(0.4F);
		} else {
			player.setWalkSpeed(0.2F);
		}
	}
		
	public void Regeneration(Player player) {
		VampireRegeneration(player);
		String race = PlayerData.get(player.getUniqueId())[0];
		if((race.equalsIgnoreCase("Werewolf") && isDay(player)) || race.equalsIgnoreCase("Ghoul") || (race.equalsIgnoreCase("Demon") && inLava(player))) {
			//If can heal
			if(player.getHealth() == 20 || player.getHealth() == 0) {
				return;
			}
			//Heal the player by half a heart
			if(player.getHealth()+1<=20) {
				player.setHealth(player.getHealth()+1);
			} else {
				player.setHealth(20);
			}
		}
 	}

	public void VampireRegeneration(Player player) {
		String race = PlayerData.get(player.getUniqueId())[0];
		if(race.equalsIgnoreCase("Vampire")) {
			if(!Regeneration.containsKey(player)) {
				Regeneration.put(player, false);
			}
			if(!Regeneration.get(player)) {
				return;
			}
			//If not at full health
			if(player.getHealth() == 20 || player.getHealth() == 0) {
				return;
			}
			
			if(getMagic(player) < this.getConfig().getInt("SpellCosts.Regeneration")) {
				return;
			}
			changeMagic(player, -this.getConfig().getInt("SpellCosts.Regeneration"));
			
			//Heal the player by half a heart
			if(player.getHealth()+1<=20) {
				player.setHealth(player.getHealth()+1);
			} else {
				player.setHealth(20);
			}
		}
 	}

	public void WaterBreathing(Player player) {
		String race = PlayerData.get(player.getUniqueId())[0];
		if(race.equalsIgnoreCase("Vampire")) {
			if(!WaterBreathing.containsKey(player)) {
				WaterBreathing.put(player, false);
			}
			if(!WaterBreathing.get(player)) {
				return;
			}
			if(player.getRemainingAir()<300) {
				if(getMagic(player) < this.getConfig().getInt("SpellCosts.Water-Breathing")) {
					return;
				}
				changeMagic(player, -this.getConfig().getInt("SpellCosts.Water-Breathing"));
				int air = player.getRemainingAir()+30;
				if(air>300) air = 300;
				player.setRemainingAir(air);
			}
		}
	}

	public void MagicFromHeat(Player p) {
		if(PlayerData.get(p.getUniqueId())[0].contains("Demon")) {
			Boolean lava = inLava(p);
			Boolean nether = inNether(p);
			if(!nether && !lava) {
				if(getMagic(p) < 5) {
					return;
				}
				changeMagic(p, -5);
			}
			if(lava) {
				if(getMagic(p) > 9995) {
					return;
				}
				changeMagic(p, 5);
			}
		}
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void KillRewards(EntityDeathEvent event) {
		//If a player
		Player killer = null;
		if(event.getEntity().getKiller() instanceof Arrow) {
			Arrow arrow = (Arrow) event.getEntity().getKiller();
			if(arrow.getShooter() instanceof Player) {
				killer = (Player) arrow.getShooter();
			} else {
				return;
			}
		} else if(event.getEntity().getKiller() instanceof Player) {
			killer = event.getEntity().getKiller();
		} else {
			return;
		}
		String race = PlayerData.get(killer.getUniqueId())[0];
		if(!(event.getEntity().getKiller() instanceof Player)) {
			return;
		}
		
		if(race.equalsIgnoreCase("Priest")) {
			return;
		}
		
		if(race.equalsIgnoreCase("Necromancer")) {
			if(!(event.getEntity() instanceof Player)) {
				return;
			}
		}

		if(race.equalsIgnoreCase("WitchHunter")) {
			if(event.getEntity() instanceof Player) {
				CollectBounty((Player) event.getEntity(), event.getEntity().getKiller());
			} else if(!monsters.contains(event.getEntityType())) {
				return;
			}
		}
		if(race.equalsIgnoreCase("Vampire")) {
			int num = killer.getFoodLevel() + (int) (event.getEntity().getMaxHealth()/2);
			if(num >= 20) num = 20;
			killer.setFoodLevel(num);
		}
		
		//Increase magic based on victims max health
		String[] playerdata = PlayerData.get(killer.getUniqueId());
		double magic = Double.parseDouble(playerdata[1]) + (event.getEntity().getMaxHealth()*20);
		if(magic>10000) {
			magic=10000;
		}
		playerdata[1] = ("" + magic);
		PlayerData.put(killer.getUniqueId(), playerdata);
		return;
	}
	
	@EventHandler
	public void DamageCalculation(EntityDamageByEntityEvent event) {
		
		//Attacker Damage Modifiers / Valid attack check
		if(event.getDamager() instanceof Player) {
			Player player = (Player) event.getDamager();
			String race = PlayerData.get(player.getUniqueId())[0];
			Boolean lava = (player.getLocation().getBlock().getType() == Material.LAVA);
			Boolean nether = (player.getWorld().getEnvironment() == World.Environment.NETHER);
			if(race.equalsIgnoreCase("Vampire")) {
				event.setDamage(event.getDamage()*2);
			}
			if(race.equalsIgnoreCase("Werewolf") && !isDay(player)) {
				event.setDamage(event.getDamage()*4);
			}
			if(race.equalsIgnoreCase("Necromancer") && (getLightLevel(player) < 8)) {
				event.setDamage(event.getDamage()*1.5);
			}
			if(race.equalsIgnoreCase("Demon") && (lava || nether)) {
				event.setDamage(event.getDamage()*1.5);
			}
			if(race.equalsIgnoreCase("Priest")) {
				if(isSupernatural(event.getEntity())) {
					event.setDamage(event.getDamage()*2.5);
					if(Math.random()*10 < 1) {
						event.getEntity().setFireTicks(100);
					}
				}
				if(isMonster(event.getEntityType())) {
					if(Math.random()*8 < 1) {
						event.getEntity().setFireTicks(100);
					}
				}
			}
			
			//class can damage entity check
			EntityType e = event.getEntity().getType();
			if(e == EntityType.PIG || e == EntityType.SHEEP || e == EntityType.COW || e == EntityType.FOX || e == EntityType.BAT || e == EntityType.CHICKEN || e == EntityType.COD || e == EntityType.OCELOT || e == EntityType.RABBIT || e == EntityType.SALMON || e == EntityType.MUSHROOM_COW || e == EntityType.SQUID || e == EntityType.TROPICAL_FISH || e == EntityType.TURTLE || e == EntityType.VILLAGER || e == EntityType.WANDERING_TRADER || e == EntityType.PUFFERFISH || e == EntityType.DONKEY || e == EntityType.HORSE || e == EntityType.CAT || e == EntityType.PARROT || e == EntityType.MULE || e == EntityType.SKELETON_HORSE) {
				if(race.equalsIgnoreCase("Priest")) {
					event.setCancelled(true);
				}
			}
			
			//class can use weapon check
			if(player.getEquipment().getItemInMainHand() != null && player.getEquipment().getItemInMainHand().getType() != Material.AIR) {
				if(race.equalsIgnoreCase("Ghoul")) { //No Weapons
					if(player.getEquipment().getItemInMainHand().getType() == Material.NETHERITE_SWORD ||
							player.getEquipment().getItemInMainHand().getType() == Material.NETHERITE_AXE ||
							player.getEquipment().getItemInMainHand().getType() == Material.NETHERITE_HOE ||
							player.getEquipment().getItemInMainHand().getType() == Material.NETHERITE_PICKAXE ||
							player.getEquipment().getItemInMainHand().getType() == Material.NETHERITE_SHOVEL ||
							player.getEquipment().getItemInMainHand().getType() == Material.DIAMOND_SWORD ||
							player.getEquipment().getItemInMainHand().getType() == Material.DIAMOND_AXE ||
							player.getEquipment().getItemInMainHand().getType() == Material.DIAMOND_HOE ||
							player.getEquipment().getItemInMainHand().getType() == Material.DIAMOND_PICKAXE ||
							player.getEquipment().getItemInMainHand().getType() == Material.DIAMOND_SHOVEL ||
							player.getEquipment().getItemInMainHand().getType() == Material.IRON_SWORD ||
							player.getEquipment().getItemInMainHand().getType() == Material.IRON_AXE ||
							player.getEquipment().getItemInMainHand().getType() == Material.IRON_HOE ||
							player.getEquipment().getItemInMainHand().getType() == Material.IRON_PICKAXE ||
							player.getEquipment().getItemInMainHand().getType() == Material.IRON_SHOVEL ||
							player.getEquipment().getItemInMainHand().getType() == Material.GOLDEN_SWORD ||
							player.getEquipment().getItemInMainHand().getType() == Material.GOLDEN_AXE ||
							player.getEquipment().getItemInMainHand().getType() == Material.GOLDEN_HOE ||
							player.getEquipment().getItemInMainHand().getType() == Material.GOLDEN_PICKAXE ||
							player.getEquipment().getItemInMainHand().getType() == Material.GOLDEN_SHOVEL ||
							player.getEquipment().getItemInMainHand().getType() == Material.STONE_SWORD ||
							player.getEquipment().getItemInMainHand().getType() == Material.STONE_AXE ||
							player.getEquipment().getItemInMainHand().getType() == Material.STONE_HOE ||
							player.getEquipment().getItemInMainHand().getType() == Material.STONE_PICKAXE ||
							player.getEquipment().getItemInMainHand().getType() == Material.STONE_SHOVEL ||
							player.getEquipment().getItemInMainHand().getType() == Material.WOODEN_SWORD ||
							player.getEquipment().getItemInMainHand().getType() == Material.WOODEN_AXE ||
							player.getEquipment().getItemInMainHand().getType() == Material.WOODEN_HOE ||
							player.getEquipment().getItemInMainHand().getType() == Material.WOODEN_PICKAXE ||
							player.getEquipment().getItemInMainHand().getType() == Material.WOODEN_SHOVEL) {
						event.setCancelled(true);
					}
				}
				if(race.equalsIgnoreCase("Werewolf") && !isDay(player)) { //No Weapons
					if(player.getEquipment().getItemInMainHand().getType() == Material.NETHERITE_SWORD ||
							player.getEquipment().getItemInMainHand().getType() == Material.NETHERITE_AXE ||
							player.getEquipment().getItemInMainHand().getType() == Material.NETHERITE_HOE ||
							player.getEquipment().getItemInMainHand().getType() == Material.NETHERITE_PICKAXE ||
							player.getEquipment().getItemInMainHand().getType() == Material.NETHERITE_SHOVEL ||
							player.getEquipment().getItemInMainHand().getType() == Material.DIAMOND_SWORD ||
							player.getEquipment().getItemInMainHand().getType() == Material.DIAMOND_AXE ||
							player.getEquipment().getItemInMainHand().getType() == Material.DIAMOND_HOE ||
							player.getEquipment().getItemInMainHand().getType() == Material.DIAMOND_PICKAXE ||
							player.getEquipment().getItemInMainHand().getType() == Material.DIAMOND_SHOVEL ||
							player.getEquipment().getItemInMainHand().getType() == Material.IRON_SWORD ||
							player.getEquipment().getItemInMainHand().getType() == Material.IRON_AXE ||
							player.getEquipment().getItemInMainHand().getType() == Material.IRON_HOE ||
							player.getEquipment().getItemInMainHand().getType() == Material.IRON_PICKAXE ||
							player.getEquipment().getItemInMainHand().getType() == Material.IRON_SHOVEL ||
							player.getEquipment().getItemInMainHand().getType() == Material.GOLDEN_SWORD ||
							player.getEquipment().getItemInMainHand().getType() == Material.GOLDEN_AXE ||
							player.getEquipment().getItemInMainHand().getType() == Material.GOLDEN_HOE ||
							player.getEquipment().getItemInMainHand().getType() == Material.GOLDEN_PICKAXE ||
							player.getEquipment().getItemInMainHand().getType() == Material.GOLDEN_SHOVEL ||
							player.getEquipment().getItemInMainHand().getType() == Material.STONE_SWORD ||
							player.getEquipment().getItemInMainHand().getType() == Material.STONE_AXE ||
							player.getEquipment().getItemInMainHand().getType() == Material.STONE_HOE ||
							player.getEquipment().getItemInMainHand().getType() == Material.STONE_PICKAXE ||
							player.getEquipment().getItemInMainHand().getType() == Material.STONE_SHOVEL ||
							player.getEquipment().getItemInMainHand().getType() == Material.WOODEN_SWORD ||
							player.getEquipment().getItemInMainHand().getType() == Material.WOODEN_AXE ||
							player.getEquipment().getItemInMainHand().getType() == Material.WOODEN_HOE ||
							player.getEquipment().getItemInMainHand().getType() == Material.WOODEN_PICKAXE ||
							player.getEquipment().getItemInMainHand().getType() == Material.WOODEN_SHOVEL) {
						event.setCancelled(true);
					}
				}
			}
			if(race.equalsIgnoreCase("Vampire")) { //No Wooden Weapons
				if(player.getEquipment().getItemInMainHand().getType() == Material.WOODEN_SWORD ||
						player.getEquipment().getItemInMainHand().getType() == Material.WOODEN_AXE ||
						player.getEquipment().getItemInMainHand().getType() == Material.WOODEN_HOE ||
						player.getEquipment().getItemInMainHand().getType() == Material.WOODEN_PICKAXE ||
						player.getEquipment().getItemInMainHand().getType() == Material.WOODEN_SHOVEL) {
					event.setCancelled(true);
					player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.vampire-wooden-object").replaceAll("%prefix%", this.getConfig().getString("Messages.prefix"))));
				}
			}
			if(race.equalsIgnoreCase("Necromancer")) { //Netherite Weapons Only
				if(player.getEquipment().getItemInMainHand().getType() == Material.DIAMOND_SWORD ||
						player.getEquipment().getItemInMainHand().getType() == Material.DIAMOND_AXE ||
						player.getEquipment().getItemInMainHand().getType() == Material.DIAMOND_HOE ||
						player.getEquipment().getItemInMainHand().getType() == Material.DIAMOND_PICKAXE ||
						player.getEquipment().getItemInMainHand().getType() == Material.DIAMOND_SHOVEL ||
						player.getEquipment().getItemInMainHand().getType() == Material.IRON_SWORD ||
						player.getEquipment().getItemInMainHand().getType() == Material.IRON_AXE ||
						player.getEquipment().getItemInMainHand().getType() == Material.IRON_HOE ||
						player.getEquipment().getItemInMainHand().getType() == Material.IRON_PICKAXE ||
						player.getEquipment().getItemInMainHand().getType() == Material.IRON_SHOVEL ||
						player.getEquipment().getItemInMainHand().getType() == Material.GOLDEN_SWORD ||
						player.getEquipment().getItemInMainHand().getType() == Material.GOLDEN_AXE ||
						player.getEquipment().getItemInMainHand().getType() == Material.GOLDEN_HOE ||
						player.getEquipment().getItemInMainHand().getType() == Material.GOLDEN_PICKAXE ||
						player.getEquipment().getItemInMainHand().getType() == Material.GOLDEN_SHOVEL ||
						player.getEquipment().getItemInMainHand().getType() == Material.STONE_SWORD ||
						player.getEquipment().getItemInMainHand().getType() == Material.STONE_AXE ||
						player.getEquipment().getItemInMainHand().getType() == Material.STONE_HOE ||
						player.getEquipment().getItemInMainHand().getType() == Material.STONE_PICKAXE ||
						player.getEquipment().getItemInMainHand().getType() == Material.STONE_SHOVEL ||
						player.getEquipment().getItemInMainHand().getType() == Material.WOODEN_SWORD ||
						player.getEquipment().getItemInMainHand().getType() == Material.WOODEN_AXE ||
						player.getEquipment().getItemInMainHand().getType() == Material.WOODEN_HOE ||
						player.getEquipment().getItemInMainHand().getType() == Material.WOODEN_PICKAXE ||
						player.getEquipment().getItemInMainHand().getType() == Material.WOODEN_SHOVEL) {
					event.setCancelled(true);
					player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.necromancer-netherite-object").replaceAll("%prefix%", this.getConfig().getString("Messages.prefix"))));
				}
			}
			if(race.equalsIgnoreCase("WitchHunter")) { //Wooden Weapons only
				if(player.getEquipment().getItemInMainHand().getType() == Material.NETHERITE_SWORD ||
						player.getEquipment().getItemInMainHand().getType() == Material.NETHERITE_AXE ||
						player.getEquipment().getItemInMainHand().getType() == Material.NETHERITE_HOE ||
						player.getEquipment().getItemInMainHand().getType() == Material.NETHERITE_PICKAXE ||
						player.getEquipment().getItemInMainHand().getType() == Material.NETHERITE_SHOVEL ||
						player.getEquipment().getItemInMainHand().getType() == Material.DIAMOND_SWORD ||
						player.getEquipment().getItemInMainHand().getType() == Material.DIAMOND_AXE ||
						player.getEquipment().getItemInMainHand().getType() == Material.DIAMOND_HOE ||
						player.getEquipment().getItemInMainHand().getType() == Material.DIAMOND_PICKAXE ||
						player.getEquipment().getItemInMainHand().getType() == Material.DIAMOND_SHOVEL ||
						player.getEquipment().getItemInMainHand().getType() == Material.IRON_SWORD ||
						player.getEquipment().getItemInMainHand().getType() == Material.IRON_AXE ||
						player.getEquipment().getItemInMainHand().getType() == Material.IRON_HOE ||
						player.getEquipment().getItemInMainHand().getType() == Material.IRON_PICKAXE ||
						player.getEquipment().getItemInMainHand().getType() == Material.IRON_SHOVEL ||
						player.getEquipment().getItemInMainHand().getType() == Material.GOLDEN_SWORD ||
						player.getEquipment().getItemInMainHand().getType() == Material.GOLDEN_AXE ||
						player.getEquipment().getItemInMainHand().getType() == Material.GOLDEN_HOE ||
						player.getEquipment().getItemInMainHand().getType() == Material.GOLDEN_PICKAXE ||
						player.getEquipment().getItemInMainHand().getType() == Material.GOLDEN_SHOVEL ||
						player.getEquipment().getItemInMainHand().getType() == Material.STONE_SWORD ||
						player.getEquipment().getItemInMainHand().getType() == Material.STONE_AXE ||
						player.getEquipment().getItemInMainHand().getType() == Material.STONE_HOE ||
						player.getEquipment().getItemInMainHand().getType() == Material.STONE_PICKAXE ||
						player.getEquipment().getItemInMainHand().getType() == Material.STONE_SHOVEL) {
					event.setCancelled(true);
					player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.witchhunter-melee-object").replaceAll("%prefix%", this.getConfig().getString("Messages.prefix"))));
				}
			}
		}
		
		//Victim Damage Modifiers
		if(event.getEntity() instanceof Player) {
			Player victim = (Player) event.getEntity();
			String race = PlayerData.get(victim.getUniqueId())[0];
			//General Damage Modifier
			if(race.equalsIgnoreCase("Vampire")) {
				event.setDamage(event.getDamage()*0.80);
			}
			if(race.equalsIgnoreCase("Ghoul")) {
				event.setDamage(event.getDamage()*0.66);
			}
			if(race.equalsIgnoreCase("Werewolf") && !isDay(victim)) {
				event.setDamage(event.getDamage()*0.25);
			}
			//Weapon Type Damage Modifier
			if(event.getDamager() instanceof LivingEntity) {
				LivingEntity damager = (LivingEntity) event.getDamager();
				if(damager.getEquipment().getItemInMainHand()!=null) {
					Material Weapon = damager.getEquipment().getItemInMainHand().getType();
					if(race.equalsIgnoreCase("Vampire")) {
						if(Weapon == Material.WOODEN_SWORD || Weapon == Material.WOODEN_AXE || Weapon == Material.WOODEN_HOE ||
								Weapon == Material.WOODEN_PICKAXE || Weapon == Material.WOODEN_SHOVEL) {
							event.setDamage(event.getDamage()*4);
						}
					}
					if(race.equalsIgnoreCase("Necromancer")) {
						if(Weapon == Material.NETHERITE_SWORD || Weapon == Material.NETHERITE_AXE || Weapon == Material.NETHERITE_HOE ||
								Weapon == Material.NETHERITE_PICKAXE || Weapon == Material.NETHERITE_SHOVEL) {
							event.setDamage(event.getDamage()*4);
						}
					}
				}
			}
		}
		
		//Supernatural Infectivity
		if((event.getEntity() instanceof Player)) {
			Player victim = (Player) event.getEntity();
			String vrace = PlayerData.get(victim.getUniqueId())[0];
			if(event.getDamage() >= victim.getHealth()) {
				if(vrace.equalsIgnoreCase("Necromancer") || vrace.equalsIgnoreCase("WitchHunter") || vrace.equalsIgnoreCase("Priest") || vrace.equalsIgnoreCase("Human")) {
					if(event.getDamager() instanceof Player) {
						Player damager = (Player) event.getDamager();
						String drace = PlayerData.get(damager.getUniqueId())[0];
						if(drace.equals("Vampire") || drace.equals("Werewolf") || drace.equals("Ghoul") || drace.equals("Demon")) {
							if(Math.random() < this.getConfig().getDouble("PercentInfectionChances.Death-By-Supernatural")/100) {
								victim.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.supernatural-infection-infected").replaceAll("%prefix%", this.getConfig().getString("Messages.prefix")).replaceAll("%class%", drace)));
								setRace(victim, drace, true);
								damager.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.supernatural-infection-infector").replaceAll("%prefix%", this.getConfig().getString("Messages.prefix")).replaceAll("%victim%", victim.getName()).replaceAll("%class%", drace)));
							   	return;
							}
						}
					} else {
						if(isMonster(event.getDamager().getType())) {
							if(Math.random() < this.getConfig().getDouble("PercentInfectionChances.Death-By-Monster")/100) {
								victim.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.supernatural-infection-infected").replaceAll("%prefix%", this.getConfig().getString("Messages.prefix")).replaceAll("%class%", "Ghoul")));
								setRace(victim, "Ghoul", true);
							   	return;
							}
						}
						if(event.getDamager().getType().equals(EntityType.WOLF)) {
							if(Math.random() < this.getConfig().getDouble("PercentInfectionChances.Death-By-Wolf")/100) {
								victim.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.supernatural-infection-infected").replaceAll("%prefix%", this.getConfig().getString("Messages.prefix")).replaceAll("%class%", "Werewolf")));
								setRace(victim, "Werewolf", true);
							   	return;
							}
						}
						if(event.getDamager().getType().equals(EntityType.BAT)) {
							if(Math.random() < this.getConfig().getDouble("PercentInfectionChances.Death-By-Bat")/100) {
								victim.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.supernatural-infection-infected").replaceAll("%prefix%", this.getConfig().getString("Messages.prefix")).replaceAll("%class%", "Vampire")));
								setRace(victim, "Vampire", true);
							   	return;
							}
						}
					}
				}
			}
		}
	}

	@EventHandler	
	public void DamageCalculation(EntityDamageEvent event) { 
		//Victim Damage Modifiers
		if(event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();
			String race = PlayerData.get(player.getUniqueId())[0];
			//General Damage Modifier
			if(race.equalsIgnoreCase("Werewolf") && !isDay(player)) {
				event.setDamage(event.getDamage()/4);
			}
			if(race.equalsIgnoreCase("Ghoul")) {
				event.setDamage(event.getDamage()/2);
			}
			//Fall Damage Modifier
			if(event.getCause() == DamageCause.FALL) {
				if(race.equalsIgnoreCase("Vampire") || race.equalsIgnoreCase("Ghoul") || race.equalsIgnoreCase("Demon")) event.setCancelled(true);
				if(race.equalsIgnoreCase("Werewolf") && !isDay(player) || race.equalsIgnoreCase("WitchHunter")) event.setDamage(event.getDamage()/3);
			}
			//Fire Damage Modifier
			if(event.getCause() == DamageCause.LAVA || event.getCause() == DamageCause.FIRE || event.getCause() == DamageCause.FIRE_TICK || event.getCause() == DamageCause.HOT_FLOOR) {
				if(race.equalsIgnoreCase("Demon")) {
					player.setFireTicks(0);
					event.setCancelled(true);
				}
			}
			//Explosion Damage Modifier
			if(event.getCause() == DamageCause.BLOCK_EXPLOSION || event.getCause() == DamageCause.ENTITY_EXPLOSION) {
				if(race.equalsIgnoreCase("Demon")) event.setCancelled(true);
			}
		}
		//Supernatural Infectivity
		if(!(event.getEntity() instanceof Player)) {
			return;
		}
		Player victim = (Player) event.getEntity();
		String vrace = PlayerData.get(victim.getUniqueId())[0];
		if(event.getDamage() < victim.getHealth()) {
			return;
		}
		if(vrace.equalsIgnoreCase("Necromancer") || vrace.equalsIgnoreCase("WitchHunter") || vrace.equalsIgnoreCase("Priest") || vrace.equalsIgnoreCase("Human")) {
			if(event.getCause() == DamageCause.LAVA) {
				if(Math.random() < this.getConfig().getDouble("PercentInfectionChances.Death-By-Lava")/100) {
					victim.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.supernatural-infection-infected").replaceAll("%prefix%", this.getConfig().getString("Messages.prefix")).replaceAll("%class%", "Demon")));
					setRace(victim, "Demon", true);
				   	return;
				}
			}
			if(inNether(victim)) {
				if(Math.random() < this.getConfig().getDouble("PercentInfectionChances.Death-In-Nether")/100) {
					victim.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.supernatural-infection-infected").replaceAll("%prefix%", this.getConfig().getString("Messages.prefix")).replaceAll("%class%", "Demon")));
					setRace(victim, "Demon", true);
				   	return;
				}
			}
		}
	}

	@EventHandler	
	public void MonsterTruce(EntityTargetEvent event) { 
		if(event.getTarget() instanceof Player) {
			Player player = (Player) event.getTarget();
			String race = PlayerData.get(player.getUniqueId())[0];
			EntityType entity = event.getEntityType();
			if(race.equalsIgnoreCase("Vampire") && (entity == EntityType.SKELETON || entity == EntityType.ZOMBIE || entity == EntityType.SPIDER || entity == EntityType.CREEPER || entity == EntityType.HUSK || entity == EntityType.STRAY)) {
				if(!event.getReason().equals(TargetReason.TARGET_ATTACKED_ENTITY)) {
					event.setCancelled(true);
					event.setTarget(null);
				}
			}
			if(race.equalsIgnoreCase("Necromancer") && isUndead(entity)) {
				event.setCancelled(true);
				event.setTarget(null);
			}
			if(race.equalsIgnoreCase("Ghoul") && isUndead(entity)) {
				if(!event.getReason().equals(TargetReason.TARGET_ATTACKED_ENTITY)) {
					event.setCancelled(true);
					event.setTarget(null);
				}
			}
			if(race.equalsIgnoreCase("Demon") && (entity == EntityType.PIGLIN || entity == EntityType.ZOMBIFIED_PIGLIN || entity == EntityType.HOGLIN || entity == EntityType.WITHER_SKELETON || entity == EntityType.BLAZE || entity == EntityType.GHAST || entity == EntityType.MAGMA_CUBE || entity == EntityType.ZOGLIN)) {
				if(!event.getReason().equals(TargetReason.TARGET_ATTACKED_ENTITY)) {
					event.setCancelled(true);
					event.setTarget(null);
				}
			}
		}
	}

	@EventHandler
	public void FoodInfection(PlayerItemConsumeEvent e) {
		Player victim = (Player) e.getPlayer();
		String vrace = PlayerData.get(victim.getUniqueId())[0];
		if(e.getItem().getType() == Material.ROTTEN_FLESH) {
			if(vrace.equalsIgnoreCase("Necromancer") || vrace.equalsIgnoreCase("WitchHunter") || vrace.equalsIgnoreCase("Priest") || vrace.equalsIgnoreCase("Human")) {
				if(Math.random() < this.getConfig().getDouble("PercentInfectionChances.Eating-Rotten-Flesh")/100) {
					victim.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.supernatural-infection-infected").replaceAll("%prefix%", this.getConfig().getString("Messages.prefix")).replaceAll("%class%", "Ghoul")));
					setRace(victim, "Ghoul", true);
				   	return;
				}
			}
		}
	}

	@EventHandler
	public void noArmour(ArmorEquipEvent e) {
		Player p = e.getPlayer();
		String race = PlayerData.get(p.getUniqueId())[0];
		if(race.equalsIgnoreCase("Demon") || race.equalsIgnoreCase("Werewolf") && !isDay(p) || race.equalsIgnoreCase("Priest")) {
			if(e.getNewArmorPiece() != null && e.getNewArmorPiece().getType() != Material.AIR) {
				e.setCancelled(true);
			}
		}
		if(race.equalsIgnoreCase("Necromancer")) {
			if(e.getNewArmorPiece() != null && e.getNewArmorPiece().getType() != Material.AIR) {
				Material m = e.getNewArmorPiece().getType();
				if(!(m.equals(Material.NETHERITE_BOOTS) || m.equals(Material.NETHERITE_LEGGINGS)
						|| m.equals(Material.NETHERITE_CHESTPLATE) || m.equals(Material.NETHERITE_HELMET))) {
					e.setCancelled(true);
				}
			}
		}
		if(race.equalsIgnoreCase("WitchHunter")) {
			if(e.getNewArmorPiece() != null && e.getNewArmorPiece().getType() != Material.AIR) {
				Material m = e.getNewArmorPiece().getType();
				if(!(m.equals(Material.LEATHER_BOOTS) || m.equals(Material.LEATHER_LEGGINGS)
						|| m.equals(Material.LEATHER_CHESTPLATE) || m.equals(Material.LEATHER_HELMET))) {
					e.setCancelled(true);
				}
			}
		}
		if(race.equalsIgnoreCase("Angel")) {
			if(e.getNewArmorPiece() != null && e.getNewArmorPiece().getType() != Material.AIR) {
				Material m = e.getNewArmorPiece().getType();
				if((m.equals(Material.NETHERITE_BOOTS) || m.equals(Material.NETHERITE_LEGGINGS)
						|| m.equals(Material.NETHERITE_CHESTPLATE) || m.equals(Material.NETHERITE_HELMET))) {
					e.setCancelled(true);
				}
			}
		}
	}

	public void ejectArmour(Player p) {
		ItemStack[] armour = p.getInventory().getArmorContents();
		String race = PlayerData.get(p.getUniqueId())[0];
		for(int i=0; i<armour.length; i++) {
			if(armour[i]!=null) {
				if(race.equalsIgnoreCase("Demon") || race.equalsIgnoreCase("Werewolf") && !isDay(p) || race.equalsIgnoreCase("Priest")) {
					addItemSafely(p, armour[i]);
					armour[i] = null;
				}
				if(race.equalsIgnoreCase("Necromancer")) {
					if(armour[i] != null && armour[i].getType() != Material.AIR) {
						Material m = armour[i].getType();
						if(!(m.equals(Material.NETHERITE_BOOTS) || m.equals(Material.NETHERITE_LEGGINGS)
								|| m.equals(Material.NETHERITE_CHESTPLATE) || m.equals(Material.NETHERITE_HELMET))) {
							addItemSafely(p, armour[i]);
							armour[i] = null;
						}
					}
				}
				if(race.equalsIgnoreCase("WitchHunter")) {
					if(armour[i] != null && armour[i].getType() != Material.AIR) {
						Material m = armour[i].getType();
						if(!(m.equals(Material.LEATHER_BOOTS) || m.equals(Material.LEATHER_LEGGINGS)
								|| m.equals(Material.LEATHER_CHESTPLATE) || m.equals(Material.LEATHER_HELMET))) {
							addItemSafely(p, armour[i]);
							armour[i] = null;
						}
					}
				}
				if(race.equalsIgnoreCase("Angel")) {
					if(armour[i] != null && armour[i].getType() != Material.AIR) {
						Material m = armour[i].getType();
						if((m.equals(Material.NETHERITE_BOOTS) || m.equals(Material.NETHERITE_LEGGINGS)
								|| m.equals(Material.NETHERITE_CHESTPLATE) || m.equals(Material.NETHERITE_HELMET))) {
							addItemSafely(p, armour[i]);
							armour[i] = null;
						}
					}
				}
			}
		}
		p.getInventory().setArmorContents(armour);
	}
	
	@EventHandler
	public void noFoodRegen(EntityRegainHealthEvent event) {
		if(event.getRegainReason().equals(RegainReason.EATING)) {
			Entity entity = event.getEntity();
			if(entity instanceof Player) {
				Player player = (Player) entity;
				if(getRace(player).equals("Vampire")) {
					event.setCancelled(true);
				}
			}
		}
	}
	
	public int getLightLevel(Player p) {
		Location l = p.getLocation();
		while(l.getBlock() == null) {
			l.add(0, -1, 0);
			if(l.getY() == 0) {
				return -1;
			}
		}
		return l.getBlock().getLightLevel();
	}
	
	public void inSunlight(Player player, int tick) {
		if(isDay(player)) {
			if(underSky(player)) {
				//Event Triggered
				String race = PlayerData.get(player.getUniqueId())[0];
				if(player.getGameMode() != GameMode.CREATIVE && player.getGameMode() != GameMode.SPECTATOR) {
					if(race.contains("Vampire")) {
						if(player.getFireTicks()<20) {
							if(player.getInventory().getHelmet() == null || player.getInventory().getHelmet().getType() != Material.GOLDEN_HELMET || getMagic(player) < (100)) {
								player.setFireTicks(20);
								return;
							} else {
								if(tick % 60 == 0) {
									changeMagic(player, -100);
								}
							}
						}
					} else if(race.contains("Angel")) {
						if(tick % 20 == 0) {
							changeMagic(player, 1);
						}
					}
				}
			}
		}
	}
 
	public Boolean rainContact(Player p) {
		@SuppressWarnings("deprecation")
		Biome biome = p.getWorld().getBiome(p.getLocation().getBlockX(), p.getLocation().getBlockZ());
		if((underSky(p) && p.getWorld().hasStorm()) && !(biome.equals(Biome.DESERT) || biome.equals(Biome.DESERT_HILLS) || biome.equals(Biome.DESERT_LAKES)
				|| biome.equals(Biome.BADLANDS) || biome.equals(Biome.BADLANDS_PLATEAU) || biome.equals(Biome.ERODED_BADLANDS) || biome.equals(Biome.MODIFIED_BADLANDS_PLATEAU)
				|| biome.equals(Biome.MODIFIED_WOODED_BADLANDS_PLATEAU) || biome.equals(Biome.WOODED_BADLANDS_PLATEAU)
				|| biome.equals(Biome.SAVANNA) || biome.equals(Biome.SAVANNA_PLATEAU) || biome.equals(Biome.SHATTERED_SAVANNA) || biome.equals(Biome.SHATTERED_SAVANNA_PLATEAU))) {
			return true;
		} else {
			return false;
		}
	}
	
	public void waterContact(Player player) {
		if(rainContact(player) || (player.getLocation().getBlock().getType() == Material.WATER)) {
			if(player.getGameMode() != GameMode.CREATIVE && player.getGameMode() != GameMode.SPECTATOR) {
				String race = PlayerData.get(player.getUniqueId())[0];
				if(race.contains("Ghoul")) {
					player.damage(5);
				}
			}
		}
	}
	
	@EventHandler
	public void onConsume(PlayerItemConsumeEvent event) {
		Player player = event.getPlayer();
		String race = PlayerData.get(player.getUniqueId())[0];
		if(event.getItem().getType() != Material.POTION) {
			if(race.equalsIgnoreCase("Vampire")) {
				event.setCancelled(true);
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.vampire-no-eat").replaceAll("%prefix%", this.getConfig().getString("Messages.prefix"))));
			}
			if(race.equalsIgnoreCase("Werewolf")) {
				//Increase magic based on meats nutritional value
				if(MeatNutrition.get(event.getItem().getType()) != null) {
					String[] playerdata = PlayerData.get(player.getUniqueId());
					double magic = Double.parseDouble(playerdata[1]) + (MeatNutrition.get(event.getItem().getType())*5);
					if(magic>10000) {
						magic=10000;
					}
					playerdata[1] = ("" + magic);
					PlayerData.put(player.getUniqueId(), playerdata);
				}
			}
		}
	}
		
	@EventHandler 
	public void MaxEat(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		String race = PlayerData.get(player.getUniqueId())[0];
		if(race.equalsIgnoreCase("Werewolf") && !isDay(player)) {
			if(event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
				if(MeatNutrition.get(event.getItem().getType()) == null) {
					return;
				}
				if(player.getFoodLevel() != 20) {
					return;
				}
				player.setFoodLevel(19);
		    	this.getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
		            public void run() {
		        		player.setFoodLevel(20);
		            }
		        }, 1L);
			}
		}
	}

    @SuppressWarnings("deprecation")
	@EventHandler
	public void onWolfSpawn(CreatureSpawnEvent event) {
		Entity entity = event.getEntity();
		if (entity.getType() == EntityType.WOLF) {
			Wolf wolf = (Wolf) entity;
			for (Entity nearbyEntity : wolf.getNearbyEntities(32, 32, 32)) {
				if (nearbyEntity instanceof Player) {
					Player target = (Player)nearbyEntity;
					if(!PlayerData.get(target.getUniqueId())[0].equalsIgnoreCase("Werewolf")) {
						if(target.getItemInHand() == null || !target.getItemInHand().getType().equals(Material.BONE)) {
							wolf.setAngry(true);
							wolf.setTarget(target);
						}
					}
				}
			}
		}
	}

	@SuppressWarnings("deprecation")
	public void HostileAnimals() {
		for (World world : this.getServer().getWorlds()) {
			for (Entity entity : this.getServer().getWorld(world.getName()).getEntities()) {
				if (entity instanceof LivingEntity) {
					LivingEntity livingEntity = (LivingEntity) entity;
					if (livingEntity instanceof Wolf) {
						Wolf wolf = (Wolf) livingEntity;
						for (Entity nearbyEntity : wolf.getNearbyEntities(16, 16, 16)) {
							if (nearbyEntity instanceof Player) {
								Player target = (Player) nearbyEntity;
								String targetrace = PlayerData.get(target.getUniqueId())[0];
								if(!targetrace.equalsIgnoreCase("Werewolf")) {
									//if not holding bone
									if(target.getItemInHand() == null || !target.getItemInHand().getType().equals(Material.BONE)) {
										//if wolf untamed
										if(wolf.getOwner() == null) {
											wolf.setAngry(true);
											wolf.setTarget(target);
										}
									}
								}
							}
						}
					}
					if (livingEntity instanceof IronGolem) {
						IronGolem irongolem = (IronGolem) livingEntity;
						for (Entity nearbyEntity : irongolem.getNearbyEntities(16, 16, 16)) {
							if (nearbyEntity instanceof Player) {
								Player target = (Player) nearbyEntity;
								if(isSupernatural(target)) {
									irongolem.setTarget(target);
								}
							}
						}
					}
				}
			}
		}
	}

	public void CollectBounty(Player target, Player collector) {
		if(PlayerBounties.containsKey(target.getUniqueId())) {
			double bounty = PlayerBounties.get(target.getUniqueId());
			PlayerBounties.remove(target.getUniqueId());
			eco.depositPlayer(collector, bounty);
			collector.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.redeem-bounty").replaceAll("%prefix%", this.getConfig().getString("Messages.prefix")).replaceAll("%bounty%", ("" + bounty)).replaceAll("%target%", target.getName())));
			if(getRace(collector).equals("WitchHunter")) changeMagic(collector, bounty);
		}
	}

	/**
	 * Custom Items / Active Abilities
	 */
	
	public void monsterList() {
		monsters.add(EntityType.BAT);
		monsters.add(EntityType.CAVE_SPIDER);
		monsters.add(EntityType.SPIDER);
		monsters.add(EntityType.ENDERMAN);
		monsters.add(EntityType.BLAZE);
		monsters.add(EntityType.CREEPER);
		monsters.add(EntityType.DROWNED);
		monsters.add(EntityType.ELDER_GUARDIAN);
		monsters.add(EntityType.ENDERMITE);
		monsters.add(EntityType.EVOKER);
		monsters.add(EntityType.GHAST);
		monsters.add(EntityType.GUARDIAN);
		monsters.add(EntityType.HUSK);
		monsters.add(EntityType.MAGMA_CUBE);
		monsters.add(EntityType.PHANTOM);
		monsters.add(EntityType.PILLAGER);
		monsters.add(EntityType.PIGLIN);
		monsters.add(EntityType.ZOMBIFIED_PIGLIN);
		monsters.add(EntityType.HOGLIN);
		monsters.add(EntityType.RAVAGER);
		monsters.add(EntityType.SHULKER);
		monsters.add(EntityType.SILVERFISH);
		monsters.add(EntityType.SKELETON);
		monsters.add(EntityType.SLIME);
		monsters.add(EntityType.STRAY);
		monsters.add(EntityType.VEX);
		monsters.add(EntityType.VINDICATOR);
		monsters.add(EntityType.WITCH);
		monsters.add(EntityType.WITHER_SKELETON);
		monsters.add(EntityType.ZOGLIN);
		monsters.add(EntityType.ZOMBIE);
		monsters.add(EntityType.ZOMBIE_VILLAGER);
		undead.add(EntityType.SKELETON);
		undead.add(EntityType.STRAY);
		undead.add(EntityType.WITHER_SKELETON);
		undead.add(EntityType.ZOMBIE);
		undead.add(EntityType.DROWNED);
		undead.add(EntityType.HUSK);
		undead.add(EntityType.ZOMBIFIED_PIGLIN);
		undead.add(EntityType.ZOMBIE_VILLAGER);
		//undead.add(EntityType.PHANTOM); [in Flying-Entity class not Monsterclass]
		//undead.add(EntityType.SKELETON_HORSE); [not in Monster class]
		//undead.add(EntityType.ZOMBIE_HORSE); [not in Monster class]
		undead.add(EntityType.ZOGLIN);
	}
	
	public void CustomItems() {
		ItemStack item = new ItemStack(Material.AIR);
    	List<String> lore = new ArrayList<String>();
		//0
		item = new ItemStack(Material.POTION);
		PotionMeta pm = (PotionMeta) item.getItemMeta();
		pm.setColor(Color.fromRGB(77, 1, 1));
		//meta.addCustomEffect(new PotionEffect(PotionEffectType.LUCK, 0, 0), false);
		pm.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
		pm.setDisplayName(ChatColor.DARK_RED + "Bloodrose Potion");
		lore = new ArrayList<String>();
		lore.add(ChatColor.RED + "Transforms the person who drinks");
		lore.add(ChatColor.RED + "this potion into a Vampire.");
		pm.setLore(lore);
		item.setItemMeta(pm);
		CustomItems.add(item);
		//1
		ItemStack writtenBook = new ItemStack(Material.WRITTEN_BOOK);
		BookMeta bookMeta = (BookMeta) writtenBook.getItemMeta();
		bookMeta.setTitle("Guide To Vamparism");
		bookMeta.setAuthor("The Vampire Lord");
		List<String> pages = new ArrayList<String>();
		pages.add("Gain Magika By:\n" + 
				"Killing Creatures\n" + 
				"Killing Players\n" + 
				"\n" + 
				"Passive Abilities:\n" + 
				"Water Breathing\n" + 
				"Health Regeneration\n" + 
				"Rage\n" + 
				"Nightvision\n" + 
				"Combat Resistance\n" + 
				"Fall Damage Immunity\n" + 
				"Truce with Monsters\n" + 
				"Killing mobs replenishes hunger\n");
		pages.add("Active Abilities:\n" + 
				"Set Teleport Location\n" + 
				"Teleport\n" + 
				"High Jump\n" + 
				"Create Bloodrose Potion\n" + 
				"\n" + 
				"Weaknesses:\n" + 
				"Catch Fire in Sunlight\n" + 
				"Increased Damage to Wooden Objects\n" + 
				"Cant use Wooden Object\n" + 
				"Cant Eat Regular Food\n");
		bookMeta.setPages(pages);
		writtenBook.setItemMeta(bookMeta);
		CustomItems.add(writtenBook);
		//2
		item = new ItemStack(Material.POTION);
		pm = (PotionMeta) item.getItemMeta();
		pm.setColor(Color.fromRGB(31, 187, 255));
		//meta.addCustomEffect(new PotionEffect(PotionEffectType.LUCK, 0, 0), false);
		pm.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
		pm.setDisplayName(ChatColor.BLUE + "Moonflower Potion");
		lore = new ArrayList<String>();
		lore.add(ChatColor.AQUA + "Transforms the person who drinks");
		lore.add(ChatColor.AQUA + "this potion into a Werewolf.");
		pm.setLore(lore);
		item.setItemMeta(pm);
		CustomItems.add(item);
		//3
		writtenBook = new ItemStack(Material.WRITTEN_BOOK);
		bookMeta = (BookMeta) writtenBook.getItemMeta();
		bookMeta.setTitle("Guide To being a Werewolf");
		bookMeta.setAuthor("The Werewolf Leader");
		pages = new ArrayList<String>();
		pages.add("Gain Magika By:\n" + 
				"Killing Creatures\n" + 
				"Killing Players\n" +  
				"Eating Meat\n" +
				"\n" + 
				"Passive Abilities:\n" +  
				"Health Regeneration\n" + 
				"4x Attack\n" + 
				"Nightvision\n" + 
				"1/4 Damage\n" + 
				"1/3 Fall Damage\n" + 
				"Eat meat when full\n" + 
				"2x Speed\n" + 
				"Truce with Wolves\n");
		pages.add("Active Abilities:\n" + 
				"Summon Wolf\n" + 
				"Dash\n" + 
				"Create Moonflower Potion\n" + 
				"\n" + 
				"Weaknesses:\n" + 
				"Abilities only work at night:\n" + 
				"You cant use Weapons or Armour during the night\n");
		bookMeta.setPages(pages);
		writtenBook.setItemMeta(bookMeta);
		CustomItems.add(writtenBook);
		//4
				item = new ItemStack(Material.POTION);
				pm = (PotionMeta) item.getItemMeta();
				pm.setColor(Color.BLACK);
				//meta.addCustomEffect(new PotionEffect(PotionEffectType.LUCK, 0, 0), false);
				pm.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
				pm.setDisplayName(ChatColor.BLUE + "Ghoulish Potion");
				lore = new ArrayList<String>();
				lore.add(ChatColor.AQUA + "Transforms the person who drinks");
				lore.add(ChatColor.AQUA + "this potion into a Ghoul.");
				pm.setLore(lore);
				item.setItemMeta(pm);
				CustomItems.add(item);
				//5
				writtenBook = new ItemStack(Material.WRITTEN_BOOK);
				bookMeta = (BookMeta) writtenBook.getItemMeta();
				bookMeta.setTitle("Guide To being a Ghoul");
				bookMeta.setAuthor("The Ghoul Prince");
				pages = new ArrayList<String>();
				pages.add("Gain Magika By:\n" + 
						"Killing Players\n" + 
						"Killing Creatures\n" +  
						"\n" + 
						"Passive Abilities:\n" +  
						"Health Regeneration\n" + 
						"2x Armour\n" + 
						"2/3 Combat Damage\n" + 
						"Nightvision\n" + 
						"Fall Damage Immunity\n" + 
						"Truce with the Undead\n");
				pages.add("Active Abilities:\n" + 
						"Summon Undead\n" + 
						"Unholy Bond\n" + 
						"Create Ghoulish Potion\n" + 
						"\n" + 
						"Weaknesses:\n" + 
						"Damaged by water & rain\n" + 
						"Cannot use weapons\n");
				bookMeta.setPages(pages);
				writtenBook.setItemMeta(bookMeta);
				CustomItems.add(writtenBook);
				//6
				item = new ItemStack(Material.POTION);
				pm = (PotionMeta) item.getItemMeta();
				pm.setColor(Color.ORANGE);
				//meta.addCustomEffect(new PotionEffect(PotionEffectType.LUCK, 0, 0), false);
				pm.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
				pm.setDisplayName(ChatColor.BLUE + "Hellish Potion");
				lore = new ArrayList<String>();
				lore.add(ChatColor.AQUA + "Transforms the person who drinks");
				lore.add(ChatColor.AQUA + "this potion into a Demon.");
				pm.setLore(lore);
				item.setItemMeta(pm);
				CustomItems.add(item);
				//7
				writtenBook = new ItemStack(Material.WRITTEN_BOOK);
				bookMeta = (BookMeta) writtenBook.getItemMeta();
				bookMeta.setTitle("Guide To being a Demon");
				bookMeta.setAuthor("The Demon King");
				pages = new ArrayList<String>();
				pages.add("Gain Magika By:\n" + 
						"Killing Players\n" + 
						"Killing Creatures\n" +  
						"Standing in Lava\n" +  
						"\n" + 
						"Passive Abilities:\n" +  
						"Immune to Fire Damage\n" + 
						"Heal while in Lava\n" + 
						"Immune to Fall Damage\n" + 
						"Nightvision\n" + 
						"Truce with nether mobs\n");
				pages.add("Active Abilities:\n" + 
						"Fireball\n" + 
						"Explosion\n" + 
						"Snare\n" + 
						"Create Hellish Potion\n" + 
						"\n" + 
						"Weaknesses:\n" + 
						"Cannot Wear Armour\n" + 
						"Lose Magika when NOT in Lava and the Nether\n");
				bookMeta.setPages(pages);
				writtenBook.setItemMeta(bookMeta);
				CustomItems.add(writtenBook);
				//8
				item = new ItemStack(Material.POTION);
				pm = (PotionMeta) item.getItemMeta();
				pm.setColor(Color.PURPLE);
				//meta.addCustomEffect(new PotionEffect(PotionEffectType.LUCK, 0, 0), false);
				pm.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
				pm.setDisplayName(ChatColor.BLUE + "Supernatural Cure");
				lore = new ArrayList<String>();
				lore.add(ChatColor.AQUA + "Transforms the person who drinks");
				lore.add(ChatColor.AQUA + "this potion back into a Human.");
				pm.setLore(lore);
				item.setItemMeta(pm);
				CustomItems.add(item);
				//9
				item = new ItemStack(Material.BOOK);
				ItemMeta meta = item.getItemMeta();
				meta.addEnchant(Enchantment.LURE, 0, false);
				meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
				meta.setDisplayName(ChatColor.BLUE + "Holy Book");
				lore = new ArrayList<String>();
				lore.add(ChatColor.AQUA + "Transforms the person who reads");
				lore.add(ChatColor.AQUA + "this book into a Priest.");
				meta.setLore(lore);
				item.setItemMeta(meta);
				CustomItems.add(item);
				//10
				writtenBook = new ItemStack(Material.WRITTEN_BOOK);
				bookMeta = (BookMeta) writtenBook.getItemMeta();
				bookMeta.setTitle("Guide To being a Priest");
				bookMeta.setAuthor("The Pope");
				pages = new ArrayList<String>();
				pages.add("Gain Magika By:\n" + 
						"Donating to the Church\n" + 
						"\n" + 
						"Passive Abilities:\n" +  
						"2.5x Dmg Against Supernaturals\n" + 
						"1/10 Fire Dmg Against Supernaturals\n" + 
						"1/8 Fire Dmg Against Monsters\n");
				pages.add("Active Abilities:\n" + 
						"Banish Supernatural\n" + 
						"Exorcise Supernatural\n" + 
						"Drain Supernatural\n" + 
						"Heal Human\n" + 
						"Guardian Angel\n" + 
						"Create Supernatural Cure\n" + 
						"Create Holy Book\n" + 
						"\n" + 
						"Weaknesses:\n" + 
						"Cannot Wear Armour\n" + 
						"Cannot Attack Animals\n");
				bookMeta.setPages(pages);
				writtenBook.setItemMeta(bookMeta);
				CustomItems.add(writtenBook);
				//11
				item = new ItemStack(Material.BOOK);
				meta = item.getItemMeta();
				meta.addEnchant(Enchantment.LURE, 0, false);
				meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
				meta.setDisplayName(ChatColor.BLUE + "Book Of Death");
				lore = new ArrayList<String>();
				lore.add(ChatColor.AQUA + "Transforms the person who reads");
				lore.add(ChatColor.AQUA + "this book into a Necromancer.");
				meta.setLore(lore);
				item.setItemMeta(meta);
				CustomItems.add(item);
				//12
				writtenBook = new ItemStack(Material.WRITTEN_BOOK);
				bookMeta = (BookMeta) writtenBook.getItemMeta();
				bookMeta.setTitle("Guide To being a Necromancer");
				bookMeta.setAuthor("The God of Death");
				pages = new ArrayList<String>();
				pages.add("Gain Magika By:\n" + 
						"Killing Players\n" + 
						"Donating to Unholy Alyer\n" + 
						"\n" + 
						"Passive Abilities:\n" +
						"Full Truce with Undead\n" + 
						"Increased Damage at Night\n" + 
						"Nightvision\n");
				pages.add("Active Abilities:\n" + 
						"Summon Skeleton - 1000 Magic\n" + 
						"Summon Undead - 5000 Magic\n" + 
						"Heal Nearby Undead in radius - 3500 Magic\n" + 
						"Ressurection Spawn - 4000 Magic\n" + 
						"Create Book of Death - 10000 Magic & 10 Hearts\n" + 
						"\n" + 
						"Weaknesses:\n" + 
						"Netherite Armour Only\n" + 
						"No Netherite Weapons\n");
				bookMeta.setPages(pages);
				writtenBook.setItemMeta(bookMeta);
				CustomItems.add(writtenBook);
				//13
				item = new ItemStack(Material.BOOK);
				meta = item.getItemMeta();
				meta.addEnchant(Enchantment.LURE, 0, false);
				meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
				meta.setDisplayName(ChatColor.BLUE + "Book of the Witch Hunter");
				lore = new ArrayList<String>();
				lore.add(ChatColor.AQUA + "Transforms the person who reads");
				lore.add(ChatColor.AQUA + "this book into a Witch Hunter.");
				meta.setLore(lore);
				item.setItemMeta(meta);
				CustomItems.add(item);
				//14
				writtenBook = new ItemStack(Material.WRITTEN_BOOK);
				bookMeta = (BookMeta) writtenBook.getItemMeta();
				bookMeta.setTitle("Guide To being a Witch Hunter");
				bookMeta.setAuthor("The Witcher Captain");
				pages = new ArrayList<String>();
				pages.add("Gain Magika By:\n" + 
						"Killing Players\n" + 
						"Killing Monsters" + 
						"Killing Players with bounties\n" + 
						"\n" + 
						"Passive Abilities:\n" +
						"1/3 Fall Damage\n" +
						"Active Abilities:\n" + 
						"Triple Arrow - 100 Power\n" + 
						"Grapple Arrow - 500 Power\n" + 
						"Fire Arrow - 500 Power\n");
				pages.add("Power Arrow - 1000 Power\n" + 
						"Volley Arrow - 300 Power\n" + 
						"Create Book of Witcher - 10000 Power & 10 Hearts\n" + 
						"\n" + 
						"Weaknesses:\n" + 
						"Can only wear Leather Armour\n" + 
						"Cant use Melee Weapons\n");
				bookMeta.setPages(pages);
				writtenBook.setItemMeta(bookMeta);
				CustomItems.add(writtenBook);
				//15
				item = new ItemStack(Material.FILLED_MAP);
				meta = item.getItemMeta();
				meta.setDisplayName(ChatColor.GOLD + "Bounty Menu");
				lore = new ArrayList<String>();
				lore.add(ChatColor.YELLOW + "Opens the Bounty Menu");
				meta.setLore(lore);
				item.setItemMeta(meta);
				CustomItems.add(item);
				//16
				item = new ItemStack(Material.POTION);
				pm.setColor(Color.fromRGB(115, 0, 0));
				pm.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
				pm.setDisplayName(ChatColor.DARK_RED + "Blood Vial");
				lore = new ArrayList<String>();
				lore.add(ChatColor.RED + "Gives Vampires 2 Hunger When Consumed");
				pm.setLore(lore);
				item.setItemMeta(pm);
				CustomItems.add(item);
	}
	
	//Vampire
	
	public void ToggleRegen(Player p) {
		if(!Regeneration.containsKey(p)) {
			Regeneration.put(p, false);
		}
		Regeneration.put(p, !Regeneration.get(p));
	}
	
	public void ToggleWater(Player p) {
		if(!WaterBreathing.containsKey(p)) {
			WaterBreathing.put(p, false);
		}
		WaterBreathing.put(p, !WaterBreathing.get(p));
	}

	public void SetTeleportLocation(Player p) {
		Location location = p.getLocation();
		TeleportLocation.put(p.getUniqueId(), location);
		p.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.set-teleport-location").replaceAll("%prefix%", this.getConfig().getString("Messages.prefix"))));
	}

	public void Teleport(Player p) {
		if(getMagic(p) < this.getConfig().getInt("SpellCosts.Teleport")) {
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.insufficient-magic").replaceAll("%prefix%", this.getConfig().getString("Messages.prefix"))));
			return;
		}
		if(TeleportLocation.containsKey(p.getUniqueId())) {
			changeMagic(p, -this.getConfig().getInt("SpellCosts.Teleport"));
			Location location = TeleportLocation.get(p.getUniqueId());
			p.teleport(location);
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.teleport-success").replaceAll("%prefix%", this.getConfig().getString("Messages.prefix"))));
		} else {
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.teleport-failure").replaceAll("%prefix%", this.getConfig().getString("Messages.prefix"))));
		}
	}

	public void HighJump(Player p) {
		//Block below
		Location l = p.getLocation();
		l.setY(l.getY()-1.25);
		if(l.getBlock().getType() == Material.AIR) {
			return;
		}
		//If enough Magic
		if(getMagic(p) < this.getConfig().getInt("SpellCosts.High-Jump")) {
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.insufficient-magic").replaceAll("%prefix%", this.getConfig().getString("Messages.prefix"))));
			return;
		}
		changeMagic(p, -this.getConfig().getInt("SpellCosts.High-Jump"));
		Vector vec = p.getVelocity();
		p.setVelocity(vec.setY(vec.getY()+1));
	}

	public void Bloodvial(Player p) {
		//If enough Magic
		if(getMagic(p) < this.getConfig().getInt("SpellCosts.BloodVial")) {
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.insufficient-magic").replaceAll("%prefix%", this.getConfig().getString("Messages.prefix"))));
			return;
		}
		if(p.getFoodLevel()>6) {
			p.setFoodLevel(p.getFoodLevel()-6);
		} else {
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.insufficient-hunger").replaceAll("%prefix%", this.getConfig().getString("Messages.prefix"))));
			return;
		}
		changeMagic(p, -this.getConfig().getInt("SpellCosts.BloodVial"));
		p.getInventory().addItem(CustomItems.get(16));
	}
	
	public void Bloodrose(Player p) {
		//Recruiting Item Limit
		Map<String, Integer> PlayerRecruitingItems = new HashMap<String, Integer>();
    	if(this.getConfig().getInt("recruitingItemLimit") != -1) {
    		if(RecruitingItems.containsKey(p.getUniqueId())) {
        		PlayerRecruitingItems = RecruitingItems.get(p.getUniqueId());
    		} else {
    			RecruitingItems.put(p.getUniqueId(), PlayerRecruitingItems);
    		}
    		if(!PlayerRecruitingItems.containsKey("Bloodrose")) {
    			PlayerRecruitingItems.put("Bloodrose", 0);
    		}
    		if(PlayerRecruitingItems.get("Bloodrose") >= this.getConfig().getInt("recruitingItemLimit")) {
    			p.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.recruiting-item-limit").replaceAll("%prefix%", this.getConfig().getString("Messages.prefix"))));
	    		return;
	    	}
    	}
		//If enough Magic
		if(getMagic(p) < this.getConfig().getInt("SpellCosts.Bloodrose")) {
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.insufficient-magic").replaceAll("%prefix%", this.getConfig().getString("Messages.prefix"))));
			return;
		}
		changeMagic(p, -this.getConfig().getInt("SpellCosts.Bloodrose"));
		PlayerRecruitingItems.put("Bloodrose", PlayerRecruitingItems.get("Bloodrose")+1);
		RecruitingItems.put(p.getUniqueId(), PlayerRecruitingItems);
		addItemSafely(p, CustomItems.get(0));
		if(p.getHealth()>10) {
			p.setHealth(p.getHealth()-10);
		} else {
			p.setHealth(0);
		}
	}

	//Werewolf
	
	public void SummonWolf(Player p) {
		if(isDay(p)) {
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.night-only-abilities").replaceAll("%prefix%", this.getConfig().getString("Messages.prefix"))));
			return;
		}
		//If enough Magic
		if(getMagic(p) < this.getConfig().getInt("SpellCosts.Summon-Wolf")) {
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.insufficient-magic").replaceAll("%prefix%", this.getConfig().getString("Messages.prefix"))));
			return;
		}
		changeMagic(p, -this.getConfig().getInt("SpellCosts.Summon-Wolf"));
		Wolf wolf = (Wolf) p.getWorld().spawnEntity(p.getLocation(), EntityType.WOLF);
		wolf.setOwner(p);
	}
	
	public void Dash(Player p) {
		if(isDay(p)) {
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.night-only-abilities").replaceAll("%prefix%", this.getConfig().getString("Messages.prefix"))));
			return;
		}
		//Block below
		Location l = p.getLocation();
		l.setY(l.getY()-1.25);
		if(l.getBlock().getType() == Material.AIR) {
			return;
		}
		//If enough Magic
		if(getMagic(p) < this.getConfig().getInt("SpellCosts.Dash")) {
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.insufficient-magic").replaceAll("%prefix%", this.getConfig().getString("Messages.prefix"))));
			return;
		}
		changeMagic(p, -this.getConfig().getInt("SpellCosts.Dash"));
		Vector direction = p.getLocation().getDirection().multiply(1.5).setY(p.getVelocity().getY());
		p.setVelocity(direction);
	}
	
	public void Moonflower(Player p) {
		//Recruiting Item Limit
		Map<String, Integer> PlayerRecruitingItems = new HashMap<String, Integer>();
    	if(this.getConfig().getInt("recruitingItemLimit") != -1) {
    		if(RecruitingItems.containsKey(p.getUniqueId())) {
        		PlayerRecruitingItems = RecruitingItems.get(p.getUniqueId());
    		} else {
    			RecruitingItems.put(p.getUniqueId(), PlayerRecruitingItems);
    		}
    		if(!PlayerRecruitingItems.containsKey("Moonflower")) {
    			PlayerRecruitingItems.put("Moonflower", 0);
    		}
    		if(PlayerRecruitingItems.get("Moonflower") >= this.getConfig().getInt("recruitingItemLimit")) {
    			p.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.recruiting-item-limit").replaceAll("%prefix%", this.getConfig().getString("Messages.prefix"))));
	    		return;
	    	}
    	}
		//If enough Magic
		if(getMagic(p) < this.getConfig().getInt("SpellCosts.Moonflower")) {
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.insufficient-magic").replaceAll("%prefix%", this.getConfig().getString("Messages.prefix"))));
			return;
		}
		changeMagic(p, -this.getConfig().getInt("SpellCosts.Moonflower"));
		PlayerRecruitingItems.put("Moonflower", PlayerRecruitingItems.get("Moonflower")+1);
		RecruitingItems.put(p.getUniqueId(), PlayerRecruitingItems);
		addItemSafely(p, CustomItems.get(2));
		if(p.getHealth()>10) {
			p.setHealth(p.getHealth()-10);
		} else {
			p.setHealth(0);
		}
	}
	
	//Ghoul
	
	public void SummonUndead(Player p) {
		//If enough Magic
		if(getMagic(p) < this.getConfig().getInt("SpellCosts.Summon-Monster")) {
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.insufficient-magic").replaceAll("%prefix%", this.getConfig().getString("Messages.prefix"))));
			return;
		}
		changeMagic(p, -this.getConfig().getInt("SpellCosts.Summon-Monster"));
		Random rand = new Random();
		int random = (int) Math.floor((rand.nextFloat()*undead.size()));
		p.getWorld().spawnEntity(p.getLocation(), undead.get(random));
	}
	
	public void UnholyBond(Player p, LivingEntity t) {
		//If enough Magic
		if(getMagic(p) < this.getConfig().getInt("SpellCosts.Unholy-Bond")) {
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.insufficient-magic").replaceAll("%prefix%", this.getConfig().getString("Messages.prefix"))));
			return;
		}
		changeMagic(p, -this.getConfig().getInt("SpellCosts.Unholy-Bond"));
		Map<LivingEntity, Integer> TaggedEntities = new HashMap<LivingEntity, Integer>();
		if(UnholyBond.containsKey(p)) TaggedEntities = UnholyBond.get(p);
		TaggedEntities.put(t, 3000); //2.5 mins
		UnholyBond.put(p, TaggedEntities);
	}

	@EventHandler
	public void UnholyBondEffect(EntityDamageByEntityEvent event) {
		if(event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();
			if (event.getEntity() instanceof LivingEntity) {
				LivingEntity entity = (LivingEntity) event.getEntity();
				Map<LivingEntity, Integer> TaggedEntities = new HashMap<LivingEntity, Integer>();
				if(UnholyBond.containsKey(player)) TaggedEntities = UnholyBond.get(player);
				if(TaggedEntities.containsKey(entity)) {
					entity.damage(event.getDamage()/4);
				}
			}
		}
	}
	
	public void UnholyBondTimer() {
		for(Player p : UnholyBond.keySet()) {
			Map<LivingEntity, Integer> TaggedEntities = UnholyBond.get(p);
			for(LivingEntity e : TaggedEntities.keySet()) {
				int tick = TaggedEntities.get(e);
				tick = tick-1;
				if(tick>0) {
					TaggedEntities.put(e, tick);
				} else {
					TaggedEntities.remove(e);
				}
			}
			UnholyBond.put(p, TaggedEntities);
		}
	}
	
	public void Ghoulish(Player p) {
		//Recruiting Item Limit
		Map<String, Integer> PlayerRecruitingItems = new HashMap<String, Integer>();
    	if(this.getConfig().getInt("recruitingItemLimit") != -1) {
    		if(RecruitingItems.containsKey(p.getUniqueId())) {
        		PlayerRecruitingItems = RecruitingItems.get(p.getUniqueId());
    		} else {
    			RecruitingItems.put(p.getUniqueId(), PlayerRecruitingItems);
    		}
    		if(!PlayerRecruitingItems.containsKey("Ghoulish")) {
    			PlayerRecruitingItems.put("Ghoulish", 0);
    		}
    		if(PlayerRecruitingItems.get("Ghoulish") >= this.getConfig().getInt("recruitingItemLimit")) {
    			p.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.recruiting-item-limit").replaceAll("%prefix%", this.getConfig().getString("Messages.prefix"))));
	    		return;
	    	}
    	}
		//If enough Magic
		if(getMagic(p) < this.getConfig().getInt("SpellCosts.Ghoulish")) {
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.insufficient-magic").replaceAll("%prefix%", this.getConfig().getString("Messages.prefix"))));
			return;
		}
		changeMagic(p, -this.getConfig().getInt("SpellCosts.Ghoulish"));
		PlayerRecruitingItems.put("Ghoulish", PlayerRecruitingItems.get("Ghoulish")+1);
		RecruitingItems.put(p.getUniqueId(), PlayerRecruitingItems);
		addItemSafely(p, CustomItems.get(4));
		if(p.getHealth()>10) {
			p.setHealth(p.getHealth()-10);
		} else {
			p.setHealth(0);
		}
	}
	
	//Demon
	
	public void Fireball(Player p) {
		//If enough Magic
		if(getMagic(p) < this.getConfig().getInt("SpellCosts.Fireball")) {
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.insufficient-magic").replaceAll("%prefix%", this.getConfig().getString("Messages.prefix"))));
			return;
		}
		changeMagic(p, -this.getConfig().getInt("SpellCosts.Fireball"));
		p.launchProjectile(LargeFireball.class);
	}
	
	public void Explosion(Player p) {
		//If enough Magic
		if(getMagic(p) < this.getConfig().getInt("SpellCosts.Explosion")) {
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.insufficient-magic").replaceAll("%prefix%", this.getConfig().getString("Messages.prefix"))));
			return;
		}
		changeMagic(p, -this.getConfig().getInt("SpellCosts.Explosion"));
        p.getWorld().createExplosion(p.getLocation(), 2, true);
	}
	
	@SuppressWarnings("deprecation")
	public void SummonStrider(Player p) {
		//If enough Magic
		if(getMagic(p) < this.getConfig().getInt("SpellCosts.Summon-Strider")) {
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.insufficient-magic").replaceAll("%prefix%", this.getConfig().getString("Messages.prefix"))));
			return;
		}
		changeMagic(p, -this.getConfig().getInt("SpellCosts.Summon-Strider"));
		Steerable strider = (Strider) p.getWorld().spawnEntity(p.getLocation(), EntityType.STRIDER);
        strider.setAdult();
        strider.setSaddle(true);
        strider.setPassenger(p);
	}

	public void Snare(Player p, LivingEntity t) {
		//If enough Magic
		if(getMagic(p) < this.getConfig().getInt("SpellCosts.Snare")) {
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.insufficient-magic").replaceAll("%prefix%", this.getConfig().getString("Messages.prefix"))));
			return;
		}
		changeMagic(p, -this.getConfig().getInt("SpellCosts.Snare"));
		t.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 200, 128));
		t.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 200, 6));
	}
	
	public void Hellish(Player p) {
		//Recruiting Item Limit
		Map<String, Integer> PlayerRecruitingItems = new HashMap<String, Integer>();
    	if(this.getConfig().getInt("recruitingItemLimit") != -1) {
    		if(RecruitingItems.containsKey(p.getUniqueId())) {
        		PlayerRecruitingItems = RecruitingItems.get(p.getUniqueId());
    		} else {
    			RecruitingItems.put(p.getUniqueId(), PlayerRecruitingItems);
    		}
    		if(!PlayerRecruitingItems.containsKey("Hellish")) {
    			PlayerRecruitingItems.put("Hellish", 0);
    		}
    		if(PlayerRecruitingItems.get("Hellish") >= this.getConfig().getInt("recruitingItemLimit")) {
    			p.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.recruiting-item-limit").replaceAll("%prefix%", this.getConfig().getString("Messages.prefix"))));
	    		return;
	    	}
    	}
		//If enough Magic
		if(getMagic(p) < this.getConfig().getInt("SpellCosts.Hellish")) {
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.insufficient-magic").replaceAll("%prefix%", this.getConfig().getString("Messages.prefix"))));
			return;
		}
		changeMagic(p, -this.getConfig().getInt("SpellCosts.Hellish"));
		PlayerRecruitingItems.put("Hellish", PlayerRecruitingItems.get("Hellish")+1);
		RecruitingItems.put(p.getUniqueId(), PlayerRecruitingItems);
		addItemSafely(p, CustomItems.get(6));
		if(p.getHealth()>10) {
			p.setHealth(p.getHealth()-10);
		} else {
			p.setHealth(0);
		}
	}
	
	//Priest
	
	public void BanishBall(Player p) {
		if(banishlocation == null) {
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.no-banish-location").replaceAll("%prefix%", this.getConfig().getString("Messages.prefix"))));
			return;
		}
		//If enough Magic
		if(getMagic(p) < this.getConfig().getInt("SpellCosts.Banish")) {
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.insufficient-magic").replaceAll("%prefix%", this.getConfig().getString("Messages.prefix"))));
			return;
		}
		changeMagic(p, -this.getConfig().getInt("SpellCosts.Banish"));
    	SmallFireball projectile = p.launchProjectile(SmallFireball.class);
    	projectile.setIsIncendiary(false);
    	projectile.setCustomName(p.getUniqueId().toString() + ":Banish Spell");
	}
	
	@EventHandler
	public void BanishSpellHit(ProjectileHitEvent event) {
		if(event.getEntity().getCustomName() != null && event.getEntity().getCustomName().split(":")[1].equals("Banish Spell")) {
			if(event.getHitEntity() != null && event.getHitEntity() instanceof LivingEntity) {
				UUID uuid = UUID.fromString(event.getEntity().getCustomName().split(":")[0]);
				Player player = Bukkit.getPlayer(uuid);
				LivingEntity target = (LivingEntity) event.getHitEntity();
				Banish(player, target);
			}
		}
	}
	
	public void Banish(Player p, LivingEntity e) {
		if(e instanceof Player) {
			Player t = (Player) e;
			if(!isSupernatural(t)) {
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("only-supernaturals").replaceAll("%prefix%", this.getConfig().getString("Messages.prefix")).replaceAll("%spellname%", "banish")));
				return;
			}
			t.teleport(banishlocation);
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.banishment-success-sender").replaceAll("%prefix%", this.getConfig().getString("Messages.prefix")).replaceAll("%player%", p.getName()).replaceAll("%target%", t.getName())));
			t.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.banishment-success-target").replaceAll("%prefix%", this.getConfig().getString("Messages.prefix")).replaceAll("%player%", p.getName()).replaceAll("%target%", t.getName())));
		}
	}

	public void ExorciseBall(Player p) {
		//If enough Magic
		if(getMagic(p) < this.getConfig().getInt("SpellCosts.Exorcise")) {
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.insufficient-magic").replaceAll("%prefix%", this.getConfig().getString("Messages.prefix"))));
			return;
		}
		changeMagic(p, -this.getConfig().getInt("SpellCosts.Exorcise"));
    	SmallFireball projectile = p.launchProjectile(SmallFireball.class);
    	projectile.setIsIncendiary(false);
    	projectile.setCustomName(p.getUniqueId().toString() + ":Exorcise Spell");
	}
	
	@EventHandler
	public void ExorciseSpellHit(ProjectileHitEvent event) {
		if(event.getEntity().getCustomName() != null && event.getEntity().getCustomName().split(":")[1].equals("Exorcise Spell")) {
			if(event.getHitEntity() != null && event.getHitEntity() instanceof LivingEntity) {
				UUID uuid = UUID.fromString(event.getEntity().getCustomName().split(":")[0]);
				Player player = Bukkit.getPlayer(uuid);
				LivingEntity target = (LivingEntity) event.getHitEntity();
				Exorcise(player, target);
			}
		}
	}
	
	public void Exorcise(Player p, LivingEntity e) {
		if(e instanceof Player) {
			Player t = (Player) e;
			if(!isSupernatural(t)) {
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("only-supernaturals").replaceAll("%prefix%", this.getConfig().getString("Messages.prefix")).replaceAll("%spellname%", "exorcise")));
				return;
			}
			//Set Class to Human
			t.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.exorcism-success-target").replaceAll("%prefix%", this.getConfig().getString("Messages.prefix")).replaceAll("%player%", p.getName()).replaceAll("%target%", t.getName())));
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.exorcism-success-sender").replaceAll("%prefix%", this.getConfig().getString("Messages.prefix")).replaceAll("%player%", p.getName()).replaceAll("%target%", t.getName())));
			setRace(t, "Human", false);
			CollectBounty(t, p);
		}
	}

	public void DrainBall(Player p) {
		//If enough Magic
		if(getMagic(p) < this.getConfig().getInt("SpellCosts.Drain")) {
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.insufficient-magic").replaceAll("%prefix%", this.getConfig().getString("Messages.prefix"))));
			return;
		}
		changeMagic(p, -this.getConfig().getInt("SpellCosts.Drain"));
    	SmallFireball projectile = p.launchProjectile(SmallFireball.class);
    	projectile.setIsIncendiary(false);
    	projectile.setCustomName(p.getUniqueId().toString() + ":Drain Spell");
	}
	
	@EventHandler
	public void DrainSpellHit(ProjectileHitEvent event) {
		if(event.getEntity().getCustomName() != null && event.getEntity().getCustomName().split(":")[1].equals("Drain Spell")) {
			if(event.getHitEntity() != null && event.getHitEntity() instanceof LivingEntity) {
				UUID uuid = UUID.fromString(event.getEntity().getCustomName().split(":")[0]);
				Player player = Bukkit.getPlayer(uuid);
				LivingEntity target = (LivingEntity) event.getHitEntity();
				Drain(player, target);
			}
		}
	}
	
	public void Drain(Player p, LivingEntity e) {
		if(e instanceof Player) {
			Player t = (Player) e;
			if(!isSupernatural(t)) {
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("only-supernaturals").replaceAll("%prefix%", this.getConfig().getString("Messages.prefix")).replaceAll("%spellname%", "drain")));
				return;
			}
			//If enough target Magic
			String[] targetdata = PlayerData.get(t.getUniqueId());
			if(Double.parseDouble(targetdata[1])<=0) {
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.target-no-magic").replaceAll("%prefix%", this.getConfig().getString("Messages.prefix"))));
				return;
			}
			//Remove Magic
			setMagic(t, getMagic(t)*0.85);
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void NoSpellDamage(EntityDamageByEntityEvent event) {
		Entity entity = event.getDamager();
		if(entity instanceof SmallFireball) {
			String name = entity.getCustomName();
			if(name != null && name.contains(":")) {
				String spell = name.split(":")[1];
				if(spell.equals("Banish Spell") || spell.equals("Exorcise Spell") || spell.equals("Drain Spell")) {
					event.setCancelled(true);
				}
			}
		}
	}
	
	public void HealHuman(Player p, LivingEntity e) {
		if(e instanceof Player) {
			Player t = (Player) e;
			if(isSupernatural(t)) {
				return;
			}
			//If enough Magic
			if(getMagic(p) < this.getConfig().getInt("SpellCosts.Heal-Human")) {
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.insufficient-magic").replaceAll("%prefix%", this.getConfig().getString("Messages.prefix"))));
				return;
			}
			if(t.getHealth()==20) {
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.full-health").replaceAll("%prefix%", this.getConfig().getString("Messages.prefix")).replaceAll("%player%", p.getName()).replaceAll("%target%", t.getName())));
				return;
			}
			//Remove Magic
			changeMagic(p, -this.getConfig().getInt("SpellCosts.Heal-Human"));
			//Increase target health
			int health = (int) (t.getHealth() + Math.round(Math.random()*5));
			if(health > 20) {
				health = 20;
			}
			t.setHealth(health);
			t.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.heal-received").replaceAll("%prefix%", this.getConfig().getString("Messages.prefix")).replaceAll("%player%", p.getName()).replaceAll("%target%", t.getName())));
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.target-healed").replaceAll("%prefix%", this.getConfig().getString("Messages.prefix")).replaceAll("%player%", p.getName()).replaceAll("%target%", t.getName())));
		}
	}

	public void GuardianAngel(Player p, LivingEntity e) {
		if(e instanceof Player) {
			Player t = (Player) e;
			if(isSupernatural(t)) {
				return;
			}
			
			//If Not Already Protected
			for(UUID uuid : GuardianAngel) {
				if(uuid.equals(t.getUniqueId())) {
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.guardian-angel-failed-sender").replaceAll("%prefix%", this.getConfig().getString("Messages.prefix")).replaceAll("%player%", p.getName()).replaceAll("%target%", t.getName())));
					t.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.guardian-angel-failed-target").replaceAll("%prefix%", this.getConfig().getString("Messages.prefix")).replaceAll("%player%", p.getName()).replaceAll("%target%", t.getName())));
					return;
				}
			}
			
			//If enough Magic
			if(getMagic(p) < this.getConfig().getInt("SpellCosts.Guardian-Angel")) {
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.insufficient-magic").replaceAll("%prefix%", this.getConfig().getString("Messages.prefix"))));
				return;
			}
			changeMagic(p, -this.getConfig().getInt("SpellCosts.Guardian-Angel"));
		
			//add guardian angel
			GuardianAngel.add(t.getUniqueId());
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.guardian-angel-succeeded-sender").replaceAll("%prefix%", this.getConfig().getString("Messages.prefix")).replaceAll("%player%", p.getName()).replaceAll("%target%", t.getName())));
			t.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.guardian-angel-succeeded-target").replaceAll("%prefix%", this.getConfig().getString("Messages.prefix")).replaceAll("%player%", p.getName()).replaceAll("%target%", t.getName())));
		}
	}

	@EventHandler
	public void GuardianAngelEffect(EntityDamageByEntityEvent event) {
		if(event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();
			if(player.getHealth()-event.getDamage()<=0) {
				for(UUID uuid : GuardianAngel) {
					if(player.getUniqueId().equals(uuid)) {
						player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.guardian-angel-use").replaceAll("%prefix%", this.getConfig().getString("Messages.prefix"))));
						GuardianAngel.remove(player.getUniqueId());
						event.setCancelled(true);
						player.setHealth(8);
						player.damage(1);
						//event.setDamage(player.getHealth()-1);
						for(PotionEffect effect : player.getActivePotionEffects()) {
							player.removePotionEffect(effect.getType());
						}
						player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 45*20, 1));
						player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 40*20, 1));
						player.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 5*20, 1));
					}
				}
			}
		}
	}
	
	@EventHandler
	public void GuardianAngelEffect(EntityDamageEvent event) {
		if(event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();
			if(player.getHealth()-event.getDamage()<=0) {
				for(UUID uuid : GuardianAngel) {
					if(player.getUniqueId().equals(uuid)) {
						player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.guardian-angel-use").replaceAll("%prefix%", this.getConfig().getString("Messages.prefix"))));
						GuardianAngel.remove(player.getUniqueId());
						event.setCancelled(true);
						player.setHealth(8);
						player.damage(1);
						//event.setDamage(player.getHealth()-1);
						for(PotionEffect effect : player.getActivePotionEffects()) {
							player.removePotionEffect(effect.getType());
						}
						player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 45*20, 1));
						player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 40*20, 1));
						player.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 5*20, 1));
					}
				}
			}
		}
	}

	public void Cure(Player p) {
		//If enough Magic
		if(getMagic(p) < this.getConfig().getInt("SpellCosts.Cure")) {
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.insufficient-magic").replaceAll("%prefix%", this.getConfig().getString("Messages.prefix"))));
			return;
		}
		changeMagic(p, -this.getConfig().getInt("SpellCosts.Cure"));
		addItemSafely(p, CustomItems.get(8));
	}

	public void HolyBook(Player p) {
		//Recruiting Item Limit
		Map<String, Integer> PlayerRecruitingItems = new HashMap<String, Integer>();
    	if(this.getConfig().getInt("recruitingItemLimit") != -1) {
    		if(RecruitingItems.containsKey(p.getUniqueId())) {
        		PlayerRecruitingItems = RecruitingItems.get(p.getUniqueId());
    		} else {
    			RecruitingItems.put(p.getUniqueId(), PlayerRecruitingItems);
    		}
    		if(!PlayerRecruitingItems.containsKey("Holy-Book")) {
    			PlayerRecruitingItems.put("Holy-Book", 0);
    		}
    		if(PlayerRecruitingItems.get("Holy-Book") >= this.getConfig().getInt("recruitingItemLimit")) {
    			p.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.recruiting-item-limit").replaceAll("%prefix%", this.getConfig().getString("Messages.prefix"))));
	    		return;
	    	}
    	}
		//If enough Magic
		if(getMagic(p) < this.getConfig().getInt("SpellCosts.Holy-Book")) {
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.insufficient-magic").replaceAll("%prefix%", this.getConfig().getString("Messages.prefix"))));
			return;
		}
		changeMagic(p, -this.getConfig().getInt("SpellCosts.Holy-Book"));
		PlayerRecruitingItems.put("Holy-Book", PlayerRecruitingItems.get("Holy-Book")+1);
		RecruitingItems.put(p.getUniqueId(), PlayerRecruitingItems);
		addItemSafely(p, CustomItems.get(9));
		if(p.getHealth()>10) {
			p.setHealth(p.getHealth()-10);
		} else {
			p.setHealth(0);
		}
	}
	
	//Necromancer
	
	public void SummonSkeleton(Player p) {
		//If enough Magic
		String[] playerdata = PlayerData.get(p.getUniqueId());
		if(Double.parseDouble(playerdata[1])<this.getConfig().getInt("SpellCosts.Summon-Skeleton")) {
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.insufficient-magic").replaceAll("%prefix%", this.getConfig().getString("Messages.prefix"))));
			return;
		}
		double magic = Double.parseDouble(playerdata[1])-this.getConfig().getInt("SpellCosts.Summon-Skeleton");
		playerdata[1] = ("" + magic);
		PlayerData.put(p.getUniqueId(), playerdata);
		Skeleton skeleton = (Skeleton) p.getWorld().spawnEntity(p.getLocation(), EntityType.SKELETON);
		skeleton.setCustomName(p.getName() + "'s Follower");
		skeleton.setCustomNameVisible(true);
		skeleton.getPersistentDataContainer().set(new NamespacedKey(this, "Leader"), PersistentDataType.STRING, p.getUniqueId().toString());
		skeleton.getPersistentDataContainer().set(new NamespacedKey(this, "Following"), PersistentDataType.INTEGER, 1);
		/*if(!Followers.containsKey(p.getUniqueId())) {
			Followers.put(p.getUniqueId(), new ArrayList<UUID>());
		}
		List<UUID> followerlist = Followers.get(p.getUniqueId());
		followerlist.add(skeleton.getUniqueId());
		Followers.put(p.getUniqueId(), followerlist);*/
	}
	
	public void SummonUndeadFollower(Player p) {
		//If enough Magic
		if(getMagic(p) < this.getConfig().getInt("SpellCosts.Summon-Undead")) {
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.insufficient-magic").replaceAll("%prefix%", this.getConfig().getString("Messages.prefix"))));
			return;
		}
		changeMagic(p, -this.getConfig().getInt("SpellCosts.Summon-Undead"));
		Random rand = new Random();
		int random = (int) Math.floor((rand.nextFloat()*undead.size()));
		Monster entity = (Monster) p.getWorld().spawnEntity(p.getLocation(), undead.get(random));
		entity.setCustomName(p.getName() + "'s Follower");
		entity.setCustomNameVisible(true);
		entity.getPersistentDataContainer().set(new NamespacedKey(this, "Leader"), PersistentDataType.STRING, p.getUniqueId().toString());
		entity.getPersistentDataContainer().set(new NamespacedKey(this, "Following"), PersistentDataType.INTEGER, 1);
		/*if(!Followers.containsKey(p.getUniqueId())) {
			Followers.put(p.getUniqueId(), new ArrayList<UUID>());
		}
		List<UUID> followerlist = Followers.get(p.getUniqueId());
		followerlist.add(entity.getUniqueId());
		Followers.put(p.getUniqueId(), followerlist);*/
	}
	
	@SuppressWarnings("deprecation")
	public void HealUndead(Player p) {
		//If enough Magic
		String[] playerdata = PlayerData.get(p.getUniqueId());
		if(Double.parseDouble(playerdata[1])<this.getConfig().getInt("SpellCosts.Heal-Undead")) {
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.insufficient-magic").replaceAll("%prefix%", this.getConfig().getString("Messages.prefix"))));
			return;
		}
		double magic = Double.parseDouble(playerdata[1])-this.getConfig().getInt("SpellCosts.Heal-Undead");
		playerdata[1] = ("" + magic);
		PlayerData.put(p.getUniqueId(), playerdata);
		for(Entity e : p.getNearbyEntities(10, 10, 10)) {
			if(e instanceof LivingEntity) {
				LivingEntity le = (LivingEntity) e;
				if(isUndead(e.getType())) {
					double health = le.getHealth() + (le.getMaxHealth()*0.35);
					if(health>le.getMaxHealth()) {
						health = le.getMaxHealth();
					}
					le.setHealth(health);
				}
			}
		}
	}

	public void SetRessurectionSpawn(Player p) {
		//If enough Magic
		String[] playerdata = PlayerData.get(p.getUniqueId());
		if(Double.parseDouble(playerdata[1])<this.getConfig().getInt("SpellCosts.Ressurection-Spawn")) {
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.insufficient-magic").replaceAll("%prefix%", this.getConfig().getString("Messages.prefix"))));
			return;
		}
		double magic = Double.parseDouble(playerdata[1])-this.getConfig().getInt("SpellCosts.Ressurection-Spawn");
		playerdata[1] = ("" + magic);
		PlayerData.put(p.getUniqueId(), playerdata);
		RessurectLocation.put(p.getUniqueId(), p.getLocation());
		p.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.set-ressurection-location").replaceAll("%prefix%", this.getConfig().getString("Messages.prefix"))));
	}

	@EventHandler
	public void RessurectionSpawn(PlayerRespawnEvent event) {
		Player player = event.getPlayer();
		if(!PlayerData.get(player.getUniqueId())[0].equalsIgnoreCase("Necromancer")) {
			return;
		}
		if(RessurectLocation.get(player.getUniqueId())!=null) {
            this.getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
                public void run() {
					player.teleport(RessurectLocation.get(player.getUniqueId()));
					player.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getConfig().getString("Messages.go-ressurection-location").replaceAll("%prefix%", main.getConfig().getString("Messages.prefix"))));
					RessurectLocation.put(player.getUniqueId(), null);
                }
            }, 1L);
		} 
	}
	
	public void BookOfDeath(Player p) {
		//Recruiting Item Limit
		Map<String, Integer> PlayerRecruitingItems = new HashMap<String, Integer>();
    	if(this.getConfig().getInt("recruitingItemLimit") != -1) {
    		if(RecruitingItems.containsKey(p.getUniqueId())) {
        		PlayerRecruitingItems = RecruitingItems.get(p.getUniqueId());
    		} else {
    			RecruitingItems.put(p.getUniqueId(), PlayerRecruitingItems);
    		}
    		if(!PlayerRecruitingItems.containsKey("Book-Of-Death")) {
    			PlayerRecruitingItems.put("Book-Of-Death", 0);
    		}
    		if(PlayerRecruitingItems.get("Book-Of-Death") >= this.getConfig().getInt("recruitingItemLimit")) {
    			p.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.recruiting-item-limit").replaceAll("%prefix%", this.getConfig().getString("Messages.prefix"))));
	    		return;
	    	}
    	}
		//If enough Magic
		if(getMagic(p) < this.getConfig().getInt("SpellCosts.Book-Of-Death")) {
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.insufficient-magic").replaceAll("%prefix%", this.getConfig().getString("Messages.prefix"))));
			return;
		}
		changeMagic(p, -this.getConfig().getInt("SpellCosts.Book-Of-Death"));
		PlayerRecruitingItems.put("Book-Of-Death", PlayerRecruitingItems.get("Book-Of-Death")+1);
		RecruitingItems.put(p.getUniqueId(), PlayerRecruitingItems);
		addItemSafely(p, CustomItems.get(11));
		if(p.getHealth()>10) {
			p.setHealth(p.getHealth()-10);
		} else {
			p.setHealth(0);
		}
	}
	
	//Witch Hunter
	
	public void ArrowSwitcher(Player p) {
		if(!ArrowType.containsKey(p)) {
			ArrowType.put(p, 0);
		} else {
			ArrowType.put(p, ((ArrowType.get(p) + 1) % 5));
		}
	}
	
	public void MultiArrow(Player p) {
		//If enough Magic
		if(getMagic(p) < this.getConfig().getInt("SpellCosts.Triple-Arrow")) {
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.insufficient-magic").replaceAll("%prefix%", this.getConfig().getString("Messages.prefix"))));
			return;
		}
		changeMagic(p, -this.getConfig().getInt("SpellCosts.Triple-Arrow"));
		Location loc = p.getLocation();
		loc.setYaw(loc.getYaw() - 10F);
		Vector playerDirection = loc.getDirection().multiply(1.5);
		Arrow arrow = p.launchProjectile(Arrow.class, playerDirection);
		arrow.setPickupStatus(Arrow.PickupStatus.DISALLOWED);
		loc.setYaw(loc.getYaw() + 10F);
		playerDirection = loc.getDirection().multiply(1.5);
		Arrow arrow2 = p.launchProjectile(Arrow.class, playerDirection);
		arrow2.setPickupStatus(Arrow.PickupStatus.DISALLOWED);
		loc.setYaw(loc.getYaw() + 10F);
		playerDirection = loc.getDirection().multiply(1.5);
		Arrow arrow3 = p.launchProjectile(Arrow.class, playerDirection);
		arrow3.setPickupStatus(Arrow.PickupStatus.DISALLOWED);
	}
	
	public void VolleyArrow(Player p) {
		if(!this.getCooldown(p.getUniqueId(),"VolleyArrow")) {
			//If enough Magic
			if(getMagic(p) < this.getConfig().getInt("SpellCosts.Volley-Arrow")) {
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.insufficient-magic").replaceAll("%prefix%", this.getConfig().getString("Messages.prefix"))));
				return;
			}
			changeMagic(p, -this.getConfig().getInt("SpellCosts.Volley-Arrow"));
			this.setCooldown(p.getUniqueId(), "VolleyArrow", 80);
			Location loc = p.getLocation();
			loc.setYaw(loc.getYaw());
			Vector playerDirection = loc.getDirection().multiply(2);
			Arrow arrow = p.launchProjectile(Arrow.class, playerDirection);
			arrow.setPickupStatus(Arrow.PickupStatus.DISALLOWED);
            this.getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
                public void run() {
        			Location loc = p.getLocation();
        			loc.setYaw(loc.getYaw());
        			Vector playerDirection = loc.getDirection().multiply(2);
        			Arrow arrow = p.launchProjectile(Arrow.class, playerDirection);
        			arrow.setPickupStatus(Arrow.PickupStatus.DISALLOWED);
                }
            }, 5L);
			this.getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
                public void run() {
        			Location loc = p.getLocation();
        			loc.setYaw(loc.getYaw());
        			Vector playerDirection = loc.getDirection().multiply(2);
        			Arrow arrow = p.launchProjectile(Arrow.class, playerDirection);
        			arrow.setPickupStatus(Arrow.PickupStatus.DISALLOWED);
                }
            }, 10L);
			this.getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
                public void run() {
        			Location loc = p.getLocation();
        			loc.setYaw(loc.getYaw());
        			Vector playerDirection = loc.getDirection().multiply(2);
        			Arrow arrow = p.launchProjectile(Arrow.class, playerDirection);
        			arrow.setPickupStatus(Arrow.PickupStatus.DISALLOWED);
                }
            }, 15L);
			this.getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
                public void run() {
        			Location loc = p.getLocation();
        			loc.setYaw(loc.getYaw());
        			Vector playerDirection = loc.getDirection().multiply(2);
        			Arrow arrow = p.launchProjectile(Arrow.class, playerDirection);
        			arrow.setPickupStatus(Arrow.PickupStatus.DISALLOWED);
                }
            }, 20L);
		} else {
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.cooldown").replaceAll("%prefix%", this.getConfig().getString("Messages.prefix"))));
		}
	}
	
	public void GrappleArrow(Player p) {
		//If enough Magic
		if(getMagic(p) < this.getConfig().getInt("SpellCosts.Grapple-Arrow")) {
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.insufficient-magic").replaceAll("%prefix%", this.getConfig().getString("Messages.prefix"))));
			return;
		}
		changeMagic(p, -this.getConfig().getInt("SpellCosts.Grapple-Arrow"));
		Location loc = p.getLocation();
		Vector playerDirection = loc.getDirection().multiply(2);
		Arrow arrow = p.launchProjectile(Arrow.class, playerDirection);
		GrappleLocation.put(p, loc.clone());
		arrow.setPickupStatus(Arrow.PickupStatus.DISALLOWED);
		arrow.setDamage(0);
		GrappleArrows.put(p, arrow);
	}
	
	@EventHandler
	public void CancelGrapple(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		if(event.getFrom().getY() + 0.419 < event.getTo().getY()) {
			if(GrappleArrows.containsKey(player)) {
				GrappleArrows.get(player).remove();
				GrappleArrows.remove(player);
			}
			if(GrappleArrows2.containsKey(player)) {
				GrappleArrows2.get(player).remove();
				GrappleArrows2.remove(player);
			}
		}
	}
	
	@EventHandler
	public void GrappleArrowStage1(ProjectileHitEvent event) {
		if(event.getEntity() instanceof Arrow) {
			Arrow arrow = (Arrow) event.getEntity();
			for(Player p : GrappleArrows.keySet()) {
				Arrow GrappleArrow = GrappleArrows.get(p);
				if(arrow.equals(GrappleArrow)) {
					Vector playerDirection = GrappleLocation.get(p).getDirection().multiply(2);
					Arrow arrow2 = p.launchProjectile(Arrow.class, playerDirection);
					arrow2.setPickupStatus(Arrow.PickupStatus.DISALLOWED);
					arrow2.setDamage(0);
					for(Player player : Bukkit.getOnlinePlayers()) ((CraftPlayer)player).getHandle().playerConnection.sendPacket(new PacketPlayOutEntityDestroy(((CraftArrow) arrow2).getHandle().getId()));
					GrappleArrows2.put(p, arrow2);
				}
			}
		}
	}
	
	public void GrappleArrowStage2(Player p) {
		if(GrappleArrows2.containsKey(p)) {
			Location loc = GrappleArrows2.get(p).getLocation();
			loc.setPitch(p.getLocation().getPitch());
			loc.setYaw(p.getLocation().getYaw());
			p.teleport(loc);
		}
	}
	
	@EventHandler
	public void GrappleArrowCleanUp(ProjectileHitEvent event) {
		if(event.getEntity() instanceof Arrow) {
			Arrow arrow = (Arrow) event.getEntity();
			for(Player p : GrappleArrows2.keySet()) {
				Arrow GrappleArrow2 = GrappleArrows2.get(p);
				if(arrow.equals(GrappleArrow2)) {
					Arrow GrappleArrow = GrappleArrows.get(p);
					GrappleArrow.remove();
					GrappleArrow2.remove();
					GrappleArrows.remove(p);
					GrappleArrows2.remove(p);
				}
			}
		}
	}
	
	public void FireArrow(Player p) {
		//If enough Magic
		if(getMagic(p) < this.getConfig().getInt("SpellCosts.Fire-Arrow")) {
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.insufficient-magic").replaceAll("%prefix%", this.getConfig().getString("Messages.prefix"))));
			return;
		}
		changeMagic(p, -this.getConfig().getInt("SpellCosts.Fire-Arrow"));
		Location loc = p.getLocation();
		Vector playerDirection = loc.getDirection().multiply(2);
		Arrow arrow = p.launchProjectile(Arrow.class, playerDirection);
		arrow.setPickupStatus(Arrow.PickupStatus.DISALLOWED);
		arrow.setFireTicks(10000);
		FlameArrows.add(arrow.getUniqueId());
	}
	
	@EventHandler
	public void FlameArrowCleanUp(ProjectileHitEvent event) {
		if(event.getEntity() instanceof Arrow) {
			Arrow arrow = (Arrow) event.getEntity();
			if(FlameArrows.contains(arrow.getUniqueId())) {
				if(event.getHitEntity() != null) {
					event.getHitEntity().setFireTicks(100);
				} else {
					if(arrow.getLocation().getBlock().getType().equals(Material.AIR)) {
						arrow.getLocation().getBlock().setType(Material.FIRE);
					}
				}
				arrow.remove();
				FlameArrows.remove(arrow.getUniqueId());
			}
		}
	}
	
	public void PowerArrow(Player p) {
		if(!this.getCooldown(p.getUniqueId(),"PowerArrow")) {
			this.setCooldown(p.getUniqueId(), "PowerArrow", 80);
			if(getMagic(p) < this.getConfig().getInt("SpellCosts.Power-Arrow")) {
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.insufficient-magic").replaceAll("%prefix%", this.getConfig().getString("Messages.prefix"))));
				return;
			}
			changeMagic(p, -this.getConfig().getInt("SpellCosts.Power-Arrow"));
			Location loc = p.getLocation();
			Vector playerDirection = loc.getDirection();
			playerDirection.multiply(4);
			Arrow arrow = p.launchProjectile(Arrow.class, playerDirection);
			arrow.setPickupStatus(Arrow.PickupStatus.DISALLOWED);
			arrow.setDamage(arrow.getDamage()*4);
			arrow.setKnockbackStrength(arrow.getKnockbackStrength()*2);
			PowerArrows.add(arrow.getUniqueId());
		} else {
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.cooldown").replaceAll("%prefix%", this.getConfig().getString("Messages.prefix"))));
		}
	}
	
	@EventHandler
	public void PowerArrowCleanUp(ProjectileHitEvent event) {
		if(event.getEntity() instanceof Arrow) {
			Arrow arrow = (Arrow) event.getEntity();
			if(PowerArrows.contains(arrow.getUniqueId())) {
			    arrow.getWorld().createExplosion(arrow.getLocation(), 1, true);
				if(event.getHitEntity() != null) {
					event.getHitEntity().setFireTicks(100);
				}
				arrow.remove();
				PowerArrows.remove(arrow);
			}
		}
	}
	
	public void BookOfWitchHunter(Player p) {
		//Recruiting Item Limit
		Map<String, Integer> PlayerRecruitingItems = new HashMap<String, Integer>();
    	if(this.getConfig().getInt("recruitingItemLimit") != -1) {
    		if(RecruitingItems.containsKey(p.getUniqueId())) {
        		PlayerRecruitingItems = RecruitingItems.get(p.getUniqueId());
    		} else {
    			RecruitingItems.put(p.getUniqueId(), PlayerRecruitingItems);
    		}
    		if(!PlayerRecruitingItems.containsKey("Book-Of-WitchHunter")) {
    			PlayerRecruitingItems.put("Book-Of-WitchHunter", 0);
    		}
    		if(PlayerRecruitingItems.get("Book-Of-WitchHunter") >= this.getConfig().getInt("recruitingItemLimit")) {
    			p.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.recruiting-item-limit").replaceAll("%prefix%", this.getConfig().getString("Messages.prefix"))));
	    		return;
	    	}
    	}
		//If enough Magic
		if(getMagic(p) < this.getConfig().getInt("SpellCosts.Book-Of-WitchHunter")) {
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.insufficient-magic").replaceAll("%prefix%", this.getConfig().getString("Messages.prefix"))));
			return;
		}
		changeMagic(p, -this.getConfig().getInt("SpellCosts.Book-Of-WitchHunter"));
		PlayerRecruitingItems.put("Book-Of-WitchHunter", PlayerRecruitingItems.get("Book-Of-WitchHunter")+1);
		RecruitingItems.put(p.getUniqueId(), PlayerRecruitingItems);
		addItemSafely(p, CustomItems.get(13));
		if(p.getHealth()>10) {
			p.setHealth(p.getHealth()-10);
		} else {
			p.setHealth(0);
		}
	}

	//Angel

	public void HolySmite(Player p) {
		p.sendMessage("This Command is WIP");
	}
	
	public void Taunt(Player p) {
		p.sendMessage("This Command is WIP");
	}
	
	public void HolyBlessing(Player p) {
		p.sendMessage("This Command is WIP");
	}
	
	public void Wings(Player p) {
		//If enough Magic
		String[] playerdata = PlayerData.get(p.getUniqueId());
		if(Double.parseDouble(playerdata[1])<this.getConfig().getInt("SpellCosts.Wings")) {
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.insufficient-magic").replaceAll("%prefix%", this.getConfig().getString("Messages.prefix"))));
			return;
		}
		double magic = Double.parseDouble(playerdata[1])-this.getConfig().getInt("SpellCosts.Wings");
		playerdata[1] = ("" + magic);
		PlayerData.put(p.getUniqueId(), playerdata);
		p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, 400, 1));
	}
	
	//Recruiting Items
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void ConsumePotion(PlayerItemConsumeEvent event) {
		Player player = event.getPlayer();
		if(event.getItem().equals(CustomItems.get(0))) {
			event.setCancelled(true);
    		if(setRace(player, "Vampire", true)) {
    	    	player.setItemInHand(new ItemStack(Material.AIR));
    		}
		}
		if(event.getItem().equals(CustomItems.get(2))) {
			event.setCancelled(true);
    		if(setRace(player, "Werewolf", true)) {
    	    	player.setItemInHand(new ItemStack(Material.AIR));
    		}
		}
		if(event.getItem().equals(CustomItems.get(4))) {
			event.setCancelled(true);
    		if(setRace(player, "Ghoul", true)) {
    	    	player.setItemInHand(new ItemStack(Material.AIR));
    		}
		}
		if(event.getItem().equals(CustomItems.get(6))) {
			event.setCancelled(true);
    		if(setRace(player, "Demon", true)) {
    	    	player.setItemInHand(new ItemStack(Material.AIR));
    		}
		}
		if(event.getItem().equals(CustomItems.get(8))) {
			event.setCancelled(true);
	    	if(isSupernatural(player)) {
	    		if(setRace(player, "Human", false)) {
					CollectBounty(player, player);
	    	    	player.setItemInHand(new ItemStack(Material.AIR));
		    		return;
	    		}
	    	} else {
        		player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.must-be-supernatural").replaceAll("%prefix%", this.getConfig().getString("Messages.prefix"))));
	    	}
		}
		if(event.getItem().equals(CustomItems.get(16))) {
			event.setCancelled(true);
    		if(getRace(player).equals("Vampire")) {
    			if(player.getFoodLevel()<20) {
    				player.setFoodLevel(player.getFoodLevel()+4);
    				player.setSaturation(player.getSaturation()+4);
        	    	player.setItemInHand(new ItemStack(Material.AIR));
    				return;
    			}
    		}
		}
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void ReadRecruitingBook(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		if(event.getAction() != Action.RIGHT_CLICK_BLOCK && event.getAction() != Action.RIGHT_CLICK_AIR) {
			return;
		}
		ItemStack item = event.getItem();
		if(item == null) {
			return;
		}
		ItemStack itemclone = item.clone();
		itemclone.setAmount(1);
		if(itemclone.equals(CustomItems.get(9))) {
			if(setRace(player, "Priest", true)) {
		    	event.setCancelled(true);
		    	if(item.getAmount()-1<1) {
			    	player.setItemInHand(new ItemStack(Material.AIR));
		    	} else {
					item.setAmount(item.getAmount()-1);
			    	player.setItemInHand(item);
		    	}
			}
		}
		if(itemclone.equals(CustomItems.get(11))) {
			if(setRace(player, "Necromancer", true)) {
		    	event.setCancelled(true);
		    	if(item.getAmount()-1<1) {
			    	player.setItemInHand(new ItemStack(Material.AIR));
		    	} else {
					item.setAmount(item.getAmount()-1);
			    	player.setItemInHand(item);
		    	}
			}
		}
		if(itemclone.equals(CustomItems.get(13))) {
			if(setRace(player, "WitchHunter", true)) {
		    	event.setCancelled(true);
		    	if(item.getAmount()-1<1) {
			    	player.setItemInHand(new ItemStack(Material.AIR));
		    	} else {
					item.setAmount(item.getAmount()-1);
			    	player.setItemInHand(item);
		    	}
			}
		}
	}

	//Altars
	
	@EventHandler()
	public void openAltar(PlayerInteractEvent event) {
		if(event.getHand() == EquipmentSlot.OFF_HAND)
		return;
		if(event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			Player player = event.getPlayer();
			Block block = event.getClickedBlock();
			Location loc = block.getLocation();
			Block down = loc.add(0, -1, 0).getBlock();
			if(block.getType().equals(Material.ENCHANTING_TABLE)) {
				//Holy Alter
				if(down.getType().equals(Material.GOLD_BLOCK)) {
					event.setCancelled(true);
					openHolyAltar(player);
				}
				//Unholy Alter
				if(down.getType().equals(Material.MAGMA_BLOCK)) {
					event.setCancelled(true);
					openUnholyAltar(player);
				}
			}
			if(block.getType().equals(Material.BOOKSHELF)) {
				//Witch Hunter Board
				if(down.getType().equals(Material.JACK_O_LANTERN)) {
					event.setCancelled(true);
					openWitchHunterBoard(player);
				}
			}
		}
	}
	
    @EventHandler()
    public void HolyAlterLogic(InventoryClickEvent e) {
    	if(e.getClickedInventory() == null || e.getWhoClicked().getInventory() == null) return;
        if (e.getClickedInventory().equals(e.getWhoClicked().getInventory())) {
            return;
        }
        if (e.getView().getTitle().contains("Holy Altar")) {
            if (e.getSlot() != 11 && e.getSlot() != 15) {
                e.setCancelled(true);
            }
            if (e.getSlot() == 15) {
                e.setCancelled(true);
        		Player player = (Player) e.getWhoClicked();
        		if(setRace(player, "Priest", true)) {
        	    	e.setCancelled(true);
        		}
            }
        }
    }
	
    @EventHandler()
    public void UnholyAlterLogic(InventoryClickEvent e) {
    	if(e.getClickedInventory() == null || e.getWhoClicked().getInventory() == null) return;
        if (e.getClickedInventory().equals(e.getWhoClicked().getInventory())) {
            return;
        }
        if (e.getView().getTitle().contains("Unholy Altar")) {
            if (e.getSlot() != 10 && e.getSlot() != 13 && e.getSlot() != 16) {
                e.setCancelled(true);
            }
            if (e.getSlot() == 13) {
                e.setCancelled(true);
        		Player player = (Player) e.getWhoClicked();
        		if(setRace(player, "Vampire", true)) {
        	    	e.setCancelled(true);
        		}
            }
            if (e.getSlot() == 16) {
                e.setCancelled(true);
        		Player player = (Player) e.getWhoClicked();
        		if(setRace(player, "Necromancer", true)) {
        	    	e.setCancelled(true);
        		}
            }
        }
    }
    
    @EventHandler()
    public void HolyAlterDonationLogic(InventoryClickEvent e) {
    	if(e.getClickedInventory() == null || e.getWhoClicked().getInventory() == null) return;
        if (e.getView().getTitle().contains("Holy Altar")) {
        	Player player = (Player) e.getWhoClicked();
           	String[] newData = PlayerData.get(player.getUniqueId());
        	String race = newData[0];
            this.getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
                public void run() {
					if(e.getView().getItem(11) != null) {
			            ItemStack donation = e.getView().getItem(11);
			           	if(race.equals("Priest") || race.equals("Angel")) {
				           	if(HolyDonationValue.get(donation.getType()) != null) {
								String[] playerdata = PlayerData.get(player.getUniqueId());
								double magic = Double.parseDouble(playerdata[1]) + (HolyDonationValue.get(donation.getType())*5*donation.getAmount());
								if(magic>10000) {
									magic=10000;
								}
								e.getView().setItem(11, new ItemStack(Material.AIR));
								playerdata[1] = ("" + magic);
								PlayerData.put(player.getUniqueId(), playerdata);
				                ItemStack cursor = player.getItemOnCursor();
				                player.updateInventory();
				                player.setItemOnCursor(cursor);
				           	} else {
				           		player.getInventory().addItem(donation);
								e.getView().setItem(11, new ItemStack(Material.AIR));
				                ItemStack cursor = player.getItemOnCursor();
				                player.updateInventory();
				                player.setItemOnCursor(cursor);
				           	}
			           	} else {
			        		player.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getConfig().getString("Messages.holy-donate-only").replaceAll("%prefix%", main.getConfig().getString("Messages.prefix"))));
			           		player.getInventory().addItem(donation);
							e.getView().setItem(11, new ItemStack(Material.AIR));
			                ItemStack cursor = player.getItemOnCursor();
			                player.updateInventory();
			                player.setItemOnCursor(cursor);
			           	}
					}
                }
            }, 1L);
        }
    }
    
    @EventHandler()
    public void UnholyAlterDonationLogic(InventoryClickEvent e) {
    	if(e.getClickedInventory() == null || e.getWhoClicked().getInventory() == null) return;
        if (e.getView().getTitle().contains("Unholy Altar")) {
        	Player player = (Player) e.getWhoClicked();
           	String[] newData = PlayerData.get(player.getUniqueId());
        	String race = newData[0];
            this.getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
                public void run() {
					if(e.getView().getItem(10) != null) {
			            ItemStack donation = e.getView().getItem(10);
			           	if(race.equals("Necromancer")) {
				           	if(UnholyDonationValue.get(donation.getType()) != null) {
								String[] playerdata = PlayerData.get(player.getUniqueId());
								double magic = Double.parseDouble(playerdata[1]) + (UnholyDonationValue.get(donation.getType())*5*donation.getAmount());
								if(magic>10000) {
									magic=10000;
								}
								e.getView().setItem(10, new ItemStack(Material.AIR));
								playerdata[1] = ("" + magic);
								PlayerData.put(player.getUniqueId(), playerdata);
				                ItemStack cursor = player.getItemOnCursor();
				                player.updateInventory();
				                player.setItemOnCursor(cursor);
				           	} else {
				           		player.getInventory().addItem(donation);
								e.getView().setItem(10, new ItemStack(Material.AIR));
				                ItemStack cursor = player.getItemOnCursor();
				                player.updateInventory();
				                player.setItemOnCursor(cursor);
				           	}
			           	} else {
			        		player.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getConfig().getString("Messages.necromancer-donate-only").replaceAll("%prefix%", main.getConfig().getString("Messages.prefix"))));
			           		player.getInventory().addItem(donation);
							e.getView().setItem(10, new ItemStack(Material.AIR));
			                ItemStack cursor = player.getItemOnCursor();
			                player.updateInventory();
			                player.setItemOnCursor(cursor);
			           	}
					}
                }
            }, 1L);
        }
    }
    
    @EventHandler()
    public void WitchHunterBoardLogic(InventoryClickEvent e) {
    	if(e.getClickedInventory() == null || e.getWhoClicked().getInventory() == null) return;
        if (e.getClickedInventory().equals(e.getWhoClicked().getInventory())) {
            return;
        }
        if(e.getView().getTitle().contains("Witch Hunter Board")) {
            e.setCancelled(true);
            if (e.getSlot() == 11) {
        		Player player = (Player) e.getWhoClicked();
            	String[] newData = PlayerData.get(player.getUniqueId());
            	String race = newData[0];
            	if(race.equals("WitchHunter")) {
            		openBountyMenu(player);
            	} else {
	        		player.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getConfig().getString("Messages.witchhunter-use-only").replaceAll("%prefix%", main.getConfig().getString("Messages.prefix"))));
	           		return;
            	}
            }
            if (e.getSlot() == 15) {
        		Player player = (Player) e.getWhoClicked();
        		if(setRace(player, "WitchHunter", true)) {
        	    	e.setCancelled(true);
        		}
            }
        }
    }
	
	public void openHolyAltar(Player p) {
    	Inventory inv = Bukkit.createInventory(null, 27, "Holy Altar");
    	ItemStack filler = new ItemStack(Material.ORANGE_STAINED_GLASS_PANE);
    	ItemMeta meta = filler.getItemMeta();
    	meta.setDisplayName(" ");
    	filler.setItemMeta(meta);

    	ItemStack donate = new ItemStack(Material.YELLOW_STAINED_GLASS_PANE);
    	meta = donate.getItemMeta();
    	meta.setDisplayName(ChatColor.GOLD + "Donate to the Church");
    	donate.setItemMeta(meta);
    	ItemStack priest = new ItemStack(Material.YELLOW_STAINED_GLASS_PANE);
    	meta = priest.getItemMeta();
    	meta.setDisplayName(ChatColor.GOLD + "Become a Priest");
    	priest.setItemMeta(meta);
    	for(int i=0; i<inv.getSize(); i++) {
    		if(i!=11 && i!=15) {
        		inv.setItem(i, filler);
    		}
    		if(i==1 || i==2 || i==3 || i==10 || i==12 || i==19 || i==20 || i==21) {
        		inv.setItem(i, donate);
    		}
    		if(i==5 || i==6 || i==7 || i==14 || i==16 || i==23 || i==24 || i==25) {
        		inv.setItem(i, priest);
    		}
    	}
    	inv.setItem(15, CustomItems.get(9));
    	p.openInventory(inv);
    }
	
	public void openUnholyAltar(Player p) {
    	Inventory inv = Bukkit.createInventory(null, 27, "Unholy Altar");
    	ItemStack donate = new ItemStack(Material.ORANGE_STAINED_GLASS_PANE);
    	ItemMeta meta = donate.getItemMeta();
    	meta.setDisplayName(ChatColor.GOLD + "Donate to God of Death");
    	donate.setItemMeta(meta);
    	ItemStack vampire = new ItemStack(Material.RED_STAINED_GLASS_PANE);
    	meta = vampire.getItemMeta();
    	meta.setDisplayName(ChatColor.DARK_RED + "Become a Vampire");
    	vampire.setItemMeta(meta);
    	ItemStack necromancer = new ItemStack(Material.ORANGE_STAINED_GLASS_PANE);
    	meta = necromancer.getItemMeta();
    	meta.setDisplayName(ChatColor.GOLD + "Become a Necromancer");
    	necromancer.setItemMeta(meta);
    	for(int i=0; i<inv.getSize(); i++) {
    		if(i==0 || i==1 || i==2 || i==9 || i==11 || i==18 || i==19 || i==20) {
        		inv.setItem(i, donate);
    		}
    		if(i==3 || i==4 || i==5 || i==12 || i==14 || i==21 || i==22 || i==23) {
        		inv.setItem(i, vampire);
    		}
    		if(i==6 || i==7 || i==8 || i==15 || i==17 || i==24 || i==25 || i==26) {
        		inv.setItem(i, necromancer);
    		}
    	}
    	inv.setItem(13, CustomItems.get(0));
    	inv.setItem(16, CustomItems.get(11));
    	p.openInventory(inv);
    }
	
	public void openWitchHunterBoard(Player p) {
    	Inventory inv = Bukkit.createInventory(null, 27, "Witch Hunter Board");
    	ItemStack filler = new ItemStack(Material.PURPLE_STAINED_GLASS_PANE);
    	ItemMeta meta = filler.getItemMeta();
    	meta.setDisplayName(" ");
    	filler.setItemMeta(meta);
    	for(int i=0; i<inv.getSize(); i++) {
    		inv.setItem(i, filler);
    	}
    	
    	ItemStack bountymenu = new ItemStack(Material.LIGHT_BLUE_STAINED_GLASS_PANE);
    	meta = bountymenu.getItemMeta();
    	meta.setDisplayName(ChatColor.GOLD + "Open Bounty Menu");
    	bountymenu.setItemMeta(meta);
    	ItemStack witchhunter = new ItemStack(Material.LIGHT_BLUE_STAINED_GLASS_PANE);
    	meta = witchhunter.getItemMeta();
    	meta.setDisplayName(ChatColor.GOLD + "Become a Witch Hunter");
    	witchhunter.setItemMeta(meta);
    	for(int i=0; i<inv.getSize(); i++) {
    		if(i!=11 && i!=15) {
        		inv.setItem(i, filler);
    		}
    		if(i==1 || i==2 || i==3 || i==10 || i==12 || i==19 || i==20 || i==21) {
        		inv.setItem(i, bountymenu);
    		}
    		if(i==5 || i==6 || i==7 || i==14 || i==16 || i==23 || i==24 || i==25) {
        		inv.setItem(i, witchhunter);
    		}
    	}
    	inv.setItem(11, CustomItems.get(15));
    	inv.setItem(15, CustomItems.get(13));
    	p.openInventory(inv);
    }
	
	@EventHandler
    public void BountyMenuLogic(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if(BountyPages.containsKey(player)) {
            List<Inventory> pages = BountyPages.get(player);
            if(!pages.isEmpty()) {
            	for(Inventory pagetest : pages) {
            		if (event.getInventory().equals(pagetest)) {
            	        event.setCancelled(true);
            	        if (event.getCurrentItem().getItemMeta().getDisplayName().contains("Close Menu")) {
            	            player.closeInventory();
            	            return;
            	        }
            	        
            	        if(event.getCurrentItem().getItemMeta().getDisplayName().contains("Next")) {
            	        	int page = Integer.parseInt(ChatColor.stripColor(player.getOpenInventory().getItem(49).getItemMeta().getLore().get(1).replace("Page: ", "")));
            	        	player.closeInventory();
            	        	player.openInventory(pages.get(page));
            	        }
            	        
            	        if(event.getCurrentItem().getItemMeta().getDisplayName().contains("Previous")) {
            	        	int page = Integer.parseInt(ChatColor.stripColor(player.getOpenInventory().getItem(49).getItemMeta().getLore().get(1).replace("Page: ", "")));
            	        	player.closeInventory();
            	        	player.openInventory(pages.get(page-2));
            	        }
            	        return;
            		}
            	}
            }
        }
    }
	
	@SuppressWarnings("deprecation")
	public void openBountyMenu(Player p) {
		p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6Opening Bounty Menu..."));
		List<Inventory> pages = new ArrayList<Inventory>();
        //Setup Variables
        int slot = 0;
        int guisize = 54;
        int invnum=0;
        String title = ChatColor.translateAlternateColorCodes('&', "&8&lBounty Menu");
        ItemStack item = new ItemStack(Material.BARRIER);
        ItemMeta meta = item.getItemMeta();
        List < String > lore = new ArrayList < String > ();
		List<ItemStack> heads = new ArrayList<ItemStack>();
		for(UUID uuid : PlayerBounties.keySet()) {
			Player player = Bukkit.getPlayer(uuid);
			ItemStack head = new ItemStack(Material.PLAYER_HEAD);
			SkullMeta sm = (SkullMeta) head.getItemMeta();
			sm.setDisplayName(ChatColor.GOLD + player.getName());
			lore = new ArrayList < String > ();
			lore.add(ChatColor.YELLOW + "Bounty: $" + PlayerBounties.get(uuid));
			sm.setLore(lore);
			sm.setOwner(player.getName());
			head.setItemMeta(sm);
			heads.add(head);
		}
		
		//Generate Pages
		
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
	        
	        if(heads.size() == 0) {
	        	item = new ItemStack(Material.RED_STAINED_GLASS);
	        	meta = item.getItemMeta();
	        	meta.setDisplayName(ChatColor.DARK_RED + "No Bounties to Display");
	        	item.setItemMeta(meta);
	        	page.setItem(22, item);
	        } else {
	        	//Add bounties to page
		        slot = 0;
		        while(slot < 45 && heads.size() > 0) {
			        item = heads.get(0);
			        heads.remove(0);
		            page.setItem(slot, item);
		            slot += 1;
	        	}
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
	        
	        if(heads.size()!=0) {
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
    		pages.add(page);
	        invnum += 1;
        } while(heads.size() > 0);
        p.openInventory(pages.get(0));
        BountyPages.put(p, pages);
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

	/**
     * Commands
     */
	
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    	if (label.equalsIgnoreCase("supernatural") || label.equalsIgnoreCase("sn")) {
            if (args.length > 0) {
            	//Commands
	            if (args[0].equalsIgnoreCase("help") || args[0].equalsIgnoreCase("?")) {
	            	HelpCommand(sender, label, args);
                	return true;
                }
	            if (args[0].equalsIgnoreCase("version") || args[0].equalsIgnoreCase("ver")) {
	            	VersionCommand(sender, label, args);
                	return true;
	            }
                if (args[0].equalsIgnoreCase("reload") || args[0].equalsIgnoreCase("rl")) {
	            	ReloadCommand(sender, label, args);
                	return true;
                }
                if (args[0].equalsIgnoreCase("setclass") || args[0].equalsIgnoreCase("sc")) {
                    SetClassCommand(sender, label, args);
                	return true;
                }
                if (args[0].equalsIgnoreCase("setmagic") || args[0].equalsIgnoreCase("sm")) {
                	SetMagicCommand(sender, label, args);
                	return true;
                }
                if (args[0].equalsIgnoreCase("class") || args[0].equalsIgnoreCase("classes")) {
                	ClassInformationCommand(sender, label, args);
                	return true;
                }
                if (args[0].equalsIgnoreCase("setbanishlocation") || args[0].equalsIgnoreCase("setbanish") || args[0].equalsIgnoreCase("banishlocation") || args[0].equalsIgnoreCase("sbl") || args[0].equalsIgnoreCase("sb") || args[0].equalsIgnoreCase("bl")) {
                    SetBanishLocationCommand(sender, label, args);
                	return true;
                }
                System.out.println(this.getConfig().getString("Messages.unknown-command"));
                return false;
            }
        	HelpCommand(sender, label, args);
        	return true;
        }
    	if (label.equalsIgnoreCase("bounty")) {
            if (args.length > 0) {
            	//Commands
	            if (args[0].equalsIgnoreCase("add") || args[0].equalsIgnoreCase("set")) {
	            	AddBountyCommand(sender, label, args);
                	return true;
                }
                if (args[0].equalsIgnoreCase("check") || args[0].equalsIgnoreCase("view") || args[0].equalsIgnoreCase("see") || args[0].equalsIgnoreCase("open")) {
	            	CheckBountyCommand(sender, label, args);
                	return true;
                }
                System.out.println(this.getConfig().getString("Messages.unknown-command"));
                return false;
            }
            CheckBountyCommand(sender, label, args);
        	return true;
        }
		return false;
    }

    public void VersionCommand(CommandSender sender, String label, String[] args) {
    	if(sender instanceof Player) {
    		Player player = (Player) sender;
    		PluginDescriptionFile pdf = this.getDescription();
        	if (player.hasPermission("supernatural.getversion")) {
		        player.sendMessage(ChatColor.WHITE + pdf.getName() + " Version: " + ChatColor.DARK_GRAY + "[" + ChatColor.WHITE + pdf.getVersion() + ChatColor.DARK_GRAY + "]");
        	}
    	}
    }
    
    public void HelpCommand(CommandSender sender, String label, String[] args) {
    	if(sender instanceof Player) {
    		Player player = (Player) sender;
    		PluginDescriptionFile pdf = this.getDescription();
        	if (player.hasPermission("supernatural.help")) {
		    	player.sendMessage(ChatColor.GRAY + "-------------------");
		        player.sendMessage(ChatColor.GREEN + pdf.getName() + " Help");
		        player.sendMessage(ChatColor.GRAY + "-------------------");
		        if (player.hasPermission("supernatural.help")) {
		            player.sendMessage(ChatColor.GREEN + "/sn help");
		        }
		        if (player.hasPermission("supernatural.getversion")) {
		            player.sendMessage(ChatColor.GREEN + "/sn version");
		        }
		        if (player.hasPermission("supernatural.reload")) {
		            player.sendMessage(ChatColor.GREEN + "/sn reload");
		        }
		        if (player.hasPermission("supernatural.setclass")) {
		            player.sendMessage(ChatColor.GREEN + "/sn setclass");
		        }
		        if (player.hasPermission("supernatural.setmagic")) {
		            player.sendMessage(ChatColor.GREEN + "/sn setmagic");
		        }
		        if (player.hasPermission("supernatural.classes")) {
		            player.sendMessage(ChatColor.GREEN + "/sn class <class>");
		        }
		        if (player.hasPermission("supernatural.setbanishlocation")) {
		            player.sendMessage(ChatColor.GREEN + "/sn sbl");
		        }
		        if (player.hasPermission("supernatural.addbounty")) {
		            player.sendMessage(ChatColor.GREEN + "/bounty add <target> <bounty>");
		        }
		        if (player.hasPermission("supernatural.checkbounty")) {
		            player.sendMessage(ChatColor.GREEN + "/bounty check OR /bounty check <target>");
		        }
		    } else {
		        player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.insufficient-permission")));
		    }
    	} else {
    		System.out.print("-------------------");
    		System.out.print("Supernatural Commands");
    		System.out.print("-------------------");
    		System.out.print("/sn help");
    		System.out.print("/sn reload");
    		System.out.print("/sn setclass");
    		System.out.print("/sn setmagic");
    		System.out.print("/sn class <class>");
    		System.out.print("/sn sbl");
    		System.out.print("/bounty add <target> <bounty>");
    		System.out.print("/bounty check OR /bounty check <target>");
    	}
    	return;
    }

    public void ReloadCommand(CommandSender sender, String label, String[] args) {
    	if(sender instanceof Player) {
    		Player player = (Player) sender;
            if (player.hasPermission("supernatural.reload")) {
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
    
    public void SetClassCommand(CommandSender sender, String label, String[] args) {
    	if(sender instanceof Player) {
    		Player player = (Player) sender;
            if (player.hasPermission("supernatural.setclass")) {
				if (Bukkit.getPlayer(args[1]) != null) {
					Player target = Bukkit.getPlayer(args[1]);
					setRaceDirect(target, args[2]);
				} else {
					setRaceDirect(player, args[1]);
				}
            } else {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.insufficient-permission").replaceAll("%prefix%", this.getConfig().getString("Messages.prefix"))));
            }
    	} else {
            System.out.println(this.getConfig().getString("Messages.no-console"));
    	}	
    }

    public void SetMagicCommand(CommandSender sender, String label, String[] args) {
    	if(sender instanceof Player) {
    		Player player = (Player) sender;
            if (player.hasPermission("supernatural.setmagic")) {
				if (Bukkit.getPlayer(args[1]) != null) {
	            	Player target = Bukkit.getPlayer(args[1]);
	            	String[] newData = PlayerData.get(target.getUniqueId());
	            	newData[1] = args[2];
	            	PlayerData.put(target.getUniqueId(), newData);
				} else {
	            	String[] newData = PlayerData.get(player.getUniqueId());
	            	newData[1] = args[1];
	            	PlayerData.put(player.getUniqueId(), newData);
				}
            } else {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.insufficient-permission")));
            }
    	} else {
            System.out.println(this.getConfig().getString("Messages.no-console"));
    	}	
    }

    public void ClassInformationCommand(CommandSender sender, String label, String[] args) {
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
    		        player.sendMessage(ChatColor.GREEN + "1. Water Breathing [" + this.getConfig().getInt("SpellCosts.Water-Breathing")*20 + " Magika / Second]");
    		        player.sendMessage(ChatColor.GREEN + "2. Regeneration [" + this.getConfig().getInt("SpellCosts. Regeneration") + " Magika / Heart]");
    		        player.sendMessage(ChatColor.GREEN + "3. Teleportation [" + this.getConfig().getInt("SpellCosts.Teleport") + " Magika]");
    		        player.sendMessage(ChatColor.GREEN + "4. High Jump [" + this.getConfig().getInt("SpellCosts.High-Jump") + " Magika]");
    		        player.sendMessage(ChatColor.GREEN + "4. Create Blood Vial [" + this.getConfig().getInt("SpellCosts.BloodVial") + " Magika]");
    		        player.sendMessage(ChatColor.GREEN + "5. Create Bloodrose Potion [" + this.getConfig().getInt("SpellCosts.Bloodrose") + " Magika & 10 Hearts]");
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
    		        player.sendMessage(ChatColor.GREEN + "1. Summon Wolf Pet [" + this.getConfig().getInt("SpellCosts.Summon-Wolf") + " Magika]");
    		        player.sendMessage(ChatColor.GREEN + "2. Dash [" + this.getConfig().getInt("SpellCosts.Dash") + " Magika]");
    		        player.sendMessage(ChatColor.GREEN + "3. Create Moonflower Potion [" + this.getConfig().getInt("SpellCosts.Moonflower") + " Magika & 10 Hearts]");
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
    		        player.sendMessage(ChatColor.GREEN + "1. Summon Undead [" + this.getConfig().getInt("SpellCosts.Summon-Monster") + " Magika]");
    		        player.sendMessage(ChatColor.GREEN + "2. Unholy Bond [" + this.getConfig().getInt("SpellCosts.Unholy-Bond") + " Magika]");
    		        player.sendMessage(ChatColor.GREEN + "3. Create Ghoulish Potion [" + this.getConfig().getInt("SpellCosts.Ghoulish") + " Magika & 10 Hearts]");
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
    		        player.sendMessage(ChatColor.GREEN + "1. Fireball [" + this.getConfig().getInt("SpellCosts.Fireball") + " Magika]");
    		        player.sendMessage(ChatColor.GREEN + "2. Explosion [" + this.getConfig().getInt("SpellCosts.Explosion") + " Magika]");
    		        player.sendMessage(ChatColor.GREEN + "3. Summon Strider [" + this.getConfig().getInt("SpellCosts.Summon-Strider") + " Magika]");
    		        player.sendMessage(ChatColor.GREEN + "4. Snare [" + this.getConfig().getInt("SpellCosts.Snare") + " Magika]");
    		        player.sendMessage(ChatColor.GREEN + "5. Create Hellash Potion [" + this.getConfig().getInt("SpellCosts.Hellish") + " Magika & 10 Hearts]");
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
    		        player.sendMessage(ChatColor.GREEN + "1. Banish Supernatural [" + this.getConfig().getInt("SpellCosts.Banish") + " Magika]");
    		        player.sendMessage(ChatColor.GREEN + "2. Exorcise Supernatural [" + this.getConfig().getInt("SpellCosts.Exorcise") + " Magika]");
    		        player.sendMessage(ChatColor.GREEN + "3. Drain Supernatural [" + this.getConfig().getInt("SpellCosts.Drain") + " Magika]");
    		        player.sendMessage(ChatColor.GREEN + "4. Heal Human [" + this.getConfig().getInt("SpellCosts.Heal-Human") + " Magika]");
    		        player.sendMessage(ChatColor.GREEN + "5. Guardian Angel [" + this.getConfig().getInt("SpellCosts.Guardian-Angel") + " Magika]");
    		        player.sendMessage(ChatColor.GREEN + "6. Create Supernatural Cure [" + this.getConfig().getInt("SpellCosts.Cure") + " Magika]");
    		        player.sendMessage(ChatColor.GREEN + "7. Create Holy Book [" + this.getConfig().getInt("SpellCosts.Holy-Book") + " Magika & 10 Hearts]");
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
    		        player.sendMessage(ChatColor.GREEN + "1. Summon Skeleton [" + this.getConfig().getInt("SpellCosts.Summon-Skeleton") + " Magika]");
    		        player.sendMessage(ChatColor.GREEN + "2. Summon Undead [" + this.getConfig().getInt("SpellCosts.Summon-Undead") + " Magika]");
    		        player.sendMessage(ChatColor.GREEN + "3. Heal-Undead [" + this.getConfig().getInt("SpellCosts.Heal-Undead") + " Magika]");
    		        player.sendMessage(ChatColor.GREEN + "4. Ressurection Spawn [" + this.getConfig().getInt("SpellCosts.Ressurection-Spawn") + " Magika]");
    		        player.sendMessage(ChatColor.GREEN + "5. Create Book of Death [" + this.getConfig().getInt("SpellCosts.Book-Of-Death") + " Magika & 10 Hearts]");
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
    		        player.sendMessage(ChatColor.GREEN + "1. Triple Arrow [" + this.getConfig().getInt("SpellCosts.Triple-Arrow") + " Magika]");
    		        player.sendMessage(ChatColor.GREEN + "2. Grapple Arrow [" + this.getConfig().getInt("SpellCosts.Grapple-Arrow") + " Magika]");
    		        player.sendMessage(ChatColor.GREEN + "3. Fire Arrow [" + this.getConfig().getInt("SpellCosts.Fire-Arrow") + " Magika]");
    		        player.sendMessage(ChatColor.GREEN + "4. Power Arrow [" + this.getConfig().getInt("SpellCosts.Power-Arrow") + " Magika]");
    		        player.sendMessage(ChatColor.GREEN + "5. Volley Arrow [" + this.getConfig().getInt("SpellCosts.Volley-Arrow") + " Magika]");
    		        player.sendMessage(ChatColor.GREEN + "5. Book of the WitchHunter [" + this.getConfig().getInt("SpellCosts.Book-Of-WitchHunter") + " Magika & 10 Hearts]");
    		        player.sendMessage(ChatColor.DARK_GREEN + "Passive Abilities");
    		        player.sendMessage(ChatColor.GREEN + "1. 1/3 Fall Damage");
    		        player.sendMessage(ChatColor.DARK_GREEN + "Weaknesses");
    		        player.sendMessage(ChatColor.GREEN + "1. Can only wear Leather Armour");
    		        player.sendMessage(ChatColor.GREEN + "2. Cannot use Melee Weapons");
    		        return;
        		}
		    } else {
		        player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.insufficient-permission")));
		    }
    	}
    	return;
    }

    public void SetBanishLocationCommand(CommandSender sender, String label, String[] args) {
    	if(sender instanceof Player) {
    		Player player = (Player) sender;
            if (player.hasPermission("supernatural.setbanishlocation")) {
            	banishlocation = player.getLocation();
            } else {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.insufficient-permission")));
            }
    	} else {
            System.out.println(this.getConfig().getString("Messages.no-console"));
    	}	
    }

    public void AddBountyCommand(CommandSender sender, String label, String[] args) {
    	if(sender instanceof Player) {
    		Player player = (Player) sender;
            if (player.hasPermission("supernatural.addbounty")) {
				if (Bukkit.getPlayer(args[1]) != null) {
					Player target = Bukkit.getPlayer(args[1]);
					if(isSupernatural(target)) {
						if(!player.getUniqueId().equals(target.getUniqueId())) {
							try {
								double bounty = Integer.parseInt(args[2]);
								if(bounty <= 0) {
									player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.invalid-bounty").replaceAll("%prefix%", this.getConfig().getString("Messages.prefix"))));
									return;
								}
								if(eco.getBalance(player) < bounty) {
									player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.insufficient-balance").replaceAll("%prefix%", this.getConfig().getString("Messages.prefix"))));
									return;
								}
		                        eco.withdrawPlayer(player, bounty);
								double tax = 0.12;
								bounty = bounty - bounty*tax;
								player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.bounty-notify-sender").replaceAll("%prefix%", this.getConfig().getString("Messages.prefix")).replaceAll("%bounty%", ("" + bounty)).replaceAll("%target%", target.getName()).replaceAll("%player%", player.getName()).replaceAll("%percenttax%", ("" + (tax*100)))));
								target.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.bounty-notify-target").replaceAll("%prefix%", this.getConfig().getString("Messages.prefix")).replaceAll("%bounty%", ("" + bounty)).replaceAll("%target%", target.getName()).replaceAll("%player%", player.getName()).replaceAll("%percenttax%", ("" + (tax*100)))));
								for(World w : Bukkit.getWorlds()) {
									for(Player p : w.getPlayers()) {
										if(getRace(p).equalsIgnoreCase("WitchHunter")) {
											if(!p.getUniqueId().equals(player.getUniqueId())) {
												p.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.bounty-notify-all").replaceAll("%prefix%", this.getConfig().getString("Messages.prefix")).replaceAll("%bounty%", ("" + bounty)).replaceAll("%target%", target.getName()).replaceAll("%player%", player.getName()).replaceAll("%percenttax%", ("" + (tax*100)))));
											}
										}
									}
								}
								if(PlayerBounties.containsKey(target.getUniqueId())) {
									bounty += PlayerBounties.get(target.getUniqueId());
								}
								PlayerBounties.put(target.getUniqueId(), bounty);
							} catch(Exception e) {
								player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.invalid-bounty").replaceAll("%prefix%", this.getConfig().getString("Messages.prefix"))));
							}
						} else {
							player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.no-bounty-on-self").replaceAll("%prefix%", this.getConfig().getString("Messages.prefix"))));
							}
					} else {
						player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.target-must-be-supernatural").replaceAll("%prefix%", this.getConfig().getString("Messages.prefix"))));
					}
				} else {
					player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.must-specify-player").replaceAll("%prefix%", this.getConfig().getString("Messages.prefix"))));
				}
            } else {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.insufficient-permission").replaceAll("%prefix%", this.getConfig().getString("Messages.prefix"))));
            }
    	} else {
            System.out.println(this.getConfig().getString("Messages.no-console"));
    	}	
    }

    public void CheckBountyCommand(CommandSender sender, String label, String[] args) {
    	if(sender instanceof Player) {
    		Player player = (Player) sender;
            if (player.hasPermission("supernatural.checkbounty")) {
            	if (args.length > 1 && Bukkit.getPlayer(args[1]) != null) {
	            	if(getRace(player).equalsIgnoreCase("WitchHunter")) {
						Player target = Bukkit.getPlayer(args[1]);
						if(PlayerBounties.containsKey(target.getUniqueId())) {
							double bounty = PlayerBounties.get(target.getUniqueId());
			                player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.bounty-get").replaceAll("%prefix%", this.getConfig().getString("Messages.prefix")).replaceAll("%bounty%", (""+bounty)).replaceAll("%target%", target.getName())));
						} else {
			                player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.no-bounty").replaceAll("%prefix%", this.getConfig().getString("Messages.prefix"))));
						}
	            	} else {
	            		player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.witchhunter-only-command").replaceAll("%prefix%", this.getConfig().getString("Messages.prefix"))));
	            	}
				} else {
	            	if(getRace(player).equalsIgnoreCase("WitchHunter")) {
		            	openBountyMenu(player);
	            	} else {
	            		player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.witchhunter-only-command").replaceAll("%prefix%", this.getConfig().getString("Messages.prefix"))));
	            	}
	            }
			}
    	}
    }
}