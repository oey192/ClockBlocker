package com.andoutay.clockstopper;

import java.util.logging.Logger;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockRedstoneEvent;

public class CBBlockRedstoneHandler implements Listener
{
	private Logger log = Logger.getLogger("Minecraft");
	
	@EventHandler
	public void onBLockRedstone(BlockRedstoneEvent evt)
	{
		long startTime = System.currentTimeMillis();
		
		
		
		
		
		
		
		
		log.info("Done! Took " + ((System.currentTimeMillis() - startTime) / 1000.0) + "s!");
	}
}
