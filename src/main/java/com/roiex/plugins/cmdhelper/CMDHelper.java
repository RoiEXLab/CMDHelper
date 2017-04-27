package com.roiex.plugins.cmdhelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.java.JavaPlugin;

public class CMDHelper extends JavaPlugin {

	@Override
	public void onEnable() {
	}

	public void registerCommandSyntax(PluginCommand command, String structure, PermissionMask... permissionMasks) {
		structure = structure.trim();
		String commandPrefix = "/" + command.getName() + ' ';
		if (structure.startsWith(commandPrefix)) {
			structure = structure.substring(commandPrefix.length());
		}
		StructureParser.validateStaticSyntax(structure);
		command.setTabCompleter(new TabCompleter() {
			@Override
			public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
				Set<String> options = new HashSet<>();
				tryLabel : try {
					if (command.getPermission() != null && !sender.hasPermission(command.getPermission())) {
						break tryLabel;
					}
					Arrays.stream(permissionMasks).filter(m -> StructureParser.matches(m, args));
					CommandArgument.args.get(command.getName());
				} catch (IllegalArgumentException e) {
					sender.sendMessage("§4Syntax Error detected while trying to Tab-Complete: Please report this to the plugin author!");
				}
				return new ArrayList<>(options);
			}
		});
	}

	public static void main(String[] args) {
	}
}
