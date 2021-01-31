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
    
    public String addSpaces(String sstr) {
        String output = "";
        char[] str=sstr.toCharArray(); 
        // Traverse the string 
        for (int i=0; i < str.length; i++) { 
            if (str[i]>='&' && str[i]<='Z' && i != 0) {
            	output += " ";
            }
            output += str[i];
        }
        return output;
    }     
    
    private String ConstructLoreLine(String directoryName) {
    	String line = "&e";
		Boolean firstArgument = true;
		int magicCost = supernatural.getConfig().getInt("Spells." + directoryName + ".Magic-Cost");
		int foodCost = supernatural.getConfig().getInt("Spells." + directoryName + ".Food-Cost");
		int healthCost = supernatural.getConfig().getInt("Spells." + directoryName + ".Health-Cost");
    	if(supernatural.getConfig().getString("Spells." + directoryName + ".TriggerMethod").equals("right")) {
    		line += "Right Click to use ";
    	}
    	if(supernatural.getConfig().getString("Spells." + directoryName + ".TriggerMethod").equals("left")) {
    		line += "Left Click to use ";
    	}
    	if(supernatural.getConfig().getString("Spells." + directoryName + ".TriggerMethod").equals("hit")) {
    		line += "Hit to use ";
    	}
    	if(supernatural.getConfig().getString("Spells." + directoryName + ".TriggerMethod").equals("shoot")) {
    		line += "Shoot to use ";
    	}
    	line += ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', supernatural.getConfig().getString("Spells." + directoryName + ".SpellName")));
		if(magicCost > 0) {
			if(firstArgument) {
				line += "&e - ";
				firstArgument = false;
			} else {
				line += " & ";
			}
			line += magicCost + " Magika";
		}
		if(foodCost > 0) {
			if(firstArgument) {
				line += "&e - ";
				firstArgument = false;
			} else {
				line += " & ";
			}
			line += foodCost + " Hunger";
		}
		if(healthCost > 0) {
			if(firstArgument) {
				line += "&e - ";
				firstArgument = false;
			} else {
				line += " & ";
			}
			line += healthCost + " Health";
		}
		return line;
    }
    
    public ItemStack createSpellIcon(String[] directoryNames) {
    	try {
    		ItemStack item = new ItemStack(Material.getMaterial(supernatural.getConfig().getString("Spells." + directoryNames[0] + ".IconMaterial")));
        	ItemMeta meta = item.getItemMeta();
        	List<String> lore = new ArrayList<String>();
        	meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
    		meta.setUnbreakable(true);
        	meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
    		meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
    		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', supernatural.getConfig().getString("Spells." + directoryNames[0] + ".SpellName")));
			lore.add(ChatColor.translateAlternateColorCodes('&', "&6----------------------"));
    		for(String directoryName : directoryNames) {
    			lore.add(ChatColor.translateAlternateColorCodes('&', ConstructLoreLine(directoryName)));
    			if(!supernatural.getConfig().getString("Spells." + directoryName + ".Description").equals("")) {
    				lore.add(ChatColor.translateAlternateColorCodes('&', supernatural.getConfig().getString("Spells." + directoryName + ".Description")));
    			}
    			lore.add(ChatColor.translateAlternateColorCodes('&', "&6----------------------"));
    		}
    		meta.setLore(lore);
    		item.setItemMeta(meta);
    		return item;
    	} catch(Exception e) {
    		ItemStack item = new ItemStack(Material.BARRIER);
    		ItemMeta meta = item.getItemMeta();
			meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&4Error: &6" + directoryNames[0]));
			item.setItemMeta(meta);
			System.out.println("[Supernatural] Error: Failed to create the spell icon for this spell --> '" + directoryNames[0] + "'. Its icon replaced by a placeholder icon for the time being, please fix the problem and restart!");
    		return item;
    	}
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

		SpellIcons.put("ToggleRegenerationEnabled", createSpellIcon(new String[] {"Vampire.ToggleRegenerationEnabled"}));
		
		SpellIcons.put("ToggleRegenerationDisabled", createSpellIcon(new String[] {"Vampire.ToggleRegenerationDisabled"}));
		
		SpellIcons.put("ToggleWaterBreathingEnabled", createSpellIcon(new String[] {"Vampire.ToggleWaterBreathingEnabled"}));
		
		SpellIcons.put("ToggleWaterBreathingDisabled", createSpellIcon(new String[] {"Vampire.ToggleWaterBreathingDisabled"}));

		SpellIcons.put("Teleport", createSpellIcon(new String[] {"Vampire.Teleport", "Vampire.SetTeleportLocation"}));

		SpellIcons.put("HighJump", createSpellIcon(new String[] {"Vampire.HighJump"}));

		SpellIcons.put("Bloodvial", createSpellIcon(new String[] {"Vampire.Bloodvial"}));

		SpellIcons.put("Bloodrose", createSpellIcon(new String[] {"Vampire.Bloodrose"}));

		SpellIcons.put("SummonWolf", createSpellIcon(new String[] {"Werewolf.SummonWolf"}));
		
		SpellIcons.put("Dash", createSpellIcon(new String[] {"Werewolf.Dash"}));
		
		SpellIcons.put("Moonflower", createSpellIcon(new String[] {"Werewolf.Moonflower"}));
		
		SpellIcons.put("SummonMonster", createSpellIcon(new String[] {"Ghoul.SummonMonster"}));
		
		SpellIcons.put("UnholyBond", createSpellIcon(new String[] {"Ghoul.UnholyBond"}));
		
		SpellIcons.put("Ghoulish", createSpellIcon(new String[] {"Ghoul.Ghoulish"}));
		
		SpellIcons.put("Fireball", createSpellIcon(new String[] {"Demon.Fireball"}));
		
		SpellIcons.put("Explosion", createSpellIcon(new String[] {"Demon.Explosion"}));
		
		SpellIcons.put("Snare", createSpellIcon(new String[] {"Demon.Snare"}));
		
		SpellIcons.put("SummonStrider", createSpellIcon(new String[] {"Demon.SummonStrider"}));
		
		SpellIcons.put("Hellish", createSpellIcon(new String[] {"Demon.Hellish"}));
		
		SpellIcons.put("Banish", createSpellIcon(new String[] {"Priest.Banish"}));
		
		SpellIcons.put("Exorcise", createSpellIcon(new String[] {"Priest.Exorcise"}));
		
		SpellIcons.put("Drain", createSpellIcon(new String[] {"Priest.Drain"}));
		
		SpellIcons.put("HealHuman", createSpellIcon(new String[] {"Priest.HealHuman"}));
		
		SpellIcons.put("GuardianAngel", createSpellIcon(new String[] {"Priest.GuardianAngel"}));
		
		SpellIcons.put("Cure", createSpellIcon(new String[] {"Priest.Cure"}));
		
		SpellIcons.put("HolyBook", createSpellIcon(new String[] {"Priest.HolyBook"}));
		
		SpellIcons.put("SummonSkeleton", createSpellIcon(new String[] {"Necromancer.SummonSkeleton"}));
		
		SpellIcons.put("SummonUndead", createSpellIcon(new String[] {"Necromancer.SummonUndead"}));
		
		SpellIcons.put("HealUndead", createSpellIcon(new String[] {"Necromancer.HealUndead"}));
		
		SpellIcons.put("SetRessurectionSpawn", createSpellIcon(new String[] {"Necromancer.SetRessurectionSpawn"}));
		
		SpellIcons.put("BookOfDeath", createSpellIcon(new String[] {"Necromancer.BookOfDeath"}));
		
		SpellIcons.put("TripleArrow", createSpellIcon(new String[] {"WitchHunter.TripleArrow", "WitchHunter.ArrowSwitcher"}));
		
		SpellIcons.put("GrappleArrow", createSpellIcon(new String[] {"WitchHunter.GrappleArrow", "WitchHunter.ArrowSwitcher"}));
		
		SpellIcons.put("FireArrow", createSpellIcon(new String[] {"WitchHunter.FireArrow", "WitchHunter.ArrowSwitcher"}));
		
		SpellIcons.put("PowerArrow", createSpellIcon(new String[] {"WitchHunter.PowerArrow", "WitchHunter.ArrowSwitcher"}));
		
		SpellIcons.put("VolleyArrow", createSpellIcon(new String[] {"WitchHunter.VolleyArrow", "WitchHunter.ArrowSwitcher"}));
		
		SpellIcons.put("BookOfWitchHunter", createSpellIcon(new String[] {"WitchHunter.BookOfWitchHunter"}));
		
		SpellIcons.put("HolySmite", createSpellIcon(new String[] {"Angel.HolySmite"}));
		
		SpellIcons.put("Taunt", createSpellIcon(new String[] {"Angel.Taunt"}));
		
		SpellIcons.put("HolyBlessing", createSpellIcon(new String[] {"Angel.HolyBlessing"}));
		
		SpellIcons.put("Wings", createSpellIcon(new String[] {"Angel.Wings"}));
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
