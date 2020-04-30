package com.github.ness.movement;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;

import com.github.ness.Utility;
import com.github.ness.WarnHacks;

public class EntitySpeedCheck {

	public static void Check(PlayerMoveEvent event) {
		Player p = event.getPlayer();
		if(p.isInsideVehicle()) {
			Entity e = p.getVehicle();
			if (e.getType() == EntityType.MINECART) {
				return;
			}
			double limit = 5.75;
			if(Utility.getMaxSpeed(event.getFrom(), event.getTo())>limit) {
				WarnHacks.warnHacks(p, "Speed", 10, -1.0D, 5, "MaxDistanceVehicle", false);
			}
		}
		
	}

}