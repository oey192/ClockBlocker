package com.andoutay.clockblocker;

import org.bukkit.configuration.Configuration;

public class CBConfig
{
	private static Configuration config;
	
	public static int replaceBlock, maxCyclesPerSec;
	public static boolean stopSignal, allowStop, monitorOnLaunch;
	private static ClockBlocker plugin;
	
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
		replaceBlock = config.getInt("replaceBlock");
		stopSignal = config.getBoolean("stopSignal");
		allowStop = config.getBoolean("allowStop");
		monitorOnLaunch = config.getBoolean("monitorOnLaunch");
		maxCyclesPerSec = config.getInt("maxCyclesPerSec");
	}
}