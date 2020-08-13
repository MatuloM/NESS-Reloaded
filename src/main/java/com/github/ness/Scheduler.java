package com.github.ness;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;

import com.github.ness.check.AntiBot;
import com.github.ness.check.InventoryHack;
import com.github.ness.check.OldMovementChecks;

public class Scheduler {

	public static void startSyncScheduler() {
		BukkitScheduler scheduler = NESSAnticheat.main.getServer().getScheduler();
		scheduler.scheduleSyncRepeatingTask(NESSAnticheat.main, new Runnable() {
			@Override
			public void run() {
				for (Player p : Bukkit.getOnlinePlayers()) {
					OldMovementChecks.oldLoc.remove(p);
					OldMovementChecks.oldLoc.put(p, p.getLocation());
				}
			}
		}, 0L, 10L);
	}
}
