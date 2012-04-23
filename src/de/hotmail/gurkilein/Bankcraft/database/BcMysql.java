package de.hotmail.gurkilein.Bankcraft.database;

import de.hotmail.gurkilein.Bankcraft.Bankcraft;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author
 * HarmO
 */
public class BcMysql extends BcDatabase{
	
	private Bankcraft plugin;

	public BcMysql(Bankcraft plugin, String url, String user, String password, String prefix) {
		super(plugin, "com.mysql.jdbc.Driver", url, user, password, prefix);
		this.plugin = plugin;
		this.setUp();
	}

	private void setUp() {
		try {
			BcConnection conn = getConnection();
			if(conn != null) {
				Statement st = conn.createStatement();
			//	String database = "CREATE DATABASE IF NOT EXISTS " + configHandler.database.get + ";";
				String tableBanks = "CREATE TABLE IF NOT EXISTS `" + getPrefix() + "banks` (id int(10) AUTO_INCREMENT, x int(10) NOT NULL, y int(10) NOT NULL, z int(10) NOT NULL, world varchar(100) NOT NULL, type int(10) NOT NULL, amounts varchar(250) NOT NULL, PRIMARY KEY(id));";
				String tableAccounts = "CREATE TABLE IF NOT EXISTS `" + getPrefix() + "accounts` (id int(10) AUTO_INCREMENT, playername varchar(50) NOT NULL, amount varchar(50) NOT NULL, PRIMARY KEY(id));";
				String tableXpAccounts = "CREATE TABLE IF NOT EXISTS `" + getPrefix() + "xp_accounts` (id int(10) AUTO_INCREMENT, playername varchar(50) NOT NULL, amount varchar(50) NOT NULL, PRIMARY KEY(id));";
			//	st.executeUpdate(database);
				st.executeUpdate(tableBanks);
				st.executeUpdate(tableAccounts);
				st.executeUpdate(tableXpAccounts);
				conn.close();
				plugin.getLogger().info("MySQL connected !!!");
			}
			else {
				System.err.println("MySQL connexion error !!!");
				plugin.disablePlugin();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			plugin.disablePlugin();
		}
	}
	
}
