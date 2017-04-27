package com.roiex.plugins.cmdhelper.cmdargs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.roiex.plugins.cmdhelper.CommandArgument;

public abstract class CoordinateArgument implements CommandArgument {
	protected abstract int getCoordinate(Location location);

	@Override
	public List<String> getSuggestions(String arg, CommandSender sender) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			Block targetBlock = player.getTargetBlock((Set<Material>) null, 5);
			return Arrays.asList(targetBlock.getType().equals(Material.AIR) ? "~" : String.valueOf(getCoordinate(targetBlock.getLocation())));
		}
		return new ArrayList<>();
	}

	@Override
	public boolean matches(String arg, CommandSender sender) {
		return arg.matches("-?\\d+(\\.\\d+)?");
	}

	public static class XCoordinateArgument extends CoordinateArgument {

		@Override
		protected int getCoordinate(Location location) {
			return location.getBlockX();
		}
	}
	public static class YCoordinateArgument extends CoordinateArgument {

		@Override
		protected int getCoordinate(Location location) {
			return location.getBlockY();
		}
	}
	public static class ZCoordinateArgument extends CoordinateArgument {

		@Override
		protected int getCoordinate(Location location) {
			return location.getBlockZ();
		}
	}
}
