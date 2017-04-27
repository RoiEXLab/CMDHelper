package com.roiex.plugins.cmdhelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.Material;

public class CommandBranch {
	private List<CommandNeed> needs = new ArrayList<>();

	private CommandBranch(CommandNeed... needs) {
		for (CommandNeed need : needs) {
			this.needs.add(need);
		}
	}

	public static CommandBranch branch(CommandNeed... commandNeeds) {
		return new CommandBranch(commandNeeds);
	}

	protected List<CommandNeed> getNeeds() {
		return needs;
	}

	private int getLength() {
		return needs.size();
	}

	protected static Collection<CommandBranch> getMatching(Collection<CommandBranch> branches, String[] args) {
		List<CommandBranch> filteredBranches = new ArrayList<>(getWithMinimumSize(branches, args.length));
		Set<CommandBranch> toRemove = new HashSet<>();
		args = Arrays.copyOf(args, args.length - 1);
		int counter = 0;
		for (String arg : args) {
			List<CommandNeed> needs = new ArrayList<>();
			for (CommandBranch branch : filteredBranches) {
				needs.add(branch.getNeeds().get(counter));
			}
			int counter2 = 0;
			for (CommandNeed need : needs) {
				CommandObject object = need.getCommandObject();
				if (!object.equals(CommandObject.STRING)) {
					if (object.equals(CommandObject.ONLINE_PLAYER)) {
						if (Bukkit.getServer().getOnlinePlayers().stream()
								.filter(player -> player.getName().equalsIgnoreCase(arg)).collect(Collectors.toList())
								.isEmpty()) {
							if (!(arg.startsWith("@a") || arg.startsWith("@e") || arg.startsWith("@p")
									|| arg.startsWith("@r"))) {
								toRemove.add(filteredBranches.get(counter2));
							} else if (!(arg.length() > 6 && arg.substring(2).startsWith("[") && arg.endsWith("]")
									&& arg.substring(3, arg.length() - 1).contains("="))) {
								toRemove.add(filteredBranches.get(counter2));
							}
						}
					} else if (object.equals(CommandObject.OFFLINE_PLAYER)) {
						if (Arrays.asList(Bukkit.getServer().getOfflinePlayers()).stream()
								.filter(player -> player.getName().equalsIgnoreCase(arg)).collect(Collectors.toList())
								.isEmpty()) {
							toRemove.add(filteredBranches.get(counter2));
						}
					} else if (object.equals(CommandObject.ITEM)) {
						if (Arrays.stream(Material.values()).filter(material -> !material.isBlock())
								.filter(material -> material.toString().equalsIgnoreCase(arg))
								.collect(
										Collectors.toList())
								.isEmpty()
								&& Arrays.stream(Material.values()).filter(material -> !material.isBlock())
										.filter(material -> (CMDHelper.MINECRAFT_PREFIX + material.toString())
												.equalsIgnoreCase(arg))
										.collect(Collectors.toList()).isEmpty()) {
							toRemove.add(filteredBranches.get(counter2));
						}
					} else if (object.equals(CommandObject.BLOCK)) {
						if (Arrays.stream(Material.values()).filter(material -> material.isBlock())
								.filter(material -> material.toString().equalsIgnoreCase(arg))
								.collect(
										Collectors.toList())
								.isEmpty()
								&& Arrays.stream(Material.values()).filter(material -> material.isBlock())
										.filter(material -> (CMDHelper.MINECRAFT_PREFIX + material.toString())
												.equalsIgnoreCase(arg))
										.collect(Collectors.toList()).isEmpty()) {
							toRemove.add(filteredBranches.get(counter2));
						}
					} else if (object.equals(CommandObject.MATERIAL)) {
						if (Arrays.stream(Material.values())
								.filter(material -> material.toString().equalsIgnoreCase(arg))
								.collect(
										Collectors.toList())
								.isEmpty()
								&& Arrays.stream(Material.values())
										.filter(material -> (CMDHelper.MINECRAFT_PREFIX + material.toString())
												.equalsIgnoreCase(arg))
										.collect(Collectors.toList()).isEmpty()) {
							toRemove.add(filteredBranches.get(counter2));
						}
					} else if (object.equals(CommandObject.XCOORD) || object.equals(CommandObject.YCOORD)
							|| object.equals(CommandObject.ZCOORD)) {
						if (arg.startsWith("~")) {
							arg.substring(1);
						}
						try {
							Integer.parseInt(arg);
						} catch (NumberFormatException e) {
							toRemove.add(filteredBranches.get(counter2));
						}
					}
				} else if (need.hasValidOptions()) {
					if (!need.getValidOptions().contains(arg)) {
						toRemove.add(filteredBranches.get(counter2));
					}
				}
				counter2++;
			}
			counter++;
		}
		for (CommandBranch branch : toRemove) {
			filteredBranches.remove(branch);
		}
		return filteredBranches;
	}

	private static Collection<CommandBranch> getWithMinimumSize(Collection<CommandBranch> branches, int size) {
		Collection<CommandBranch> result = new ArrayList<>();
		for (CommandBranch branch : branches) {
			if (branch.getLength() >= size) {
				result.add(branch);
			}
		}
		return result;
	}
}
