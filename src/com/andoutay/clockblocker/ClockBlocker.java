package com.andoutay.clockblocker;

import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class ClockBlocker extends JavaPlugin
{
	private Logger log = Logger.getLogger("Minecraft");
	public static String chPref = ChatColor.DARK_RED + "[CB] " + ChatColor.RESET;
	public static String logPref = "[ClockBlocker] ";
	
	@Override
	public void onEnable()
	{
		PluginManager pm = getServer().getPluginManager();
		
		pm.registerEvents(new CBBlockRedstoneHandler(), this);
		log.info(logPref + "enabled successfully!");
	}
	
	
	@Override
	public void onDisable()
	{
		log.info(logPref + "disabled successfully!");
	}
}
