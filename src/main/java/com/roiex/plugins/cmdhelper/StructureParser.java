package com.roiex.plugins.cmdhelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.command.CommandSender;

public class StructureParser {
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

	public static boolean matches(PermissionMask mask, String[] args) {
		return true;//TODO
	}

	private static List<CommandArgument> getCommandArguments(int index, String pattern, String[] args,
			CommandSender sender) {
		String nextArg = findNextArg(pattern);
		String[] subArgs = nextArg.split("/\\|/");
		List<CommandArgument> commandArgs = new ArrayList<>();
		for (String subArg : subArgs) {
			if (subArg.startsWith("<") && subArg.endsWith(">")) {
				String identifier = subArg.substring(1, subArg.length() - 1);
				CommandArgument current = CommandArgument.args.get(identifier);
				if (current != null) {
					if (index + 1 >= args.length) {
						commandArgs.add(current);
					} else if (current.matches(args[index], sender)) {
						commandArgs.addAll(getCommandArguments(index + 1, pattern.substring(nextArg.length()), args, sender));
					}
				} else {
					throw new IllegalArgumentException("Indentifier '" + identifier + "' doesn't exist!");
				}
			} else if (subArg.startsWith("[") && subArg.endsWith("]")) {
				commandArgs.addAll(getCommandArguments(index, subArg.substring(1, subArg.length() - 1) + pattern.substring(nextArg.length()), args, sender));
			} else {
				if (index + 1 >= args.length && subArg.startsWith(args[index])) {
					commandArgs.add(new CommandArgument() {
						@Override
						public List<String> getSuggestions(String arg, CommandSender sender) {
							return Arrays.stream(subArgs).filter(a -> a.startsWith(subArg)).collect(Collectors.toList());
						}

						@Override
						public boolean matches(String arg, CommandSender sender) {
							return Arrays.stream(subArgs).anyMatch(arg::equalsIgnoreCase);
						}
					});
				} else if (args[index].equalsIgnoreCase(subArg)) {
					commandArgs.addAll(getCommandArguments(index + 1, pattern.substring(nextArg.length()), args, sender));
				}

			}
		}
		return commandArgs;
	}

	public static List<CommandArgument> getCommandArguments(String pattern, String[] args, CommandSender sender) {
		return getCommandArguments(0, pattern, args, sender);
	}

	public static String findNextArg(String input) {
		char[] chars = input.toCharArray();
		int bracketCount = 0;
		for (int i = 0; i < chars.length; i++) {
			if (chars[i] == ' ' && bracketCount == 0) {
				return input.substring(0, i);
			} else if (chars[i] == '[') {
				bracketCount++;
			} else if (chars[i] == ']') {
				bracketCount--;
			}
		}
		return input;
	}
}
