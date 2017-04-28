package com.roiex.plugins.cmdhelper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.command.CommandSender;

import com.roiex.plugins.cmdhelper.cmdargs.BaseMaterialArgument;
import com.roiex.plugins.cmdhelper.cmdargs.CoordinateArgument;
import com.roiex.plugins.cmdhelper.cmdargs.OfflinePlayerArgument;
import com.roiex.plugins.cmdhelper.cmdargs.OnlinePlayerArgument;
import com.roiex.plugins.cmdhelper.cmdargs.StringArgument;

public interface CommandArgument {

	static final Map<String, CommandArgument> args = new HashMap<>();

	public static void registerArgument(String name, CommandArgument argument) {
		args.put(name, argument);
	}

	static void registerDefaults() {
		registerArgument("x", new CoordinateArgument.XCoordinateArgument());
		registerArgument("y", new CoordinateArgument.YCoordinateArgument());
		registerArgument("z", new CoordinateArgument.ZCoordinateArgument());
		registerArgument("block", new BaseMaterialArgument.BlockArgument());
		registerArgument("material", new BaseMaterialArgument.MaterialArgument());
		registerArgument("offline_player", new OfflinePlayerArgument());
		registerArgument("online_player", new OnlinePlayerArgument());
		registerArgument("string", new StringArgument());
	}

	List<String> getSuggestions(String arg, CommandSender sender);

	boolean matches(String arg, CommandSender sender);
}
