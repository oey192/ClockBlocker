ClockBlocker
===

ClockBlocker is a non-resource intensive way to make sure no redstone circuits are getting out of control on your server. Perfect for large servers where repeating redstone circuits can cause lag issues.


Commands
===

/clockblocker start - starts monitoring

/clockblocker stop - stops monitoring

/clockblocker view [page] - shows the log of suspicious blocks

/clockblocker tp \<number\> - teleports the player to the location of a suspicous block based off the number given in the log

/clockblocker reload - reloads the conifguration file

/clockblocker version - shows the current version


Permissions
===

clockblocker.start:<br/>
Allows the player to start monitoring

clockblocker.stop:<br/>
Allows the player to stop monitoring

clockblocker.view:<br/>
Allows the player to view suspicious redstone activity
clockblocker.tp:<br/>
Allows the player to teleport to suspicious activity

clockblocker.reload:<br/>
Allows the player to reload the config file

clockblocker.version:<br/>
Allows the player to check the plugin version


Configuration
===

allowStop:<br/>
When true, users with appropriate permissions can start and stop monitoring redstone activity

monitorOnLaunch:<br/>
When true, the plugin will begin monitoring redstone activity when it is enabled. When false, it will only start monitoring when a player with appropriate permissions issues the command: /clockblocker start

maxCyclesPerMin:<br/>
Set how many times a redstone block can fire per minute before it is considered a "lag threat" and dealt with

dropRedstone:<br/>
When true, signs placed by ClockBLocker drop the item that they replaced if the server has not been restarted since the redstone item was replaced by a sign
