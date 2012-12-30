package com.andoutay.clockstopper;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class ClockStopper extends JavaPlugin
{
	@Override
	public void onEnable()
	{
		PluginManager pm = getServer().getPluginManager();
		
		pm.registerEvents(new CSBlockRedstoneHandler(), this);
	}
	
	
	@Override
	public void onDisable()
	{
		
	}
}
