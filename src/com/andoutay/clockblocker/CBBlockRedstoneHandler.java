package com.andoutay.clockblocker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
//import java.util.logging.Logger;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.inventory.ItemStack;

public class CBBlockRedstoneHandler implements Listener
{
	//private Logger log = Logger.getLogger("Minecraft");
	private long lastTimeChecked;
	private ClockBlocker plugin;
	private HashMap<Location, Long> std;
	private HashMap<Location, Integer> suspicious;
	private HashMap<Location, Long> signs;
	private ArrayList<Location> clockblocks;
	
	CBBlockRedstoneHandler(ClockBlocker plugin)
	{
		this.plugin = plugin;
		std = new HashMap<Location, Long>();
		suspicious = new HashMap<Location, Integer>();
		clockblocks = new ArrayList<Location>();
		signs = new HashMap<Location, Long>();
		lastTimeChecked = System.currentTimeMillis();
	}
	
	@EventHandler
	public void onSignChange(SignChangeEvent evt)
	{
		if (evt.getBlock().getState() instanceof Sign)
		{
			Sign s = (Sign)evt.getBlock().getState();
			if (s.getLine(1).equalsIgnoreCase("removed by") && s.getLine(2).equalsIgnoreCase("clockblocker"))
				evt.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent evt)
	{
		Block b = evt.getBlock();
		if (b.getState() instanceof Sign)
		{
			Sign s = (Sign)b.getState();
			if (s.getLine(1).equalsIgnoreCase("removed by") && s.getLine(2).equalsIgnoreCase("clockblocker"))
			{
				String msg = ClockBlocker.chPref + "Removed a block of a clock or other quickly repeating circuit";
				b.setTypeId(0);
				if (signs.containsKey(b.getLocation()))
				{
					int id = 0;
					boolean foundNum = true;
					try
					{
						id = Integer.parseInt(s.getLine(3));
					}
					catch (NumberFormatException e)
					{
						foundNum = false;
					}
					
					if (foundNum && evt.getPlayer().getGameMode() != GameMode.CREATIVE)
					{
						World w = evt.getBlock().getWorld();
						w.dropItemNaturally(b.getLocation(), new ItemStack(id));
					}
					msg += " " + parseTime(System.currentTimeMillis() - signs.get(b.getLocation())) + " ago";
				}
				evt.getPlayer().sendMessage(msg);
				evt.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onBLockRedstone(BlockRedstoneEvent evt)
	{
		doBlockRedstone(evt);
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
				evt.setNewCurrent(0);
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
			s.setLine(3, "" + oldId);
			s.update();
			signs.put(b.getLocation(), System.currentTimeMillis());
		}
		
		clockblocks.remove(coord);
	}
	
	public HashMap<Location, Integer> getSuspicious()
	{
		return suspicious;
	}
	
	public static String parseTime(Long timestamp)
	{
		timestamp /= 1000;
		String h, m, s;
		long hour, min, sec;
		hour = timestamp / 3600;
		min = (timestamp - hour * 3600) / 60;
		sec = timestamp- hour * 3600 - min * 60;
		h = (hour == 1) ? "hour" : "hours";
		m = (min == 1) ? "min" : "mins";
		s = (sec == 1) ? "sec" : "secs";
		return hour + " " + h + " " + min + " " + m + " " + sec + " " + s;
	}
}
