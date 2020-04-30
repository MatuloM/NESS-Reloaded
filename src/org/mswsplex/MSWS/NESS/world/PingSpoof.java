package org.mswsplex.MSWS.NESS.world;

import org.bukkit.entity.Player;
import org.mswsplex.MSWS.NESS.MovementPlayerData;
import org.mswsplex.MSWS.NESS.NESS;
import org.mswsplex.MSWS.NESS.PlayerManager;
import org.mswsplex.MSWS.NESS.WarnHacks;

public class PingSpoof {

	public static void Check(Player sender, Object packet) {
		MovementPlayerData mp = MovementPlayerData.getInstance(sender);
		mp.pingspooftimer = System.currentTimeMillis();
		double diff = mp.pingspooftimer - mp.oldpingspooftimer;
		if (PlayerManager.getPing(sender) > 300 && (diff > 40) && (diff < 65)) {
			if (NESS.main.devMode) {
				sender.sendMessage("PingSpoof: difference " + diff + " Ping: " + PlayerManager.getPing(sender));
			}
			WarnHacks.warnHacks(sender, "PingSpoof", 5, -1.0D, 1, "Packets", null);
			PlayerManager.setPing(sender, 100);
		}
		mp.oldpingspooftimer = mp.pingspooftimer;
	}

	public static void Check1(Player sender, Object packet) {
		MovementPlayerData mp = MovementPlayerData.getInstance(sender);
		mp.pingspooftimer = System.currentTimeMillis();
		double diff = mp.pingspooftimer - mp.oldpingspooftimer;
		if (NESS.main.devMode) {
			sender.sendMessage("PingSpoof: difference " + diff + " Ping: " + PlayerManager.getPing(sender));
		}
		mp.oldpingspooftimer = mp.pingspooftimer;
	}
}