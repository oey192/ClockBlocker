package com.andoutay.clockblocker;

import java.util.List;

import org.bukkit.configuration.Configuration;

public class CBConfig
{
	private static Configuration config;
	
	public static int maxCyclesPerMin, replaceBlock;
	public static boolean allowStop, monitorOnLaunch, dropRedstone;
	private static ClockBlocker plugin;
	public static List<String> worldBlacklist; 
	
	CBConfig(ClockBlocker plugin)
	{
		CBConfig.plugin = plugin;
		config = plugin.getConfig().getRoot();
		if (config.getString("stopSignal") == null || config.getString("monitorOnLaunch") == null);
			config.options().copyDefaults(true);
		plugin.saveConfig();
	}
	
	public static void onEnable()
	{
		loadConfigVals();
	}
	
	public static void reload()
	{
		plugin.reloadConfig();
		config = plugin.getConfig().getRoot();
		onEnable();
	}
	
	private static void loadConfigVals()
	{
		allowStop = config.getBoolean("allowStop");
		monitorOnLaunch = config.getBoolean("monitorOnLaunch");
		maxCyclesPerMin = config.getInt("maxCyclesPerMin");
		dropRedstone = config.getBoolean("dropRedstone");
		replaceBlock = config.getInt("replaceBlock");
		worldBlacklist = config.getStringList("worldBlacklist");
	}

	
}
