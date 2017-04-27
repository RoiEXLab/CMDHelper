package com.roiex.plugins.cmdhelper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

public class CommandNeed {
	private CommandObject commandObject;
	private boolean optional;
	Optional<String> permission = Optional.empty();

	private CommandNeed(CommandObject object, boolean optional) {
		commandObject = object;
		this.optional = optional;
	}

	public static CommandNeed optional(CommandObject object) {
		return new CommandNeed(object, true);
	}

	public static CommandNeed required(CommandObject object) {
		return new CommandNeed(object, false);
	}

	protected boolean isOptional() {
		return optional;
	}

	protected CommandObject getCommandObject() {
		return commandObject;
	}

	public CommandNeed setPermission(String permission) {
		this.permission = Optional.ofNullable(permission);
		return this;
	}

	protected String getRequiredPermission() {
		return permission.get();
	}

	protected boolean isPermissionSet() {
		return permission.isPresent();
	}

	private Collection<String> validOptions = new ArrayList<>();

	protected Collection<String> getValidOptions() {
		return validOptions;
	}

	protected boolean hasValidOptions() {
		return !validOptions.isEmpty();
	}

	protected CommandNeed addValidOption(String option) {
		validOptions.add(option);
		return this;
	}

	public static CommandNeed optionalArgsetOf(String... args) {
		CommandNeed result = new CommandNeed(CommandObject.STRING, true);
		for (String option : args) {
			result.addValidOption(option);
		}
		return result;
	}

	public static CommandNeed requiredArgsetOf(String... args) {
		CommandNeed result = new CommandNeed(CommandObject.STRING, false);
		for (String option : args) {
			result.addValidOption(option);
		}
		return result;
	}

	public static String[] numbers() {
		return new String[] { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9" };
	}
}
