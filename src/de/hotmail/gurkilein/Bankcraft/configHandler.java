package de.hotmail.gurkilein.Bankcraft;

import de.hotmail.gurkilein.Bankcraft.database.BcMysql;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class configHandler {

	public static Double exchangerate, interest, interestxp, limit, limitxp, maxloan, maxloanxp, loaninterest, loaninterestxp;
	public static ChatColor bankcolor, color;
	public static Integer timer, loandamage;
	public static Boolean log, broadcast, broadcastxp, onlinemoney, onlinexp;
	public static String exchangesign, exchangesignxp, success4, success4xp, comexchange, comexchangexp, balancexpother, balanceother, nowinloan, nowinloanxp, nolongerloanxp, nolongerloan, loangroup, loangroupxp, interestlimitmsg, interestlimitmsgxp, limitmsg, limitmsgxp, comhelp, combalance, combalancexp, comdeposit, comdebit, comdepositxp, comdebitxp, comtransfer, comtransferxp, comadmhelp, comadmset, comadmsetxp, comadmgrant, comadmgrantxp, comadmclear, comadmclearxp, amountadded, prefix, make, destroy, depositsign, debitsign, balancesign, depositsignxp, debitsignxp, balancesignxp, disallow, errorcreate, lowmoney1, lowmoney2, lowmoney3, success1, success2, balance, lowmoney1xp, lowmoney2xp, lowmoney3xp, success3, success3xp, success1xp, success2xp, balancexp, interestmsg, interestxpmsg;
	private static final ChatColor[] colors = new ChatColor[]{ChatColor.AQUA, ChatColor.BLACK, ChatColor.BLUE, ChatColor.DARK_AQUA, ChatColor.DARK_BLUE, ChatColor.DARK_GRAY, ChatColor.DARK_GREEN, ChatColor.DARK_PURPLE, ChatColor.DARK_RED, ChatColor.GOLD, ChatColor.GRAY, ChatColor.GREEN, ChatColor.LIGHT_PURPLE, ChatColor.RED, ChatColor.WHITE, ChatColor.YELLOW};
	public static String[][] interestGroups;
	private Bankcraft plugin;
	private static BcMysql database;
	private static boolean mysql = false;

	public configHandler(Bankcraft b1) {
		this.plugin = b1;
	}
	
	public static boolean isMysql() {
		return mysql;
	}
	
	public static BcMysql getDb() {
		return database;
	}
	
	public static boolean isPlayer(String pstring) {
		try {
			Player p = Bankcraft.server.getPlayer(pstring);
			p.getLevel();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public static String getMessage(String input, String pstring, Double amount) {
		String output;
		output = input;
		output = output.replace("%exchangerate", exchangerate + "");
		output = output.replace("%maxloanxp", maxloanxp + "");
		output = output.replace("%maxloan", maxloan + "");
		output = output.replace("%limitxp", limitxp + "");
		output = output.replace("%limit", limit + "");
		if (isPlayer(pstring)) {
			output = output.replace("%player", pstring);
			if (isMysql()){
				output = output.replace("%balancexp", bankInteract.getBalanceXP(pstring) + "");
				output = output.replace("%balance", bankInteract.getBalance(pstring) + "");
				output = output.replace("%pocketxp", bankInteract.getTotalXp(Bankcraft.server.getPlayer(pstring)) + "");
			}
			else {
				if ((new File("plugins" + System.getProperty("file.separator") + "Bankcraft" + System.getProperty("file.separator") + "XPAccounts" + System.getProperty("file.separator") + pstring + ".db").exists() && !isMysql()) || (isMysql() && getDb().getXpAccount(pstring))) {
					output = output.replaceAll("%balancexp", bankInteract.getBalanceXP(pstring) + "");
				}
				if ((new File("plugins" + System.getProperty("file.separator") + "Bankcraft" + System.getProperty("file.separator") + "Accounts" + System.getProperty("file.separator") + pstring + ".db").exists() && !isMysql()) || (isMysql() && getDb().getAccount(pstring))) {
					output = output.replace("%balance", bankInteract.getBalance(pstring) + "");
				}
			}
			if (Bankcraft.econ.hasAccount(pstring)) {
					output = output.replace("%pocket", Bankcraft.econ.getBalance(pstring) + "");
			}
			if (Bankcraft.server.getPlayer(pstring) != null) {
				output = output.replace("%pocketxp", bankInteract.getTotalXp(Bankcraft.server.getPlayer(pstring)) + "");
			}
		}
		if (amount != null) {
			output = output.replace("%amountint", amount.intValue() + "");
			output = output.replace("%amount", amount + "");
		}
		return output;
	}

	public boolean setConfig() {

		try {
			File interestdb = new File("plugins" + System.getProperty("file.separator") + "Bankcraft" + System.getProperty("file.separator") + "interests.db");
			interestdb.createNewFile();

			FileReader fr = new FileReader(interestdb);
			BufferedReader reader = new BufferedReader(fr);
			String st;
			List<String> interestGroupsLoad1 = new ArrayList<>();
			List<String> interestGroupsLoad2 = new ArrayList<>();
			List<String> interestGroupsLoad3 = new ArrayList<>();
			List<String> interestGroupsLoad4 = new ArrayList<>();
			List<String> interestGroupsLoad5 = new ArrayList<>();
			while ((st = reader.readLine()) != null) {
				interestGroupsLoad1.add(st.split("=")[0]);
				interestGroupsLoad2.add(st.split("=")[1]);
				interestGroupsLoad3.add(st.split("=")[2]);
				interestGroupsLoad4.add(st.split("=")[3]);
				interestGroupsLoad5.add(st.split("=")[4]);
			}
			interestGroups = new String[5][interestGroupsLoad2.size()];
			for (int i = 0; i < interestGroups[1].length; i++) {
				interestGroups[0][i] = interestGroupsLoad1.get(i);
				interestGroups[1][i] = interestGroupsLoad2.get(i);
				interestGroups[2][i] = interestGroupsLoad3.get(i);
				interestGroups[3][i] = interestGroupsLoad4.get(i);
				interestGroups[4][i] = interestGroupsLoad5.get(i);
			}
			fr.close();
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		color = colors[this.plugin.getConfig().getInt("language.color")];
		bankcolor = colors[this.plugin.getConfig().getInt("language.signcolor")];
		loandamage = this.plugin.getConfig().getInt("general.loan.damage");
		if (loandamage < 0) {
			loandamage = 0;
		}
		prefix = this.plugin.getConfig().getString("language.prefix") + " ";
		loangroup = this.plugin.getConfig().getString("general.loan.group");
		loangroupxp = this.plugin.getConfig().getString("general.loan.groupxp");
		loaninterest = this.plugin.getConfig().getDouble("general.loan.interest");
		loaninterestxp = this.plugin.getConfig().getDouble("general.loan.interestxp");
		maxloan = this.plugin.getConfig().getDouble("general.loan.maxloan");
		if (maxloan < 0) {
			maxloan = 0D;
		}
		maxloanxp = this.plugin.getConfig().getDouble("general.loan.maxloanxp");
		if (maxloanxp < 0) {
			maxloanxp = 0D;
		}
		log = this.plugin.getConfig().getBoolean("general.log");
		limit = this.plugin.getConfig().getDouble("general.limit");
		limitxp = this.plugin.getConfig().getDouble("general.limitxp");
		exchangerate = this.plugin.getConfig().getDouble("general.exchangerate");
		interest = this.plugin.getConfig().getDouble("general.interest");
		interestxp = this.plugin.getConfig().getDouble("general.interestxp");
		onlinemoney = this.plugin.getConfig().getBoolean("general.justonlinemoney");
		onlinexp = this.plugin.getConfig().getBoolean("general.justonlinexp");
		broadcast = this.plugin.getConfig().getBoolean("general.broadcast");
		broadcastxp = this.plugin.getConfig().getBoolean("general.broadcastxp");
		timer = this.plugin.getConfig().getInt("general.timer");
		exchangesign = this.plugin.getConfig().getString("language.exchangesign");
		exchangesignxp = this.plugin.getConfig().getString("language.exchangesignxp");
		depositsign = this.plugin.getConfig().getString("language.depositsign");
		debitsign = this.plugin.getConfig().getString("language.debitsign");
		balancesign = this.plugin.getConfig().getString("language.balancesign");
		depositsignxp = this.plugin.getConfig().getString("language.depositsignxp");
		debitsignxp = this.plugin.getConfig().getString("language.debitsignxp");
		balancesignxp = this.plugin.getConfig().getString("language.balancesignxp");
		make = (color + prefix + this.plugin.getConfig().getString("language.make"));
		destroy = (color + prefix + this.plugin.getConfig().getString("language.destroy"));
		disallow = (color + prefix + this.plugin.getConfig().getString("language.disallow"));
		errorcreate = (color + prefix + this.plugin.getConfig().getString("language.errorcreate"));
		lowmoney1 = (color + prefix + this.plugin.getConfig().getString("language.lowmoney1"));
		lowmoney2 = (color + prefix + this.plugin.getConfig().getString("language.lowmoney2"));
		lowmoney3 = (color + prefix + this.plugin.getConfig().getString("language.lowmoney3"));
		success1 = (color + prefix + this.plugin.getConfig().getString("language.success1"));
		success2 = (color + prefix + this.plugin.getConfig().getString("language.success2"));
		success3 = (color + prefix + this.plugin.getConfig().getString("language.success3"));
		balance = (color + prefix + this.plugin.getConfig().getString("language.balance"));
		balanceother = (color + prefix + this.plugin.getConfig().getString("language.balanceother"));
		balancexpother = (color + prefix + this.plugin.getConfig().getString("language.balancexpother"));
		lowmoney1xp = (color + prefix + this.plugin.getConfig().getString("language.lowmoney1xp"));
		lowmoney2xp = (color + prefix + this.plugin.getConfig().getString("language.lowmoney2xp"));
		lowmoney3xp = (color + prefix + this.plugin.getConfig().getString("language.lowmoney3xp"));
		success1xp = (color + prefix + this.plugin.getConfig().getString("language.success1xp"));
		success2xp = (color + prefix + this.plugin.getConfig().getString("language.success2xp"));
		success3xp = (color + prefix + this.plugin.getConfig().getString("language.success3xp"));
		success4 = (color + prefix + this.plugin.getConfig().getString("language.success4"));
		success4xp = (color + prefix + this.plugin.getConfig().getString("language.success4xp"));
		balancexp = (color + prefix + this.plugin.getConfig().getString("language.balancexp"));
		interestmsg = (color + prefix + this.plugin.getConfig().getString("language.interestmsg"));
		interestxpmsg = (color + prefix + this.plugin.getConfig().getString("language.interestxpmsg"));
		amountadded = (color + prefix + this.plugin.getConfig().getString("language.amountadded"));
		comhelp = (this.plugin.getConfig().getString("command.user.help"));
		combalance = (this.plugin.getConfig().getString("command.user.balance"));
		combalancexp = (this.plugin.getConfig().getString("command.user.balancexp"));
		comdeposit = (this.plugin.getConfig().getString("command.user.deposit"));
		comdebit = (this.plugin.getConfig().getString("command.user.debit"));
		comdepositxp = (this.plugin.getConfig().getString("command.user.depositxp"));
		comdebitxp = (this.plugin.getConfig().getString("command.user.debitxp"));
		comtransfer = (this.plugin.getConfig().getString("command.user.transfer"));
		comtransferxp = (this.plugin.getConfig().getString("command.user.transferxp"));
		comexchange = (this.plugin.getConfig().getString("command.user.exchange"));
		comexchangexp = (this.plugin.getConfig().getString("command.user.exchangexp"));
		comadmhelp = (this.plugin.getConfig().getString("command.admin.help"));
		comadmset = (this.plugin.getConfig().getString("command.admin.set"));
		comadmsetxp = (this.plugin.getConfig().getString("command.admin.setxp"));
		comadmgrant = (this.plugin.getConfig().getString("command.admin.grant"));
		comadmgrantxp = (this.plugin.getConfig().getString("command.admin.grantxp"));
		comadmclear = (this.plugin.getConfig().getString("command.admin.clear"));
		comadmclearxp = (this.plugin.getConfig().getString("command.admin.clearxp"));
		limitmsg = (color + prefix + this.plugin.getConfig().getString("language.limitmsg"));
		limitmsgxp = (color + prefix + this.plugin.getConfig().getString("language.limitmsgxp"));
		interestlimitmsg = (color + prefix + this.plugin.getConfig().getString("language.interestlimitmsg"));
		interestlimitmsgxp = (color + prefix + this.plugin.getConfig().getString("language.interestlimitmsgxp"));
		nolongerloan = (color + prefix + this.plugin.getConfig().getString("language.nolongerloan"));
		nowinloan = (color + prefix + this.plugin.getConfig().getString("language.nowinloan"));
		nolongerloanxp = (color + prefix + this.plugin.getConfig().getString("language.nolongerloanxp"));
		nowinloanxp = (color + prefix + this.plugin.getConfig().getString("language.nowinloanxp"));

		// MySQL settings
		String dbType = plugin.getConfig().getString("database.type");
		if (dbType.equalsIgnoreCase("mysql")) {
			configHandler.mysql = true;
			String url = plugin.getConfig().getString("database.url");
			String user = plugin.getConfig().getString("database.user");
			String password = plugin.getConfig().getString("database.password");
			String dbPrefix = plugin.getConfig().getString("database.prefix");
			if (plugin.isEnabled()) {
				configHandler.database = new BcMysql(plugin, url, user, password, dbPrefix);
			}
		} else {
			plugin.getLogger().info("File storage selected");
		}

		return true;
	}

	public boolean defaultConfig() {
		FileConfiguration config;
		try {
			plugin.reloadConfig();
			config = plugin.getConfig();
			File configfile = new File("plugins" + System.getProperty("file.separator") + "Bankcraft");
			configfile.mkdirs();
			//CONFIGDEFAULTS

			// Database choice and config for mySql
			if (!config.contains("database.type")) {
				config.addDefault("database.type", "file");
			}
			if (!config.contains("database.user")) {
				config.addDefault("database.user", "root");
			}
			if (!config.contains("database.password")) {
				config.addDefault("database.password", "******");
			}
			if (!config.contains("database.url")) {
				config.addDefault("database.url", "jdbc:mysql://localhost:3306/minecraft");
			}
			if (!config.contains("database.prefix")) {
				config.addDefault("database.prefix", "bc_");
			}
			config.options().copyDefaults(true);
			config.options().header("Default config for BankCraft \r\n - Types of database : file or mysql \r\n");
			config.options().copyHeader(true);
			//
			if (!config.contains("general.log")) {
				config.set("general.log", false);
			}
			if (!config.contains("general.interest")) {
				config.set("general.interest", 0.005);
			}
			if (!config.contains("general.interestxp")) {
				config.set("general.interestxp", 0.005);
			}
			if (!config.contains("general.exchangerate")) {
				config.set("general.exchangerate", 0.05);
			}
			if (!config.contains("general.limit")) {
				config.set("general.limit", -1);
			}
			if (!config.contains("general.limitxp")) {
				config.set("general.limitxp", -1);
			}
			if (!config.contains("general.timer")) {
				config.set("general.timer", 120);
			}
			if (!config.contains("general.broadcast")) {
				config.set("general.broadcast", true);
			}
			if (!config.contains("general.broadcastxp")) {
				config.set("general.broadcastxp", true);
			}
			if (!config.contains("general.justonlinemoney")) {
				config.set("general.justonlinemoney", false);
			}
			if (!config.contains("general.justonlinexp")) {
				config.set("general.justonlinexp", false);
			}
			if (!config.contains("general.loan.interest")) {
				config.set("general.loan.interest", 0.005);
			}
			if (!config.contains("general.loan.interestxp")) {
				config.set("general.loan.interestxp", 0.005);
			}
			if (!config.contains("general.loan.group")) {
				config.set("general.loan.group", "loaner");
			}
			if (!config.contains("general.loan.groupxp")) {
				config.set("general.loan.groupxp", "xploaner");
			}
			if (!config.contains("general.loan.damage")) {
				config.set("general.loan.damage", 5);
			}
			if (!config.contains("general.loan.maxloan")) {
				config.set("general.loan.maxloan", 20000);
			}
			if (!config.contains("general.loan.maxloanxp")) {
				config.set("general.loan.maxloanxp", 20000);
			}
			if (!config.contains("language.color")) {
				config.set("language.color", 11);
			}
			if (!config.contains("language.prefix")) {
				config.set("language.prefix", "[BANK]");
			}
			if (!config.contains("language.signcolor")) {
				config.set("language.signcolor", 11);
			}
			if (!config.contains("language.balancesign")) {
				config.set("language.balancesign", "Balance");
			}
			if (!config.contains("language.depositsign")) {
				config.set("language.depositsign", "Deposit");
			}
			if (!config.contains("language.debitsign")) {
				config.set("language.debitsign", "Debit");
			}
			if (!config.contains("language.balancesignxp")) {
				config.set("language.balancesignxp", "XPBalance");
			}
			if (!config.contains("language.depositsignxp")) {
				config.set("language.depositsignxp", "XPDeposit");
			}
			if (!config.contains("language.debitsignxp")) {
				config.set("language.debitsignxp", "XPDebit");
			}
			if (!config.contains("language.exchangesign")) {
				config.set("language.exchangesign", "Exchange");
			}
			if (!config.contains("language.exchangesignxp")) {
				config.set("language.exchangesignxp", "XPExchange");
			}
			if (!config.contains("language.make")) {
				config.set("language.make", "Bank created!");
			}
			if (!config.contains("language.destroy")) {
				config.set("language.destroy", "Bank removed!");
			}
			if (!config.contains("language.disallow")) {
				config.set("language.disallow", "You are not allowed to do this!");
			}
			if (!config.contains("language.errorcreate")) {
				config.set("language.errorcreate", "Error creating bank!");
			}
			if (!config.contains("language.lowmoney1")) {
				config.set("language.lowmoney1", "Not enough money to do this!");
			}
			if (!config.contains("language.lowmoney2")) {
				config.set("language.lowmoney2", "Not enough money on your account!");
			}
			if (!config.contains("language.lowmoney3")) {
				config.set("language.lowmoney3", "Not enough money on the players account!");
			}
			if (!config.contains("language.success1")) {
				config.set("language.success1", "Money deposited!");
			}
			if (!config.contains("language.success2")) {
				config.set("language.success2", "Money debited!");
			}
			if (!config.contains("language.success3")) {
				config.set("language.success3", "Money transfered!");
			}
			if (!config.contains("language.success4")) {
				config.set("language.success4", "%amount Dollars exchanged! You now have %balancexp Experience banked!");
			}
			if (!config.contains("language.balance")) {
				config.set("language.balance", "Balance: %balance Dollar");
			}
			if (!config.contains("language.balanceother")) {
				config.set("language.balanceother", "%player's Balance: %balance Dollar");
			}
			if (!config.contains("language.balancexpother")) {
				config.set("language.balancexpother", "%player's XP: %balancexp XP");
			}
			if (!config.contains("language.lowmoney1xp")) {
				config.set("language.lowmoney1xp", "Not enough experience to do this!");
			}
			if (!config.contains("language.lowmoney2xp")) {
				config.set("language.lowmoney2xp", "Not enough experience on your account!");
			}
			if (!config.contains("language.lowmoney3xp")) {
				config.set("language.lowmoney3xp", "Not enough experience on the players account!");
			}
			if (!config.contains("language.success1xp")) {
				config.set("language.success1xp", "Experience deposited!");
			}
			if (!config.contains("language.success2xp")) {
				config.set("language.success2xp", "Experience debited!");
			}
			if (!config.contains("language.success3xp")) {
				config.set("language.success3xp", "Experience transfered!");
			}
			if (!config.contains("language.success4xp")) {
				config.set("language.success4xp", "%amount Experience exchanged! You now have %balance Dollars!");
			}
			if (!config.contains("language.balancexp")) {
				config.set("language.balancexp", "Banked experience: %balancexp XP");
			}
			if (!config.contains("language.interestmsg")) {
				config.set("language.interestmsg", "%amount money granted! You now have %balance!");
			}
			if (!config.contains("language.interestxpmsg")) {
				config.set("language.interestxpmsg", "%amountint XP granted! You now have %balancexp!");
			}
			if (!config.contains("language.amountadded")) {
				config.set("language.amountadded", "Your value was successfully added to the sign!");
			}
			if (!config.contains("language.nolongerloan")) {
				config.set("language.nolongerloan", "You paid your debts!");
			}
			if (!config.contains("language.nowinloan")) {
				config.set("language.nowinloan", "You now have debts!");
			}
			if (!config.contains("language.nolongerloanxp")) {
				config.set("language.nolongerloanxp", "You payed your XP-debts!");
			}
			if (!config.contains("language.nowinloanxp")) {
				config.set("language.nowinloanxp", "You now have XP-debts!");
			}
			if (!config.contains("command.user.help")) {
				config.set("command.user.help", "help");
			}
			if (!config.contains("command.user.balance")) {
				config.set("command.user.balance", "balance");
			}
			if (!config.contains("command.user.balancexp")) {
				config.set("command.user.balancexp", "balancexp");
			}
			if (!config.contains("command.user.deposit")) {
				config.set("command.user.deposit", "deposit");
			}
			if (!config.contains("command.user.debit")) {
				config.set("command.user.debit", "debit");
			}
			if (!config.contains("command.user.depositxp")) {
				config.set("command.user.depositxp", "depositxp");
			}
			if (!config.contains("command.user.debitxp")) {
				config.set("command.user.debitxp", "debitxp");
			}
			if (!config.contains("command.user.transfer")) {
				config.set("command.user.transfer", "transfer");
			}
			if (!config.contains("command.user.transferxp")) {
				config.set("command.user.transferxp", "transferxp");
			}
			if (!config.contains("command.user.exchange")) {
				config.set("command.user.exchange", "exchange");
			}
			if (!config.contains("command.user.exchangexp")) {
				config.set("command.user.exchangexp", "exchangexp");
			}
			if (!config.contains("command.admin.help")) {
				config.set("command.admin.help", "help");
			}
			if (!config.contains("command.admin.set")) {
				config.set("command.admin.set", "set");
			}
			if (!config.contains("command.admin.setxp")) {
				config.set("command.admin.setxp", "setxp");
			}
			if (!config.contains("command.admin.grant")) {
				config.set("command.admin.grant", "grant");
			}
			if (!config.contains("command.admin.grantxp")) {
				config.set("command.admin.grantxp", "grantxp");
			}
			if (!config.contains("command.admin.clear")) {
				config.set("command.admin.clear", "clear");
			}
			if (!config.contains("command.admin.clearxp")) {
				config.set("command.admin.clearxp", "clearxp");
			}
			if (!config.contains("language.limitmsg")) {
				config.set("language.limitmsg", "Your %amount Dollars could not be transfered! The limit is set at %limit");
			}
			if (!config.contains("language.limitmsgxp")) {
				config.set("language.limitmsgxp", "Your %amount XP could not be transfered! The limit is set at %limitxp!");
			}
			if (!config.contains("language.interestlimitmsg")) {
				config.set("language.interestlimitmsg", "Your interest of %amount Dollars was not granted! You reached the limit of %limit Dollars!");
			}
			if (!config.contains("language.interestlimitmsgxp")) {
				config.set("language.interestlimitmsgxp", "Your interest of %amount XP was not granted! You reached the limit of %limit XP!");
			}

			this.plugin.saveConfig();
		} catch (Exception e1) {

			e1.printStackTrace();

		}
		return false;

	}
}
