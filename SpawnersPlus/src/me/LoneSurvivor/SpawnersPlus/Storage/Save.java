package me.LoneSurvivor.SpawnersPlus.Storage;

import java.util.Map;
import me.LoneSurvivor.SpawnersPlus.Files.DataManager;

import org.bukkit.Location;

public class Save {
	public void SaveSpawnerLevels(Map <Location, int[]> SpawnerLevels, DataManager data) {
        data.getConfig().set("spawner-levels", null);
		if (!SpawnerLevels.isEmpty()) {
            for (Map.Entry < Location, int[] > entry: SpawnerLevels.entrySet()) {
                Location l = entry.getKey();
                String SerializedLocation = l.getWorld().getName() + ":" + l.getBlockX() + ":" + l.getBlockY() + ":" + l.getBlockZ();
                String SerializedLevels = entry.getValue()[0] + ":" + entry.getValue()[1] + ":" + entry.getValue()[2];
                data.getConfig().set("spawner-levels." + SerializedLocation, SerializedLevels);
            }
            data.saveConfig();
        }
	}
	
}