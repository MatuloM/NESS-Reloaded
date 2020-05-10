package com.github.ness;

import java.io.File;
import java.util.List;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import com.github.ness.check.AbstractCheck;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class NessConfig {

	private static final int CONFIG_VERSION = 1;
	private static final int MESSAGES_VERSION = 1;
	
	private final String cfgFileName;
	private final String msgsFileName;
	
	private YamlConfiguration config;
	@Getter(AccessLevel.PACKAGE)
	private YamlConfiguration messages;
	
	void reloadConfiguration(NESSAnticheat ness) {
		File dataFolder = ness.getDataFolder();
		ness.saveResource(cfgFileName, false);
		ness.saveResource(msgsFileName, false);
		config = YamlConfiguration.loadConfiguration(new File(dataFolder, cfgFileName));
		messages = YamlConfiguration.loadConfiguration(new File(dataFolder, msgsFileName));
	}
	
	boolean checkConfigVersion() {
		return config.getInt("config-version", -1) == CONFIG_VERSION;
	}
	
	boolean checkMessagesVersion() {
		return messages.getInt("messages-version", -1) == MESSAGES_VERSION;
	}
	
	List<String> getEnabledChecks() {
		return config.getStringList("enabled-checks");
	}
	
	ConfigurationSection getViolationHandling() {
		return config.getConfigurationSection("violation-handling");
	}

	public ConfigurationSection getCheck(Class<? extends AbstractCheck<?>> check) {
		return config.getConfigurationSection("checks." + check.getSimpleName().toLowerCase());
	}
	
}