package com.roiex.plugins.cmdhelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class CMDHelper extends JavaPlugin {

	protected static final String MINECRAFT_PREFIX = "minecraft:";

	@Override
	public void onEnable() {
		// TODO
	}

	public void registerCommandStructure(PluginCommand command, StructuredCommand structure) {
		command.setTabCompleter(new TabCompleter() {

			@Override
			public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
				Set<String> options = new HashSet<>();
				if (command.getPermission() != null && !sender.hasPermission(command.getPermission())) {
					return new ArrayList<>();
				}
				Collection<CommandBranch> matching = CommandBranch.getMatching(structure.getBranches(), args);
				if (matching.isEmpty()) {
					return new ArrayList<>();
				}
				Collection<CommandNeed> needs = new ArrayList<>();
				for (CommandBranch branch : matching) {
					CommandNeed need = branch.getNeeds().get(args.length - 1);
					if (!need.isPermissionSet() || sender.hasPermission(need.getRequiredPermission())) {
						needs.add(need);
					}
				}
				String incompleteArg = args[args.length - 1].toLowerCase();
				for (CommandNeed need : needs) {
					if (need.getCommandObject().equals(CommandObject.OFFLINE_PLAYER)) {
						for (String option : getPlayerNames(Arrays.asList(sender.getServer().getOfflinePlayers()))) {
							if (option.toLowerCase().startsWith(incompleteArg)) {
								options.add(option);
							}
						}
					} else if (need.getCommandObject().equals(CommandObject.ONLINE_PLAYER)) {
						for (String option : getPlayerNames(sender.getServer().getOnlinePlayers())) {
							if (option.toLowerCase().startsWith(incompleteArg)) {
								options.add(option);
							}
						}
					} else if (need.getCommandObject().equals(CommandObject.BLOCK)) {
						List<Material> blocks = Arrays.stream(Material.values()).filter(material -> material.isBlock())
								.collect(Collectors.toList());
						for (Material material : blocks) {
							String option = material.toString().toLowerCase();
							incompleteArg = trimArgument(incompleteArg);
							if (option.startsWith(incompleteArg)) {
								options.add(MINECRAFT_PREFIX + option);
							}
						}
					} else if (need.getCommandObject().equals(CommandObject.ITEM)) {
						List<Material> items = Arrays.stream(Material.values()).filter(material -> !material.isBlock())
								.collect(Collectors.toList());
						for (Material material : items) {
							String option = material.toString().toLowerCase();
							incompleteArg = trimArgument(incompleteArg);
							if (option.startsWith(incompleteArg)) {
								options.add(MINECRAFT_PREFIX + option);
							}
						}
					} else if (need.getCommandObject().equals(CommandObject.MATERIAL)) {
						List<Material> items = Arrays.asList(Material.values());
						for (Material material : items) {
							String option = material.toString().toLowerCase();
							incompleteArg = trimArgument(incompleteArg);
							if (option.startsWith(incompleteArg)) {
								options.add(MINECRAFT_PREFIX + option);
							}
						}
					} else if (need.getCommandObject().equals(CommandObject.XCOORD)
							|| need.getCommandObject().equals(CommandObject.YCOORD)
							|| need.getCommandObject().equals(CommandObject.ZCOORD)) {
						if (sender instanceof Player) {
							Player player = (Player) sender;
							Block target = player.getTargetBlock((Set<Material>) null, 5);
							if (!target.getType().equals(Material.AIR)) {
								switch (need.getCommandObject()) {
								case XCOORD:
									options.add(String.valueOf(target.getX()));
									break;
								case YCOORD:
									options.add(String.valueOf(target.getY()));
									break;
								case ZCOORD:
									options.add(String.valueOf(target.getZ()));
									break;
								default:
									throw new IllegalStateException("Object must be one of the Coordinate enums");

								}
							} else {
								options.add("~");
							}
						}
					} else if (need.getCommandObject().equals(CommandObject.STRING)) {
						if (need.hasValidOptions()) {
							for (String option : need.getValidOptions()) {
								if (option.startsWith(incompleteArg)) {
									options.add(option);
								}
							}
						}
					}
				}
				return new ArrayList<>(options);
			}
		});
	}

	private List<String> getPlayerNames(Collection<? extends OfflinePlayer> players) {
		List<String> result = new ArrayList<>();
		for (OfflinePlayer player : players) {
			result.add(player.getName());
		}
		return result;
	}

	private String trimArgument(String arg) {
		if (arg.startsWith(MINECRAFT_PREFIX)) {
			arg = arg.substring(arg.indexOf(':') + 1);
		}
		if (MINECRAFT_PREFIX.startsWith(arg)) {
			arg = "";
		}
		return arg;
	}
}
