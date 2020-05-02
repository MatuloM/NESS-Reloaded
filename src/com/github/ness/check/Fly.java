package com.github.ness.check;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import org.mswsplex.MSWS.NESS.MSG;
import org.mswsplex.MSWS.NESS.NESS;
import org.mswsplex.MSWS.NESS.PlayerManager;
import com.github.ness.CheckManager;
import com.github.ness.NessPlayer;
import com.github.ness.Utilities;
import com.github.ness.Utility;
import com.github.ness.Violation;

public class Fly extends AbstractCheck<PlayerMoveEvent> {

	protected HashMap<String, Integer> noground = new HashMap<String, Integer>();
	
	public Fly(CheckManager manager) {
		super(manager, CheckInfo.eventOnly(PlayerMoveEvent.class));
	}

	@Override
	void checkEvent(PlayerMoveEvent e) {
		Check(e);
		Check1(e);
		Check2(e);
		Check3(e);
		Check4(e);
		Check5(e);
		Check6(e);
		Check7(e);
		Check8(e);
		Check9(e);
		Check10(e);
		Check11(e);
	}

	protected List<String> bypasses = Arrays.asList("slab", "stair", "snow", "bed", "skull", "step", "slime");

	public void Check(PlayerMoveEvent event) {
		if (!bypass(event.getPlayer()) && !Utility.hasBlock(event.getPlayer(), Material.SLIME_BLOCK)) {
			Player player = event.getPlayer();
			if (!event.getPlayer().isOnGround()) {
				double fallDist = (double) event.getPlayer().getFallDistance();
				if (event.getPlayer().getVelocity().getY() < -1.0D && fallDist == 0.0D) {
					player.setHealth(player.getHealth() - 1.0D);
					manager.getPlayer(event.getPlayer()).setViolation(new Violation("Fly"));
				}
			}

		}
	}

	public void Check1(PlayerMoveEvent event) {
		Player p = event.getPlayer();
		if (!bypass(event.getPlayer())) {
			if (Utilities.isClimbableBlock(p.getLocation().getBlock()) && !Utilities.isInWater(p)) {
				double distance = Utility.around(event.getTo().getY() - event.getFrom().getY(), 6);
				if (distance > 0.12D) {
					if (distance == 0.164D || distance == 0.248D || distance == 0.333D || distance == 0.419D) {
						return;
					}
					manager.getPlayer(event.getPlayer()).setViolation(new Violation("Fly"));
				}
			}

		}
	}

	public void Check2(PlayerMoveEvent event) {
		Player p = event.getPlayer();
		if (!bypass(event.getPlayer())) {
			if (p.getVelocity().getY() < -1.0D && p.isOnGround() && !Utility.hasBlock(p, Material.SLIME_BLOCK)) {
				manager.getPlayer(event.getPlayer()).setViolation(new Violation("Fly"));
			}

		}
	}

	public void Check3(PlayerMoveEvent event) {
		int airBuffer = 0;
		double lastYOffset = 0.0D;
		Player p = event.getPlayer();
		if (!bypass(event.getPlayer())) {
			double deltaY = event.getFrom().getY() - event.getTo().getY();
			if (deltaY > 1.0D && p.getFallDistance() < 1.0F
					|| deltaY > 3.0D && !Utility.hasBlock(p, Material.SLIME_BLOCK)) {
				manager.getPlayer(event.getPlayer()).setViolation(new Violation("Fly"));
			} else {
				if (p.getFallDistance() >= 1.0F) {
					airBuffer = 10;
				}

				if (airBuffer > 0) {
					return;
				}

				Location playerLoc = p.getLocation();
				double change = Math.abs(deltaY - lastYOffset);
				float maxChange = 0.8F;
				if (Utility.isInAir(p) && playerLoc.getBlock().getType() == Material.AIR && change > (double) maxChange
						&& change != 0.5D && !Utility.hasBlock(p, Material.SLIME_BLOCK)) {
					manager.getPlayer(event.getPlayer()).setViolation(new Violation("Fly"));
				}
			}

		}
	}

