package me.LoneSurvivor.RepairKeys;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

public class RepairKeys extends JavaPlugin implements Listener {
    Map < Player, Integer > KeySlot = new HashMap < Player, Integer > ();

    public void onEnable() {
        this.saveDefaultConfig();
        Bukkit.getPluginManager().registerEvents(this, this);
    }

    public void onDisable() {
        this.saveDefaultConfig();
    }

    @SuppressWarnings("deprecation")
    @EventHandler
    public void RepairKeysGUI(CommandSender sender, String label, String[] args) {
        Player p = (Player) sender;
        if (p.hasPermission("repairkeys.gui")) {
	        ItemStack item = p.getItemInHand();
        	if(!item.hasItemMeta()) {
	            p.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.no-key")));
        		return;
        	}
	        Boolean success = false;
	        for (String key: this.getConfig().getConfigurationSection("RepairKeys").getKeys(false)) {
	            if (item.getItemMeta().getDisplayName().equals(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("RepairKeys." + key + ".Key")))) {
	            	KeySlot.put(p, p.getInventory().getHeldItemSlot());
	                Inventory GUI = Bukkit.createInventory(null, 27, ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("RepairKeys." + key + ".Key")) + " GUI");
	                ItemStack filler = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
	                ItemMeta fillermeta = filler.getItemMeta();
	                fillermeta.setDisplayName(" ");
	                filler.setItemMeta(fillermeta);
	                for (int i = 0; i < GUI.getSize(); i++) {
	                    if (i != 10 && i != 13 && i != 16) {
	                        GUI.setItem(i, filler);
	                    }
	                }
	                p.openInventory(GUI);
	                success = true;
	            }
	        }
	        if(success == false) {
	            p.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.no-key")));
	        }
        } else {
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.insufficient-permission")));
        }
    }

    @EventHandler()
    public void FreezeFillerItems(InventoryClickEvent e) {
        if (e.getClickedInventory().equals(e.getWhoClicked().getInventory())) {
            return;
        }
        if (e.getView().getTitle().contains("Repair Key GUI")) {
            if (e.getSlot() != 10 && e.getSlot() != 13 && e.getSlot() != 16) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler()
    public void NoInputOnOutput(InventoryClickEvent e) {
        if (e.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY) {
            if (e.getClickedInventory().equals(e.getWhoClicked().getInventory())) {
                if (e.getView().getTitle().contains("Repair Key GUI")) {
                    if (e.getInventory().getItem(10) != null && e.getInventory().getItem(13) != null && e.getInventory().getItem(16) == null) {
                        e.setCancelled(true);
                    }
                }
            }
        }
        if (!e.getClickedInventory().equals(e.getWhoClicked().getInventory())) {
            if (e.getView().getTitle().contains("Repair Key GUI")) {
                if (e.getAction() == InventoryAction.HOTBAR_SWAP || e.getAction() == InventoryAction.SWAP_WITH_CURSOR || e.getAction() == InventoryAction.PLACE_ALL || e.getAction() == InventoryAction.PLACE_SOME || e.getAction() == InventoryAction.PLACE_ONE) {
                    if (e.getSlot() == 16) {
                        e.setCancelled(true);
                    }
                }
            }
        }
    }

    @EventHandler()
    public void NoMoveKey(InventoryClickEvent e) {
        if (e.getClickedInventory().equals(e.getWhoClicked().getInventory())) {
            if (e.getView().getTitle().contains("Repair Key GUI")) {
                if (e.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY || e.getAction() == InventoryAction.PICKUP_ALL || e.getAction() == InventoryAction.PICKUP_HALF || e.getAction() == InventoryAction.PICKUP_ONE || e.getAction() == InventoryAction.PICKUP_SOME) {
                    if (e.getSlot() == KeySlot.get((Player) e.getWhoClicked())) {
                        e.setCancelled(true);
                    }
                }
            }
        }
    }

    @SuppressWarnings("deprecation")
    @EventHandler()
    public void RemoveInputOnTakeOutput(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        if (e.getClickedInventory().equals(e.getWhoClicked().getInventory())) {
            return;
        }
        if (e.getView().getTitle().contains("Repair Key GUI")) {
            if (e.getSlot() == 16) {
                if (e.getAction() == InventoryAction.PICKUP_ALL || e.getAction() == InventoryAction.PICKUP_HALF || e.getAction() == InventoryAction.PICKUP_ONE || e.getAction() == InventoryAction.PICKUP_SOME) {
                    e.setCancelled(true);
                    ItemStack item = e.getClickedInventory().getItem(16);
                    ItemStack item2 = e.getClickedInventory().getItem(13);
                    ItemStack item3 = e.getClickedInventory().getItem(10).clone();
                    Damageable item3Meta = (Damageable) item3.getItemMeta();
                    int maxdamage = item3.getType().getMaxDurability();
                    int damage = item3Meta.getDamage();
                    int AmountUsed = 0;
                    while (damage > 0 && AmountUsed < item2.getAmount()) {
                        damage -= maxdamage / 4;
                        AmountUsed += 1;
                    }
                    if (AmountUsed == item2.getAmount()) {
                        e.getClickedInventory().setItem(13, new ItemStack(Material.AIR));
                    } else {
                        item2.setAmount(item2.getAmount() - AmountUsed);
                        e.getClickedInventory().setItem(13, item2);
                    }
                    e.getClickedInventory().setItem(10, new ItemStack(Material.AIR));
                    e.getClickedInventory().setItem(16, new ItemStack(Material.AIR));
                    p.updateInventory();
                    e.setCursor(item);

                    //remove 1 use from key
                    ItemStack key = p.getInventory().getItem(KeySlot.get((Player) e.getWhoClicked()));
                    ItemStack UnusedKeys = new ItemStack(Material.AIR);
                    if (key.getAmount() > 1) {
                        UnusedKeys = key.clone();
                        UnusedKeys.setAmount(UnusedKeys.getAmount() - 1);
                        key.setAmount(1);
                    }
                    ItemMeta keymeta = key.getItemMeta();
                    List < String > keylore = keymeta.getLore();
                    String str = keylore.get(0).replace(ChatColor.translateAlternateColorCodes('&', "&fUses: "), "");
                    String[] str2 = str.split("/");
                    int count = Integer.parseInt(str2[0]) - 1;
                    if (count == 0) {
                        p.getInventory().setItem(KeySlot.get(p), new ItemStack(Material.AIR));
                        if (e.getCursor() != null) {
                            p.getInventory().addItem(e.getCursor());
                            e.setCursor(null);
                        }
                        if (UnusedKeys.getType() != Material.AIR) {
                            p.getInventory().addItem(UnusedKeys);
                        }
                        p.closeInventory();
                        return;
                    } else {
                        keylore.set(0, ChatColor.translateAlternateColorCodes('&', "&fUses: " + count + "/" + str2[1]));
                        keymeta.setLore(keylore);
                        key.setItemMeta(keymeta);
                        if (UnusedKeys.getType() != Material.AIR) {
                            p.getInventory().addItem(UnusedKeys);
                        }
                    }
                }
                if (e.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY) {
                    ItemStack item = e.getClickedInventory().getItem(16);
                    ItemStack item2 = e.getClickedInventory().getItem(13);
                    ItemStack item3 = e.getClickedInventory().getItem(10).clone();
                    Damageable item3Meta = (Damageable) item3.getItemMeta();
                    int maxdamage = item3.getType().getMaxDurability();
                    int damage = item3Meta.getDamage();
                    int AmountUsed = 0;
                    while (damage > 0 && AmountUsed < item2.getAmount()) {
                        damage -= maxdamage / 4;
                        AmountUsed += 1;
                    }
                    if (AmountUsed == item2.getAmount()) {
                        e.getClickedInventory().setItem(13, new ItemStack(Material.AIR));
                    } else {
                        item2.setAmount(item2.getAmount() - AmountUsed);
                        e.getClickedInventory().setItem(13, item2);
                    }
                    e.getClickedInventory().setItem(10, new ItemStack(Material.AIR));
                    e.getClickedInventory().setItem(16, new ItemStack(Material.AIR));
                    p.getInventory().addItem(item);
                    ItemStack cursor = p.getItemOnCursor();
                    p.updateInventory();
                    p.setItemOnCursor(cursor);

                    //remove 1 use from key
                    ItemStack key = p.getInventory().getItem(KeySlot.get((Player) e.getWhoClicked()));
                    ItemStack UnusedKeys = new ItemStack(Material.AIR);
                    if (key.getAmount() > 1) {
                        UnusedKeys = key.clone();
                        UnusedKeys.setAmount(UnusedKeys.getAmount() - 1);
                        key.setAmount(1);
                    }
                    ItemMeta keymeta = key.getItemMeta();
                    List < String > keylore = keymeta.getLore();
                    String str = keylore.get(0).replace(ChatColor.translateAlternateColorCodes('&', "&fUses: "), "");
                    String[] str2 = str.split("/");
                    int count = Integer.parseInt(str2[0]) - 1;
                    if (count == 0) {
                        p.getInventory().setItem(KeySlot.get(p), new ItemStack(Material.AIR));
                        if (e.getCursor() != null) {
                            p.getInventory().addItem(e.getCursor());
                            e.setCursor(null);
                        }
                        if (UnusedKeys.getType() != Material.AIR) {
                            p.getInventory().addItem(UnusedKeys);
                        }
                        p.closeInventory();
                        return;
                    } else {
                        keylore.set(0, ChatColor.translateAlternateColorCodes('&', "&fUses: " + count + "/" + str2[1]));
                        keymeta.setLore(keylore);
                        key.setItemMeta(keymeta);
                        if (UnusedKeys.getType() != Material.AIR) {
                            p.getInventory().addItem(UnusedKeys);
                        }
                        ItemStack cursor2 = p.getItemOnCursor();
                        p.updateInventory();
                        p.setItemOnCursor(cursor2);
                    }
                }
            }
        }
    }

    @EventHandler()
    public void SetOutput(InventoryClickEvent e) {
        if (!e.getView().getTitle().contains("Repair Key GUI")) {
            return;
        }
        Player p = (Player) e.getWhoClicked();
        RepairKeys repairkeys = this;
        this.getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
            public void run() {
                e.getView().setItem(16, new ItemStack(Material.AIR));
                String KeyRarity = p.getInventory().getItem(KeySlot.get((Player) e.getWhoClicked())).getItemMeta().getDisplayName();
                ItemStack item = e.getView().getItem(10);
                if (item == null) {
                    return;
                }
                ItemMeta meta = item.getItemMeta();
                List < String > lore = meta.getLore();
                if (lore == null || lore.size() == 0) {
                    return;
                }
                String ItemRarity = lore.get(lore.size() - 1);
                Boolean raritymatch = false;
                for (String rarity: repairkeys.getConfig().getConfigurationSection("RepairKeys").getKeys(false)) {
                    String Key = ChatColor.translateAlternateColorCodes('&', repairkeys.getConfig().getString("RepairKeys." + rarity + ".Key"));
                    String Item = ChatColor.translateAlternateColorCodes('&', repairkeys.getConfig().getString("RepairKeys." + rarity + ".Item"));
                    if (Key.equals(KeyRarity) && Item.equals(ItemRarity)) {
                        raritymatch = true;
                    }
                }
                if (raritymatch == false) {
                    return;
                }
                ItemStack item1 = e.getView().getItem(10);
                if (item1.getType() == null) {
                    return;
                }
                if (getrepairmaterial(item1.getType()) == null) {
                    return;
                }
                Material[] Valid2ndItems = getrepairmaterial(item1.getType());
                ItemStack item2 = e.getView().getItem(13);
                if (item2 == null) {
                    return;
                }
                Boolean canrepair = false;
                for (Material m: Valid2ndItems) {
                    if (m.equals(item2.getType())) {
                        canrepair = true;
                    }
                }
                if (canrepair == true) {
                    ItemStack item3 = item1.clone();
                    Damageable item3Meta = (Damageable) item3.getItemMeta();
                    int maxdamage = item3.getType().getMaxDurability();
                    int damage = item3Meta.getDamage();
                    damage -= maxdamage / 4 * item2.getAmount();
                    if (damage < 0) {
                        damage = 0;
                    }
                    item3Meta.setDamage(damage);
                    item3.setItemMeta((ItemMeta) item3Meta);
                    e.getView().setItem(16, item3);
                }
                ItemStack cursor = p.getItemOnCursor();
                p.updateInventory();
                p.setItemOnCursor(cursor);
            }
        }, 1L);
    }

    @EventHandler()
    public void dropItems(InventoryCloseEvent e) {
        Player p = (Player) e.getPlayer();
        if (e.getView().getTitle().contains("Repair Key GUI")) {
            if (e.getView().getItem(10) != null) {
                p.getInventory().addItem(e.getView().getItem(10));
            }
            if (e.getView().getItem(13) != null) {
                p.getInventory().addItem(e.getView().getItem(13));
            }
        }
    }

    public Material[] getrepairmaterial(Material input) {
    	/*if (input == Material.NETHERITE_BOOTS || input == Material.NETHERITE_LEGGINGS || input == Material.NETHERITE_CHESTPLATE || input == Material.NETHERITE_HELMET ||
                input == Material.NETHERITE_SWORD || input == Material.NETHERITE_PICKAXE || input == Material.NETHERITE_AXE ||
                input == Material.NETHERITE_SHOVEL || input == Material.NETHERITE_HOE) {
            return new Material[] {
                Material.NETHERITE_INGOT
            };
        }*/
        if (input == Material.DIAMOND_BOOTS || input == Material.DIAMOND_LEGGINGS || input == Material.DIAMOND_CHESTPLATE || input == Material.DIAMOND_HELMET ||
            input == Material.DIAMOND_SWORD || input == Material.DIAMOND_PICKAXE || input == Material.DIAMOND_AXE ||
            input == Material.DIAMOND_SHOVEL || input == Material.DIAMOND_HOE) {
            return new Material[] {
                Material.DIAMOND
            };
        }
        if (input == Material.IRON_BOOTS || input == Material.IRON_LEGGINGS || input == Material.IRON_CHESTPLATE || input == Material.IRON_HELMET ||
            input == Material.IRON_SWORD || input == Material.IRON_PICKAXE || input == Material.IRON_AXE ||
            input == Material.IRON_SHOVEL || input == Material.IRON_HOE) {
            return new Material[] {
                Material.IRON_INGOT
            };
        }
        if (input == Material.GOLDEN_BOOTS || input == Material.GOLDEN_LEGGINGS || input == Material.GOLDEN_CHESTPLATE || input == Material.GOLDEN_HELMET ||
            input == Material.GOLDEN_SWORD || input == Material.GOLDEN_PICKAXE || input == Material.GOLDEN_AXE ||
            input == Material.GOLDEN_SHOVEL || input == Material.GOLDEN_HOE) {
            return new Material[] {
                Material.GOLD_INGOT
            };
        }
        if (input == Material.STONE_SWORD || input == Material.STONE_PICKAXE || input == Material.STONE_AXE ||
            input == Material.STONE_SHOVEL || input == Material.STONE_HOE) {
            return new Material[] {
                Material.COBBLESTONE, //Material.BLACKSTONE
            };
        }
        if (input == Material.LEATHER_BOOTS || input == Material.LEATHER_LEGGINGS || input == Material.LEATHER_CHESTPLATE || input == Material.LEATHER_HELMET) {
            return new Material[] {
                Material.LEATHER
            };
        }
        if (input == Material.WOODEN_SWORD || input == Material.WOODEN_PICKAXE || input == Material.WOODEN_AXE ||
            input == Material.WOODEN_SHOVEL || input == Material.WOODEN_HOE) {
            return new Material[] {
                Material.OAK_PLANKS, /*Material.CRIMSON_PLANKS, Material.WARPED_PLANKS,*/ Material.ACACIA_PLANKS, Material.BIRCH_PLANKS, Material.DARK_OAK_PLANKS, Material.JUNGLE_PLANKS, Material.SPRUCE_PLANKS
            };
        }
        if (input == Material.ELYTRA) {
            return new Material[] {
                Material.PHANTOM_MEMBRANE
            };
        }
        if (input == Material.TURTLE_HELMET) {
            return new Material[] {
                Material.SCUTE
            };
        }
        return null;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (label.equalsIgnoreCase("repairkeys") || label.equalsIgnoreCase("rk")) {
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
                if (args[0].equalsIgnoreCase("givekey") || args[0].equalsIgnoreCase("gk")) {
                    GiveKeyCommand(sender, label, args);
                    return true;
                }
                if (args[0].equalsIgnoreCase("addrarity") || args[0].equalsIgnoreCase("ar")) {
                    AddRarityCommand(sender, label, args);
                    return true;
                }
            }
        }
        if (label.equalsIgnoreCase("rkgui")) {
        	RepairKeysGUI(sender, label, args);
        	return true;
        }
        return false;
    }

    public void HelpCommand(CommandSender sender, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("repairkeys.help")) {
                player.sendMessage(ChatColor.GRAY + "-------------------");
                player.sendMessage(ChatColor.GREEN + "RepairKeys Commands");
                player.sendMessage(ChatColor.GRAY + "-------------------");
                if (player.hasPermission("repairkeys.help")) {
                    player.sendMessage(ChatColor.GREEN + "/rk help");
                }
                if (player.hasPermission("repairkeys.reload")) {
                    player.sendMessage(ChatColor.GREEN + "/rk reload");
                }
                if (player.hasPermission("repairkeys.givekey")) {
                    player.sendMessage(ChatColor.GREEN + "/rk givekey <player> <rarity> <quantity> <uses>");
                }
                if (player.hasPermission("repairkeys.addrarity")) {
                    player.sendMessage(ChatColor.GREEN + "/rk addrarity <rarity>");
                }
            } else {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.insufficient-permission")));
            }
        } else {
            System.out.print("-------------------");
            System.out.print("RepairKeys Commands");
            System.out.print("-------------------");
            System.out.print("/rk help");
            System.out.print("/rk reload");
            System.out.print("/rk givekey <player> <rarity> <quantity> <uses>");
            System.out.print("/rk addrarity <rarity>");
        }
        return;
    }

    public void ReloadCommand(CommandSender sender, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("repairkeys.reload")) {
                this.saveDefaultConfig();
                this.reloadConfig();
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.prefix") + " " + this.getConfig().getString("Messages.reload-success")));
                return;
            } else {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.insufficient-permission")));
            }
        } else {
            this.saveDefaultConfig();
            this.reloadConfig();
            System.out.println(ChatColor.stripColor(this.getConfig().getString("Messages.prefix") + " " + this.getConfig().getString("Messages.reload-success")));
        }
    }

    public void GiveKeyCommand(CommandSender sender, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("repairkeys.givekey")) {
                Player target = Bukkit.getPlayer(args[1]);
                if (target == null) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.player-not-found")));
                    return;
                }
                String type = args[2];
                int quantity = 1;
                int uses = 5;
                if (args.length > 3) {
                    quantity = Integer.parseInt(args[3]);
                }
                if (args.length > 4) {
                    uses = Integer.parseInt(args[4]);
                }
                ItemStack key = new ItemStack(Material.TRIPWIRE_HOOK);
                ItemMeta meta = key.getItemMeta();
                List < String > lore = new ArrayList < String > ();
                if(!this.getConfig().contains("RepairKeys." + type + ".Key")) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.unknown-rarity")));
                    return;
                }
                String str = ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("RepairKeys." + type + ".Key"));
                meta.setDisplayName(str);
                key.setAmount(quantity);
                lore.add(ChatColor.translateAlternateColorCodes('&', "&fUses: " + uses + "/" + uses));
                meta.setLore(lore);
                key.setItemMeta(meta);
                target.getInventory().addItem(key);
            } else {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.insufficient-permission")));
            }
        } else {

        }
        return;
    }

    @SuppressWarnings("deprecation")
    public void AddRarityCommand(CommandSender sender, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("repairkeys.addrarity")) {
                ItemStack item = player.getItemInHand();
                ItemMeta meta = item.getItemMeta();
                List < String > lore = new ArrayList < String > ();
                if(!this.getConfig().contains("RepairKeys." + args[1] + ".Item")) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.unknown-rarity")));
                    return;
                }
                String str = ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("RepairKeys." + args[1] + ".Item"));
                lore.add(str);
                meta.setLore(lore);
                item.setItemMeta(meta);
            } else {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.insufficient-permission")));
            }
        } else {

        }
        return;
    }
}