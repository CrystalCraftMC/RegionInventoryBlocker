package me.clip.regioninventoryblocker;

import java.util.Iterator;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class WorldGuardHook {

	private WorldGuardPlugin worldguard;
	
	public WorldGuardHook() {
		
		if (Bukkit.getPluginManager().isPluginEnabled("WorldGuard")) {
			
			worldguard = (WorldGuardPlugin) Bukkit.getPluginManager().getPlugin("WorldGuard");
		}
	}
	
	public boolean isWorldGuardHooked() {
		return worldguard != null;
	}
	
	public String getRegion(Location l) {
		
		if (worldguard == null) {
			return null;
		}
		
		ApplicableRegionSet regions = worldguard.getRegionManager(l.getWorld()).getApplicableRegions(l);
		
		if (regions == null) {
			return null;
		}
		
		Iterator<ProtectedRegion> iter = regions.iterator();
			
		ProtectedRegion reg = null;
		
		while (iter.hasNext()) {
			
			reg = iter.next();
			
			return reg.getId();
		}
		
		return null;
	}
}