	public void Check4(PlayerMoveEvent e) {
		final Location from = e.getFrom();
		final Location to = e.getTo();
		double fromy = e.getFrom().getY();
		double toy = e.getTo().getY();
		final Player p = e.getPlayer();
		final double defaultvalue = 0.08307781780646906D;
		final double defaultjump = 0.41999998688697815D;
		final double distance = toy - fromy;
		if (!bypass(e.getPlayer()) && !from.getBlock().getType().isSolid() && !to.getBlock().getType().isSolid()) {
			Bukkit.getScheduler().runTaskLater(NESS.main, new Runnable() {
				public void run() {
					if (to.getY() > from.getY()) {
						if (distance > defaultjump) {
							ArrayList<Block> blocchivicini = Utility.getSurrounding(Utilities.getPlayerUnderBlock(p),
									true);
							boolean bypass = Utility.hasBlock(p, Material.SLIME_BLOCK);
							Iterator var4 = blocchivicini.iterator();

							while (var4.hasNext()) {
								Block s = (Block) var4.next();
								Iterator var6 = bypasses.iterator();

								while (var6.hasNext()) {
									String b = (String) var6.next();
									if (s.getType().toString().toLowerCase().contains(b)) {
										bypass = true;
									}
								}
							}

							if (!bypass) {
								manager.getPlayer(e.getPlayer()).setViolation(new Violation("Fly"));
							}
						} else if (distance == defaultvalue || distance == defaultjump) {
							Location loc = p.getLocation();
							Location loc1 = p.getLocation();
							loc1.setY(loc.getY() - 2.0D);
							if (loc1.getBlock().getType() == Material.AIR
									&& Utilities.getPlayerUnderBlock(p).getType().equals(Material.AIR)
									&& p.getVelocity().getY() <= -0.078D
									&& !loc.getBlock().getType().name().contains("STAIR")
									&& !loc1.getBlock().getType().name().contains("STAIR")
									&& p.getNoDamageTicks() <= 1) {
								manager.getPlayer(e.getPlayer()).setViolation(new Violation("Fly"));
							}
						}
					}

				}
			}, 2L);
		}
	}

	public void Check5(PlayerMoveEvent event) {
		Location from = event.getFrom();
		Location to = event.getTo();
		double dist = Math.pow(to.getX() - from.getX(), 2.0D) + Math.pow(to.getZ() - from.getZ(), 2.0D);
		double defaultvalue = 0.9800000190734863D;
		if (!bypass(event.getPlayer())) {
			double result = dist / defaultvalue;
			if (calculate(result, 1.15D) >= 0 && calculate(dist, 0.8D) >= 0) {
				manager.getPlayer(event.getPlayer()).setViolation(new Violation("Fly"));
			}

		}
	}

	public void Check6(PlayerMoveEvent event) {
		Player p = event.getPlayer();
		if (!bypass(event.getPlayer())) {
			if (!Utility.SpecificBlockNear(event.getTo(), Material.SLIME_BLOCK)
					&& !Utility.SpecificBlockNear(event.getFrom(), Material.SLIME_BLOCK)
					&& !Utility.hasBlock(p, Material.SLIME_BLOCK)) {
				if (p.isOnline()) {
					double yaw = (double) Math.abs(event.getFrom().getYaw() - event.getTo().getYaw());
					double pitch = (double) Math.abs(event.getFrom().getPitch() - event.getTo().getPitch());
					if (Math.abs(p.getVelocity().getY()) > 3.92D) {
						manager.getPlayer(event.getPlayer()).setViolation(new Violation("Fly"));
					} else if (event.getTo().getY() - event.getFrom().getY() > 0.7D) {
						manager.getPlayer(event.getPlayer()).setViolation(new Violation("Fly"));
					} else if (event.getFrom().distanceSquared(event.getTo()) == 0.0D
							&& Utilities.getPlayerUpperBlock(p).getType().equals(Material.AIR) && !Utility.isOnGround(p)
							&& !Utility.isOnGround(p) && pitch < 0.1D && yaw < 0.1D) {
						manager.getPlayer(event.getPlayer()).setViolation(new Violation("Fly"));
					}
				}

			}
		}
	}

