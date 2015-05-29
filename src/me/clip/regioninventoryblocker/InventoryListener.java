package me.clip.regioninventoryblocker;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryListener implements Listener {
	
	private RegionInventoryBlocker plugin;
	
	public InventoryListener(RegionInventoryBlocker instance) {
		
		plugin = instance;
		
		Bukkit.getPluginManager().registerEvents(this, instance);
	}
	
	@EventHandler
	public void onClick(InventoryClickEvent e) {
		
		if (!(e.getWhoClicked() instanceof Player)) {
			return;
		}
		
		Player p = (Player) e.getWhoClicked();
		
		if (p.hasPermission("regioninventoryblocker.bypass")) {
			return;
		}
		
		if (plugin.isNoInventoryRegion(p.getLocation())) {
			
			e.setCancelled(true);
			
			p.closeInventory();
			
			if (plugin.getMessage() != null && !plugin.getMessage().isEmpty()) {
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getMessage()));
			}
		}
	}
}
