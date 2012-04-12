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
				String tableAccounts = "CREATE TABLE IF NOT EXISTS `" + getPrefix() + "accounts` (id int(10) AUTO_INCREMENT, playername varchar(50) NOT NULL, amount double NOT NULL, PRIMARY KEY(id));";
				st.executeUpdate(tableAccounts);
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