	public void Check7(PlayerMoveEvent e) {
		Player player = e.getPlayer();
		Location from = e.getFrom();
		if (!bypass(e.getPlayer())) {
			boolean isonground = Utility.isOnGround(player.getLocation());
			Location to = e.getTo();
			if (player.isOnline() && !Utility.hasBlock(player, Material.SLIME_BLOCK)) {
				Vector vec = new Vector(to.getX(), to.getY(), to.getZ());
				double Distance = vec.distance(new Vector(from.getX(), from.getY(), from.getZ()));
				if (player.getFallDistance() == 0.0F
						&& player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == Material.AIR
						&& player.getLocation().getBlock().getRelative(BlockFace.UP).getType() == Material.AIR) {
					if (Distance > 0.5D && !isonground && e.getTo().getY() > e.getFrom().getY()
							&& e.getTo().getX() == e.getFrom().getX() && e.getTo().getZ() == e.getFrom().getZ()) {
						manager.getPlayer(e.getPlayer()).setViolation(new Violation("Fly"));
					} else if (Distance > 0.9D && !isonground && e.getTo().getY() > e.getFrom().getY()
							&& e.getTo().getX() == e.getFrom().getX() && e.getTo().getZ() == e.getFrom().getZ()) {
						manager.getPlayer(e.getPlayer()).setViolation(new Violation("Fly"));
					} else if (Distance > 1.0D && !isonground && e.getTo().getY() > e.getFrom().getY()
							&& e.getTo().getX() == e.getFrom().getX() && e.getTo().getZ() == e.getFrom().getZ()) {
						manager.getPlayer(e.getPlayer()).setViolation(new Violation("Fly"));
					} else if (Distance > 3.24D && !isonground && e.getTo().getY() > e.getFrom().getY()
							&& e.getTo().getX() == e.getFrom().getX() && e.getTo().getZ() == e.getFrom().getZ()) {
						manager.getPlayer(e.getPlayer()).setViolation(new Violation("Fly"));
					}
				}
			}

		}
	}

	public void Check8(PlayerMoveEvent e) {
		Player player = e.getPlayer();
		if (!bypass(e.getPlayer())) {
			if (player.isOnline() && !Utility.hasBlock(player, Material.SLIME_BLOCK) && player.isOnGround()
					&& !Utility.checkGround(e.getTo().getY())) {
				manager.getPlayer(e.getPlayer()).setViolation(new Violation("Fly"));
			}

		}
	}

	public void Check9(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		if (player.isOnline() && !Utility.hasBlock(player, Material.SLIME_BLOCK)) {
			Location to = event.getTo();
			Location from = event.getFrom();
			if (!player.getAllowFlight() && player.getVehicle() == null
					&& !player.hasPotionEffect(PotionEffectType.SPEED)) {
				double dist = Math.pow(to.getX() - from.getX(), 2.0D) + Math.pow(to.getZ() - from.getZ(), 2.0D);
				double motion = dist / 0.9800000190734863D;
				if (motion >= 1.0D && dist >= 0.8D && !bypass(player)) {
					manager.getPlayer(event.getPlayer()).setViolation(new Violation("Fly"));
				}
			}
		}

	}

	public void Check10(PlayerMoveEvent e) {
		Player p = e.getPlayer();
		double diff = e.getTo().getY() - e.getFrom().getY();
		if (e.getTo().getY() >= e.getFrom().getY()) {
			if (p.getLocation().getBlock().getType() != Material.CHEST
					&& p.getLocation().getBlock().getType() != Material.TRAPPED_CHEST
					&& p.getLocation().getBlock().getType() != Material.ENDER_CHEST
					&& !Utility.checkGround(p.getLocation().getY()) && !Utility.isOnGround(p)
					&& Math.abs(p.getVelocity().getY() - diff) > 1.0E-6D && e.getFrom().getY() < e.getTo().getY()
					&& (p.getVelocity().getY() >= 0.0D || p.getVelocity().getY() < -0.392D)
					&& (double) p.getNoDamageTicks() == 0.0D && bypass(p)) {
				manager.getPlayer(e.getPlayer()).setViolation(new Violation("Fly"));
			}

		}
	}

