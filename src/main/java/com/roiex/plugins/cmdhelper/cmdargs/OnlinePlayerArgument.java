package com.roiex.plugins.cmdhelper.cmdargs;

import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.command.CommandSender;

import com.google.common.base.Preconditions;
import com.roiex.plugins.cmdhelper.CommandArgument;

public class OnlinePlayerArgument implements CommandArgument {

	@Override
	public List<String> getSuggestions(String arg, CommandSender sender) {
		Preconditions.checkNotNull(arg);
		return sender.getServer().getOnlinePlayers().stream().map(p -> p.getName()).filter(n -> n.startsWith(arg.toLowerCase())).collect(Collectors.toList());
	}

	@Override
	public boolean matches(String arg, CommandSender sender) {
		Preconditions.checkNotNull(arg);
		return arg.startsWith("@") || sender.getServer().getOnlinePlayers().parallelStream().map(p -> p.getName().toLowerCase()).anyMatch(arg::equals);
	}
}
