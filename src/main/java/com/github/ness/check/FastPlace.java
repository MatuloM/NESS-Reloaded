package com.github.ness.check;

import org.bukkit.event.block.BlockPlaceEvent;

import com.github.ness.CheckManager;
import com.github.ness.NessPlayer;
import com.github.ness.Violation;

public class FastPlace extends AbstractCheck<BlockPlaceEvent>{
	
	public FastPlace(CheckManager manager) {
		super(manager, CheckInfo.eventOnly(BlockPlaceEvent.class));
		// TODO Auto-generated constructor stub
	}
	
	@Override
	void checkEvent(BlockPlaceEvent e) {
       Check(e);
	}
    /**
     * A Simple FastPlace check
     * @param event
     */
	public void Check(BlockPlaceEvent event) {
		NessPlayer player = manager.getPlayer(event.getPlayer());
		player.setBlockplace(player.getBlockplace()+1);
		if(player.getBlockplace()>5) {
			player.setViolation(new Violation("FastPlace"));
			event.setCancelled(true);
		}
		
	}

}