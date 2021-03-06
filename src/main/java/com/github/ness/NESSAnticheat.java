package com.github.ness;

import java.io.File;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.ness.api.NESSApi;
import com.github.ness.packets.PacketListener;

import lombok.Getter;

public class NESSAnticheat extends JavaPlugin {
	@Getter
	private ScheduledExecutorService executor;
	static NESSAnticheat main;
	@Getter
	private NessConfig nessConfig;
	@Getter
	private CheckManager checkManager;
	@Getter
	private ViolationManager violationManager;
	@Getter
	private int minecraftVersion;
	@Getter
	private MouseRecord mouseRecord;
	private static final Logger logger = Logger.getLogger(NESSAnticheat.class.getName());

	@Override
	public void onEnable() {
		main = this;

		mouseRecord = new MouseRecord(this);
		nessConfig = new NessConfig("config.yml", "messages.yml");
		nessConfig.reloadConfiguration(this);
		if (!nessConfig.checkConfigVersion()) {
			getLogger().warning(
					"Your config.yml is outdated! Until you regenerate it, NESS will use default values for some checks.");
		}
		if (!nessConfig.checkMessagesVersion()) {
			getLogger().warning(
					"Your messages.yml is outdated! Until you regenerate it, NESS will use default values for some messages.");
		}
		logger.fine("Configuration loaded. Initiating checks...");
		if (this.getVersion() > 1152 && this.getVersion() < 1162) {
			getLogger().warning("Please use 1.16.2 Spigot Version since 1.16/1.16.1 has a lot of false flags");
		}
		executor = Executors.newSingleThreadScheduledExecutor();
		getCommand("ness").setExecutor(new NessCommands(this));
		if (!new File(this.getDataFolder(), "records").exists()) {
			new File(this.getDataFolder(), "records").mkdir();
		}
		checkManager = new CheckManager(this);
		CompletableFuture<?> future = checkManager.loadChecks();
		getServer().getPluginManager().registerEvents(checkManager.coreListener, this);

		violationManager = new ViolationManager(this);
		violationManager.addDefaultActions();
		violationManager.initiatePeriodicTask();
		getServer().getScheduler().runTaskLater(this, future::join, 1L);

		getServer().getServicesManager().register(NESSApi.class, new NESSApiImpl(this), this, ServicePriority.Low);
		minecraftVersion = this.getVersion();
		if (!Bukkit.getName().toLowerCase().contains("glowstone")) {
			getServer().getPluginManager().registerEvents(new PacketListener(), this);
		}
		if (this.getNessConfig().getViolationHandling().getConfigurationSection("notify-staff").getBoolean("bungeecord",
				false)) {
			this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
			this.getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", new BungeeCordListener());
		}
	}

	public int getVersion() {
		String first = Bukkit.getVersion().substring(Bukkit.getVersion().indexOf("(MC: "));
		return Integer.valueOf(first.replace("(MC: ", "").replace(")", "").replace(" ", "").replace(".", ""));
	}

	public static NESSAnticheat getInstance() {
		return NESSAnticheat.main;
	}

	@Override
	public void onDisable() {
		if (checkManager != null) {
			checkManager.close();
		}
		if (executor != null) {
			try {
				executor.shutdown();
				executor.awaitTermination(10L, TimeUnit.SECONDS);
			} catch (InterruptedException ex) {
				logger.log(Level.WARNING, "Failed to complete thread pool termination", ex);
			}
		}
	}

}