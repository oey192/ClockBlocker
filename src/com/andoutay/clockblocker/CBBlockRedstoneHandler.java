package com.andoutay.clockblocker;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Set;
import java.util.logging.Logger;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockRedstoneEvent;

public class CBBlockRedstoneHandler implements Listener
{
	private Logger log = Logger.getLogger("Minecraft");
	private long lastTimeChecked;
	private ClockBlocker plugin;
	private HashMap<CBPoint3, Long> std;
	private HashMap<CBPoint3, Integer> suspicious;
	private ArrayList<CBPoint3> clockblocks;
	
	CBBlockRedstoneHandler(ClockBlocker plugin)
	{
		this.plugin = plugin;
		std = new HashMap<CBPoint3, Long>();
		suspicious = new HashMap<CBPoint3, Integer>();
		clockblocks = new ArrayList<CBPoint3>();
		lastTimeChecked = System.currentTimeMillis();
	}
	
	@EventHandler
	public void onBLockRedstone(BlockRedstoneEvent evt)
	{
		if (plugin.shouldMonitor() && evt.getNewCurrent() >= 1)
		{
			long startTime = System.currentTimeMillis();
			CBPoint3 coord = new CBPoint3(evt.getBlock());
			
			if (std.containsKey(coord))
			{
				if ((startTime - std.get(coord)) < 2000.0 / CBConfig.maxCyclesPerSec)
				{
					std.remove(coord);
					suspicious.put(coord, 1);
				}
				else
					std.put(coord, startTime);
			}
			else if (suspicious.containsKey(coord))
				suspicious.put(coord, suspicious.get(coord) + 1);
			else if (clockblocks.contains(coord))
			{
				if (CBConfig.stopSignal)
					evt.setNewCurrent(0);
				else
					evt.getBlock().setTypeId(CBConfig.replaceBlock);
				clockblocks.remove(coord);
			}
			else
				std.put(coord, startTime);
			
			if (startTime - lastTimeChecked > 60 * 1000)
			{
				Set<CBPoint3> temp = suspicious.keySet();
					for (CBPoint3 p : temp)
					{
						if (suspicious.get(p) > 60 * CBConfig.maxCyclesPerSec)
							clockblocks.add(p);
						else
							std.put(p, startTime);
					}
					suspicious.clear();
				
				lastTimeChecked = startTime;
			}
			
			log.info("Done! Took " + ((System.currentTimeMillis() - startTime) / 1000.0) + "s!");
		}
	}
}
