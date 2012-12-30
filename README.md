Clockstopper
===

ClockStopper is a non-resource intensive way to make sure no redstone circuits are getting out of control on your server. Perfect for large servers where repeating redstone circuits can cause lag issues.


Commands
===

/clockstopper start

/clockstopper stop

/clockstopper view [page]

/clockstopper tp \<number\>


Permissions
===


Configuration
===

stopSignal: when set to true, the plugin will stop any signals that fire too quickly. When set to false, it will use the data value from the next config line and replace the redstone block with the block whose ID is specified

replaceBlock: the ID of the block which you would like to replace offending redstone with. If stopSignal is set to true, this value is irrelevent

allowStop: when true, users with appropriate permissions can start and stop monitoring redstone activity

monitorOnLaunch: when true, the plugin will begin monitoring redstone activity when it is enabled. When false, it will only start monitoring when a player with appropriate permissions issues the command: /clockblocker start

maxCyclesPerSec: set how many times a redstone block can fire per second before it is considered a "lag threat" and dealt with