	public void Check11(PlayerMoveEvent e) {
		Player p = e.getPlayer();
		if (!bypass(e.getPlayer())) {
			if (!p.getLocation().getBlock().isLiquid()) {
				if (!Utility.checkGround(p.getLocation().getY()) && !Utility.isOnGround(p)) {
					ArrayList<Block> blocks = Utility.getSurrounding(p.getLocation().getBlock(), true);
					Iterator var4 = blocks.iterator();

					while (var4.hasNext()) {
						Block b = (Block) var4.next();
						if (b.isLiquid()) {
							return;
						}

						if (b.getType().isSolid()) {
							return;
						}
					}

					if (!bypass(e.getPlayer()) && !e.getFrom().getBlock().getType().isSolid()
							&& !e.getTo().getBlock().getType().isSolid()) {
						double dist = e.getFrom().getY() - e.getTo().getY();
						NessPlayer player = new NessPlayer(p);
						double oldY = player.getOldY();
						player.setOldY(dist);
						if (e.getFrom().getY() > e.getTo().getY()) {
							if (oldY >= dist && oldY != 0.0D) {
								manager.getPlayer(e.getPlayer()).setViolation(new Violation("Fly"));
							}
						} else {
							player.setOldY(0.0D);
						}

					}
				}
			}
		}
	}

	public void Check12(PlayerMoveEvent e) {
		Player player = e.getPlayer();
		Location from = e.getFrom();
		Location to = e.getTo();
		boolean groundAround = Utility.groundAround(player.getLocation());
		if (player.isInsideVehicle() && !groundAround && from.getY() <= to.getY() && (!player.isInsideVehicle()
				|| (player.isInsideVehicle() && player.getVehicle().getType() != EntityType.HORSE))) {
			// nogroundfly
		}
	}

	public void Check13(PlayerMoveEvent e) {
		Player player = e.getPlayer();
		Location from = e.getFrom();
		Location to = e.getTo();
		Double hozDist = to.distanceSquared(from) - (to.getY() - from.getY());
		boolean groundAround = Utility.groundAround(player.getLocation());
		if (!groundAround) {
			if (hozDist > 0.35 && PlayerManager.timeSince("wasIce", player) >= 1000.0 && !player.isFlying()) {
				// hack
			}
		}
	}

	public void Check14(PlayerMoveEvent e) {
		Player player = e.getPlayer();
		Location from = e.getFrom();
		Location to = e.getTo();
		Double hozDist = to.distanceSquared(from) - (to.getY() - from.getY());
		boolean groundAround = Utility.groundAround(player.getLocation());
		if (!groundAround) {
			if (hozDist > 0.35 && PlayerManager.timeSince("wasIce", player) >= 1000.0 && !player.isFlying()) {
				// hack
			}
		}
	}

	public void Check15(PlayerMoveEvent e) {
		Player player = e.getPlayer();
		Location from = e.getFrom();
		Location to = e.getTo();
		Double hozDist = to.distanceSquared(from) - (to.getY() - from.getY());

	}

	public void Check16(PlayerMoveEvent e) {
		Player player = e.getPlayer();
		if (player.getLocation().getYaw() > 360.0f || player.getLocation().getYaw() < -360.0f
				|| player.getLocation().getPitch() > 90.0f || player.getLocation().getPitch() < -90.0f) {
			manager.getPlayer(player).setViolation(new Violation("IllegalMovement"));
		}
	}

