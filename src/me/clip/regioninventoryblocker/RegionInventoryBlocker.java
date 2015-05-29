package me.clip.regioninventoryblocker;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class RegionInventoryBlocker extends JavaPlugin {
	
	private WorldGuardHook worldguardHook;
	
	private Map<String, List<String>> regions;
	
	private String message;
	
	@Override
	public void onEnable() {
		
		worldguardHook = new WorldGuardHook();
		
		if (worldguardHook.isWorldGuardHooked()) {

			getLogger().info("Successfully hooked into WorldGuard");
			
			loadDefaultConfig();
			
			getLogger().info(loadRegions() + " inventory blocked regions loaded");
			
			loadMessage();
			
			getCommand("regioninventoryblocker").setExecutor(new RegionInventoryBlockerCommands(this));
			
			new InventoryListener(this);
			
		} else {
		
			getLogger().severe("Could not hook into WorldGuard! RegionInventoryBlocker will now disable!");
			Bukkit.getPluginManager().disablePlugin(this);
		}
	}
	
	private void loadDefaultConfig() {
		
		FileConfiguration c = getConfig();
		
		c.options().header("RegionInventoryBlocker version " + getDescription().getVersion() + " configuration file"
				+ "\nWorldGuard returns region names in lower case\nMake sure all regions listed are lower case!");
		
		if (!c.contains("regions")) {
			
			if (!c.isConfigurationSection("regions")) {
				c.set("regions.world", Arrays.asList(new String[] {
					"region1", "region2"
				}));
			}
		}
		
		c.addDefault("message", "&cYou can't open inventories in this region!");
		
		c.options().copyDefaults(true);
		
		saveConfig();
		
		reloadConfig();
	}
	
	protected int loadRegions() {
		
		FileConfiguration c = getConfig();
		
		regions = new HashMap<String, List<String>>();
		
		if (!c.contains("regions") || !c.isConfigurationSection("regions")) {
			return 0;
		}
		
		Set<String> worlds = c.getConfigurationSection("regions").getKeys(false);
		
		if (worlds == null || worlds.isEmpty()) {
			return 0;
		}
		
		int loaded = 0;
			
		for (String world : worlds) {
			
			if (!c.isList("regions."+world)) {
				continue;
			}
			
			List<String> worldRegions = c.getStringList("regions."+world);
			
			if (worldRegions == null || worldRegions.isEmpty()) {
				continue;
			}
			
			loaded = loaded+worldRegions.size();
			
			regions.put(world, worldRegions);	
		}
			
		return loaded;
	}
	
	protected void loadMessage() {
		message = getConfig().getString("message");
	}
	
	public boolean isNoInventoryRegion(Location l) {
		
		if (regions == null) {
			return false;
		}
		
		String world = l.getWorld().getName();
		
		if (!regions.containsKey(world)) {
			return false;
		}
		
		String region = worldguardHook.getRegion(l);
		
		if (region == null) {
			return false;
		}
		
		return regions.get(world) != null && regions.get(world).contains(region);
	}
	
	public String getMessage() {
		return message;
	}

}
