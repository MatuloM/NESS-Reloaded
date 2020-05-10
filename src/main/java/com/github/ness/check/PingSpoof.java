package com.github.ness.check;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.github.ness.MovementPlayerData;
import com.github.ness.NESSAnticheat;
import com.github.ness.Violation;
import com.github.ness.utility.Utility;

public class PingSpoof {

	public static void Check(Player sender, Object packet) {
		if(sender==null) {
			return;
		}
		Bukkit.getScheduler().scheduleSyncDelayedTask(NESSAnticheat.main, () -> {
			MovementPlayerData mp = MovementPlayerData.getInstance(sender);
			mp.pingspooftimer = System.currentTimeMillis();
			double diff = mp.pingspooftimer - mp.oldpingspooftimer;
			if (Utility.getPing(sender) > 300 && (diff > 40) && (diff < 65)) {
				InventoryHack.manageraccess.getPlayer(sender).setViolation(new Violation("PingSpoof"));
				Utility.setPing(sender, 100);
			}
			mp.oldpingspooftimer = mp.pingspooftimer;
		}, 0);
	}
}