	public void Check17(PlayerMoveEvent e) {
		Player player = e.getPlayer();
		Material below = player.getWorld().getBlockAt(player.getLocation().subtract(0.0, 1.0, 0.0)).getType();
		if ((!player.isSneaking() || below != Material.LADDER) && !player.isFlying() && !player.isOnGround()
				&& player.getLocation().getY() % 1.0 == 0.0 && PlayerManager.timeSince("lastJoin", player) >= 1000.0
				&& PlayerManager.timeSince("teleported", player) >= 500.0 && !below.toString().contains("stairs")) {
			int failed = noground.getOrDefault(player.getName(), 0);
			noground.put(player.getName(), failed + 1);
			if (failed > 2) {
				// MSG.tell((CommandSender)player, "&7NoGround: &e" + failed);
				if (!below.equals(Material.SLIME_BLOCK)) {
					manager.getPlayer(player).setViolation(new Violation("NoGround"));
				}
			}
		}
	}

	public void Check18(PlayerMoveEvent e) {
		Player player = e.getPlayer();
		Location from = e.getFrom();
		Location to = e.getTo();
		boolean web = false;
		for (int x2 = -2; x2 <= 2; ++x2) {
			for (int y = -2; y <= 3; ++y) {
				for (int z3 = -2; z3 <= 2; ++z3) {
					final Material belowSel2 = player.getWorld()
							.getBlockAt(player.getLocation().add((double) x2, (double) y, (double) z3)).getType();
					if (belowSel2 == Material.WEB) {
						web = true;
					}
				}
			}
		}
		boolean groundAround = Utility.groundAround(player.getLocation());
		if (!groundAround && !player.isFlying()) {
			if (from.getY() >= to.getY()) {
				final double vel = from.getY() - to.getY();
				if (!web && ((vel > 0.0799 && vel < 0.08) || (vel > 0.01 && vel < 0.02) || (vel > 0.549 && vel < 0.55))
						&& !player.isFlying() && PlayerManager.timeSince("wasFlight", player) >= 3000.0
						&& PlayerManager.timeSince("isHit", player) >= 1000.0) {
					manager.getPlayer(player).setViolation(new Violation("Fly"));
				}
				if (vel > 0.0999 && vel < 0.1 && to.getY() > 0.0) {
					manager.getPlayer(player).setViolation(new Violation("Fly"));
				}
				if (vel == 0.125) {
					manager.getPlayer(player).setViolation(new Violation("Fly"));
				}
			}
		}
	}

	public void Check19(PlayerMoveEvent e) {
		Player player = e.getPlayer();
		Location from = e.getFrom();
		Location to = e.getTo();
		double dTG = 0.0;
		for (int x = -1; x <= 1; ++x) {
			for (int z = -1; z <= 1; ++z) {
				int y;
				for (y = 0; !player.getLocation().subtract((double) x, (double) y, (double) z).getBlock().getType()
						.isSolid() && y < 20; ++y) {
				}
				if (y < dTG || dTG == 0.0) {
					dTG = y;
				}
			}
		}
		Material bottom = player.getLocation().getWorld().getBlockAt(player.getLocation().subtract(0.0, dTG, 0.0)).getType();
		Material below = player.getWorld().getBlockAt(player.getLocation().subtract(0.0, 1.0, 0.0)).getType();
		boolean groundAround = Utility.groundAround(player.getLocation());
		double hozDist = to.distanceSquared(from) - (to.getY() - from.getY());
		double fallDist = (double) player.getFallDistance();
		if (from.getY() - to.getY() > 0.3 && fallDist <= 0.4 && below != Material.STATIONARY_WATER
				&& !player.getLocation().getBlock().isLiquid()) {
			if (hozDist < 0.1 || !groundAround) {
				if (PlayerManager.timeSince("breakTime", player) >= 2000.0
						&& PlayerManager.timeSince("teleported", player) >= 500.0 && below != Material.PISTON_BASE
						&& below != Material.PISTON_STICKY_BASE
						&& (!player.isInsideVehicle()
								|| (player.isInsideVehicle() && player.getVehicle().getType() != EntityType.HORSE))
						&& !player.isFlying() && to.getY() > 0.0 && bottom != Material.SLIME_BLOCK) {
					//NESSPlayer np = NESSPlayer.getInstance(player);
					manager.getPlayer(player).setViolation(new Violation("NoFall"));
				}
			}
		}
	}

