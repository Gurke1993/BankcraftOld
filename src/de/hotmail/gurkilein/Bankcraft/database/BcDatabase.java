package de.hotmail.gurkilein.Bankcraft.database;

import de.hotmail.gurkilein.Bankcraft.Bankcraft;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author
 * HarmO
 */
public abstract class BcDatabase {
	private Bankcraft plugin;
	private String prefix;
	private BcConnectionPool pool;
	
	public BcDatabase(Bankcraft plugin, String driver, String url, String user, String password, String prefix) {
		this.plugin = plugin;
		this.prefix = prefix;
		try {
			pool = new BcConnectionPool(driver, url, user, password);
		} catch (Exception e){
			e.printStackTrace();
			System.err.print("Can't connect to database !!!");
			this.plugin.disablePlugin();
		}
	}
	
	/********************************************/
	/*                   SQL REQUESTS HERE                          */
	/********************************************/
	
	public void setAccount(String player, Double amount) {
		String sql = "INSERT INTO `"  + prefix + "accounts` (`id`, `playername`, `amount`) VALUES (?, ?, ?);";
		try {
			BcConnection conn = getConnection();
			PreparedStatement prest = conn.prepareStatement(sql);
			prest.setString(1, null);
			prest.setString(2, player);
			prest.setString(3, amount.toString());
			prest.executeUpdate();
			prest.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			plugin.disablePlugin();
		}
	}
	
	public Boolean getAccount(String player) {
		String sql = "SELECT COUNT(`amount`) FROM `"  + prefix + "accounts` WHERE `playername`=\"" + player + "\";";
		boolean exist = false;
		try {
			BcConnection conn = getConnection();
			PreparedStatement prest = conn.prepareStatement(sql);
			ResultSet res = prest.executeQuery();
			while (res.next()) {
				exist = true;
			}
			prest.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			plugin.disablePlugin();
		}
		return exist;
	}
	
	public String getBalance(String player) {
		String sql = "SELECT * FROM `"  + prefix + "accounts` WHERE `playername`=\"" + player + "\";";
		String amount = "";
		try {
			BcConnection conn = getConnection();
			PreparedStatement prest = conn.prepareStatement(sql);
			ResultSet res = prest.executeQuery();
			while (res.next()) {
				plugin.getLogger().info(res.getString("amount"));
				amount = res.getString("amount");
			}
			prest.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			plugin.disablePlugin();
		}
		return amount;
	}
	
	/********************************************/
	
	protected String getPrefix(){
		return prefix;
	}
	
	protected BcConnection getConnection() throws SQLException{
		return pool.getConnection();
	}
	
	public void closeConnections(){
		pool.closeConnections();
	}
}
