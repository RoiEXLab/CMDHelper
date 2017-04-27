package com.roiex.plugins.cmdhelper;

import java.util.ArrayList;
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

	public void registerCommandSyntax(PluginCommand command, String structure, String... permissionMasks) {
		structure = structure.trim();
		String commandPrefix = "/" + command.getName() + ' ';
		if (structure.startsWith(commandPrefix)) {
			structure = structure.substring(commandPrefix.length());
		}
		validateStaticSyntax(structure);
		command.setTabCompleter(new TabCompleter() {
			@Override
			public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
				Set<String> options = new HashSet<>();
				tryLabel : try {
					if (command.getPermission() != null && !sender.hasPermission(command.getPermission())) {
						break tryLabel;
					}
					CommandArgument.args.get(command.getName());
				} catch (IllegalArgumentException e) {
					sender.sendMessage("§4Syntax Error detected while trying to Tab-Complete: Please report this to the plugin author!");
				}
				return new ArrayList<>(options);
			}
		});
	}

	private static final String regexSyntaxCheck = "(((<([a-zA-Z]+)>)|((((?=[^\\|])[a-zA-Z])(\\|(?=[^\\s]))?)+))((\\s(?!$))|$|\\|))*(\\[([\\|\\<\\>a-zA-Z\\[\\]]|(\\s(?!\\])))*\\](\\s(?!$))?)*";

	public static void validateStaticSyntax(String string) {
		validate(string);
		checkRecursively(string.toCharArray(), 0);
	}

	private static void validate(String string) {
		if (!string.matches(regexSyntaxCheck)) {
			throw new IllegalArgumentException("Invalid String, check your Syntax!");
		}
	}

	private static int checkRecursively(char[] chars, int startIndex) {
		for (int i = startIndex; i < chars.length; i++) {
			if (chars[i] == '[') {
				int end = checkRecursively(chars, ++i);
				validate(new String(chars, i, end - i));
				i = end;
			} else if (chars[i] == ']') {
				return i;
			}
		}
		return chars.length - 1;
	}

	public static void main(String[] args) {
		validateStaticSyntax("<x> <y> <z> [<a>|b|c <x> [a]] [<x>]");
	}
}
