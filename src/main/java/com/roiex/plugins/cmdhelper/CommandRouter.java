package com.roiex.plugins.cmdhelper;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandRouter implements CommandExecutor {

	private Map<String, CommandConsumer> routes = new HashMap<>();

	public void addRoute(String pattern, CommandConsumer consumer){
		routes.put(pattern, consumer);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		boolean executed = false;
		boolean success = true;
		for(Map.Entry<String, CommandConsumer> entry : routes.entrySet()){
			if(StructureParser.matches(entry.getKey(), args, sender)){
				executed = true;
				success &= entry.getValue().runAction(sender, command, label, args);
			}
		}
		return success && executed;
	}

}
