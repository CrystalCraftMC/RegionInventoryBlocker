package me.clip.regioninventoryblocker;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RegionInventoryBlockerCommands implements CommandExecutor {
	
	RegionInventoryBlocker plugin;
	
	public RegionInventoryBlockerCommands(RegionInventoryBlocker i) {
		plugin = i;
	}
	
	private void sms(CommandSender s, String msg) {
		s.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
	}

	@Override
	public boolean onCommand(CommandSender s, Command c, String label,
			String[] args) {

		if (s instanceof Player) {
			Player p = (Player) s;
			
			if (!p.hasPermission("regioninventoryblocker.admin")) {
				sms(p, "&cYou don't have permission to do that!");
				return true;
			}
		}
		
		if (args.length == 0) {
			
			sms(s, "&8&m+----------------+");
			sms(s, "&cRegionInventoryBlocker &f&o"+plugin.getDescription().getVersion());
			sms(s, "&7Created by: &f&oextended_clip");
			sms(s, "&8&m+----------------+");
			
		} else if (args.length != 0 && args[0].equalsIgnoreCase("help")) {
			
			sms(s, "&8&m+----------------+");
			sms(s, "&cRegionInventoryBlocker &f&oHelp");
			sms(s, "&7/rib - &fshow plugin version");
			sms(s, "&7/rib reload - &freload config");
			sms(s, "&8&m+----------------+");
			return true;
			
		} else if (args.length != 0 && args[0].equalsIgnoreCase("reload")) {
			
			plugin.reloadConfig();
			plugin.saveConfig();
			int loaded = plugin.loadRegions();
			plugin.loadMessage();
			sms(s, "&8&m+----------------+");
			sms(s, "&7Configuration successfully reloaded!");
			sms(s, loaded + " region(s) will prevent inventories from being clicked!");
			sms(s, "&8&m+----------------+");
			return true;
			
		} else {
			
			sms(s, "&cIncorrect usage! Use /rib help!");
		}
		return true;
	}
	


}
