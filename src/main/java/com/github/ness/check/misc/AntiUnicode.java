package com.github.ness.check.misc;

import java.nio.charset.CharsetEncoder;
import java.nio.charset.StandardCharsets;

import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.github.ness.CheckManager;
import com.github.ness.api.Violation;
import com.github.ness.check.AbstractCheck;
import com.github.ness.check.CheckInfo;

public class AntiUnicode extends AbstractCheck<AsyncPlayerChatEvent>  {

	private static final ThreadLocal<CharsetEncoder> asciiEncoder = ThreadLocal.withInitial(() -> StandardCharsets.US_ASCII.newEncoder());
	
	public AntiUnicode(CheckManager manager) {
		super(manager, CheckInfo.eventOnly(AsyncPlayerChatEvent.class));
	}

	@Override
	protected void checkEvent(AsyncPlayerChatEvent e) {
		/**
		 * Check if player send Unicode message
		 */
		if (!asciiEncoder.get().canEncode(e.getMessage())) {
			manager.getPlayer(e.getPlayer()).setViolation(new Violation("AntiUnicode", e.getMessage()), e);
		}	
	}

}
