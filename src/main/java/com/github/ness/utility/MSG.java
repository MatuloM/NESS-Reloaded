package com.github.ness.utility;

import org.bukkit.command.CommandSender;

import net.md_5.bungee.api.ChatColor;

public class MSG {

	public static String torF(Boolean bool) {
		if (bool) {
			return "&aTrue&r";
		}
		return "&cFalse&r";
	}

	public static void tell(CommandSender sender, String msg) {
		if (msg != null)
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
	}
}
