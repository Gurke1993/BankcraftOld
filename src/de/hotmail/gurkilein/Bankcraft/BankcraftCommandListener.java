package de.hotmail.gurkilein.Bankcraft;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BankcraftCommandListener implements CommandExecutor {

	@SuppressWarnings("unused")
	private Bankcraft plugin;
	public Double betrag;

	public void sendHelp(Player p) {
		p.sendMessage("---Bankcraft-Help---");
		p.sendMessage("/bank help                     Shows this help");
		p.sendMessage("/bank balance PLAYER           Shows your banked money");
		p.sendMessage("/bank balancexp PLAYER         Shows your banked XP");
		p.sendMessage("/bank deposit AMOUNT           Deposits money on your Account");
		p.sendMessage("/bank debit AMOUNT             Debits money from your Account");
		p.sendMessage("/bank depositxp AMOUNT         Deposits XP on your Account");
		p.sendMessage("/bank debitxp AMOUNT           Debits XP from your Account");
		p.sendMessage("/bank transfer PLAYER AMOUNT   Transfers money");
		p.sendMessage("/bank transferxp PLAYER AMOUNT Transfers XP");
		p.sendMessage("/bank exchange AMOUNT   		  Exchanges money to XP");
		p.sendMessage("/bank exchangexp AMOUNT 		  Exchanges XP to money");
		//Exchange
	}

	public void sendAdminHelp(Player p) {
		p.sendMessage("---Bankcraft-AdminHelp---");
		p.sendMessage("/bankadmin help                  Shows this help");
		p.sendMessage("/bankadmin set PLAYER AMOUNT     Sets a players money");
		p.sendMessage("/bankadmin setxp PLAYER AMOUNT   Sets a players XP");
		p.sendMessage("/bankadmin grant PLAYER AMOUNT   Grants a Player money");
		p.sendMessage("/bankadmin grantxp PLAYER AMOUNT Grants a player XP");
		p.sendMessage("/bankadmin clear PLAYER          Clears a players money-account");
		p.sendMessage("/bankadmin clearxp PLAYER        Clears a players XP-account");
	}

	public BankcraftCommandListener(Bankcraft b1) {
		plugin = b1;
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

	@Override
	public boolean onCommand(CommandSender sender, Command command, String cmdlabel, String[] vars) {
		File file;
		FileWriter writer;
		Player p;
		if (sender instanceof Player) {
			p = (Player) sender;
			if (cmdlabel.equalsIgnoreCase("bank") || cmdlabel.equalsIgnoreCase("bc")) {
				if (vars.length == 0) {
					sendHelp(p);
					return true;
				}
				if (vars.length == 1) {
					if (vars[0].equalsIgnoreCase(configHandler.comhelp)) {
						sendHelp(p);
						return true;
					}

					if (vars[0].equalsIgnoreCase(configHandler.combalance) && (Bankcraft.perms.has(p, "bankcraft.command.balance") || Bankcraft.perms.has(p, "bankcraft.command"))) {
						bankInteract.use(p, "", 0, null, "");
						return true;
					}
					if (vars[0].equalsIgnoreCase(configHandler.combalancexp) && (Bankcraft.perms.has(p, "bankcraft.command.balancexp") || Bankcraft.perms.has(p, "bankcraft.command"))) {
						bankInteract.use(p, "", 5, null, "");
						return true;
					}
				}
				if (vars.length == 2) {
					if (isPositive(vars[1]) || vars[1].equalsIgnoreCase("all")) {
						if (vars[0].equalsIgnoreCase("add") && (Bankcraft.perms.has(p, "bankcraft.admin"))) {
							Block signblock = p.getTargetBlock(null, 50);
							if (signblock.getType() == Material.WALL_SIGN) {
								Sign sign = (Sign) signblock.getState();
								if (sign.getLine(0).contains("[Bank]")) {
									Integer typsign = -1;
									try {
										typsign = bankInteract.getTypeBank(signblock.getX(), signblock.getY(), signblock.getZ(), signblock.getWorld());
									} catch (Exception e) {
										e.printStackTrace();
									}
									if (typsign == 1 | typsign == 2 | typsign == 3 | typsign == 6 | typsign == 7 | typsign == 12 | typsign == 13) {
										if (configHandler.isMysql()) {
											// MySQL
											Integer x = signblock.getX();
											Integer y = signblock.getY();
											Integer z = signblock.getZ();
											World w = signblock.getWorld();
											boolean exist = configHandler.getDb().getBank(x, y, z, w.getName());
											if(exist) {
												String list = configHandler.getDb().getAmountsOfBank(x, y, z, w.getName());
												Integer typ = typsign;
												if (typ == 1) {
													list = sign.getLine(2) + "," + vars[1];
													typ = 3;
												}
												else if (typ == 2) {
													list = sign.getLine(2) + "," + vars[1];
													typ = 4;
												}
												else if (typ == 3) {
													list = list + "," + vars[1];
												}
												else if (typ == 6) {
													list = sign.getLine(2) + "," + vars[1];
													typ = 8;
												}
												else if (typ == 7) {
													list = sign.getLine(2) + "," + vars[1];
													typ = 9;
												}
												else if (typ == 12) {
													list = sign.getLine(2) + "," + vars[1];
													typ = 14;
												}
												else if (typ == 13) {
													list = sign.getLine(2) + "," + vars[1];
													typ = 15;
												}
												configHandler.getDb().deleteBank(x, y, z, w.getName());
												configHandler.getDb().setBank(x, y, z, w.getName(), typ, list);
												p.sendMessage(configHandler.getMessage(configHandler.amountadded, p.getName(), 0D));
											}
										}
										else {
											// File
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
													Integer typ = new Integer(st.split(":")[4]);
													String list = st.split(":")[5];
													Block block = p.getTargetBlock(null, 100);

													World cordW = Bankcraft.server.getWorld(st.split(":")[3]);
													if (!(cordX == block.getX() & cordY == block.getY() & cordZ == block.getZ() && cordW.equals(block.getWorld()))) {
														stringneu += st + System.getProperty("line.separator");
													} else {

														list += "," + vars[1];
														if (typ == 1) {
															list = sign.getLine(2) + "," + vars[1];
															typ = 3;
														}
														if (typ == 2) {
															list = sign.getLine(2) + "," + vars[1];
															typ = 4;
														}
														if (typ == 6) {
															list = sign.getLine(2) + "," + vars[1];
															typ = 8;
														}
														if (typ == 7) {
															list = sign.getLine(2) + "," + vars[1];
															typ = 9;
														}
														if (typ == 12) {
															list = sign.getLine(2) + "," + vars[1];
															typ = 14;
														}
														if (typ == 13) {
															list = sign.getLine(2) + "," + vars[1];
															typ = 15;
														}
														stringneu += cordX + ":" + cordY + ":" + cordZ + ":" + cordW.getName() + ":" + typ + ":" + list + System.getProperty("line.separator");
														p.sendMessage(configHandler.getMessage(configHandler.amountadded, p.getName(), 0D));
													}
												}
												fr.close();
												reader.close();
												writer = new FileWriter(file);
												writer.write(stringneu);
												writer.flush();
												writer.close();
												return true;
											} catch (IOException e) {
												e.printStackTrace();
											}
										}
									}
								}
							}
						}

						else if (vars[0].equalsIgnoreCase(configHandler.comdeposit) && (Bankcraft.perms.has(p, "bankcraft.command.deposit") || Bankcraft.perms.has(p, "bankcraft.command"))) {

							bankInteract.use(p, vars[1], 1, null, "");
							return true;
						}


						else if (vars[0].equalsIgnoreCase(configHandler.comdebit) && (Bankcraft.perms.has(p, "bankcraft.command.debit") || Bankcraft.perms.has(p, "bankcraft.command"))) {
							bankInteract.use(p, vars[1], 2, null, "");
							return true;
						}

						else if (vars[0].equalsIgnoreCase(configHandler.comdepositxp) && (Bankcraft.perms.has(p, "bankcraft.command.depositxp") || Bankcraft.perms.has(p, "bankcraft.command"))) {

							bankInteract.use(p, vars[1], 6, null, "");
							return true;
						}
						else if (vars[0].equalsIgnoreCase(configHandler.comdebitxp) && (Bankcraft.perms.has(p, "bankcraft.command.debitxp") || Bankcraft.perms.has(p, "bankcraft.command"))) {
							bankInteract.use(p, vars[1], 7, null, "");
							return true;
						}

						else if (vars[0].equalsIgnoreCase(configHandler.comexchange) && (Bankcraft.perms.has(p, "bankcraft.command.exchange") || Bankcraft.perms.has(p, "bankcraft.command"))) {
							bankInteract.use(p, vars[1], 12, null, "");
							return true;
						}

						else if (vars[0].equalsIgnoreCase(configHandler.comexchangexp) && (Bankcraft.perms.has(p, "bankcraft.command.exchangexp") || Bankcraft.perms.has(p, "bankcraft.command"))) {
							bankInteract.use(p, vars[1], 13, null, "");
							return true;
						}

						else {
							p.sendMessage(ChatColor.RED + configHandler.prefix + "Wrong Syntax or missing permissions! Please see /bank help for more information!");
						}

					} else {
						if (vars[0].equalsIgnoreCase(configHandler.combalance) && (Bankcraft.perms.has(p, "bankcraft.command.balance.other") || Bankcraft.perms.has(p, "bankcraft.command"))) {
							bankInteract.use(p, "", 10, null, vars[1]);
							return true;
						}


						else if (vars[0].equalsIgnoreCase(configHandler.combalancexp) && (Bankcraft.perms.has(p, "bankcraft.command.balancexp.other") || Bankcraft.perms.has(p, "bankcraft.command"))) {
							bankInteract.use(p, "", 11, null, vars[1]);
							return true;
						}
					}


				}

				if (vars.length == 3) {
					if (isPositive(vars[2]) || vars[2].equalsIgnoreCase("all")) {
						if (vars[0].equalsIgnoreCase(configHandler.comtransfer) && (Bankcraft.perms.has(p, "bankcraft.command.transfer") || Bankcraft.perms.has(p, "bankcraft.command"))) {
							betrag = new Double(vars[2]);
							if (configHandler.limit == -1 || configHandler.limit >= betrag + bankInteract.getBalance(vars[1])) {
								String differenzstring = bankInteract.kontoneu(-betrag, p.getName(), false);
								if (!differenzstring.equals("error")) {
									if (bankInteract.getBalance(p.getName()) < 0 && bankInteract.getBalance(p.getName()) + betrag >= 0) {
										bankInteract.startLoanPunishment(p.getName());
									}

									bankInteract.kontoneu(betrag, vars[1], false);
									if (bankInteract.getBalance(vars[1]) >= 0 && bankInteract.getBalance(vars[1]) - betrag < 0) {
										bankInteract.stopLoanPunishment(vars[1]);
									}
									if (configHandler.log) {
										Bankcraft.log.info("[Bankcraft] "+p.getName()+" transfered "+betrag+" money to "+vars[1]+"!");
									}
									p.sendMessage(configHandler.getMessage(configHandler.success3, p.getName(), betrag));

								} else {
									p.sendMessage(configHandler.getMessage(configHandler.lowmoney2, p.getName(), betrag));
								}
							} else {
								p.sendMessage(configHandler.getMessage(configHandler.limitmsg, p.getName(), betrag));
							}
							return true;
						}


						else if (vars[0].equalsIgnoreCase(configHandler.comtransferxp) && (Bankcraft.perms.has(p, "bankcraft.command.transferxp") || Bankcraft.perms.has(p, "bankcraft.command"))) {

							if (configHandler.limitxp == -1 || configHandler.limitxp >= betrag + bankInteract.getBalanceXP(vars[1])) {
								String differenzstring = bankInteract.kontoneuxp(-betrag.intValue(), p.getName(), false);
								if (!differenzstring.equals("error")) {
									if (bankInteract.getBalanceXP(p.getName()) < 0 && bankInteract.getBalanceXP(p.getName()) + betrag >= 0) {
										bankInteract.startLoanPunishmentXP(p.getName());
									}
									bankInteract.kontoneuxp(betrag.intValue(), vars[1], false);
									if (bankInteract.getBalanceXP(vars[1]) >= 0 && bankInteract.getBalance(vars[1]) - betrag < 0) {
										bankInteract.stopLoanPunishmentXP(vars[1]);
									}
									if (configHandler.log) {
										Bankcraft.log.info("[Bankcraft] "+p.getName()+" transfered "+betrag+" Xp to "+vars[1]+"!");
									}
									p.sendMessage(configHandler.getMessage(configHandler.success3xp, p.getName(), betrag));
								} else {
									p.sendMessage(configHandler.getMessage(configHandler.lowmoney2xp, p.getName(), betrag));
								}
								return true;
							} else {
								p.sendMessage(configHandler.getMessage(configHandler.limitmsgxp, vars[1], betrag));
							}
						}
						
						else {
							p.sendMessage(ChatColor.RED + configHandler.prefix + "Wrong Syntax or missing permissions! Please see /bank help for more information!");
						}
					}
				}

				return true;
			} else {

				if (cmdlabel.equalsIgnoreCase("bankadmin") || cmdlabel.equalsIgnoreCase("bcadmin")) {
					if (vars.length == 0) {
						sendAdminHelp(p);
						return true;
					}
					else if (vars.length == 1) {

						if (vars[0].equalsIgnoreCase(configHandler.comadmhelp)) {
							sendAdminHelp(p);
							return true;
						}
					}
					else if (vars.length == 2) {
						if (vars[0].equalsIgnoreCase(configHandler.comadmclear) && (Bankcraft.perms.has(p, "bankcraft.command.clear") || Bankcraft.perms.has(p, "bankcraft.command.admin"))) {

							if (bankInteract.getBalance(vars[1]) < 0) {
								bankInteract.stopLoanPunishment(vars[1]);
							}
							if (configHandler.isMysql()) {
								//MySQL
								boolean exist = configHandler.getDb().getAccount(vars[1]);
								if (!exist) {
									configHandler.getDb().setAccount(vars[1], 0D);
								}
								else {
									configHandler.getDb().deposit("0.00", vars[1]);
								}
							}
							else {
								// File
								file = new File("plugins" + System.getProperty("file.separator") + "Bankcraft" + System.getProperty("file.separator") + "Accounts" + System.getProperty("file.separator") + vars[1] + ".db");
								try {
									if (!file.exists()) {
										file.createNewFile();
									}
									writer = new FileWriter(file, false);
									writer.write("0.00");
									writer.flush();
									writer.close();
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
							if (configHandler.log) {
								Bankcraft.log.info("[Bankcraft] "+p.getName()+" cleared "+vars[1]+"'s money-account!");
							}
							p.sendMessage(configHandler.color + configHandler.prefix + "Account cleared!");
							return true;
						}
						if (vars[0].equalsIgnoreCase(configHandler.comadmclearxp) && (Bankcraft.perms.has(p, "bankcraft.command.clearxp") || Bankcraft.perms.has(p, "bankcraft.command.admin"))) {
							if (bankInteract.getBalanceXP(vars[1]) < 0) {
								bankInteract.stopLoanPunishmentXP(vars[1]);
							}
							if (configHandler.isMysql()) {
								// MySQL
								boolean exist = configHandler.getDb().getXpAccount(vars[1]);
								if (!exist) {
									configHandler.getDb().setXpAccount(vars[1], "0");
								}
								else {
									configHandler.getDb().depositXp("0", vars[1]);
								}
							}
							else {
								// File
								file = new File("plugins" + System.getProperty("file.separator") + "Bankcraft" + System.getProperty("file.separator") + "XPAccounts" + System.getProperty("file.separator") + vars[1] + ".db");
								try {
									if (!file.exists()) {
										file.createNewFile();
									}
									writer = new FileWriter(file, false);
									writer.write("0.00");
									writer.flush();
									writer.close();
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
							if (configHandler.log) {
								Bankcraft.log.info("[Bankcraft] "+p.getName()+" cleared "+vars[1]+"'s Xp-account!");
							}
							p.sendMessage(configHandler.color + configHandler.prefix + "XP-Account cleared!");
							return true;
						}
					}
					else if (vars.length == 3) {
						if (isDouble(vars[2])) {
							if (vars[0].equalsIgnoreCase(configHandler.comadmset) && (Bankcraft.perms.has(p, "bankcraft.command.set") || Bankcraft.perms.has(p, "bankcraft.command.admin"))) {
								if (configHandler.isMysql()) {
									// MySQL
									boolean exist = configHandler.getDb().getAccount(vars[1]);
									if (!exist) {
										configHandler.getDb().setAccount(vars[1], new Double(vars[2]));
									}
									else {
										configHandler.getDb().deposit(vars[2], vars[1]);
									}
									Double konto = bankInteract.getBalance(vars[1]);
									if (bankInteract.getBalance(vars[1]) >= 0 && konto < 0) {
										bankInteract.stopLoanPunishment(vars[1]);
									}
									if (bankInteract.getBalance(vars[1]) < 0 && konto >= 0) {
										bankInteract.startLoanPunishment(vars[1]);
									}
								}
								else {
									file = new File("plugins" + System.getProperty("file.separator") + "Bankcraft" + System.getProperty("file.separator") + "Accounts" + System.getProperty("file.separator") + vars[1] + ".db");
									try {
										if (!file.exists()) {
											file.createNewFile();
										}
										Double konto = bankInteract.getBalance(vars[1]);
										writer = new FileWriter(file, false);
										writer.write(vars[2]);
										writer.flush();
										writer.close();
										if (bankInteract.getBalance(vars[1]) >= 0 && konto < 0) {
											bankInteract.stopLoanPunishment(vars[1]);
										}
										if (bankInteract.getBalance(vars[1]) < 0 && konto >= 0) {
											bankInteract.startLoanPunishment(vars[1]);
										}
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
								if (configHandler.log) {
									Bankcraft.log.info("[Bankcraft] "+p.getName()+" set "+vars[1]+"'s money account to "+vars[2]+"!");
								}
								p.sendMessage(configHandler.color + configHandler.prefix + "Account set!");
								return true;
							}

							if (vars[0].equalsIgnoreCase(configHandler.comadmsetxp) && (Bankcraft.perms.has(p, "bankcraft.command.setxp") || Bankcraft.perms.has(p, "bankcraft.command.admin"))) {
								if (configHandler.isMysql()) {
									// MySQL
									boolean exist = configHandler.getDb().getXpAccount(vars[1]);
									if (!exist) {
										configHandler.getDb().setXpAccount(vars[1], vars[2]);
									}
									else {
										configHandler.getDb().depositXp(vars[2], vars[1]);
									}
									Integer kontoxp = bankInteract.getBalanceXP(vars[1]);
									if (bankInteract.getBalanceXP(vars[1]) >= 0 && kontoxp < 0) {
										bankInteract.stopLoanPunishment(vars[1]);
									}
									if (bankInteract.getBalanceXP(vars[1]) < 0 && kontoxp >= 0) {
										bankInteract.startLoanPunishment(vars[1]);
									}
								}
								else {
									file = new File("plugins" + System.getProperty("file.separator") + "Bankcraft" + System.getProperty("file.separator") + "XPAccounts" + System.getProperty("file.separator") + vars[1] + ".db");
									try {
										if (!file.exists()) {
											file.createNewFile();
										}
										Integer kontoxp = bankInteract.getBalanceXP(vars[1]);
										writer = new FileWriter(file, false);
										writer.write(vars[2]);
										writer.flush();
										writer.close();
										if (bankInteract.getBalanceXP(vars[1]) >= 0 && kontoxp < 0) {
											bankInteract.stopLoanPunishment(vars[1]);
										}
										if (bankInteract.getBalanceXP(vars[1]) < 0 && kontoxp >= 0) {
											bankInteract.startLoanPunishment(vars[1]);
										}
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
								if (configHandler.log) {
									Bankcraft.log.info("[Bankcraft] "+p.getName()+" set "+vars[1]+"'s Xp-account to "+vars[2]+"!");
								}
								p.sendMessage(configHandler.color + configHandler.prefix + "XP-Account set!");
								return true;
							}


							if (vars[0].equalsIgnoreCase(configHandler.comadmgrant) && (Bankcraft.perms.has(p, "bankcraft.command.grant") || Bankcraft.perms.has(p, "bankcraft.command.admin"))) {
								betrag = new Double(vars[2]);
								String differenzstring = bankInteract.kontoneu(betrag, vars[1], false);
								if (!differenzstring.equals("error")) {
									if (bankInteract.getBalance(vars[1]) >= 0 && bankInteract.getBalance(vars[1]) - betrag < 0) {
										bankInteract.stopLoanPunishment(vars[1]);
									}
									if (bankInteract.getBalance(vars[1]) < 0 && bankInteract.getBalance(vars[1]) + betrag >= 0) {
										bankInteract.startLoanPunishment(vars[1]);
									}
									if (configHandler.log) {
										Bankcraft.log.info("[Bankcraft] "+p.getName()+" granted "+vars[1]+" "+vars[2]+" money!");
									}
									p.sendMessage(configHandler.getMessage(configHandler.success1, vars[1], betrag));
								} else {
									p.sendMessage(configHandler.getMessage(configHandler.lowmoney3, vars[1], betrag));
								}
								return true;

							}

							if (vars[0].equalsIgnoreCase(configHandler.comadmgrantxp) && (Bankcraft.perms.has(p, "bankcraft.command.grantxp") || Bankcraft.perms.has(p, "bankcraft.command.admin"))) {
								betrag = new Double(vars[2]);
								String differenzstring = bankInteract.kontoneuxp(betrag.intValue(), vars[1], false);
								if (!differenzstring.equals("error")) {
									if (bankInteract.getBalanceXP(vars[1]) >= 0 && bankInteract.getBalanceXP(vars[1]) - betrag < 0) {
										bankInteract.stopLoanPunishment(vars[1]);
									}
									if (bankInteract.getBalanceXP(vars[1]) < 0 && bankInteract.getBalanceXP(vars[1]) + betrag >= 0) {
										bankInteract.startLoanPunishment(vars[1]);
									}
									if (configHandler.log) {
										Bankcraft.log.info("[Bankcraft] "+p.getName()+" granted "+vars[1]+" "+vars[2]+" Xp!");
									}
									p.sendMessage(configHandler.getMessage(configHandler.success1xp, vars[1], betrag));
								} else {
									p.sendMessage(configHandler.getMessage(configHandler.lowmoney3xp, vars[1], betrag));
								}
								return true;
							}
						}
					}
					else {
						p.sendMessage(ChatColor.RED + configHandler.prefix + "Wrong Syntax or missing permissions! Please see /bank help for more information!");
					}
					return true;
				}
			}
		} else {
			Bankcraft.log.info("[Bankcraft] Please use this ingame!");
		}

		return false;
	}
}