	public void Check20(PlayerMoveEvent e) {
		Player player = e.getPlayer();
		Location from = e.getFrom();
		Location to = e.getTo();
		double dTG = 0.0;
		for (int x = -1; x <= 1; ++x) {
			for (int z = -1; z <= 1; ++z) {
				int y;
				for (y = 0; !player.getLocation().subtract((double) x, (double) y, (double) z).getBlock().getType()
						.isSolid() && y < 20; ++y) {
				}
				if (y < dTG || dTG == 0.0) {
					dTG = y;
				}
			}
		}
		dTG += player.getLocation().getY() % 1.0;
		Material below = player.getWorld().getBlockAt(player.getLocation().subtract(0.0, 1.0, 0.0)).getType();
		if (to.getY() > from.getY()) {
			final double lastDTG = PlayerManager.getAction("lastDTG", player);
			final String diff2 = new StringBuilder(String.valueOf(Math.abs(dTG - lastDTG))).toString();
			if (player.getLocation().getY() % 0.5 != 0.0 && !player.isFlying() && !below.isSolid()
					&& (new StringBuilder(String.valueOf(dTG)).toString().contains("99999999")
							|| new StringBuilder(String.valueOf(dTG)).toString().contains("00000000")
							|| diff2.contains("000000") || diff2.startsWith("0.286"))
					&& PlayerManager.timeSince("isHit", player) >= 500.0
					&& !below.toString().toLowerCase().contains("water")
					&& !below.toString().toLowerCase().contains("lava")) {
				if (!Utility.SpecificBlockNear(player.getLocation(), Material.STATIONARY_LAVA)
						&& !Utility.SpecificBlockNear(player.getLocation(), Material.WATER)
						&& !Utility.SpecificBlockNear(player.getLocation(), Material.LAVA)
						&& !Utility.SpecificBlockNear(player.getLocation(), Material.STATIONARY_WATER)) {
					manager.getPlayer(player).setViolation(new Violation("Spider"));
				}
				if (NESS.main.devMode) {
					MSG.tell((CommandSender) player, "&9Dev> &7dTG: " + dTG + " diff: " + diff2);
				}
			}
			PlayerManager.setInfo("lastDTG", (OfflinePlayer) player, dTG);
		}
	}
	
	public void Check21(PlayerMoveEvent e) {
		Player player = e.getPlayer();
		Location from = e.getFrom();
		Location to = e.getTo();
		Double hozDist = to.distanceSquared(from) - (to.getY() - from.getY());
		if (player.getWorld().getBlockAt(player.getLocation()).getType() == Material.WEB && hozDist > 0.140
				&& !player.isFlying() && !player.hasPotionEffect(PotionEffectType.SPEED)) {
			manager.getPlayer(player).setViolation(new Violation("NoWeb"));
		}
	}

	private int calculate(double var0, double var2) {
		double var4;
		return (var4 = var0 - var2) == 0.0D ? 0 : (var4 < 0.0D ? -1 : 1);
	}

	public boolean bypass(Player p) {
		if (p.isInsideVehicle()) {
			return false;
		}
		if (p.hasPotionEffect(PotionEffectType.SPEED) || p.hasPotionEffect(PotionEffectType.JUMP)) {
			return false;
		}
		if (Utilities.isInWeb(p)) {
			return false;
		}
		if (Utility.hasflybypass(p)) {
			return false;
		}
		if (Utilities.getPlayerUnderBlock(p).getType().equals(Material.LADDER)
				&& !Utilities.getPlayerUnderBlock(p).getType().equals(Material.VINE)
				&& Utilities.getPlayerUnderBlock(p).getType().equals(Material.WATER)) {
			return false;
		}
		for (Block b : Utility.getSurrounding(p.getLocation().getBlock(), true)) {
			if (b.getType().isSolid()) {
				return false;
			}
		}
		return true;
	}
}
