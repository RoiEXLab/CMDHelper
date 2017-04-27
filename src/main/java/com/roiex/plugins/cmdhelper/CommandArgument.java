package com.roiex.plugins.cmdhelper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.command.CommandSender;

public interface CommandArgument {

	static final Map<String, CommandArgument> args = new HashMap<>();

	public static void registerArgument(String name, CommandArgument argument) {
		args.put(name, argument);
	}

	static void registerDefaults() {
		// TODO
	}

	List<String> getSuggestions(String arg, CommandSender sender);

	boolean matches(String arg, CommandSender sender);
}
