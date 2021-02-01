package me.LoneSurvivor.Supernatural;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.IronGolem;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.entity.SmallFireball;
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
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import me.LoneSurvivor.Supernatural.ArmourEquptEvent.*;
import me.LoneSurvivor.Supernatural.repositories.Constants;
import me.LoneSurvivor.Supernatural.Files.DataManager;
import me.LoneSurvivor.Supernatural.commands.Commands;
import me.LoneSurvivor.Supernatural.Classes.Vampire.ActiveAbilities.*;
import me.LoneSurvivor.Supernatural.Classes.Werewolf.ActiveAbilities.*;
import me.LoneSurvivor.Supernatural.Classes.Ghoul.ActiveAbilities.*;
import me.LoneSurvivor.Supernatural.Classes.Demon.ActiveAbilities.*;
import me.LoneSurvivor.Supernatural.Classes.Priest.ActiveAbilities.*;
import me.LoneSurvivor.Supernatural.Classes.Necromancer.ActiveAbilities.*;
import me.LoneSurvivor.Supernatural.Classes.WitchHunter.ActiveAbilities.*;
import me.LoneSurvivor.Supernatural.Classes.Angel.ActiveAbilities.*;
import net.minecraft.server.v1_16_R3.ItemArmor;
import net.minecraft.server.v1_16_R3.PacketPlayOutEntityDestroy;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.minecraft.server.v1_16_R3.EntityInsentient;

import org.bukkit.craftbukkit.v1_16_R3.entity.CraftArrow;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;

public class Supernatural extends JavaPlugin implements Listener {
    private Economy eco;
    private Chat chat;
    Boolean VaultChat = true;
    Map < UUID, Map < UUID, Integer > > UnholyBond = new HashMap < UUID, Map < UUID, Integer > > ();
    public DataManager data = new DataManager(this);
    public Constants constants = new Constants(this);
    FileConfiguration config = this.getConfig();
    Map < Player, Boolean > SpellbarOpen = new HashMap < Player, Boolean > ();
    Map < Player, Inventory > Hotbars = new HashMap < Player, Inventory > ();
    Map < Player, BossBar > MagicBar = new HashMap < Player, BossBar> ();
    Map < Player, Boolean  > Regeneration = new HashMap < Player, Boolean> ();
    Map < Player, Boolean  > WaterBreathing = new HashMap < Player, Boolean> ();
    Map < Player, Integer  > ArrowType = new HashMap < Player, Integer> ();
    Map < Player, Arrow > GrappleArrows = new HashMap <Player, Arrow> ();
    Map < Player, Location > GrappleLocation = new HashMap <Player, Location> ();
    Map < Player, Arrow > GrappleArrows2 = new HashMap <Player, Arrow> ();
    Map < Player, List<Inventory> > BountyPages = new HashMap < Player, List<Inventory> >();
    Map < UUID, Map < String, Integer > > Cooldowns = new HashMap < UUID, Map < String, Integer > >();
    List<UUID> PlayerDropItemOnTick = new ArrayList<UUID>();
    HashMap<Player, ItemStack> arrowReplacedItem = new HashMap<Player, ItemStack>();
    
    /**
     * Setup
     */
    
