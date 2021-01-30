package me.LoneSurvivor.Supernatural.repositories;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;

import me.LoneSurvivor.Supernatural.Supernatural;

public class Constants {
	private final Supernatural supernatural;
	Map < Material, Integer > MeatNutrition = new HashMap < Material, Integer> ();
	Map <String, ItemStack> SpellIcons = new HashMap < String, ItemStack > ();
	Map < Material, Integer > HolyDonationValue = new HashMap < Material, Integer> ();
	Map < Material, Integer > UnholyDonationValue = new HashMap < Material, Integer> ();
	Map <String, ItemStack> CustomItems = new HashMap<String, ItemStack>();
	ArrayList<EntityType> monsters = new ArrayList<EntityType>();
	ArrayList<EntityType> undead = new ArrayList<EntityType>();
	
	public Constants(Supernatural plugin) {
		this.supernatural = plugin;
    	MeatNutritionSetup();
    	SpellIcons();
    	DonationValueSetup();
    	CustomItems();
    	monsterList();
	}
    
    public Map <String, ItemStack> getSpellIcons() {
    	return SpellIcons;
    }
    
    public Map <String, ItemStack> getCustomItems() {
    	return CustomItems;
    }
    
    public Map <Material, Integer> getMeatNutrition() {
    	return MeatNutrition;
    }
    
    public Map <Material, Integer> getHolyDonationValue() {
    	return HolyDonationValue;
    }
    
    public Map <Material, Integer> getUnholyDonationValue() {
    	return UnholyDonationValue;
    }
    
    public ArrayList<EntityType> getMonsters() {
    	return monsters;
    }
    
