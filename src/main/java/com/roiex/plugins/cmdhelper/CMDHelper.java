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
		CommandArgument.registerDefaults();
		//		PluginCommand testCommand = getCommand("testCommand");
		//		testCommand.setExecutor(new CommandExecutor() {
		//
		//			@Override
		//			public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		//				sender.sendMessage("Hi");
		//				return true;
		//			}
		//		});
		//		registerCommandSyntax(testCommand, "[<x> <y> <z> <block>]|[<block> <material>]|[<block> <x> <y>] [<x>]", new PermissionMask("<block> <material> <x>", "permission-coolness"));
	}

	public void registerCommandSyntax(PluginCommand command, String structure, PermissionMask... permissionMasks) {
		structure = structure.trim();
		String commandPrefix = "/" + command.getName() + ' ';
		if (structure.startsWith(commandPrefix)) {
			structure = structure.substring(commandPrefix.length());
		}
		StructureParser.validateStaticSyntax(structure);
		final String pattern = structure;
		command.setTabCompleter(new TabCompleter() {
			@Override
			public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
				Set<String> options = new HashSet<>();
				tryLabel : try {
					if (command.getPermission() != null && !sender.hasPermission(command.getPermission())) {
						break tryLabel;
					}
					Arrays.stream(permissionMasks).filter(m -> StructureParser.matches(m, args, sender));
					List<CommandArgument> cmdArgs = StructureParser.getCommandArguments(pattern, args, sender);
					for (PermissionMask mask : permissionMasks) {
						if (!sender.hasPermission(mask.getPermission())) {
							List<CommandArgument> prohibitedArgs = StructureParser.getCommandArguments(mask.getMask(), args, sender);
							prohibitedArgs.forEach(cmdArgs::remove);
						}
					}
					cmdArgs.stream().map(a -> a.getSuggestions(args[args.length - 1], sender)).forEach(options::addAll);
				} catch (IllegalArgumentException e) {
					sender.sendMessage("§4Syntax Error detected while trying to Tab-Complete: Please report this to the plugin author!\nMessage: " + e.getMessage());
					e.printStackTrace();
				}
				return new ArrayList<>(options);
			}
		});
	}
}
