package com.andoutay.clockblocker;

import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class ClockBlocker extends JavaPlugin
{
	private Logger log = Logger.getLogger("Minecraft");
	public static String chPref = ChatColor.DARK_RED + "[ClockBlocker] " + ChatColor.RESET;
	public static String logPref = "[ClockBlocker] ";
	private boolean monitoringEnabled;
	private PluginManager pm;
	
	public void onLoad()
	{
		new CBConfig(this);
	}
	
	@Override
	public void onEnable()
	{
		CBConfig.onEnable();
		pm = getServer().getPluginManager();
		pm.registerEvents(new CBBlockRedstoneHandler(this), this);
		
		monitoringEnabled = CBConfig.monitorOnLaunch;
		
		log.info(logPref + "enabled successfully!");
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		Player p = null;
		String cmdName = cmd.getName();
		if (sender instanceof Player)
			p = (Player)sender;
		
		if (cmdName.equalsIgnoreCase("clockblocker"))
			if (args.length == 1 && args[0].equalsIgnoreCase("start"))
			{
				if (p == null || p.hasPermission("clockblocker.start"))
					if (CBConfig.allowStop)
					{
						startMonitor();
						sender.sendMessage(chPref + "Monitoring enabled");
					}
					else
						sender.sendMessage(chPref + ChatColor.RED + "The server admin is not allowing use of that command");
				else
					sender.sendMessage(ChatColor.RED + "Just what do you think you're doing?");
				return true;
			}
			else if (args.length == 1 && args[0].equalsIgnoreCase("stop"))
			{
				if (p == null || p.hasPermission("clockblocker.stop"))
					if (CBConfig.allowStop)
					{
						stopMonitor();
						sender.sendMessage(chPref + "Monitoring disabled");
					}
					else
						sender.sendMessage(chPref + ChatColor.RED + "The server admin is not allowing use of that command");
				else
					sender.sendMessage(ChatColor.RED + "Just what do you think you're doing?");
				return true;
			}
			else if ((args.length == 1 || args.length == 2) && args[0].equalsIgnoreCase("view"))
			{
				if (p == null || p.hasPermission("clockblocker.view"))
				{
					sender.sendMessage(chPref + "See ALL the glory!");
				}
				else
					sender.sendMessage(ChatColor.RED + "Just what do you think you're doing?");
				return true;
			}
			else if (args.length == 2 && args[0].equalsIgnoreCase("tp"))
			{
				if (p == null || p.hasPermission("clockblocker.tp"))
				{
					sender.sendMessage("still a WIP");
				}
				else
					sender.sendMessage(ChatColor.RED + "Just what do you think you're doing?");
				return true;
			}
			else if (args.length == 1 && args[0].equalsIgnoreCase("reload"))
			{
				if (p == null || p.hasPermission("clockblocker.reload"))
				{
					CBConfig.reload();
					sender.sendMessage(chPref + "Config reloaded");
				}
				else
					sender.sendMessage(ChatColor.RED + "Just what do you think you're doing?");
				return true;
			}
			else if (args.length == 1 && args[0].equalsIgnoreCase("version"))
			{
				if (p == null || p.hasPermission("clockblocker.version"))
					sender.sendMessage(chPref + "Current version: " + getDescription().getVersion());
				else
					sender.sendMessage(ChatColor.RED + "Just what do you think you're doing?");
				return true;
			}
			else if (args.length == 1 && (args[0].equalsIgnoreCase("help") || args[0].equalsIgnoreCase("?")))
			{
				sender.sendMessage(chPref + "Help:");
				sender.sendMessage("usage: /clockblocker <arguments>");
				sender.sendMessage("Arguments:");
				sender.sendMessage("start - starts monitoring redstone activity");
				sender.sendMessage("stop - stops monitoring redstone activity");
				sender.sendMessage("view - view suspicious activity");
				sender.sendMessage("tp <num> - teleport to a suspicious block from the list");
				sender.sendMessage("version - see the current plugin version");
				sender.sendMessage("help or ? - view this help message");
				return true;
			}
		
		return false;
	}
	
	@Override
	public void onDisable()
	{
		log.info(logPref + "disabled successfully!");
	}
	
	private void startMonitor()
	{
		monitoringEnabled = true;
	}
	
	private void stopMonitor()
	{
		monitoringEnabled = false;
	}
	
	public boolean shouldMonitor()
	{
		return monitoringEnabled;
	}
}