    public ArrayList<EntityType> getUndead() {
    	return undead;
    }
    
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
		monsters.add(EntityType.SKELETON);
		monsters.add(EntityType.STRAY);
		monsters.add(EntityType.WITHER_SKELETON);
		monsters.add(EntityType.ZOMBIE);
		monsters.add(EntityType.DROWNED);
		monsters.add(EntityType.HUSK);
		monsters.add(EntityType.ZOMBIFIED_PIGLIN);
		monsters.add(EntityType.ZOMBIE_VILLAGER);
		monsters.add(EntityType.ZOGLIN);
		undead.add(EntityType.SKELETON);
		undead.add(EntityType.STRAY);
		undead.add(EntityType.WITHER_SKELETON);
		undead.add(EntityType.ZOMBIE);
		undead.add(EntityType.DROWNED);
		undead.add(EntityType.HUSK);
		undead.add(EntityType.ZOMBIFIED_PIGLIN);
		undead.add(EntityType.ZOMBIE_VILLAGER);
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
		CustomItems.put("Bloodrose", item);
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
		CustomItems.put("VampireBook", writtenBook);
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
		CustomItems.put("Moonflower", item);
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
		CustomItems.put("WerewolfBook", writtenBook);
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
				CustomItems.put("Ghoulish", item);
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
				CustomItems.put("GhoulBook", writtenBook);
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
				CustomItems.put("Hellish", item);
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
				CustomItems.put("DemonBook", writtenBook);
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
				CustomItems.put("Cure", item);
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
				CustomItems.put("HolyBook", item);
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
				CustomItems.put("PriestBook", writtenBook);
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
				CustomItems.put("BookOfDeath", item);
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
				CustomItems.put("NecromancerBook", writtenBook);
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
				CustomItems.put("BookOfWitchHunter", item);
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
				CustomItems.put("WitchHunterBook", writtenBook);
				//15
				item = new ItemStack(Material.FILLED_MAP);
				meta = item.getItemMeta();
				meta.setDisplayName(ChatColor.GOLD + "Bounty Menu");
				lore = new ArrayList<String>();
				lore.add(ChatColor.YELLOW + "Opens the Bounty Menu");
				meta.setLore(lore);
				item.setItemMeta(meta);
				CustomItems.put("BountyMenu", item);
				//16
				item = new ItemStack(Material.POTION);
				pm.setColor(Color.fromRGB(115, 0, 0));
				pm.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
				pm.setDisplayName(ChatColor.DARK_RED + "Blood Vial");
				lore = new ArrayList<String>();
				lore.add(ChatColor.RED + "Gives Vampires 2 Hunger When Consumed");
				pm.setLore(lore);
				item.setItemMeta(pm);
				CustomItems.put("BloodVial", item);
	}
    
    public void SpellIcons() {
    	
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
    	SpellIcons.put("Hotbar", item);
    	
		item = new ItemStack(Material.BOOK);
    	meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&l&6Abilities"));
    	item.setItemMeta(meta);
    	SpellIcons.put("Abilities", item);
    	
		item = new ItemStack(Material.BARRIER);
		meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&cEmpty Slot"));
		item.setItemMeta(meta);
		SpellIcons.put("Empty", item);
		
		item = new ItemStack(Material.PAPER);
		meta = item.getItemMeta();
    	meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
    	meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		meta.setUnbreakable(true);
		meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6Teleport"));
		lore = new ArrayList<String>();
		lore.add(ChatColor.translateAlternateColorCodes('&', "&eLeft Click to Set Teleport Location"));
		lore.add(ChatColor.translateAlternateColorCodes('&', "&eRight Click to Teleport - " + supernatural.getConfig().getInt("Spells.Vampire.Teleport.Cost") + " Magika"));
		meta.setLore(lore);
		item.setItemMeta(meta);
		SpellIcons.put("Teleport", item);
		
		item = new ItemStack(Material.FEATHER);
		meta = item.getItemMeta();
    	meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
    	meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		meta.setUnbreakable(true);
		meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6High Jump"));
		lore = new ArrayList<String>();
		lore.add(ChatColor.translateAlternateColorCodes('&', "&eRight Click to High Jump - " + supernatural.getConfig().getInt("Spells.Vampire.HighJump.Cost") + " Magika"));
		meta.setLore(lore);
		item.setItemMeta(meta);
		SpellIcons.put("HighJump", item);
		
		item = new ItemStack(Material.FERMENTED_SPIDER_EYE);
		meta = item.getItemMeta();
    	meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
    	meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		meta.setUnbreakable(true);
		meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6Create Blood Vial"));
		lore = new ArrayList<String>();
		lore.add(ChatColor.translateAlternateColorCodes('&', "&eRight Click to Create a Blood Vial - " + supernatural.getConfig().getInt("Spells.Vampire.Bloodvial.Cost") + " Magika and 3 Hunger"));
		meta.setLore(lore);
		item.setItemMeta(meta);
		SpellIcons.put("Bloodvial", item);
		
		item = new ItemStack(Material.APPLE);
		meta = item.getItemMeta();
    	meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
    	meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		meta.setUnbreakable(true);
		meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6Regeneration - Enabled"));
		lore = new ArrayList<String>();
		lore.add(ChatColor.translateAlternateColorCodes('&', "&eRight Click to Toggle Regeneration - " + supernatural.getConfig().getInt("Spells.Vampire.Regeneration.Cost") + " Magika/Heart"));
		meta.setLore(lore);
		item.setItemMeta(meta);
		SpellIcons.put("ToggleRegenerationEnabled", item);
		
		item = new ItemStack(Material.APPLE);
		meta = item.getItemMeta();
		meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		meta.setUnbreakable(true);
		meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6Regeneration - Disabled"));
		lore = new ArrayList<String>();
		lore.add(ChatColor.translateAlternateColorCodes('&', "&eRight Click to Toggle Regeneration - " + supernatural.getConfig().getInt("Spells.Vampire.Regeneration.Cost") + " Magika/Heart"));
		meta.setLore(lore);
		item.setItemMeta(meta);
		SpellIcons.put("ToggleRegenerationDisabled", item);
		
		item = new ItemStack(Material.LILY_PAD);
		meta = item.getItemMeta();
    	meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
    	meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		meta.setUnbreakable(true);
		meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6Water Breathing - Enabled"));
		lore = new ArrayList<String>();
		lore.add(ChatColor.translateAlternateColorCodes('&', "&eRight Click to Toggle Water Breathing - " + supernatural.getConfig().getInt("Spells.Vampire.WaterBreathing.Cost")*20 + " Magika/Sec"));
		meta.setLore(lore);
		item.setItemMeta(meta);
		SpellIcons.put("ToggleWaterBreathingEnabled", item);
		
		item = new ItemStack(Material.LILY_PAD);
		meta = item.getItemMeta();
		meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		meta.setUnbreakable(true);
		meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6Water Breathing - Disabled"));
		lore = new ArrayList<String>();
		lore.add(ChatColor.translateAlternateColorCodes('&', "&eRight Click to Toggle Water Breathing - " + supernatural.getConfig().getInt("Spells.Vampire.WaterBreathing.Cost")*20 + " Magika/Sec"));
		meta.setLore(lore);
		item.setItemMeta(meta);
		SpellIcons.put("ToggleWaterBreathingDisabled", item);
		
		item = new ItemStack(Material.POPPY);
		meta = item.getItemMeta();
    	meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
    	meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		meta.setUnbreakable(true);
		meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6Create Bloodrose Potion"));
		lore = new ArrayList<String>();
		lore.add(ChatColor.translateAlternateColorCodes('&', "&eRight Click to Create Bloodrose Potion - " + supernatural.getConfig().getInt("Spells.Vampire.Bloodrose.Cost") + " Magika and 10 Health"));
		meta.setLore(lore);
		item.setItemMeta(meta);
		SpellIcons.put("Bloodrose", item);
		
		item = new ItemStack(Material.BONE);
		meta = item.getItemMeta();
    	meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
    	meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		meta.setUnbreakable(true);
		meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6Summon Wolf"));
		lore = new ArrayList<String>();
		lore.add(ChatColor.translateAlternateColorCodes('&', "&eRight Click to Summon Wolf - " + supernatural.getConfig().getInt("Spells.Werewolf.SummonWolf.Cost") + " Magika"));
		meta.setLore(lore);
		item.setItemMeta(meta);
		SpellIcons.put("SummonWolf", item);
		
		item = new ItemStack(Material.FEATHER);
		meta = item.getItemMeta();
    	meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
    	meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		meta.setUnbreakable(true);
		meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6Dash"));
		lore = new ArrayList<String>();
		lore.add(ChatColor.translateAlternateColorCodes('&', "&eRight Click to Dash - " + supernatural.getConfig().getInt("Spells.Werewolf.Dash.Cost") + " Magika"));
		meta.setLore(lore);
		item.setItemMeta(meta);
		SpellIcons.put("Dash", item);
		
		item = new ItemStack(Material.BLUE_ORCHID);
		meta = item.getItemMeta();
    	meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
    	meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		meta.setUnbreakable(true);
		meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6Create Moonflower Potion"));
		lore = new ArrayList<String>();
		lore.add(ChatColor.translateAlternateColorCodes('&', "&eRight Click to Create Moonflower Potion - " + supernatural.getConfig().getInt("Spells.Werewolf.Moonflower.Cost") + " Magika and 10 Health"));
		meta.setLore(lore);
		item.setItemMeta(meta);
		SpellIcons.put("Moonflower", item);
		
		item = new ItemStack(Material.BONE_MEAL);
		meta = item.getItemMeta();
    	meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
    	meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		meta.setUnbreakable(true);
		meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6Summon Monster"));
		lore = new ArrayList<String>();
		lore.add(ChatColor.translateAlternateColorCodes('&', "&eRight Click to Summon a Monster - " + supernatural.getConfig().getInt("Spells.Ghoul.SummonUndead.Cost") + " Magika"));
		meta.setLore(lore);
		item.setItemMeta(meta);
		SpellIcons.put("SummonMonster", item);

		item = new ItemStack(Material.BONE);
		meta = item.getItemMeta();
    	meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
    	meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		meta.setUnbreakable(true);
		meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6Unholy Bond"));
		lore = new ArrayList<String>();
		lore.add(ChatColor.translateAlternateColorCodes('&', "&eHit target to use Unhold Bond - " + supernatural.getConfig().getInt("Spells.Ghoul.UnholyBond.Cost") + " Magika"));
		meta.setLore(lore);
		item.setItemMeta(meta);
		SpellIcons.put("UnholyBond", item);

		item = new ItemStack(Material.WITHER_ROSE);
		meta = item.getItemMeta();
    	meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
    	meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		meta.setUnbreakable(true);
		meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6Create Goulish Potion"));
		lore = new ArrayList<String>();
		lore.add(ChatColor.translateAlternateColorCodes('&', "&eRight Click to Create Goulish Potion - " + supernatural.getConfig().getInt("Spells.Ghoul.Ghoulish.Cost") + " Magika and 10 Health"));
		meta.setLore(lore);
		item.setItemMeta(meta);
		SpellIcons.put("Ghoulish", item);

		item = new ItemStack(Material.REDSTONE);
		meta = item.getItemMeta();
    	meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
    	meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		meta.setUnbreakable(true);
		meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6Fireball"));
		lore = new ArrayList<String>();
		lore.add(ChatColor.translateAlternateColorCodes('&', "&eLaunches a fireball in the direction your looking - " + supernatural.getConfig().getInt("Spells.Demon.Fireball.Cost") + " Magika"));
		meta.setLore(lore);
		item.setItemMeta(meta);
		SpellIcons.put("Fireball", item);

		item = new ItemStack(Material.TNT);
		meta = item.getItemMeta();
    	meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
    	meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		meta.setUnbreakable(true);
		meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6Explosion"));
		lore = new ArrayList<String>();
		lore.add(ChatColor.translateAlternateColorCodes('&', "&eCauses an explosion where your standing - " + supernatural.getConfig().getInt("Spells.Demon.Explosion.Cost") + " Magika"));
		meta.setLore(lore);
		item.setItemMeta(meta);
		SpellIcons.put("Explosion", item);

		item = new ItemStack(Material.INK_SAC);
		meta = item.getItemMeta();
    	meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
    	meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		meta.setUnbreakable(true);
		meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6Snare"));
		lore = new ArrayList<String>();
		lore.add(ChatColor.translateAlternateColorCodes('&', "&eFreezes players you hit for 10s - " + supernatural.getConfig().getInt("Spells.Demon.Snare.Cost") + " Magika"));
		meta.setLore(lore);
		item.setItemMeta(meta);
		SpellIcons.put("Snare", item);
		
		item = new ItemStack(Material.WARPED_FUNGUS_ON_A_STICK);
		meta = item.getItemMeta();
    	meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
    	meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		meta.setUnbreakable(true);
		meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6Summon Strider"));
		lore = new ArrayList<String>();
		lore.add(ChatColor.translateAlternateColorCodes('&', "&eLeft Click to Summon a Strider - " + supernatural.getConfig().getInt("Spells.Demon.SummonStrider.Cost") + " Magika"));
		meta.setLore(lore);
		item.setItemMeta(meta);
		SpellIcons.put("SummonStrider", item);

		item = new ItemStack(Material.NETHER_WART);
		meta = item.getItemMeta();
    	meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
    	meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		meta.setUnbreakable(true);
		meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6Create Hellish Potion"));
		lore = new ArrayList<String>();
		lore.add(ChatColor.translateAlternateColorCodes('&', "&eRight Click to Create Hellish Potion - " + supernatural.getConfig().getInt("Spells.Demon.Hellish.Cost") + " Magika and 10 Health"));
		meta.setLore(lore);
		item.setItemMeta(meta);
		SpellIcons.put("Hellish", item);

		item = new ItemStack(Material.IRON_BARS);
		meta = item.getItemMeta();
    	meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
    	meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		meta.setUnbreakable(true);
		meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6Banish Supernatural"));
		lore = new ArrayList<String>();
		lore.add(ChatColor.translateAlternateColorCodes('&', "&eLeft Click to Banish Supernatural - " + supernatural.getConfig().getInt("Spells.Priest.Banish.Cost") + " Magika"));
		meta.setLore(lore);
		item.setItemMeta(meta);
		SpellIcons.put("Banish", item);

		item = new ItemStack(Material.FLINT_AND_STEEL);
		meta = item.getItemMeta();
    	meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
    	meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		meta.setUnbreakable(true);
		meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6Exorcise Supernatural"));
		lore = new ArrayList<String>();
		lore.add(ChatColor.translateAlternateColorCodes('&', "&eLeft Click to Exorcise Supernatural - " + supernatural.getConfig().getInt("Spells.Priest.Exorcise.Cost") + " Magika"));
		meta.setLore(lore);
		item.setItemMeta(meta);
		SpellIcons.put("Exorcise", item);

		item = new ItemStack(Material.STICK);
		meta = item.getItemMeta();
    	meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
    	meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		meta.setUnbreakable(true);
		meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6Drain Supernatural"));
		lore = new ArrayList<String>();
		lore.add(ChatColor.translateAlternateColorCodes('&', "&eLeft Click to Drain Supernatural - " + supernatural.getConfig().getInt("Spells.Priest.Drain.Cost") + " Magika"));
		meta.setLore(lore);
		item.setItemMeta(meta);
		SpellIcons.put("Drain", item);

		item = new ItemStack(Material.PAPER);
		meta = item.getItemMeta();
    	meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
    	meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		meta.setUnbreakable(true);
		meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6Heal Human"));
		lore = new ArrayList<String>();
		lore.add(ChatColor.translateAlternateColorCodes('&', "&eLeft Click to Heal Human - " + supernatural.getConfig().getInt("Spells.Priest.HealHuman.Cost") + " Magika"));
		meta.setLore(lore);
		item.setItemMeta(meta);
		SpellIcons.put("HealHuman", item);

		item = new ItemStack(Material.WHITE_WOOL);
		meta = item.getItemMeta();
    	meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
    	meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		meta.setUnbreakable(true);
		meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6Guardian Angel"));
		lore = new ArrayList<String>();
		lore.add(ChatColor.translateAlternateColorCodes('&', "&eLeft Click to Guardian Angel - " + supernatural.getConfig().getInt("Spells.Priest.GuardianAngel.Cost") + " Magika"));
		meta.setLore(lore);
		item.setItemMeta(meta);
		SpellIcons.put("GuardianAngel", item);

		item = new ItemStack(Material.BAMBOO);
		meta = item.getItemMeta();
    	meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
    	meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		meta.setUnbreakable(true);
		meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6Create Supernatural Cure"));
		lore = new ArrayList<String>();
		lore.add(ChatColor.translateAlternateColorCodes('&', "&eRight Click to Create Supernatural Cure - " + supernatural.getConfig().getInt("Spells.Priest.Cure.Cost") + " Magika"));
		meta.setLore(lore);
		item.setItemMeta(meta);
		SpellIcons.put("Cure", item);
		
		item = new ItemStack(Material.BOOK);
		meta = item.getItemMeta();
    	meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
    	meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		meta.setUnbreakable(true);
		meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6Create Holy Book"));
		lore = new ArrayList<String>();
		lore.add(ChatColor.translateAlternateColorCodes('&', "&eRight Click to Create Holy Book - " + supernatural.getConfig().getInt("Spells.Priest.HolyBook.Cost") + " Magika and 10 Health"));
		meta.setLore(lore);
		item.setItemMeta(meta);
		SpellIcons.put("HolyBook", item);
		
		item = new ItemStack(Material.BONE);
		meta = item.getItemMeta();
    	meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
    	meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		meta.setUnbreakable(true);
		meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6Summon Skeleton"));
		lore = new ArrayList<String>();
		lore.add(ChatColor.translateAlternateColorCodes('&', "&eRight Click to Summon a Skeleton - " + supernatural.getConfig().getInt("Spells.Necromancer.SummonSkeleton.Cost") + " Magika"));
		meta.setLore(lore);
		item.setItemMeta(meta);
		SpellIcons.put("SummonSkeleton", item);
		
		item = new ItemStack(Material.ROTTEN_FLESH);
		meta = item.getItemMeta();
    	meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
    	meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		meta.setUnbreakable(true);
		meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6Summon Undead"));
		lore = new ArrayList<String>();
		lore.add(ChatColor.translateAlternateColorCodes('&', "&eRight Click to Summon an Undead Monster - " + supernatural.getConfig().getInt("Spells.Necromancer.SummonUndeadFollower.Cost") + " Magika"));
		meta.setLore(lore);
		item.setItemMeta(meta);
		SpellIcons.put("SummonUndead", item);

		item = new ItemStack(Material.RED_DYE);
		meta = item.getItemMeta();
    	meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
    	meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		meta.setUnbreakable(true);
		meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6Heal Undead"));
		lore = new ArrayList<String>();
		lore.add(ChatColor.translateAlternateColorCodes('&', "&eRight Click to Heal Nearby Undead - " + supernatural.getConfig().getInt("Spells.Necromancer.HealUndead.Cost") + " Magika"));
		meta.setLore(lore);
		item.setItemMeta(meta);
		SpellIcons.put("HealUndead", item);

		item = new ItemStack(Material.BONE_MEAL);
		meta = item.getItemMeta();
    	meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
    	meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		meta.setUnbreakable(true);
		meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6Ressurection Spawn"));
		lore = new ArrayList<String>();
		lore.add(ChatColor.translateAlternateColorCodes('&', "&eRight Click to set next spawn point - " + supernatural.getConfig().getInt("Spells.Necromancer.RessurectionSpawn.Cost") + " Magika"));
		meta.setLore(lore);
		item.setItemMeta(meta);
		SpellIcons.put("RessurectionSpawn", item);
		
		item = new ItemStack(Material.BOOK);
		meta = item.getItemMeta();
    	meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
    	meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		meta.setUnbreakable(true);
		meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6Create Book Of Death"));
		lore = new ArrayList<String>();
		lore.add(ChatColor.translateAlternateColorCodes('&', "&eRight Click to Create Book Of Death - " + supernatural.getConfig().getInt("Spells.Necromancer.BookOfDeath.Cost") + " Magika and 10 Health"));
		meta.setLore(lore);
		item.setItemMeta(meta);
		SpellIcons.put("BookOfDeath", item);

		item = new ItemStack(Material.BOW);
		meta = item.getItemMeta();
    	meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
    	meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		meta.setUnbreakable(true);
		meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6Bow - Triple Arrow"));
		lore = new ArrayList<String>();
		lore.add(ChatColor.translateAlternateColorCodes('&', "&eLeft Click to Cycle through arrow types"));
		lore.add(ChatColor.translateAlternateColorCodes('&', "&eRight Click to Shoot Triple Arrow - " + supernatural.getConfig().getInt("Spells.Witchhunter.TripleArrow.Cost") + " Magika"));
		meta.setLore(lore);
		item.setItemMeta(meta);
		SpellIcons.put("TripleArrow", item);

		item = new ItemStack(Material.BOW);
		meta = item.getItemMeta();
    	meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
    	meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		meta.setUnbreakable(true);
		meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6Bow - Grapple Arrow"));
		lore = new ArrayList<String>();
		lore.add(ChatColor.translateAlternateColorCodes('&', "&eLeft Click to Cycle through arrow types"));
		lore.add(ChatColor.translateAlternateColorCodes('&', "&eRight Click to Shoot Grapple Arrow - " + supernatural.getConfig().getInt("Spells.Witchhunter.GrappleArrow.Cost") + " Magika"));
		lore.add(ChatColor.translateAlternateColorCodes('&', "&cJump to Cancel Grapple Arrow"));
		meta.setLore(lore);
		item.setItemMeta(meta);
		SpellIcons.put("GrappleArrow", item);

		item = new ItemStack(Material.BOW);
		meta = item.getItemMeta();
    	meta.addEnchant(Enchantment.ARROW_FIRE, 1, true);
    	meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		meta.setUnbreakable(true);
		meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6Bow - Fire Arrow"));
		lore = new ArrayList<String>();
		lore.add(ChatColor.translateAlternateColorCodes('&', "&eLeft Click to Cycle through arrow types"));
		lore.add(ChatColor.translateAlternateColorCodes('&', "&eRight Click to Shoot Fire Arrow - " + supernatural.getConfig().getInt("Spells.Witchhunter.FireArrow.Cost") + " Magika"));
		meta.setLore(lore);
		item.setItemMeta(meta);
		SpellIcons.put("FireArrow", item);

		item = new ItemStack(Material.BOW);
		meta = item.getItemMeta();
    	meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
    	meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		meta.setUnbreakable(true);
		meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6Bow - Power Arrow"));
		lore = new ArrayList<String>();
		lore.add(ChatColor.translateAlternateColorCodes('&', "&eLeft Click to Cycle through arrow types"));
		lore.add(ChatColor.translateAlternateColorCodes('&', "&eRight Click to Shoot Power Arrow - " + supernatural.getConfig().getInt("Spells.Witchhunter.PowerArrow.Cost") + " Magika"));
		meta.setLore(lore);
		item.setItemMeta(meta);
		SpellIcons.put("PowerArrow", item);

		item = new ItemStack(Material.BOW);
		meta = item.getItemMeta();
    	meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
    	meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		meta.setUnbreakable(true);
		meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6Bow - Volley Arrow"));
		lore = new ArrayList<String>();
		lore.add(ChatColor.translateAlternateColorCodes('&', "&eLeft Click to Cycle through arrow types"));
		lore.add(ChatColor.translateAlternateColorCodes('&', "&eRight Click to Shoot Volley Arrow - " + supernatural.getConfig().getInt("Spells.Witchhunter.VolleyArrow.Cost") + " Magika"));
		meta.setLore(lore);
		item.setItemMeta(meta);
		SpellIcons.put("VolleyArrow", item);

		item = new ItemStack(Material.BOOK);
		meta = item.getItemMeta();
    	meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
    	meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		meta.setUnbreakable(true);
		meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6Create Book Of Witch Hunter"));
		lore = new ArrayList<String>();
		lore.add(ChatColor.translateAlternateColorCodes('&', "&eRight Click to Create Book Of Witch Hunter - " + supernatural.getConfig().getInt("Spells.Witchhunter.BookOfWitchHunter.Cost") + " Magika and 10 Health"));
		meta.setLore(lore);
		item.setItemMeta(meta);
		SpellIcons.put("BookOfWitchHunter", item);

		item = new ItemStack(Material.BONE_MEAL);
		meta = item.getItemMeta();
    	meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
    	meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		meta.setUnbreakable(true);
		meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6Holy Smite"));
		lore = new ArrayList<String>();
		lore.add(ChatColor.translateAlternateColorCodes('&', "&eLeft Click an enemy to use Holy Smite on them - " + supernatural.getConfig().getInt("Spells.Angel.HolySmite.Cost") + " Magika"));
		meta.setLore(lore);
		item.setItemMeta(meta);
		SpellIcons.put("HolySmite", item);

		item = new ItemStack(Material.LEAD);
		meta = item.getItemMeta();
    	meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
    	meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		meta.setUnbreakable(true);
		meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6Taunt"));
		lore = new ArrayList<String>();
		lore.add(ChatColor.translateAlternateColorCodes('&', "&eLeft Click someone to use Taunt on them - " + supernatural.getConfig().getInt("Spells.Angel.Taunt.Cost") + " Magika"));
		meta.setLore(lore);
		item.setItemMeta(meta);
		SpellIcons.put("Taunt", item);

		item = new ItemStack(Material.DANDELION);
		meta = item.getItemMeta();
    	meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
    	meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		meta.setUnbreakable(true);
		meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6Holy Blessing"));
		lore = new ArrayList<String>();
		lore.add(ChatColor.translateAlternateColorCodes('&', "&eLeft Click someone to use Holy Blessing on them - " + supernatural.getConfig().getInt("Spells.Angel.HolyBlessing.Cost") + " Magika"));
		meta.setLore(lore);
		item.setItemMeta(meta);
		SpellIcons.put("HolyBlessing", item);

		item = new ItemStack(Material.FEATHER);
		meta = item.getItemMeta();
    	meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
    	meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		meta.setUnbreakable(true);
		meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6Wings"));
		lore = new ArrayList<String>();
		lore.add(ChatColor.translateAlternateColorCodes('&', "&eLeft Click to glide for the next 20s - " + supernatural.getConfig().getInt("Spells.Angel.Wings.Cost") + " Magika"));
		meta.setLore(lore);
		item.setItemMeta(meta);
		SpellIcons.put("Wings", item);
    }
    
	public void DonationValueSetup() {
		for(String key : supernatural.getConfig().getConfigurationSection("DonationValue.HolyAlter").getKeys(true)) {
			HolyDonationValue.put(Material.getMaterial(key), supernatural.getConfig().getInt(("DonationValue.HolyAlter." + key)));
		}
		for(String key : supernatural.getConfig().getConfigurationSection("DonationValue.UnholyAlter").getKeys(true)) {
			UnholyDonationValue.put(Material.getMaterial(key), supernatural.getConfig().getInt(("DonationValue.UnholyAlter." + key)));
		}
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
}
