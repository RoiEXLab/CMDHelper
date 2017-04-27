package com.roiex.plugins.cmdhelper.cmdargs;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandSender;

import com.roiex.plugins.cmdhelper.CommandArgument;

public class StringArgument implements CommandArgument {

	@Override
	public List<String> getSuggestions(String arg, CommandSender sender) {
		return new ArrayList<>();
	}

	@Override
	public boolean matches(String arg, CommandSender sender) {
		return true;
	}
}
