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
	
	public Boolean getBank(int x, int y, int z, String world) {
		String sql = "SELECT COUNT(`type`) AS type FROM `"  + prefix + "banks` WHERE `x`=" + x + " AND `y`=" + y + " AND `z`=" + z + " AND `world`=\"" + world + "\";";
		boolean exist = false;
		try {
			BcConnection conn = getConnection();
			PreparedStatement prest = conn.prepareStatement(sql);
			ResultSet res = prest.executeQuery();
			while (res.next()) {
				if(!"0".equals(res.getString("type")))
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
	
	public Boolean getAccount(String player) {
		String sql = "SELECT COUNT(`amount`) AS amount FROM `"  + prefix + "accounts` WHERE `playername`=\"" + player + "\";";
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
	
	public Boolean getXpAccount(String player) {
		String sql = "SELECT COUNT(`amount`) AS amount FROM `"  + prefix + "xp_accounts` WHERE `playername`=\"" + player + "\";";
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
	
	public void setBank(int x, int y, int z, String world, int type, String amounts) {
		String sql = "INSERT INTO `"  + prefix + "banks` (`id`, `x`, `y`, `z`, `world`, `type`, `amounts`) VALUES (?, ?, ?, ?, ?, ?, ?);";
		try {
			BcConnection conn = getConnection();
			PreparedStatement prest = conn.prepareStatement(sql);
			prest.setString(1, null);
			prest.setInt(2, x);
			prest.setInt(3, y);
			prest.setInt(4, z);
			prest.setString(5, world);
			prest.setInt(6, type);
			prest.setString(7, amounts);
			prest.executeUpdate();
			prest.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			plugin.disablePlugin();
		}
	}
	
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
	
	public void setXpAccount(String player, String amount) {
		String sql = "INSERT INTO `"  + prefix + "xp_accounts` (`id`, `playername`, `amount`) VALUES (?, ?, ?);";
		try {
			BcConnection conn = getConnection();
			PreparedStatement prest = conn.prepareStatement(sql);
			prest.setString(1, null);
			prest.setString(2, player);
			prest.setString(3, amount);
			prest.executeUpdate();
			prest.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			plugin.disablePlugin();
		}
	}
	
	public String getBalance(String player) {
		String sql = "SELECT * FROM `"  + prefix + "accounts` WHERE `playername`=\"" + player + "\";";
		String amount = "";
		try {
			BcConnection conn = getConnection();
			PreparedStatement prest = conn.prepareStatement(sql);
			ResultSet res = prest.executeQuery();
			while (res.next()) {
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
	
	public String getXpBalance(String player) {
		String sql = "SELECT * FROM `"  + prefix + "xp_accounts` WHERE `playername`=\"" + player + "\";";
		String amount = "";
		try {
			BcConnection conn = getConnection();
			PreparedStatement prest = conn.prepareStatement(sql);
			ResultSet res = prest.executeQuery();
			while (res.next()) {
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
	
	public String getAmountsOfBank(int x, int y, int z, String world) {
		String sql = "SELECT * FROM `"  + prefix + "banks` WHERE `x`=" + x + " AND `y`=" + y + " AND `z`=" + z + " AND `world`=\"" + world + "\";";
		String amounts = "";
		try {
			BcConnection conn = getConnection();
			PreparedStatement prest = conn.prepareStatement(sql);
			ResultSet res = prest.executeQuery();
			while (res.next()) {
				amounts = res.getString("amounts");
			}
			prest.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			plugin.disablePlugin();
		}
		return amounts;
	}
	
	public Integer getTypeOfBank(int x, int y, int z, String world) {
		String sql = "SELECT type FROM `"  + prefix + "banks` WHERE `x`=" + x + " AND `y`=" + y + " AND `z`=" + z + " AND `world`=\"" + world + "\";";
		Integer type = null;
		try {
			BcConnection conn = getConnection();
			PreparedStatement prest = conn.prepareStatement(sql);
			ResultSet res = prest.executeQuery();
			while (res.next()) {
				type = res.getInt("type");
			}
			prest.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			plugin.disablePlugin();
		}
		return type;
	}
	
	public void deposit(String amount, String player) {
		String sql = "UPDATE `" + prefix + "accounts` SET `amount`=\"" + amount + "\" WHERE `playername`=\"" + player + "\";";
		try {
			BcConnection conn = getConnection();
			PreparedStatement prest = conn.prepareStatement(sql);
			prest.executeUpdate();
			prest.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			plugin.disablePlugin();
		}
	}
	
	public void depositXp(String amount, String player) {
		String sql = "UPDATE `" + prefix + "xp_accounts` SET `amount`=\"" + amount + "\" WHERE `playername`=\"" + player + "\";";
		try {
			BcConnection conn = getConnection();
			PreparedStatement prest = conn.prepareStatement(sql);
			prest.executeUpdate();
			prest.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			plugin.disablePlugin();
		}
	}
	
	public void deleteBank(int x, int y, int z, String world) {
		String sql = "DELETE FROM `" + prefix + "banks` WHERE `x`=" + x + " AND `y`=" + y + " AND `z`=" + z + " AND `world`=\"" + world + "\";";
		try {
			BcConnection conn = getConnection();
			PreparedStatement prest = conn.prepareStatement(sql);
			prest.executeUpdate();
			prest.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			plugin.disablePlugin();
		}
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
