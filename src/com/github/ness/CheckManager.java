package com.github.ness;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.github.ness.check.AbstractCheck;
import com.github.ness.check.AntiASCII;
import com.github.ness.check.CPSCheck;

import lombok.Getter;

public class CheckManager implements AutoCloseable {
	
	private final ConcurrentHashMap<UUID, NessPlayer> players = new ConcurrentHashMap<>();
	
	private final Set<AbstractCheck<?>> checks = new HashSet<>();
	
	@Getter
	private final NESSAnticheat ness;
	
	CheckManager(NESSAnticheat ness) {
		this.ness = ness;
	}
	
	private void addCheck(AbstractCheck<?> check) {
		check.initiatePeriodicTasks();
		checks.add(check);
	}
	
	void addAllChecks() {
		addCheck(new CPSCheck(this));
		addCheck(new AntiASCII(this));
	}
	
	void registerListener() {
		Bukkit.getPluginManager().registerEvents(new Listener() {
			@EventHandler
			private void onAnyEvent(Event evt) {
				if (evt instanceof PlayerJoinEvent) {
					Player player = ((PlayerJoinEvent) evt).getPlayer();
					players.put(player.getUniqueId(), new NessPlayer(player));
				} else if (evt instanceof PlayerQuitEvent) {
					players.remove(((PlayerQuitEvent) evt).getPlayer().getUniqueId()).close();
				} else {
					checks.forEach((check) -> check.checkAnyEvent(evt));
				}
			}
		}, ness);
	}
	
	@Override
	public void close() {
		checks.forEach(AbstractCheck::close);
		checks.clear();
		HandlerList.unregisterAll(ness);
	}
	
	/**
	 * Gets a NessPlayer or creates one if it does not exist
	 * 
	 * @param player the corresponding player
	 * @return the ness player
	 */
	public NessPlayer getPlayer(Player player) {
		return players.get(player.getUniqueId());
	}
	
	/**
	 * Do something for each NessPlayer
	 * 
	 * @param action what to do
	 */
	public void forEachPlayer(Consumer<NessPlayer> action) {
		players.values().forEach(action);
	}
	
}