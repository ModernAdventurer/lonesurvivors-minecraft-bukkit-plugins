package me.LoneSurvivor.SpawnersPlus.Storage;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import me.LoneSurvivor.SpawnersPlus.Files.DataManager;

public class Load {
	public Map <Location, int[]> SpawnerLevels(DataManager data) {
	    Map < Location, int[] > SpawnerLevels = new HashMap < Location, int[] > ();
	    if (data.getConfig().getConfigurationSection("spawner-levels").getKeys(false).size() != 0) {
	        data.getConfig().getConfigurationSection("spawner-levels").getKeys(false).forEach(key -> {
	            int[] content = new int[data.getConfig().getString("spawner-levels." + key).split(":").length];
	            for (int i = 0; i < data.getConfig().getString("spawner-levels." + key).split(":").length; i++) {
	                content[i] = Integer.parseInt(data.getConfig().getString("spawner-levels." + key).split(":")[i]);
	            }
	            final String[] locationparts = key.split(":");
	            Location location = new Location(Bukkit.getServer().getWorld(locationparts[0]), Integer.parseInt(locationparts[1]),
	                Integer.parseInt(locationparts[2]), Integer.parseInt(locationparts[3]));
	            SpawnerLevels.put(location, content);
	        });
	        data.getConfig().set("spawner-levels", null);
	        data.saveConfig();
	    }
	    return SpawnerLevels;
	}
}
