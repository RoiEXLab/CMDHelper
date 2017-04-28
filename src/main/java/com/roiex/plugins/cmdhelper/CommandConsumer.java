package com.roiex.plugins.cmdhelper;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

@FunctionalInterface
public interface CommandConsumer {
	boolean runAction(CommandSender sender, Command command, String label, String[] args);
}
