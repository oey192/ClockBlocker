package com.andoutay.clockblocker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.logging.Logger;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.inventory.ItemStack;

public class CBBlockRedstoneHandler implements Listener
{
	private Logger log = Logger.getLogger("Minecraft");
	private long lastTimeChecked;
	private ClockBlocker plugin;
	private HashMap<Location, Long> std;
	private HashMap<Location, Integer> suspicious;
	private ArrayList<Location> clockblocks;
	
	CBBlockRedstoneHandler(ClockBlocker plugin)
	{
		this.plugin = plugin;
		std = new HashMap<Location, Long>();
		suspicious = new HashMap<Location, Integer>();
		clockblocks = new ArrayList<Location>();
		lastTimeChecked = System.currentTimeMillis();
	}
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent evt)
	{
		if (evt.getBlock().getState() instanceof Sign)
		{
			Sign s = (Sign)evt.getBlock().getState();
			if (s.getLine(1).equalsIgnoreCase("removed by") && s.getLine(2).equalsIgnoreCase("clockblocker"))
			{
				int id = 3;
				try
				{
					id = Integer.parseInt(s.getLine(3));
				}
				catch (NumberFormatException e)
				{

				}
				evt.getBlock().setTypeId(0);
				if (evt.getPlayer().getGameMode() != GameMode.CREATIVE) evt.getPlayer().getInventory().addItem(new ItemStack(id));
				evt.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onBLockRedstone(BlockRedstoneEvent evt)
	{
		long startTime = System.currentTimeMillis();
		doBlockRedstone(evt);
		log.info("Done! Took " + ((System.currentTimeMillis() - startTime) / 1000.0) + "s!");
	}
	
	private void doBlockRedstone(BlockRedstoneEvent evt)
	{
		if (plugin.shouldMonitor() && evt.getNewCurrent() >= 1)
		{
			long startTime = System.currentTimeMillis();
			Location coord = evt.getBlock().getLocation();
			
			if (std.containsKey(coord))
			{
				// * 1.1 is to add a 10% margin of error so we suspect more things
				if ((startTime - std.get(coord)) < (60.0 / CBConfig.maxCyclesPerMin) * 1.1 * 1000)
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
				if (CBConfig.stopSignal) evt.setNewCurrent(0);
				makeSign(evt, coord);
			}
			else
				std.put(coord, startTime);

			if (startTime - lastTimeChecked > 60 * 1000)
			{
				Set<Location> temp = suspicious.keySet();
				for (Location p : temp)
					if (suspicious.get(p) > CBConfig.maxCyclesPerMin)
						clockblocks.add(p);
					else
						std.put(p, startTime);
				
				suspicious.clear();
				lastTimeChecked = startTime;
			}
		}
	}
	
	private void makeSign(BlockRedstoneEvent evt, Location coord)
	{
		Block b = evt.getBlock();
		int oldId = ((ItemStack)b.getDrops().toArray()[0]).getTypeId();
		b.setTypeId(63);
		Sign s = null;
		if (b.getState() instanceof Sign)
		{
			s = (Sign) b.getState();
			s.setLine(1, "Removed by");
			s.setLine(2, "ClockBlocker");
			s.setLine(3, "" + oldId);//TODO: add SignChangeEvent listener, cancel that event if block is a sign and the first two lines match
			s.update();
		}
		
		clockblocks.remove(coord);
	}
	
	public HashMap<Location, Integer> getSuspicious()
	{
		return suspicious;
	}
}
