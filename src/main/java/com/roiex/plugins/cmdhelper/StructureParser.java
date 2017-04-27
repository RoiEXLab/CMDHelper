package com.roiex.plugins.cmdhelper;

import java.util.ArrayList;
import java.util.List;

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

	public static List<CommandArgument> getArgumentsAt(int index, String pattern, String[] args) {
		return new ArrayList<>();//TODO
	}
}
