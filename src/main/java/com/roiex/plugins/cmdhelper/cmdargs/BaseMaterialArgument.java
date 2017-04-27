package com.roiex.plugins.cmdhelper.cmdargs;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;

import com.roiex.plugins.cmdhelper.CommandArgument;

public abstract class BaseMaterialArgument implements CommandArgument {

	public static final String MINECRAFT_PREFIX = "minecraft:";

	protected abstract Predicate<Material> getFilterCriteria();

	@Override
	public List<String> getSuggestions(String arg, CommandSender sender) {
		final String trimmedArg = trimArgument(arg);
		return Arrays.stream(Material.values()).filter(getFilterCriteria()).map(m -> m.toString().toLowerCase()).filter(n -> n.startsWith(trimmedArg)).map(n -> MINECRAFT_PREFIX + n).collect(Collectors.toList());
	}

	@Override
	public boolean matches(String arg, CommandSender sender) {
		final boolean startsWithPrefix = arg.startsWith(MINECRAFT_PREFIX);
		if (arg.contains(":") && !startsWithPrefix) {
			return true;//We can't check non-vanilla items
		}
		if (startsWithPrefix) {
			arg = trimArgument(arg);
		}
		return Arrays.stream(Material.values()).filter(getFilterCriteria()).anyMatch(arg::equals);
	}

	private String trimArgument(String arg) {
		if (MINECRAFT_PREFIX.startsWith(arg)) {
			arg = "";
		} else if (arg.startsWith(MINECRAFT_PREFIX)) {
			arg = arg.substring(MINECRAFT_PREFIX.length());
		}
		return arg;
	}
	public static class BlockArgument extends BaseMaterialArgument {
		@Override
		protected Predicate<Material> getFilterCriteria() {
			return m -> m.isBlock();
		}
	}
	public static class MaterialArgument extends BaseMaterialArgument {
		@Override
		protected Predicate<Material> getFilterCriteria() {
			return m -> true;
		}
	}
}
