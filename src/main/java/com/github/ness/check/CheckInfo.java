package com.github.ness.check;

import java.util.concurrent.TimeUnit;

import org.bukkit.event.Event;

/**
 * Relates the type of the check, specifically when it occurs,
 * whether on an event, periodically, etc.
 * 
 * @author A248
 *
 * @param <T> the type of the event
 */
public class CheckInfo<T extends Event> {

	/**
	 * Interval of repeating async task, {@code -1} for none
	 * 
	 */
	final long asyncInterval;
	final TimeUnit units;
	
	/**
	 * Event to listen to, {@code null} for none
	 * 
	 */
	final Class<T> event;
	
	private CheckInfo(Class<T> event, long asyncInterval, TimeUnit units) {
		this.event = event;
		this.asyncInterval = asyncInterval;
		this.units = units;
	}
	
	public static <T extends Event> CheckInfo<T> eventWithAsyncPeriodic(Class<T> event, long interval, TimeUnit units) {
		return new CheckInfo<>(event, interval, units);
	}
	
	public static <T extends Event> CheckInfo<T> eventOnly(Class<T> event) {
		return new CheckInfo<>(event, -1L, null);
	}
	
}
