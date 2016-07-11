package me.tmods.serveraddons;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import me.tmods.serverutils.Methods;
public class InfFlight extends JavaPlugin{
	public List<Player> infflight = new ArrayList<Player>();
	@Override
	public void onDisable() {
		Bukkit.getScheduler().cancelTasks(this);
	}

	@Override
	public void onEnable() {
		try {
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
			@Override
			public void run() {
				try {
				if (infflight.size() > 0) {
					for (Player p:infflight) {
						if (p.getInventory().getChestplate() != null) {
							if (p.getInventory().getChestplate().getType() != Material.ELYTRA) {
								p.getInventory().addItem(p.getInventory().getChestplate());
							}
						}
						p.getInventory().setChestplate(new ItemStack(Material.ELYTRA));
					}
				}
				} catch (Exception e) {
					Methods.log(e);
				}
			}	
		}, 200, 200);
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
			@Override
			public void run() {
				try {
				if (infflight.size() > 0) {
					for (Player p:infflight) {
						if (p.hasPermission("ServerAddons.doubleJump")) {
							p.setVelocity(p.getLocation().getDirection());
						}
					}
				}
				} catch (Exception e) {
					Methods.log(e);
				}
			}
		}, 1, 1);
		} catch (Exception e) {
			Methods.log(e);
		}
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		try {
		if (cmd.getName().equalsIgnoreCase("infflight") && sender instanceof Player) {
			if (args.length == 1 && sender.hasPermission("ServerAddons.infflight.fly")) {
				if (Bukkit.getPlayer(args[0]) != null) {
					if (infflight.contains(Bukkit.getPlayer(args[0]))) {
						infflight.remove(Bukkit.getPlayer(args[0]));
					} else {
						Player p = Bukkit.getPlayer(args[0]);
						infflight.add(p);
						(p).setVelocity(new Vector(0,10,0));
						if (p.getInventory().getChestplate() != null) {
							if (p.getInventory().getChestplate().getType() != Material.ELYTRA) {
								p.getInventory().addItem(p.getInventory().getChestplate());
							}
						}
						p.getInventory().setChestplate(new ItemStack(Material.ELYTRA));
					}
				} else {
					sender.sendMessage("this player is not online");
				}
			} else {
				if (!sender.hasPermission("ServerAddons.infflight.toggle")) {
					return true;
				}
				if (infflight.contains((Player) sender)) {
					infflight.remove((Player) sender);
				} else {
					Player p = (Player) sender;
					infflight.add(p);
					(p).setVelocity(new Vector(0,10,0));
					if (p.getInventory().getChestplate() != null) {
						if (p.getInventory().getChestplate().getType() != Material.ELYTRA) {
							p.getInventory().addItem(p.getInventory().getChestplate());
						}
					}
					p.getInventory().setChestplate(new ItemStack(Material.ELYTRA));
				}
			}
		}
		} catch (Exception e) {
			Methods.log(e);
		}
		return true;
	}	
}