	public void onEnable() {
        //this.data = new DataManager(this);
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

		//Init commands
		Commands commands = new Commands(this, eco);
		getCommand("supernatural").setExecutor(commands);
		getCommand("sn").setExecutor(commands);
		getCommand("bounty").setExecutor(commands);
        
		if(!setupArmourEquipEvent()) {
        	this.getLogger().log(Level.SEVERE, "Setup Armour Equip Event Failed");
        	getServer().getPluginManager().disablePlugin(this);
		}
		
	    //Setup Repeating Tasks
    	this.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
    		int tick=0;
            public void run() {
            	tick+=1;
            	Followers(tick);
            	for(World world : Bukkit.getWorlds()) {
            		for(Player player : world.getPlayers()) {
            			//tick frequency non critical
            			if(tick % config.getInt("RunnableFrequency.updateMagicDisplay") == 0) updateMagicDisplay(player); //error here?
            			if(tick % config.getInt("RunnableFrequency.NightVision") == 0) NightVision(player);
            			if(tick % config.getInt("RunnableFrequency.Speed") == 0) Speed(player);
            			if(tick % config.getInt("RunnableFrequency.ejectIlligalArmour") == 0) ejectIlligalArmour(player);
            			if(tick % config.getInt("RunnableFrequency.immovableSpells") == 0) immovableSpells(player);
            			if(tick % config.getInt("RunnableFrequency.HostileAnimals") == 0) HostileAnimals();
            			if(tick % config.getInt("RunnableFrequency.GrappleArrowTeleport") == 0) GrappleArrowStage2(player);
        				//if(tick % config.getInt("RunnableFrequency.LavaSwimStartStop") == 0) lavaswimstartstop(player);
            			//tick frequency critical 
            			if(tick % config.getInt("Spells.Vampire.Regeneration.Frequency") == 0) Regeneration(player);
        				if(tick % config.getInt("Spells.Vampire.WaterBreathing.Frequency") == 0) WaterBreathing(player); 		
        				if(tick % 20 == 0) MagicFromHeat(player);
            			if(tick % 20 == 0) waterContact(player);
            			inSunlight(player, tick);       		
        				UnholyBondTimer();
        				tickCooldowns();
            		}
            	}
            }
        }, 0L, 1L);
	}
	
	public void onDisable() {
		for(World world : Bukkit.getServer().getWorlds()) {
			for(Player player : world.getPlayers()) {
				if(player.getInventory().getItem(8).equals(constants.getSpellIcons().get("Abilities"))) {
					CloseSpellbar(player);
				}
				if(MagicBar.containsKey(player)) {
					MagicBar.get(player).removePlayer(player);
				}
			}
		}
	}

	/* Lava Swimming Test
	List<UUID> swimming = new ArrayList<UUID>();
	
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
	
	private boolean setupChat() {
        RegisteredServiceProvider<Chat> chatProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.chat.Chat.class);
        if (chatProvider != null) {
            chat = chatProvider.getProvider();
        }
        return (chat != null);
    }
	
	private boolean setupEconomy() {
        RegisteredServiceProvider<Economy> rsp = Bukkit.getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp != null) {
            eco = rsp.getProvider();
        }
        return eco != null;
    }
 	
	private boolean setupArmourEquipEvent() {
        //Setup ArmorEquipEvent
		try {
			getServer().getPluginManager().registerEvents(new ArmorListener(getConfig().getStringList("blocked")), this);
			Class.forName("org.bukkit.event.block.BlockDispenseArmorEvent");
			getServer().getPluginManager().registerEvents(new DispenserArmorListener(), this);
			return true;
		} catch(Exception e) {
			return false;
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
	
	public void updateMagicDisplay(Player player) {
		double magic = getMagic(player);
		if(!MagicBar.containsKey(player)) {
			BossBar bar = Bukkit.createBossBar("Magika: " + (int) magic, BarColor.BLUE, BarStyle.SOLID);
			MagicBar.put(player, bar);
			bar.addPlayer(player);
		}
		MagicBar.get(player).setTitle("Magika: " + (int) magic);
	}

	//Getting and setting to and from data file
	
	public List<String> getGuardianAngelList() {
    	return data.getConfig().getStringList("GuardianAngel");
	}
	
	public Boolean addGuardianAngel(Player player) {
		List<String> GuardianAngelList = getGuardianAngelList();
		if(GuardianAngelList.contains(player.getUniqueId().toString())) {
			return false;
		} else {
			GuardianAngelList.add(player.getUniqueId().toString());
			data.getConfig().set("GuardianAngel", GuardianAngelList);
	        data.saveConfig();
			return true;
		}
	}
	
	public Boolean removeGuardianAngel(Player player) {
		List<String> GuardianAngelList = getGuardianAngelList();
		if(GuardianAngelList.contains(player.getUniqueId().toString())) {
			GuardianAngelList.remove(player.getUniqueId().toString());
			data.getConfig().set("GuardianAngel", GuardianAngelList);
	        data.saveConfig();
			return true;
		} else {
			return false;
		}
	}
	
	public String getRace(Player player) {
    	if (data.getConfig().getConfigurationSection("PlayerRace") != null) {
    	    if(data.getConfig().getConfigurationSection("PlayerRace").contains(player.getUniqueId().toString())) {
    	    	return data.getConfig().getString("PlayerRace." + player.getUniqueId().toString());
    	    }
    	}
        data.getConfig().set("PlayerRace." + player.getUniqueId().toString(), this.getConfig().getString("StartingClass"));
        data.saveConfig();
        return getRace(player);
	}
	
	public Boolean setRace(Player player, String race, Boolean cureFirst, Boolean giveItem) {
		//Makes sure inputted race is valid
		race = race.replaceAll(" ", "");
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
    		player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.unknown-race").replaceAll("%prefix%", this.getConfig().getString("Messages.prefix")).replaceAll("%class%", race.replaceAll("([^A-Z^_^ ])([A-Z])", "$1 $2"))));
			return false;
		}
    	if(getRace(player).equals(race)) {
    		player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.already-class").replaceAll("%prefix%", this.getConfig().getString("Messages.prefix")).replaceAll("%class%", race.replaceAll("([^A-Z^_^ ])([A-Z])", "$1 $2"))));
    		return false;
    	}
		if(cureFirst && isSupernatural(getRace(player))) {
	    	player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.cured-first").replaceAll("%prefix%", this.getConfig().getString("Messages.prefix")).replaceAll("%class%", race.replaceAll("([^A-Z^_^ ])([A-Z])", "$1 $2"))));
	    	return false;
		}
		if(giveItem) {
			if(race.equalsIgnoreCase("Vampire")) {
	    		player.getInventory().addItem(constants.getCustomItems().get("VampireBook"));
			}
			if(race.equalsIgnoreCase("Werewolf")) {
	    		player.getInventory().addItem(constants.getCustomItems().get("WerewolfBook"));
			}
			if(race.equalsIgnoreCase("Ghoul")) {
	    		player.getInventory().addItem(constants.getCustomItems().get("GhoulBook"));
			}
			if(race.equalsIgnoreCase("Demon")) {
	    		player.getInventory().addItem(constants.getCustomItems().get("DemonBook"));
			}
			if(race.equalsIgnoreCase("Priest")) {
    			player.getInventory().addItem(constants.getCustomItems().get("PriestBook"));
    		}
			if(race.equalsIgnoreCase("Necromancer")) {
    			player.getInventory().addItem(constants.getCustomItems().get("NecromancerBook"));
    		}
			if(race.equalsIgnoreCase("WitchHunter")) {
    			player.getInventory().addItem(constants.getCustomItems().get("WitchHunterBook"));
    		}
		}
        data.getConfig().set("PlayerRace." + player.getUniqueId().toString(), race);
        data.saveConfig(); 
		player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.setrace-success").replaceAll("%prefix%", this.getConfig().getString("Messages.prefix")).replaceAll("%class%", race.replaceAll("([^A-Z^_^ ])([A-Z])", "$1 $2"))));
		return true;
	}

	public int getMagic(Player player) {
    	if (data.getConfig().getConfigurationSection("PlayerMagic") != null) {
    	    if(data.getConfig().getConfigurationSection("PlayerMagic").contains(player.getUniqueId().toString())) {
    	    	return data.getConfig().getInt("PlayerMagic." + player.getUniqueId().toString());
    	    }
    	}
        data.getConfig().set("PlayerMagic." + player.getUniqueId().toString(), this.getConfig().getString("StartingMagic"));
        data.saveConfig();
        return getMagic(player);
	}
	
	public void setMagic(Player player, Integer magic, Boolean ignoreCap) {
		if(magic < 0) magic = 0;
		if(!ignoreCap && magic > this.getConfig().getInt("MagicCap")) magic = this.getConfig().getInt("MagicCap");
        data.getConfig().set("PlayerMagic." + player.getUniqueId().toString(), magic);
        data.saveConfig();
        return;
	}

	public int getRecruitingItems(Player player, String race) {
    	if (data.getConfig().contains("RecruitingItems." + player.getUniqueId().toString() + "." + race)) {
    	    return data.getConfig().getInt("RecruitingItems." + player.getUniqueId().toString() + "." + race);
    	}
    	data.getConfig().set("RecruitingItems." + player.getUniqueId().toString() + "." + race, 0);
        data.saveConfig();
        return getRecruitingItems(player, race);
	}
	
	public void setRecruitingItems(Player player, String race, Integer amount) {
    	data.getConfig().set("RecruitingItems." + player.getUniqueId().toString() + "." + race, amount);
    	data.saveConfig();
    	return;
	}
	
	public Location getTeleportLocation(Player player) {
    	if (data.getConfig().getConfigurationSection("TeleportLocation") != null) {
    	    if(data.getConfig().getConfigurationSection("TeleportLocation").contains(player.getUniqueId().toString())) {
    	    	String[] LPs = data.getConfig().getString("TeleportLocation." + player.getUniqueId().toString()).split(":");
    	    	Location location = new Location(Bukkit.getServer().getWorld(LPs[0]), Double.parseDouble(LPs[1]), Double.parseDouble(LPs[2]), Double.parseDouble(LPs[3]), Float.parseFloat(LPs[4]), Float.parseFloat(LPs[5]));
    	    	return location;
    	    }
    	}
        return null;
	}
	
	public void setTeleportLocation(Player player, Location location) {
	    data.getConfig().set("TeleportLocation." + player.getUniqueId().toString(), (location.getWorld().getName() + ":" + location.getBlockX() + ":" + location.getBlockY() + ":" + location.getBlockZ() + ":" + location.getYaw() + ":" + location.getPitch()));
	    data.saveConfig();
	    return;
	}
	
	public double getPlayerBounty(Player player) {
    	if (data.getConfig().getConfigurationSection("PlayerBounties") != null) {
    	    if(data.getConfig().getConfigurationSection("PlayerBounties").contains(player.getUniqueId().toString())) {
    	    	return data.getConfig().getDouble("PlayerBounties." + player.getUniqueId().toString());
    	    }
    	}
        data.getConfig().set("PlayerBounties." + player.getUniqueId().toString(), 0);
        data.saveConfig();
        return getPlayerBounty(player);
	}
	
	public double getPlayerBounty(UUID uuid) {
    	if (data.getConfig().getConfigurationSection("PlayerBounties") != null) {
    	    if(data.getConfig().getConfigurationSection("PlayerBounties").contains(uuid.toString())) {
    	    	return data.getConfig().getDouble("PlayerBounties." + uuid.toString());
    	    }
    	}
        data.getConfig().set("PlayerBounties." + uuid.toString(), 0);
        data.saveConfig();
        return getPlayerBounty(uuid);
	}
	
	public Map<UUID, Double> getPlayerBounties() {
		Map<UUID, Double> PlayerBounties = new HashMap<UUID, Double>();
    	if (data.getConfig().getConfigurationSection("PlayerBounties") != null) {
    	    data.getConfig().getConfigurationSection("PlayerBounties").getKeys(false).forEach(key -> {
    	    	UUID uuid = UUID.fromString(key);
    	    	Double bounty = data.getConfig().getDouble("PlayerBounties." + key);
    	    	PlayerBounties.put(uuid, bounty);
    	    });
    	}
		return PlayerBounties;
	}
	
	public void setPlayerBounty(Player player, Double bounty) {
        data.getConfig().set(("PlayerBounties." + player.getUniqueId().toString()), bounty);
        data.saveConfig();
        return;
	}
	
	public Location getBanishLocation() {
	    Location banishlocation = null;
    	if(data.getConfig().getString("BanishLocation") != null) {
        	String[] LPs = data.getConfig().getString("BanishLocation").split(":");
        	banishlocation = new Location(Bukkit.getServer().getWorld(LPs[0]), Double.parseDouble(LPs[1]), Double.parseDouble(LPs[2]), Double.parseDouble(LPs[3]), Float.parseFloat(LPs[4]), Float.parseFloat(LPs[5]));
    	}
		return banishlocation;
	}
	
	public void setBanishLocation(Location location) {
		if(location != null) {
			data.getConfig().set("BanishLocation", (location.getWorld().getName() + ":" + location.getBlockX() + ":" + location.getBlockY() + ":" + location.getBlockZ() + ":" + location.getYaw() + ":" + location.getPitch()));
			data.saveConfig();
		}
		return;
	}
	
	public Location getRessurectLocation(Player player) {
    	if (data.getConfig().getConfigurationSection("RessurectLocation") != null) {
    	    if(data.getConfig().getConfigurationSection("RessurectLocation").contains(player.getUniqueId().toString())) {
    	    	String[] LPs = data.getConfig().getString("RessurectLocation." + player.getUniqueId().toString()).split(":");
    	    	Location location = new Location(Bukkit.getServer().getWorld(LPs[0]), Double.parseDouble(LPs[1]), Double.parseDouble(LPs[2]), Double.parseDouble(LPs[3]), Float.parseFloat(LPs[4]), Float.parseFloat(LPs[5]));
    	    	return location;
    	    }
    	}
        return null;
	}
	
	public void setRessurectLocation(Player player, Location location) {
	    data.getConfig().set("RessurectLocation." + player.getUniqueId().toString(), (location.getWorld().getName() + ":" + location.getBlockX() + ":" + location.getBlockY() + ":" + location.getBlockZ() + ":" + location.getYaw() + ":" + location.getPitch()));
	    data.saveConfig();
	    return;
	}
	
	public void advanceArrowType(Player p) {
		if(!ArrowType.containsKey(p)) {
			ArrowType.put(p, 0);
		} else {
			ArrowType.put(p, ((ArrowType.get(p) + 1) % 5));
		}
	}
	
	public void setGrappleLocation(Player p, Location loc) {
		GrappleLocation.put(p, loc.clone());
	}
	
	public void setGrappleArrows(Player p, Arrow arrow) {
		GrappleArrows.put(p, arrow);
	}
	
	//Map getting setting
	
	public Map<Player, Boolean> getRegeneration() {
		return Regeneration;
	}
	
	public Boolean getRegeneration(Player player) {
		return Regeneration.get(player);
	}
	
	public void setRegeneration(Player player, Boolean bool) {
		Regeneration.put(player, bool);
	}
	
	public Map<Player, Boolean> getWaterBreathing() {
		return WaterBreathing;
	}
	
	public Boolean getWaterBreathing(Player player) {
		return WaterBreathing.get(player);
	}
	
	public void setWaterBreathing(Player player, Boolean bool) {
		WaterBreathing.put(player, bool);
	}

	public Map < UUID, Map < UUID, Integer > > getUnholyBond() {
		return UnholyBond;
	}
	
	public void setUnholyBond(Map < UUID, Map < UUID, Integer > > map) {
		UnholyBond = map;
	}
	
	/**
	 * Spellbar Logic    
	 */
	
    
	public void SpellManager(Player p, LivingEntity t, String action) {
		ItemStack EventItem = p.getInventory().getItemInMainHand();
		String race = this.getRace(p);
    	//Spells
    	if(EventItem.equals(constants.getSpellIcons().get("Hotbar"))) this.OpenSpellbar(p);
    	if(EventItem.equals(constants.getSpellIcons().get("Abilities"))) this.CloseSpellbar(p);
		if(race.equalsIgnoreCase("Vampire")) {
			if(EventItem.equals(constants.getSpellIcons().get("Teleport")) && this.triggerRequirements(p, action, "SetTeleportLocation")) new SetTeleportLocation(this, constants, p);
			if(EventItem.equals(constants.getSpellIcons().get("Teleport")) && this.triggerRequirements(p, action, "Teleport")) new Teleport(this, constants, p);
			if(EventItem.equals(constants.getSpellIcons().get("HighJump")) && this.triggerRequirements(p, action, "HighJump")) new HighJump(this, constants, p);
			if(EventItem.equals(constants.getSpellIcons().get("ToggleRegenerationEnabled")) && this.triggerRequirements(p, action, "ToggleRegenerationEnabled")) new ToggleRegen(this, constants, p);
			if(EventItem.equals(constants.getSpellIcons().get("ToggleRegenerationDisabled")) && this.triggerRequirements(p, action, "ToggleRegenerationDisabled")) new ToggleRegen(this, constants, p);
			if(EventItem.equals(constants.getSpellIcons().get("ToggleWaterBreathingEnabled")) && this.triggerRequirements(p, action, "ToggleWaterBreathingEnabled")) new ToggleWaterBreathing(this, constants, p);
			if(EventItem.equals(constants.getSpellIcons().get("ToggleWaterBreathingDisabled")) && this.triggerRequirements(p, action, "ToggleWaterBreathingDisabled")) new ToggleWaterBreathing(this, constants, p);
			if(EventItem.equals(constants.getSpellIcons().get("Bloodvial")) && this.triggerRequirements(p, action, "Bloodvial")) new Bloodvial(this, constants, p);
			if(EventItem.equals(constants.getSpellIcons().get("Bloodrose")) && this.triggerRequirements(p, action, "Bloodrose")) new Bloodrose(this, constants, p);
		}
		if(race.equalsIgnoreCase("Werewolf")) {
			if(EventItem.equals(constants.getSpellIcons().get("SummonWolf")) && this.triggerRequirements(p, action, "SummonWolf")) new SummonWolf(this, constants, p);
			if(EventItem.equals(constants.getSpellIcons().get("Dash")) && this.triggerRequirements(p, action, "Dash")) new Dash(this, constants, p);
			if(EventItem.equals(constants.getSpellIcons().get("Moonflower")) && this.triggerRequirements(p, action, "Moonflower")) new Moonflower(this, constants, p);
		}
		if(race.equalsIgnoreCase("Ghoul")) {
			if(EventItem.equals(constants.getSpellIcons().get("SummonMonster")) && this.triggerRequirements(p, action, "SummonMonster")) new SummonMonster(this, constants, p);
			if(EventItem.equals(constants.getSpellIcons().get("UnholyBond")) && this.triggerRequirements(p, action, "UnholyBond")) new UnholyBond(this, constants, p, t);
			if(EventItem.equals(constants.getSpellIcons().get("Ghoulish")) && this.triggerRequirements(p, action, "Ghoulish")) new Ghoulish(this, constants, p);
		}
		if(race.equalsIgnoreCase("Demon")) {
			if(EventItem.equals(constants.getSpellIcons().get("Fireball")) && this.triggerRequirements(p, action, "Fireball")) new Fireball(this, constants, p);
			if(EventItem.equals(constants.getSpellIcons().get("Explosion")) && this.triggerRequirements(p, action, "Explosion")) new Explosion(this, constants, p);
			if(EventItem.equals(constants.getSpellIcons().get("SummonStrider")) && this.triggerRequirements(p, action, "SummonStrider")) new SummonStrider(this, constants, p);
			if(EventItem.equals(constants.getSpellIcons().get("Snare")) && this.triggerRequirements(p, action, "Snare")) new Snare(this, constants, p, t);
			if(EventItem.equals(constants.getSpellIcons().get("Hellish")) && this.triggerRequirements(p, action, "Hellish")) new Hellish(this, constants, p);
		}
		if(race.equalsIgnoreCase("Priest")) {
			if(EventItem.equals(constants.getSpellIcons().get("Banish")) && this.triggerRequirements(p, action, "Banish")) new BanishBall(this, constants, p);
			if(EventItem.equals(constants.getSpellIcons().get("Exorcise")) && this.triggerRequirements(p, action, "Exorcise")) new ExorciseBall(this, constants, p);
			if(EventItem.equals(constants.getSpellIcons().get("Drain")) && this.triggerRequirements(p, action, "Drain")) new DrainBall(this, constants, p);
			if(EventItem.equals(constants.getSpellIcons().get("HealHuman")) && this.triggerRequirements(p, action, "HealHuman")) new HealHuman(this, constants, p, t);
			if(EventItem.equals(constants.getSpellIcons().get("GuardianAngel")) && this.triggerRequirements(p, action, "GuardianAngel")) new GuardianAngel(this, constants, p, t);
			if(EventItem.equals(constants.getSpellIcons().get("Cure")) && this.triggerRequirements(p, action, "Cure")) new Cure(this, constants, p);
			if(EventItem.equals(constants.getSpellIcons().get("HolyBook")) && this.triggerRequirements(p, action, "HolyBook")) new HolyBook(this, constants, p);
		}
		if(race.equalsIgnoreCase("Necromancer")) {
			if(EventItem.equals(constants.getSpellIcons().get("SummonSkeleton")) && this.triggerRequirements(p, action, "SummonSkeleton")) new SummonSkeleton(this, constants, p);
			if(EventItem.equals(constants.getSpellIcons().get("SummonUndead")) && this.triggerRequirements(p, action, "SummonUndead")) new SummonUndead(this, constants, p);
			if(EventItem.equals(constants.getSpellIcons().get("HealUndead")) && this.triggerRequirements(p, action, "HealUndead")) new HealUndead(this, constants, p);
			if(EventItem.equals(constants.getSpellIcons().get("SetRessurectionSpawn")) && this.triggerRequirements(p, action, "SetRessurectionSpawn")) new SetRessurectionSpawn(this, constants, p);
			if(EventItem.equals(constants.getSpellIcons().get("BookOfDeath")) && this.triggerRequirements(p, action, "BookOfDeath")) new BookOfDeath(this, constants, p);
		}
		if(race.equalsIgnoreCase("WitchHunter")) {
			if((EventItem.equals(constants.getSpellIcons().get("TripleArrow")) || EventItem.equals(constants.getSpellIcons().get("GrappleArrow")) || EventItem.equals(constants.getSpellIcons().get("FireArrow"))
					|| EventItem.equals(constants.getSpellIcons().get("PowerArrow")) || EventItem.equals(constants.getSpellIcons().get("VolleyArrow")))
					&& this.triggerRequirements(p, action, "ArrowSwitcher")) new ArrowSwitcher(this, constants, p);
			if(EventItem.equals(constants.getSpellIcons().get("TripleArrow")) && this.triggerRequirements(p, action, "TripleArrow")) new TripleArrow(this, constants, p);
			if(EventItem.equals(constants.getSpellIcons().get("GrappleArrow")) && this.triggerRequirements(p, action, "GrappleArrow")) new GrappleArrow(this, constants, p);
			if(EventItem.equals(constants.getSpellIcons().get("FireArrow")) && this.triggerRequirements(p, action, "FireArrow")) new FireArrow(this, constants, p);
			if(EventItem.equals(constants.getSpellIcons().get("PowerArrow")) && this.triggerRequirements(p, action, "PowerArrow")) new PowerArrow(this, constants, p);
			if(EventItem.equals(constants.getSpellIcons().get("VolleyArrow")) && this.triggerRequirements(p, action, "VolleyArrow")) new VolleyArrow(this, constants, p);
			if(EventItem.equals(constants.getSpellIcons().get("BookOfWitchHunter")) && this.triggerRequirements(p, action, "BookOfWitchHunter")) new BookOfWitchHunter(this, constants, p);
		}
		if(race.equalsIgnoreCase("Angel")) {
			if(EventItem.equals(constants.getSpellIcons().get("HolySmite")) && this.triggerRequirements(p, action, "HolySmite")) new HolySmite(this, constants, p);
			if(EventItem.equals(constants.getSpellIcons().get("Taunt")) && this.triggerRequirements(p, action, "Taunt")) new Taunt(this, constants, p);
			if(EventItem.equals(constants.getSpellIcons().get("HolyBlessing")) && this.triggerRequirements(p, action, "HolyBlessing")) new HolyBlessing(this, constants, p);
			if(EventItem.equals(constants.getSpellIcons().get("Wings")) && this.triggerRequirements(p, action, "Wings")) new Wings(this, constants, p);
		}
	}
	
    @EventHandler
    public void undroppableSpells(PlayerDropItemEvent event) {
		PlayerDropItemOnTick.add(event.getPlayer().getUniqueId());
    	this.getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
            public void run() {
        		PlayerDropItemOnTick.remove(event.getPlayer().getUniqueId());
            }
        }, 2L);
    	if(constants.getSpellIcons().containsValue(event.getItemDrop().getItemStack())) {
			event.setCancelled(true);
			return;
		}
    }

    @EventHandler
    public void unconsumableSpells(PlayerItemConsumeEvent event) {
    	if(constants.getSpellIcons().containsValue(event.getItem())) {
			event.setCancelled(true);
			return;
		}
    }
    
    public void immovableSpells(Player p) {
    	Inventory TopInventory = p.getOpenInventory().getTopInventory();
    	Inventory BottomInventory = p.getOpenInventory().getBottomInventory();
		String race = getRace(p);
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
			if(constants.getSpellIcons().containsValue(item)) {
				p.setItemOnCursor(new ItemStack(Material.AIR));
				updateInventory = true;
			}
		}
		
		//remove all spells top inventory
		for(int i=0; i<TopInventory.getSize(); i++) {
			if(TopInventory.getItem(i) != null) {
				ItemStack item = TopInventory.getItem(i).clone();
				item.setAmount(1);
				if(constants.getSpellIcons().containsValue(item)) {
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
					if(constants.getSpellIcons().containsValue(item)) {
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
						if(!constants.getSpellIcons().containsValue(temp1)) {
							addItemSafely(p, temp);
						}
					}
					updateInventory = true;
				}
			}
		} else {
			ItemStack temp = BottomInventory.getItem(8);
			if(temp == null || !temp.equals(constants.getSpellIcons().get("Hotbar"))) {
				BottomInventory.setItem(8, constants.getSpellIcons().get("Hotbar"));
				if(temp != null) {
					ItemStack temp1 = temp.clone();
					temp1.setAmount(1);
					if(!temp1.equals(constants.getSpellIcons().get("Hotbar"))) {
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
		ItemStack EventItem = p.getInventory().getItemInMainHand();
		if(constants.getSpellIcons().containsValue(EventItem)) {
			event.setCancelled(true);
		}
    }
    
	@EventHandler
	public void ClickWithSpell(PlayerInteractEvent event) {
		Player p = event.getPlayer();
		ItemStack EventItem = p.getInventory().getItemInMainHand();
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
	        		if(constants.getSpellIcons().containsValue(EventItem)) {
    					if(!EventItem.equals(constants.getSpellIcons().get("TripleArrow")) && !EventItem.equals(constants.getSpellIcons().get("GrappleArrow"))
        						&& !EventItem.equals(constants.getSpellIcons().get("FireArrow")) && !EventItem.equals(constants.getSpellIcons().get("PowerArrow")) && !EventItem.equals(constants.getSpellIcons().get("VolleyArrow"))) {
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
			//No Using Spells as Tools
			if(constants.getSpellIcons().containsValue(event.getBow())) {
				event.setCancelled(true);
				player.updateInventory();
			}
		}
	}
	
	public Boolean triggerRequirements(Player p, String action, String spell) {
		if(action.equals(this.getConfig().getString("Spells." + getRace(p) + "." + spell + ".TriggerMethod"))) {
			//If not on cooldown
			if(!this.getCooldown(p.getUniqueId(), spell)) {
				//If enough magika
				if(getMagic(p) >= this.getConfig().getInt("Spells." + getRace(p) + "." + spell + ".Magic-Cost")) {
					//If enough health
					if(p.getHealth() > this.getConfig().getInt("Spells." + getRace(p) + "." + spell + ".Health-Cost")) {
						//If enough food
						if(p.getFoodLevel() >= this.getConfig().getInt("Spells." + getRace(p) + "." + spell + ".Food-Cost")) {
							return true;
						} else {
							p.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.insufficient-hunger").replaceAll("%prefix%", this.getConfig().getString("Messages.prefix"))));
						}
					} else {
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.insufficient-health").replaceAll("%prefix%", this.getConfig().getString("Messages.prefix"))));
					}
				} else {
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.insufficient-magic").replaceAll("%prefix%", this.getConfig().getString("Messages.prefix"))));
				}
			} else {
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.cooldown").replaceAll("%prefix%", this.getConfig().getString("Messages.prefix"))));
			}
		}
		return false;
	}
	
	public Inventory getSpellbar(String race, Player player) {
		Inventory spellbar = Bukkit.createInventory(null, 9);
		ItemStack empty = constants.getSpellIcons().get("Empty");
		spellbar.setItem(0, empty);
		spellbar.setItem(1, empty);
		spellbar.setItem(2, empty);
		spellbar.setItem(3, empty);
		spellbar.setItem(4, empty);
		spellbar.setItem(5, empty);
		spellbar.setItem(6, empty);
		spellbar.setItem(7, empty);
		spellbar.setItem(8, constants.getSpellIcons().get("Abilities"));
		if(race.equalsIgnoreCase("Human")) {
		}
		if(race.equalsIgnoreCase("Vampire")) {
			if(Regeneration.get(player) != null && Regeneration.get(player) == true) spellbar.setItem(0, constants.getSpellIcons().get("ToggleRegenerationEnabled"));
			else spellbar.setItem(0, constants.getSpellIcons().get("ToggleRegenerationDisabled"));
			if(WaterBreathing.get(player) != null && WaterBreathing.get(player) == true) spellbar.setItem(1, constants.getSpellIcons().get("ToggleWaterBreathingEnabled"));
			else spellbar.setItem(1, constants.getSpellIcons().get("ToggleWaterBreathingDisabled"));
			spellbar.setItem(2, constants.getSpellIcons().get("Teleport"));
			spellbar.setItem(3, constants.getSpellIcons().get("HighJump"));
			spellbar.setItem(4, constants.getSpellIcons().get("Bloodvial"));
			spellbar.setItem(5, constants.getSpellIcons().get("Bloodrose"));
		}
		if(race.equalsIgnoreCase("Werewolf")) {
			spellbar.setItem(0, constants.getSpellIcons().get("SummonWolf"));
			spellbar.setItem(1, constants.getSpellIcons().get("Dash"));
			spellbar.setItem(2, constants.getSpellIcons().get("Moonflower"));
		}
		if(race.equalsIgnoreCase("Ghoul")) {
			spellbar.setItem(0, constants.getSpellIcons().get("SummonMonster"));
			spellbar.setItem(1, constants.getSpellIcons().get("UnholyBond"));
			spellbar.setItem(2, constants.getSpellIcons().get("Ghoulish"));
		}
		if(race.equalsIgnoreCase("Demon")) {
			spellbar.setItem(0, constants.getSpellIcons().get("Fireball"));
			spellbar.setItem(1, constants.getSpellIcons().get("Explosion"));
			spellbar.setItem(2, constants.getSpellIcons().get("Snare"));
			spellbar.setItem(3, constants.getSpellIcons().get("SummonStrider"));
			spellbar.setItem(4, constants.getSpellIcons().get("Hellish"));
		}
		if(race.equalsIgnoreCase("Priest")) {
			spellbar.setItem(0, constants.getSpellIcons().get("Banish"));
			spellbar.setItem(1, constants.getSpellIcons().get("Exorcise"));
			spellbar.setItem(2, constants.getSpellIcons().get("Drain"));
			spellbar.setItem(3, constants.getSpellIcons().get("HealHuman"));
			spellbar.setItem(4, constants.getSpellIcons().get("GuardianAngel"));
			spellbar.setItem(5, constants.getSpellIcons().get("Cure"));
			spellbar.setItem(6, constants.getSpellIcons().get("HolyBook"));
		}
		if(race.equalsIgnoreCase("Necromancer")) {
			spellbar.setItem(0, constants.getSpellIcons().get("SummonSkeleton"));
			spellbar.setItem(1, constants.getSpellIcons().get("SummonUndead"));
			spellbar.setItem(2, constants.getSpellIcons().get("HealUndead"));
			spellbar.setItem(3, constants.getSpellIcons().get("RessurectionSpawn"));
			spellbar.setItem(4, constants.getSpellIcons().get("BookOfDeath"));
		}
		if(race.equalsIgnoreCase("WitchHunter")) {
			if(ArrowType.get(player) != null && ArrowType.get(player) == 1) spellbar.setItem(0, constants.getSpellIcons().get("GrappleArrow"));
			else if(ArrowType.get(player) != null && ArrowType.get(player) == 2) spellbar.setItem(0, constants.getSpellIcons().get("FireArrow"));
			else if(ArrowType.get(player) != null && ArrowType.get(player) == 3) spellbar.setItem(0, constants.getSpellIcons().get("PowerArrow"));
			else if(ArrowType.get(player) != null && ArrowType.get(player) == 4) spellbar.setItem(0, constants.getSpellIcons().get("VolleyArrow"));
			else spellbar.setItem(0, constants.getSpellIcons().get("TripleArrow"));
			spellbar.setItem(1, constants.getSpellIcons().get("BookOfWitchHunter"));
		}
		if(race.equalsIgnoreCase("Angel")) {
			spellbar.setItem(0, constants.getSpellIcons().get("HolySmite"));
			spellbar.setItem(1, constants.getSpellIcons().get("Taunt"));
			spellbar.setItem(2, constants.getSpellIcons().get("HolyBlessing"));
			spellbar.setItem(3, constants.getSpellIcons().get("Wings"));
		}
		if(race.equalsIgnoreCase("Shade")) {
		}
		if(race.equalsIgnoreCase("Siren")) {
		}
		if(race.equalsIgnoreCase("EarthElemental")) {
		}
		if(race.equalsIgnoreCase("WaterElemental")) {
		}
		if(race.equalsIgnoreCase("FireElemental")) {
		}
		return spellbar;
	}
	
	public void OpenSpellbar(Player p) {
		if(!this.getCooldown(p.getUniqueId(),"HotbarSwitcher")) {
			this.setCooldown(p.getUniqueId(), "HotbarSwitcher", 5);
			SpellbarOpen.put(p, true);
			Inventory inv = p.getInventory();
			String race = getRace(p);
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
			inv.setItem(8, constants.getSpellIcons().get("Hotbar"));
			p.updateInventory();
		} else {
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.cooldown").replaceAll("%prefix%", this.getConfig().getString("Messages.prefix"))));
		}
	}
	
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event) {
		Location l = event.getEntity().getLocation();
		Player p = event.getEntity();
		if(event.getDrops().contains(constants.getSpellIcons().get("Abilities"))) {
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
			for(ItemStack item : constants.getSpellIcons().values()) {
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
		if(item.equals(constants.getSpellIcons().get("Abilities"))) {
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
		if(!(p.getInventory().getItemInMainHand().getType().equals(Material.BOW))) return;
		if(!constants.getSpellIcons().containsValue(p.getInventory().getItemInMainHand())) return;
		int slot = p.getInventory().getSize() - 1;
		ItemStack item = p.getInventory().getItem(slot);
		arrowReplacedItem.put(p, item);
		p.getInventory().setItem(slot, new ItemStack(Material.ARROW, 1));
	}
	
	//Commonly Used Functions
	
	public Boolean isArmour(ItemStack item) {
	    return (CraftItemStack.asNMSCopy(item).getItem() instanceof ItemArmor);
	}

	public Boolean isDay(Player player) {
		Boolean day = (0 <= player.getWorld().getTime() && player.getWorld().getTime() < 14000);
		Boolean clear = !player.getWorld().hasStorm();
		return (day && clear);
	}
	
	public Boolean inLava(Player player) {
		return (player.getLocation().getBlock().getType() == Material.LAVA);
	}

	public Boolean inNether(Player player) {
		return (player.getWorld().getEnvironment() == World.Environment.NETHER);
	}

	public Boolean isSupernatural(String race) {
		if(race.equalsIgnoreCase("Vampire") || race.equalsIgnoreCase("Werewolf") || race.equalsIgnoreCase("Ghoul") || race.equalsIgnoreCase("Demon")) {
			return true;
		}
		return false;
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
	
	public double FireballEstimateDistanceFromTime(double ticks) {
		double time = ticks/20;
		if(time>0.05 && time<=1) {
			return (((11.9335532*time*time))+(2.00593576*time)-0.24731786);
		}
		if(time>1 && time<3) {
			return (((2.80323201*time*time))+(20.85083994*time)-10.0316079);
		}
		if(time>=3) {
			return (((0.010111785*time*time))+(37.6632468*time)-35.881427);
		}
		return 0;
	}
	
	public double FireballEstimateTimeFromDistance(double distance) {
		double output = 0;
		if(distance>0 && distance<=13.622) {
			double a = 11.9335532;
			double b = 2.00593576;
			double c = -0.24731786-distance;
			output = ((-b+Math.sqrt((b*b)-(4*a*c)))/(2*a));
		}
		if(distance>13.622 && distance<77.751) {
			double a = 2.80323201;
			double b = 20.85083994;
			double c = -10.0316079-distance;
			output = ((-b+Math.sqrt((b*b)-(4*a*c)))/(2*a));
		}
		if(distance>=77.751) {
			double a = 0.010111785;
			double b = 37.6632468;
			double c = -35.881427-distance;
			output = ((-b+Math.sqrt((b*b)-(4*a*c)))/(2*a));
		}
		if(output<0) {
			output = 0;
		}
		return output;
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
								EntityInsentient e = ((EntityInsentient) ((CraftEntity) monster).getHandle());
								if(following == 1) {
									if(leader != null && leader.isOnline()) {
										if(monster.getTarget() == null) {
									        if((leader.getLocation().distance(monster.getLocation()) > this.getConfig().getInt("FollowerSettings.ApproachLeaderDistance"))) {
									            e.getNavigation().a(leader.getLocation().getX(), leader.getLocation().getY(), leader.getLocation().getZ(), (float) 1.5);
									        } else if((leader.getLocation().distance(monster.getLocation()) < this.getConfig().getInt("FollowerSettings.StayDistance"))) {
									        }
										}
										if((leader.getLocation().distance(monster.getLocation()) > this.getConfig().getInt("FollowerSettings.TeleportDistance"))) {
											monster.setTarget(null);
											monster.teleport(leader);
										}
									} else if(monster.getTarget() == null) {
									    e.getNavigation().a(monster.getLocation().getX(), monster.getLocation().getY(), monster.getLocation().getZ(), (float) 1.5);
									}
								} else {
						        	e.getNavigation().a(monster.getLocation().getX(), monster.getLocation().getY(), monster.getLocation().getZ(), (float) 1.5);
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
		String race = getRace(player);
		if(race.equalsIgnoreCase("Vampire") || (race.equalsIgnoreCase("Werewolf") && !isDay(player)) || race.equalsIgnoreCase("Ghoul") || race.equalsIgnoreCase("Demon") || race.equalsIgnoreCase("Necromancer")) {
			player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 320, 1));
		}
	}
	
	public void NightVision(Player player) {
		String race = getRace(player);
		if(race.equalsIgnoreCase("Vampire") || (race.equalsIgnoreCase("Werewolf") && !isDay(player)) || race.equalsIgnoreCase("Ghoul") || race.equalsIgnoreCase("Demon") || race.equalsIgnoreCase("Necromancer")) {
			player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 320, 1));
		}
	}
	
	public void Speed(Player player) {
		String race = getRace(player);
		if(race.contains("Werewolf") && !isDay(player)) {
			player.setWalkSpeed(0.4F);
		} else {
			player.setWalkSpeed(0.2F);
		}
	}
		
	public void Regeneration(Player player) {
		VampireRegeneration(player);
		String race = getRace(player);
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

	public void MagicFromHeat(Player p) {
		String race = getRace(p);
		if(race.equalsIgnoreCase("Demon")) {
			Boolean lava = inLava(p);
			Boolean nether = inNether(p);
			if(!nether && !lava) {
				setMagic(p, getMagic(p) - 5, false);
			}
			if(lava) {
				setMagic(p, getMagic(p) + 5, false);
			}
		}
	}

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
		String race = getRace(killer);
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
			} else if(!constants.getMonsters().contains(event.getEntityType())) {
				return;
			}
		}
		if(race.equalsIgnoreCase("Vampire")) {
			int num = killer.getFoodLevel() + (int) (event.getEntity().getAttribute(Attribute.GENERIC_MAX_HEALTH).getDefaultValue()/2);
			if(num >= 20) num = 20;
			killer.setFoodLevel(num);
		}
		
		//Increase magic based on victims max health
		setMagic(killer, getMagic(killer) + (((int) event.getEntity().getAttribute(Attribute.GENERIC_MAX_HEALTH).getDefaultValue())*20), false);
		return;
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void DamageCalculation(EntityDamageByEntityEvent event) {
		
		//Attacker Damage Modifiers / Valid attack check
		if(event.getDamager() instanceof Player) {
			Player player = (Player) event.getDamager();
			String race = getRace(player);
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
				if(event.getEntity() instanceof Player) {
					if(isSupernatural(getRace((Player) event.getEntity()))) {
						event.setDamage(event.getDamage()*2.5);
						if(Math.random()*10 < 1) {
							event.getEntity().setFireTicks(100);
						}
					}
					if(constants.getMonsters().contains(event.getEntityType())) {
						if(Math.random()*8 < 1) {
							event.getEntity().setFireTicks(100);
						}
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
			String race = getRace(victim);
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
		
		//UnholyBond Effect
		if(event.getEntity() instanceof Player) {
			UUID VictimUUID = event.getEntity().getUniqueId();
			if (event.getDamager() instanceof LivingEntity) {
				UUID DamagerUUID = event.getEntity().getUniqueId();
				if(UnholyBond.containsKey(VictimUUID)) {
					Map<UUID, Integer> TaggedEntities = UnholyBond.get(VictimUUID);
					if(TaggedEntities.containsKey(DamagerUUID)) {
						event.setDamage(event.getDamage()/4);
					}
				}
			}
		}
		
		//Supernatural Infectivity
		if((event.getEntity() instanceof Player)) {
			Player victim = (Player) event.getEntity();
			String vrace = getRace(victim);
			if(event.getDamage() >= victim.getHealth()) {
				if(!isSupernatural(vrace)) {
					if(event.getDamager() instanceof Player) {
						Player damager = (Player) event.getDamager();
						String drace = getRace(damager);
						if(isSupernatural(drace)) {
							if(Math.random() < this.getConfig().getDouble("PercentInfectionChances.Death-By-Supernatural")/100) {
								victim.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.supernatural-infection-infected").replaceAll("%prefix%", this.getConfig().getString("Messages.prefix")).replaceAll("%class%", drace)));
								setRace(victim, drace, true, true);
								damager.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.supernatural-infection-infector").replaceAll("%prefix%", this.getConfig().getString("Messages.prefix")).replaceAll("%victim%", victim.getName()).replaceAll("%class%", drace)));
							   	return;
							}
						}
					} else {
						if(constants.getMonsters().contains(event.getDamager().getType())) {
							if(Math.random() < this.getConfig().getDouble("PercentInfectionChances.Death-By-Monster")/100) {
								victim.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.supernatural-infection-infected").replaceAll("%prefix%", this.getConfig().getString("Messages.prefix")).replaceAll("%class%", "Ghoul")));
								setRace(victim, "Ghoul", true, true);
							   	return;
							}
						}
						if(event.getDamager().getType().equals(EntityType.WOLF)) {
							if(Math.random() < this.getConfig().getDouble("PercentInfectionChances.Death-By-Wolf")/100) {
								victim.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.supernatural-infection-infected").replaceAll("%prefix%", this.getConfig().getString("Messages.prefix")).replaceAll("%class%", "Werewolf")));
								setRace(victim, "Werewolf", true, true);
							   	return;
							}
						}
						if(event.getDamager().getType().equals(EntityType.BAT)) {
							if(Math.random() < this.getConfig().getDouble("PercentInfectionChances.Death-By-Bat")/100) {
								victim.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.supernatural-infection-infected").replaceAll("%prefix%", this.getConfig().getString("Messages.prefix")).replaceAll("%class%", "Vampire")));
								setRace(victim, "Vampire", true, true);
							   	return;
							}
						}
					}
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void DamageCalculation(EntityDamageEvent event) { 
		//Victim Damage Modifiers
		if(event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();
			String race = getRace(player);
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
		String vrace = getRace(victim);
		if(event.getDamage() < victim.getHealth()) {
			return;
		}
		if(!isSupernatural(vrace)) {
			if(event.getCause() == DamageCause.LAVA) {
				if(Math.random() < this.getConfig().getDouble("PercentInfectionChances.Death-By-Lava")/100) {
					victim.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.supernatural-infection-infected").replaceAll("%prefix%", this.getConfig().getString("Messages.prefix")).replaceAll("%class%", "Demon")));
					setRace(victim, "Demon", true, true);
				   	return;
				}
			}
			if(inNether(victim)) {
				if(Math.random() < this.getConfig().getDouble("PercentInfectionChances.Death-In-Nether")/100) {
					victim.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.supernatural-infection-infected").replaceAll("%prefix%", this.getConfig().getString("Messages.prefix")).replaceAll("%class%", "Demon")));
					setRace(victim, "Demon", true, true);
				   	return;
				}
			}
		}
	}

	@EventHandler	
	public void MonsterTruce(EntityTargetEvent event) { 
		if(event.getTarget() instanceof Player) {
			Player player = (Player) event.getTarget();
			String race = getRace(player);
			EntityType entity = event.getEntityType();
			if(race.equalsIgnoreCase("Vampire") && (entity == EntityType.SKELETON || entity == EntityType.ZOMBIE || entity == EntityType.SPIDER || entity == EntityType.CREEPER || entity == EntityType.HUSK || entity == EntityType.STRAY)) {
				if(!event.getReason().equals(TargetReason.TARGET_ATTACKED_ENTITY)) {
					event.setCancelled(true);
					event.setTarget(null);
				}
			}
			if(race.equalsIgnoreCase("Necromancer") && constants.getUndead().contains(entity)) {
				event.setCancelled(true);
				event.setTarget(null);
			}
			if(race.equalsIgnoreCase("Ghoul") && constants.getUndead().contains(entity)) {
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
		String vrace = getRace(victim);
		if(e.getItem().getType() == Material.ROTTEN_FLESH) {
			if(!isSupernatural(vrace)) {
				if(Math.random() < this.getConfig().getDouble("PercentInfectionChances.Eating-Rotten-Flesh")/100) {
					victim.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.supernatural-infection-infected").replaceAll("%prefix%", this.getConfig().getString("Messages.prefix")).replaceAll("%class%", "Ghoul")));
					setRace(victim, "Ghoul", true, true);
				   	return;
				}
			}
		}
	}

	@EventHandler
	public void noArmour(ArmorEquipEvent e) {
		Player p = e.getPlayer();
		String race = getRace(p);
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

	public void ejectIlligalArmour(Player p) {
		ItemStack[] armour = p.getInventory().getArmorContents();
		String race = getRace(p);
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
				String race = getRace(player);
				if(player.getGameMode() != GameMode.CREATIVE && player.getGameMode() != GameMode.SPECTATOR) {
					if(race.contains("Vampire")) {
						if(player.getFireTicks()<20) {
							if(player.getInventory().getHelmet() == null || player.getInventory().getHelmet().getType() != Material.GOLDEN_HELMET || getMagic(player) < (100)) {
								player.setFireTicks(20);
								return;
							} else {
								if(tick % 60 == 0) {
									setMagic(player, getMagic(player) - 100, false);
								}
							}
						}
					} else if(race.contains("Angel")) {
						if(tick % 20 == 0) {
							setMagic(player, getMagic(player)+1, false);
						}
					}
				}
			}
		}
	}
 
	public Boolean rainContact(Player p) {
		Biome biome = p.getWorld().getBiome(p.getLocation().getBlockX(), p.getLocation().getBlockY(), p.getLocation().getBlockZ());
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
				String race = getRace(player);
				if(race.contains("Ghoul")) {
					player.damage(5);
				}
			}
		}
	}
	
	@EventHandler
	public void onConsume(PlayerItemConsumeEvent event) {
		Player player = event.getPlayer();
		String race = getRace(player);
		if(event.getItem().getType() != Material.POTION) {
			if(race.equalsIgnoreCase("Vampire")) {
				event.setCancelled(true);
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.vampire-no-eat").replaceAll("%prefix%", this.getConfig().getString("Messages.prefix"))));
			}
			if(race.equalsIgnoreCase("Werewolf")) {
				//Increase magic based on meats nutritional value
				if(constants.getMeatNutrition().get(event.getItem().getType()) != null) {
					setMagic(player, getMagic(player) + (constants.getMeatNutrition().get(event.getItem().getType())*5), false);
				}
			}
		}
	}
		
	@EventHandler 
	public void MaxEat(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		String race = getRace(player);
		if(race.equalsIgnoreCase("Werewolf") && !isDay(player)) {
			if(event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
				if(constants.getMeatNutrition().containsKey(event.getItem().getType())) {
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
	}

	@EventHandler
	public void onWolfSpawn(CreatureSpawnEvent event) {
		Entity entity = event.getEntity();
		if (entity.getType() == EntityType.WOLF) {
			Wolf wolf = (Wolf) entity;
			for (Entity nearbyEntity : wolf.getNearbyEntities(32, 32, 32)) {
				if (nearbyEntity instanceof Player) {
					Player target = (Player)nearbyEntity;
					if(!getRace(target).equalsIgnoreCase("Werewolf")) {
						if(target.getInventory().getItemInMainHand() == null || !target.getInventory().getItemInMainHand().getType().equals(Material.BONE)) {
							wolf.setAngry(true);
							wolf.setTarget(target);
						}
					}
				}
			}
		}
	}

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
								String targetrace = getRace(target);
								if(!targetrace.equalsIgnoreCase("Werewolf")) {
									//if not holding bone
									if(target.getInventory().getItemInMainHand() == null || !target.getInventory().getItemInMainHand().getType().equals(Material.BONE)) {
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
								if(isSupernatural(getRace(target))) {
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
		if(getPlayerBounty(target) != 0) {
			double bounty = getPlayerBounty(target);
			setPlayerBounty(target, (double) 0);
			eco.depositPlayer(collector, bounty);
			collector.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.redeem-bounty").replaceAll("%prefix%", this.getConfig().getString("Messages.prefix")).replaceAll("%bounty%", ("" + bounty)).replaceAll("%target%", target.getName())));
			if(getRace(collector).equals("WitchHunter")) setMagic(collector, getMagic(collector) + ((int) bounty), false);
		}
	}
	
	//Vampire

	public void VampireRegeneration(Player player) {
		String race = getRace(player);
		if(race.equalsIgnoreCase("Vampire")) {
			if(!Regeneration.containsKey(player)) Regeneration.put(player, false);
			if(!Regeneration.get(player)) return;
			if(player.getHealth() == 20 || player.getHealth() == 0) return;
			if(getMagic(player) < this.getConfig().getInt("Spells.Vampire.Regeneration.Magic-Cost")) return;
			if(player.getFoodLevel() < this.getConfig().getInt("Spells.Vampire.Regeneration.Food-Cost")) return;
			if(player.getHealth() <= this.getConfig().getInt("Spells.Vampire.Regeneration.Health-Cost")) return;
			setMagic(player, getMagic(player) - this.getConfig().getInt("Spells.Vampire.Regeneration.Magic-Cost"), false);
			player.setFoodLevel(player.getFoodLevel() - this.getConfig().getInt("Spells.Vampire.Regeneration.Food-Cost"));
			player.setHealth(player.getHealth() - this.getConfig().getInt("Spells.Vampire.Regeneration.Health-Cost"));
			
			//Heal the player by half a heart
			if(player.getHealth()+1<=20) {
				player.setHealth(player.getHealth()+1);
			} else {
				player.setHealth(20);
			}
		}
 	}

	public void WaterBreathing(Player player) {
		String race = getRace(player);
		if(race.equalsIgnoreCase("Vampire")) {
			if(!WaterBreathing.containsKey(player)) WaterBreathing.put(player, false);
			if(!WaterBreathing.get(player)) return;
			if(player.getRemainingAir() == player.getMaximumAir()) return;
			if(getMagic(player) < this.getConfig().getInt("Spells.Vampire.WaterBreathing.Magic-Cost")) return;
			if(player.getFoodLevel() < this.getConfig().getInt("Spells.Vampire.WaterBreathing.Food-Cost")) return;
			if(player.getHealth() <= this.getConfig().getInt("Spells.Vampire.WaterBreathing.Health-Cost")) return;
			setMagic(player, getMagic(player) - this.getConfig().getInt("Spells.Vampire.WaterBreathing.Magic-Cost"), false);
			player.setFoodLevel(player.getFoodLevel() - this.getConfig().getInt("Spells.Vampire.WaterBreathing.Food-Cost"));
			player.setHealth(player.getHealth() - this.getConfig().getInt("Spells.Vampire.WaterBreathing.Health-Cost"));
			int air = player.getRemainingAir() + 30;
			if(air > player.getMaximumAir()) air = player.getMaximumAir();
			player.setRemainingAir(air);
		}
	}
	
	//Ghoul
	
	public void UnholyBondTimer() {
		for(UUID TaggerUUID : UnholyBond.keySet()) {
			Map<UUID, Integer> TaggedEntities = UnholyBond.get(TaggerUUID);
			for(UUID VictimUUID : TaggedEntities.keySet()) {
				int tick = TaggedEntities.get(VictimUUID);
				tick = tick-1;
				if(tick>0) {
					TaggedEntities.put(VictimUUID, tick);
				} else {
					TaggedEntities.remove(VictimUUID);
				}
			}
			UnholyBond.put(TaggerUUID, TaggedEntities);
		}
	}
	
	//Priest

	@EventHandler
	public void SpellHit(ProjectileHitEvent event) {
		if(event.getEntity().getCustomName() != null && event.getHitEntity() != null && event.getHitEntity() instanceof LivingEntity) {
			String[] ProjectileInfo = event.getEntity().getCustomName().split(":");
			if(ProjectileInfo.length == 2) {
				UUID uuid = UUID.fromString(ProjectileInfo[0]);
				String spell = ProjectileInfo[1];
				Player player = Bukkit.getPlayer(uuid);
				LivingEntity target = (LivingEntity) event.getHitEntity();
				if(spell.equals("Banish")) {
					Banish(player, target);
				}
				if(spell.equals("Exorcise")) {
					Exorcise(player, target);
				}
				if(spell.equals("Drain")) {
					Drain(player, target);
				}
			}
		}
	}
	
	@EventHandler
	public void ArrowHit(ProjectileHitEvent event) {
		if(event.getEntity().getCustomName() != null && event.getEntity() instanceof Arrow) {
			String[] ProjectileInfo = event.getEntity().getCustomName().split(":");
			if(ProjectileInfo.length == 2) {
				String spell = ProjectileInfo[1];
				if(spell.equals("PowerArrow")) {
					event.getEntity().getWorld().createExplosion(event.getEntity().getLocation(), 1, true);
					if(event.getHitEntity() != null) {
						event.getHitEntity().setFireTicks(100);
					}
				}
				if(spell.equals("FireArrow")) {
					if(event.getHitEntity() != null) {
						event.getHitEntity().setFireTicks(100);
					} else {
						if(event.getEntity().getLocation().getBlock().getType().equals(Material.AIR)) {
							event.getEntity().getLocation().getBlock().setType(Material.FIRE);
						}
					}
				}
				event.getEntity().remove();
			}
		}
	}
	
	public void Banish(Player p, LivingEntity e) {
		if(e instanceof Player) {
			Player t = (Player) e;
			if(!isSupernatural(getRace(t))) {
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("only-supernaturals").replaceAll("%prefix%", this.getConfig().getString("Messages.prefix")).replaceAll("%spellname%", "banish")));
				return;
			}
			t.teleport(getBanishLocation());
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.banishment-success-sender").replaceAll("%prefix%", this.getConfig().getString("Messages.prefix")).replaceAll("%player%", p.getName()).replaceAll("%target%", t.getName())));
			t.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.banishment-success-target").replaceAll("%prefix%", this.getConfig().getString("Messages.prefix")).replaceAll("%player%", p.getName()).replaceAll("%target%", t.getName())));
		}
	}

	public void Exorcise(Player p, LivingEntity e) {
		if(e instanceof Player) {
			Player t = (Player) e;
			if(!isSupernatural(getRace(t))) {
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("only-supernaturals").replaceAll("%prefix%", this.getConfig().getString("Messages.prefix")).replaceAll("%spellname%", "exorcise")));
				return;
			}
			//Set Class to Human
			t.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.exorcism-success-target").replaceAll("%prefix%", this.getConfig().getString("Messages.prefix")).replaceAll("%player%", p.getName()).replaceAll("%target%", t.getName())));
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.exorcism-success-sender").replaceAll("%prefix%", this.getConfig().getString("Messages.prefix")).replaceAll("%player%", p.getName()).replaceAll("%target%", t.getName())));
			setRace(t, "Human", false, false);
			CollectBounty(t, p);
		}
	}

	public void Drain(Player p, LivingEntity e) {
		if(e instanceof Player) {
			Player t = (Player) e;
			if(!isSupernatural(getRace(t))) {
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("only-supernaturals").replaceAll("%prefix%", this.getConfig().getString("Messages.prefix")).replaceAll("%spellname%", "drain")));
				return;
			}
			//If enough target Magic
			int magic = getMagic(t);
			if(magic<=0) {
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.target-no-magic").replaceAll("%prefix%", this.getConfig().getString("Messages.prefix"))));
				return;
			}
			//Remove Magic
			setMagic(t, (int) (getMagic(t)*0.85), false);
		}
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void NoSpellDamage(EntityDamageByEntityEvent event) {
		Entity entity = event.getDamager();
		if(entity instanceof SmallFireball) {
			String name = entity.getCustomName();
			if(name != null && name.contains(":")) {
				String spell = name.split(":")[1];
				if(spell.equals("Banish") || spell.equals("Exorcise") || spell.equals("Drain")) {
					event.setCancelled(true);
				}
			}
		}
	}
	
	@EventHandler
	public void GuardianAngelEffect(EntityDamageByEntityEvent event) {
		if(event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();
			if(player.getHealth()-event.getDamage()<=0) {
				for(String uuid : getGuardianAngelList()) {
					if(player.getUniqueId().toString().equals(uuid)) {
						player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.guardian-angel-use").replaceAll("%prefix%", this.getConfig().getString("Messages.prefix"))));
						removeGuardianAngel(player);
						event.setCancelled(true);
						player.setHealth(8);
						player.damage(1);
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
				for(String uuid : getGuardianAngelList()) {
					if(player.getUniqueId().toString().equals(uuid)) {
						player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.guardian-angel-use").replaceAll("%prefix%", this.getConfig().getString("Messages.prefix"))));
						removeGuardianAngel(player);
						event.setCancelled(true);
						player.setHealth(8);
						player.damage(1);
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

	//Necromancer
	
	@EventHandler
	public void RessurectionSpawn(PlayerRespawnEvent event) {
		Player player = event.getPlayer();
		if(!getRace(player).equalsIgnoreCase("Necromancer")) {
			return;
		}
		if(getRessurectLocation(player) != null) {
            this.getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
                public void run() {
					player.teleport(getRessurectLocation(player));
					player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("Messages.go-ressurection-location").replaceAll("%prefix%", config.getString("Messages.prefix"))));
					setRessurectLocation(player, null);
                }
            }, 1L);
		} 
	}
	
	//Witch Hunter
	
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
	
	//Recruiting Items
	
	@EventHandler
	public void ConsumePotion(PlayerItemConsumeEvent event) {
		Player player = event.getPlayer();
		if(event.getItem().equals(constants.getCustomItems().get("Bloodrose"))) {
			event.setCancelled(true);
    		if(setRace(player, "Vampire", true, true)) {
    	    	player.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
    		}
		}
		if(event.getItem().equals(constants.getCustomItems().get("Moonflower"))) {
			event.setCancelled(true);
    		if(setRace(player, "Werewolf", true, true)) {
    	    	player.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
    		}
		}
		if(event.getItem().equals(constants.getCustomItems().get("Ghoulish"))) {
			event.setCancelled(true);
    		if(setRace(player, "Ghoul", true, true)) {
    	    	player.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
    		}
		}
		if(event.getItem().equals(constants.getCustomItems().get("Hellish"))) {
			event.setCancelled(true);
    		if(setRace(player, "Demon", true, true)) {
    	    	player.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
    		}
		}
		if(event.getItem().equals(constants.getCustomItems().get("Cure"))) {
			event.setCancelled(true);
	    	if(isSupernatural(getRace(player))) {
	    		if(setRace(player, "Human", false, false)) {
					CollectBounty(player, player);
	    	    	player.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
		    		return;
	    		}
	    	} else {
        		player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.must-be-supernatural").replaceAll("%prefix%", this.getConfig().getString("Messages.prefix"))));
	    	}
		}
		if(event.getItem().equals(constants.getCustomItems().get("BloodVial"))) {
			event.setCancelled(true);
    		if(getRace(player).equals("Vampire")) {
    			if(player.getFoodLevel()<20) {
    				player.setFoodLevel(player.getFoodLevel()+4);
    				player.setSaturation(player.getSaturation()+4);
        	    	player.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
    				return;
    			}
    		}
		}
	}

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
		if(itemclone.equals(constants.getCustomItems().get("HolyBook"))) {
			if(setRace(player, "Priest", true, true)) {
		    	event.setCancelled(true);
		    	if(item.getAmount()-1<1) {
			    	player.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
		    	} else {
					item.setAmount(item.getAmount()-1);
			    	player.getInventory().setItemInMainHand(item);
		    	}
			}
		}
		if(itemclone.equals(constants.getCustomItems().get("BookOfDeath"))) {
			if(setRace(player, "Necromancer", true, true)) {
		    	event.setCancelled(true);
		    	if(item.getAmount()-1<1) {
			    	player.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
		    	} else {
					item.setAmount(item.getAmount()-1);
			    	player.getInventory().setItemInMainHand(item);
		    	}
			}
		}
		if(itemclone.equals(constants.getCustomItems().get("BookOfWitchHunter"))) {
			if(setRace(player, "WitchHunter", true, true)) {
		    	event.setCancelled(true);
		    	if(item.getAmount()-1<1) {
			    	player.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
		    	} else {
					item.setAmount(item.getAmount()-1);
			    	player.getInventory().setItemInMainHand(item);
		    	}
			}
		}
	}

	//Alters
	
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
        		setRace(player, "Priest", true, true);
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
            e.setCancelled(true);
            if (e.getSlot() != 10 && e.getSlot() != 13 && e.getSlot() != 16) {
                e.setCancelled(true);
            }
            if (e.getSlot() == 13) {
                e.setCancelled(true);
        		Player player = (Player) e.getWhoClicked();
        		setRace(player, "Vampire", true, true);
            }
            if (e.getSlot() == 16) {
                e.setCancelled(true);
        		Player player = (Player) e.getWhoClicked();
        		setRace(player, "Necromancer", true, true);
            }
        }
    }
    
    @EventHandler()
    public void HolyAlterDonationLogic(InventoryClickEvent e) {
    	if(e.getClickedInventory() == null || e.getWhoClicked().getInventory() == null) return;
        if (e.getView().getTitle().contains("Holy Altar")) {
        	Player player = (Player) e.getWhoClicked();
        	String race = getRace(player);
            this.getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
                public void run() {
					if(e.getView().getItem(11) != null) {
			            ItemStack donation = e.getView().getItem(11);
			           	if(race.equals("Priest") || race.equals("Angel")) {
				           	if(constants.getHolyDonationValue().get(donation.getType()) != null) {
				           		int magic = getMagic(player) + (constants.getHolyDonationValue().get(donation.getType())*5*donation.getAmount());
								e.getView().setItem(11, new ItemStack(Material.AIR));
								setMagic(player, magic, false);
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
			        		player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("Messages.holy-donate-only").replaceAll("%prefix%", config.getString("Messages.prefix"))));
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
        	String race = getRace(player);
            this.getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
                public void run() {
					if(e.getView().getItem(10) != null) {
			            ItemStack donation = e.getView().getItem(10);
			           	if(race.equals("Necromancer")) {
				           	if(constants.getUnholyDonationValue().get(donation.getType()) != null) {
				           		int magic = getMagic(player) + (constants.getUnholyDonationValue().get(donation.getType())*5*donation.getAmount());
								e.getView().setItem(10, new ItemStack(Material.AIR));
								setMagic(player, magic, false);
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
			        		player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("Messages.necromancer-donate-only").replaceAll("%prefix%", config.getString("Messages.prefix"))));
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
            	String race = getRace(player);
            	if(race.equals("WitchHunter")) {
            		openBountyMenu(player);
            	} else {
	        		player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.witchhunter-use-only").replaceAll("%prefix%", this.getConfig().getString("Messages.prefix"))));
	           		return;
            	}
            }
            if (e.getSlot() == 15) {
        		Player player = (Player) e.getWhoClicked();
        		setRace(player, "WitchHunter", true, true);
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
    	inv.setItem(15, constants.getCustomItems().get("HolyBook"));
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
    	inv.setItem(13, constants.getCustomItems().get("Bloodrose"));
    	inv.setItem(16, constants.getCustomItems().get("BookOfDeath"));
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
    	inv.setItem(11, constants.getCustomItems().get("BountyMenu"));
    	inv.setItem(15, constants.getCustomItems().get("BookOfWitchHunter"));
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
		for(UUID uuid : getPlayerBounties().keySet()) {
			Player player = Bukkit.getPlayer(uuid);
			ItemStack head = new ItemStack(Material.PLAYER_HEAD);
			SkullMeta sm = (SkullMeta) head.getItemMeta();
			sm.setDisplayName(ChatColor.GOLD + player.getName());
			lore = new ArrayList < String > ();
			lore.add(ChatColor.YELLOW + "Bounty: $" + getPlayerBounty(uuid));
			sm.setLore(lore);
			sm.setOwningPlayer(player);
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
}