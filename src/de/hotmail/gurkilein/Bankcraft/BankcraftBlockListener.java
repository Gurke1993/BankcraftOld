package de.hotmail.gurkilein.Bankcraft;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;

public class BankcraftBlockListener implements Listener {

	private static final BlockFace[] faces = new BlockFace[]{BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST};
	FileWriter writer;
	File file, file2;

	public void speichern(int x, int y, int z, int typ, World w, String betrag) {
		if (configHandler.isMysql()){
			// MySQL
			boolean exist = configHandler.getDb().getBank(x, y, z, w.getName());
			if(!exist){
				configHandler.getDb().setBank(x, y, z, w.getName(), typ, betrag);
			}	
		}
		else {
			// File anlegen
			file = new File("plugins" + System.getProperty("file.separator") + "Bankcraft");
			try {
				if (!file.exists()) {
					file.mkdirs();
				}
				file2 = new File(file + System.getProperty("file.separator") + "banks.db");
				file2.createNewFile();
				writer = new FileWriter(file2, true);

				// Text wird in den Stream geschrieben
				writer.write(x + ":" + y + ":" + z + ":" + w.getName() + ":" + typ + ":" + betrag);
				writer.write(System.getProperty("line.separator"));
				writer.flush();

				// Schlie??t den Stream  English please ! i m using utf8 =)
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void delete(int x, int y, int z, World w) {
		if (configHandler.isMysql()) {
			// MySQL
			configHandler.getDb().deleteBank(x, y, z, w.getName());
		}
		else {
			// File anlegen
			file = new File("plugins" + System.getProperty("file.separator") + "Bankcraft" + System.getProperty("file.separator") + "banks.db");
			try {
				String stringneu = "";
				FileReader fr = new FileReader(file);
				BufferedReader reader = new BufferedReader(fr);
				String st = "";
				while ((st = reader.readLine()) != null) {
					Integer cordX = new Integer(st.split(":")[0]);
					Integer cordY = new Integer(st.split(":")[1]);
					Integer cordZ = new Integer(st.split(":")[2]);
					World cordW = Bankcraft.server.getWorld(st.split(":")[3]);
					if (!(cordX == x & cordY == y & cordZ == z && cordW.equals(w))) {
						stringneu += st + System.getProperty("line.separator");
					}
				}
				fr.close();
				reader.close();
				writer = new FileWriter(file);
				writer.write(stringneu);
				writer.flush();
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public boolean isDouble(String input) {
		try {
			Double.parseDouble(input);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public boolean isPositive(String input) {
		if (isDouble(input)) {
			if (new Double(input) >= 0) {
				return true;
			}
		}
		return false;
	}

	@EventHandler
	public void onBlockDestroy(BlockBreakEvent event) throws Exception {
		Player p = event.getPlayer();
		Block testblock = event.getBlock();
		Material type = testblock.getType();
		//Check blocks around destroyed block for signs
		for (BlockFace face : faces) {
			type = testblock.getRelative(face).getType();
			Block nachbarblock = testblock.getRelative(face);

			//if sign found
			if (type == Material.WALL_SIGN) {
				if ((nachbarblock.getData() == 3 & face.equals(BlockFace.WEST)) | (nachbarblock.getData() == 2 & face.equals(BlockFace.EAST)) | (nachbarblock.getData() == 4 & face.equals(BlockFace.NORTH)) | (nachbarblock.getData() == 5 & face.equals(BlockFace.SOUTH))) {

					//check the found sign for the players name.
					Sign sign = (Sign) testblock.getRelative(face).getState();
					//You may want to add to lowercase function
					if (sign.getLine(0).contains("[Bank]")) {
						if (!Bankcraft.perms.has(p, "bankcraft.admin")) {
							p.sendMessage(configHandler.disallow);
							event.setCancelled(true);
							return;
						} else {
							if (configHandler.isMysql()) {
								// MySQL
								Integer x = event.getBlock().getX();
								Integer y = event.getBlock().getY();
								Integer z = event.getBlock().getZ();
								World w = event.getBlock().getWorld();
								boolean exist = configHandler.getDb().getBank(x, y, z, w.getName());
								if(exist) {
									delete(x, y, z, w);
								}
							}
							else {
								// File
								File f = new File("plugins" + System.getProperty("file.separator") + "Bankcraft" + System.getProperty("file.separator") + "banks.db");

								f.createNewFile();

								FileReader fr = new FileReader(f);
								BufferedReader reader = new BufferedReader(fr);
								String st = "";
								while ((st = reader.readLine()) != null) {
									Integer cordX = new Integer(st.split(":")[0]);
									Integer cordY = new Integer(st.split(":")[1]);
									Integer cordZ = new Integer(st.split(":")[2]);
									World cordw = Bankcraft.server.getWorld(st.split(":")[3]);
									if (cordX == nachbarblock.getX() & cordY == nachbarblock.getY() & cordZ == nachbarblock.getZ() & cordw.equals(nachbarblock.getWorld())) {
										delete(cordX, cordY, cordZ, cordw);
									}
								}
							}
							p.sendMessage(configHandler.destroy);
						}
					}
				}
			}
		}
		String block = event.getBlock().getType().toString();
		if ("WALL_SIGN".equals(block)) {
			Sign sign = ((Sign) event.getBlock().getState());
			if (sign.getLine(0).contains("[Bank]")) {
				if (p.getGameMode().equals(GameMode.CREATIVE) && !p.isSneaking()) {
					event.setCancelled(true);
					return;
				}
				if (Bankcraft.perms.has(p, "bankcraft.admin")) {
					if (configHandler.isMysql()) {
						// MySQL
						Integer x = event.getBlock().getX();
						Integer y = event.getBlock().getY();
						Integer z = event.getBlock().getZ();
						World w = event.getBlock().getWorld();
						boolean exist = configHandler.getDb().getBank(x, y, z, w.getName());
						if(exist) {
							delete(x, y, z, w);
						}
					}
					else {
						// File
						File f = new File("plugins" + System.getProperty("file.separator") + "Bankcraft" + System.getProperty("file.separator") + "banks.db");

						f.createNewFile();

						FileReader fr = new FileReader(f);
						BufferedReader reader = new BufferedReader(fr);
						String st = "";
						while ((st = reader.readLine()) != null) {
							Integer cordX = new Integer(st.split(":")[0]);
							Integer cordY = new Integer(st.split(":")[1]);
							Integer cordZ = new Integer(st.split(":")[2]);
							World cordw = Bankcraft.server.getWorld(st.split(":")[3]);
							if (cordX == event.getBlock().getX() & cordY == event.getBlock().getY() & cordZ == event.getBlock().getZ() & event.getBlock().getWorld().equals(cordw)) {
								delete(cordX, cordY, cordZ, cordw);
							}
						}
					}
					p.sendMessage(configHandler.destroy);
				} else {
					event.setCancelled(true);
					p.sendMessage(configHandler.disallow);
				}
			}
		}
	}

	@EventHandler
	public void onSignPlace(SignChangeEvent event) {
		Player p = event.getPlayer();
		String ersteReihe = event.getLine(0);
		if (ersteReihe.equalsIgnoreCase("[Bank]")) {
			if (Bankcraft.perms.has(p, "bankcraft.admin")) {
				if (((event.getLine(1).equals(configHandler.depositsign) | event.getLine(1).equals(configHandler.exchangesign) | event.getLine(1).equals(configHandler.exchangesignxp) | event.getLine(1).equals(configHandler.debitsign) | event.getLine(1).equals(configHandler.debitsignxp) | event.getLine(1).equals(configHandler.depositsignxp)) && (isPositive(event.getLine(2))) || event.getLine(2).equalsIgnoreCase("all")) == true) {
					//ERSTELLEN DER BANK
					event.setLine(0, configHandler.bankcolor + "[Bank]");
					double betrag = 0;
					String typreihe = event.getLine(1);
					int signX = event.getBlock().getX();
					int signY = event.getBlock().getY();
					int signZ = event.getBlock().getZ();
					Integer typ = -1;
					if (typreihe.equals(configHandler.depositsign)) {
						if (event.getLine(2).equalsIgnoreCase("all")) {
							event.setLine(2, "All");
							betrag = -1;
						} else {
							betrag = new Double(event.getLine(2));
						}
						typ = 1;
					}
					if (typreihe.equals(configHandler.debitsign)) {
						if (event.getLine(2).equalsIgnoreCase("all")) {
							event.setLine(2, "All");
							betrag = -1;
						} else {
							betrag = new Double(event.getLine(2));
						}
						typ = 2;
					}

					if (typreihe.equals(configHandler.depositsignxp)) {
						if (event.getLine(2).equalsIgnoreCase("all")) {
							event.setLine(2, "All");
							betrag = -1;
						} else {
							betrag = new Double(event.getLine(2));
						}
						typ = 6;
					}
					if (typreihe.equals(configHandler.debitsignxp)) {
						if (event.getLine(2).equalsIgnoreCase("all")) {
							event.setLine(2, "All");
							betrag = -1;
						} else {
							betrag = new Double(event.getLine(2));
						}
						typ = 7;
					}
					if (typreihe.equals(configHandler.exchangesign)) {
						if (event.getLine(2).equalsIgnoreCase("all")) {
							event.setLine(2, "All");
							betrag = -1;
						} else {
							betrag = new Double(event.getLine(2));
						}
						typ = 12;
					}
					if (typreihe.equals(configHandler.exchangesignxp)) {
						if (event.getLine(2).equalsIgnoreCase("all")) {
							event.setLine(2, "All");
							betrag = -1;
						} else {
							betrag = new Double(event.getLine(2));
						}
						typ = 13;
					}
					speichern(signX, signY, signZ, typ, event.getBlock().getWorld(), betrag + "");
					p.sendMessage(configHandler.make);

				} else {
					if (event.getLine(1).equals(configHandler.balancesign) | (event.getLine(1).equals(configHandler.balancesignxp))) {
						event.setLine(0, configHandler.bankcolor + "[Bank]");
						int signX = event.getBlock().getX();
						int signY = event.getBlock().getY();
						int signZ = event.getBlock().getZ();
						if (event.getLine(1).equals(configHandler.balancesignxp)) {
							if (event.getLine(2).isEmpty()) {
								speichern(signX, signY, signZ, 5, event.getBlock().getWorld(), "0");
							} else {
								speichern(signX, signY, signZ, 11, event.getBlock().getWorld(), "0");
							}
						} else {
							if (event.getLine(2).isEmpty()) {
								speichern(signX, signY, signZ, 0, event.getBlock().getWorld(), "0");
							} else {
								speichern(signX, signY, signZ, 10, event.getBlock().getWorld(), "0");
							}
						}
						p.sendMessage(configHandler.make);
					} else {
						p.sendMessage(configHandler.errorcreate);
						event.setLine(0, "");
						event.setLine(1, "");
						event.setLine(2, "");
						event.setLine(3, "");
					}
				}
			} else {
				p.sendMessage(configHandler.disallow);
				event.setLine(0, "");
				event.setLine(1, "");
				event.setLine(2, "");
				event.setLine(3, "");
			}
		}

	}
}
